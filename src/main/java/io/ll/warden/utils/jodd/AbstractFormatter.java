package io.ll.warden.utils.jodd;

import java.io.*;

public abstract class AbstractFormatter implements JdtFormatter
{
  protected char[][] patterns;
  protected char escapeChar;

  public AbstractFormatter() {
    super();
    this.escapeChar = '\'';
  }

  protected void preparePatterns(final String[] spat) {
    this.patterns = new char[spat.length][];
    for (int i = 0; i < spat.length; ++i) {
      this.patterns[i] = spat[i].toCharArray();
    }
  }

  protected int findPattern(final char[] format, final int i) {
    final int frmtc_len = format.length;
    int lastn = -1;
    int maxLen = 0;
    for (int n = 0; n < this.patterns.length; ++n) {
      final char[] curr = this.patterns[n];
      if (i <= frmtc_len - curr.length) {
        boolean match = true;
        for (int delta = 0; delta < curr.length; ++delta) {
          if (curr[delta] != format[i + delta]) {
            match = false;
            break;
          }
        }
        if (match && this.patterns[n].length > maxLen) {
          lastn = n;
          maxLen = this.patterns[n].length;
        }
      }
    }
    return lastn;
  }

  protected abstract String convertPattern(final int p0, final JDateTime p1);

  public String convert(final JDateTime jdt, final String format) {
    final char[] fmtc = format.toCharArray();
    final int fmtc_len = fmtc.length;
    final StringBuilder result = new StringBuilder(fmtc_len);
    int i = 0;
    while (i < fmtc_len) {
      if (fmtc[i] == this.escapeChar) {
        int end;
        for (end = i + 1; end < fmtc_len; ++end) {
          if (fmtc[end] == this.escapeChar) {
            if (end + 1 < fmtc_len) {
              ++end;
              if (fmtc[end] != this.escapeChar) {
                break;
              }
              result.append(this.escapeChar);
            }
          }
          else {
            result.append(fmtc[end]);
          }
        }
        i = end;
      }
      else {
        final int n = this.findPattern(fmtc, i);
        if (n != -1) {
          result.append(this.convertPattern(n, jdt));
          i += this.patterns[n].length;
        }
        else {
          result.append(fmtc[i]);
          ++i;
        }
      }
    }
    return result.toString();
  }

  protected abstract void parseValue(final int p0, final String p1, final DateTimeStamp p2);

  public DateTimeStamp parse(final String value, final String format) {
    final char[] valueChars = value.toCharArray();
    final char[] formatChars = format.toCharArray();
    int i = 0;
    int j = 0;
    final int valueLen = valueChars.length;
    final int formatLen = formatChars.length;
    boolean useSeparators = true;
    if (valueLen == formatLen) {
      useSeparators = false;
      for (final char valueChar : valueChars) {
        if (!CharUtil.isDigit(valueChar)) {
          useSeparators = true;
          break;
        }
      }
    }
    final DateTimeStamp time = new DateTimeStamp();
    final StringBuilder sb = new StringBuilder();
    while (true) {
      final int n = this.findPattern(formatChars, i);
      if (n != -1) {
        final int patternLen = this.patterns[n].length;
        i += patternLen;
        sb.setLength(0);
        if (!useSeparators) {
          for (int k = 0; k < patternLen; ++k) {
            sb.append(valueChars[j++]);
          }
        }
        else {
          char next = '\uffff';
          if (i < formatLen) {
            next = formatChars[i];
          }
          while (j < valueLen && valueChars[j] != next) {
            final char scj = valueChars[j];
            if (scj != ' ' && scj != '\t') {
              sb.append(valueChars[j]);
            }
            ++j;
          }
        }
        this.parseValue(n, sb.toString(), time);
      }
      else {
        if (!useSeparators) {
          throw new IllegalArgumentException("Invalid value: " + value);
        }
        if (formatChars[i] == valueChars[j]) {
          ++j;
        }
        ++i;
      }
      if (i != formatLen && j != valueLen) {
        continue;
      }
      return time;
    }
  }

  protected String print2(final int value) {
    if (value < 0) {
      throw new IllegalArgumentException("Value must be positive: " + value);
    }
    if (value < 10) {
      return '0' + Integer.toString(value);
    }
    if (value < 100) {
      return Integer.toString(value);
    }
    throw new IllegalArgumentException("Value too big: " + value);
  }

  protected String print3(final int value) {
    if (value < 0) {
      throw new IllegalArgumentException("Value must be positive: " + value);
    }
    if (value < 10) {
      return "00" + Integer.toString(value);
    }
    if (value < 100) {
      return '0' + Integer.toString(value);
    }
    if (value < 1000) {
      return Integer.toString(value);
    }
    throw new IllegalArgumentException("Value too big: " + value);
  }

  protected String printPad4(int value) {
    final char[] result = new char[4];
    int count = 0;
    if (value < 0) {
      result[count++] = '-';
      value = -value;
    }
    final String str = Integer.toString(value);
    if (value < 10) {
      result[count++] = '0';
      result[count++] = '0';
      result[count++] = '0';
      result[count++] = str.charAt(0);
    }
    else if (value < 100) {
      result[count++] = '0';
      result[count++] = '0';
      result[count++] = str.charAt(0);
      result[count++] = str.charAt(1);
    }
    else if (value < 1000) {
      result[count++] = '0';
      result[count++] = str.charAt(0);
      result[count++] = str.charAt(1);
      result[count++] = str.charAt(2);
    }
    else {
      if (count > 0) {
        return '-' + str;
      }
      return str;
    }
    return new String(result, 0, count);
  }
}

