package io.ll.warden.utils.zopfli;

import io.ll.warden.utils.zopfli.ZopfliCookie.Node;

/**
 * Creator: eustas
 * Date: 3/26/2015
 * Project: Warden
 * Usage: Zopfli porting
 */
public class ZopfliKatajainen {

  static void lengthLimitedCodeLengths(ZopfliCookie cookie, int[] frequencies, int maxBits,
                                       int[] bitLengths) {
    cookie.resetPool();
    int n = frequencies.length;
    int nn = 0;
    Node[] leaves = cookie.leaves1;
    for (int i = 0; i < n; i++) {
      if (frequencies[i] != 0) {
        leaves[nn] = cookie.node(frequencies[i], i, null);
        nn++;
      }
    }

    if (nn == 0) {
      return;
    }
    if (nn == 1) {
      bitLengths[leaves[0].count] = 1;
      return;
    }

    Node[] leaves2 = cookie.leaves2;
    System.arraycopy(leaves, 0, leaves2, 0, nn);
    sort(leaves2, leaves, 0, nn);

    Node[] list0 = cookie.list0;
    Node node0 = cookie.node(leaves[0].weight, 1, null);

    Node[] list1 = cookie.list1;
    Node node1 = cookie.node(leaves[1].weight, 2, null);

    for (int i = 0; i < maxBits; ++i) {
      list0[i] = node0;
      list1[i] = node1;
    }

    int numBoundaryPmRuns = 2 * nn - 4;
    for (int i = 0; i < numBoundaryPmRuns; i++) {
      boolean last = i == numBoundaryPmRuns - 1;
      boundaryPm(cookie, leaves, list0, list1, nn, maxBits - 1, last);
    }

    for (Node node = list1[maxBits - 1]; node != null; node = node.tail) {
      for (int i = node.count - 1; i >= 0; --i) {
        bitLengths[leaves[i].count]++;
      }
    }
  }

  private static void boundaryPm(ZopfliCookie cookie, Node[] leaves, Node[] list0, Node[] list1,
                                 int numSymbols, int index,
                                 boolean last) {
    int lastCount = list1[index].count;

    if (index == 0 && lastCount >= numSymbols) {
      return;
    }

    list0[index] = list1[index];

    if (index == 0) {
      list1[index] = cookie.node(leaves[lastCount].weight, lastCount + 1, null);
    } else {
      int sum = list0[index - 1].weight + list1[index - 1].weight;
      if (lastCount < numSymbols && sum > leaves[lastCount].weight) {
        list1[index] = cookie.node(leaves[lastCount].weight, lastCount + 1, list1[index].tail);
      } else {
        list1[index] = cookie.node(sum, lastCount, list1[index - 1]);
        if (!last) {
          boundaryPm(cookie, leaves, list0, list1, numSymbols, index - 1, false);
          boundaryPm(cookie, leaves, list0, list1, numSymbols, index - 1, false);
        }
      }
    }
  }

  private static void sort(Node[] src, Node[] dest, int low, int high) {
    int length = high - low;

    if (length < 7) {
      for (int i = low + 1; i < high; i++) {
        for (int j = i, k = i - 1; j > low && (dest[k].weight > dest[j].weight); --j, --k) {
          Node t = dest[j];
          dest[j] = dest[k];
          dest[k] = t;
        }
      }
      return;
    }

    int mid = (low + high) >>> 1;
    sort(dest, src, low, mid);
    sort(dest, src, mid, high);

    for (int i = low, p = low, q = mid; i < high; i++) {
      if (q >= high || p < mid && (src[p].weight <= src[q].weight)) {
        dest[i] = src[p++];
      } else {
        dest[i] = src[q++];
      }
    }
  }
}
