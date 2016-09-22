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
  DEFAULT,

  /**
   * Condition: Checks the player's destination<br>
   * Function: Sets the player's destination<br>
   * Accepts a string or null.
   */
  DESTINATION;

  public boolean acceptsValue(@Nullable Object value) {
    switch (this) {
      case DESTINATION: return value == null || value instanceof String;
      default: return value == null;
    }
  }

}
