package io.ll.warden.accounts;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.ll.warden.Warden;
import io.ll.warden.commands.AuthAction;
import io.ll.warden.storage.Database;
import io.ll.warden.utils.PasswordUtils;

/**
 * Author: LordLambda
 * Date: 3/17/2015
 * Project: Warden
 * Usage: The account manager
 */
public class WardenAccountManager implements CommandExecutor, Listener {

  private static WardenAccountManager manager;
  private List<WardenAccount> wardenAccounts;
  private List<UUID> uuids;
  private boolean shouldHaveAccounts;
  private boolean blockingTor;
  private Database db;
  private Thread dbRefreshThread = new Thread() {
    @Override
    public void run() {
      try {
        db.openConnection();
        if (db.checkConnection()) {
          //Create table if it doesn't exist.
          db.querySQL("CREATE TABLE IF NOT EXISTS Warden"
                      + "(UUID varchar(36) NOT NULL,"
                      + "RANK tinyint(1) NOT NULL,"
                      + "VERIFICATIONKEY varchar(1600) NOT NULL,"
                      + "PRIMARY KEY (UUID)"
                      + ")");
          ResultSet rs = db.querySQL("SELECT UUID FROM Warden");
          while (rs.next()) {
            String s = rs.getString(1);
            uuids.add(UUID.fromString(s));
          }
        }
        db.closeConnection();
      } catch (Exception e1) {
        Warden.get().log(String.format("Refreshing DB Error [ %s ]",
                                       e1.getLocalizedMessage()));
      }
    }
  };

  /**
   * Initalize everything we need to.
   */
  protected WardenAccountManager() {
    shouldHaveAccounts = false;
    wardenAccounts = new ArrayList<WardenAccount>();
    uuids = new ArrayList<UUID>();
    db = null;
  }

  /**
   * Get the current singleton instance
   *
   * @return The current singleton instance.
   */
  public static WardenAccountManager get() {
    if (manager == null) {
      synchronized (WardenAccountManager.class) {
        if (manager == null) {
          manager = new WardenAccountManager();
        }
      }
    }
    return manager;
  }

  /**
   * Check is a UUID is in list
   *
   * @param uuid The UUID to check
   * @return If it's in the list.
   */
  private boolean inUUIDList(UUID uuid) {
    for (UUID u : uuids) {
      if (u.equals(uuid)) {
        return true;
      }
    }
    return false;
  }

  public AuthAction.AuthLevel getAuthLevel(UUID uuid) {
    if (playerHasWardenAccount(uuid)) {
      for (WardenAccount wa : wardenAccounts) {
        if (wa.getPlayerUUID().equals(uuid)) {
          return wa.getLevel();
        }
      }
    }
    return null;
  }

  /**
   * Set the configuration file for data
   *
   * @param cF The configuration
   */
  public void setConfig(FileConfiguration cF) {
    shouldHaveAccounts = cF.getBoolean("UseWardenAccounts");
    blockingTor = cF.getBoolean("BlockTor");
  }

  /**
   * @return If warden accounts are loged.
   */
  public boolean hasWardenAccounts() {
    return shouldHaveAccounts;
  }

  /**
   * If the player has a warden account (and is logged in).
   *
   * @param uuid The UUID
   * @return IF the passed UUID has a warden account.
   */
  public boolean playerHasWardenAccount(UUID uuid) {
    for (WardenAccount wa : wardenAccounts) {
      if (wa.getPlayerUUID().equals(uuid)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Set the databse
   *
   * @param db Heres the database
   */
  public void setDB(Database db) {
    this.db = db;
    if (dbRefreshThread.isAlive()) {
      dbRefreshThread.stop();
    }
    dbRefreshThread.run();
  }

  /**
   * Process a command
   *
   * @param sender  The sender of the command
   * @param command The command being sent
   * @param name    The name duh
   * @param args    The arguments passed
   * @return If the command executed correctly.
   */
  @Override
  public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
    if (command.getName().equals("registerWarden") ||
        command.getName().equals("rW")) {
      if (sender instanceof Player) {
        Player p = (Player) sender;
        if (playerHasWardenAccount(p.getUniqueId())) {
          p.sendMessage("This command can't be executed from a player.");
        }
        return true;
      }
      if (args.length != 3) {
        Warden.get().log("Incorrect amount of arguments!");
        Warden.get().log("Proper Usage: /rW <username> <pass> <level>");
      }
      String username = args[0];
      String password = PasswordUtils.hash(args[1]);
      AuthAction.AuthLevel level = AuthAction.AuthLevel.valueOf(args[3]);
      if (level == null) {
        level = AuthAction.AuthLevel.USER;
      }
      UUID userID = Bukkit.getOfflinePlayer(username).getUniqueId();
      if (playerHasWardenAccount(userID)) {
        Warden.get().log("User Already has an account!");
        return true;
      }
      try {
        db.querySQL(String.format("INSERT INTO Warden "
                                  + "(UUID, RANK, VERIFICATIONKEY)"
                                  + "VALUES ('%s', %d, '%s')",
                                  userID.toString(), level.ordinal(),
                                  password));
        if (Bukkit.getPlayer(userID).isOnline()) {
          WardenAccount wa = new WardenAccount(userID, level, password.getBytes());
          wardenAccounts.add(wa);
        }
      } catch (Exception e1) {
        Warden.get().log("Failed to create account!");
        e1.printStackTrace();
      }
    } else if (command.getName().equals("loginWarden") ||
               command.getName().equals("login")) {
      if (sender instanceof Player) {
        Player p = (Player) sender;
        if (inUUIDList(p.getUniqueId())) {
          if (args.length == 1) {
            p.sendMessage("Logging in...");
            try {
              ResultSet rs = db.querySQL(String.format("SELECT VERIFICATIONKEY,"
                                                       + " RANK "
                                                       + "FROM Warden WHERE"
                                                       + " UUID='%s'",
                                                       p.getUniqueId().toString()));
              String verificationKey = rs.getString(1);
              int rankOrd = rs.getInt(2);
              if (verificationKey.equals(PasswordUtils.hash(args[0]))) {
                WardenAccount wa = new WardenAccount(p.getUniqueId(),
                                                     AuthAction.AuthLevel.getByOrdinal(rankOrd),
                                                     verificationKey.getBytes());
                wardenAccounts.add(wa);
                p.sendMessage("You are now logged on!");
              }
            } catch (Exception e1) {
              Warden.get().log(String.format("Failed to login [ %s ]",
                                             e1.getLocalizedMessage()));
              e1.printStackTrace();
              p.sendMessage("Failed to login! Error!");
            }
          } else {
            p.sendMessage("Incorrect amount of args!");
          }
        }
      } else {
        Warden.get().log("Sorry this command can't be executed from console!");
      }
    } else if (command.getName().equals("promoteWarden") ||
               command.getName().equals("pW")) {
      if (sender instanceof Player) {
        Player p = (Player) sender;
        if (playerHasWardenAccount(p.getUniqueId())) {
          p.sendMessage("This can't be executed from a player account!");
        }
      }
      if (args.length != 2) {
        Warden.get().log("Incorrect amount of args!");
        Warden.get().log("Try: /pW <user> <rank>");
      }
      String userName = args[0];
      AuthAction.AuthLevel al = AuthAction.AuthLevel.valueOf(args[1]);
      if (al == null) {
        al = AuthAction.AuthLevel.USER;
      }
      UUID uuid = Bukkit.getPlayer(userName).getUniqueId();
      try {
        db.querySQL(String.format("UPDATE WARDEN"
                                  + " SET RANK=%d"
                                  + " WHERE UUID='%s'",
                                  al.ordinal(),
                                  uuid.toString()));
        //Check if he's logged in
        if (playerHasWardenAccount(uuid)) {
          //Yep he is.
          WardenAccount wa = getWardenAccount(uuid);
          wa.setLevel(al);
          Warden.get().log("Logged in player!");
        }
      } catch (Exception e1) {
        Warden.get().log("Failed to promote player!");
        e1.printStackTrace();
        return true;
      }
    }
    return true;
  }

  /**
   * Listen for a player login
   *
   * @param event The event we are listening too.
   */
  @EventHandler
  public void playerLoginEvent(PlayerLoginEvent event) {
    final Player p = event.getPlayer();
    final InetAddress ip = event.getAddress();
    if (blockingTor) {
      new Thread() {
        @Override
        public void run() {
          List<String> lines = new ArrayList<String>();
          File torList = new File("exitNodes.txt");
          String line;
          Player localP = p;
          InetAddress local = ip;
          if (!torList.exists()) {
            //Get it into a local instance.
            URL url;
            InputStream is;
            BufferedReader br;
            try {
              url = new URL("https://dan.me.ul/tornodes/");
              is = url.openStream();
              br = new BufferedReader(new InputStreamReader(is));
              while ((line = br.readLine()) != null) {
                lines.add(line);
              }
              torList.createNewFile();
              BufferedWriter bw = new BufferedWriter(new FileWriter(torList));
              for (String s : lines) {
                bw.write(s);
              }
              bw.close();
              br.close();
              is.close();
            } catch (IOException e1) {
              Warden.get().log("Failed to grab tor list!");
              e1.printStackTrace();
            }
          } else {
            try {
              BufferedReader br = new BufferedReader(new FileReader(torList));
              while ((line = br.readLine()) != null) {
                lines.add(line);
              }
              br.close();
            } catch (Exception e1) {
              //Won't happen.
            }
          }
          for (String s : lines) {
            if (s.equals(local.getHostAddress())) {
              localP.kickPlayer("TOR is not allowed!");
              break;
            }
          }
        }
      }.run();
    }
    try {
      ResultSet rs = db.querySQL(String.format("SELECT * FROM WARDEN "
                                               + "WHERE UUID=\"%s\"", p.getUniqueId()));
      if (rs.next()) {
        p.sendMessage("You have a Warden account. Please Log-In.");
      }
    } catch (Exception e1) {
      e1.printStackTrace();
    }
  }

  /**
   * Get a warden account instance based off a uuid
   *
   * @param uuid The UUID to check
   * @return Null if the player doesn't exist, or the Warden account insstance.
   */
  public WardenAccount getWardenAccount(UUID uuid) {
    if (playerHasWardenAccount(uuid)) {
      for (WardenAccount wa : wardenAccounts) {
        if (wa.getPlayerUUID().equals(uuid)) {
          return wa;
        }
      }
    }
    return null;
  }

  /**
   * Happens when a player logs out
   *
   * @param event The PlayerQuitEvent
   */
  @EventHandler
  public void onLogout(PlayerQuitEvent event) {
    Player p = event.getPlayer();
    if (playerHasWardenAccount(p.getUniqueId())) {
      WardenAccount wa = getWardenAccount(p.getUniqueId());
      wardenAccounts.remove(wa);
    }
  }
}
