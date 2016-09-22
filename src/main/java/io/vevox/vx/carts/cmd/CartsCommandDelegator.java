package io.vevox.vx.carts.cmd;

import io.vevox.vx.carts.vxCarts;
import org.apache.commons.lang.ArrayUtils;
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
  private List<HelpEntry> help = new ArrayList<>();

  public CartsCommandDelegator(vxCarts plugin) {
    this.plugin = plugin;

    help.add(new HelpEntry("help", "Shows this message", "help", null, false));
    help.add(new HelpEntry("rules", "Creates/Modifies rules", "<condition=value> <state=value>", "rules", true));
    help.add(new HelpEntry("dest", "Sets or clears your destination", "[destiation]", null, true));
    help.add(new HelpEntry("reload", "Reloads the vxCarts config", "reload", "admin", false));
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
    if (cmd.getName().equals("carts")) {
      try {
        delegate(sender, args);
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
   * @param args The command arguments.
   */
  private void delegate(CommandSender sender, String... args) throws CommandException {
    if (args.length == 0) {
      delegate(sender, "help");
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
