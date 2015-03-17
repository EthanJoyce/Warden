package io.ll.warden;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

import io.ll.warden.commands.AuthAction;
import io.ll.warden.storage.Database;
import io.ll.warden.storage.MySQL;
import io.ll.warden.storage.SQLite;

/**
 * Author: LordLambda
 * Date: 3/16/2015
 * Project: Warden
 * Usage: Main Warden Class
 */
public class Warden extends JavaPlugin {

  Logger l;
  private ProtocolManager protocolManager;
  private static Warden instance;
  private Database db;

  /**
   * Called on plugin enable.
   */
  @Override
  public void onEnable() {
    instance = this;
    l = getLogger();
    saveDefaultConfig();
    log("Starting Warden...");

    log("Getting Protocol Manager....");
    protocolManager = ProtocolLibrary.getProtocolManager();
    log("Got.");

    log("Registering commands...");
    getCommand("verify").setExecutor(AuthAction.get());
    log("Done.");

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
      }catch(Exception e1) {
        log("Failed to connect to DB! This is fatal!");
        e1.printStackTrace();
        return;
      }
    }else {
      db = new SQLite(this, getConfig().getString("DBName"));
      try {
        db.openConnection();
        log(String.format("DB connection test %b", db.checkConnection()));
        db.closeConnection();
      }catch(Exception e1) {
        log("Failed to connect to DB! This is fatal!");
        e1.printStackTrace();
        return;
      }
    }
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
    l.log(logLevel, String.format("[Warden AC] %s", toLog));
  }

  /**
   * Gets the current warden instance if there is any.
   *
   * @return The current warden instance. (Could be null).
   */
  public static Warden get() {
    return instance;
  }

  /**
   * Gets my custom version of a DB
   *
   * @return The Database
   */
  public Database getDB() {
    return db;
  }
}
