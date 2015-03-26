package io.ll.warden.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.ll.warden.Warden;
import io.ll.warden.accounts.WardenAccount;
import io.ll.warden.accounts.WardenAccountManager;

/**
 * Creator: LordLambda
 * Date: 3/16/2015
 * Project: Warden
 * Usage: Authenticate action
 */
public class AuthAction implements CommandExecutor {

  private static AuthAction instance;
  private List<WardenAccount> toAuthBackTooAccounts;

  /**
   * Initalize the list. Thats all we need to do.
   */
  protected AuthAction() {
    toAuthBackTooAccounts = new ArrayList<WardenAccount>();
  }

  /**
   * A thread safe way to handle Singletons
   *
   * @return The Singleton instance.
   */
  public static AuthAction get() {
    if (instance == null) {
      synchronized (AuthAction.class) {
        if (instance == null) {
          instance = new AuthAction();
        }
      }
    }
    return instance;
  }

  /**
   * The method for processing a verification command.
   *
   * @param sender  The Sender of the command
   * @param command The command that was sent
   * @param name    Don't feel like I have to explain this.
   * @param args    The arguments split into an array.
   * @return Wether the command executed successfully.
   */
  @Override
  public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
    if (!command.getName().equalsIgnoreCase("verify")) {
      return false;
    }
    if (!(sender instanceof Player)) {
      Warden.get().log("Console can't execute This command.");
      return true;
    }
    if (!WardenAccountManager.get().hasWardenAccounts()) {
      return true;
    } else if (!WardenAccountManager.get()
        .playerHasWardenAccount(((Player) sender).getUniqueId())) {
      return true;
    }
    for (WardenAccount wa : toAuthBackTooAccounts) {
      Player p = (Player) sender;
      if (!(p.getUniqueId() == wa.getPlayerUUID())) {
        continue;
      }
      List<AuthCallback> toAuthBackToo = wa.getVerifications();
      if (toAuthBackToo.size() == 0) {
        sender.sendMessage("[Warden] You have nothing to verify!");
        return true;
      } else {
        for (AuthCallback ac : toAuthBackToo) {
          if (ac.getAuthBackName().equalsIgnoreCase(args[0])) {
            ac.onCallback( ((Player) sender).getUniqueId() , wa.getLevel());
            wa.remCallback(ac);
            if (wa.getVerifications().size() == 0) {
              toAuthBackTooAccounts.remove(wa);
            }
            return true;
          }
        }
        sender.sendMessage("[Warden] Couldn't find that command to verify?");
      }
    }
    return true;
  }

  /**
   * Register an action with an account that needs to be verified
   *
   * @param wa The Warden account that needs to verify
   * @param ac The auth callback.
   */
  public void registerAuthCallback(WardenAccount wa, AuthCallback ac) {
    wa.addCallback(ac);
    if (!toAuthBackTooAccounts.contains(wa)) {
      toAuthBackTooAccounts.add(wa);
    }
  }

  /**
   * The possible AuthLevels for a warden account.
   */
  public enum AuthLevel {
    NONE, //If the player failed authentication
    USER, //If the player is just a general user
    MODERATOR, //If the player is a moderator
    ADMIN, //If the player is an admin
    OWNER;

    public static AuthLevel getByOrdinal(int ord) {
      for (AuthLevel al : AuthLevel.values()) {
        if (al.ordinal() == ord) {
          return al;
        }
      }
      return null;
    }
  }

  /**
   * An interface for something that wants to get a callback.
   */
  public interface AuthCallback {

    public String getAuthBackName();

    public void onCallback(UUID u,AuthLevel level);
  }
}
