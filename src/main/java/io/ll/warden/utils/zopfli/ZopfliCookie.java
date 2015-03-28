package io.ll.warden.utils.zopfli;

/**
 * Creator: eustas
 * Date: 3/26/2015
 * Project: Warden
 * Usage: Zopfli porting
 */
public class ZopfliCookie {

  static class Node {

    int weight;
    Node tail;
    int count;
  }

  static final int SPLIT_PARTITIONS = 9;
  private static final int POOL_MAX = 10240;

  final static int[] intZeroes = new int[65536];
  final static char[] charZeroes = new char[65536];
  final static byte[] byteZeroes = new byte[65536];
  final static int[] intMOnes = new int[65536];
  final static char[] charOnes = new char[65536];
  final static long[] costMax = new long[65536];

  static {
    for (int i = 0; i < 64; ++i) {
      intMOnes[i] = -1;
      charOnes[i] = 1;
      costMax[i] = Long.MAX_VALUE;
    }
    expand(intMOnes);
    expand(charOnes);
    expand(costMax);
  }

  static void expand(Object array) {
    for (int i = 64; i < 65536; i = i + i) {
      System.arraycopy(array, 0, array, i, i);
    }
  }

  final Node[] list0 = new Node[15];
  final Node[] list1 = new Node[15];
  final Node[] leaves1 = new Node[288];
  final Node[] leaves2 = new Node[288];
  private final Node[] nodes = new Node[POOL_MAX];
  private int nextNode;

  //final Node[] leaves2 = new Node[288];

  /*private final static Comparator<Node> wc = new Comparator<Node>() {
    @Override
    public int compare(Node node, Node node2) {
      int r = node.weight - node2.weight;
      return r == 0 ? node.count - node2.count : r;
    }
  };*/

  final int[] i320a = new int[320];
  final int[] i320b = new int[320];
  final int[] i320c = new int[320];
  final int[] i288a = new int[288];
  final int[] i288b = new int[288];
  final int[] i288c = new int[288];
  final int[] i289a = new int[289];
  final char[] c259a = new char[259];
  final int[] i32a = new int[32];
  final int[] i32b = new int[32];
  final int[] i32c = new int[32];
  final int[] i19a = new int[19];
  final int[] i19b = new int[19];
  final int[] i19c = new int[19];
  final int[] i16a = new int[16];
  final int[] i16b = new int[16];


  final int[] p = new int[SPLIT_PARTITIONS];
  final int[] vp = new int[SPLIT_PARTITIONS];

  final char[] lengthArray; // unsigned short, but really values are 0..258 == MAX_MATCH
  final long[] costs;
  final char[] path;
  final int[] splitPoints;
  final int[] splitSize;


  final ZopfliSymbolStats stats = new ZopfliSymbolStats();
  final ZopfliSymbolStats bestStats = new ZopfliSymbolStats();
  final ZopfliSymbolStats lastStats = new ZopfliSymbolStats();
  final ZopfliHash h = new ZopfliHash();


  int lenVal;
  int distVal;
  int rnd = 42;


  final ZopfliLzStore store1;
  final ZopfliLzStore store2;
  final ZopfliLongestMatchCache lmc;


  final int masterBlockSize;

  final Node node(int weight, int count, Node tail) {
    Node result = nodes[nextNode++];
    result.weight = weight;
    result.count = count;
    result.tail = tail;
    return result;
  }

  final void resetPool() {
    nextNode = 0;
  }

  static void fill0(int[] array, int length) {
    int i = 0;
    while (i < length) {
      int j = i + 65536;
      if (j > length) {
        j = length;
      }
      System.arraycopy(intZeroes, 0, array, i, j - i);
      i = j;
    }
  }

  static void fill0(char[] array, int length) {
    int i = 0;
    while (i < length) {
      int j = i + 65536;
      if (j > length) {
        j = length;
      }
      System.arraycopy(charZeroes, 0, array, i, j - i);
      i = j;
    }
  }

  static void fillCostMax(long[] array, int length) {
    int i = 0;
    while (i < length) {
      int j = i + 65536;
      if (j > length) {
        j = length;
      }
      System.arraycopy(costMax, 0, array, i, j - i);
      i = j;
    }
  }

  /**
   * Maximum amount of blocks to split into. <p/> {@code 0} for unlimited.
   */
  final int blockSplittingMax = 15;

  ZopfliCookie(int masterBlockSize) { // TODO: + maxBlockSize?
    this.masterBlockSize = masterBlockSize;

    for (int i = 0; i < POOL_MAX; i++) {
      nodes[i] = new Node();
    }
    splitPoints = new int[blockSplittingMax + 1];
    splitSize = new int[blockSplittingMax + 1];

    lengthArray = new char[masterBlockSize + 1];  //  2
    costs = new long[masterBlockSize + 1];      //  8
    path = new char[masterBlockSize + 1];         //  2
    lmc = new ZopfliLongestMatchCache(masterBlockSize); // 28
    store1 = new ZopfliLzStore(masterBlockSize);        //  4
    store2 = new ZopfliLzStore(masterBlockSize);        //  4
    // 2 + 8 + 2 + 28 + 4 + 4 = 48
  }
}
