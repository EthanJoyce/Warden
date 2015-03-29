package io.ll.warden.utils.jodd;

import java.util.*;
import java.io.*;

public class StringUtil
{
  public static String replace(final String s, final String sub, final String with) {
    int c = 0;
    int i = s.indexOf(sub, c);
    if (i == -1) {
      return s;
    }
    final int length = s.length();
    final StringBuilder sb = new StringBuilder(length + with.length());
    do {
      sb.append(s.substring(c, i));
      sb.append(with);
      c = i + sub.length();
    } while ((i = s.indexOf(sub, c)) != -1);
    if (c < length) {
      sb.append(s.substring(c, length));
    }
    return sb.toString();
  }

  public static String replaceChar(final String s, final char sub, final char with) {
    final int startIndex = s.indexOf(sub);
    if (startIndex == -1) {
      return s;
    }
    final char[] str = s.toCharArray();
    for (int i = startIndex; i < str.length; ++i) {
      if (str[i] == sub) {
        str[i] = with;
      }
    }
    return new String(str);
  }

  public static String replaceChars(final String s, final char[] sub, final char[] with) {
    final char[] str = s.toCharArray();
    for (int i = 0; i < str.length; ++i) {
      final char c = str[i];
      for (int j = 0; j < sub.length; ++j) {
        if (c == sub[j]) {
          str[i] = with[j];
          break;
        }
      }
    }
    return new String(str);
  }

  public static String replaceFirst(final String s, final String sub, final String with) {
    final int i = s.indexOf(sub);
    if (i == -1) {
      return s;
    }
    return s.substring(0, i) + with + s.substring(i + sub.length());
  }

  public static String replaceFirst(final String s, final char sub, final char with) {
    final int index = s.indexOf(sub);
    if (index == -1) {
      return s;
    }
    final char[] str = s.toCharArray();
    str[index] = with;
    return new String(str);
  }

  public static String replaceLast(final String s, final String sub, final String with) {
    final int i = s.lastIndexOf(sub);
    if (i == -1) {
      return s;
    }
    return s.substring(0, i) + with + s.substring(i + sub.length());
  }

  public static String replaceLast(final String s, final char sub, final char with) {
    final int index = s.lastIndexOf(sub);
    if (index == -1) {
      return s;
    }
    final char[] str = s.toCharArray();
    str[index] = with;
    return new String(str);
  }

  public static String remove(final String s, final String sub) {
    int c = 0;
    final int sublen = sub.length();
    if (sublen == 0) {
      return s;
    }
    int i = s.indexOf(sub, c);
    if (i == -1) {
      return s;
    }
    final StringBuilder sb = new StringBuilder(s.length());
    do {
      sb.append(s.substring(c, i));
      c = i + sublen;
    } while ((i = s.indexOf(sub, c)) != -1);
    if (c < s.length()) {
      sb.append(s.substring(c, s.length()));
    }
    return sb.toString();
  }

  public static String removeChars(final String src, final String chars) {
    final int i = src.length();
    final StringBuilder sb = new StringBuilder(i);
    for (int j = 0; j < i; ++j) {
      final char c = src.charAt(j);
      if (chars.indexOf(c) == -1) {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  public static String removeChars(final String src, final char... chars) {
    final int i = src.length();
    final StringBuilder sb = new StringBuilder(i);
    int j = 0;
    Label_0017:
    while (j < i) {
      final char c = src.charAt(j);
      while (true) {
        for (final char aChar : chars) {
          if (c == aChar) {
            ++j;
            continue Label_0017;
          }
        }
        sb.append(c);
        continue;
      }
    }
    return sb.toString();
  }

  public static String remove(final String string, final char ch) {
    final int stringLen = string.length();
    final char[] result = new char[stringLen];
    int offset = 0;
    for (int i = 0; i < stringLen; ++i) {
      final char c = string.charAt(i);
      if (c != ch) {
        result[offset] = c;
        ++offset;
      }
    }
    if (offset == stringLen) {
      return string;
    }
    return new String(result, 0, offset);
  }

  public static boolean equals(final String s1, final String s2) {
    return Util.equals(s1, s2);
  }

  public static boolean isEmpty(final CharSequence string) {
    return string == null || string.length() == 0;
  }

  public static boolean isAllEmpty(final String... strings) {
    for (final String string : strings) {
      if (!isEmpty(string)) {
        return false;
      }
    }
    return true;
  }

  public static boolean isBlank(final CharSequence string) {
    return string == null || containsOnlyWhitespaces(string);
  }

  public static boolean isNotBlank(final String string) {
    return string != null && !containsOnlyWhitespaces(string);
  }

  public static boolean isAllBlank(final String... strings) {
    for (final String string : strings) {
      if (!isBlank(string)) {
        return false;
      }
    }
    return true;
  }

  public static boolean containsOnlyWhitespaces(final CharSequence string) {
    for (int size = string.length(), i = 0; i < size; ++i) {
      final char c = string.charAt(i);
      if (!CharUtil.isWhitespace(c)) {
        return false;
      }
    }
    return true;
  }

  public static boolean containsOnlyDigits(final CharSequence string) {
    for (int size = string.length(), i = 0; i < size; ++i) {
      final char c = string.charAt(i);
      if (!CharUtil.isDigit(c)) {
        return false;
      }
    }
    return true;
  }

  public static boolean containsOnlyDigitsAndSigns(final CharSequence string) {
    for (int size = string.length(), i = 0; i < size; ++i) {
      final char c = string.charAt(i);
      if (!CharUtil.isDigit(c) && c != '-' && c != '+') {
        return false;
      }
    }
    return true;
  }

  public static boolean isNotEmpty(final CharSequence string) {
    return string != null && string.length() > 0;
  }

  public static String toString(final Object value) {
    if (value == null) {
      return null;
    }
    return value.toString();
  }

  public static String toSafeString(final Object value) {
    if (value == null) {
      return "";
    }
    return value.toString();
  }

  public static String toPrettyString(final Object value) {
    if (value == null) {
      return "null";
    }
    final Class<?> type = value.getClass();
    if (type.isArray()) {
      final Class componentType = type.getComponentType();
      if (componentType.isPrimitive()) {
        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        if (componentType == Integer.TYPE) {
          sb.append(ArraysUtil.toString((int[])value));
        }
        else if (componentType == Long.TYPE) {
          sb.append(ArraysUtil.toString((long[])value));
        }
        else if (componentType == Double.TYPE) {
          sb.append(ArraysUtil.toString((double[])value));
        }
        else if (componentType == Float.TYPE) {
          sb.append(ArraysUtil.toString((float[])value));
        }
        else if (componentType == Boolean.TYPE) {
          sb.append(ArraysUtil.toString((boolean[])value));
        }
        else if (componentType == Short.TYPE) {
          sb.append(ArraysUtil.toString((short[])value));
        }
        else {
          if (componentType != Byte.TYPE) {
            throw new IllegalArgumentException();
          }
          sb.append(ArraysUtil.toString((byte[])value));
        }
        sb.append(']');
        return sb.toString();
      }
      final StringBuilder sb = new StringBuilder();
      sb.append('[');
      final Object[] array = (Object[])value;
      for (int i = 0; i < array.length; ++i) {
        if (i > 0) {
          sb.append(',');
        }
        sb.append(toPrettyString(array[i]));
      }
      sb.append(']');
      return sb.toString();
    }
    else {
      if (value instanceof Iterable) {
        final Iterable iterable = (Iterable)value;
        final StringBuilder sb = new StringBuilder();
        sb.append('{');
        int j = 0;
        for (final Object o : iterable) {
          if (j > 0) {
            sb.append(',');
          }
          sb.append(toPrettyString(o));
          ++j;
        }
        sb.append('}');
        return sb.toString();
      }
      return value.toString();
    }
  }

  public static String[] toStringArray(final Object value) {
    if (value == null) {
      return new String[0];
    }
    final Class<?> type = value.getClass();
    if (!type.isArray()) {
      return new String[] { value.toString() };
    }
    final Class componentType = type.getComponentType();
    if (!componentType.isPrimitive()) {
      return ArraysUtil.toStringArray((Object[])value);
    }
    if (componentType == Integer.TYPE) {
      return ArraysUtil.toStringArray((int[])value);
    }
    if (componentType == Long.TYPE) {
      return ArraysUtil.toStringArray((long[])value);
    }
    if (componentType == Double.TYPE) {
      return ArraysUtil.toStringArray((double[])value);
    }
    if (componentType == Float.TYPE) {
      return ArraysUtil.toStringArray((float[])value);
    }
    if (componentType == Boolean.TYPE) {
      return ArraysUtil.toStringArray((boolean[])value);
    }
    if (componentType == Short.TYPE) {
      return ArraysUtil.toStringArray((short[])value);
    }
    if (componentType == Byte.TYPE) {
      return ArraysUtil.toStringArray((byte[])value);
    }
    throw new IllegalArgumentException();
  }

  public static String capitalize(final String str) {
    return changeFirstCharacterCase(true, str);
  }

  public static String uncapitalize(final String str) {
    return changeFirstCharacterCase(false, str);
  }

  private static String changeFirstCharacterCase(final boolean capitalize, final String string) {
    final int strLen = string.length();
    if (strLen == 0) {
      return string;
    }
    final char ch = string.charAt(0);
    char modifiedCh;
    if (capitalize) {
      modifiedCh = Character.toUpperCase(ch);
    }
    else {
      modifiedCh = Character.toLowerCase(ch);
    }
    if (modifiedCh == ch) {
      return string;
    }
    final char[] chars = string.toCharArray();
    chars[0] = modifiedCh;
    return new String(chars);
  }

  public static String decapitalize(final String name) {
    if (name.length() == 0) {
      return name;
    }
    if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) && Character.isUpperCase(name.charAt(0))) {
      return name;
    }
    final char[] chars = name.toCharArray();
    final char c = chars[0];
    final char modifiedChar = Character.toLowerCase(c);
    if (modifiedChar == c) {
      return name;
    }
    chars[0] = modifiedChar;
    return new String(chars);
  }

  public static String title(final String string) {
    final char[] chars = string.toCharArray();
    boolean wasWhitespace = true;
    for (int i = 0; i < chars.length; ++i) {
      final char c = chars[i];
      if (CharUtil.isWhitespace(c)) {
        wasWhitespace = true;
      }
      else {
        if (wasWhitespace) {
          chars[i] = Character.toUpperCase(c);
        }
        else {
          chars[i] = Character.toLowerCase(c);
        }
        wasWhitespace = false;
      }
    }
    return new String(chars);
  }

  public static String truncate(String string, final int length) {
    if (string.length() > length) {
      string = string.substring(0, length);
    }
    return string;
  }

  public static String substring(final String string, int fromIndex, int toIndex) {
    final int len = string.length();
    if (fromIndex < 0) {
      fromIndex += len;
      if (toIndex == 0) {
        toIndex = len;
      }
    }
    if (toIndex < 0) {
      toIndex += len;
    }
    if (fromIndex < 0) {
      fromIndex = 0;
    }
    if (toIndex > len) {
      toIndex = len;
    }
    if (fromIndex >= toIndex) {
      return "";
    }
    return string.substring(fromIndex, toIndex);
  }

  public static String[] split(final String src, final String delimiter) {
    final int maxparts = src.length() / delimiter.length() + 2;
    final int[] positions = new int[maxparts];
    final int dellen = delimiter.length();
    int j = 0;
    int count = 0;
    positions[0] = -dellen;
    int i;
    while ((i = src.indexOf(delimiter, j)) != -1) {
      ++count;
      positions[count] = i;
      j = i + dellen;
    }
    ++count;
    positions[count] = src.length();
    final String[] result = new String[count];
    for (i = 0; i < count; ++i) {
      result[i] = src.substring(positions[i] + dellen, positions[i + 1]);
    }
    return result;
  }

  public static String[] splitc(final String src, final String d) {
    if (d.length() == 0 || src.length() == 0) {
      return new String[] { src };
    }
    return splitc(src, d.toCharArray());
  }

  public static String[] splitc(final String src, final char[] delimiters) {
    if (delimiters.length == 0 || src.length() == 0) {
      return new String[] { src };
    }
    final char[] srcc = src.toCharArray();
    final int maxparts = srcc.length + 1;
    final int[] start = new int[maxparts];
    final int[] end = new int[maxparts];
    int count = 0;
    start[0] = 0;
    int s = 0;
    if (CharUtil.equalsOne(srcc[0], delimiters)) {
      end[0] = 0;
      ++count;
      s = CharUtil.findFirstDiff(srcc, 1, delimiters);
      if (s == -1) {
        return new String[] { "", "" };
      }
      start[1] = s;
    }
    while (true) {
      final int e = CharUtil.findFirstEqual(srcc, s, delimiters);
      if (e == -1) {
        end[count] = srcc.length;
        break;
      }
      end[count] = e;
      ++count;
      s = CharUtil.findFirstDiff(srcc, e, delimiters);
      if (s == -1) {
        start[count] = (end[count] = srcc.length);
        break;
      }
      start[count] = s;
    }
    final String[] result = new String[++count];
    for (int i = 0; i < count; ++i) {
      result[i] = src.substring(start[i], end[i]);
    }
    return result;
  }

  public static String[] splitc(final String src, final char delimiter) {
    if (src.length() == 0) {
      return new String[] { "" };
    }
    final char[] srcc = src.toCharArray();
    final int maxparts = srcc.length + 1;
    final int[] start = new int[maxparts];
    final int[] end = new int[maxparts];
    int count = 0;
    start[0] = 0;
    int s = 0;
    if (srcc[0] == delimiter) {
      end[0] = 0;
      ++count;
      s = CharUtil.findFirstDiff(srcc, 1, delimiter);
      if (s == -1) {
        return new String[] { "", "" };
      }
      start[1] = s;
    }
    while (true) {
      final int e = CharUtil.findFirstEqual(srcc, s, delimiter);
      if (e == -1) {
        end[count] = srcc.length;
        break;
      }
      end[count] = e;
      ++count;
      s = CharUtil.findFirstDiff(srcc, e, delimiter);
      if (s == -1) {
        start[count] = (end[count] = srcc.length);
        break;
      }
      start[count] = s;
    }
    final String[] result = new String[++count];
    for (int i = 0; i < count; ++i) {
      result[i] = src.substring(start[i], end[i]);
    }
    return result;
  }

  public static String compressChars(final String s, final char c) {
    final int len = s.length();
    final StringBuilder sb = new StringBuilder(len);
    boolean wasChar = false;
    for (int i = 0; i < len; ++i) {
      final char c2 = s.charAt(i);
      if (c2 == c) {
        if (wasChar) {
          continue;
        }
        wasChar = true;
      }
      else {
        wasChar = false;
      }
      sb.append(c2);
    }
    if (sb.length() == len) {
      return s;
    }
    return sb.toString();
  }

  public static int indexOf(final String src, final String sub, int startIndex, int endIndex) {
    if (startIndex < 0) {
      startIndex = 0;
    }
    final int srclen = src.length();
    if (endIndex > srclen) {
      endIndex = srclen;
    }
    final int sublen = sub.length();
    if (sublen == 0) {
      return (startIndex > srclen) ? srclen : startIndex;
    }
    final int total = endIndex - sublen + 1;
    final char c = sub.charAt(0);
    Label_0130:
    for (int i = startIndex; i < total; ++i) {
      if (src.charAt(i) == c) {
        for (int j = 1, k = i + 1; j < sublen; ++j, ++k) {
          if (sub.charAt(j) != src.charAt(k)) {
            continue Label_0130;
          }
        }
        return i;
      }
    }
    return -1;
  }

  public static int indexOf(final String src, final char c, int startIndex, int endIndex) {
    if (startIndex < 0) {
      startIndex = 0;
    }
    final int srclen = src.length();
    if (endIndex > srclen) {
      endIndex = srclen;
    }
    for (int i = startIndex; i < endIndex; ++i) {
      if (src.charAt(i) == c) {
        return i;
      }
    }
    return -1;
  }

  public static int indexOfIgnoreCase(final String src, char c, int startIndex, int endIndex) {
    if (startIndex < 0) {
      startIndex = 0;
    }
    final int srclen = src.length();
    if (endIndex > srclen) {
      endIndex = srclen;
    }
    c = Character.toLowerCase(c);
    for (int i = startIndex; i < endIndex; ++i) {
      if (Character.toLowerCase(src.charAt(i)) == c) {
        return i;
      }
    }
    return -1;
  }

  public static int indexOfIgnoreCase(final String src, final String subS) {
    return indexOfIgnoreCase(src, subS, 0, src.length());
  }

  public static int indexOfIgnoreCase(final String src, final String subS, final int startIndex) {
    return indexOfIgnoreCase(src, subS, startIndex, src.length());
  }

  public static int indexOfIgnoreCase(final String src, String sub, int startIndex, int endIndex) {
    if (startIndex < 0) {
      startIndex = 0;
    }
    final int srclen = src.length();
    if (endIndex > srclen) {
      endIndex = srclen;
    }
    final int sublen = sub.length();
    if (sublen == 0) {
      return (startIndex > srclen) ? srclen : startIndex;
    }
    sub = sub.toLowerCase();
    final int total = endIndex - sublen + 1;
    final char c = sub.charAt(0);
    Label_0145:
    for (int i = startIndex; i < total; ++i) {
      if (Character.toLowerCase(src.charAt(i)) == c) {
        for (int j = 1, k = i + 1; j < sublen; ++j, ++k) {
          final char source = Character.toLowerCase(src.charAt(k));
          if (sub.charAt(j) != source) {
            continue Label_0145;
          }
        }
        return i;
      }
    }
    return -1;
  }

  public static int lastIndexOfIgnoreCase(final String s, final String subS) {
    return lastIndexOfIgnoreCase(s, subS, s.length(), 0);
  }

  public static int lastIndexOfIgnoreCase(final String src, final String subS, final int startIndex) {
    return lastIndexOfIgnoreCase(src, subS, startIndex, 0);
  }

  public static int lastIndexOfIgnoreCase(final String src, String sub, int startIndex, int endIndex) {
    final int sublen = sub.length();
    final int srclen = src.length();
    if (sublen == 0) {
      return (startIndex > srclen) ? srclen : ((startIndex < -1) ? -1 : startIndex);
    }
    sub = sub.toLowerCase();
    final int total = srclen - sublen;
    if (total < 0) {
      return -1;
    }
    if (startIndex >= total) {
      startIndex = total;
    }
    if (endIndex < 0) {
      endIndex = 0;
    }
    final char c = sub.charAt(0);
    Label_0159:
    for (int i = startIndex; i >= endIndex; --i) {
      if (Character.toLowerCase(src.charAt(i)) == c) {
        for (int j = 1, k = i + 1; j < sublen; ++j, ++k) {
          final char source = Character.toLowerCase(src.charAt(k));
          if (sub.charAt(j) != source) {
            continue Label_0159;
          }
        }
        return i;
      }
    }
    return -1;
  }

  public static int lastIndexOf(final String src, final String sub, int startIndex, int endIndex) {
    final int sublen = sub.length();
    final int srclen = src.length();
    if (sublen == 0) {
      return (startIndex > srclen) ? srclen : ((startIndex < -1) ? -1 : startIndex);
    }
    final int total = srclen - sublen;
    if (total < 0) {
      return -1;
    }
    if (startIndex >= total) {
      startIndex = total;
    }
    if (endIndex < 0) {
      endIndex = 0;
    }
    final char c = sub.charAt(0);
    Label_0144:
    for (int i = startIndex; i >= endIndex; --i) {
      if (src.charAt(i) == c) {
        for (int j = 1, k = i + 1; j < sublen; ++j, ++k) {
          if (sub.charAt(j) != src.charAt(k)) {
            continue Label_0144;
          }
        }
        return i;
      }
    }
    return -1;
  }

  public static int lastIndexOf(final String src, final char c, int startIndex, int endIndex) {
    final int total = src.length() - 1;
    if (total < 0) {
      return -1;
    }
    if (startIndex >= total) {
      startIndex = total;
    }
    if (endIndex < 0) {
      endIndex = 0;
    }
    for (int i = startIndex; i >= endIndex; --i) {
      if (src.charAt(i) == c) {
        return i;
      }
    }
    return -1;
  }

  public static int lastIndexOfIgnoreCase(final String src, char c, int startIndex, int endIndex) {
    final int total = src.length() - 1;
    if (total < 0) {
      return -1;
    }
    if (startIndex >= total) {
      startIndex = total;
    }
    if (endIndex < 0) {
      endIndex = 0;
    }
    c = Character.toLowerCase(c);
    for (int i = startIndex; i >= endIndex; --i) {
      if (Character.toLowerCase(src.charAt(i)) == c) {
        return i;
      }
    }
    return -1;
  }

  public static int lastIndexOfWhitespace(final String src) {
    return lastIndexOfWhitespace(src, src.length(), 0);
  }

  public static int lastIndexOfWhitespace(final String src, final int startIndex) {
    return lastIndexOfWhitespace(src, startIndex, 0);
  }

  public static int lastIndexOfWhitespace(final String src, int startIndex, int endIndex) {
    final int total = src.length() - 1;
    if (total < 0) {
      return -1;
    }
    if (startIndex >= total) {
      startIndex = total;
    }
    if (endIndex < 0) {
      endIndex = 0;
    }
    for (int i = startIndex; i >= endIndex; --i) {
      if (Character.isWhitespace(src.charAt(i))) {
        return i;
      }
    }
    return -1;
  }

  public static int lastIndexOfNonWhitespace(final String src) {
    return lastIndexOfNonWhitespace(src, src.length(), 0);
  }

  public static int lastIndexOfNonWhitespace(final String src, final int startIndex) {
    return lastIndexOfNonWhitespace(src, startIndex, 0);
  }

  public static int lastIndexOfNonWhitespace(final String src, int startIndex, int endIndex) {
    final int total = src.length() - 1;
    if (total < 0) {
      return -1;
    }
    if (startIndex >= total) {
      startIndex = total;
    }
    if (endIndex < 0) {
      endIndex = 0;
    }
    for (int i = startIndex; i >= endIndex; --i) {
      if (!Character.isWhitespace(src.charAt(i))) {
        return i;
      }
    }
    return -1;
  }

  public static boolean startsWithIgnoreCase(final String src, final String subS) {
    return startsWithIgnoreCase(src, subS, 0);
  }

  public static boolean startsWithIgnoreCase(final String src, final String subS, final int startIndex) {
    final String sub = subS.toLowerCase();
    final int sublen = sub.length();
    if (startIndex + sublen > src.length()) {
      return false;
    }
    for (int j = 0, i = startIndex; j < sublen; ++j, ++i) {
      final char source = Character.toLowerCase(src.charAt(i));
      if (sub.charAt(j) != source) {
        return false;
      }
    }
    return true;
  }

  public static boolean endsWithIgnoreCase(final String src, final String subS) {
    final String sub = subS.toLowerCase();
    final int sublen = sub.length();
    int j = 0;
    int i = src.length() - sublen;
    if (i < 0) {
      return false;
    }
    while (j < sublen) {
      final char source = Character.toLowerCase(src.charAt(i));
      if (sub.charAt(j) != source) {
        return false;
      }
      ++j;
      ++i;
    }
    return true;
  }

  public static boolean startsWithChar(final String s, final char c) {
    return s.length() != 0 && s.charAt(0) == c;
  }

  public static boolean endsWithChar(final String s, final char c) {
    return s.length() != 0 && s.charAt(s.length() - 1) == c;
  }

  public static int count(final String source, final String sub) {
    return count(source, sub, 0);
  }

  public static int count(final String source, final String sub, final int start) {
    int count = 0;
    int j = start;
    final int sublen = sub.length();
    if (sublen == 0) {
      return 0;
    }
    while (true) {
      final int i = source.indexOf(sub, j);
      if (i == -1) {
        break;
      }
      ++count;
      j = i + sublen;
    }
    return count;
  }

  public static int count(final String source, final char c) {
    return count(source, c, 0);
  }

  public static int count(final String source, final char c, final int start) {
    int count = 0;
    int j = start;
    while (true) {
      final int i = source.indexOf(c, j);
      if (i == -1) {
        break;
      }
      ++count;
      j = i + 1;
    }
    return count;
  }

  public static int countIgnoreCase(final String source, final String sub) {
    int count = 0;
    int j = 0;
    final int sublen = sub.length();
    if (sublen == 0) {
      return 0;
    }
    while (true) {
      final int i = indexOfIgnoreCase(source, sub, j);
      if (i == -1) {
        break;
      }
      ++count;
      j = i + sublen;
    }
    return count;
  }

  public static int[] indexOf(final String s, final String[] arr) {
    return indexOf(s, arr, 0);
  }

  public static int[] indexOf(final String s, final String[] arr, final int start) {
    final int arrLen = arr.length;
    int index = Integer.MAX_VALUE;
    int last = -1;
    for (int j = 0; j < arrLen; ++j) {
      final int i = s.indexOf(arr[j], start);
      if (i != -1 && i < index) {
        index = i;
        last = j;
      }
    }
    int[] array;
    if (last == -1) {
      array = null;
    }
    else {
      final int[] array2 = array = new int[2];
      array2[0] = last;
      array2[1] = index;
    }
    return array;
  }

  public static int[] indexOfIgnoreCase(final String s, final String[] arr) {
    return indexOfIgnoreCase(s, arr, 0);
  }

  public static int[] indexOfIgnoreCase(final String s, final String[] arr, final int start) {
    final int arrLen = arr.length;
    int index = Integer.MAX_VALUE;
    int last = -1;
    for (int j = 0; j < arrLen; ++j) {
      final int i = indexOfIgnoreCase(s, arr[j], start);
      if (i != -1 && i < index) {
        index = i;
        last = j;
      }
    }
    int[] array;
    if (last == -1) {
      array = null;
    }
    else {
      final int[] array2 = array = new int[2];
      array2[0] = last;
      array2[1] = index;
    }
    return array;
  }

  public static int[] lastIndexOf(final String s, final String[] arr) {
    return lastIndexOf(s, arr, s.length());
  }

  public static int[] lastIndexOf(final String s, final String[] arr, final int fromIndex) {
    final int arrLen = arr.length;
    int index = -1;
    int last = -1;
    for (int j = 0; j < arrLen; ++j) {
      final int i = s.lastIndexOf(arr[j], fromIndex);
      if (i != -1 && i > index) {
        index = i;
        last = j;
      }
    }
    int[] array;
    if (last == -1) {
      array = null;
    }
    else {
      final int[] array2 = array = new int[2];
      array2[0] = last;
      array2[1] = index;
    }
    return array;
  }

  public static int[] lastIndexOfIgnoreCase(final String s, final String[] arr) {
    return lastIndexOfIgnoreCase(s, arr, s.length());
  }

  public static int[] lastIndexOfIgnoreCase(final String s, final String[] arr, final int fromIndex) {
    final int arrLen = arr.length;
    int index = -1;
    int last = -1;
    for (int j = 0; j < arrLen; ++j) {
      final int i = lastIndexOfIgnoreCase(s, arr[j], fromIndex);
      if (i != -1 && i > index) {
        index = i;
        last = j;
      }
    }
    int[] array;
    if (last == -1) {
      array = null;
    }
    else {
      final int[] array2 = array = new int[2];
      array2[0] = last;
      array2[1] = index;
    }
    return array;
  }

  public static boolean equals(final String[] as, final String[] as1) {
    if (as.length != as1.length) {
      return false;
    }
    for (int i = 0; i < as.length; ++i) {
      if (!as[i].equals(as1[i])) {
        return false;
      }
    }
    return true;
  }

  public static boolean equalsIgnoreCase(final String[] as, final String[] as1) {
    if (as.length != as1.length) {
      return false;
    }
    for (int i = 0; i < as.length; ++i) {
      if (!as[i].equalsIgnoreCase(as1[i])) {
        return false;
      }
    }
    return true;
  }

  public static String replace(final String s, final String[] sub, final String[] with) {
    if (sub.length != with.length || sub.length == 0) {
      return s;
    }
    int start = 0;
    final StringBuilder buf = new StringBuilder(s.length());
    while (true) {
      final int[] res = indexOf(s, sub, start);
      if (res == null) {
        break;
      }
      final int end = res[1];
      buf.append(s.substring(start, end));
      buf.append(with[res[0]]);
      start = end + sub[res[0]].length();
    }
    buf.append(s.substring(start));
    return buf.toString();
  }

  public static String replaceIgnoreCase(final String s, final String[] sub, final String[] with) {
    if (sub.length != with.length || sub.length == 0) {
      return s;
    }
    int start = 0;
    final StringBuilder buf = new StringBuilder(s.length());
    while (true) {
      final int[] res = indexOfIgnoreCase(s, sub, start);
      if (res == null) {
        break;
      }
      final int end = res[1];
      buf.append(s.substring(start, end));
      buf.append(with[res[0]]);
      start = end + sub[0].length();
    }
    buf.append(s.substring(start));
    return buf.toString();
  }

  public static int equalsOne(final String src, final String[] dest) {
    for (int i = 0; i < dest.length; ++i) {
      if (src.equals(dest[i])) {
        return i;
      }
    }
    return -1;
  }

  public static int equalsOneIgnoreCase(final String src, final String[] dest) {
    for (int i = 0; i < dest.length; ++i) {
      if (src.equalsIgnoreCase(dest[i])) {
        return i;
      }
    }
    return -1;
  }

  public static int startsWithOne(final String src, final String[] dest) {
    for (int i = 0; i < dest.length; ++i) {
      final String m = dest[i];
      if (m != null) {
        if (src.startsWith(m)) {
          return i;
        }
      }
    }
    return -1;
  }

  public static int startsWithOneIgnoreCase(final String src, final String[] dest) {
    for (int i = 0; i < dest.length; ++i) {
      final String m = dest[i];
      if (m != null) {
        if (startsWithIgnoreCase(src, m)) {
          return i;
        }
      }
    }
    return -1;
  }

  public static int endsWithOne(final String src, final String[] dest) {
    for (int i = 0; i < dest.length; ++i) {
      final String m = dest[i];
      if (m != null) {
        if (src.endsWith(m)) {
          return i;
        }
      }
    }
    return -1;
  }

  public static int endsWithOneIgnoreCase(final String src, final String[] dest) {
    for (int i = 0; i < dest.length; ++i) {
      final String m = dest[i];
      if (m != null) {
        if (endsWithIgnoreCase(src, m)) {
          return i;
        }
      }
    }
    return -1;
  }

  public static int indexOfChars(final String string, final String chars) {
    return indexOfChars(string, chars, 0);
  }

  public static int indexOfChars(final String string, final String chars, int startindex) {
    final int stringLen = string.length();
    final int charsLen = chars.length();
    if (startindex < 0) {
      startindex = 0;
    }
    for (int i = startindex; i < stringLen; ++i) {
      final char c = string.charAt(i);
      for (int j = 0; j < charsLen; ++j) {
        if (c == chars.charAt(j)) {
          return i;
        }
      }
    }
    return -1;
  }

  public static int indexOfChars(final String string, final char[] chars) {
    return indexOfChars(string, chars, 0);
  }

  public static int indexOfChars(final String string, final char[] chars, final int startindex) {
    final int stringLen = string.length();
    final int charsLen = chars.length;
    for (int i = startindex; i < stringLen; ++i) {
      final char c = string.charAt(i);
      for (int j = 0; j < charsLen; ++j) {
        if (c == chars[j]) {
          return i;
        }
      }
    }
    return -1;
  }

  public static int indexOfWhitespace(final String string) {
    return indexOfWhitespace(string, 0, string.length());
  }

  public static int indexOfWhitespace(final String string, final int startindex) {
    return indexOfWhitespace(string, startindex, string.length());
  }

  public static int indexOfWhitespace(final String string, final int startindex, final int endindex) {
    for (int i = startindex; i < endindex; ++i) {
      if (CharUtil.isWhitespace(string.charAt(i))) {
        return i;
      }
    }
    return -1;
  }

  public static int indexOfNonWhitespace(final String string) {
    return indexOfNonWhitespace(string, 0, string.length());
  }

  public static int indexOfNonWhitespace(final String string, final int startindex) {
    return indexOfNonWhitespace(string, startindex, string.length());
  }

  public static int indexOfNonWhitespace(final String string, final int startindex, final int endindex) {
    for (int i = startindex; i < endindex; ++i) {
      if (!CharUtil.isWhitespace(string.charAt(i))) {
        return i;
      }
    }
    return -1;
  }

  public static String stripLeadingChar(final String string, final char c) {
    if (string.length() > 0 && string.charAt(0) == c) {
      return string.substring(1);
    }
    return string;
  }

  public static String stripTrailingChar(final String string, final char c) {
    if (string.length() > 0 && string.charAt(string.length() - 1) == c) {
      return string.substring(0, string.length() - 1);
    }
    return string;
  }

  public static String stripChar(final String string, final char c) {
    if (string.length() == 0) {
      return string;
    }
    if (string.length() != 1) {
      int left = 0;
      int right = string.length();
      if (string.charAt(left) == c) {
        ++left;
      }
      if (string.charAt(right - 1) == c) {
        --right;
      }
      return string.substring(left, right);
    }
    if (string.charAt(0) == c) {
      return "";
    }
    return string;
  }

  public static String stripToChar(final String string, final char c) {
    final int ndx = string.indexOf(c);
    if (ndx == -1) {
      return string;
    }
    return string.substring(ndx);
  }

  public static String stripFromChar(final String string, final char c) {
    final int ndx = string.indexOf(c);
    if (ndx == -1) {
      return string;
    }
    return string.substring(0, ndx);
  }

  public static void trimAll(final String[] strings) {
    for (int i = 0; i < strings.length; ++i) {
      final String string = strings[i];
      if (string != null) {
        strings[i] = string.trim();
      }
    }
  }

  public static void trimDownAll(final String[] strings) {
    for (int i = 0; i < strings.length; ++i) {
      final String string = strings[i];
      if (string != null) {
        strings[i] = trimDown(string);
      }
    }
  }

  public static String trimDown(String string) {
    string = string.trim();
    if (string.length() == 0) {
      string = null;
    }
    return string;
  }

  public static String crop(final String string) {
    if (string.length() == 0) {
      return null;
    }
    return string;
  }

  public static void cropAll(final String[] strings) {
    for (int i = 0; i < strings.length; ++i) {
      String string = strings[i];
      if (string != null) {
        string = crop(strings[i]);
      }
      strings[i] = string;
    }
  }

  public static String trimLeft(final String src) {
    int len;
    int st;
    for (len = src.length(), st = 0; st < len && CharUtil.isWhitespace(src.charAt(st)); ++st) {}
    return (st > 0) ? src.substring(st) : src;
  }

  public static String trimRight(final String src) {
    int count;
    int len;
    for (len = (count = src.length()); len > 0 && CharUtil.isWhitespace(src.charAt(len - 1)); --len) {}
    return (len < count) ? src.substring(0, len) : src;
  }

  public static int[] indexOfRegion(final String string, final String leftBoundary, final String rightBoundary) {
    return indexOfRegion(string, leftBoundary, rightBoundary, 0);
  }

  public static int[] indexOfRegion(final String string, final String leftBoundary, final String rightBoundary, final int offset) {
    final int[] res = new int[4];
    int ndx = string.indexOf(leftBoundary, offset);
    if (ndx == -1) {
      return null;
    }
    res[0] = ndx;
    ndx += leftBoundary.length();
    res[1] = ndx;
    ndx = string.indexOf(rightBoundary, ndx);
    if (ndx == -1) {
      return null;
    }
    res[2] = ndx;
    res[3] = ndx + rightBoundary.length();
    return res;
  }

  public static int[] indexOfRegion(final String string, final String leftBoundary, final String rightBoundary, final char escape) {
    return indexOfRegion(string, leftBoundary, rightBoundary, escape, 0);
  }

  public static int[] indexOfRegion(final String string, final String leftBoundary, final String rightBoundary, final char escape, final int offset) {
    int ndx = offset;
    final int[] res = new int[4];
    int leftBoundaryLen;
    while (true) {
      ndx = string.indexOf(leftBoundary, ndx);
      if (ndx == -1) {
        return null;
      }
      leftBoundaryLen = leftBoundary.length();
      if (ndx <= 0 || string.charAt(ndx - 1) != escape) {
        break;
      }
      boolean cont = true;
      if (ndx > 1 && string.charAt(ndx - 2) == escape) {
        --ndx;
        ++leftBoundaryLen;
        cont = false;
      }
      if (!cont) {
        break;
      }
      ndx += leftBoundaryLen;
    }
    res[0] = ndx;
    ndx += leftBoundaryLen;
    res[1] = ndx;
    while (true) {
      ndx = string.indexOf(rightBoundary, ndx);
      if (ndx == -1) {
        return null;
      }
      if (ndx <= 0 || string.charAt(ndx - 1) != escape) {
        res[2] = ndx;
        res[3] = ndx + rightBoundary.length();
        return res;
      }
      ndx += rightBoundary.length();
    }
  }

  public static String join(final Object... array) {
    if (array == null) {
      return null;
    }
    if (array.length == 0) {
      return "";
    }
    if (array.length == 1) {
      return String.valueOf(array[0]);
    }
    final StringBuilder sb = new StringBuilder(array.length * 16);
    for (int i = 0; i < array.length; ++i) {
      sb.append(array[i]);
    }
    return sb.toString();
  }

  public static String join(final Object[] array, final char separator) {
    if (array == null) {
      return null;
    }
    if (array.length == 0) {
      return "";
    }
    if (array.length == 1) {
      return String.valueOf(array[0]);
    }
    final StringBuilder sb = new StringBuilder(array.length * 16);
    for (int i = 0; i < array.length; ++i) {
      if (i > 0) {
        sb.append(separator);
      }
      sb.append(array[i]);
    }
    return sb.toString();
  }

  public static String join(final Object[] array, final String separator) {
    if (array == null) {
      return null;
    }
    if (array.length == 0) {
      return "";
    }
    if (array.length == 1) {
      return String.valueOf(array[0]);
    }
    final StringBuilder sb = new StringBuilder(array.length * 16);
    for (int i = 0; i < array.length; ++i) {
      if (i > 0) {
        sb.append(separator);
      }
      sb.append(array[i]);
    }
    return sb.toString();
  }

  public static String convertCharset(final String source, final String srcCharsetName, final String newCharsetName) {
    if (srcCharsetName.equals(newCharsetName)) {
      return source;
    }
    try {
      return new String(source.getBytes(srcCharsetName), newCharsetName);
    }
    catch (UnsupportedEncodingException unex) {
      throw new IllegalArgumentException(unex);
    }
  }

  public static String escapeJava(final String string) {
    final int strLen = string.length();
    final StringBuilder sb = new StringBuilder(strLen);
    for (int i = 0; i < strLen; ++i) {
      final char c = string.charAt(i);
      switch (c) {
        case '\b': {
          sb.append("\\b");
          break;
        }
        case '\t': {
          sb.append("\\t");
          break;
        }
        case '\n': {
          sb.append("\\n");
          break;
        }
        case '\f': {
          sb.append("\\f");
          break;
        }
        case '\r': {
          sb.append("\\r");
          break;
        }
        case '\"': {
          sb.append("\\\"");
          break;
        }
        case '\\': {
          sb.append("\\\\");
          break;
        }
        default: {
          if (c < ' ' || c > '\u007f') {
            final String hex = Integer.toHexString(c);
            sb.append("\\u");
            for (int k = hex.length(); k < 4; ++k) {
              sb.append('0');
            }
            sb.append(hex);
            break;
          }
          sb.append(c);
          break;
        }
      }
    }
    return sb.toString();
  }

  public static String unescapeJava(final String str) {
    final char[] chars = str.toCharArray();
    final StringBuilder sb = new StringBuilder(str.length());
    for (int i = 0; i < chars.length; ++i) {
      char c = chars[i];
      if (c != '\\') {
        sb.append(c);
      }
      else {
        ++i;
        c = chars[i];
        switch (c) {
          case 'b': {
            sb.append('\b');
            break;
          }
          case 't': {
            sb.append('\t');
            break;
          }
          case 'n': {
            sb.append('\n');
            break;
          }
          case 'f': {
            sb.append('\f');
            break;
          }
          case 'r': {
            sb.append('\r');
            break;
          }
          case '\"': {
            sb.append('\"');
            break;
          }
          case '\\': {
            sb.append('\\');
            break;
          }
          case 'u': {
            final char hex = (char)Integer.parseInt(new String(chars, i + 1, 4), 16);
            sb.append(hex);
            i += 4;
            break;
          }
          default: {
            throw new IllegalArgumentException("Invalid escaping character: " + c);
          }
        }
      }
    }
    return sb.toString();
  }

  public static boolean isCharAtEqual(final String string, final int index, final char charToCompare) {
    return index >= 0 && index < string.length() && string.charAt(index) == charToCompare;
  }

  public static String surround(final String string, final String fix) {
    return surround(string, fix, fix);
  }

  public static String surround(String string, final String prefix, final String suffix) {
    if (!string.startsWith(prefix)) {
      string = prefix + string;
    }
    if (!string.endsWith(suffix)) {
      string += suffix;
    }
    return string;
  }

  public static String prefix(String string, final String prefix) {
    if (!string.startsWith(prefix)) {
      string = prefix + string;
    }
    return string;
  }

  public static String suffix(String string, final String suffix) {
    if (!string.endsWith(suffix)) {
      string += suffix;
    }
    return string;
  }

  public static String cutToIndexOf(String string, final String substring) {
    final int i = string.indexOf(substring);
    if (i != -1) {
      string = string.substring(0, i);
    }
    return string;
  }

  public static String cutToIndexOf(String string, final char c) {
    final int i = string.indexOf(c);
    if (i != -1) {
      string = string.substring(0, i);
    }
    return string;
  }

  public static String cutFromIndexOf(String string, final String substring) {
    final int i = string.indexOf(substring);
    if (i != -1) {
      string = string.substring(i);
    }
    return string;
  }

  public static String cutFromIndexOf(String string, final char c) {
    final int i = string.indexOf(c);
    if (i != -1) {
      string = string.substring(i);
    }
    return string;
  }

  public static String cutPrefix(String string, final String prefix) {
    if (string.startsWith(prefix)) {
      string = string.substring(prefix.length());
    }
    return string;
  }

  public static String cutSuffix(String string, final String suffix) {
    if (string.endsWith(suffix)) {
      string = string.substring(0, string.length() - suffix.length());
    }
    return string;
  }

  public static String cutSurrounding(final String string, final String fix) {
    return cutSurrounding(string, fix, fix);
  }

  public static String cutSurrounding(final String string, final String prefix, final String suffix) {
    int start = 0;
    int end = string.length();
    if (string.startsWith(prefix)) {
      start = prefix.length();
    }
    if (string.endsWith(suffix)) {
      end -= suffix.length();
    }
    return string.substring(start, end);
  }

  public static boolean isCharAtEscaped(final String src, int ndx, final char escapeChar) {
    if (ndx == 0) {
      return false;
    }
    --ndx;
    return src.charAt(ndx) == escapeChar;
  }

  public static int indexOfUnescapedChar(final String src, final char sub, final char escapeChar) {
    return indexOfUnescapedChar(src, sub, escapeChar, 0);
  }

  public static int indexOfUnescapedChar(final String src, final char sub, final char escapeChar, int startIndex) {
    if (startIndex < 0) {
      startIndex = 0;
    }
    final int srclen = src.length();
    char c = '\0';
    for (int i = startIndex; i < srclen; ++i) {
      final char previous = c;
      c = src.charAt(i);
      if (c == sub && (i <= startIndex || previous != escapeChar)) {
        return i;
      }
    }
    return -1;
  }

  public static String insert(final String src, final String insert) {
    return insert(src, insert, 0);
  }

  public static String insert(final String src, final String insert, int offset) {
    if (offset < 0) {
      offset = 0;
    }
    if (offset > src.length()) {
      offset = src.length();
    }
    final StringBuilder sb = new StringBuilder(src);
    sb.insert(offset, insert);
    return sb.toString();
  }

  public static String repeat(final String source, int count) {
    final StringBand result = new StringBand(count);
    while (count > 0) {
      result.append(source);
      --count;
    }
    return result.toString();
  }

  public static String repeat(final char c, final int count) {
    final char[] result = new char[count];
    for (int i = 0; i < count; ++i) {
      result[i] = c;
    }
    return new String(result);
  }

  public static String reverse(final String s) {
    final StringBuilder result = new StringBuilder(s.length());
    for (int i = s.length() - 1; i >= 0; --i) {
      result.append(s.charAt(i));
    }
    return result.toString();
  }

  public static String maxCommonPrefix(final String one, final String two) {
    final int minLength = Math.min(one.length(), two.length());
    final StringBuilder sb = new StringBuilder(minLength);
    for (int pos = 0; pos < minLength; ++pos) {
      final char currentChar = one.charAt(pos);
      if (currentChar != two.charAt(pos)) {
        break;
      }
      sb.append(currentChar);
    }
    return sb.toString();
  }

  public static String fromCamelCase(final String input, final char separator) {
    final int length = input.length();
    final StringBuilder result = new StringBuilder(length * 2);
    int resultLength = 0;
    boolean prevTranslated = false;
    for (int i = 0; i < length; ++i) {
      char c = input.charAt(i);
      if (i > 0 || c != separator) {
        if (Character.isUpperCase(c)) {
          if (!prevTranslated && resultLength > 0 && result.charAt(resultLength - 1) != separator) {
            result.append(separator);
            ++resultLength;
          }
          c = Character.toLowerCase(c);
          prevTranslated = true;
        }
        else {
          prevTranslated = false;
        }
        result.append(c);
        ++resultLength;
      }
    }
    return (resultLength > 0) ? result.toString() : input;
  }

  public static String toCamelCase(final String input, final boolean firstCharUppercase, final char separator) {
    final int length = input.length();
    final StringBuilder sb = new StringBuilder(length);
    boolean upperCase = firstCharUppercase;
    for (int i = 0; i < length; ++i) {
      final char ch = input.charAt(i);
      if (ch == separator) {
        upperCase = true;
      }
      else if (upperCase) {
        sb.append(Character.toUpperCase(ch));
        upperCase = false;
      }
      else {
        sb.append(ch);
      }
    }
    return sb.toString();
  }

  public static String findCommonPrefix(final String... strings) {
    final StringBuilder prefix = new StringBuilder();
    int index = 0;
    char c = '\0';
    Label_0086:
    while (true) {
      for (int i = 0; i < strings.length; ++i) {
        final String s = strings[i];
        if (index == s.length()) {
          break Label_0086;
        }
        if (i == 0) {
          c = s.charAt(index);
        }
        else if (s.charAt(index) != c) {
          break Label_0086;
        }
      }
      ++index;
      prefix.append(c);
    }
    return (prefix.length() == 0) ? "" : prefix.toString();
  }

  public static String shorten(String s, int length, final String suffix) {
    length -= suffix.length();
    if (s.length() > length) {
      for (int j = length; j >= 0; --j) {
        if (CharUtil.isWhitespace(s.charAt(j))) {
          length = j;
          break;
        }
      }
      final String temp = s.substring(0, length);
      s = temp.concat(suffix);
    }
    return s;
  }

  public static String formatParagraph(final String src, final int len, final boolean breakOnWhitespace) {
    final StringBuilder str = new StringBuilder();
    int to;
    for (int total = src.length(), from = 0; from < total; from = to) {
      to = from + len;
      if (to >= total) {
        to = total;
      }
      else if (breakOnWhitespace) {
        final int ndx = lastIndexOfWhitespace(src, to - 1, from);
        if (ndx != -1) {
          to = ndx + 1;
        }
      }
      final int cutFrom = indexOfNonWhitespace(src, from, to);
      if (cutFrom != -1) {
        final int cutTo = lastIndexOfNonWhitespace(src, to - 1, from) + 1;
        str.append(src.substring(cutFrom, cutTo));
      }
      str.append('\n');
    }
    return str.toString();
  }

  public static String convertTabsToSpaces(final String line, final int tabWidth) {
    int last_tab_index = 0;
    int added_chars = 0;
    if (tabWidth == 0) {
      return remove(line, '\t');
    }
    final StringBuilder result = new StringBuilder();
    int tab_index;
    while ((tab_index = line.indexOf(9, last_tab_index)) != -1) {
      int tab_size = tabWidth - (tab_index + added_chars) % tabWidth;
      if (tab_size == 0) {
        tab_size = tabWidth;
      }
      added_chars += tab_size - 1;
      result.append(line.substring(last_tab_index, tab_index));
      result.append(repeat(' ', tab_size));
      last_tab_index = tab_index + 1;
    }
    if (last_tab_index == 0) {
      return line;
    }
    result.append(line.substring(last_tab_index));
    return result.toString();
  }

  public static String toLowerCase(final String s) {
    return toLowerCase(s, null);
  }

  public static String toLowerCase(final String s, Locale locale) {
    if (s == null) {
      return null;
    }
    StringBuilder sb = null;
    for (int i = 0; i < s.length(); ++i) {
      final char c = s.charAt(i);
      if (c > '\u007f') {
        if (locale == null) {
          locale = Locale.getDefault();
        }
        return s.toLowerCase(locale);
      }
      if (c >= 'A' && c <= 'Z') {
        if (sb == null) {
          sb = new StringBuilder(s);
        }
        sb.setCharAt(i, (char)(c + ' '));
      }
    }
    if (sb == null) {
      return s;
    }
    return sb.toString();
  }

  public static String toUpperCase(final String s) {
    return toUpperCase(s, null);
  }

  public static String toUpperCase(final String s, Locale locale) {
    if (s == null) {
      return null;
    }
    StringBuilder sb = null;
    for (int i = 0; i < s.length(); ++i) {
      final char c = s.charAt(i);
      if (c > '\u007f') {
        if (locale == null) {
          locale = Locale.getDefault();
        }
        return s.toUpperCase(locale);
      }
      if (c >= 'a' && c <= 'z') {
        if (sb == null) {
          sb = new StringBuilder(s);
        }
        sb.setCharAt(i, (char)(c - ' '));
      }
    }
    if (sb == null) {
      return s;
    }
    return sb.toString();
  }

  public static String toHexString(final byte[] bytes) {
    final char[] chars = new char[bytes.length * 2];
    int i = 0;
    for (final byte b : bytes) {
      chars[i++] = CharUtil.int2hex((b & 0xF0) >> 4);
      chars[i++] = CharUtil.int2hex(b & 0xF);
    }
    return new String(chars);
  }
}
