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

  protected WardenAccountManager() {
    shouldHaveAccounts = false;
    wardenAccounts = new ArrayList<WardenAccount>();
    db = null;
  }

  public void setConfig(FileConfiguration cF) {
    shouldHaveAccounts = cF.getBoolean("UseWardenAccounts");
    blockingTor = cF.getBoolean("BlockTor");
  }

  public boolean hasWardenAccounts() {
    return shouldHaveAccounts;
  }

  public boolean playerHasWardenAccount(UUID uuid) {
    for(WardenAccount wa : wardenAccounts) {
      if(wa.getPlayerUUID().equals(uuid)) {
        return true;
      }
    }
    return false;
  }

  public void setDB(Database db) {
    this.db = db;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {

    return true;
  }

  private Thread dbRefreshThread = new Thread() {
    @Override
    public void run() {

    }
  };

  @EventHandler
  public void playerLoginEvent(PlayerLoginEvent event) {
    final Player p = event.getPlayer();
    final InetAddress ip = event.getAddress();
    if(blockingTor) {
      new Thread() {
        @Override
        public void run() {
          List<String> lines = new ArrayList<String>();
          File torList = new File("exitNodes.txt");
          String line;
          Player localP = p;
          InetAddress local = ip;
          if(!torList.exists()) {
            //Get it into a local instance.
            URL url;
            InputStream is;
            BufferedReader br;
            try {
              url = new URL("https://dan.me.ul/tornodes/");
              is = url.openStream();
              br = new BufferedReader(new InputStreamReader(is));
              while((line = br.readLine()) != null) {
                lines.add(line);
              }
              br.close();
              is.close();
            } catch (IOException e1) {
              Warden.get().log("Failed to grab tor list!");
              e1.printStackTrace();
            }
          }else {
            try {
              BufferedReader br = new BufferedReader(new FileReader(torList));
              while((line = br.readLine()) != null) {
                lines.add(line);
              }
              br.close();
            }catch(Exception e1) {
              //Won't happen.
            }
          }
          for(String s : lines) {
            if(s.equals(local.getHostAddress())) {
              localP.kickPlayer("TOR is not allowed!");
              break;
            }
          }
        }
      }.run();
    }
    try {
      ResultSet rs = db.querySQL(String.format("SELECT * WHERE UUID=\"%s\"", p.getUniqueId()));
      if(rs.next()) {
        p.sendMessage("You have a Warden account. Please Log-In.");
      }
    }catch(Exception e1) {
      e1.printStackTrace();
    }
  }

  public static WardenAccountManager get() {
    if(manager == null) {
      synchronized (WardenAccountManager.class) {
        if(manager == null) {
          manager = new WardenAccountManager();
        }
      }
    }
    return manager;
  }
}
