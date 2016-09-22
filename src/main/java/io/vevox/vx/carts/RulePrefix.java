package io.vevox.vx.carts;

import javax.annotation.Nullable;

/**
 * @author Matthew Struble
 */
public enum RulePrefix {

  /**
   * Condition: Acts as the default option if no others pass.<br>
   * Function: Have no affect. The cart continues as normal.<br>
   * Accepts nothing.
   */
  DEFAULT;

  boolean isCondition() {
    switch (this) {
      default: return true;
    }
  }

  boolean isFunction() {
    switch (this) {
      default: return true;
    }
  }

  boolean acceptedValue(Object value) {
    switch (this) {
      default: return false;
    }
  }

}
