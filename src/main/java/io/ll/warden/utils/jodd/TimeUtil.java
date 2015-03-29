package io.ll.warden.utils.jodd;

import java.text.*;
import java.util.*;

public class TimeUtil
{
  public static final int SECONDS_IN_DAY = 86400;
  public static final long MILLIS_IN_DAY = 86400000L;
  static final int[] MONTH_LENGTH;
  public static final SimpleDateFormat HTTP_DATE_FORMAT;

  public static int dayOfYear(final int year, final int month, final int day) {
    int day_of_year;
    if (isLeapYear(year)) {
      day_of_year = 275 * month / 9 - (month + 9) / 12 + day - 30;
    }
    else {
      day_of_year = 275 * month / 9 - ((month + 9) / 12 << 1) + day - 30;
    }
    return day_of_year;
  }

  public static boolean isLeapYear(final int y) {
    boolean result = false;
    if (y % 4 == 0 && (y < 1582 || y % 100 != 0 || y % 400 == 0)) {
      result = true;
    }
    return result;
  }

  public static int getMonthLength(final int year, final int month) {
    return getMonthLength(year, month, isLeapYear(year));
  }

  static int getMonthLength(final int year, final int month, final boolean leap) {
    if (month < 1 || month > 12) {
      throw new IllegalArgumentException("Invalid month: " + month);
    }
    if (month == 2) {
      return leap ? 29 : 28;
    }
    if (year == 1582 && month == 10) {
      return 21;
    }
    return TimeUtil.MONTH_LENGTH[month];
  }

  public static boolean isValidDate(final int year, final int month, final int day) {
    if (month < 1 || month > 12) {
      return false;
    }
    final int ml = getMonthLength(year, month);
    return day >= 1 && day <= ml;
  }

  public static boolean isValidTime(final int hour, final int minute, final int second, final int millisecond) {
    return hour >= 0 && hour < 24 && minute >= 0 && minute < 60 && second >= 0 && second < 60 && millisecond >= 0 && millisecond < 1000;
  }

  public static boolean isValidDateTime(final int year, final int month, final int day, final int hour, final int minute, final int second, final int millisecond) {
    return isValidDate(year, month, day) && isValidTime(hour, minute, second, millisecond);
  }

  public static boolean isValidDateTime(final DateTimeStamp dts) {
    return isValidDate(dts.year, dts.month, dts.day) && isValidTime(dts.hour, dts.minute, dts.second, dts.millisecond);
  }

  public static JulianDateStamp toJulianDate(final DateTimeStamp time) {
    return toJulianDate(time.year, time.month, time.day, time.hour, time.minute, time.second, time.millisecond);
  }

  public static JulianDateStamp toJulianDate(int year, int month, int day, final int hour, final int minute, final int second, final int millisecond) {
    if (month > 12 || month < -12) {
      final int delta = --month / 12;
      year += delta;
      month -= delta * 12;
      ++month;
    }
    if (month < 0) {
      --year;
      month += 12;
    }
    double frac = hour / 24.0 + minute / 1440.0 + second / 86400.0 + millisecond / 8.64E7;
    if (frac < 0.0) {
      final int delta2 = (int)(-frac) + 1;
      frac += delta2;
      day -= delta2;
    }
    final double gyr = year + 0.01 * month + 1.0E-4 * (day + frac) + 1.0E-9;
    int iy0;
    int im0;
    if (month <= 2) {
      iy0 = year - 1;
      im0 = month + 12;
    }
    else {
      iy0 = year;
      im0 = month;
    }
    final int ia = iy0 / 100;
    final int ib = 2 - ia + (ia >> 2);
    int jd;
    if (year <= 0) {
      jd = (int)(365.25 * iy0 - 0.75) + (int)(30.6001 * (im0 + 1)) + day + 1720994;
    }
    else {
      jd = (int)(365.25 * iy0) + (int)(30.6001 * (im0 + 1)) + day + 1720994;
    }
    if (gyr >= 1582.1015) {
      jd += ib;
    }
    return new JulianDateStamp(jd, frac + 0.5);
  }

  public static DateTimeStamp fromJulianDate(final double JD) {
    return fromJulianDate(new JulianDateStamp(JD));
  }

  public static DateTimeStamp fromJulianDate(final JulianDateStamp jds) {
    final DateTimeStamp time = new DateTimeStamp();
    int ka = (int)(jds.fraction + 0.5);
    final int jd = jds.integer + ka;
    final double frac = jds.fraction + 0.5 - ka + 1.0E-10;
    ka = jd;
    if (jd >= 2299161) {
      final int ialp = (int)((jd - 1867216.25) / 36524.25);
      ka = jd + 1 + ialp - (ialp >> 2);
    }
    final int kb = ka + 1524;
    final int kc = (int)((kb - 122.1) / 365.25);
    final int kd = (int)(kc * 365.25);
    final int ke = (int)((kb - kd) / 30.6001);
    int day = kb - kd - (int)(ke * 30.6001);
    int month;
    if (ke > 13) {
      month = ke - 13;
    }
    else {
      month = ke - 1;
    }
    if (month == 2 && day > 28) {
      day = 29;
    }
    int year;
    if (month == 2 && day == 29 && ke == 3) {
      year = kc - 4716;
    }
    else if (month > 2) {
      year = kc - 4716;
    }
    else {
      year = kc - 4715;
    }
    time.year = year;
    time.month = month;
    time.day = day;
    final double d_hour = frac * 24.0;
    time.hour = (int)d_hour;
    final double d_minute = (d_hour - time.hour) * 60.0;
    time.minute = (int)d_minute;
    final double d_second = (d_minute - time.minute) * 60.0;
    time.second = (int)d_second;
    final double d_millis = (d_second - time.second) * 1000.0;
    time.millisecond = (int)((d_millis * 10.0 + 0.5) / 10.0);
    return time;
  }

  public static int toCalendarMonth(final int month) {
    return month - 1;
  }

  public static int toCalendarDayOfWeek(final int dayOfWeek) {
    return dayOfWeek % 7 + 1;
  }

  public static String formatHttpDate(final long millis) {
    final Date date = new Date(millis);
    return TimeUtil.HTTP_DATE_FORMAT.format(date);
  }

  static {
    MONTH_LENGTH = new int[] { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    HTTP_DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
  }
}
