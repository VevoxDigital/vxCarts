package io.vevox.vx.carts.crafting;

import io.vevox.vx.carts.RulePrefix;
import net.minecraft.server.v1_10_R1.*;
import net.minecraft.server.v1_10_R1.ItemStack;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.inventory.*;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * @author Matthew Struble
 */
public class RecipesRules {

  public static void updateItemMeta(org.bukkit.inventory.ItemStack item) {
    updateItemMeta(CraftItemStack.asNMSCopy(item));
  }

  private static String makePrefixString(NBTTagCompound compound, boolean isCondition) {
    StringBuilder builder = new StringBuilder();
    System.out.println(compound.getString("prefix"));
    System.out.println(compound.get("prefix").toString());
    RulePrefix prefix = RulePrefix.valueOf(compound.getString("prefix"));
    builder.append(ChatColor.YELLOW)
        .append(isCondition ? "IF" : "THEN").append(' ')
        .append(ChatColor.GRAY)
        .append(prefix.toString().toLowerCase());
    if (compound.hasKey("value"))
      builder.append('=').append(compound.get("value").toString());
    return builder.toString();
}

  private static void updateItemMeta(ItemStack item) {
    if (item.getTag() == null) return;
    NBTTagCompound rules = item.getTag().getCompound("cartRule");
    if (rules == null) return;

    NBTTagCompound display = new NBTTagCompound();
    display.set("Name", new NBTTagString(ChatColor.BLUE + "vxCarts Rule"));

    NBTTagList lore = new NBTTagList();
    lore.add(new NBTTagString(makePrefixString(rules.getCompound("condition"), true)));
    lore.add(new NBTTagString(makePrefixString(rules.getCompound("function"), false)));
    display.set("Lore", lore);

    item.getTag().set("display", display);
  }

  public static void registerRecipes() {
    // Create a rule
    // TODO Make the rule crafting item configurable.
    ItemStack newRule = new ItemStack(Items.PAPER);
    NBTTagCompound rule = new NBTTagCompound();
    NBTTagCompound c = new NBTTagCompound();
    c.set("prefix", new NBTTagString(RulePrefix.DEFAULT.toString()));
    rule.set("condition", c);
    rule.set("function", c.g());
    NBTTagCompound tag = new NBTTagCompound();
    tag.set("cartRule", rule);
    newRule.setTag(tag);
    updateItemMeta(newRule);
    CraftingManager.getInstance().registerShapelessRecipe(newRule, Items.REDSTONE, Items.PAPER);

  }

  static class CopyRule implements IRecipe {
    // matches
    // Checks if crafting window is valid.
    @Override
    public boolean a(InventoryCrafting inventoryCrafting, World world) {
      return false;
    }

    @Nullable
    @Override
    public ItemStack craftItem(InventoryCrafting inventoryCrafting) {
      return new ItemStack(Block.getByName("stone"));
    }

    // getRecipeSize
    // Gets the size of the recipe area
    @Override
    public int a() {
      return 0;
    }

    // getRecipeOutput
    @Nullable
    @Override
    public ItemStack b() {
      return null;
    }

    // getRemainingItems
    @Override
    public net.minecraft.server.v1_10_R1.ItemStack[] b(InventoryCrafting inventoryCrafting) {
      return new net.minecraft.server.v1_10_R1.ItemStack[0];
    }

    @Override
    public Recipe toBukkitRecipe() {
      return null;
    }

    @Override
    public List<net.minecraft.server.v1_10_R1.ItemStack> getIngredients() {
      return Arrays.asList(new ItemStack(Item.REGISTRY.get(new MinecraftKey("paper"))));
    }
  }

}
