package io.vevox.vx.carts.cmd;

import io.vevox.vx.carts.vxCarts;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Matthew Struble
 */
public class CartsCommandDelegator implements CommandExecutor {

  private static class HelpEntry {
    final String cmd, desc, permission, usage;
    final boolean isPlayerOnly;

    HelpEntry(String cmd, String desc, String usage, String permission, boolean isPlayerOnly) {
      this.cmd = cmd;
      this.desc = desc;
      this.usage = usage;
      this.permission = permission;
      this.isPlayerOnly = isPlayerOnly;
    }
  }

  private final vxCarts plugin;
  private List<HelpEntry> helpEntries = new ArrayList<>();

  public CartsCommandDelegator(vxCarts plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
    if (cmd.getName().equals("carts")) {
      try {
        delegate(sender, cmd, args);
      } catch (CommandException e) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
            String.format("&c%s&r: %s", e.getClass().getSimpleName(), e.getMessage())));
      }
      return true;
    } else return false;
  }

  /**
   * Delegator for commands. Sends sub-commands to proper methods.
   * @param sender The command sender.
   * @param cmd The command.
   * @param args The command arguments.
   */
  private void delegate(CommandSender sender, Command cmd, String... args) throws CommandException {
    if (args.length == 0) {
      delegate(sender, cmd, "help");
      return;
    }

    // Lower the case of the sub-command (so case matches), then switch
    switch (args[0].toLowerCase()) {
      case "reload":
        reloadConfig(sender);
        break;
      // Default to throwing unknown arg
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

  }

}
