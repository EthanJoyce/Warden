package io.ll.warden.heuristics;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;

import io.ll.warden.Warden;
import io.ll.warden.commands.AuthAction;
import io.ll.warden.configuration.BanConfig;
import io.ll.warden.events.BanEvent;
import io.ll.warden.events.CheckFailedEvent;
import io.ll.warden.storage.Database;
import io.ll.warden.utils.UUIDFetcher;
import io.ll.warden.utils.ViolationLevelWithPoints;

/**
 * Creator: LordLambda
 * Date: 3/25/2015
 * Project: Warden
 * Usage: A simple ban manager.
 *
 * This also handles things like when people fail checks what level of "Hacker" they are at.
 */
public class BanManager implements Listener, CommandExecutor, AuthAction.AuthCallback {

  private static BanManager instance;
  private Database db;
  private ConcurrentHashMap<UUID, ViolationLevelWithPoints> hackerMap;
  private FileConfiguration config;
  private ConcurrentHashMap<UUID, String[]> waitingForVerification;
  private ConcurrentHashMap<UUID, BanConfig> banConfigs;
  private List<UUID> bannedByWarden;
  private Thread constantBanThread;
  private Thread constanstCheckBanThread;
  private int[] pointsNeededPerLevel;
  private ViolationLevel banAt;
  private Lock dbLock;

  protected BanManager() {
    dbLock = new ReentrantLock();
    hackerMap = new ConcurrentHashMap<UUID, ViolationLevelWithPoints>();
    banConfigs = new ConcurrentHashMap<UUID, BanConfig>();
    waitingForVerification = new ConcurrentHashMap<UUID, String[]>();
    bannedByWarden = new ArrayList<UUID>();
    constantBanThread = new Thread() {
      @Override
      public void run() {
        for (UUID u : bannedByWarden) {
          Player p = Bukkit.getServer().getPlayer(u);
          if (!p.isBanned()) {
            p.setBanned(true);
          }
        }
      }
    };
    constanstCheckBanThread = new Thread() {
      @Override
      public void run() {
        for (UUID u : hackerMap.keySet()) {
          ViolationLevelWithPoints vlwp = hackerMap.get(u);
          int points = vlwp.getPoints();
          ViolationLevel vl = pointsToLevel(points);
          if (vl != vlwp.getLevel()) {
            dbLock.lock();
            try {
              db.querySQL(String.format("UPDATE WardenBans SET HLEVEL=%d WHERE"
                                        + "UUID='%s'", vl.ordinal(), u));
            } catch (Exception e1) {
              Warden.get().log("Failed to update players level on DB!");
              e1.printStackTrace();
            }finally {
              dbLock.unlock();
            }
            hackerMap.put(u, new ViolationLevelWithPoints(vl, points));
          }
          //Update if neccesarry
          vlwp = hackerMap.get(u);
          if (vlwp.getLevel().ordinal() >= banAt.ordinal()) {
            Bukkit.getPlayer(u).setBanned(true);
            dbLock.lock();
            try {
              db.querySQL(String.format("UPDATE WardenBans SET ISBANNEDBYWARDEN=1 WHERE"
                                        + "UUID='%s'", u));
            } catch (Exception e1) {
              Warden.get().log("Failed to update player banned on DB!");
              e1.printStackTrace();
            }finally {
              dbLock.unlock();
            }
            bannedByWarden.add(u);
            Bukkit.getPluginManager().callEvent(new BanEvent(
              u, points
            ));
          }
        }
      }
    };
  }

  public void setup(Warden w, Database db) {
    config = w.getConfig();
    pointsNeededPerLevel = new int[]{
        0, config.getInt("SMALLTIME"), config.getInt("MID"), config.getInt("HIGH"),
        config.getInt("HIGHEST")
    };
    banAt = ViolationLevel.valueOf(config.getString("BANAT"));
    if (banAt == null) {
      banAt = ViolationLevel.HIGH;
    }
    this.db = db;
    dbLock.lock();
    try {
      this.db.querySQL("CREATE TABLE IF NOT EXISTS WardenBans"
                       + "(UUID varchar(36) NOT NULL,"
                       + "HLEVEL tinyint(1) NOT NULL,"
                       + "HPOINTS int(4) NOT NULL,"
                       + "ISBANEEDBYWARDEN tinyint(1) NOT NULL,"
                       + "PRIMARY KEY (UUID)"
                       + ")");
      ResultSet rs = this.db.querySQL("SELECT UUID From WardenBans");
      while (rs.next()) {
        UUID uuid = UUID.fromString(rs.getString("UUID"));
        int points = rs.getInt("HPOINTS");
      }
    } catch (Exception e1) {
      w.log(Level.SEVERE, "Failed to connect to with the DB table for BanManagement! This is fatal!"
                          + " So therefore warden will now shut everything down!");
      e1.printStackTrace();
      System.exit(-980);
    }finally {
      dbLock.unlock();
    }
    File banDir = new File(w.getDataFolder(), "Warden/Bans");
    if(!banDir.exists()) {
      banDir.mkdir();
    }else {
      for(File f : banDir.listFiles()) {
        try {
          BanConfig bc = new BanConfig(f.getName(), true);
          UUID uuid = UUID.fromString(bc.getFullName().substring(0, bc.getFullName()
              .lastIndexOf('.')));
          banConfigs.put(uuid, bc);
        }catch(Exception e1) {
          w.log(String.format("Failed to load banconfig: [ %s ]", f.getAbsolutePath()));
          e1.printStackTrace();
        }
      }
    }
    Bukkit.getPluginManager().registerEvents(this, w);
    constanstCheckBanThread.start();
    constantBanThread.start();
  }

  public static BanManager get() {
    if (instance == null) {
      synchronized (BanManager.class) {
        if (instance == null) {
          instance = new BanManager();
        }
      }
    }
    return instance;
  }

  @EventHandler
  public void onCheckFail(final CheckFailedEvent event) {
    //Execute SQL
    new Thread() {
      @Override
      public void run() {
        UUID u = event.getPlayer();
        ViolationLevelWithPoints vlwp = hackerMap.get(u);
        hackerMap.put(u, new ViolationLevelWithPoints(vlwp.getLevel(), vlwp.getPoints() +
                                                                       (int) event.getDamage()));
        vlwp = hackerMap.get(u);
        dbLock.lock();
        try {
          db.querySQL(String.format("UPDATE WardenBans SET HPOINTS=%d WHERE"
                                    + "UUID='%s'", vlwp.getPoints(), u));
        } catch (Exception e1) {
          Warden.get().log("Failed to update player point record!");
          e1.printStackTrace();
        }finally {
          dbLock.unlock();
        }
      }
    }.run();
    BanConfig bc = banConfigs.get(event.getPlayer());
    bc.addCheckFailed(event.getCheckName(), event.getPlayer(), event.getDamage());
  }

  @EventHandler
  public void onBan(final BanEvent event) {
    //SQL was already executed. Now we need to save to file.
    BanConfig bc = banConfigs.get(event.getUUID());
    bc.addBan(event.getPoints());
  }

  @EventHandler
  public void playerLoginEvent(PlayerLoginEvent event) {
    if (!hackerMap.containsKey(event.getPlayer().getUniqueId())) {
      //First time login add to table
      dbLock.lock();
      try {
        db.querySQL(String.format("INSERT INTO WardenBans ("
                                  + "UUID, HLEVEL, HPOINTS, ISBANNEDBYWARDEN)"
                                  + "VALUES ('%s', 0, 0, 0)", event.getPlayer().getUniqueId()));
      } catch (Exception e1) {
        Warden.get().log("Failed to add player to ban table!");
      }finally {
        dbLock.unlock();
      }
      hackerMap.put(event.getPlayer().getUniqueId(), new ViolationLevelWithPoints(
          ViolationLevel.NONE, 0));
    }
  }

  private ViolationLevel pointsToLevel(int points) {
    if (points < pointsNeededPerLevel[1]) {
      return ViolationLevel.NONE;
    } else if (points >= pointsNeededPerLevel[1] && points < pointsNeededPerLevel[2]) {
      return ViolationLevel.SMALLTIME;
    } else if (points >= pointsNeededPerLevel[2] && points < pointsNeededPerLevel[3]) {
      return ViolationLevel.MID;
    } else if (points >= pointsNeededPerLevel[3] && points < pointsNeededPerLevel[4]) {
      return ViolationLevel.HIGH;
    } else if (points >= pointsNeededPerLevel[5]) {
      return ViolationLevel.BRUDIN;
    }
    return null;
  }

  public static enum ViolationLevel {
    NONE,
    SMALLTIME,
    MID,
    HIGH,
    BRUDIN; //Also known as HIGHEST

    public static ViolationLevel getByOrdinal(int ord) {
      for (ViolationLevel vl : values()) {
        if (vl.ordinal() == ord) {
          return vl;
        }
      }
      return null;
    }
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
    if(name.equalsIgnoreCase("queryBan") || name.equalsIgnoreCase("qB")) {
      if(!(sender instanceof Player)) {
        Warden.get().log("This commands can only be called by a player!");
      }
      Player theSender = (Player) sender;
      if(args.length != 1) {
        theSender.sendMessage(String.format("[Warden] Incorrect Args!"));
        return false;
      }
      String playerName = args[0];
      UUID u;
      try {
        u = UUIDFetcher.getUUIDOf(playerName);
      }catch(Exception e1) {
        theSender.sendMessage("[Warden] Failed to grab UUID of specified name!");
        e1.printStackTrace();
        return false;
      }
      BanConfig bc = banConfigs.get(u);
      //TODO: Split into length so it displays in chat, and send message to player
    }else if(name.equalsIgnoreCase("queryChecks") || name.equalsIgnoreCase("qC")) {
      if(!(sender instanceof Player)) {
        Warden.get().log("This commands can only be called by a player!");
      }
      Player theSender = (Player) sender;
      if(args.length != 1) {
        theSender.sendMessage(String.format("[Warden] Incorrect Args!"));
        return false;
      }
      String playerName = args[0];
      UUID u;
      try {
        u = UUIDFetcher.getUUIDOf(playerName);
      }catch(Exception e1) {
        theSender.sendMessage("[Warden] Failed to grab UUID of specified name!");
        e1.printStackTrace();
        return false;
      }
      BanConfig bc = banConfigs.get(u);
      //TODO: Split into length so it displays in chat, and send message to player
    }
    return true;
  }

  @Override
  public void onCallback(UUID u, AuthAction.AuthLevel level) {

  }

  @Override
  public String getAuthBackName() {
    return "BanManager";
  }
}
