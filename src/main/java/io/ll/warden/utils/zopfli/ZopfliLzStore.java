package io.ll.warden.utils.zopfli;

/**
 * Creator: eustas
 * Date: 3/26/2015
 * Project: Warden
 * Usage: Zopfli porting
 */
public class ZopfliLzStore {

  final char[] litLens;
  final char[] dists;
  int size;

  ZopfliLzStore(final int maxBlockSize) {
    litLens = new char[maxBlockSize];
    dists = new char[maxBlockSize];
  }

  final void append(final char length, final char dist) {
    litLens[size] = length;
    dists[size++] = dist;
  }

  final void reset() {
    size = 0;
  }

  final void copy(final ZopfliLzStore source) {
    size = source.size;
    System.arraycopy(source.litLens, 0, litLens, 0, size);
    System.arraycopy(source.dists, 0, dists, 0, size);
  }
}
