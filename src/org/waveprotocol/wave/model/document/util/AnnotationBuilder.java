// Copyright 2009 Google Inc. All Rights Reserved.

package org.waveprotocol.wave.model.document.util;

import org.waveprotocol.wave.model.document.ReadableWDocument;
import org.waveprotocol.wave.model.document.operation.Nindo;

import org.waveprotocol.wave.model.util.Preconditions;

/**
 * A builder for applying annotations for a single key to a document in a linear order.
 *
 * This builder will generate the minimal mutation sequence to apply the requested
 * annotations.
 *
 * NOTE(user): This class is a temporary demo hack to minimise the number
 * of mutations emitted. It can modify the semantics of the intended operations
 * in the presence of transformation. Only use this class if you know what you
 * are doing.
 *
*
 */
@Deprecated
public class AnnotationBuilder<N, E extends N, T extends N> {
  /** The builder we will use to construct the annotation */
  private final Nindo.Builder b = new Nindo.Builder();
  /** The document we will be applying the ops to */
  private final ReadableWDocument<N,E,T> doc;
  /** The key that we are applying annotations for */
  private final String key;
  /** Accumulated skips that are yet to be applied */
  private int skipAccum;
  /** Current position in the range */
  private int currentPos;
  /** The limit of the range that we are applying to */
  private final int rangeEnd;
  /** Whether we have actually applied anything yet */
  private boolean dirty = false;

  /**
   * Construct a new annotation builder.
   *
   * @param doc indexed doc to apply the annotations to
   * @param rangeStart document location to begin applying annotations from
   * @param rangeEnd document location to apply annotations up to
   * @param key key to apply annotations for
   */
  public AnnotationBuilder(ReadableWDocument<N,E,T> doc, int rangeStart, int rangeEnd, String key) {
    this.doc = doc;
    this.skipAccum = rangeStart;
    this.currentPos = rangeStart;
    this.rangeEnd = rangeEnd;
    this.key = key;
  }

  private static boolean equal(String left, String right) {
    return left == null ? right == null : left.equals(right);
  }

  /**
   * Sets the annotation to the given value up to the given location.
   *
   * @param value value to set for the annotation
   * @param end absolute location to set up to
   */
  public void setUpTo(String value, int end) {
    Preconditions.checkPositionIndexes(currentPos, end, rangeEnd);
    while (currentPos < end) {
      String currentValue = doc.getAnnotation(currentPos, key);
      int nextChange = doc.firstAnnotationChange(currentPos, end, key, currentValue);
      if (nextChange == -1) {
        nextChange = end;
      }
      int size = nextChange - currentPos;
      if (size > 0) {
        if (!equal(currentValue, value)) {
          if (skipAccum > 0) {
            b.skip(skipAccum);
            skipAccum = 0;
          }
          b.startAnnotation(key, value);
          b.skip(size);
          b.endAnnotation(key);
          dirty = true;
        } else {
          skipAccum += size;
        }
      }
      currentPos = nextChange;
    }
    assert (currentPos == end);
  }

  /** @return the current position we have applied up to */
  public int getCurrentPos() {
    return currentPos;
  }

  /** @return true if we have build an annotation to apply */
  public boolean getDirty() {
    return dirty;
  }

  /** @return the built document mutation */
  public Nindo build() {
    return b.build();
  }
}
