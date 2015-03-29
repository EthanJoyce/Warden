package io.ll.warden.heuristics;

import io.ll.warden.utils.jodd.DateTimeStamp;
import io.ll.warden.utils.jodd.Iso8601JdtFormatter;
import io.ll.warden.utils.jodd.JdtFormat;

/**
 * Creator: LordLambda
 * Date: 3/28/2015
 * Project: Warden
 * Usage: A report for a check failed
 */
public class CheckFailedReport {

  private DateTimeStamp time;
  private String timeAsStr;
  private JdtFormat format;
  private String nameOfCheck;
  private String playerUUID;
  private float raiseValue;

  public CheckFailedReport(String time, String nameOfCheck, float raiseValue, String playerUUID) {
    format = new JdtFormat(new Iso8601JdtFormatter(), time);
    this.time = format.parse(time);
    timeAsStr = time;
    this.nameOfCheck = nameOfCheck;
    this.playerUUID = playerUUID;
    this.raiseValue = raiseValue;
  }

  public String getPlayerUUID() {
    return playerUUID;
  }

  public float getRaiseValue() {
    return raiseValue;
  }

  public String getNameOfCheck() {
    return nameOfCheck;
  }

  public String getPlayerString() {
    return String.format("Check Failed For [ %s ]."
                         + " Month: [ %d ], Day: [ %d ],"
                         + " Year: [ %d ], Seconds: [ %d ], "
                         + "Milliseconds: [ %d ]. Check Failed: [ %s ],"
                         + "RaiseValue: [ %s ].", playerUUID, time.getMonth(),
                         time.getDay(), time.getYear(), time.getSecond(),
                         time.getMillisecond(), nameOfCheck, raiseValue);
  }

  public String getTimeAsString() {
    return timeAsStr;
  }
}
