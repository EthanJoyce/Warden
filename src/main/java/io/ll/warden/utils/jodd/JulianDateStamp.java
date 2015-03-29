package io.ll.warden.utils.jodd;

import java.io.*;
import java.math.*;

public class JulianDateStamp implements Serializable, Cloneable
{
  protected int integer;
  protected double fraction;

  public int getInteger() {
    return this.integer;
  }

  public double getFraction() {
    return this.fraction;
  }

  public int getSignificantFraction() {
    return (int)(this.fraction * 1.0E8);
  }

  public int getJulianDayNumber() {
    if (this.fraction >= 0.5) {
      return this.integer + 1;
    }
    return this.integer;
  }

  public JulianDateStamp() {
    super();
  }

  public JulianDateStamp(final double jd) {
    super();
    this.set(jd);
  }

  public JulianDateStamp(final int i, final double f) {
    super();
    this.set(i, f);
  }

  public JulianDateStamp(BigDecimal bd) {
    super();
    final double d = bd.doubleValue();
    this.integer = (int)d;
    bd = bd.subtract(new BigDecimal(this.integer));
    this.fraction = bd.doubleValue();
  }

  public double doubleValue() {
    return this.integer + this.fraction;
  }

  public BigDecimal toBigDecimal() {
    final BigDecimal bd = new BigDecimal(this.integer);
    return bd.add(new BigDecimal(this.fraction));
  }

  public String toString() {
    String s = Double.toString(this.fraction);
    final int i = s.indexOf(46);
    s = s.substring(i);
    return this.integer + s;
  }

  public JulianDateStamp add(final JulianDateStamp jds) {
    final int i = this.integer + jds.integer;
    final double f = this.fraction + jds.fraction;
    this.set(i, f);
    return this;
  }

  public JulianDateStamp add(final double delta) {
    this.set(this.integer, this.fraction + delta);
    return this;
  }

  public JulianDateStamp sub(final JulianDateStamp jds) {
    final int i = this.integer - jds.integer;
    final double f = this.fraction - jds.fraction;
    this.set(i, f);
    return this;
  }

  public JulianDateStamp sub(final double delta) {
    this.set(this.integer, this.fraction - delta);
    return this;
  }

  public void set(final int i, double f) {
    this.integer = i;
    final int fi = (int)f;
    f -= fi;
    this.integer += fi;
    if (f < 0.0) {
      ++f;
      --this.integer;
    }
    this.fraction = f;
  }

  public void set(final double jd) {
    this.integer = (int)jd;
    this.fraction = jd - this.integer;
  }

  public int daysBetween(final JulianDateStamp otherDate) {
    final int difference = this.daysSpan(otherDate);
    return (difference >= 0) ? difference : (-difference);
  }

  public int daysSpan(final JulianDateStamp otherDate) {
    final int now = this.getJulianDayNumber();
    final int then = otherDate.getJulianDayNumber();
    return now - then;
  }

  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof JulianDateStamp)) {
      return false;
    }
    final JulianDateStamp stamp = (JulianDateStamp)object;
    return stamp.integer == this.integer && Double.compare(stamp.fraction, this.fraction) == 0;
  }

  public int hashCode() {
    int result = 173;
    result = HashCode.hash(result, this.integer);
    result = HashCode.hash(result, this.fraction);
    return result;
  }

  protected JulianDateStamp clone() {
    return new JulianDateStamp(this.integer, this.fraction);
  }

  public JulianDateStamp getReducedJulianDate() {
    return new JulianDateStamp(this.integer - 2400000, this.fraction);
  }

  public void setReducedJulianDate(final double rjd) {
    this.set(rjd + 2400000.0);
  }

  public JulianDateStamp getModifiedJulianDate() {
    return new JulianDateStamp(this.integer - 2400000, this.fraction - 0.5);
  }

  public void setModifiedJulianDate(final double mjd) {
    this.set(mjd + 2400000.5);
  }

  public JulianDateStamp getTruncatedJulianDate() {
    return new JulianDateStamp(this.integer - 2440000, this.fraction - 0.5);
  }

  public void setTruncatedJulianDate(final double tjd) {
    this.set(tjd + 2440000.5);
  }
}
