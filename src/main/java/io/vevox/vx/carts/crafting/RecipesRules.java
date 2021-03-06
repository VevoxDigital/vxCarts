package io.vevox.vx.carts.crafting;

import io.vevox.vx.carts.RulePrefix;
import net.minecraft.server.v1_10_R1.*;
import net.minecraft.server.v1_10_R1.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.inventory.*;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Matthew Struble
 */
public class RecipesRules {

  public static boolean isRule(org.bukkit.inventory.ItemStack item) {
    return isRule(CraftItemStack.asNMSCopy(item));
  }

  public static boolean isRule(ItemStack item) {
    return item.getTag() != null && item.getTag().get("cartRule") != null;
  }

  public static void setState(ItemStack item, Pair<RulePrefix, Object> state, boolean isCondition) {
    if (!isRule(item))
      throw new IllegalArgumentException("Given item is not a rule");
    RulePrefix prefix = state.getKey();
    Object value = state.getValue();
    if (!prefix.acceptsValue(value))
      throw new IllegalArgumentException(prefix.toString() + " does not accept " + value.toString());

    @SuppressWarnings("ConstantConditions")
    NBTTagCompound rule = item.getTag().getCompound("cartRule")
        .getCompound(isCondition ? "condition" : "function");

    rule.remove("value");
    rule.setString("prefix", prefix.name());
    if (value != null) {
      if (value instanceof Boolean) rule.setBoolean("value", (Boolean) value);
      else if (value instanceof Integer) rule.setInt("value", (Integer) value);
      else if (value instanceof Double) rule.setDouble("value", (Double) value);
      else rule.setString("value", value.toString());
    }
  }

  public static void updateItemMeta(ItemStack item) {
    if (item.getTag() == null) return;
    NBTTagCompound rules = item.getTag().getCompound("cartRule");
    if (rules == null) return;

    NBTTagCompound display = new NBTTagCompound();
    display.set("Name", new NBTTagString(ChatColor.RESET + "Track Rule"));

    NBTTagList lore = new NBTTagList();
    lore.add(new NBTTagString(makePrefixString(rules.getCompound("condition"), true)));
    lore.add(new NBTTagString(makePrefixString(rules.getCompound("function"), false)));
    lore.add(new NBTTagString(ChatColor.BLUE.toString() + ChatColor.ITALIC + "vxCarts"));
    display.set("Lore", lore);

    item.getTag().set("display", display);
  }

  @SuppressWarnings("ConstantConditions")
  public static void destroyRule(ItemStack stack) throws IllegalArgumentException {
    if (!isRule(stack))
      throw new IllegalArgumentException("Target stack is not a rule");
    int count = stack.count;
    stack.count = 1;
    stack.getTag().remove("display");
    stack.getTag().remove("cartRule");
    stack.count = count;
  }

  private static String makePrefixString(NBTTagCompound compound, boolean isCondition) {
    StringBuilder builder = new StringBuilder();
    RulePrefix prefix = RulePrefix.valueOf(compound.getString("prefix"));
    builder.append(ChatColor.YELLOW)
        .append(isCondition ? "IF" : "THEN").append(' ')
        .append(ChatColor.GRAY)
        .append(prefix.toString().toLowerCase());
    if (compound.hasKey("value"))
      builder.append('=').append(compound.get("value") instanceof NBTTagString
          ? "\"" + compound.getString("value") + "\"" : compound.get("value").toString());
    return builder.toString();
  }

  private static ItemStack getDefaultRule() {
    // TODO Make the rule crafting item configurable.
    ItemStack newRule = new ItemStack(Items.PAPER);
    NBTTagCompound rule = new NBTTagCompound();
    NBTTagCompound c = new NBTTagCompound();
    c.set("prefix", new NBTTagString(RulePrefix.DEFAULT.name()));
    rule.set("condition", c);
    rule.set("function", c.g());
    NBTTagCompound tag = new NBTTagCompound();
    tag.set("cartRule", rule);
    newRule.setTag(tag);
    updateItemMeta(newRule);
    return newRule;
  }

  public static void registerRecipes() {
    CraftingManager.getInstance().registerShapelessRecipe(getDefaultRule(), Items.REDSTONE, Items.PAPER);
    CraftingManager.getInstance().a(new CopyRule());
  }

  private static class CopyRule implements IRecipe {

    @Nullable
    private ItemStack[] getRuleAndBlank(InventoryCrafting inventoryCrafting) {
      ItemStack rule = null, blank = null;
      ItemStack redstone = null;

      for (int i = 0; i < inventoryCrafting.getSize(); i++) {
        ItemStack stack = inventoryCrafting.getItem(i);
        if (stack != null) {
          if (stack.getItem() == Items.REDSTONE) {
            if (redstone != null) return null;
            redstone = stack;
            continue;
          }
          if (stack.getItem() != Items.PAPER) return null;
          if (rule != null && blank != null) return null;

          NBTTagCompound tag = stack.getTag();
          boolean flag = tag != null && tag.get("cartRule") != null;

          if (rule != null) {
            if (flag) return null;
            blank = stack;
          } else if (blank != null) {
            if (!flag) return null;
            rule = stack;
          } else if (flag) rule = stack;
          else blank = stack;
        }
      }

      if (rule == null || blank == null || redstone == null) return null;
      return new ItemStack[]{ rule, blank };
    }

    // matches
    // Checks if crafting window is valid.
    @Override
    public boolean a(InventoryCrafting inventoryCrafting, World world) {
      return getRuleAndBlank(inventoryCrafting) != null;
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    @Override
    public ItemStack craftItem(InventoryCrafting inventoryCrafting) {
      return getRuleAndBlank(inventoryCrafting)[0].cloneItemStack();
    }

    // getRecipeSize
    // Gets the size of the recipe area
    @Override
    public int a() {
      return 4;
    }

    // getRecipeOutput
    @Nullable
    @Override
    public ItemStack b() {
      return getDefaultRule();
    }

    // getRemainingItems
    @Override
    public net.minecraft.server.v1_10_R1.ItemStack[] b(InventoryCrafting inventoryCrafting) {
      ItemStack[] remaining = new ItemStack[inventoryCrafting.getSize()];
      for (int i = 0; i < remaining.length; i++) {
        ItemStack stack = inventoryCrafting.getItem(i);
        if (stack != null && stack.getTag() != null && stack.getTag().get("cartRule") != null) {
          remaining[i] = stack.cloneItemStack();
          remaining[i].count = 1;
        }
      }
      return remaining;
    }

    @Override
    public Recipe toBukkitRecipe() {
      return new ShapelessRecipe(CraftItemStack.asBukkitCopy(b()));
    }

    @Override
    public List<net.minecraft.server.v1_10_R1.ItemStack> getIngredients() {
      return Arrays.asList(
          new ItemStack(Items.PAPER),
          getDefaultRule()
      );
    }
  }

}
