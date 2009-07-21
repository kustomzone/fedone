/**
 * Copyright 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.waveprotocol.wave.model.document.operation.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.waveprotocol.wave.model.util.Preconditions;

public abstract class ImmutableUpdateMap<T extends ImmutableUpdateMap<T, U>, U extends UpdateMap>
    implements UpdateMap {

  public static class AttributeUpdate {
    public final String name;
    final String oldValue;
    final String newValue;

    public AttributeUpdate(String name, String oldValue, String newValue) {
      Preconditions.checkNotNull(name, "Null name in AttributeUpdate");
      this.name = name;
      this.oldValue = oldValue;
      this.newValue = newValue;
    }

    @Override
    public String toString() {
      return "[" + name + ": " + oldValue + " -> " + newValue + "]";
    }
  }

  protected final List<AttributeUpdate> updates;

  @Override
  public int changeSize() {
    return updates.size();
  }

  @Override
  public String getChangeKey(int i) {
    return updates.get(i).name;
  }

  @Override
  public String getOldValue(int i) {
    return updates.get(i).oldValue;
  }

  @Override
  public String getNewValue(int i) {
    return updates.get(i).newValue;
  }

  public ImmutableUpdateMap() {
    updates = Collections.emptyList();
  }

  public ImmutableUpdateMap(String name, String oldValue, String newValue) {
    updates = Collections.singletonList(new AttributeUpdate(name, oldValue, newValue));
  }

  protected ImmutableUpdateMap(List<AttributeUpdate> updates) {
    this.updates = updates;
  }

  public T exclude(Collection<String> names) {
    List<AttributeUpdate> newAttributes = new ArrayList<AttributeUpdate>();
    for (AttributeUpdate update : updates) {
      if (!names.contains(update.name)) {
        newAttributes.add(update);
      }
    }
    return createFromList(newAttributes);
  }

  protected static final Comparator<AttributeUpdate> comparator = new Comparator<AttributeUpdate>() {
    @Override
    public int compare(AttributeUpdate a, AttributeUpdate b) {
      return a.name.compareTo(b.name);
    }
  };

  public T composeWith(U mutation) {
    List<AttributeUpdate> newAttributes = new ArrayList<AttributeUpdate>();
    Iterator<AttributeUpdate> iterator = updates.iterator();
    AttributeUpdate nextAttribute = iterator.hasNext() ? iterator.next() : null;
    // TODO: Have a slow path when the cast would fail.
    List<AttributeUpdate> mutationAttributes = ((ImmutableUpdateMap<?,?>) mutation).updates;
    loop: for (AttributeUpdate attribute : mutationAttributes) {
      while (nextAttribute != null) {
        int comparison = comparator.compare(attribute, nextAttribute);
        if (comparison < 0) {
          break;
        } else if (comparison > 0) {
          newAttributes.add(nextAttribute);
          nextAttribute = iterator.hasNext() ? iterator.next() : null;
        } else {
          if (!areEqual(nextAttribute.newValue, attribute.oldValue)) {
            throw new IllegalArgumentException("Mismatched old value: attempt to update " +
                nextAttribute + " with " + attribute);
          }
          newAttributes.add(new AttributeUpdate(attribute.name, nextAttribute.oldValue,
              attribute.newValue));
          nextAttribute = iterator.hasNext() ? iterator.next() : null;
          continue loop;
        }
      }
      newAttributes.add(attribute);
    }
    if (nextAttribute != null) {
      newAttributes.add(nextAttribute);
      while (iterator.hasNext()) {
        newAttributes.add(iterator.next());
      }
    }
    return createFromList(newAttributes);
  }

  protected abstract T createFromList(List<AttributeUpdate> attributes);

  // TODO: Is there a utility method for this somewhere?
  private boolean areEqual(Object a, Object b) {
    return (a == null) ? b == null : a.equals(b);
  }

  @Override
  public String toString() {
    return "Updates: " + updates;
  }
}