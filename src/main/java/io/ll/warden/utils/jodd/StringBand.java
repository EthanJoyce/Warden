package io.ll.warden.utils.jodd;

public class StringBand
{
  private static final int DEFAULT_ARRAY_CAPACITY = 16;
  private String[] array;
  private int index;
  private int length;

  public StringBand() {
    super();
    this.array = new String[16];
  }

  public StringBand(final int initialCapacity) {
    super();
    if (initialCapacity <= 0) {
      throw new IllegalArgumentException("Invalid initial capacity");
    }
    this.array = new String[initialCapacity];
  }

  public StringBand(final String s) {
    this();
    this.array[0] = s;
    this.index = 1;
    this.length = s.length();
  }

  public StringBand(final Object o) {
    this(String.valueOf(o));
  }

  public StringBand append(final boolean b) {
    return this.append(b ? "true" : "false");
  }

  public StringBand append(final double d) {
    return this.append(Double.toString(d));
  }

  public StringBand append(final float f) {
    return this.append(Float.toString(f));
  }

  public StringBand append(final int i) {
    return this.append(Integer.toString(i));
  }

  public StringBand append(final long l) {
    return this.append(Long.toString(l));
  }

  public StringBand append(final short s) {
    return this.append(Short.toString(s));
  }

  public StringBand append(final char c) {
    return this.append(String.valueOf(c));
  }

  public StringBand append(final byte b) {
    return this.append(Byte.toString(b));
  }

  public StringBand append(final Object obj) {
    return this.append(String.valueOf(obj));
  }

  public StringBand append(String s) {
    if (s == null) {
      s = "null";
    }
    if (this.index >= this.array.length) {
      this.expandCapacity();
    }
    this.array[this.index++] = s;
    this.length += s.length();
    return this;
  }

  public int capacity() {
    return this.array.length;
  }

  public int length() {
    return this.length;
  }

  public int index() {
    return this.index;
  }

  public void setIndex(final int newIndex) {
    if (newIndex < 0) {
      throw new ArrayIndexOutOfBoundsException(newIndex);
    }
    if (newIndex > this.array.length) {
      final String[] newArray = new String[newIndex];
      System.arraycopy(this.array, 0, newArray, 0, this.index);
      this.array = newArray;
    }
    if (newIndex > this.index) {
      for (int i = this.index; i < newIndex; ++i) {
        this.array[i] = "";
      }
    }
    else if (newIndex < this.index) {
      for (int i = newIndex; i < this.index; ++i) {
        this.array[i] = null;
      }
    }
    this.index = newIndex;
    this.length = this.calculateLength();
  }

  public char charAt(final int pos) {
    int len = 0;
    for (int i = 0; i < this.index; ++i) {
      final int newlen = len + this.array[i].length();
      if (pos < newlen) {
        return this.array[i].charAt(pos - len);
      }
      len = newlen;
    }
    throw new IllegalArgumentException("Invalid char index");
  }

  public String stringAt(final int index) {
    if (index >= this.index) {
      throw new ArrayIndexOutOfBoundsException();
    }
    return this.array[index];
  }

  public String toString() {
    if (this.index == 0) {
      return "";
    }
    final char[] destination = new char[this.length];
    int start = 0;
    for (int i = 0; i < this.index; ++i) {
      final String s = this.array[i];
      final int len = s.length();
      s.getChars(0, len, destination, start);
      start += len;
    }
    return new String(destination);
  }

  protected void expandCapacity() {
    final String[] newArray = new String[this.array.length << 1];
    System.arraycopy(this.array, 0, newArray, 0, this.index);
    this.array = newArray;
  }

  protected int calculateLength() {
    int len = 0;
    for (int i = 0; i < this.index; ++i) {
      len += this.array[i].length();
    }
    return len;
  }
}
