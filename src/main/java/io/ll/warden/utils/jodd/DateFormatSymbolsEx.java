package io.ll.warden.utils.jodd;

import java.util.*;
import java.text.*;

public class DateFormatSymbolsEx
{
  protected final String[] months;
  protected final String[] shortMonths;
  protected final String[] weekdays;
  protected final String[] shortWeekdays;
  protected final String[] eras;
  protected final String[] ampms;

  public DateFormatSymbolsEx(final Locale locale) {
    super();
    final DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
    this.months = dateFormatSymbols.getMonths();
    this.shortMonths = dateFormatSymbols.getShortMonths();
    this.weekdays = dateFormatSymbols.getWeekdays();
    this.shortWeekdays = dateFormatSymbols.getShortWeekdays();
    this.eras = dateFormatSymbols.getEras();
    this.ampms = dateFormatSymbols.getAmPmStrings();
  }

  public String getMonth(final int i) {
    return this.months[i];
  }

  public String getShortMonth(final int i) {
    return this.shortMonths[i];
  }

  public String getWeekday(final int i) {
    return this.weekdays[i];
  }

  public String getShortWeekday(final int i) {
    return this.shortWeekdays[i];
  }

  public String getBcEra() {
    return this.eras[0];
  }

  public String getAdEra() {
    return this.eras[1];
  }

  public String getAM() {
    return this.ampms[0];
  }

  public String getPM() {
    return this.ampms[1];
  }
}

