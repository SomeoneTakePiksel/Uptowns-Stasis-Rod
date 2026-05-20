package me.piksel.uptownsStasisRod.rod;


import me.piksel.uptownsStasisRod.UptownsStasisRod;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class saveStasisRod implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("stasisRod")) {
            execute(sender,args);
            return true;
        }

        return false;
    }

    public void execute(CommandSender source, String[] args) {
        if (!(source instanceof Player player))return;
        if (!player.hasPermission("uptown.stasisRod.use")){return;}
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        if (item.getType() != Material.FISHING_ROD){
            player.sendRichMessage(UptownsStasisRod.getInstance().getConfig().getString("not-holding-fishing-rods"));
            return;

        }
        NamespacedKey keyX = new NamespacedKey(UptownsStasisRod.getInstance(),"cordX");
        NamespacedKey keyY = new NamespacedKey(UptownsStasisRod.getInstance(),"cordY");
        NamespacedKey keyZ = new NamespacedKey(UptownsStasisRod.getInstance(),"cordZ");
        NamespacedKey keyWorld = new NamespacedKey(UptownsStasisRod.getInstance(),"world");

        List<String> lore = new ArrayList<>();



        meta.getPersistentDataContainer().set(
                keyX,
                PersistentDataType.INTEGER,
                player.getLocation().getBlockX()
        );


        meta.getPersistentDataContainer().set(
                keyY,
                PersistentDataType.INTEGER,
                player.getLocation().getBlockY()
        );

        meta.getPersistentDataContainer().set(
                keyZ,
                PersistentDataType.INTEGER,
                player.getLocation().getBlockZ()
        );
        meta.getPersistentDataContainer().set(
                keyWorld,
                PersistentDataType.STRING,
                player.getWorld().getUID().toString()
        );
        if (UptownsStasisRod.getInstance().getConfig().getBoolean("show-cords")) {
            lore.add(ChatColor.DARK_PURPLE + Integer.toString(player.getLocation().getBlockX()));
            lore.add(ChatColor.DARK_PURPLE + Integer.toString(player.getLocation().getBlockY()));
            lore.add(ChatColor.DARK_PURPLE + Integer.toString(player.getLocation().getBlockZ()));
            lore.add(ChatColor.DARK_PURPLE + player.getLocation().getWorld().toString());
        }


        meta.setLore(lore);
        if (UptownsStasisRod.getInstance().getConfig().getString("rod-name") != "") {
            meta.setDisplayName(UptownsStasisRod.getInstance().getConfig().getString("rod-name"));
        }
        item.setItemMeta(meta);
        if (UptownsStasisRod.getInstance().getConfig().getBoolean("destroy-rod")){
            item.setDurability((short) 60);
            item.removeEnchantments();
        }

        //item.damage(60,player);
        player.sendRichMessage(UptownsStasisRod.getInstance().getConfig().getString("saving-sRod"));
    }
}
