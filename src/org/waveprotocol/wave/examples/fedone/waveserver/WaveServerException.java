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
 * Generic exception for use in the wave server.
 *
 *
 */
class WaveServerException extends Exception {

  /** don't use the empty constructor. */
  @SuppressWarnings("unused")
  private WaveServerException() {}

  public WaveServerException(String message) {
    super(message);
  }

  public WaveServerException(Throwable cause) {
    super(cause);
  }

  public WaveServerException(String message, Throwable cause) {
    super(message, cause);
  }

}
