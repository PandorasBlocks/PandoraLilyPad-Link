package me.DawnBudgie.PandoraLilyPad;

/*
 * @Author DawnBudgie
 * 
 * PandoraLilyPad Server App
 * 
 */
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class PandoraLilyPad
  extends JavaPlugin
  implements CommandExecutor, Listener
{
  public Inventory compassInv;
  public ItemStack compassItem;
  public HashMap<String, String> itemsAndCommands;
  public FileConfiguration config;
  public FileConfiguration admin;
  
  public void onEnable()
  {
    this.config = getConfig();
    saveDefaultConfig();
    
    getServer().getPluginManager().registerEvents(new LilyPadCompassListener(this), this);
    

    this.compassInv = makeInventory(this.config);
    this.compassItem = makeItem(this.config);
    this.itemsAndCommands = makeCommandList(this.config, this.compassInv);
  }
  
  private Inventory makeInventory(FileConfiguration fileConf)
  {
    int slots = fileConf.getInt("global.slots");
    if (slots % 9 != 0) {
      slots = 9;
    }
    String title = ChatColor.translateAlternateColorCodes('&', fileConf.getString("global.invTitle"));
    
    return getServer().createInventory(null, slots, title);
  }
  
  private ItemStack makeItem(FileConfiguration fileConf)
  {
    ItemStack item = new ItemStack(Material.COMPASS, 1);
    
    Material mat = Material.matchMaterial(this.config.getString("item.type"));
    if (mat != null) {
      item.setType(mat);
    }
    ItemMeta meta = item.getItemMeta();
    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', fileConf.getString("item.title")));
    if (getConfig().getString("AllowCompassLore").equalsIgnoreCase("true")) {
      meta.setLore(Arrays.asList(new String[] { ChatColor.translateAlternateColorCodes('&', fileConf.getString("item.lore")) }));
    }
    item.setItemMeta(meta);
    return item;
  }
  
  @SuppressWarnings("unused")
private Material Material(String string)
  {
    return null;
  }
  
  private HashMap<String, String> makeCommandList(FileConfiguration fileConf, Inventory inv)
  {
    HashMap<String, String> map = new HashMap<String, String>();
    for (String key : fileConf.getConfigurationSection("commands").getKeys(false))
    {
      Material mat = Material.matchMaterial(key.toUpperCase());
      if (mat == null)
      {
        getLogger().info("The item " + key + " is unknown and was skipped!");
      }
      else
      {
        map.put(mat.name(), fileConf.getString("commands." + key + ".command"));
        

        ItemStack item = new ItemStack(mat, 1);
        
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', fileConf.getString("commands." + key + ".title")));
        if (getConfig().getString("AllowItemLore").equalsIgnoreCase("true")) {
          meta.setLore(Arrays.asList(new String[] { ChatColor.translateAlternateColorCodes('&', fileConf.getString("commands." + key + ".lore")) }));
        }
        item.setItemMeta(meta);
        if (inv.firstEmpty() != -1) {
          inv.addItem(new ItemStack[] { item });
        }
      }
    }
    return map;
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
  {
    if ((sender instanceof Player))
    {
      Player p = (Player)sender;
      if (cmd.getName().equalsIgnoreCase("compass")) {
        if (p.hasPermission("lpc.item"))
        {
          if (p.getInventory().firstEmpty() != -1) {
            p.getInventory().addItem(new ItemStack[] { this.compassItem });
          }
          sender.sendMessage(ChatColor.GREEN + "The compass has been added.");
        }
        else
        {
          sender.sendMessage(ChatColor.RED + "You do not have permission.");
        }
      }
    }
    return true;
  }
}