package io.ll.warden.utils.jodd;

public class Iso8601JdtFormatter extends AbstractFormatter
{
  public Iso8601JdtFormatter() {
    super();
    this.preparePatterns(new String[] { "YYYY", "MM", "DD", "D", "MML", "MMS", "DL", "DS", "hh", "mm", "ss", "mss", "DDD", "WW", "WWW", "W", "E", "TZL", "TZS" });
  }

  protected String convertPattern(final int patternIndex, final JDateTime jdt) {
    final DateFormatSymbolsEx dfs = LocaleUtil.getDateFormatSymbols(jdt.getLocale());
    switch (patternIndex) {
      case 0: {
        return this.printPad4(jdt.getYear());
      }
      case 1: {
        return this.print2(jdt.getMonth());
      }
      case 2: {
        return this.print2(jdt.getDay());
      }
      case 3: {
        return Integer.toString(jdt.getDayOfWeek());
      }
      case 4: {
        return dfs.getMonth(jdt.getMonth() - 1);
      }
      case 5: {
        return dfs.getShortMonth(jdt.getMonth() - 1);
      }
      case 6: {
        return dfs.getWeekday(jdt.getDayOfWeek() % 7 + 1);
      }
      case 7: {
        return dfs.getShortWeekday(jdt.getDayOfWeek() % 7 + 1);
      }
      case 8: {
        return this.print2(jdt.getHour());
      }
      case 9: {
        return this.print2(jdt.getMinute());
      }
      case 10: {
        return this.print2(jdt.getSecond());
      }
      case 11: {
        return this.print3(jdt.getMillisecond());
      }
      case 12: {
        return this.print3(jdt.getDayOfYear());
      }
      case 13: {
        return this.print2(jdt.getWeekOfYear());
      }
      case 14: {
        return 'W' + this.print2(jdt.getWeekOfYear());
      }
      case 15: {
        return Integer.toString(jdt.getWeekOfMonth());
      }
      case 16: {
        return (jdt.getEra() == 1) ? dfs.getAdEra() : dfs.getBcEra();
      }
      case 17: {
        return jdt.getTimeZone().getDisplayName(jdt.isInDaylightTime(), 1, jdt.getLocale());
      }
      case 18: {
        return jdt.getTimeZone().getDisplayName(jdt.isInDaylightTime(), 0, jdt.getLocale());
      }
      default: {
        return new String(this.patterns[patternIndex]);
      }
    }
  }

  protected void parseValue(final int patternIndex, final String value, final DateTimeStamp destination) {
    final int v = Integer.parseInt(value);
    switch (patternIndex) {
      case 0: {
        destination.year = v;
        break;
      }
      case 1: {
        destination.month = v;
        break;
      }
      case 2: {
        destination.day = v;
        break;
      }
      case 8: {
        destination.hour = v;
        break;
      }
      case 9: {
        destination.minute = v;
        break;
      }
      case 10: {
        destination.second = v;
        break;
      }
      case 11: {
        destination.millisecond = v;
        break;
      }
      default: {
        throw new IllegalArgumentException("Invalid template: " + new String(this.patterns[patternIndex]));
      }
    }
  }
}

