package com.blueearthcat.dpbc.functions;

import com.blueearthcat.dpbc.BlankCheck;
import com.darksoldier1404.dppc.api.inventory.DInventory;
import com.darksoldier1404.dppc.builder.item.ItemBuilder;
import com.darksoldier1404.dppc.lang.DLang;
import com.darksoldier1404.dppc.utils.InventoryUtils;
import com.darksoldier1404.dppc.utils.NBT;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.darksoldier1404.dppc.api.essentials.MoneyAPI.hasEnoughMoney;
import static com.darksoldier1404.dppc.api.essentials.MoneyAPI.takeMoney;

public class DPBCFunction {
    private static final BlankCheck plugin = BlankCheck.getInstance();
    private static final DLang lang = plugin.data.getLang();
    private static final String prefix = plugin.data.getPrefix();
    private static final YamlConfiguration config = plugin.data.getConfig();

    public static void init(){
        if (config.getItemStack("Settings.CheckItem") == null) {
            YamlConfiguration data = config;
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta im = item.getItemMeta();
            im.setDisplayName("§f<check_price>$ Check");
            List<String> lore = new ArrayList<>();
            lore.add("§7");
            lore.add("§7<check_issuer>");
            lore.add("§7<check_issue_date>");
            im.setLore(lore);
            item.setItemMeta(im);
            data.set("Settings.CheckItem", item);
            plugin.data.setConfig(data);
            plugin.data.save();
        }
    }
    public static ItemStack setItemName(ItemStack item, String name){
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(name);
        item.setItemMeta(im);
        return item;
    }
    public static void openItemSettingGUI(Player p) {
        DInventory inv = new DInventory(lang.get("item_setting_title"), 27, plugin);
        for (int i = 0; i < inv.getSize(); i++) {
            if (i == 13) continue;
            ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            inv.setItem(i,setItemName(item,"§r"));
        }
        inv.setItem(13, config.getItemStack("Settings.CheckItem"));
        inv.setChannel(0);
        p.openInventory(inv.getInventory());
    }

    public static void openConfigSettingGUI(Player p) {
        DInventory inv = new DInventory(lang.get("config_setting_title"), 27, plugin);
        for (int i = 0; i < inv.getSize(); i++) {
            if (i == 12 || i == 14) continue;
            ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            ItemMeta im = item.getItemMeta();
            im.setDisplayName("§r");
            item.setItemMeta(im);
            inv.setItem(i, item);
        }
        ItemStack item = NBT.setStringTag(new ItemBuilder(new ItemStack(Material.GLASS_PANE))
                .setDisplayName(lang.get("config_check_issuer") + lang.get("config_check_true"))
                .addLore("")
                .addLore(lang.get("config_check_issuer_lore"))
                .build(), "dpbc_checkIssuer", "true");
        ItemMeta im = item.getItemMeta();
        if (config.getBoolean("Settings.UseCheckIssuer")) {
            item.setType(Material.LIME_STAINED_GLASS_PANE);
            im.setDisplayName(lang.get("config_check_issuer") + lang.get("config_check_true"));
        } else {
            item.setType(Material.RED_STAINED_GLASS_PANE);
            im.setDisplayName(lang.get("config_check_issuer") + lang.get("config_check_false"));
        }
        item.setItemMeta(im);
        inv.setItem(12, item);
        item = NBT.setStringTag(new ItemBuilder(new ItemStack(Material.LIME_STAINED_GLASS_PANE))
                .setDisplayName(lang.get("config_check_issue_date") + lang.get("config_check_true"))
                .addLore("")
                .addLore(lang.get("config_check_issue_date_lore"))
                .build(), "dpbc_checkIssueDate", "true");
        im = item.getItemMeta();
        if (config.getBoolean("Settings.UseCheckIssueDate")) {
            item.setType(Material.LIME_STAINED_GLASS_PANE);
            im.setDisplayName(lang.get("config_check_issue_date") + lang.get("config_check_true"));
        } else {
            item.setType(Material.RED_STAINED_GLASS_PANE);
            im.setDisplayName(lang.get("config_check_issue_date") + lang.get("config_check_false"));
        }
        item.setItemMeta(im);
        inv.setItem(14, item);
        inv.setChannel(1);
        p.openInventory(inv.getInventory());
    }

    public static void giveCheck(Player p, String a, String b) {
        int price, amount = 1;
        try {
            price = Integer.parseInt(a);
        } catch (NumberFormatException e){
            p.sendMessage(prefix + lang.get("check_price_wrong"));
            return;
        }
        if (b!=null) {
            try {
                amount = Integer.parseInt(b);
            } catch (NumberFormatException e){
                p.sendMessage(prefix + lang.get("check_amount_wrong"));
                return;
            }
        }
        ItemStack checkItem = NBT.setIntTag(config.getItemStack("Settings.CheckItem").clone(), "dpbc_price", price);
        ItemMeta im = checkItem.getItemMeta();
        String name = im.getDisplayName();
        if (name.contains("<check_price>")) name = name.replace("<check_price>", Integer.toString(price));
        if (name.contains("<check_issuer>")) name = name.replace("<check_issuer>", config.getBoolean("Settings.UseCheckIssuer") ? p.getName() : "");
        if (name.contains("<check_issue_date>"))
            name = name.replace("<check_issue_date>", config.getBoolean("Settings.UseCheckIssueDate") ? LocalDateTime.now().format(DateTimeFormatter.ofPattern(config.getString("Settings.DateFormat"))) : "");
        im.setDisplayName(name);
        if (im.hasLore()) {
            List<String> lore = im.getLore();
            for (int i = 0; i < lore.size(); i++) {
                if (lore.get(i).contains("<check_price>"))
                    lore.set(i, lore.get(i).replace("<check_price>", Integer.toString(price)));
                if (lore.get(i).contains("<check_issuer>"))
                    lore.set(i, lore.get(i).replace("<check_issuer>", config.getBoolean("Settings.UseCheckIssuer")? p.getName() : ""));
                if (lore.get(i).contains("<check_issue_date>"))
                    lore.set(i, lore.get(i).replace("<check_issue_date>", config.getBoolean("Settings.UseCheckIssueDate")? LocalDateTime.now().format(DateTimeFormatter.ofPattern(config.getString("Settings.DateFormat"))) : ""));

            }
            im.setLore(lore);
        }
        checkItem.setItemMeta(im);
        checkItem.setAmount(amount);
        if (!hasEnoughMoney(p, price*amount)) {
            p.sendMessage(prefix + lang.get("check_no_money"));
            return;
        }
        if (!InventoryUtils.hasEnoughSpace(p.getInventory().getStorageContents(), checkItem)){
            p.sendMessage(prefix + lang.get("check_has_no_space"));
            return;
        }
        takeMoney(p, price*amount);
        p.getInventory().addItem(checkItem);
    }
}
