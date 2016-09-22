package io.vevox.vx.carts;

import io.vevox.vx.carts.cmd.CartsCommandDelegator;
import io.vevox.vx.carts.crafting.RecipesRules;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Matthew Struble
 */
public class vxCarts extends JavaPlugin {

  @Override
  public void onEnable() {

    getLogger().info("Hooking handlers and delegators...");
    getCommand("carts").setExecutor(new CartsCommandDelegator(this));

    RecipesRules.registerRecipes();
  }

}
