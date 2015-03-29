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

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import io.ll.warden.Warden;
import io.ll.warden.events.CheckFailedEvent;
import io.ll.warden.storage.Database;
import io.ll.warden.utils.ViolationLevelWithPoints;

/**
 * Creator: LordLambda
 * Date: 3/25/2015
 * Project: Warden
 * Usage: A simple ban manager.
 *
 * This also handles things like when people fail checks what level of "Hacker" they are at.
 */
public class BanManager implements Listener, CommandExecutor {

  //TODO: Create real mutex type things instead of this crappy boolean system.

  private static BanManager instance;
  private Database db;
  private HashMap<UUID, ViolationLevelWithPoints> hackerMap;
  private FileConfiguration config;
  private HashMap<UUID, String[]> waitingForVerification;
  private List<UUID> bannedByWarden;
  private Thread constantBanThread;
  private Thread constanstCheckBanThread;
  private int[] pointsNeededPerLevel;
  private ViolationLevel banAt;
  private boolean dbMutex;
  private boolean hackerMapMutex;

  protected BanManager() {
    dbMutex = false;
    hackerMapMutex = false;
    hackerMap = new HashMap<UUID, ViolationLevelWithPoints>();
    waitingForVerification = new HashMap<UUID, String[]>();
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
            while (dbMutex) {
              //Wait for DB to open up
            }
            dbMutex = true;
            try {
              db.querySQL(String.format("UPDATE WardenBans SET HLEVEL=%d WHERE"
                                        + "UUID='%s'", vl.ordinal(), u));
            } catch (Exception e1) {
              Warden.get().log("Failed to update players level on DB!");
              e1.printStackTrace();
            }
            dbMutex = false;
            while (hackerMapMutex) {
              //Wait for it to open up
            }
            hackerMapMutex = true;
            hackerMap.put(u, new ViolationLevelWithPoints(vl, points));
            hackerMapMutex = false;
          }
          //Update if neccesarry
          vlwp = hackerMap.get(u);
          if (vlwp.getLevel().ordinal() >= banAt.ordinal()) {
            Bukkit.getPlayer(u).setBanned(true);
            while (dbMutex) {
              //Wait
            }
            dbMutex = true;
            try {
              db.querySQL(String.format("UPDATE WardenBans SET ISBANNEDBYWARDEN=1 WHERE"
                                        + "UUID='%s'", u));
            } catch (Exception e1) {
              Warden.get().log("Failed to update player banned on DB!");
              e1.printStackTrace();
            }
            dbMutex = false;
            bannedByWarden.add(u);
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
    while (dbMutex) {
      //Wait for it to open
    }
    dbMutex = true;
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
    }
    dbMutex = false;
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
        while (hackerMapMutex) {
          //Wait
        }
        hackerMapMutex = true;
        hackerMap.put(u, new ViolationLevelWithPoints(vlwp.getLevel(), vlwp.getPoints() +
                                                                       (int) event.getDamage()));
        hackerMapMutex = false;
        vlwp = hackerMap.get(u);
        while (dbMutex) {
          //Wait for it to open.
        }
        dbMutex = true;
        try {
          db.querySQL(String.format("UPDATE WardenBans SET HPOINTS=%d WHERE"
                                    + "UUID='%s'", vlwp.getPoints(), u));
        } catch (Exception e1) {
          Warden.get().log("Failed to update player point record!");
          e1.printStackTrace();
        }
        dbMutex = false;
      }
    }.run();
    //TODO: Log, and keep track for emails?
  }

  @EventHandler
  public void playerLoginEvent(PlayerLoginEvent event) {
    if (!hackerMap.containsKey(event.getPlayer().getUniqueId())) {
      //First time login add to table
      while (dbMutex) {
        //Wait for it to open
      }
      dbMutex = true;
      try {
        db.querySQL(String.format("INSERT INTO WardenBans ("
                                  + "UUID, HLEVEL, HPOINTS, ISBANNEDBYWARDEN)"
                                  + "VALUES ('%s', 0, 0, 0)", event.getPlayer().getUniqueId()));
      } catch (Exception e1) {
        Warden.get().log("Failed to add player to ban table!");
      }
      dbMutex = false;
      while (hackerMapMutex) {
        //Wait
      }
      hackerMapMutex = true;
      hackerMap.put(event.getPlayer().getUniqueId(), new ViolationLevelWithPoints(
          ViolationLevel.NONE, 0));
      hackerMapMutex = false;
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

    return true;
  }
}
