/**
 * Copyright 2010 The PlayN Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package playn.core;

/**
 * Simple PlayN logging interface.
 */
public abstract class Log {

  private Collector collector;
  private Level minLevel = Level.DEBUG;

  /** Tags a log message with a level. */
  public static enum Level { DEBUG, INFO, WARN, ERROR }

  /** Allows for collection of log messages (in addition to standard logging).
    * See {@link #setCollector}. */
  public static interface Collector {
    /**
     * Called when a message is logged.
     *
     * @param level the level at which the message was logged.
     * @param msg the message that was logged.
     * @param e the exception logged with the message, or null.
     */
    void logged(Level level, String msg, Throwable e);
  }

  /**
   * Configures a log message collector. This allows games to intercept (and record and submit with
   * bug reports, for example) all messages logged via the PlayN logging system. This will include
   * errors logged internally by PlayN code.
   */
  public void setCollector (Collector collector) {
    this.collector = collector;
  }

  /**
   * Configures the minimum log level that will be logged. Messages at a level lower than
   * {@code level} will be suppressed. Note that all messages are still passed to any registered
   * {@link Collector}, but suppressed messages are not sent to the platform logging system.
   */
  public void setMinLevel (Level level) {
    assert level != null;
    minLevel = level;
  }

  /** Logs {@code msg} at the debug level. */
  public void debug (String msg) {
    debug(msg, (Throwable)null);
  }

  /** Logs {@code msg} at the debug level. */
  public void debug (String msg, Object... args) {
    debug(format(msg, args), (Throwable)null);
  }

  /** Logs {@code msg} and {@code e} at the debug level. */
  public void debug (String msg, Throwable e) {
    log(Level.DEBUG, msg, e);
  }

  /** Logs {@code msg} at the info level. */
  public void info (String msg) {
    info(msg, (Throwable)null);
  }

  /** Logs {@code msg} at the info level. */
  public void info (String msg, Object... args) {
    info(format(msg, args), (Throwable)null);
  }

  /** Logs {@code msg} and {@code e} at the info level. */
  public void info (String msg, Throwable e) {
    log(Level.INFO, msg, e);
  }

  /** Logs {@code msg} at the warn level. */
  public void warn (String msg) {
    warn(msg, (Throwable)null);
  }

  /** Logs {@code msg} at the warn level. */
  public void warn (String msg, Object... args) {
    warn(format(msg, args), (Throwable)null);
  }

  /** Logs {@code msg} and {@code e} at the warn level. */
  public void warn (String msg, Throwable e) {
    log(Level.WARN, msg, e);
  }

  /** Logs {@code msg} at the error level. */
  public void error (String msg) {
    error(msg, (Throwable)null);
  }

  /** Logs {@code msg} at the error level. */
  public void error (String msg, Object... args) {
    error(format(msg, args), (Throwable)null);
  }

  /** Logs {@code msg} and {@code e} at the error level. */
  public void error (String msg, Throwable e) {
    log(Level.ERROR, msg, e);
  }

  protected String format (String msg, Object[] args) {
    return msg; // TODO
  }

  protected void log (Level level, String msg, Throwable e) {
    if (collector != null) collector.logged(level, msg, e);
    if (level.ordinal() >= minLevel.ordinal()) logImpl(level, msg, e);
  }

  protected abstract void logImpl (Level level, String msg, Throwable e);
}
