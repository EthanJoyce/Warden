package io.ll.warden.utils.zopfli;

/**
 * Creator: eustas
 * Date: 3/26/2015
 * Project: Warden
 * Usage: zopfli porting
 */
public class ZopfliBlockSplitter {

  static int split(ZopfliCookie cookie, byte[] input, int from, int to) {
    ZopfliLzStore store = cookie.store1;
    store.reset();
    ZopfliDeflate.greedy(cookie, null, input, from, to, store);

    int nPoints = splitLz(cookie, store.litLens, store.dists, store.size);

    int pos = from;
    char[] dists = store.dists;
    char[] litLens = store.litLens;
    int[] points = cookie.splitPoints;

    points[0] = pos;
    for (int i = 0, j = 1; j <= nPoints; ++j) {
      for (int pj = points[j]; i < pj; ++i) {
        pos += (dists[i] == 0) ? 1 : litLens[i];
      }
      points[j] = pos;
    }
    return nPoints;
  }

  // TODO: May be use some kind of SORTED data-structure for splitPoints?
  static int splitLz(ZopfliCookie cookie, char[] litLens, char[] dists, int llSize) {
    int[] splitPoints = cookie.splitPoints;
    int[] splitSize = cookie.splitSize;
    splitPoints[0] = 0;
    splitSize[0] = ZopfliDeflate.calculateBlockSize(cookie, litLens, dists, 0, llSize);
    splitPoints[1] = llSize;
    splitSize[1] = -1;
    int numBlocks = 1;
    int maxBlocks = cookie.blockSplittingMax;

    if (llSize < 10) {
      return numBlocks;
    }

    int lStart = 0;
    int lEnd = llSize;
    int blockN = 0;
    while (numBlocks < maxBlocks) {
      int llPos = findMinimum(cookie, litLens, dists, lStart, lEnd);

      int splitL = ZopfliDeflate.calculateBlockSize(cookie, litLens, dists, lStart, llPos);
      int splitR = ZopfliDeflate.calculateBlockSize(cookie, litLens, dists, llPos, lEnd);

      if (splitL + splitR > splitSize[blockN] || llPos == lStart + 1 || llPos == lEnd) {
        splitSize[blockN] = -1;
      } else {
        splitSize[blockN] = splitL;
        numBlocks++;
        blockN++;
        System.arraycopy(splitPoints, blockN, splitPoints, blockN + 1, numBlocks - blockN);
        System.arraycopy(splitSize, blockN, splitSize, blockN + 1, numBlocks - blockN);
        splitPoints[blockN] = llPos;
        splitSize[blockN] = splitR;
      }

      int longest = 0;
      boolean found = false;
      for (int i = 0; i < numBlocks; i++) {
        int start = splitPoints[i];
        int end = splitPoints[i + 1];
        if ((splitSize[i] != -1) && end - start > longest) {
          lStart = start;
          lEnd = end;
          found = true;
          longest = end - start;
          blockN = i;
        }
      }
      if (!found) {
        break;
      }

      if (lEnd - lStart < 10) {
        break;
      }
    }

    return numBlocks;
  }

  private static int findMinimum(ZopfliCookie cookie, char[] litLens, char[] dists, int from,
                                 int to) {
    int start = from + 1;
    int end = to;
    if (end - start < 1024) {
      int best = Integer.MAX_VALUE;
      int result = start;
      for (int i = start; i < end; i++) {
        int v = ZopfliDeflate.calculateBlockSize(cookie, litLens, dists, from, i)
                + ZopfliDeflate.calculateBlockSize(cookie, litLens, dists, i, to);
        if (v < best) {
          best = v;
          result = i;
        }
      }
      return result;
    } else {
      int n = ZopfliCookie.SPLIT_PARTITIONS;
      int[] p = cookie.p;
      int[] vp = cookie.vp;
      int lastBest = Integer.MAX_VALUE;
      int pos = start;

      while (true) {
        if (end - start <= n) {
          break;
        }

        for (int i = 0; i < n; i++) {
          p[i] = start + (i + 1) * ((end - start) / (n + 1));
          vp[i] = ZopfliDeflate.calculateBlockSize(cookie, litLens, dists, from, p[i])
                  + ZopfliDeflate.calculateBlockSize(cookie, litLens, dists, p[i], to);
        }
        int bestI = 0;
        int best = vp[0];
        for (int i = 1; i < n; i++) {
          if (vp[i] < best) {
            best = vp[i];
            bestI = i;
          }
        }
        if (best > lastBest) {
          break;
        }

        start = bestI == 0 ? start : p[bestI - 1];
        end = bestI == n - 1 ? end : p[bestI + 1];

        pos = p[bestI];
        lastBest = best;
      }
      return pos;
    }
  }

}