package io.ll.warden.utils.zopfli;

/**
 * Creator: eustas
 * Date: 3/26/2015
 * Project: Warden
 * Usage: zopfli porting
 */
public class ZopfliBuffer {

  public byte[] data;
  public int size;
  private int bp;

  ZopfliBuffer() {
    data = new byte[65536];
  }

  void append(byte value) {
    if (size == data.length) {
      byte[] copy = new byte[size * 2];
      System.arraycopy(data, 0, copy, 0, size);
      data = copy;
    }
    data[size++] = value;
  }

  void addBits(int symbol, int length) {
    for (int i = 0; i < length; i++) {
      if (bp == 0) {
        append((byte) 0);
      }
      int bit = (symbol >> i) & 1;
      data[size - 1] |= bit << bp;
      bp = (bp + 1) & 7;
    }
  }

  void addHuffmanBits(int symbol, int length) {
    for (int i = 0; i < length; i++) {
      if (bp == 0) {
        append((byte) 0);
      }
      int bit = (symbol >> (length - i - 1)) & 1;
      data[size - 1] |= bit << bp;
      bp = (bp + 1) & 7;
    }
  }
}
