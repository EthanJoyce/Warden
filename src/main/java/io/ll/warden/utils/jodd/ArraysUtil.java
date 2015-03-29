package io.ll.warden.utils.jodd;

import java.lang.reflect.*;

public class ArraysUtil
{
  public static <T> T[] array(final T... elements) {
    return elements;
  }

  public static byte[] bytes(final byte... elements) {
    return elements;
  }

  public static char[] chars(final char... elements) {
    return elements;
  }

  public static short[] shorts(final short... elements) {
    return elements;
  }

  public static int[] ints(final int... elements) {
    return elements;
  }

  public static long[] longs(final long... elements) {
    return elements;
  }

  public static float[] floats(final float... elements) {
    return elements;
  }

  public static double[] doubles(final double... elements) {
    return elements;
  }

  public static boolean[] booleans(final boolean... elements) {
    return elements;
  }

  public static <T> T[] join(final T[]... arrays) {
    final Class<T> componentType = (Class<T>)arrays.getClass().getComponentType().getComponentType();
    return join(componentType, arrays);
  }

  public static <T> T[] join(final Class<T> componentType, final T[][] arrays) {
    if (arrays.length == 1) {
      return arrays[0];
    }
    int length = 0;
    for (final T[] array : arrays) {
      length += array.length;
    }
    final T[] result = (T[])Array.newInstance(componentType, length);
    length = 0;
    for (final T[] array2 : arrays) {
      System.arraycopy(array2, 0, result, length, array2.length);
      length += array2.length;
    }
    return result;
  }

  public static String[] join(final String[]... arrays) {
    if (arrays.length == 0) {
      return new String[0];
    }
    if (arrays.length == 1) {
      return arrays[0];
    }
    int length = 0;
    for (final String[] array : arrays) {
      length += array.length;
    }
    final String[] result = new String[length];
    length = 0;
    for (final String[] array2 : arrays) {
      System.arraycopy(array2, 0, result, length, array2.length);
      length += array2.length;
    }
    return result;
  }

  public static byte[] join(final byte[]... arrays) {
    if (arrays.length == 0) {
      return new byte[0];
    }
    if (arrays.length == 1) {
      return arrays[0];
    }
    int length = 0;
    for (final byte[] array : arrays) {
      length += array.length;
    }
    final byte[] result = new byte[length];
    length = 0;
    for (final byte[] array2 : arrays) {
      System.arraycopy(array2, 0, result, length, array2.length);
      length += array2.length;
    }
    return result;
  }

  public static char[] join(final char[]... arrays) {
    if (arrays.length == 0) {
      return new char[0];
    }
    if (arrays.length == 1) {
      return arrays[0];
    }
    int length = 0;
    for (final char[] array : arrays) {
      length += array.length;
    }
    final char[] result = new char[length];
    length = 0;
    for (final char[] array2 : arrays) {
      System.arraycopy(array2, 0, result, length, array2.length);
      length += array2.length;
    }
    return result;
  }

  public static short[] join(final short[]... arrays) {
    if (arrays.length == 0) {
      return new short[0];
    }
    if (arrays.length == 1) {
      return arrays[0];
    }
    int length = 0;
    for (final short[] array : arrays) {
      length += array.length;
    }
    final short[] result = new short[length];
    length = 0;
    for (final short[] array2 : arrays) {
      System.arraycopy(array2, 0, result, length, array2.length);
      length += array2.length;
    }
    return result;
  }

  public static int[] join(final int[]... arrays) {
    if (arrays.length == 0) {
      return new int[0];
    }
    if (arrays.length == 1) {
      return arrays[0];
    }
    int length = 0;
    for (final int[] array : arrays) {
      length += array.length;
    }
    final int[] result = new int[length];
    length = 0;
    for (final int[] array2 : arrays) {
      System.arraycopy(array2, 0, result, length, array2.length);
      length += array2.length;
    }
    return result;
  }

  public static long[] join(final long[]... arrays) {
    if (arrays.length == 0) {
      return new long[0];
    }
    if (arrays.length == 1) {
      return arrays[0];
    }
    int length = 0;
    for (final long[] array : arrays) {
      length += array.length;
    }
    final long[] result = new long[length];
    length = 0;
    for (final long[] array2 : arrays) {
      System.arraycopy(array2, 0, result, length, array2.length);
      length += array2.length;
    }
    return result;
  }

  public static float[] join(final float[]... arrays) {
    if (arrays.length == 0) {
      return new float[0];
    }
    if (arrays.length == 1) {
      return arrays[0];
    }
    int length = 0;
    for (final float[] array : arrays) {
      length += array.length;
    }
    final float[] result = new float[length];
    length = 0;
    for (final float[] array2 : arrays) {
      System.arraycopy(array2, 0, result, length, array2.length);
      length += array2.length;
    }
    return result;
  }

  public static double[] join(final double[]... arrays) {
    if (arrays.length == 0) {
      return new double[0];
    }
    if (arrays.length == 1) {
      return arrays[0];
    }
    int length = 0;
    for (final double[] array : arrays) {
      length += array.length;
    }
    final double[] result = new double[length];
    length = 0;
    for (final double[] array2 : arrays) {
      System.arraycopy(array2, 0, result, length, array2.length);
      length += array2.length;
    }
    return result;
  }

  public static boolean[] join(final boolean[]... arrays) {
    if (arrays.length == 0) {
      return new boolean[0];
    }
    if (arrays.length == 1) {
      return arrays[0];
    }
    int length = 0;
    for (final boolean[] array : arrays) {
      length += array.length;
    }
    final boolean[] result = new boolean[length];
    length = 0;
    for (final boolean[] array2 : arrays) {
      System.arraycopy(array2, 0, result, length, array2.length);
      length += array2.length;
    }
    return result;
  }

  public static <T> T[] resize(final T[] buffer, final int newSize) {
    final Class<T> componentType = (Class<T>)buffer.getClass().getComponentType();
    final T[] temp = (T[])Array.newInstance(componentType, newSize);
    System.arraycopy(buffer, 0, temp, 0, (buffer.length >= newSize) ? newSize : buffer.length);
    return temp;
  }

  public static String[] resize(final String[] buffer, final int newSize) {
    final String[] temp = new String[newSize];
    System.arraycopy(buffer, 0, temp, 0, (buffer.length >= newSize) ? newSize : buffer.length);
    return temp;
  }

  public static byte[] resize(final byte[] buffer, final int newSize) {
    final byte[] temp = new byte[newSize];
    System.arraycopy(buffer, 0, temp, 0, (buffer.length >= newSize) ? newSize : buffer.length);
    return temp;
  }

  public static char[] resize(final char[] buffer, final int newSize) {
    final char[] temp = new char[newSize];
    System.arraycopy(buffer, 0, temp, 0, (buffer.length >= newSize) ? newSize : buffer.length);
    return temp;
  }

  public static short[] resize(final short[] buffer, final int newSize) {
    final short[] temp = new short[newSize];
    System.arraycopy(buffer, 0, temp, 0, (buffer.length >= newSize) ? newSize : buffer.length);
    return temp;
  }

  public static int[] resize(final int[] buffer, final int newSize) {
    final int[] temp = new int[newSize];
    System.arraycopy(buffer, 0, temp, 0, (buffer.length >= newSize) ? newSize : buffer.length);
    return temp;
  }

  public static long[] resize(final long[] buffer, final int newSize) {
    final long[] temp = new long[newSize];
    System.arraycopy(buffer, 0, temp, 0, (buffer.length >= newSize) ? newSize : buffer.length);
    return temp;
  }

  public static float[] resize(final float[] buffer, final int newSize) {
    final float[] temp = new float[newSize];
    System.arraycopy(buffer, 0, temp, 0, (buffer.length >= newSize) ? newSize : buffer.length);
    return temp;
  }

  public static double[] resize(final double[] buffer, final int newSize) {
    final double[] temp = new double[newSize];
    System.arraycopy(buffer, 0, temp, 0, (buffer.length >= newSize) ? newSize : buffer.length);
    return temp;
  }

  public static boolean[] resize(final boolean[] buffer, final int newSize) {
    final boolean[] temp = new boolean[newSize];
    System.arraycopy(buffer, 0, temp, 0, (buffer.length >= newSize) ? newSize : buffer.length);
    return temp;
  }

  public static <T> T[] append(final T[] buffer, final T newElement) {
    final T[] t = resize(buffer, buffer.length + 1);
    t[buffer.length] = newElement;
    return t;
  }

  public static String[] append(final String[] buffer, final String newElement) {
    final String[] t = resize(buffer, buffer.length + 1);
    t[buffer.length] = newElement;
    return t;
  }

  public static byte[] append(final byte[] buffer, final byte newElement) {
    final byte[] t = resize(buffer, buffer.length + 1);
    t[buffer.length] = newElement;
    return t;
  }

  public static char[] append(final char[] buffer, final char newElement) {
    final char[] t = resize(buffer, buffer.length + 1);
    t[buffer.length] = newElement;
    return t;
  }

  public static short[] append(final short[] buffer, final short newElement) {
    final short[] t = resize(buffer, buffer.length + 1);
    t[buffer.length] = newElement;
    return t;
  }

  public static int[] append(final int[] buffer, final int newElement) {
    final int[] t = resize(buffer, buffer.length + 1);
    t[buffer.length] = newElement;
    return t;
  }

  public static long[] append(final long[] buffer, final long newElement) {
    final long[] t = resize(buffer, buffer.length + 1);
    t[buffer.length] = newElement;
    return t;
  }

  public static float[] append(final float[] buffer, final float newElement) {
    final float[] t = resize(buffer, buffer.length + 1);
    t[buffer.length] = newElement;
    return t;
  }

  public static double[] append(final double[] buffer, final double newElement) {
    final double[] t = resize(buffer, buffer.length + 1);
    t[buffer.length] = newElement;
    return t;
  }

  public static boolean[] append(final boolean[] buffer, final boolean newElement) {
    final boolean[] t = resize(buffer, buffer.length + 1);
    t[buffer.length] = newElement;
    return t;
  }

  public static <T> T[] remove(final T[] buffer, final int offset, final int length) {
    final Class<T> componentType = (Class<T>)buffer.getClass().getComponentType();
    return remove(buffer, offset, length, componentType);
  }

  public static <T> T[] remove(final T[] buffer, final int offset, final int length, final Class<T> componentType) {
    final int len2 = buffer.length - length;
    final T[] temp = (T[])Array.newInstance(componentType, len2);
    System.arraycopy(buffer, 0, temp, 0, offset);
    System.arraycopy(buffer, offset + length, temp, offset, len2 - offset);
    return temp;
  }

  public static String[] remove(final String[] buffer, final int offset, final int length) {
    final int len2 = buffer.length - length;
    final String[] temp = new String[len2];
    System.arraycopy(buffer, 0, temp, 0, offset);
    System.arraycopy(buffer, offset + length, temp, offset, len2 - offset);
    return temp;
  }

  public static byte[] remove(final byte[] buffer, final int offset, final int length) {
    final int len2 = buffer.length - length;
    final byte[] temp = new byte[len2];
    System.arraycopy(buffer, 0, temp, 0, offset);
    System.arraycopy(buffer, offset + length, temp, offset, len2 - offset);
    return temp;
  }

  public static char[] remove(final char[] buffer, final int offset, final int length) {
    final int len2 = buffer.length - length;
    final char[] temp = new char[len2];
    System.arraycopy(buffer, 0, temp, 0, offset);
    System.arraycopy(buffer, offset + length, temp, offset, len2 - offset);
    return temp;
  }

  public static short[] remove(final short[] buffer, final int offset, final int length) {
    final int len2 = buffer.length - length;
    final short[] temp = new short[len2];
    System.arraycopy(buffer, 0, temp, 0, offset);
    System.arraycopy(buffer, offset + length, temp, offset, len2 - offset);
    return temp;
  }

  public static int[] remove(final int[] buffer, final int offset, final int length) {
    final int len2 = buffer.length - length;
    final int[] temp = new int[len2];
    System.arraycopy(buffer, 0, temp, 0, offset);
    System.arraycopy(buffer, offset + length, temp, offset, len2 - offset);
    return temp;
  }

  public static long[] remove(final long[] buffer, final int offset, final int length) {
    final int len2 = buffer.length - length;
    final long[] temp = new long[len2];
    System.arraycopy(buffer, 0, temp, 0, offset);
    System.arraycopy(buffer, offset + length, temp, offset, len2 - offset);
    return temp;
  }

  public static float[] remove(final float[] buffer, final int offset, final int length) {
    final int len2 = buffer.length - length;
    final float[] temp = new float[len2];
    System.arraycopy(buffer, 0, temp, 0, offset);
    System.arraycopy(buffer, offset + length, temp, offset, len2 - offset);
    return temp;
  }

  public static double[] remove(final double[] buffer, final int offset, final int length) {
    final int len2 = buffer.length - length;
    final double[] temp = new double[len2];
    System.arraycopy(buffer, 0, temp, 0, offset);
    System.arraycopy(buffer, offset + length, temp, offset, len2 - offset);
    return temp;
  }

  public static boolean[] remove(final boolean[] buffer, final int offset, final int length) {
    final int len2 = buffer.length - length;
    final boolean[] temp = new boolean[len2];
    System.arraycopy(buffer, 0, temp, 0, offset);
    System.arraycopy(buffer, offset + length, temp, offset, len2 - offset);
    return temp;
  }

  public static <T> T[] subarray(final T[] buffer, final int offset, final int length) {
    final Class<T> componentType = (Class<T>)buffer.getClass().getComponentType();
    return subarray(buffer, offset, length, componentType);
  }

  public static <T> T[] subarray(final T[] buffer, final int offset, final int length, final Class<T> componentType) {
    final T[] temp = (T[])Array.newInstance(componentType, length);
    System.arraycopy(buffer, offset, temp, 0, length);
    return temp;
  }

  public static String[] subarray(final String[] buffer, final int offset, final int length) {
    final String[] temp = new String[length];
    System.arraycopy(buffer, offset, temp, 0, length);
    return temp;
  }

  public static byte[] subarray(final byte[] buffer, final int offset, final int length) {
    final byte[] temp = new byte[length];
    System.arraycopy(buffer, offset, temp, 0, length);
    return temp;
  }

  public static char[] subarray(final char[] buffer, final int offset, final int length) {
    final char[] temp = new char[length];
    System.arraycopy(buffer, offset, temp, 0, length);
    return temp;
  }

  public static short[] subarray(final short[] buffer, final int offset, final int length) {
    final short[] temp = new short[length];
    System.arraycopy(buffer, offset, temp, 0, length);
    return temp;
  }

  public static int[] subarray(final int[] buffer, final int offset, final int length) {
    final int[] temp = new int[length];
    System.arraycopy(buffer, offset, temp, 0, length);
    return temp;
  }

  public static long[] subarray(final long[] buffer, final int offset, final int length) {
    final long[] temp = new long[length];
    System.arraycopy(buffer, offset, temp, 0, length);
    return temp;
  }

  public static float[] subarray(final float[] buffer, final int offset, final int length) {
    final float[] temp = new float[length];
    System.arraycopy(buffer, offset, temp, 0, length);
    return temp;
  }

  public static double[] subarray(final double[] buffer, final int offset, final int length) {
    final double[] temp = new double[length];
    System.arraycopy(buffer, offset, temp, 0, length);
    return temp;
  }

  public static boolean[] subarray(final boolean[] buffer, final int offset, final int length) {
    final boolean[] temp = new boolean[length];
    System.arraycopy(buffer, offset, temp, 0, length);
    return temp;
  }

  public static <T> T[] insert(final T[] dest, final T[] src, final int offset) {
    final Class<T> componentType = (Class<T>)dest.getClass().getComponentType();
    return insert(dest, src, offset, componentType);
  }

  public static <T> T[] insert(final T[] dest, final T src, final int offset) {
    final Class<T> componentType = (Class<T>)dest.getClass().getComponentType();
    return insert(dest, src, offset, componentType);
  }

  public static <T> T[] insert(final T[] dest, final T[] src, final int offset, final Class componentType) {
    final T[] temp = (T[])Array.newInstance(componentType, dest.length + src.length);
    System.arraycopy(dest, 0, temp, 0, offset);
    System.arraycopy(src, 0, temp, offset, src.length);
    System.arraycopy(dest, offset, temp, src.length + offset, dest.length - offset);
    return temp;
  }

  public static <T> T[] insert(final T[] dest, final T src, final int offset, final Class componentType) {
    final T[] temp = (T[])Array.newInstance(componentType, dest.length + 1);
    System.arraycopy(dest, 0, temp, 0, offset);
    temp[offset] = src;
    System.arraycopy(dest, offset, temp, offset + 1, dest.length - offset);
    return temp;
  }

  public static String[] insert(final String[] dest, final String[] src, final int offset) {
    final String[] temp = new String[dest.length + src.length];
    System.arraycopy(dest, 0, temp, 0, offset);
    System.arraycopy(src, 0, temp, offset, src.length);
    System.arraycopy(dest, offset, temp, src.length + offset, dest.length - offset);
    return temp;
  }

  public static String[] insert(final String[] dest, final String src, final int offset) {
    final String[] temp = new String[dest.length + 1];
    System.arraycopy(dest, 0, temp, 0, offset);
    temp[offset] = src;
    System.arraycopy(dest, offset, temp, offset + 1, dest.length - offset);
    return temp;
  }

  public static byte[] insert(final byte[] dest, final byte[] src, final int offset) {
    final byte[] temp = new byte[dest.length + src.length];
    System.arraycopy(dest, 0, temp, 0, offset);
    System.arraycopy(src, 0, temp, offset, src.length);
    System.arraycopy(dest, offset, temp, src.length + offset, dest.length - offset);
    return temp;
  }

  public static byte[] insert(final byte[] dest, final byte src, final int offset) {
    final byte[] temp = new byte[dest.length + 1];
    System.arraycopy(dest, 0, temp, 0, offset);
    temp[offset] = src;
    System.arraycopy(dest, offset, temp, offset + 1, dest.length - offset);
    return temp;
  }

  public static char[] insert(final char[] dest, final char[] src, final int offset) {
    final char[] temp = new char[dest.length + src.length];
    System.arraycopy(dest, 0, temp, 0, offset);
    System.arraycopy(src, 0, temp, offset, src.length);
    System.arraycopy(dest, offset, temp, src.length + offset, dest.length - offset);
    return temp;
  }

  public static char[] insert(final char[] dest, final char src, final int offset) {
    final char[] temp = new char[dest.length + 1];
    System.arraycopy(dest, 0, temp, 0, offset);
    temp[offset] = src;
    System.arraycopy(dest, offset, temp, offset + 1, dest.length - offset);
    return temp;
  }

  public static short[] insert(final short[] dest, final short[] src, final int offset) {
    final short[] temp = new short[dest.length + src.length];
    System.arraycopy(dest, 0, temp, 0, offset);
    System.arraycopy(src, 0, temp, offset, src.length);
    System.arraycopy(dest, offset, temp, src.length + offset, dest.length - offset);
    return temp;
  }

  public static short[] insert(final short[] dest, final short src, final int offset) {
    final short[] temp = new short[dest.length + 1];
    System.arraycopy(dest, 0, temp, 0, offset);
    temp[offset] = src;
    System.arraycopy(dest, offset, temp, offset + 1, dest.length - offset);
    return temp;
  }

  public static int[] insert(final int[] dest, final int[] src, final int offset) {
    final int[] temp = new int[dest.length + src.length];
    System.arraycopy(dest, 0, temp, 0, offset);
    System.arraycopy(src, 0, temp, offset, src.length);
    System.arraycopy(dest, offset, temp, src.length + offset, dest.length - offset);
    return temp;
  }

  public static int[] insert(final int[] dest, final int src, final int offset) {
    final int[] temp = new int[dest.length + 1];
    System.arraycopy(dest, 0, temp, 0, offset);
    temp[offset] = src;
    System.arraycopy(dest, offset, temp, offset + 1, dest.length - offset);
    return temp;
  }

  public static long[] insert(final long[] dest, final long[] src, final int offset) {
    final long[] temp = new long[dest.length + src.length];
    System.arraycopy(dest, 0, temp, 0, offset);
    System.arraycopy(src, 0, temp, offset, src.length);
    System.arraycopy(dest, offset, temp, src.length + offset, dest.length - offset);
    return temp;
  }

  public static long[] insert(final long[] dest, final long src, final int offset) {
    final long[] temp = new long[dest.length + 1];
    System.arraycopy(dest, 0, temp, 0, offset);
    temp[offset] = src;
    System.arraycopy(dest, offset, temp, offset + 1, dest.length - offset);
    return temp;
  }

  public static float[] insert(final float[] dest, final float[] src, final int offset) {
    final float[] temp = new float[dest.length + src.length];
    System.arraycopy(dest, 0, temp, 0, offset);
    System.arraycopy(src, 0, temp, offset, src.length);
    System.arraycopy(dest, offset, temp, src.length + offset, dest.length - offset);
    return temp;
  }

  public static float[] insert(final float[] dest, final float src, final int offset) {
    final float[] temp = new float[dest.length + 1];
    System.arraycopy(dest, 0, temp, 0, offset);
    temp[offset] = src;
    System.arraycopy(dest, offset, temp, offset + 1, dest.length - offset);
    return temp;
  }

  public static double[] insert(final double[] dest, final double[] src, final int offset) {
    final double[] temp = new double[dest.length + src.length];
    System.arraycopy(dest, 0, temp, 0, offset);
    System.arraycopy(src, 0, temp, offset, src.length);
    System.arraycopy(dest, offset, temp, src.length + offset, dest.length - offset);
    return temp;
  }

  public static double[] insert(final double[] dest, final double src, final int offset) {
    final double[] temp = new double[dest.length + 1];
    System.arraycopy(dest, 0, temp, 0, offset);
    temp[offset] = src;
    System.arraycopy(dest, offset, temp, offset + 1, dest.length - offset);
    return temp;
  }

  public static boolean[] insert(final boolean[] dest, final boolean[] src, final int offset) {
    final boolean[] temp = new boolean[dest.length + src.length];
    System.arraycopy(dest, 0, temp, 0, offset);
    System.arraycopy(src, 0, temp, offset, src.length);
    System.arraycopy(dest, offset, temp, src.length + offset, dest.length - offset);
    return temp;
  }

  public static boolean[] insert(final boolean[] dest, final boolean src, final int offset) {
    final boolean[] temp = new boolean[dest.length + 1];
    System.arraycopy(dest, 0, temp, 0, offset);
    temp[offset] = src;
    System.arraycopy(dest, offset, temp, offset + 1, dest.length - offset);
    return temp;
  }

  public static <T> T[] insertAt(final T[] dest, final T[] src, final int offset) {
    final Class<T> componentType = (Class<T>)dest.getClass().getComponentType();
    return insertAt(dest, src, offset, componentType);
  }

  public static <T> T[] insertAt(final T[] dest, final T[] src, final int offset, final Class componentType) {
    final T[] temp = (T[])Array.newInstance(componentType, dest.length + src.length - 1);
    System.arraycopy(dest, 0, temp, 0, offset);
    System.arraycopy(src, 0, temp, offset, src.length);
    System.arraycopy(dest, offset + 1, temp, src.length + offset, dest.length - offset - 1);
    return temp;
  }

  public static String[] insertAt(final String[] dest, final String[] src, final int offset) {
    final String[] temp = new String[dest.length + src.length - 1];
    System.arraycopy(dest, 0, temp, 0, offset);
    System.arraycopy(src, 0, temp, offset, src.length);
    System.arraycopy(dest, offset + 1, temp, src.length + offset, dest.length - offset - 1);
    return temp;
  }

  public static byte[] insertAt(final byte[] dest, final byte[] src, final int offset) {
    final byte[] temp = new byte[dest.length + src.length - 1];
    System.arraycopy(dest, 0, temp, 0, offset);
    System.arraycopy(src, 0, temp, offset, src.length);
    System.arraycopy(dest, offset + 1, temp, src.length + offset, dest.length - offset - 1);
    return temp;
  }

  public static char[] insertAt(final char[] dest, final char[] src, final int offset) {
    final char[] temp = new char[dest.length + src.length - 1];
    System.arraycopy(dest, 0, temp, 0, offset);
    System.arraycopy(src, 0, temp, offset, src.length);
    System.arraycopy(dest, offset + 1, temp, src.length + offset, dest.length - offset - 1);
    return temp;
  }

  public static short[] insertAt(final short[] dest, final short[] src, final int offset) {
    final short[] temp = new short[dest.length + src.length - 1];
    System.arraycopy(dest, 0, temp, 0, offset);
    System.arraycopy(src, 0, temp, offset, src.length);
    System.arraycopy(dest, offset + 1, temp, src.length + offset, dest.length - offset - 1);
    return temp;
  }

  public static int[] insertAt(final int[] dest, final int[] src, final int offset) {
    final int[] temp = new int[dest.length + src.length - 1];
    System.arraycopy(dest, 0, temp, 0, offset);
    System.arraycopy(src, 0, temp, offset, src.length);
    System.arraycopy(dest, offset + 1, temp, src.length + offset, dest.length - offset - 1);
    return temp;
  }

  public static long[] insertAt(final long[] dest, final long[] src, final int offset) {
    final long[] temp = new long[dest.length + src.length - 1];
    System.arraycopy(dest, 0, temp, 0, offset);
    System.arraycopy(src, 0, temp, offset, src.length);
    System.arraycopy(dest, offset + 1, temp, src.length + offset, dest.length - offset - 1);
    return temp;
  }

  public static float[] insertAt(final float[] dest, final float[] src, final int offset) {
    final float[] temp = new float[dest.length + src.length - 1];
    System.arraycopy(dest, 0, temp, 0, offset);
    System.arraycopy(src, 0, temp, offset, src.length);
    System.arraycopy(dest, offset + 1, temp, src.length + offset, dest.length - offset - 1);
    return temp;
  }

  public static double[] insertAt(final double[] dest, final double[] src, final int offset) {
    final double[] temp = new double[dest.length + src.length - 1];
    System.arraycopy(dest, 0, temp, 0, offset);
    System.arraycopy(src, 0, temp, offset, src.length);
    System.arraycopy(dest, offset + 1, temp, src.length + offset, dest.length - offset - 1);
    return temp;
  }

  public static boolean[] insertAt(final boolean[] dest, final boolean[] src, final int offset) {
    final boolean[] temp = new boolean[dest.length + src.length - 1];
    System.arraycopy(dest, 0, temp, 0, offset);
    System.arraycopy(src, 0, temp, offset, src.length);
    System.arraycopy(dest, offset + 1, temp, src.length + offset, dest.length - offset - 1);
    return temp;
  }

  public static byte[] values(final Byte[] array) {
    final byte[] dest = new byte[array.length];
    for (int i = 0; i < array.length; ++i) {
      final Byte v = array[i];
      if (v != null) {
        dest[i] = v;
      }
    }
    return dest;
  }

  public static Byte[] valuesOf(final byte[] array) {
    final Byte[] dest = new Byte[array.length];
    for (int i = 0; i < array.length; ++i) {
      dest[i] = array[i];
    }
    return dest;
  }

  public static char[] values(final Character[] array) {
    final char[] dest = new char[array.length];
    for (int i = 0; i < array.length; ++i) {
      final Character v = array[i];
      if (v != null) {
        dest[i] = v;
      }
    }
    return dest;
  }

  public static Character[] valuesOf(final char[] array) {
    final Character[] dest = new Character[array.length];
    for (int i = 0; i < array.length; ++i) {
      dest[i] = array[i];
    }
    return dest;
  }

  public static short[] values(final Short[] array) {
    final short[] dest = new short[array.length];
    for (int i = 0; i < array.length; ++i) {
      final Short v = array[i];
      if (v != null) {
        dest[i] = v;
      }
    }
    return dest;
  }

  public static Short[] valuesOf(final short[] array) {
    final Short[] dest = new Short[array.length];
    for (int i = 0; i < array.length; ++i) {
      dest[i] = array[i];
    }
    return dest;
  }

  public static int[] values(final Integer[] array) {
    final int[] dest = new int[array.length];
    for (int i = 0; i < array.length; ++i) {
      final Integer v = array[i];
      if (v != null) {
        dest[i] = v;
      }
    }
    return dest;
  }

  public static Integer[] valuesOf(final int[] array) {
    final Integer[] dest = new Integer[array.length];
    for (int i = 0; i < array.length; ++i) {
      dest[i] = array[i];
    }
    return dest;
  }

  public static long[] values(final Long[] array) {
    final long[] dest = new long[array.length];
    for (int i = 0; i < array.length; ++i) {
      final Long v = array[i];
      if (v != null) {
        dest[i] = v;
      }
    }
    return dest;
  }

  public static Long[] valuesOf(final long[] array) {
    final Long[] dest = new Long[array.length];
    for (int i = 0; i < array.length; ++i) {
      dest[i] = array[i];
    }
    return dest;
  }

  public static float[] values(final Float[] array) {
    final float[] dest = new float[array.length];
    for (int i = 0; i < array.length; ++i) {
      final Float v = array[i];
      if (v != null) {
        dest[i] = v;
      }
    }
    return dest;
  }

  public static Float[] valuesOf(final float[] array) {
    final Float[] dest = new Float[array.length];
    for (int i = 0; i < array.length; ++i) {
      dest[i] = array[i];
    }
    return dest;
  }

  public static double[] values(final Double[] array) {
    final double[] dest = new double[array.length];
    for (int i = 0; i < array.length; ++i) {
      final Double v = array[i];
      if (v != null) {
        dest[i] = v;
      }
    }
    return dest;
  }

  public static Double[] valuesOf(final double[] array) {
    final Double[] dest = new Double[array.length];
    for (int i = 0; i < array.length; ++i) {
      dest[i] = array[i];
    }
    return dest;
  }

  public static boolean[] values(final Boolean[] array) {
    final boolean[] dest = new boolean[array.length];
    for (int i = 0; i < array.length; ++i) {
      final Boolean v = array[i];
      if (v != null) {
        dest[i] = v;
      }
    }
    return dest;
  }

  public static Boolean[] valuesOf(final boolean[] array) {
    final Boolean[] dest = new Boolean[array.length];
    for (int i = 0; i < array.length; ++i) {
      dest[i] = array[i];
    }
    return dest;
  }

  public static int indexOf(final byte[] array, final byte value) {
    for (int i = 0; i < array.length; ++i) {
      if (array[i] == value) {
        return i;
      }
    }
    return -1;
  }

  public static boolean contains(final byte[] array, final byte value) {
    return indexOf(array, value) != -1;
  }

  public static int indexOf(final byte[] array, final byte value, final int startIndex) {
    for (int i = startIndex; i < array.length; ++i) {
      if (array[i] == value) {
        return i;
      }
    }
    return -1;
  }

  public static int indexOf(final byte[] array, final byte value, final int startIndex, final int endIndex) {
    for (int i = startIndex; i < endIndex; ++i) {
      if (array[i] == value) {
        return i;
      }
    }
    return -1;
  }

  public static int indexOf(final char[] array, final char value) {
    for (int i = 0; i < array.length; ++i) {
      if (array[i] == value) {
        return i;
      }
    }
    return -1;
  }

  public static boolean contains(final char[] array, final char value) {
    return indexOf(array, value) != -1;
  }

  public static int indexOf(final char[] array, final char value, final int startIndex) {
    for (int i = startIndex; i < array.length; ++i) {
      if (array[i] == value) {
        return i;
      }
    }
    return -1;
  }

  public static int indexOf(final char[] array, final char value, final int startIndex, final int endIndex) {
    for (int i = startIndex; i < endIndex; ++i) {
      if (array[i] == value) {
        return i;
      }
    }
    return -1;
  }

  public static int indexOf(final short[] array, final short value) {
    for (int i = 0; i < array.length; ++i) {
      if (array[i] == value) {
        return i;
      }
    }
    return -1;
  }

  public static boolean contains(final short[] array, final short value) {
    return indexOf(array, value) != -1;
  }

  public static int indexOf(final short[] array, final short value, final int startIndex) {
    for (int i = startIndex; i < array.length; ++i) {
      if (array[i] == value) {
        return i;
      }
    }
    return -1;
  }

  public static int indexOf(final short[] array, final short value, final int startIndex, final int endIndex) {
    for (int i = startIndex; i < endIndex; ++i) {
      if (array[i] == value) {
        return i;
      }
    }
    return -1;
  }

  public static int indexOf(final int[] array, final int value) {
    for (int i = 0; i < array.length; ++i) {
      if (array[i] == value) {
        return i;
      }
    }
    return -1;
  }

  public static boolean contains(final int[] array, final int value) {
    return indexOf(array, value) != -1;
  }

  public static int indexOf(final int[] array, final int value, final int startIndex) {
    for (int i = startIndex; i < array.length; ++i) {
      if (array[i] == value) {
        return i;
      }
    }
    return -1;
  }

  public static int indexOf(final int[] array, final int value, final int startIndex, final int endIndex) {
    for (int i = startIndex; i < endIndex; ++i) {
      if (array[i] == value) {
        return i;
      }
    }
    return -1;
  }

  public static int indexOf(final long[] array, final long value) {
    for (int i = 0; i < array.length; ++i) {
      if (array[i] == value) {
        return i;
      }
    }
    return -1;
  }

  public static boolean contains(final long[] array, final long value) {
    return indexOf(array, value) != -1;
  }

  public static int indexOf(final long[] array, final long value, final int startIndex) {
    for (int i = startIndex; i < array.length; ++i) {
      if (array[i] == value) {
        return i;
      }
    }
    return -1;
  }

  public static int indexOf(final long[] array, final long value, final int startIndex, final int endIndex) {
    for (int i = startIndex; i < endIndex; ++i) {
      if (array[i] == value) {
        return i;
      }
    }
    return -1;
  }

  public static int indexOf(final boolean[] array, final boolean value) {
    for (int i = 0; i < array.length; ++i) {
      if (array[i] == value) {
        return i;
      }
    }
    return -1;
  }

  public static boolean contains(final boolean[] array, final boolean value) {
    return indexOf(array, value) != -1;
  }

  public static int indexOf(final boolean[] array, final boolean value, final int startIndex) {
    for (int i = startIndex; i < array.length; ++i) {
      if (array[i] == value) {
        return i;
      }
    }
    return -1;
  }

  public static int indexOf(final boolean[] array, final boolean value, final int startIndex, final int endIndex) {
    for (int i = startIndex; i < endIndex; ++i) {
      if (array[i] == value) {
        return i;
      }
    }
    return -1;
  }

  public static int indexOf(final float[] array, final float value) {
    for (int i = 0; i < array.length; ++i) {
      if (Float.compare(array[i], value) == 0) {
        return i;
      }
    }
    return -1;
  }

  public static boolean contains(final float[] array, final float value) {
    return indexOf(array, value) != -1;
  }

  public static int indexOf(final float[] array, final float value, final int startIndex) {
    for (int i = startIndex; i < array.length; ++i) {
      if (Float.compare(array[i], value) == 0) {
        return i;
      }
    }
    return -1;
  }

  public static int indexOf(final float[] array, final float value, final int startIndex, final int endIndex) {
    for (int i = startIndex; i < endIndex; ++i) {
      if (Float.compare(array[i], value) == 0) {
        return i;
      }
    }
    return -1;
  }

  public static int indexOf(final double[] array, final double value) {
    for (int i = 0; i < array.length; ++i) {
      if (Double.compare(array[i], value) == 0) {
        return i;
      }
    }
    return -1;
  }

  public static boolean contains(final double[] array, final double value) {
    return indexOf(array, value) != -1;
  }

  public static int indexOf(final double[] array, final double value, final int startIndex) {
    for (int i = startIndex; i < array.length; ++i) {
      if (Double.compare(array[i], value) == 0) {
        return i;
      }
    }
    return -1;
  }

  public static int indexOf(final double[] array, final double value, final int startIndex, final int endIndex) {
    for (int i = startIndex; i < endIndex; ++i) {
      if (Double.compare(array[i], value) == 0) {
        return i;
      }
    }
    return -1;
  }

  public static int indexOf(final Object[] array, final Object value) {
    for (int i = 0; i < array.length; ++i) {
      if (array[i].equals(value)) {
        return i;
      }
    }
    return -1;
  }

  public static boolean contains(final Object[] array, final Object value) {
    return indexOf(array, value) != -1;
  }

  public static int indexOf(final Object[] array, final Object value, final int startIndex) {
    for (int i = startIndex; i < array.length; ++i) {
      if (array[i].equals(value)) {
        return i;
      }
    }
    return -1;
  }

  public static boolean contains(final Object[] array, final Object value, final int startIndex) {
    return indexOf(array, value, startIndex) != -1;
  }

  public static int indexOf(final byte[] array, final byte[] sub) {
    return indexOf(array, sub, 0, array.length);
  }

  public static boolean contains(final byte[] array, final byte[] sub) {
    return indexOf(array, sub) != -1;
  }

  public static int indexOf(final byte[] array, final byte[] sub, final int startIndex) {
    return indexOf(array, sub, startIndex, array.length);
  }

  public static int indexOf(final byte[] array, final byte[] sub, final int startIndex, final int endIndex) {
    final int sublen = sub.length;
    if (sublen == 0) {
      return startIndex;
    }
    final int total = endIndex - sublen + 1;
    final byte c = sub[0];
    Label_0088:
    for (int i = startIndex; i < total; ++i) {
      if (array[i] == c) {
        for (int j = 1, k = i + 1; j < sublen; ++j, ++k) {
          if (sub[j] != array[k]) {
            continue Label_0088;
          }
        }
        return i;
      }
    }
    return -1;
  }

  public static int indexOf(final char[] array, final char[] sub) {
    return indexOf(array, sub, 0, array.length);
  }

  public static boolean contains(final char[] array, final char[] sub) {
    return indexOf(array, sub) != -1;
  }

  public static int indexOf(final char[] array, final char[] sub, final int startIndex) {
    return indexOf(array, sub, startIndex, array.length);
  }

  public static int indexOf(final char[] array, final char[] sub, final int startIndex, final int endIndex) {
    final int sublen = sub.length;
    if (sublen == 0) {
      return startIndex;
    }
    final int total = endIndex - sublen + 1;
    final char c = sub[0];
    Label_0088:
    for (int i = startIndex; i < total; ++i) {
      if (array[i] == c) {
        for (int j = 1, k = i + 1; j < sublen; ++j, ++k) {
          if (sub[j] != array[k]) {
            continue Label_0088;
          }
        }
        return i;
      }
    }
    return -1;
  }

  public static int indexOf(final short[] array, final short[] sub) {
    return indexOf(array, sub, 0, array.length);
  }

  public static boolean contains(final short[] array, final short[] sub) {
    return indexOf(array, sub) != -1;
  }

  public static int indexOf(final short[] array, final short[] sub, final int startIndex) {
    return indexOf(array, sub, startIndex, array.length);
  }

  public static int indexOf(final short[] array, final short[] sub, final int startIndex, final int endIndex) {
    final int sublen = sub.length;
    if (sublen == 0) {
      return startIndex;
    }
    final int total = endIndex - sublen + 1;
    final short c = sub[0];
    Label_0088:
    for (int i = startIndex; i < total; ++i) {
      if (array[i] == c) {
        for (int j = 1, k = i + 1; j < sublen; ++j, ++k) {
          if (sub[j] != array[k]) {
            continue Label_0088;
          }
        }
        return i;
      }
    }
    return -1;
  }

  public static int indexOf(final int[] array, final int[] sub) {
    return indexOf(array, sub, 0, array.length);
  }

  public static boolean contains(final int[] array, final int[] sub) {
    return indexOf(array, sub) != -1;
  }

  public static int indexOf(final int[] array, final int[] sub, final int startIndex) {
    return indexOf(array, sub, startIndex, array.length);
  }

  public static int indexOf(final int[] array, final int[] sub, final int startIndex, final int endIndex) {
    final int sublen = sub.length;
    if (sublen == 0) {
      return startIndex;
    }
    final int total = endIndex - sublen + 1;
    final int c = sub[0];
    Label_0088:
    for (int i = startIndex; i < total; ++i) {
      if (array[i] == c) {
        for (int j = 1, k = i + 1; j < sublen; ++j, ++k) {
          if (sub[j] != array[k]) {
            continue Label_0088;
          }
        }
        return i;
      }
    }
    return -1;
  }

  public static int indexOf(final long[] array, final long[] sub) {
    return indexOf(array, sub, 0, array.length);
  }

  public static boolean contains(final long[] array, final long[] sub) {
    return indexOf(array, sub) != -1;
  }

  public static int indexOf(final long[] array, final long[] sub, final int startIndex) {
    return indexOf(array, sub, startIndex, array.length);
  }

  public static int indexOf(final long[] array, final long[] sub, final int startIndex, final int endIndex) {
    final int sublen = sub.length;
    if (sublen == 0) {
      return startIndex;
    }
    final int total = endIndex - sublen + 1;
    final long c = sub[0];
    Label_0090:
    for (int i = startIndex; i < total; ++i) {
      if (array[i] == c) {
        for (int j = 1, k = i + 1; j < sublen; ++j, ++k) {
          if (sub[j] != array[k]) {
            continue Label_0090;
          }
        }
        return i;
      }
    }
    return -1;
  }

  public static int indexOf(final boolean[] array, final boolean[] sub) {
    return indexOf(array, sub, 0, array.length);
  }

  public static boolean contains(final boolean[] array, final boolean[] sub) {
    return indexOf(array, sub) != -1;
  }

  public static int indexOf(final boolean[] array, final boolean[] sub, final int startIndex) {
    return indexOf(array, sub, startIndex, array.length);
  }

  public static int indexOf(final boolean[] array, final boolean[] sub, final int startIndex, final int endIndex) {
    final int sublen = sub.length;
    if (sublen == 0) {
      return startIndex;
    }
    final int total = endIndex - sublen + 1;
    final boolean c = sub[0];
    Label_0088:
    for (int i = startIndex; i < total; ++i) {
      if (array[i] == c) {
        for (int j = 1, k = i + 1; j < sublen; ++j, ++k) {
          if (sub[j] != array[k]) {
            continue Label_0088;
          }
        }
        return i;
      }
    }
    return -1;
  }

  public static int indexOf(final float[] array, final float[] sub) {
    return indexOf(array, sub, 0, array.length);
  }

  public static boolean contains(final float[] array, final float[] sub) {
    return indexOf(array, sub) != -1;
  }

  public static int indexOf(final float[] array, final float[] sub, final int startIndex) {
    return indexOf(array, sub, startIndex, array.length);
  }

  public static int indexOf(final float[] array, final float[] sub, final int startIndex, final int endIndex) {
    final int sublen = sub.length;
    if (sublen == 0) {
      return startIndex;
    }
    final int total = endIndex - sublen + 1;
    final float c = sub[0];
    Label_0094:
    for (int i = startIndex; i < total; ++i) {
      if (Float.compare(array[i], c) == 0) {
        for (int j = 1, k = i + 1; j < sublen; ++j, ++k) {
          if (Float.compare(sub[j], array[k]) != 0) {
            continue Label_0094;
          }
        }
        return i;
      }
    }
    return -1;
  }

  public static int indexOf(final double[] array, final double[] sub) {
    return indexOf(array, sub, 0, array.length);
  }

  public static boolean contains(final double[] array, final double[] sub) {
    return indexOf(array, sub) != -1;
  }

  public static int indexOf(final double[] array, final double[] sub, final int startIndex) {
    return indexOf(array, sub, startIndex, array.length);
  }

  public static int indexOf(final double[] array, final double[] sub, final int startIndex, final int endIndex) {
    final int sublen = sub.length;
    if (sublen == 0) {
      return startIndex;
    }
    final int total = endIndex - sublen + 1;
    final double c = sub[0];
    Label_0094:
    for (int i = startIndex; i < total; ++i) {
      if (Double.compare(array[i], c) == 0) {
        for (int j = 1, k = i + 1; j < sublen; ++j, ++k) {
          if (Double.compare(sub[j], array[k]) != 0) {
            continue Label_0094;
          }
        }
        return i;
      }
    }
    return -1;
  }

  public static String toString(final Object[] array) {
    if (array == null) {
      return "null";
    }
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length; ++i) {
      if (i != 0) {
        sb.append(',');
      }
      sb.append(array[i]);
    }
    return sb.toString();
  }

  public static String toString(final String[] array) {
    if (array == null) {
      return "null";
    }
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length; ++i) {
      if (i != 0) {
        sb.append(',');
      }
      sb.append(array[i]);
    }
    return sb.toString();
  }

  public static String toString(final byte[] array) {
    if (array == null) {
      return "null";
    }
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length; ++i) {
      if (i != 0) {
        sb.append(',');
      }
      sb.append(array[i]);
    }
    return sb.toString();
  }

  public static String toString(final char[] array) {
    if (array == null) {
      return "null";
    }
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length; ++i) {
      if (i != 0) {
        sb.append(',');
      }
      sb.append(array[i]);
    }
    return sb.toString();
  }

  public static String toString(final short[] array) {
    if (array == null) {
      return "null";
    }
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length; ++i) {
      if (i != 0) {
        sb.append(',');
      }
      sb.append(array[i]);
    }
    return sb.toString();
  }

  public static String toString(final int[] array) {
    if (array == null) {
      return "null";
    }
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length; ++i) {
      if (i != 0) {
        sb.append(',');
      }
      sb.append(array[i]);
    }
    return sb.toString();
  }

  public static String toString(final long[] array) {
    if (array == null) {
      return "null";
    }
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length; ++i) {
      if (i != 0) {
        sb.append(',');
      }
      sb.append(array[i]);
    }
    return sb.toString();
  }

  public static String toString(final float[] array) {
    if (array == null) {
      return "null";
    }
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length; ++i) {
      if (i != 0) {
        sb.append(',');
      }
      sb.append(array[i]);
    }
    return sb.toString();
  }

  public static String toString(final double[] array) {
    if (array == null) {
      return "null";
    }
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length; ++i) {
      if (i != 0) {
        sb.append(',');
      }
      sb.append(array[i]);
    }
    return sb.toString();
  }

  public static String toString(final boolean[] array) {
    if (array == null) {
      return "null";
    }
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length; ++i) {
      if (i != 0) {
        sb.append(',');
      }
      sb.append(array[i]);
    }
    return sb.toString();
  }

  public static String[] toStringArray(final Object[] array) {
    if (array == null) {
      return null;
    }
    final String[] result = new String[array.length];
    for (int i = 0; i < array.length; ++i) {
      result[i] = StringUtil.toString(array[i]);
    }
    return result;
  }

  public static String[] toStringArray(final String[] array) {
    if (array == null) {
      return null;
    }
    final String[] result = new String[array.length];
    for (int i = 0; i < array.length; ++i) {
      result[i] = String.valueOf(array[i]);
    }
    return result;
  }

  public static String[] toStringArray(final byte[] array) {
    if (array == null) {
      return null;
    }
    final String[] result = new String[array.length];
    for (int i = 0; i < array.length; ++i) {
      result[i] = String.valueOf(array[i]);
    }
    return result;
  }

  public static String[] toStringArray(final char[] array) {
    if (array == null) {
      return null;
    }
    final String[] result = new String[array.length];
    for (int i = 0; i < array.length; ++i) {
      result[i] = String.valueOf(array[i]);
    }
    return result;
  }

  public static String[] toStringArray(final short[] array) {
    if (array == null) {
      return null;
    }
    final String[] result = new String[array.length];
    for (int i = 0; i < array.length; ++i) {
      result[i] = String.valueOf(array[i]);
    }
    return result;
  }

  public static String[] toStringArray(final int[] array) {
    if (array == null) {
      return null;
    }
    final String[] result = new String[array.length];
    for (int i = 0; i < array.length; ++i) {
      result[i] = String.valueOf(array[i]);
    }
    return result;
  }

  public static String[] toStringArray(final long[] array) {
    if (array == null) {
      return null;
    }
    final String[] result = new String[array.length];
    for (int i = 0; i < array.length; ++i) {
      result[i] = String.valueOf(array[i]);
    }
    return result;
  }

  public static String[] toStringArray(final float[] array) {
    if (array == null) {
      return null;
    }
    final String[] result = new String[array.length];
    for (int i = 0; i < array.length; ++i) {
      result[i] = String.valueOf(array[i]);
    }
    return result;
  }

  public static String[] toStringArray(final double[] array) {
    if (array == null) {
      return null;
    }
    final String[] result = new String[array.length];
    for (int i = 0; i < array.length; ++i) {
      result[i] = String.valueOf(array[i]);
    }
    return result;
  }

  public static String[] toStringArray(final boolean[] array) {
    if (array == null) {
      return null;
    }
    final String[] result = new String[array.length];
    for (int i = 0; i < array.length; ++i) {
      result[i] = String.valueOf(array[i]);
    }
    return result;
  }
}