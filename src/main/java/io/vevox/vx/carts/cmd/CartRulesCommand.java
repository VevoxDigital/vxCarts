package io.vevox.vx.carts.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Matthew Struble
 */
class CartRulesCommand {

  static void command(CommandSender sender, String... args) throws CommandException {
    if (!(sender instanceof Player))
      throw new CommandException.ConsoleExecutionException();

  }

}
