package com.blueearthcat.dpbc.events;

import com.blueearthcat.dpbc.BlankCheck;
import com.darksoldier1404.dppc.api.essentials.MoneyAPI;
import com.darksoldier1404.dppc.api.inventory.DInventory;
import com.darksoldier1404.dppc.lang.DLang;
import com.darksoldier1404.dppc.utils.NBT;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DPBCEvent implements Listener {
    private static final BlankCheck plugin = BlankCheck.getInstance();
    private static final String prefix = plugin.data.getPrefix();
    private static final DLang lang = plugin.data.getLang();

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;
        if (e.getHand() == EquipmentSlot.OFF_HAND) return;
        if (e.getItem() == null) return;
        ItemStack item = e.getItem();
        if(NBT.hasTagKey(item, "dpbc_price")){
            e.setCancelled(true);
            if (e.getPlayer().isSneaking()){
                int price = NBT.getIntegerTag(item, "dpbc_price") * item.getAmount();
                MoneyAPI.addMoney(e.getPlayer(), price);
                item.setAmount(0);
                e.getPlayer().sendMessage(prefix + price + lang.get("add_money"));
            }
            else {
                int price = NBT.getIntegerTag(item, "dpbc_price");
                MoneyAPI.addMoney(e.getPlayer(), price);
                item.setAmount(item.getAmount() - 1);
                e.getPlayer().sendMessage(prefix + price + lang.get("add_money"));
            }
        }
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
        if (e.getInventory() instanceof DInventory){
            DInventory inv = (DInventory) e.getInventory();
            if (!inv.isValidHandler(plugin)) return;
            if (inv.getChannel() == 0){
                if (e.getInventory().getItem(13) == null) return;
                YamlConfiguration config = plugin.data.getConfig();
                config.set("Settings.CheckItem",e.getInventory().getItem(13));
                plugin.data.setConfig(config);
                plugin.data.save();
                e.getPlayer().sendMessage(prefix + lang.get("save_check_item"));
            }
            if (inv.getChannel() == 1){
                plugin.data.save();
                e.getPlayer().sendMessage(prefix + lang.get("save_config"));
            }
        }
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if (e.getInventory() instanceof DInventory){
            DInventory inv = (DInventory) e.getInventory();
            if (!inv.isValidHandler(plugin)) return;
            if (e.getCurrentItem() == null) return;
            ItemStack item = e.getCurrentItem();

            if (inv.getChannel() == 0){
                if (e.getClickedInventory() == null) return;
                if (e.getSlot() != 13 && e.getClickedInventory().getType() != InventoryType.PLAYER)
                    e.setCancelled(true);
            }
            if (inv.getChannel() == 1){
                e.setCancelled(true);
                if (NBT.hasTagKey(item, "dpbc_checkIssuer")){
                    ItemMeta im = item.getItemMeta();
                    if (item.getType() == Material.LIME_STAINED_GLASS_PANE){
                        item.setType(Material.RED_STAINED_GLASS_PANE);
                        im.setDisplayName(lang.get("config_check_issuer") + lang.get("config_check_false"));
                    }else{
                        item.setType(Material.LIME_STAINED_GLASS_PANE);
                        im.setDisplayName(lang.get("config_check_issuer") + lang.get("config_check_true"));
                    }
                    item.setItemMeta(im);
                    YamlConfiguration config = plugin.data.getConfig();
                    config.set("Settings.UseCheckIssuer",!config.getBoolean("Settings.UseCheckIssuer"));
                    plugin.data.setConfig(config);
                }
                if (NBT.hasTagKey(item, "dpbc_checkIssueDate")){
                    ItemMeta im = item.getItemMeta();
                    if (item.getType() == Material.LIME_STAINED_GLASS_PANE){
                        item.setType(Material.RED_STAINED_GLASS_PANE);
                        im.setDisplayName(lang.get("config_check_issuer") + lang.get("config_check_false"));
                    }else{
                        item.setType(Material.LIME_STAINED_GLASS_PANE);
                        im.setDisplayName(lang.get("config_check_issuer") + lang.get("config_check_true"));
                    }
                    item.setItemMeta(im);
                    YamlConfiguration config = plugin.data.getConfig();
                    config.set("Settings.UseCheckIssueDate",!config.getBoolean("Settings.UseCheckIssueDate"));
                    plugin.data.setConfig(config);
                }
            }
        }
    }
}
