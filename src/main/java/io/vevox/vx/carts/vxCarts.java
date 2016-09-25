package io.vevox.vx.carts;

import io.vevox.vx.carts.cmd.CartsCommandDelegator;
import io.vevox.vx.carts.crafting.RecipesRules;
import io.vevox.vx.carts.rail.RailEventMonitor;
import io.vevox.vx.lib.vxPlugin;

/**
 * @author Matthew Struble
 */
public class vxCarts extends vxPlugin {

  public final String TAG_NAME;

  {
    TAG_NAME = getName();
    enable(l -> {
      logger().info("Hooking handlers and delegators...");
      getCommand("carts").setExecutor(new CartsCommandDelegator(this));
      getServer().getPluginManager().registerEvents(new RailEventMonitor(this), this);

      RecipesRules.registerRecipes();

      return true;
    });
  }

}
