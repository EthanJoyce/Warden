package io.ll.warden.utils.jodd;

import java.util.*;
import java.lang.reflect.*;

public class Util
{
  public static boolean equals(final Object obj1, final Object obj2) {
    return (obj1 != null) ? obj1.equals(obj2) : (obj2 == null);
  }

  public static String toString(final Object value) {
    if (value == null) {
      return null;
    }
    return value.toString();
  }

  public static int length(final Object obj) {
    if (obj == null) {
      return 0;
    }
    if (obj instanceof CharSequence) {
      return ((CharSequence)obj).length();
    }
    if (obj instanceof Collection) {
      return ((Collection)obj).size();
    }
    if (obj instanceof Map) {
      return ((Map)obj).size();
    }
    if (obj instanceof Iterator) {
      final Iterator iter = (Iterator)obj;
      int count = 0;
      while (iter.hasNext()) {
        ++count;
        iter.next();
      }
      return count;
    }
    if (obj instanceof Enumeration) {
      final Enumeration enumeration = (Enumeration)obj;
      int count = 0;
      while (enumeration.hasMoreElements()) {
        ++count;
        enumeration.nextElement();
      }
      return count;
    }
    if (obj.getClass().isArray()) {
      return Array.getLength(obj);
    }
    return -1;
  }

  public static boolean containsElement(final Object obj, final Object element) {
    if (obj == null) {
      return false;
    }
    if (obj instanceof String) {
      return element != null && ((String)obj).contains(element.toString());
    }
    if (obj instanceof Collection) {
      return ((Collection)obj).contains(element);
    }
    if (obj instanceof Map) {
      return ((Map)obj).values().contains(element);
    }
    if (obj instanceof Iterator) {
      final Iterator iter = (Iterator)obj;
      while (iter.hasNext()) {
        final Object o = iter.next();
        if (equals(o, element)) {
          return true;
        }
      }
      return false;
    }
    if (obj instanceof Enumeration) {
      final Enumeration enumeration = (Enumeration)obj;
      while (enumeration.hasMoreElements()) {
        final Object o = enumeration.nextElement();
        if (equals(o, element)) {
          return true;
        }
      }
      return false;
    }
    if (obj.getClass().isArray()) {
      for (int len = Array.getLength(obj), i = 0; i < len; ++i) {
        final Object o2 = Array.get(obj, i);
        if (equals(o2, element)) {
          return true;
        }
      }
    }
    return false;
  }
}

