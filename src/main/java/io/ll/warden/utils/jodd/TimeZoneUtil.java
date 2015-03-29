package io.ll.warden.utils.jodd;

import java.util.*;

public class TimeZoneUtil
{
  public static int getRawOffsetDifference(final TimeZone from, final TimeZone to) {
    final int offsetBefore = from.getRawOffset();
    final int offsetAfter = to.getRawOffset();
    return offsetAfter - offsetBefore;
  }

  public static int getOffsetDifference(final long now, final TimeZone from, final TimeZone to) {
    final int offsetBefore = from.getOffset(now);
    final int offsetAfter = to.getOffset(now);
    return offsetAfter - offsetBefore;
  }

  public static int getOffset(final JDateTime jdt, final TimeZone tz) {
    return tz.getOffset(jdt.getEra(), jdt.getYear(), jdt.getMonth() - 1, jdt.getDay(), TimeUtil.toCalendarDayOfWeek(jdt.getDayOfWeek()), jdt.getMillisOfDay());
  }

  public static int getOffsetDifference(final JDateTime jdt, final TimeZone from, final TimeZone to) {
    final int offsetBefore = getOffset(jdt, from);
    final int offsetAfter = getOffset(jdt, to);
    return offsetAfter - offsetBefore;
  }
}

