package me.DawnBudgie.PandoraLilyPad;
/*
 * @Author DawnBudgie
 * 
 * PandoraLilyPad Server App
 * 
 */

import org.bukkit.event.Listener;

import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class LilyPadCompassListener
  implements Listener
{
  private PandoraLilyPad plugin;
  
  public LilyPadCompassListener(PandoraLilyPad instance)
  {
    this.plugin = instance;
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent event)
  {
    if (event.getInventory().getTitle().equals(this.plugin.compassInv.getTitle()))
    {
      fireGUI(event, this.plugin.itemsAndCommands);
      event.setCancelled(true);
    }
  }
  
  private void fireGUI(InventoryClickEvent event, HashMap<String, String> map)
  {
    Player player = (Player)event.getWhoClicked();
    if (event.getCurrentItem() == null)
    {
      event.setCancelled(true);
      return;
    }
    String item = event.getCurrentItem().getType().name();
    if (map.containsKey(item))
    {
      String command = (String)map.get(item);
      
      event.setCancelled(true);
      player.closeInventory();
      player.performCommand(command);
    }
  }
  
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event)
  {
    Player player = event.getPlayer();
    if (!player.hasPlayedBefore()) {
      player.getInventory().addItem(new ItemStack[] { this.plugin.compassItem });
    } else if (!player.getInventory().contains(this.plugin.compassItem)) {
      event.getPlayer().getInventory().addItem(new ItemStack[] { this.plugin.compassItem });
    }
  }
  
  @EventHandler
  public void onItemDrop(PlayerDropItemEvent event)
  {
    if (event.getItemDrop().getItemStack().equals(this.plugin.compassItem)) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event)
  {
    Player player = event.getPlayer();
    if (event.getItem() == null) {
      return;
    }
    if ((event.getItem().equals(this.plugin.compassItem)) && (
      (event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK))) {
      player.openInventory(this.plugin.compassInv);
    }
  }
}