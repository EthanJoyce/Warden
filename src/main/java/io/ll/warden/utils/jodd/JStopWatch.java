package io.ll.warden.utils.jodd;

import java.util.*;

public class JStopWatch
{
  protected String name;
  protected long startTime;
  protected long stopTime;
  protected long spanTime;
  protected long totalTime;
  protected boolean running;
  protected List<long[]> laps;

  public JStopWatch() {
    this("#jStopWatch");
  }

  public JStopWatch(final String name) {
    super();
    this.name = name;
    this.start();
  }

  public String getName() {
    return this.name;
  }

  public boolean isRunning() {
    return this.running;
  }

  public long start() {
    if (!this.running) {
      this.startTime = System.currentTimeMillis();
      this.running = true;
    }
    return this.startTime;
  }

  public long restart() {
    this.startTime = System.currentTimeMillis();
    this.running = true;
    return this.startTime;
  }

  public long stop() {
    if (this.running) {
      this.stopTime = System.currentTimeMillis();
      if (this.laps != null) {
        this.lap(this.stopTime);
      }
      this.spanTime = this.stopTime - this.startTime;
      this.totalTime += this.stopTime - this.startTime;
      this.running = false;
    }
    return this.spanTime;
  }

  public long elapsed() {
    return System.currentTimeMillis() - this.startTime;
  }

  public long total() {
    this.stop();
    return this.totalTime;
  }

  public long span() {
    this.stop();
    return this.spanTime;
  }

  public long lap() {
    return this.lap(System.currentTimeMillis());
  }

  protected long lap(final long lap) {
    if (!this.running) {
      return 0L;
    }
    final long lapSpanTime = lap - this.startTime;
    long lapTime;
    if (this.laps == null) {
      lapTime = lapSpanTime;
      this.laps = new ArrayList<long[]>();
    }
    else {
      final long[] previous = this.laps.get(this.laps.size() - 1);
      lapTime = lap - previous[2];
    }
    this.laps.add(new long[] { lapTime, lapSpanTime, lap });
    return lapTime;
  }

  public int totalLaps() {
    if (this.laps == null) {
      return 0;
    }
    return this.laps.size();
  }

  public long[] getLapTimes(final int index) {
    if (this.laps == null) {
      return null;
    }
    if (index <= 0 || index > this.laps.size()) {
      return null;
    }
    return this.laps.get(index - 1);
  }

  public String toString() {
    final long elapsed = this.elapsed();
    final StringBuilder sb = new StringBuilder();
    sb.append("JStopWatch ").append(this.name).append(this.running ? " is running." : "").append('\n');
    if (this.running) {
      sb.append("elapsed: ").append(formatTimeSpan(elapsed));
    }
    else {
      if (this.spanTime != this.totalTime) {
        sb.append("span:  ").append(formatTimeSpan(this.spanTime)).append('\n');
      }
      sb.append("\ntotal: ").append(formatTimeSpan(this.totalTime));
    }
    if (this.laps != null) {
      if (!this.laps.isEmpty()) {
        sb.append('\n');
        sb.append("\n\t\t\tlap\t\telapsed\n");
      }
      for (int i = 0; i < this.laps.size(); ++i) {
        final long[] longs = this.laps.get(i);
        sb.append("  lap #").append(i + 1).append(':').append('\t');
        sb.append(formatTimeSpan(longs[0])).append('\t');
        sb.append(formatTimeSpan(longs[1])).append('\n');
      }
    }
    return sb.toString();
  }

  public static String formatTimeSpan(long millis) {
    long seconds = 0L;
    long minutes = 0L;
    long hours = 0L;
    if (millis > 1000L) {
      seconds = millis / 1000L;
      millis %= 1000L;
    }
    if (seconds > 60L) {
      minutes = seconds / 60L;
      seconds %= 60L;
    }
    if (minutes > 60L) {
      hours = minutes / 60L;
      minutes %= 60L;
    }
    final StringBuilder result = new StringBuilder(20);
    boolean out = false;
    if (hours > 0L) {
      result.append(hours).append(':');
      out = true;
    }
    if (out || minutes > 0L) {
      if (minutes < 10L) {
        result.append('0');
      }
      result.append(minutes).append(':');
    }
    if (seconds < 10L) {
      result.append('0');
    }
    result.append(seconds).append('.');
    if (millis < 10L) {
      result.append('0');
    }
    if (millis < 100L) {
      result.append('0');
    }
    result.append(millis);
    return result.toString();
  }
}
