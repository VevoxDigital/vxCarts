package io.vevox.vx.carts.cmd;

/**
 * Generic exception used in commands.
 * @author Matthew Struble
 * @since 1.10.2-r0.1
 */
abstract class CommandException extends Exception {

  private CommandException(String msg) { super(msg); }

  /**
   * Exception used when an argument is unknown.
   * @author Matthew Struble
   * @since 1.10.2-r0.1
   */
  static class UnknownArgumentException extends CommandException {
    UnknownArgumentException(int index, String arg) {
      super(String.format("Argument %d (\"%s\") is unknown", index + 1, arg));
    }
  }

  static class InsufficientPermissionsException extends CommandException {
    InsufficientPermissionsException(String permission) {
      super("Missing permission " + permission);
    }
  }

}
