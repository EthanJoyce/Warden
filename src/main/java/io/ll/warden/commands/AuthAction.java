package io.ll.warden.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Creator: LordLambda
 * Date: 3/16/2015
 * Project: Warden
 * Usage: Authenticate action
 */
public class AuthAction implements CommandExecutor {

  private List<AuthCallback> toAuthBackToo;
  private static AuthAction instance;

  protected AuthAction() {
    toAuthBackToo = new ArrayList<AuthCallback>();
  }

  public static AuthAction get() {
    if(instance == null) {
      synchronized (AuthAction.class) {
        if(instance == null) {
          instance = new AuthAction();
        }
      }
    }
    return instance;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
    if(toAuthBackToo.size() == 0) {
      //TODO: Check if we want to hide commands from regular users.
      sender.sendMessage("[Warden] You have nothing to verify!");
      return true;
    }else {
      for(AuthCallback ac : toAuthBackToo) {
        if(ac.getAuthBackName().equalsIgnoreCase(args[0])) {
          //TODO: check what user is.
          ac.onCallback(AuthLevel.NONE);
          toAuthBackToo.remove(ac);
          return true;
        }
      }
      //TODO: Check if we want to hide commands from regular users.
      sender.sendMessage("[Warden] Couldn't find that command to verify?");
    }
    return true;
  }

  public void registerAuthCallback(AuthCallback ac) {
    toAuthBackToo.add(ac);
  }

  public interface AuthCallback {
    public String getAuthBackName();
    public void onCallback(AuthLevel level);
  }

  public enum AuthLevel {
    NONE, //If the player failed authentication
    USER, //If the player is just a general user
    MODERATOR, //If the player is a moderator
    ADMIN, //If the player is an admin
    OWNER
  }
}
