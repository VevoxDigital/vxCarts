package io.vevox.vx.carts;

import io.vevox.vx.carts.cmd.CartsCommandDelegator;
import io.vevox.vx.carts.crafting.RecipesRules;
import io.vevox.vx.lib.vxPlugin;

/**
 * @author Matthew Struble
 */
public class vxCarts extends vxPlugin {

  {
    enable(l -> {
      logger().info("Hooking handlers and delegators...");
      getCommand("carts").setExecutor(new CartsCommandDelegator(this));

      RecipesRules.registerRecipes();

      return true;
    });
  }

}
