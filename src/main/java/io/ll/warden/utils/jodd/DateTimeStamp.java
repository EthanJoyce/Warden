package io.ll.warden.utils.jodd;

import java.io.*;

public class DateTimeStamp implements Comparable, Serializable, Cloneable
{
  public int year;
  public int month;
  public int day;
  public int hour;
  public int minute;
  public int second;
  public int millisecond;

  public DateTimeStamp() {
    super();
    this.month = 1;
    this.day = 1;
  }

  public DateTimeStamp(final int year, final int month, final int day, final int hour, final int minute, final int second, final int millisecond) {
    super();
    this.month = 1;
    this.day = 1;
    this.year = year;
    this.month = month;
    this.day = day;
    this.hour = hour;
    this.minute = minute;
    this.second = second;
    this.millisecond = millisecond;
  }

  public DateTimeStamp(final int year, final int month, final int day) {
    this(year, month, day, 0, 0, 0, 0);
  }

  public int getYear() {
    return this.year;
  }

  public void setYear(final int year) {
    this.year = year;
  }

  public int getMonth() {
    return this.month;
  }

  public void setMonth(final int month) {
    this.month = month;
  }

  public int getDay() {
    return this.day;
  }

  public void setDay(final int day) {
    this.day = day;
  }

  public int getHour() {
    return this.hour;
  }

  public void setHour(final int hour) {
    this.hour = hour;
  }

  public int getMinute() {
    return this.minute;
  }

  public void setMinute(final int minute) {
    this.minute = minute;
  }

  public int getSecond() {
    return this.second;
  }

  public void setSecond(final int second) {
    this.second = second;
  }

  public int getMillisecond() {
    return this.millisecond;
  }

  public void setMillisecond(final int millisecond) {
    this.millisecond = millisecond;
  }

  public int compareTo(final Object o) {
    final DateTimeStamp dts = (DateTimeStamp)o;
    int date1 = this.year * 10000 + this.month * 100 + this.day;
    int date2 = dts.year * 10000 + dts.month * 100 + dts.day;
    if (date1 < date2) {
      return -1;
    }
    if (date1 > date2) {
      return 1;
    }
    date1 = this.hour * 10000000 + this.minute * 100000 + this.second * 1000 + this.millisecond;
    date2 = dts.hour * 10000000 + dts.minute * 100000 + dts.second * 1000 + dts.millisecond;
    if (date1 < date2) {
      return -1;
    }
    if (date1 > date2) {
      return 1;
    }
    return 0;
  }

  public int compareDateTo(final Object o) {
    final DateTimeStamp dts = (DateTimeStamp)o;
    final int date1 = this.year * 10000 + this.month * 100 + this.day;
    final int date2 = dts.year * 10000 + dts.month * 100 + dts.day;
    if (date1 < date2) {
      return -1;
    }
    if (date1 > date2) {
      return 1;
    }
    return 0;
  }

  public String toString() {
    final StringBuilder sb = new StringBuilder(25);
    sb.append(this.year).append('-').append(this.month).append('-').append(this.day).append(' ');
    sb.append(this.hour).append(':').append(this.minute).append(':').append(this.second).append('.').append(this.millisecond);
    return sb.toString();
  }

  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof DateTimeStamp)) {
      return false;
    }
    final DateTimeStamp stamp = (DateTimeStamp)object;
    return stamp.year == this.year && stamp.month == this.month && stamp.day == this.day && stamp.hour == this.hour && stamp.minute == this.minute && stamp.second == this.second && stamp.millisecond == this.millisecond;
  }

  public int hashCode() {
    int result = 173;
    result = HashCode.hash(result, this.year);
    result = HashCode.hash(result, this.month);
    result = HashCode.hash(result, this.day);
    result = HashCode.hash(result, this.hour);
    result = HashCode.hash(result, this.minute);
    result = HashCode.hash(result, this.second);
    result = HashCode.hash(result, this.millisecond);
    return result;
  }

  protected DateTimeStamp clone() {
    final DateTimeStamp dts = new DateTimeStamp();
    dts.year = this.year;
    dts.month = this.month;
    dts.day = this.day;
    dts.hour = this.hour;
    dts.minute = this.minute;
    dts.second = this.second;
    dts.millisecond = this.millisecond;
    return dts;
  }

  public boolean isEqualDate(final DateTimeStamp date) {
    return date.day == this.day && date.month == this.month && date.year == this.year;
  }

  public boolean isEqualTime(final DateTimeStamp time) {
    return time.hour == this.hour && time.minute == this.minute && time.second == this.second && time.millisecond == this.millisecond;
  }
}