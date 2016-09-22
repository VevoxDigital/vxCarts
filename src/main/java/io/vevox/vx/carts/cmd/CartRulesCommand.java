package io.vevox.vx.carts.cmd;

import io.vevox.vx.carts.RulePrefix;
import io.vevox.vx.carts.crafting.RecipesRules;
import net.minecraft.server.v1_10_R1.EntityPlayer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Matthew Struble
 */
class CartRulesCommand {

  private static Pair<RulePrefix, Object> getState(String... args) throws CommandException {
    if (args.length == 0 || args[0].equals("clear")) return Pair.of(RulePrefix.DEFAULT, null);

    RulePrefix prefix;
    try {
      prefix = RulePrefix.valueOf(args[0].toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new CommandException.UnknownArgumentException(2, "Unknown rule prefix " + args[0]);
    }

    Object value = null;
    if (args.length > 1) {
      String arg = args[1];
      if (arg.startsWith("\"") && arg.endsWith("\"")) value = arg.substring(1, arg.length() - 1);
      else if (arg.startsWith("\'") && arg.endsWith("\'")) value = arg.substring(1, arg.length() - 1);
      else if (arg.equalsIgnoreCase("true") || arg.equalsIgnoreCase("yes")) value = true;
      else if (arg.equalsIgnoreCase("false") || arg.equalsIgnoreCase("no")) value = false;
      else try {
          value = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
          try {
            value = Double.parseDouble(arg);
          } catch (NumberFormatException e2) {
            value = arg;
          }
        }
    }

    if (!prefix.acceptsValue(value))
      throw new CommandException(prefix.toString() + " cannot accept the given value");
    return Pair.of(prefix, value);
  }

  @SuppressWarnings({"deprecation", "ConstantConditions"})
  static void command(CommandSender sender, String... args) throws CommandException {
    if (!(sender instanceof Player))
      throw new CommandException.ConsoleExecutionException();
    Player player = (Player) sender;
    if (args.length == 0)
      throw new CommandException.UnknownArgumentException(1, "");

    ItemStack rule = player.getItemInHand();
    if (!RecipesRules.isRule(rule))
      throw new CommandException("Must be holding rule(s) in main hand");

    EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
    net.minecraft.server.v1_10_R1.ItemStack nmsRule = entityPlayer.inventory.getItemInHand();

    if (args[0].equalsIgnoreCase("clear")) {
      RecipesRules.setState(nmsRule, getState(), true);
      RecipesRules.setState(nmsRule, getState(), false);
      RecipesRules.updateItemMeta(nmsRule);
    } else if (args[0].equalsIgnoreCase("condition") || args[0].equalsIgnoreCase("c")) {
      RecipesRules.setState(nmsRule, getState(ArrayUtils.subarray(args, 1, args.length)), true);
      RecipesRules.updateItemMeta(nmsRule);
    } else if (args[0].equalsIgnoreCase("function") || args[0].equalsIgnoreCase("f")) {
      RecipesRules.setState(nmsRule, getState(ArrayUtils.subarray(args, 1, args.length)), false);
      RecipesRules.updateItemMeta(nmsRule);
    } else if (args[0].equalsIgnoreCase("destroy")) {
      player.getWorld().dropItemNaturally(player.getLocation(), new ItemStack(Material.REDSTONE, nmsRule.count));
      RecipesRules.destroyRule(nmsRule);
    }
  }

}
