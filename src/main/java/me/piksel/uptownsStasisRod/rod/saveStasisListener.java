package me.piksel.uptownsStasisRod.rod;

import me.piksel.uptownsStasisRod.UptownsStasisRod;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class saveStasisListener implements Listener {


    @EventHandler
    public void onCrouch(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (!UptownsStasisRod.getInstance().getConfig().getBoolean("save-fishing"))return;
        if (player.isSneaking()) {
            if (!player.hasPermission("uptown.stasisRod.use")) {return;}
            ItemStack item = player.getInventory().getItemInMainHand();
            ItemMeta meta = item.getItemMeta();
            //which chestplate
            if (item.getType() != Material.FISHING_ROD) return;
            FishHook hook = player.getFishHook();
            if (!(hook != null && hook.isValid())) return;
            Location loc = hook.getLocation();
            if (UptownsStasisRod.getInstance().getConfig().getBoolean("check-if-pressureplate")){
                if (!(Tag.PRESSURE_PLATES.isTagged(loc.getBlock().getType()))) return;
            }
            NamespacedKey keyX = new NamespacedKey(UptownsStasisRod.getInstance(),"cordX");
            NamespacedKey keyY = new NamespacedKey(UptownsStasisRod.getInstance(),"cordY");
            NamespacedKey keyZ = new NamespacedKey(UptownsStasisRod.getInstance(),"cordZ");
            NamespacedKey keyWorld = new NamespacedKey(UptownsStasisRod.getInstance(),"world");

            List<String> lore = new ArrayList<>();



            meta.getPersistentDataContainer().set(
                    keyX,
                    PersistentDataType.INTEGER,
                    loc.getBlockX()
            );


            meta.getPersistentDataContainer().set(
                    keyY,
                    PersistentDataType.INTEGER,
                    loc.getBlockY()
            );

            meta.getPersistentDataContainer().set(
                    keyZ,
                    PersistentDataType.INTEGER,
                    loc.getBlockZ()
            );
            meta.getPersistentDataContainer().set(
                    keyWorld,
                    PersistentDataType.STRING,
                    loc.getWorld().getUID().toString()
            );
            if (UptownsStasisRod.getInstance().getConfig().getBoolean("show-cords")) {
                lore.add(ChatColor.DARK_PURPLE + Integer.toString(loc.getBlockX()));
                lore.add(ChatColor.DARK_PURPLE + Integer.toString(loc.getBlockY()));
                lore.add(ChatColor.DARK_PURPLE + Integer.toString(loc.getBlockZ()));
                lore.add(ChatColor.DARK_PURPLE + loc.getWorld().toString());
            }
            if (UptownsStasisRod.getInstance().getConfig().getBoolean("destroy-rod")){
                item.setDurability((short) 60);
                item.removeEnchantments();
            }
            if (UptownsStasisRod.getInstance().getConfig().getBoolean("enchantment-glint")){
                meta.setEnchantmentGlintOverride(true);
            }
            if (UptownsStasisRod.getInstance().getConfig().getString("rod-name") != "" &&
                    !UptownsStasisRod.getInstance().getConfig().getBoolean("custom-name")) {
                meta.setDisplayName(UptownsStasisRod.getInstance().getConfig().getString("rod-name"));
            }


            meta.setLore(lore);
            if(UptownsStasisRod.getInstance().getConfig().getBoolean("alaways-armorstanmd")){
                Location locc = loc.getBlock().getLocation();
                ArmorStand a = (ArmorStand) player.getLocation().getWorld().spawnEntity(locc, EntityType.ARMOR_STAND);
                a.setInvisible(true);
                a.setGravity(false);
                a.setSmall(true);
                a.setCanMove(false);
                a.setCanPickupItems(false);
                String name = locc.getBlock().getLocation().toString() ;
                a.setCustomName(name);
                //a.setCustomNameVisible(true);
                //player.sendRichMessage("armorstand spawned");
            }
            item.setItemMeta(meta);
            player.getWorld().playSound(player.getLocation(),Sound.BLOCK_NOTE_BLOCK_HAT,2f,1f);
            hook.remove();
            player.sendRichMessage(UptownsStasisRod.getInstance().getConfig().getString("saving-sRod"));

        }
    }





    @EventHandler
    public void onPlayerEnterNether(PlayerPortalEvent event) {
        if (!UptownsStasisRod.getInstance().getConfig().getBoolean("vanilla-saving"))return;
        Player player = event.getPlayer();
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            World toWorld = event.getTo().getWorld();


            if (toWorld != null) {
                ItemStack item = new ItemStack(Material.FISHING_ROD);
                if (toWorld.getEnvironment() == World.Environment.NETHER) {
                    if (!player.hasPermission("uptown.stasisRod.use")) {return;}
                    if (player.getInventory().getItemInMainHand().getType() == Material.FISHING_ROD) {
                        item = player.getInventory().getItemInMainHand();
                    } else if (player.getInventory().getItemInOffHand().getType() == Material.FISHING_ROD) {
                        item = player.getInventory().getItemInOffHand();
                    } else return;


                    ItemMeta meta = item.getItemMeta();
                    //which chestplate
                    //if (item.getType() != Material.FISHING_ROD) return;
                    FishHook hook = player.getFishHook();
                    if (!(hook != null && hook.isValid())) return;
                    Location loc = hook.getLocation();
                    if (UptownsStasisRod.getInstance().getConfig().getBoolean("check-if-pressureplate")){
                        if (!(Tag.PRESSURE_PLATES.isTagged(loc.getBlock().getType()))) return;
                    }
                    NamespacedKey keyX = new NamespacedKey(UptownsStasisRod.getInstance(),"cordX");
                    NamespacedKey keyY = new NamespacedKey(UptownsStasisRod.getInstance(),"cordY");
                    NamespacedKey keyZ = new NamespacedKey(UptownsStasisRod.getInstance(),"cordZ");
                    NamespacedKey keyWorld = new NamespacedKey(UptownsStasisRod.getInstance(),"world");

                    List<String> lore = new ArrayList<>();



                    meta.getPersistentDataContainer().set(
                            keyX,
                            PersistentDataType.INTEGER,
                            loc.getBlockX()
                    );


                    meta.getPersistentDataContainer().set(
                            keyY,
                            PersistentDataType.INTEGER,
                            loc.getBlockY()
                    );

                    meta.getPersistentDataContainer().set(
                            keyZ,
                            PersistentDataType.INTEGER,
                            loc.getBlockZ()
                    );
                    meta.getPersistentDataContainer().set(
                            keyWorld,
                            PersistentDataType.STRING,
                            loc.getWorld().getUID().toString()
                    );
                    if (UptownsStasisRod.getInstance().getConfig().getBoolean("show-cords")) {
                        lore.add(ChatColor.DARK_PURPLE + Integer.toString(loc.getBlockX()));
                        lore.add(ChatColor.DARK_PURPLE + Integer.toString(loc.getBlockY()));
                        lore.add(ChatColor.DARK_PURPLE + Integer.toString(loc.getBlockZ()));
                        lore.add(ChatColor.DARK_PURPLE + loc.getWorld().toString());
                    }
                    if (UptownsStasisRod.getInstance().getConfig().getBoolean("destroy-rod")){
                        item.setDurability((short) 60);
                        item.removeEnchantments();
                    }
                    if (UptownsStasisRod.getInstance().getConfig().getBoolean("enchantment-glint")){
                        meta.setEnchantmentGlintOverride(true);
                    }
                    if (UptownsStasisRod.getInstance().getConfig().getString("rod-name") != "" &&
                            !UptownsStasisRod.getInstance().getConfig().getBoolean("custom-name")) {
                        meta.setDisplayName(UptownsStasisRod.getInstance().getConfig().getString("rod-name"));
                    }


                    meta.setLore(lore);
                    if(UptownsStasisRod.getInstance().getConfig().getBoolean("alaways-armorstanmd")){
                        Location locc = loc.getBlock().getLocation();
                        ArmorStand a = (ArmorStand) player.getLocation().getWorld().spawnEntity(locc, EntityType.ARMOR_STAND);
                        a.setInvisible(true);
                        a.setGravity(false);
                        a.setSmall(true);
                        a.setCanMove(false);
                        a.setCanPickupItems(false);
                        String name = locc.getBlock().getLocation().toString() ;
                        a.setCustomName(name);
                        //a.setCustomNameVisible(true);
                        //player.sendRichMessage("armorstand spawned");
                    }
                    item.setItemMeta(meta);
                    player.getWorld().playSound(player.getLocation(),Sound.BLOCK_NOTE_BLOCK_HAT,2f,1f);
                    hook.remove();
                    player.sendRichMessage(UptownsStasisRod.getInstance().getConfig().getString("saving-sRod"));

                }
            }
        }
    }


}
