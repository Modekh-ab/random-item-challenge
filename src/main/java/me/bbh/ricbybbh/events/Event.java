package me.bbh.ricbybbh.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import me.bbh.ricbybbh.Main;
import me.bbh.ricbybbh.commands.Toggle;
import me.bbh.ricbybbh.utils.ChatUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class Event implements Listener {
    private static ArrayList<Material> materials = new ArrayList();
    private static boolean activated = false;
    public static Main plugin;

    public Event(Main plugin) {
        Event.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public static void enable() {
        if (!activated) {
            materials.clear();
            Iterator v1 = plugin.getConfig().getStringList("items").iterator();

            while(v1.hasNext()) {
                String item = (String)v1.next();
                Material[] v5;
                int v4 = (v5 = Material.values()).length;

                for(int i = 0; i < v4; ++i) {
                    Material m = v5[i];
                    if (item.equalsIgnoreCase(m.name())) {
                        materials.add(m);
                    }
                }
            }

            activated = true;
            v1 = Toggle.getWorld().getPlayers().iterator();

            while(v1.hasNext()) {
                Player p = (Player)v1.next();
                p.getInventory().clear();
                p.teleport(Toggle.getWorld().getSpawnLocation());
                p.setGameMode(GameMode.SURVIVAL);
                p.setHealth(20.0);
                Toggle.getWorld().setTime(1000);
                Toggle.getWorld().setClearWeatherDuration(999999);
            }

            (new BukkitRunnable() {
                public void run() {

                    if (!Event.activated) {
                        this.cancel();
                    }

                    if (Event.activated) {
                        World world = Toggle.getWorld();
                        Iterator v3 = world.getPlayers().iterator();

                        while (v3.hasNext()) {
                            Player p = (Player)v3.next();
                            p.sendMessage(ChatUtils.color("&6&l→ ITEM DROP HAS ARRIVED!"));
                            Location spawnitems = p.getLocation();
                            Random random = new Random();
                            int length = Event.materials.size();
                            int rng = random.nextInt(length);
                            ItemStack selected = new ItemStack((Material)Event.materials.get(rng), 1);
                            if (selected.getType().getMaxStackSize() == 16) {
                                selected = new ItemStack((Material)Event.materials.get(rng), 16);
                            } else if (selected.getType().getMaxStackSize() == 1) {
                                selected = new ItemStack((Material)Event.materials.get(rng), 1);
                            } else if (selected.getType().getMaxStackSize() == 64) {
                                selected = new ItemStack((Material)Event.materials.get(rng), 64);
                            }

                            ItemMeta meta = selected.getItemMeta();
                            ArrayList<String> lore = new ArrayList();
                            lore.add(ChatColor.WHITE + p.getName());
                            meta.setLore(lore);
                            selected.setItemMeta(meta);

                            for(int i = 0; i < plugin.getConfig().getInt("giving-item-stacks-amount"); ++i) {
                                world.dropItem(spawnitems, selected);
                            }
                        }
                    }

                }
            }).runTaskTimer(plugin, plugin.getConfig().getLong("ticks-between-item-drop"), plugin.getConfig().getLong("ticks-between-item-drop"));
            (new BukkitRunnable() {
                public void run() {
                    int smc = 0;

                    for(Iterator v3 = Toggle.getWorld().getPlayers().iterator(); v3.hasNext(); ++smc) {
                        Player p = (Player)v3.next();
                    }

                    if (smc < 2 && Toggle.getWorld().getPlayers().size() > 2) {
                        Event.disable();
                    }

                }
            }).runTaskTimer(plugin, 5L, 5L);
        }
    }

    public static void disable() {
        if (activated) {
            Iterator v1 = Toggle.getWorld().getPlayers().iterator();

            while(v1.hasNext()) {
                Player p = (Player)v1.next();
                p.getInventory().clear();
                p.setHealth(20.0);
                p.setFoodLevel(20);
                p.teleport(Toggle.getWorld().getSpawnLocation());
                if (p.getGameMode() == GameMode.SPECTATOR) {
                    p.setGameMode(GameMode.SURVIVAL);
                }
            }

            activated = false;
        } else {
            activated = false;
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (activated && e.getEntity().getWorld() == Toggle.getWorld() && e.getEntity() instanceof Player) {
            Player p = (Player)e.getEntity();
            p.setGameMode(GameMode.SPECTATOR);
        }

    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e) {
        if (activated && e.getEntity().getWorld() == Toggle.getWorld() && e.getEntity() instanceof Player) {
            Player p = (Player)e.getEntity();
            if (!e.getItem().getItemStack().hasItemMeta() && plugin.getConfig().getString("take-floor-drop").equals("false")) {
                e.setCancelled(true);
            } else if (!e.getItem().getItemStack().getItemMeta().hasLore() && plugin.getConfig().getString("take-floor-drop").equals("false")) {
                e.setCancelled(true);
            } else {
                ItemStack item = e.getItem().getItemStack();
                ItemMeta meta = item.getItemMeta();
                ArrayList<String> lore = new ArrayList();
                if (meta.getLore().contains(ChatColor.WHITE + p.getName())) {
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                } else {
                    if (plugin.getConfig().getString("take-floor-drop").equals("false"))
                        e.setCancelled(true);
                }
            }
        }

    }

    @EventHandler
    private void onHungerDeplete(FoodLevelChangeEvent e) {
        if (plugin.getConfig().getString("food-level-decreases").equals("false")) {
            if (activated && e.getEntity().getWorld() == Toggle.getWorld()) {
                e.getEntity().setFoodLevel(20);
                e.setCancelled(true);
            }
        }
    }
}