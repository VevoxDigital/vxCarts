package io.vevox.vx.carts.rail;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * @author Matthew Struble
 */
@SuppressWarnings("WeakerAccess")
public class PlayerApplyRailRulesEvent extends PlayerEvent implements Cancellable {

  private static final HandlerList handlers = new HandlerList();

  private boolean cancel = false;
  private final Block rail;

  PlayerApplyRailRulesEvent(Player player, Block rail) {
    super(player);
    this.rail = rail;
  }

  public Block getRail() {
    return rail;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  @Override
  public boolean isCancelled() {
    return cancel;
  }

  @Override
  public void setCancelled(boolean b) {
    cancel = b;
  }
}
