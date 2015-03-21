package io.ll.warden;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

import io.ll.warden.accounts.WardenAccountManager;
import io.ll.warden.check.CheckManager;
import io.ll.warden.commands.AuthAction;
import io.ll.warden.storage.Database;
import io.ll.warden.storage.MySQL;
import io.ll.warden.storage.SQLite;
import io.ll.warden.utils.MovementHelper;

/**
 * Author: LordLambda
 * Date: 3/16/2015
 * Project: Warden
 * Usage: Main Warden Class
 */
public class Warden extends JavaPlugin {

  private static Warden instance;
  Logger l;
  private ProtocolManager protocolManager;
  private Database db;
  private boolean usingLongLog;

  /**
   * Gets the current warden instance if there is any.
   *
   * @return The current warden instance. (Could be null).
   */
  public static Warden get() {
    return instance;
  }

  /**
   * Called on plugin enable.
   */
  @Override
  public void onEnable() {
    instance = this;
    l = getLogger();
    saveDefaultConfig();
    usingLongLog = getConfig().getBoolean("LongLogMessage");
    log("Starting Warden...");

    log("Getting Protocol Manager....");
    protocolManager = ProtocolLibrary.getProtocolManager();
    log("Got.");

    log("Setting Up the database");
    boolean useMySQL = getConfig().getString("DatabaseType").equalsIgnoreCase("MYSQL");
    log(Level.FINE, String.format("Using [ %s ] as storage.", useMySQL ? "MySQL" : "SQLite"));
    if (useMySQL) {
      db =
          new MySQL(this, getConfig().getString("DBHost"), getConfig().getString("DBPort"),
                    getConfig().getString("DBName"), getConfig().getString("DBUsername"),
                    getConfig().getString("DBPassword"));
      try {
        db.openConnection();
        log(String.format("DB connection test %b", db.checkConnection()));
        db.closeConnection();
      } catch (Exception e1) {
        log("Failed to connect to DB! This is fatal!");
        e1.printStackTrace();
        return;
      }
    } else {
      db = new SQLite(this, getConfig().getString("DBName"));
      try {
        db.openConnection();
        log(String.format("DB connection test %b", db.checkConnection()));
        db.closeConnection();
      } catch (Exception e1) {
        log("Failed to connect to DB! This is fatal!");
        e1.printStackTrace();
        return;
      }
    }
    log("Done.");

    log("Registering commands...");
    getCommand("verify").setExecutor(AuthAction.get());
    WardenAccountManager wam = WardenAccountManager.get();
    wam.setConfig(getConfig());
    wam.setDB(db);
    getCommand("registerWarden").setExecutor(wam);
    getCommand("rW").setExecutor(wam);
    getCommand("loginWarden").setExecutor(wam);
    getCommand("login").setExecutor(wam);
    getCommand("promoteWarden").setExecutor(wam);
    getCommand("pW").setExecutor(wam);
    log("Done.");

    log("Setting up CheckManager.");
    CheckManager.get();
    log("Done.");

    log("Setting up movement helper...");
    MovementHelper.get();
    MovementHelper.get().setProtocolManager(protocolManager);
    log("Done.");

    log("Registering Listeners...");
    CheckManager.get().registerListeners(this, getServer().getPluginManager());
    getServer().getPluginManager().registerEvents(wam, this);
    log("Done.");

    /**
     * Load a shutdown hook here.
     * Note this shutdown hook is loaded instead of onDisable for multiple reasons.
     * The first being it doesn't allow poison plugins to just call onDisable.
     * Besides Spigot will be nice, and just null our listeners for us. Soooo.
     * Literally it's just a way to shutdown without a poison plugin being able to call it.
     * An admin can come in, and past the code inside the run() method here in the onDisable()
     * and voila have it be disable-able again, but why would you want that?
     */
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        log("Disabling Warden...");
      }
    });
  }

  /**
   * Called on plugin disable. Not implemented because of reasons listed above.
   */
  @Override
  public void onDisable() {
    log("Someone tried to shut us down. Shame if I had enabled this ;)");
  }

  /**
   * Logs a string w/ Formatting at INFO level
   *
   * @param toLog The string to log.
   */
  public void log(String toLog) {
    log(Level.INFO, toLog);
  }

  /**
   * Logs a string w/ formatting.
   *
   * @param logLevel The log level to log at.
   * @param toLog    The string to log.
   */
  public void log(Level logLevel, String toLog) {
    l.log(logLevel, String.format("[%s] %s", usingLongLog ?
                                             "Warden AC" : "WAC", toLog));
  }

  /**
   * Gets my custom version of a DB
   *
   * @return The Database
   */
  public Database getDB() {
    return db;
  }

  /**
   * Gets the instance of the protocol manager.
   *
   * @return The protocol manager.
   */
  public ProtocolManager getProtocolManager() {
    return protocolManager;
  }

  public static void main(String[] args) {
    System.out.println("This can only be run as a spigot plugin!");
  }
}
