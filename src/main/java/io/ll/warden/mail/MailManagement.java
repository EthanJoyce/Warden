package io.ll.warden.mail;

/**
 * Author: LordLambda
 * Date: 3/22/2015
 * Project: Warden
 * Usage: E-Mail Stuff
 */
public class MailManagement {

  private static MailManagement instance;
  private static final String wardenURL;
  private static final String updateURL;

  static {
    wardenURL = "http://trinityapi.com/714.png";
    updateURL = "http://trinityapi.com/currentVers.txt";
  }

  protected MailManagement() {

  }

  public static MailManagement get() {
    if(instance == null) {
      synchronized(MailManagement.class) {
        if(instance == null) {
          instance = new MailManagement();
        }
      }
    }
    return instance;
  }
}
