package me.piksel.uptownsStasisRod.rod;

import me.piksel.uptownsStasisRod.UptownsStasisRod;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class useStasisRod implements Listener {
    private boolean can = true ;

    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        //some checks to make sure
        if (!event.getAction().toString().contains("RIGHT_CLICK")) return;
        Player player = event.getPlayer();
        if (!player.hasPermission("uptown.stasisRod.use")) {return;}
        if (!can)return;
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        //which chestplate
        if (item.getType() != Material.FISHING_ROD) return;
        //keys
        NamespacedKey keyX = new NamespacedKey(UptownsStasisRod.getInstance(), "cordX");
        NamespacedKey keyY = new NamespacedKey(UptownsStasisRod.getInstance(), "cordY");
        NamespacedKey keyZ = new NamespacedKey(UptownsStasisRod.getInstance(), "cordZ");

        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        int cordX = 0;
        int cordY = 0;
        int cordZ = 0;

        if (pdc.has(keyX, PersistentDataType.INTEGER)) {
            int v = pdc.get(keyX, PersistentDataType.INTEGER);
            cordX = v;
        } else return;
        if (pdc.has(keyY, PersistentDataType.INTEGER)) {
            int v = pdc.get(keyY, PersistentDataType.INTEGER);
            cordY = v;
        } else return;
        if (pdc.has(keyZ, PersistentDataType.INTEGER)) {
            int v = pdc.get(keyZ, PersistentDataType.INTEGER);
            cordZ = v;
        } else return;
        Location loc = player.getLocation();
        final int x = cordX;
        final int y = cordY;
        final int z = cordZ;
        loc.setX(x);
        loc.setY(y);
        loc.setZ(z);
        can = false;


        Bukkit.getScheduler().runTaskLater(
                Bukkit.getPluginManager().getPlugin("UptownsStasisRod"),
                () -> tp(item,loc),
                10
        );
    }

    public void tp(ItemStack item,Location loc) {
        ArmorStand a = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        a.setInvisible(true);
        a.setInvulnerable(true);
        item.damage(9999, a);


        Bukkit.getScheduler().runTaskLater(
                Bukkit.getPluginManager().getPlugin("UptownsStasisRod"),
                () -> allowUse(a),
                10
        );
    }
    private void allowUse(ArmorStand a){
        a.remove();
        can = true;
    }
}
