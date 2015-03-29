package io.ll.warden.utils.jodd;

public class HashCode
{
  private static final int C1 = -862048943;
  private static final int C2 = 461845907;
  public static final int SEED = 173;
  public static final int PRIME = 37;

  public static int smear(final int hashCode) {
    return 461845907 * Integer.rotateLeft(hashCode * -862048943, 15);
  }

  public static int hash(final int seed, final boolean aBoolean) {
    return 37 * seed + (aBoolean ? 1231 : 1237);
  }

  public static int hash(int seed, final boolean[] booleanArray) {
    if (booleanArray == null) {
      return 0;
    }
    for (final boolean aBoolean : booleanArray) {
      seed = hash(seed, aBoolean);
    }
    return seed;
  }

  public static int hashBooleanArray(final int seed, final boolean... booleanArray) {
    return hash(seed, booleanArray);
  }

  public static int hash(final int seed, final char aChar) {
    return 37 * seed + aChar;
  }

  public static int hash(int seed, final char[] charArray) {
    if (charArray == null) {
      return 0;
    }
    for (final char aChar : charArray) {
      seed = hash(seed, aChar);
    }
    return seed;
  }

  public static int hashCharArray(final int seed, final char... charArray) {
    return hash(seed, charArray);
  }

  public static int hash(final int seed, final int anInt) {
    return 37 * seed + anInt;
  }

  public static int hash(int seed, final int[] intArray) {
    if (intArray == null) {
      return 0;
    }
    for (final int anInt : intArray) {
      seed = hash(seed, anInt);
    }
    return seed;
  }

  public static int hashIntArray(final int seed, final int... intArray) {
    return hash(seed, intArray);
  }

  public static int hash(int seed, final short[] shortArray) {
    if (shortArray == null) {
      return 0;
    }
    for (final short aShort : shortArray) {
      seed = hash(seed, aShort);
    }
    return seed;
  }

  public static int hashShortArray(final int seed, final short... shortArray) {
    return hash(seed, shortArray);
  }

  public static int hash(int seed, final byte[] byteArray) {
    if (byteArray == null) {
      return 0;
    }
    for (final byte aByte : byteArray) {
      seed = hash(seed, aByte);
    }
    return seed;
  }

  public static int hashByteArray(final int seed, final byte... byteArray) {
    return hash(seed, byteArray);
  }

  public static int hash(final int seed, final long aLong) {
    return 37 * seed + (int)(aLong ^ aLong >>> 32);
  }

  public static int hash(int seed, final long[] longArray) {
    if (longArray == null) {
      return 0;
    }
    for (final long aLong : longArray) {
      seed = hash(seed, aLong);
    }
    return seed;
  }

  public static int hashLongArray(final int seed, final long... longArray) {
    return hash(seed, longArray);
  }

  public static int hash(final int seed, final float aFloat) {
    return hash(seed, Float.floatToIntBits(aFloat));
  }

  public static int hash(int seed, final float[] floatArray) {
    if (floatArray == null) {
      return 0;
    }
    for (final float aFloat : floatArray) {
      seed = hash(seed, aFloat);
    }
    return seed;
  }

  public static int hashFloatArray(final int seed, final float... floatArray) {
    return hash(seed, floatArray);
  }

  public static int hash(final int seed, final double aDouble) {
    return hash(seed, Double.doubleToLongBits(aDouble));
  }

  public static int hash(int seed, final double[] doubleArray) {
    if (doubleArray == null) {
      return 0;
    }
    for (final double aDouble : doubleArray) {
      seed = hash(seed, aDouble);
    }
    return seed;
  }

  public static int hashDoubleArray(final int seed, final double... doubleArray) {
    return hash(seed, doubleArray);
  }

  public static int hash(final int seed, final Object aObject) {
    int result = seed;
    if (aObject == null) {
      result = hash(result, 0);
    }
    else if (!aObject.getClass().isArray()) {
      result = hash(result, aObject.hashCode());
    }
    else {
      final Object[] objects = (Object[])aObject;
      for (int length = objects.length, idx = 0; idx < length; ++idx) {
        result = hash(result, objects[idx]);
      }
    }
    return result;
  }
}
