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

package org.waveprotocol.wave.examples.fedone.waveserver;

/**
 * Indicates a caller tried to submit or request deltas for a
 * participant that is not a particiant on the wavelet.
 *
 *
 *
 */
public class AccessControlException extends WaveServerException {
  public AccessControlException(String message) {
    super(message);
  }
  public AccessControlException(Throwable cause) {
    super(cause);
  }
  public AccessControlException(String message, Throwable cause) {
    super(message, cause);
  }
}
