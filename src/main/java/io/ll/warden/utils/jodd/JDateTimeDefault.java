package io.ll.warden.utils.jodd;

import java.util.*;

public class JDateTimeDefault
{
  public static boolean monthFix;
  public static TimeZone timeZone;
  public static Locale locale;
  public static String format;
  public static JdtFormatter formatter;
  public static int firstDayOfWeek;
  public static int mustHaveDayOfFirstWeek;
  public static int minDaysInFirstWeek;
  public static boolean trackDST;

  static {
    JDateTimeDefault.monthFix = true;
    JDateTimeDefault.timeZone = null;
    JDateTimeDefault.locale = null;
    JDateTimeDefault.format = "YYYY-MM-DD hh:mm:ss.mss";
    JDateTimeDefault.formatter = new Iso8601JdtFormatter();
    JDateTimeDefault.firstDayOfWeek = 1;
    JDateTimeDefault.mustHaveDayOfFirstWeek = 4;
    JDateTimeDefault.minDaysInFirstWeek = 4;
    JDateTimeDefault.trackDST = false;
  }
}