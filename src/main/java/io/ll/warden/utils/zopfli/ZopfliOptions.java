package io.ll.warden.utils.zopfli;

/**
 * Creator: eustas
 * Date: 3/26/2015
 * Project: Warden
 * Usage: Zopfli porting
 */
public class ZopfliOptions {

  public static enum BlockSplitting {
    FIRST,
    LAST,
    NONE
  }

  public static enum OutputFormat {
    DEFLATE,
    GZIP,
    ZLIB
  }

  public final int numIterations;
  public final BlockSplitting blockSplitting;
  public final OutputFormat outputType;

  public ZopfliOptions(OutputFormat outputType, BlockSplitting blockSplitting,
                 int numIterations) {
    this.outputType = outputType;
    this.blockSplitting = blockSplitting;
    this.numIterations = numIterations;
  }

  public ZopfliOptions() {
    this(OutputFormat.GZIP, BlockSplitting.FIRST, 15);
  }
}
