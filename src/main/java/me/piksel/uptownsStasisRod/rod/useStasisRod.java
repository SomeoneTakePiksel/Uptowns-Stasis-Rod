package me.piksel.uptownsStasisRod.rod;

import me.piksel.uptownsStasisRod.UptownsStasisRod;
import org.bukkit.*;
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
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class useStasisRod implements Listener {
    private boolean can = true ;
    private Plugin plugin;

    public void TemporaryChunkLoader(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        //some checks to make sure
        if (!event.getAction().toString().contains("RIGHT_CLICK")) return;
        Player player = event.getPlayer();
        if (!player.hasPermission("uptown.stasisRod.use")) {return;}
        if (!can){
            event.setCancelled(true);
            return;
        }
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        //which chestplate
        if (item.getType() != Material.FISHING_ROD) return;
        //keys
        NamespacedKey keyX = new NamespacedKey(UptownsStasisRod.getInstance(), "cordX");
        NamespacedKey keyY = new NamespacedKey(UptownsStasisRod.getInstance(), "cordY");
        NamespacedKey keyZ = new NamespacedKey(UptownsStasisRod.getInstance(), "cordZ");
        NamespacedKey keyWorld = new NamespacedKey(UptownsStasisRod.getInstance(), "world");

        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        int cordX = 0;
        int cordY = 0;
        int cordZ = 0;
        World w;

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
        if (pdc.has(keyWorld, PersistentDataType.STRING)) {
            String v = pdc.get(keyWorld, PersistentDataType.STRING);
            w = Bukkit.getWorld(UUID.fromString(v));
        } else return;
        Location loc = player.getLocation();
        final int x = cordX;
        final int y = cordY;
        final int z = cordZ;
        final World world = w;
        loc.setX(x);
        loc.setY(y);
        loc.setZ(z);
        loc.setWorld(world);
        can = false;
        Chunk chunk = loc.getChunk();
        chunk.load(true);
        loadChunkTemporarily(loc,UptownsStasisRod.getInstance().getConfig().getInt("chunks-sec"));
        Bukkit.getScheduler().runTaskLater(
                Bukkit.getPluginManager().getPlugin("UptownsStasisRod"),
                () -> {tp(item,loc);event.setCancelled(true);},
                10
        );
    }

    public void tp(ItemStack item,Location loc) {
        ArmorStand a = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        a.setInvisible(true);
        a.setInvulnerable(true);
        if (UptownsStasisRod.getInstance().getConfig().getBoolean("destroy-rod")) {
            item.damage(9999, a);
        }


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
    //wtf da fuq is this
    // the most gemini code i could find xdddd
    public void loadChunkTemporarily(Location location, int durationSeconds) {
        World world = location.getWorld();
        int chunkX = location.getBlockX() >> UptownsStasisRod.getInstance().getConfig().getInt("chunks");
        int chunkZ = location.getBlockZ() >> UptownsStasisRod.getInstance().getConfig().getInt("chunks");

        if (world == null) return;

        world.getChunkAtAsync(chunkX, chunkZ, true).thenAccept(chunk -> {
            new BukkitRunnable() {
                @Override
                public void run() {
                    chunk.addPluginChunkTicket(plugin);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            chunk.removePluginChunkTicket(plugin);
                        }
                    }.runTaskLater(plugin, durationSeconds * 20L);

                }
            }.runTask(plugin);
        });
    }

}
