package me.bbh.ricbybbh.commands;

import me.bbh.ricbybbh.Main;
import me.bbh.ricbybbh.events.Event;
import me.bbh.ricbybbh.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class Toggle implements Listener, CommandExecutor {
    public static World world;
    private Main plugin;

    public Toggle(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        plugin.getCommand("ric").setExecutor(this);
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player)sender;
            if (p.hasPermission("ric.admin")) {
                if (args.length < 1) {
                    p.sendMessage(ChatColor.RED + "Invalid Usage. Correct Usage: /ric <on/off> ");
                    return false;
                }

                if (!args[0].equalsIgnoreCase("on") && !args[0].equalsIgnoreCase("enable")) {
                    if (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("disable")) {
                        world = p.getWorld();
                        p.sendMessage(ChatColor.AQUA + "→ Random Item Challenge de-activated in world.");
//                        p.sendTitle(ChatColor.BLUE + "→ GAME OVER! ←", "", 20, 40, 20);
                        Event.disable();
                    } else if (args[0].equalsIgnoreCase("sp")) {
                        sender.sendMessage(ChatUtils.color("&6&l→ ") + p.getName() + ChatUtils.color("&6&l is a SPEEDRUNNER now!"));
                    } else if (args[0].equalsIgnoreCase("hunter")) {
                        sender.sendMessage(ChatUtils.color("&6&l→ ") + p.getName() + ChatUtils.color("&6&l is a HUNTER now!"));
                        p.getInventory().addItem(new ItemStack(Material.COMPASS));
                    }
                } else {
                    world = p.getWorld();
                    p.sendMessage(ChatColor.AQUA + "→ Random Item Challenge activated in world.");
//                    p.sendTitle(ChatColor.AQUA + "→ START! ←", "", 20, 40, 20);
                    this.plugin.reloadConfig();
                    Event.enable();
                }
            } else {
                p.sendMessage(ChatColor.RED + "You do not have permission to execute this command!");
            }
        }

        return false;
    }

    public static World getWorld() {
        return world;
    }

    public static void setWorld(World world) {
        Toggle.world = world;
    }
}
