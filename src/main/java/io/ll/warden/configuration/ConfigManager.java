package io.ll.warden.configuration;

import java.util.ArrayList;
import java.util.List;

import io.ll.warden.utils.zopfli.Zopfli;
import io.ll.warden.utils.zopfli.ZopfliBuffer;
import io.ll.warden.utils.zopfli.ZopfliOptions;
import io.ll.warden.utils.zopfli.ZopfliOptions.BlockSplitting;
import io.ll.warden.utils.zopfli.ZopfliOptions.OutputFormat;

/**
 * Creator: LordLambda
 * Date: 3/28/2015
 * Project: Warden
 * Usage: Configuration Manager
 */
public class ConfigManager {

  private static ConfigManager instance;
  private Zopfli zopfli;
  private ZopfliOptions compression;
  private ZopfliOptions decompression;

  protected ConfigManager() {
    zopfli = new Zopfli(8 * 1024 * 1024);
    compression = new ZopfliOptions(OutputFormat.ZLIB, BlockSplitting.FIRST, 15);
    decompression = new ZopfliOptions(OutputFormat.DEFLATE, BlockSplitting.FIRST, 15);
  }

  public static ConfigManager get() {
    if(instance == null) {
      synchronized(ConfigManager.class) {
        if(instance == null) {
          instance = new ConfigManager();
        }
      }
    }
    return instance;
  }

  public byte[] decompress(byte[] data) {
    List<Byte[]> toDecomp = splitByteArrayToLength(data, 65536);
    byte[] origdata = new byte[]{};
    for(Byte[] b : toDecomp) {
      ZopfliBuffer zb = zopfli.compress(decompression, toUnwrappedByte(b));
      origdata = concat(origdata, zb.data);
    }
    return origdata;
  }

  private List<Byte[]> splitByteArrayToLength(byte[] original, int size) {
    List<Byte[]> retV = new ArrayList<Byte[]>();
    if(original.length <= size) {
      retV.add(toWrappedByte(original));
    }else {
      if(original.length % size == 0) {
        for(int i = 0; i < original.length;) {
          byte[] buffer = new byte[size];
          for(int j = 0; j < size; ++i, ++j) {
            buffer[j] = original[i];
          }
          retV.add(toWrappedByte(buffer));
        }
      }else {
        int difference = original.length - size;
        int i = 0;
        for(; i < original.length;) {
          byte[] buffer = new byte[size];
          for(int j = 0; j < size; ++i, ++j) {
            buffer[j] = original[i];
          }
          retV.add(toWrappedByte(buffer));
        }
        byte[] b = new byte[difference];
        for(int j = 0; j < difference; ++j, ++i) {
          b[j] = original[i];
        }
        retV.add(toWrappedByte(b));
      }
    }
    return retV;
  }

  private Byte[] toWrappedByte(byte[] b) {
    Byte[] B = new Byte[b.length];
    for(int i = 0; i < b.length; ++i) {
      B[i] = b[i];
    }
    return B;
  }

  private byte[] toUnwrappedByte(Byte[] b) {
    byte[] B = new byte[b.length];
    for(int i = 0; i < b.length; ++i) {
      B[i] = b[i];
    }
    return B;
  }

  private byte[] concat(byte[] a1, byte[] a2) {
    byte[] b = new byte[a1.length + a2.length];
    int j = 0;
    for(int i = 0; i < a1.length; ++i, ++j) {
      b[j] = a1[i];
    }
    for(int i = 0; i < a2.length; ++i, ++j) {
      b[j] = a2[i];
    }
    return b;
  }
}
