package io.ll.warden.accounts;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
import io.ll.warden.storage.Database;

/**
 * Author: LordLambda
 * Date: 3/17/2015
 * Project: Warden
 * Usage: The account manager
 */
public class WardenAccountManager implements CommandExecutor, Listener {

  private static WardenAccountManager manager;
  private List<WardenAccount> wardenAccounts;
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
                      + ");");
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
   * If the player has a warden account
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
}
