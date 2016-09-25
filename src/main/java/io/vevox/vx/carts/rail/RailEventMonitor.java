package io.vevox.vx.carts.rail;

import io.vevox.vx.carts.crafting.RecipesRules;
import io.vevox.vx.carts.vxCarts;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_10_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @author Matthew Struble
 */
@SuppressWarnings("unused")
public class RailEventMonitor implements Listener {

  private vxCarts plugin;

  public RailEventMonitor(vxCarts plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent e) {
    if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
    if (e.getClickedBlock().getState().getType() != Material.ACTIVATOR_RAIL) return;

    EntityPlayer player = ((CraftPlayer) e.getPlayer()).getHandle();
    // TODO Check for rulebook not rule
    if (!RecipesRules.isRule(player.getItemInMainHand())
        && !RecipesRules.isRule(player.getItemInOffHand())) return;

    PlayerApplyRailRulesEvent applyEvent = new PlayerApplyRailRulesEvent(e.getPlayer(), e.getClickedBlock());
    plugin.getServer().getPluginManager().callEvent(applyEvent);

    if (!applyEvent.isCancelled()) {
      Location loc = e.getClickedBlock().getLocation();
      RailRules.apply(
          player.getWorld(), new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()),
          RecipesRules.isRule(player.getItemInMainHand()) ? player.getItemInMainHand() : player.getItemInOffHand());
    }
  }

}
