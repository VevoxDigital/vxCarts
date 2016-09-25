package io.vevox.vx.carts.cmd;

import io.vevox.vx.carts.vxCarts;
import io.vevox.vx.lib.cmd.CommandDelegator;
import io.vevox.vx.lib.cmd.CommandException;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author Matthew Struble
 */
public class CartsCommandDelegator implements CommandDelegator {

  private final vxCarts plugin;

  public CartsCommandDelegator(vxCarts plugin) {
    this.plugin = plugin;
  }

  public void command(CommandSender sender, Command cmd, String... args) throws CommandException {
    if (args.length == 0) {
      command(sender, cmd, "help");
      return;
    }

    // Lower the case of the sub-command (so case matches), then switch
    switch (args[0].toLowerCase()) {
      case "reload":
        reloadConfig(sender);
        break;
      case "help":
      case "?":
        help(sender);
        break;
      case "rules":
      case "r":
        CartRulesCommand.command(sender, (String[]) ArrayUtils.subarray(args, 1, args.length));
        break;
      default:
        throw new CommandException.UnknownArgumentException(0, args[0]);
    }
  }

  private void reloadConfig(CommandSender sender) throws CommandException {
    if (!sender.hasPermission("vx.carts.admin"))
      throw new CommandException.InsufficientPermissionsException("to reload config");
    plugin.reloadConfig();
  }

  private void help(CommandSender sender) {
    sender.sendMessage("TODO Need to write help");
  }

}
