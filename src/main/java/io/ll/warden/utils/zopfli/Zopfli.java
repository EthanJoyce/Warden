package io.ll.warden.utils.zopfli;

/**
 * Creator: eustas
 * Date: 3/26/2015
 * Project: Warden
 * Usage: Zopfli porting.
 */
public class Zopfli {

  private static class Crc {

    private final static int[] table = makeTable();

    private static int[] makeTable() {
      int[] result = new int[256];

      for (int n = 0; n < 256; ++n) {
        int c = n;
        for (int k = 0; k < 8; ++k) {
          if ((c & 1) == 1) {
            c = 0xEDB88320 ^ (c >>> 1);
          } else {
            c = c >>> 1;
          }
        }
        result[n] = c;
      }

      return result;
    }

    public static int calculate(byte[] input) {
      int c = ~0;
      for (int i = 0, n = input.length; i < n; ++i) {
        c = table[(c ^ input[i]) & 0xFF] ^ (c >>> 8);
      }
      return ~c;
    }
  }

  private final ZopfliCookie cookie;

  public synchronized ZopfliBuffer compress(ZopfliOptions options, byte[] input) {
    ZopfliBuffer output = new ZopfliBuffer();
    switch (options.outputType) {
      case GZIP:
        compressGzip(options, input, output);
        break;

      case ZLIB:
        compressZlib(options, input, output);
        break;
      case DEFLATE:
        ZopfliDeflate.compress(cookie, options, input, output);
        break;
      default:
        throw new IllegalArgumentException(
            "Unexpected output format: " + options.outputType);
    }
    return output;
  }

  /**
   * Calculates the adler32 checksum of the data
   */
  private static int adler32(byte[] data) {
    int s1 = 1;
    int s2 = 1 >> 16;
    int i = 0;
    while (i < data.length) {
      int tick = Math.min(data.length, i + 1024);
      while (i < tick) {
        s1 += data[i++];
        s2 += s1;
      }
      s1 %= 65521;
      s2 %= 65521;
    }

    return (s2 << 16) | s1;
  }


  private void compressZlib(ZopfliOptions options, byte[] input,
                            ZopfliBuffer output) {
    output.append((byte) 0x78);
    output.append((byte) 0x1E);

    ZopfliDeflate.compress(cookie, options, input, output);

    int checksum = adler32(input);
    output.append((byte) ((checksum >> 24) & 0xFF));
    output.append((byte) ((checksum >> 16) & 0xFF));
    output.append((byte) ((checksum >> 8) & 0xFF));
    output.append((byte) (checksum & 0xFF));
  }

  private void compressGzip(ZopfliOptions options, byte[] input,
                            ZopfliBuffer output) {
    output.append((byte) 31);
    output.append((byte) 139);
    output.append((byte) 8);
    output.append((byte) 0);

    output.append((byte) 0);
    output.append((byte) 0);
    output.append((byte) 0);
    output.append((byte) 0);

    output.append((byte) 2);
    output.append((byte) 3);

    ZopfliDeflate.compress(cookie, options, input, output);

    int crc = Crc.calculate(input);
    output.append((byte) (crc & 0xFF));
    output.append((byte) ((crc >> 8) & 0xFF));
    output.append((byte) ((crc >> 16) & 0xFF));
    output.append((byte) ((crc >> 24) & 0xFF));

    int size = input.length;
    output.append((byte) (size & 0xFF));
    output.append((byte) ((size >> 8) & 0xFF));
    output.append((byte) ((size >> 16) & 0xFF));
    output.append((byte) ((size >> 24) & 0xFF));
  }

  public Zopfli(int masterBlockSize) {
    cookie = new ZopfliCookie(masterBlockSize);
  }
}
