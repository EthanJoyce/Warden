package io.ll.warden.utils.jodd;

import java.io.*;

public class CharUtil
{
  public static final char[] HEX_CHARS;

  public static char toChar(final byte b) {
    return (char)(b & 0xFF);
  }

  public static byte[] toSimpleByteArray(final char[] carr) {
    final byte[] barr = new byte[carr.length];
    for (int i = 0; i < carr.length; ++i) {
      barr[i] = (byte)carr[i];
    }
    return barr;
  }

  public static byte[] toSimpleByteArray(final CharSequence charSequence) {
    final byte[] barr = new byte[charSequence.length()];
    for (int i = 0; i < barr.length; ++i) {
      barr[i] = (byte)charSequence.charAt(i);
    }
    return barr;
  }

  public static char[] toSimpleCharArray(final byte[] barr) {
    final char[] carr = new char[barr.length];
    for (int i = 0; i < barr.length; ++i) {
      carr[i] = (char)(barr[i] & 0xFF);
    }
    return carr;
  }

  public static int toAscii(final char c) {
    if (c <= '\u00ff') {
      return c;
    }
    return 63;
  }

  public static byte[] toAsciiByteArray(final char[] carr) {
    final byte[] barr = new byte[carr.length];
    for (int i = 0; i < carr.length; ++i) {
      barr[i] = (byte)((carr[i] <= '\u00ff') ? carr[i] : '?');
    }
    return barr;
  }

  public static byte[] toAsciiByteArray(final CharSequence charSequence) {
    final byte[] barr = new byte[charSequence.length()];
    for (int i = 0; i < barr.length; ++i) {
      final char c = charSequence.charAt(i);
      barr[i] = (byte)((c <= '\u00ff') ? c : '?');
    }
    return barr;
  }

  public static byte[] toRawByteArray(final char[] carr) {
    final byte[] barr = new byte[carr.length << 1];
    int i = 0;
    int bpos = 0;
    while (i < carr.length) {
      final char c = carr[i];
      barr[bpos++] = (byte)((c & '\uff00') >> 8);
      barr[bpos++] = (byte)(c & '\u00ff');
      ++i;
    }
    return barr;
  }

  public static char[] toRawCharArray(final byte[] barr) {
    int carrLen = barr.length >> 1;
    if (carrLen << 1 < barr.length) {
      ++carrLen;
    }
    final char[] carr = new char[carrLen];
    int i = 0;
    int j = 0;
    while (i < barr.length) {
      char c = (char)(barr[i] << 8);
      if (++i != barr.length) {
        c += (char)(barr[i] & 0xFF);
        ++i;
      }
      carr[j++] = c;
    }
    return carr;
  }

  public static byte[] toByteArray(final char[] carr) throws UnsupportedEncodingException {
    return new String(carr).getBytes();
  }

  public static byte[] toByteArray(final char[] carr, final String charset) throws UnsupportedEncodingException {
    return new String(carr).getBytes(charset);
  }

  public static char[] toCharArray(final byte[] barr) throws UnsupportedEncodingException {
    return new String(barr).toCharArray();
  }

  public static char[] toCharArray(final byte[] barr, final String charset) throws UnsupportedEncodingException {
    return new String(barr, charset).toCharArray();
  }

  public static boolean equalsOne(final char c, final char[] match) {
    for (final char aMatch : match) {
      if (c == aMatch) {
        return true;
      }
    }
    return false;
  }

  public static int findFirstEqual(final char[] source, final int index, final char[] match) {
    for (int i = index; i < source.length; ++i) {
      if (equalsOne(source[i], match)) {
        return i;
      }
    }
    return -1;
  }

  public static int findFirstEqual(final char[] source, final int index, final char match) {
    for (int i = index; i < source.length; ++i) {
      if (source[i] == match) {
        return i;
      }
    }
    return -1;
  }

  public static int findFirstDiff(final char[] source, final int index, final char[] match) {
    for (int i = index; i < source.length; ++i) {
      if (!equalsOne(source[i], match)) {
        return i;
      }
    }
    return -1;
  }

  public static int findFirstDiff(final char[] source, final int index, final char match) {
    for (int i = index; i < source.length; ++i) {
      if (source[i] != match) {
        return i;
      }
    }
    return -1;
  }

  public static boolean isWhitespace(final char c) {
    return c <= ' ';
  }

  public static boolean isLowercaseAlpha(final char c) {
    return c >= 'a' && c <= 'z';
  }

  public static boolean isUppercaseAlpha(final char c) {
    return c >= 'A' && c <= 'Z';
  }

  public static boolean isAlphaOrDigit(final char c) {
    return isDigit(c) || isAlpha(c);
  }

  public static boolean isWordChar(final char c) {
    return isDigit(c) || isAlpha(c) || c == '_';
  }

  public static boolean isPropertyNameChar(final char c) {
    return isDigit(c) || isAlpha(c) || c == '_' || c == '.' || c == '[' || c == ']';
  }

  public static boolean isAlpha(final char c) {
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
  }

  public static boolean isDigit(final char c) {
    return c >= '0' && c <= '9';
  }

  public static boolean isHexDigit(final char c) {
    return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
  }

  public static boolean isGenericDelimiter(final int c) {
    switch (c) {
      case 35:
      case 47:
      case 58:
      case 63:
      case 64:
      case 91:
      case 93: {
        return true;
      }
      default: {
        return false;
      }
    }
  }

  protected static boolean isSubDelimiter(final int c) {
    switch (c) {
      case 33:
      case 36:
      case 38:
      case 39:
      case 40:
      case 41:
      case 42:
      case 43:
      case 44:
      case 59:
      case 61: {
        return true;
      }
      default: {
        return false;
      }
    }
  }

  protected static boolean isReserved(final char c) {
    return isGenericDelimiter(c) || isSubDelimiter(c);
  }

  protected static boolean isUnreserved(final char c) {
    return isAlpha(c) || isDigit(c) || c == '-' || c == '.' || c == '_' || c == '~';
  }

  protected static boolean isPchar(final char c) {
    return isUnreserved(c) || isSubDelimiter(c) || c == ':' || c == '@';
  }

  public static char toUpperAscii(char c) {
    if (isLowercaseAlpha(c)) {
      c -= ' ';
    }
    return c;
  }

  public static char toLowerAscii(char c) {
    if (isUppercaseAlpha(c)) {
      c += ' ';
    }
    return c;
  }

  public static int hex2int(final char c) {
    switch (c) {
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9': {
        return c - '0';
      }
      case 'A':
      case 'B':
      case 'C':
      case 'D':
      case 'E':
      case 'F': {
        return c - '7';
      }
      case 'a':
      case 'b':
      case 'c':
      case 'd':
      case 'e':
      case 'f': {
        return c - 'W';
      }
      default: {
        throw new IllegalArgumentException("Not a hex: " + c);
      }
    }
  }

  public static char int2hex(final int i) {
    return CharUtil.HEX_CHARS[i];
  }

  static {
    HEX_CHARS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
  }
}

