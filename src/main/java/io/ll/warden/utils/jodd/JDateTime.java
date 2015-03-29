package io.ll.warden.utils.jodd;

import java.io.*;
import java.util.*;
import java.sql.*;
import java.util.Date;

public class JDateTime implements Comparable, Cloneable, Serializable
{
  public static final String DEFAULT_FORMAT = "YYYY-MM-DD hh:mm:ss.mss";
  public static final int MONDAY = 1;
  public static final int TUESDAY = 2;
  public static final int WEDNESDAY = 3;
  public static final int THURSDAY = 4;
  public static final int FRIDAY = 5;
  public static final int SATURDAY = 6;
  public static final int SUNDAY = 7;
  public static final int JANUARY = 1;
  public static final int FEBRUARY = 2;
  public static final int MARCH = 3;
  public static final int APRIL = 4;
  public static final int MAY = 5;
  public static final int JUNE = 6;
  public static final int JULY = 7;
  public static final int AUGUST = 8;
  public static final int SEPTEMBER = 9;
  public static final int OCTOBER = 10;
  public static final int NOVEMBER = 11;
  public static final int DECEMBER = 12;
  protected DateTimeStamp time;
  protected int dayofweek;
  protected int dayofyear;
  protected boolean leap;
  protected int weekofyear;
  protected int weekofmonth;
  protected JulianDateStamp jdate;
  public static final JulianDateStamp JD_1970;
  public static final JulianDateStamp JD_2001;
  private static final int[] NUM_DAYS;
  private static final int[] LEAP_NUM_DAYS;
  protected boolean trackDST;
  protected boolean monthFix;
  protected TimeZone timezone;
  protected Locale locale;
  protected String format;
  protected JdtFormatter jdtFormatter;
  protected int firstDayOfWeek;
  protected int mustHaveDayOfFirstWeek;
  protected int minDaysInFirstWeek;

  public DateTimeStamp getDateTimeStamp() {
    return this.time;
  }

  public JDateTime setDateTimeStamp(final DateTimeStamp dts) {
    return this.set(dts.year, dts.month, dts.day, dts.hour, dts.minute, dts.second, dts.millisecond);
  }

  public JDateTime setJulianDate(final JulianDateStamp jds) {
    this.setJdOnly(jds.clone());
    this.calculateAdditionalData();
    return this;
  }

  public JulianDateStamp getJulianDate() {
    return this.jdate;
  }

  public int getJulianDayNumber() {
    return this.jdate.getJulianDayNumber();
  }

  private void calculateAdditionalData() {
    this.leap = TimeUtil.isLeapYear(this.time.year);
    this.dayofweek = this.calcDayOfWeek();
    this.dayofyear = this.calcDayOfYear();
    this.weekofyear = this.calcWeekOfYear(this.firstDayOfWeek, this.mustHaveDayOfFirstWeek);
    this.weekofmonth = this.calcWeekNumber(this.time.day, this.dayofweek);
  }

  private void setJdOnly(final JulianDateStamp jds) {
    this.jdate = jds;
    this.time = TimeUtil.fromJulianDate(jds);
  }

  public JDateTime set(final int year, final int month, final int day, final int hour, final int minute, final int second, final int millisecond) {
    this.jdate = TimeUtil.toJulianDate(year, month, day, hour, minute, second, millisecond);
    if (TimeUtil.isValidDateTime(year, month, day, hour, minute, second, millisecond)) {
      this.time.year = year;
      this.time.month = month;
      this.time.day = day;
      this.time.hour = hour;
      this.time.minute = minute;
      this.time.second = second;
      this.time.millisecond = millisecond;
      this.calculateAdditionalData();
    }
    else {
      this.setJulianDate(this.jdate);
    }
    return this;
  }

  private void setJdOnly(final int year, final int month, final int day, final int hour, final int minute, final int second, final int millisecond) {
    this.setJdOnly(TimeUtil.toJulianDate(year, month, day, hour, minute, second, millisecond));
  }

  private int calcDayOfWeek() {
    final int jd = (int)(this.jdate.doubleValue() + 0.5);
    return jd % 7 + 1;
  }

  private int calcDayOfYear() {
    if (this.leap) {
      return JDateTime.LEAP_NUM_DAYS[this.time.month] + this.time.day;
    }
    return JDateTime.NUM_DAYS[this.time.month] + this.time.day;
  }

  private int calcWeekOfYear(final int start, final int must) {
    int delta = 0;
    if (start <= this.dayofweek) {
      if (must < start) {
        delta = 7;
      }
    }
    else if (must >= start) {
      delta = -7;
    }
    final int jd = (int)(this.jdate.doubleValue() + 0.5) + delta;
    final int WeekDay = jd % 7 + 1;
    int time_year = this.time.year;
    int DayOfYearNumber = this.dayofyear + delta;
    if (DayOfYearNumber < 1) {
      DayOfYearNumber = (TimeUtil.isLeapYear(--time_year) ? (366 + DayOfYearNumber) : (365 + DayOfYearNumber));
    }
    else if (DayOfYearNumber > (this.leap ? 366 : 365)) {
      DayOfYearNumber = (this.leap ? (DayOfYearNumber - 366) : (DayOfYearNumber - 365));
      ++time_year;
    }
    final int firstDay = jd - DayOfYearNumber + 1;
    final int Jan1WeekDay = firstDay % 7 + 1;
    int YearNumber = time_year;
    int WeekNumber = 52;
    if (DayOfYearNumber <= 8 - Jan1WeekDay && Jan1WeekDay > must) {
      --YearNumber;
      if (Jan1WeekDay == must + 1 || (Jan1WeekDay == must + 2 && TimeUtil.isLeapYear(YearNumber))) {
        WeekNumber = 53;
      }
    }
    int m = 365;
    if (YearNumber == time_year) {
      if (TimeUtil.isLeapYear(time_year)) {
        m = 366;
      }
      if (m - DayOfYearNumber < must - WeekDay) {
        YearNumber = time_year + 1;
        WeekNumber = 1;
      }
    }
    if (YearNumber == time_year) {
      final int n = DayOfYearNumber + (7 - WeekDay) + (Jan1WeekDay - 1);
      WeekNumber = n / 7;
      if (Jan1WeekDay > must) {
        --WeekNumber;
      }
    }
    return WeekNumber;
  }

  private int calcWeekNumber(final int dayOfPeriod, final int dayOfWeek) {
    int periodStartDayOfWeek = (dayOfWeek - this.firstDayOfWeek - dayOfPeriod + 1) % 7;
    if (periodStartDayOfWeek < 0) {
      periodStartDayOfWeek += 7;
    }
    int weekNo = (dayOfPeriod + periodStartDayOfWeek - 1) / 7;
    if (7 - periodStartDayOfWeek >= this.minDaysInFirstWeek) {
      ++weekNo;
    }
    return weekNo;
  }

  public JDateTime add(final int year, final int month, final int day, final int hour, final int minute, final int second, final int millisecond, final boolean monthFix) {
    int difference = 0;
    if (this.trackDST) {
      difference = TimeZoneUtil.getOffset(this, this.timezone);
    }
    this.addNoDST(year, month, day, hour, minute, second, millisecond, monthFix);
    if (this.trackDST) {
      difference = TimeZoneUtil.getOffset(this, this.timezone) - difference;
      if (difference != 0) {
        this.addNoDST(0, 0, 0, 0, 0, 0, difference, false);
      }
    }
    return this;
  }

  protected void addNoDST(int year, int month, int day, int hour, int minute, int second, int millisecond, final boolean monthFix) {
    millisecond += this.time.millisecond;
    second += this.time.second;
    minute += this.time.minute;
    hour += this.time.hour;
    day += this.time.day;
    if (!monthFix) {
      month += this.time.month;
      year += this.time.year;
      this.set(year, month, day, hour, minute, second, millisecond);
    }
    else {
      this.setJdOnly(this.time.year, this.time.month, day, hour, minute, second, millisecond);
      final int from = this.time.day;
      month += this.time.month + year * 12;
      this.setJdOnly(this.time.year, month, this.time.day, this.time.hour, this.time.minute, this.time.second, this.time.millisecond);
      if (this.time.day < from) {
        this.set(this.time.year, this.time.month, 0, this.time.hour, this.time.minute, this.time.second, this.time.millisecond);
      }
      else {
        this.calculateAdditionalData();
      }
    }
  }

  public JDateTime sub(final int year, final int month, final int day, final int hour, final int minute, final int second, final int millisecond, final boolean monthFix) {
    return this.add(-year, -month, -day, -hour, -minute, -second, -millisecond, monthFix);
  }

  public JDateTime add(final int year, final int month, final int day, final int hour, final int minute, final int second, final int millisecond) {
    return this.add(year, month, day, hour, minute, second, millisecond, this.monthFix);
  }

  public JDateTime sub(final int year, final int month, final int day, final int hour, final int minute, final int second, final int millisecond) {
    return this.add(-year, -month, -day, -hour, -minute, -second, millisecond, this.monthFix);
  }

  public JDateTime add(final int year, final int month, final int day, final boolean monthFix) {
    return this.add(year, month, day, 0, 0, 0, 0, monthFix);
  }

  public JDateTime sub(final int year, final int month, final int day, final boolean monthFix) {
    return this.add(-year, -month, -day, 0, 0, 0, 0, monthFix);
  }

  public JDateTime add(final int year, final int month, final int day) {
    return this.add(year, month, day, this.monthFix);
  }

  public JDateTime sub(final int year, final int month, final int day) {
    return this.add(-year, -month, -day, this.monthFix);
  }

  public JDateTime addTime(final int hour, final int minute, final int second, final int millisecond, final boolean monthFix) {
    return this.add(0, 0, 0, hour, minute, second, millisecond, monthFix);
  }

  public JDateTime subTime(final int hour, final int minute, final int second, final int millisecond, final boolean monthFix) {
    return this.add(0, 0, 0, -hour, -minute, -second, -millisecond, monthFix);
  }

  public JDateTime addTime(final int hour, final int minute, final int second, final boolean monthFix) {
    return this.add(0, 0, 0, hour, minute, second, 0, monthFix);
  }

  public JDateTime subTime(final int hour, final int minute, final int second, final boolean monthFix) {
    return this.add(0, 0, 0, -hour, -minute, -second, 0, monthFix);
  }

  public JDateTime addTime(final int hour, final int minute, final int second, final int millisecond) {
    return this.addTime(hour, minute, second, millisecond, this.monthFix);
  }

  public JDateTime subTime(final int hour, final int minute, final int second, final int millisecond) {
    return this.addTime(-hour, -minute, -second, -millisecond, this.monthFix);
  }

  public JDateTime addTime(final int hour, final int minute, final int second) {
    return this.addTime(hour, minute, second, 0, this.monthFix);
  }

  public JDateTime subTime(final int hour, final int minute, final int second) {
    return this.addTime(-hour, -minute, -second, 0, this.monthFix);
  }

  public JDateTime addYear(final int y, final boolean monthFix) {
    return this.add(y, 0, 0, monthFix);
  }

  public JDateTime subYear(final int y, final boolean monthFix) {
    return this.add(-y, 0, 0, monthFix);
  }

  public JDateTime addYear(final int y) {
    return this.addYear(y, this.monthFix);
  }

  public JDateTime subYear(final int y) {
    return this.addYear(-y, this.monthFix);
  }

  public JDateTime addMonth(final int m, final boolean monthFix) {
    return this.add(0, m, 0, monthFix);
  }

  public JDateTime subMonth(final int m, final boolean monthFix) {
    return this.add(0, -m, 0, monthFix);
  }

  public JDateTime addMonth(final int m) {
    return this.addMonth(m, this.monthFix);
  }

  public JDateTime subMonth(final int m) {
    return this.addMonth(-m, this.monthFix);
  }

  public JDateTime addDay(final int d, final boolean monthFix) {
    return this.add(0, 0, d, monthFix);
  }

  public JDateTime subDay(final int d, final boolean monthFix) {
    return this.add(0, 0, -d, monthFix);
  }

  public JDateTime addDay(final int d) {
    return this.addDay(d, this.monthFix);
  }

  public JDateTime subDay(final int d) {
    return this.addDay(-d, this.monthFix);
  }

  public JDateTime addHour(final int h, final boolean monthFix) {
    return this.addTime(h, 0, 0, 0, monthFix);
  }

  public JDateTime subHour(final int h, final boolean monthFix) {
    return this.addTime(-h, 0, 0, 0, monthFix);
  }

  public JDateTime addHour(final int h) {
    return this.addHour(h, this.monthFix);
  }

  public JDateTime subHour(final int h) {
    return this.addHour(-h, this.monthFix);
  }

  public JDateTime addMinute(final int m, final boolean monthFix) {
    return this.addTime(0, m, 0, 0, monthFix);
  }

  public JDateTime subMinute(final int m, final boolean monthFix) {
    return this.addTime(0, -m, 0, 0, monthFix);
  }

  public JDateTime addMinute(final int m) {
    return this.addMinute(m, this.monthFix);
  }

  public JDateTime subMinute(final int m) {
    return this.addMinute(-m, this.monthFix);
  }

  public JDateTime addSecond(final int s, final boolean monthFix) {
    return this.addTime(0, 0, s, 0, monthFix);
  }

  public JDateTime subSecond(final int s, final boolean monthFix) {
    return this.addTime(0, 0, -s, 0, monthFix);
  }

  public JDateTime addSecond(final int s) {
    return this.addSecond(s, this.monthFix);
  }

  public JDateTime subSecond(final int s) {
    return this.addSecond(-s, this.monthFix);
  }

  public JDateTime addMillisecond(final int ms, final boolean monthFix) {
    return this.addTime(0, 0, 0, ms, monthFix);
  }

  public JDateTime subMillisecond(final int ms, final boolean monthFix) {
    return this.addTime(0, 0, 0, -ms, monthFix);
  }

  public JDateTime addMillisecond(final int ms) {
    return this.addMillisecond(ms, this.monthFix);
  }

  public JDateTime subMillisecond(final int ms) {
    return this.addMillisecond(-ms, this.monthFix);
  }

  public JDateTime(final int year, final int month, final int day, final int hour, final int minute, final int second, final int millisecond) {
    super();
    this.time = new DateTimeStamp();
    this.trackDST = JDateTimeDefault.trackDST;
    this.monthFix = JDateTimeDefault.monthFix;
    this.timezone = ((JDateTimeDefault.timeZone == null) ? TimeZone.getDefault() : JDateTimeDefault.timeZone);
    this.locale = ((JDateTimeDefault.locale == null) ? Locale.getDefault() : JDateTimeDefault.locale);
    this.format = JDateTimeDefault.format;
    this.jdtFormatter = JDateTimeDefault.formatter;
    this.firstDayOfWeek = JDateTimeDefault.firstDayOfWeek;
    this.mustHaveDayOfFirstWeek = JDateTimeDefault.mustHaveDayOfFirstWeek;
    this.minDaysInFirstWeek = JDateTimeDefault.minDaysInFirstWeek;
    this.set(year, month, day, hour, minute, second, millisecond);
  }

  public JDateTime set(final int year, final int month, final int day) {
    return this.set(year, month, day, 0, 0, 0, 0);
  }

  public JDateTime(final int year, final int month, final int day) {
    super();
    this.time = new DateTimeStamp();
    this.trackDST = JDateTimeDefault.trackDST;
    this.monthFix = JDateTimeDefault.monthFix;
    this.timezone = ((JDateTimeDefault.timeZone == null) ? TimeZone.getDefault() : JDateTimeDefault.timeZone);
    this.locale = ((JDateTimeDefault.locale == null) ? Locale.getDefault() : JDateTimeDefault.locale);
    this.format = JDateTimeDefault.format;
    this.jdtFormatter = JDateTimeDefault.formatter;
    this.firstDayOfWeek = JDateTimeDefault.firstDayOfWeek;
    this.mustHaveDayOfFirstWeek = JDateTimeDefault.mustHaveDayOfFirstWeek;
    this.minDaysInFirstWeek = JDateTimeDefault.minDaysInFirstWeek;
    this.set(year, month, day);
  }

  public JDateTime setTime(final int hour, final int minute, final int second, final int millisecond) {
    return this.set(this.time.year, this.time.month, this.time.day, hour, minute, second, millisecond);
  }

  public JDateTime setDate(final int year, final int month, final int day) {
    return this.set(year, month, day, this.time.hour, this.time.minute, this.time.second, this.time.millisecond);
  }

  public JDateTime(final long millis) {
    super();
    this.time = new DateTimeStamp();
    this.trackDST = JDateTimeDefault.trackDST;
    this.monthFix = JDateTimeDefault.monthFix;
    this.timezone = ((JDateTimeDefault.timeZone == null) ? TimeZone.getDefault() : JDateTimeDefault.timeZone);
    this.locale = ((JDateTimeDefault.locale == null) ? Locale.getDefault() : JDateTimeDefault.locale);
    this.format = JDateTimeDefault.format;
    this.jdtFormatter = JDateTimeDefault.formatter;
    this.firstDayOfWeek = JDateTimeDefault.firstDayOfWeek;
    this.mustHaveDayOfFirstWeek = JDateTimeDefault.mustHaveDayOfFirstWeek;
    this.minDaysInFirstWeek = JDateTimeDefault.minDaysInFirstWeek;
    this.setTimeInMillis(millis);
  }

  public JDateTime setTimeInMillis(long millis) {
    millis += this.timezone.getOffset(millis);
    int integer = (int)(millis / 86400000L);
    double fraction = millis % 86400000L / 8.64E7;
    integer += JDateTime.JD_1970.integer;
    fraction += JDateTime.JD_1970.fraction;
    return this.setJulianDate(new JulianDateStamp(integer, fraction));
  }

  public long getTimeInMillis() {
    double then = (this.jdate.fraction - JDateTime.JD_1970.fraction) * 8.64E7;
    then += (this.jdate.integer - JDateTime.JD_1970.integer) * 86400000L;
    then -= this.timezone.getOffset((long)then);
    then += ((then > 0.0) ? 1.0E-6 : -1.0E-6);
    return (long)then;
  }

  public JDateTime setYear(final int y) {
    return this.setDate(y, this.time.month, this.time.day);
  }

  public JDateTime setMonth(final int m) {
    return this.setDate(this.time.year, m, this.time.day);
  }

  public JDateTime setDay(final int d) {
    return this.setDate(this.time.year, this.time.month, d);
  }

  public JDateTime setHour(final int h) {
    return this.setTime(h, this.time.minute, this.time.second, this.time.millisecond);
  }

  public JDateTime setMinute(final int m) {
    return this.setTime(this.time.hour, m, this.time.second, this.time.millisecond);
  }

  public JDateTime setSecond(final int s) {
    return this.setTime(this.time.hour, this.time.minute, s, this.time.millisecond);
  }

  public JDateTime setSecond(final int s, final int m) {
    return this.setTime(this.time.hour, this.time.minute, s, m);
  }

  public JDateTime setMillisecond(final int m) {
    return this.setTime(this.time.hour, this.time.minute, this.time.second, m);
  }

  public int getYear() {
    return this.time.year;
  }

  public int getMonth() {
    return this.time.month;
  }

  public int getDay() {
    return this.time.day;
  }

  public int getDayOfMonth() {
    return this.time.day;
  }

  public int getHour() {
    return this.time.hour;
  }

  public int getMinute() {
    return this.time.minute;
  }

  public int getSecond() {
    return this.time.second;
  }

  public int getMillisecond() {
    return this.time.millisecond;
  }

  public int getDayOfWeek() {
    return this.dayofweek;
  }

  public int getDayOfYear() {
    return this.dayofyear;
  }

  public boolean isLeapYear() {
    return this.leap;
  }

  public int getWeekOfYear() {
    return this.weekofyear;
  }

  public int getWeekOfMonth() {
    return this.weekofmonth;
  }

  public int getMonthLength(final int month) {
    return TimeUtil.getMonthLength(this.time.year, month, this.leap);
  }

  public int getMonthLength() {
    return this.getMonthLength(this.time.month);
  }

  public int getEra() {
    return (this.time.year > 0) ? 1 : 0;
  }

  public int getMillisOfDay() {
    return ((this.time.hour * 60 + this.time.minute) * 60 + this.time.second) * 1000 + this.time.millisecond;
  }

  public JDateTime setCurrentTime() {
    return this.setTimeInMillis(System.currentTimeMillis());
  }

  public JDateTime() {
    super();
    this.time = new DateTimeStamp();
    this.trackDST = JDateTimeDefault.trackDST;
    this.monthFix = JDateTimeDefault.monthFix;
    this.timezone = ((JDateTimeDefault.timeZone == null) ? TimeZone.getDefault() : JDateTimeDefault.timeZone);
    this.locale = ((JDateTimeDefault.locale == null) ? Locale.getDefault() : JDateTimeDefault.locale);
    this.format = JDateTimeDefault.format;
    this.jdtFormatter = JDateTimeDefault.formatter;
    this.firstDayOfWeek = JDateTimeDefault.firstDayOfWeek;
    this.mustHaveDayOfFirstWeek = JDateTimeDefault.mustHaveDayOfFirstWeek;
    this.minDaysInFirstWeek = JDateTimeDefault.minDaysInFirstWeek;
    this.setCurrentTime();
  }

  public JDateTime(final Calendar calendar) {
    super();
    this.time = new DateTimeStamp();
    this.trackDST = JDateTimeDefault.trackDST;
    this.monthFix = JDateTimeDefault.monthFix;
    this.timezone = ((JDateTimeDefault.timeZone == null) ? TimeZone.getDefault() : JDateTimeDefault.timeZone);
    this.locale = ((JDateTimeDefault.locale == null) ? Locale.getDefault() : JDateTimeDefault.locale);
    this.format = JDateTimeDefault.format;
    this.jdtFormatter = JDateTimeDefault.formatter;
    this.firstDayOfWeek = JDateTimeDefault.firstDayOfWeek;
    this.mustHaveDayOfFirstWeek = JDateTimeDefault.mustHaveDayOfFirstWeek;
    this.minDaysInFirstWeek = JDateTimeDefault.minDaysInFirstWeek;
    this.setDateTime(calendar);
  }

  public JDateTime setDateTime(final Calendar calendar) {
    this.setTimeInMillis(calendar.getTimeInMillis());
    this.changeTimeZone(calendar.getTimeZone());
    return this;
  }

  public JDateTime(final Date date) {
    super();
    this.time = new DateTimeStamp();
    this.trackDST = JDateTimeDefault.trackDST;
    this.monthFix = JDateTimeDefault.monthFix;
    this.timezone = ((JDateTimeDefault.timeZone == null) ? TimeZone.getDefault() : JDateTimeDefault.timeZone);
    this.locale = ((JDateTimeDefault.locale == null) ? Locale.getDefault() : JDateTimeDefault.locale);
    this.format = JDateTimeDefault.format;
    this.jdtFormatter = JDateTimeDefault.formatter;
    this.firstDayOfWeek = JDateTimeDefault.firstDayOfWeek;
    this.mustHaveDayOfFirstWeek = JDateTimeDefault.mustHaveDayOfFirstWeek;
    this.minDaysInFirstWeek = JDateTimeDefault.minDaysInFirstWeek;
    this.setDateTime(date);
  }

  public JDateTime setDateTime(final Date date) {
    return this.setTimeInMillis(date.getTime());
  }

  public Date convertToDate() {
    return new Date(this.getTimeInMillis());
  }

  public Calendar convertToCalendar() {
    final Calendar calendar = Calendar.getInstance(this.getTimeZone());
    calendar.setTimeInMillis(this.getTimeInMillis());
    return calendar;
  }

  public java.sql.Date convertToSqlDate() {
    return new java.sql.Date(this.getTimeInMillis());
  }

  public Time convertToSqlTime() {
    return new Time(this.getTimeInMillis());
  }

  public Timestamp convertToSqlTimestamp() {
    return new Timestamp(this.getTimeInMillis());
  }

  public JDateTime(final DateTimeStamp dts) {
    super();
    this.time = new DateTimeStamp();
    this.trackDST = JDateTimeDefault.trackDST;
    this.monthFix = JDateTimeDefault.monthFix;
    this.timezone = ((JDateTimeDefault.timeZone == null) ? TimeZone.getDefault() : JDateTimeDefault.timeZone);
    this.locale = ((JDateTimeDefault.locale == null) ? Locale.getDefault() : JDateTimeDefault.locale);
    this.format = JDateTimeDefault.format;
    this.jdtFormatter = JDateTimeDefault.formatter;
    this.firstDayOfWeek = JDateTimeDefault.firstDayOfWeek;
    this.mustHaveDayOfFirstWeek = JDateTimeDefault.mustHaveDayOfFirstWeek;
    this.minDaysInFirstWeek = JDateTimeDefault.minDaysInFirstWeek;
    this.setDateTimeStamp(dts);
  }

  public JDateTime(final JulianDateStamp jds) {
    super();
    this.time = new DateTimeStamp();
    this.trackDST = JDateTimeDefault.trackDST;
    this.monthFix = JDateTimeDefault.monthFix;
    this.timezone = ((JDateTimeDefault.timeZone == null) ? TimeZone.getDefault() : JDateTimeDefault.timeZone);
    this.locale = ((JDateTimeDefault.locale == null) ? Locale.getDefault() : JDateTimeDefault.locale);
    this.format = JDateTimeDefault.format;
    this.jdtFormatter = JDateTimeDefault.formatter;
    this.firstDayOfWeek = JDateTimeDefault.firstDayOfWeek;
    this.mustHaveDayOfFirstWeek = JDateTimeDefault.mustHaveDayOfFirstWeek;
    this.minDaysInFirstWeek = JDateTimeDefault.minDaysInFirstWeek;
    this.setJulianDate(jds);
  }

  public JDateTime(final double jd) {
    super();
    this.time = new DateTimeStamp();
    this.trackDST = JDateTimeDefault.trackDST;
    this.monthFix = JDateTimeDefault.monthFix;
    this.timezone = ((JDateTimeDefault.timeZone == null) ? TimeZone.getDefault() : JDateTimeDefault.timeZone);
    this.locale = ((JDateTimeDefault.locale == null) ? Locale.getDefault() : JDateTimeDefault.locale);
    this.format = JDateTimeDefault.format;
    this.jdtFormatter = JDateTimeDefault.formatter;
    this.firstDayOfWeek = JDateTimeDefault.firstDayOfWeek;
    this.mustHaveDayOfFirstWeek = JDateTimeDefault.mustHaveDayOfFirstWeek;
    this.minDaysInFirstWeek = JDateTimeDefault.minDaysInFirstWeek;
    this.setJulianDate(new JulianDateStamp(jd));
  }

  public JDateTime(final String src) {
    super();
    this.time = new DateTimeStamp();
    this.trackDST = JDateTimeDefault.trackDST;
    this.monthFix = JDateTimeDefault.monthFix;
    this.timezone = ((JDateTimeDefault.timeZone == null) ? TimeZone.getDefault() : JDateTimeDefault.timeZone);
    this.locale = ((JDateTimeDefault.locale == null) ? Locale.getDefault() : JDateTimeDefault.locale);
    this.format = JDateTimeDefault.format;
    this.jdtFormatter = JDateTimeDefault.formatter;
    this.firstDayOfWeek = JDateTimeDefault.firstDayOfWeek;
    this.mustHaveDayOfFirstWeek = JDateTimeDefault.mustHaveDayOfFirstWeek;
    this.minDaysInFirstWeek = JDateTimeDefault.minDaysInFirstWeek;
    this.parse(src);
  }

  public JDateTime(final String src, final String template) {
    super();
    this.time = new DateTimeStamp();
    this.trackDST = JDateTimeDefault.trackDST;
    this.monthFix = JDateTimeDefault.monthFix;
    this.timezone = ((JDateTimeDefault.timeZone == null) ? TimeZone.getDefault() : JDateTimeDefault.timeZone);
    this.locale = ((JDateTimeDefault.locale == null) ? Locale.getDefault() : JDateTimeDefault.locale);
    this.format = JDateTimeDefault.format;
    this.jdtFormatter = JDateTimeDefault.formatter;
    this.firstDayOfWeek = JDateTimeDefault.firstDayOfWeek;
    this.mustHaveDayOfFirstWeek = JDateTimeDefault.mustHaveDayOfFirstWeek;
    this.minDaysInFirstWeek = JDateTimeDefault.minDaysInFirstWeek;
    this.parse(src, template);
  }

  public JDateTime(final String src, final JdtFormat jdtFormat) {
    super();
    this.time = new DateTimeStamp();
    this.trackDST = JDateTimeDefault.trackDST;
    this.monthFix = JDateTimeDefault.monthFix;
    this.timezone = ((JDateTimeDefault.timeZone == null) ? TimeZone.getDefault() : JDateTimeDefault.timeZone);
    this.locale = ((JDateTimeDefault.locale == null) ? Locale.getDefault() : JDateTimeDefault.locale);
    this.format = JDateTimeDefault.format;
    this.jdtFormatter = JDateTimeDefault.formatter;
    this.firstDayOfWeek = JDateTimeDefault.firstDayOfWeek;
    this.mustHaveDayOfFirstWeek = JDateTimeDefault.mustHaveDayOfFirstWeek;
    this.minDaysInFirstWeek = JDateTimeDefault.minDaysInFirstWeek;
    this.parse(src, jdtFormat);
  }

  public boolean isTrackDST() {
    return this.trackDST;
  }

  public JDateTime setTrackDST(final boolean trackDST) {
    this.trackDST = trackDST;
    return this;
  }

  public boolean isMonthFix() {
    return this.monthFix;
  }

  public JDateTime setMonthFix(final boolean monthFix) {
    this.monthFix = monthFix;
    return this;
  }

  public JDateTime changeTimeZone(final TimeZone timezone) {
    final long now = this.getTimeInMillis();
    final int difference = TimeZoneUtil.getOffsetDifference(now, this.timezone, timezone);
    this.timezone = timezone;
    if (difference != 0) {
      this.addMillisecond(difference);
    }
    return this;
  }

  public JDateTime changeTimeZone(final TimeZone from, final TimeZone to) {
    this.timezone = from;
    this.changeTimeZone(to);
    return this;
  }

  public JDateTime setTimeZone(final TimeZone timezone) {
    this.timezone = timezone;
    return this;
  }

  public TimeZone getTimeZone() {
    return this.timezone;
  }

  public boolean isInDaylightTime() {
    final long now = this.getTimeInMillis();
    final int offset = this.timezone.getOffset(now);
    final int rawOffset = this.timezone.getRawOffset();
    return offset != rawOffset;
  }

  public JDateTime setLocale(final Locale locale) {
    this.locale = locale;
    return this;
  }

  public Locale getLocale() {
    return this.locale;
  }

  public JDateTime setFormat(final String format) {
    this.format = format;
    return this;
  }

  public String getFormat() {
    return this.format;
  }

  public JDateTime setJdtFormatter(final JdtFormatter jdtFormatter) {
    this.jdtFormatter = jdtFormatter;
    return this;
  }

  public JdtFormatter getJdtFormatter() {
    return this.jdtFormatter;
  }

  public JDateTime setJdtFormat(final JdtFormat jdtFormat) {
    this.format = jdtFormat.getFormat();
    this.jdtFormatter = jdtFormat.getFormatter();
    return this;
  }

  public String toString(final String format) {
    return this.jdtFormatter.convert(this, format);
  }

  public String toString() {
    return this.jdtFormatter.convert(this, this.format);
  }

  public String toString(final JdtFormat jdtFormat) {
    return jdtFormat.convert(this);
  }

  public void parse(final String src, final String format) {
    this.setDateTimeStamp(this.jdtFormatter.parse(src, format));
  }

  public void parse(final String src) {
    this.setDateTimeStamp(this.jdtFormatter.parse(src, this.format));
  }

  public void parse(final String src, final JdtFormat jdtFormat) {
    this.setDateTimeStamp(jdtFormat.parse(src));
  }

  public boolean isValid(final String s) {
    return this.isValid(s, this.format);
  }

  public boolean isValid(final String s, final String template) {
    DateTimeStamp dtsOriginal;
    try {
      dtsOriginal = this.jdtFormatter.parse(s, template);
    }
    catch (Exception ignore) {
      return false;
    }
    return dtsOriginal != null && TimeUtil.isValidDateTime(dtsOriginal);
  }

  public JDateTime setWeekDefinition(final int start, final int must) {
    if (start >= 1 && start <= 7) {
      this.firstDayOfWeek = start;
    }
    if (must >= 1 && must <= 7) {
      this.mustHaveDayOfFirstWeek = must;
      this.minDaysInFirstWeek = convertMin2Must(this.firstDayOfWeek, must);
    }
    return this;
  }

  public int getFirstDayOfWeek() {
    return this.firstDayOfWeek;
  }

  public int getMustHaveDayOfFirstWeek() {
    return this.mustHaveDayOfFirstWeek;
  }

  public int getMinDaysInFirstWeek() {
    return this.minDaysInFirstWeek;
  }

  public JDateTime setWeekDefinitionAlt(final int start, final int min) {
    if (start >= 1 && start <= 7) {
      this.firstDayOfWeek = start;
    }
    if (min >= 1 && min <= 7) {
      this.mustHaveDayOfFirstWeek = convertMin2Must(this.firstDayOfWeek, min);
      this.minDaysInFirstWeek = min;
    }
    return this;
  }

  private static int convertMin2Must(final int start, final int min) {
    int must = 8 - min + (start - 1);
    if (must > 7) {
      must -= 7;
    }
    return must;
  }

  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof JDateTime)) {
      return false;
    }
    final JDateTime jdt = (JDateTime)obj;
    return this.monthFix == jdt.monthFix && this.firstDayOfWeek == jdt.firstDayOfWeek && this.mustHaveDayOfFirstWeek == jdt.mustHaveDayOfFirstWeek && this.time.equals(jdt.time) && this.timezone.equals(jdt.timezone);
  }

  public int hashCode() {
    int result = 173;
    result = HashCode.hash(result, this.time);
    result = HashCode.hash(result, this.timezone);
    result = HashCode.hash(result, this.monthFix);
    result = HashCode.hash(result, this.firstDayOfWeek);
    result = HashCode.hash(result, this.mustHaveDayOfFirstWeek);
    return result;
  }

  public JDateTime clone() {
    final JDateTime jdt = new JDateTime(this.jdate);
    jdt.monthFix = this.monthFix;
    jdt.timezone = this.timezone;
    jdt.locale = this.locale;
    jdt.format = this.format;
    jdt.jdtFormatter = this.jdtFormatter;
    jdt.firstDayOfWeek = this.firstDayOfWeek;
    jdt.mustHaveDayOfFirstWeek = this.mustHaveDayOfFirstWeek;
    jdt.trackDST = this.trackDST;
    return jdt;
  }

  public int compareTo(final Object o) {
    return this.time.compareTo(((JDateTime)o).getDateTimeStamp());
  }

  public int compareTo(final JDateTime jd) {
    return this.time.compareTo(jd.getDateTimeStamp());
  }

  public int compareDateTo(final JDateTime jd) {
    return this.time.compareDateTo(jd.getDateTimeStamp());
  }

  public boolean isAfter(final JDateTime then) {
    return this.time.compareTo(then.getDateTimeStamp()) > 0;
  }

  public boolean isBefore(final JDateTime then) {
    return this.time.compareTo(then.getDateTimeStamp()) < 0;
  }

  public boolean isAfterDate(final JDateTime then) {
    return this.time.compareDateTo(then.getDateTimeStamp()) > 0;
  }

  public boolean isBeforeDate(final JDateTime then) {
    return this.time.compareDateTo(then.getDateTimeStamp()) < 0;
  }

  public int daysBetween(final JDateTime then) {
    return this.jdate.daysBetween(then.jdate);
  }

  public int daysBetween(final JulianDateStamp then) {
    return this.jdate.daysBetween(then);
  }

  public double getJulianDateDouble() {
    return this.jdate.doubleValue();
  }

  public JDateTime setJulianDate(final double jd) {
    return this.setJulianDate(new JulianDateStamp(jd));
  }

  public boolean equalsDate(final int year, final int month, final int day) {
    return this.time.year == year && this.time.month == month && this.time.day == day;
  }

  public boolean equalsDate(final JDateTime date) {
    return this.time.isEqualDate(date.time);
  }

  public boolean equalsTime(final JDateTime date) {
    return this.time.isEqualTime(date.time);
  }

  static {
    JD_1970 = new JulianDateStamp(2440587, 0.5);
    JD_2001 = new JulianDateStamp(2451910, 0.5);
    NUM_DAYS = new int[] { -1, 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334 };
    LEAP_NUM_DAYS = new int[] { -1, 0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335 };
  }
}
