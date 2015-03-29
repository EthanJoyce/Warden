package io.ll.warden.heuristics;

import io.ll.warden.utils.jodd.DateTimeStamp;
import io.ll.warden.utils.jodd.Iso8601JdtFormatter;
import io.ll.warden.utils.jodd.JdtFormat;

/**
 * Creator: LordLambda
 * Date: 3/28/2015
 * Project: Warden
 * Usage: A ban report
 */
public class BanReport {

  private DateTimeStamp time;
  private String timeAsStr;
  private JdtFormat format;
  private float pointsAtBan;

  public BanReport(String time, float pointsAtBan) {
    format = new JdtFormat(new Iso8601JdtFormatter(), time);
    timeAsStr = time;
    this.time = format.parse(time);
    this.pointsAtBan = pointsAtBan;
  }

  public String getTimeAsString() {
    return timeAsStr;
  }

  public String getTimeForPlayerMessage() {
    return String.format("Month: [ %d ], Day: [ %d ], Year: [ %d]"
                         + ", Hour: [ %d ], Minute: [ %d ],"
                         + " Second: [ %d ], Millisecond: [ %d ],"
                         + "Points: [ %s ]", time.getMonth(),
                         time.getDay(), time.getYear(), time.getHour(),
                         time.getMinute(), time.getSecond(), time.getMillisecond(),
                         pointsAtBan);
  }

  public float getPointsAtBan() {
    return pointsAtBan;
  }
}
