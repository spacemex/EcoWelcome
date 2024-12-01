package com.github.spacemex.ecoWelcome;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class EcoWelcome extends JavaPlugin implements Listener, CommandExecutor {
    private boolean hasPlaceholderApi = false;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("EcoWelcome Enabled, Hello World :)");

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getLogger().info("Found Placeholder API - Hooking...");
            hasPlaceholderApi = true;
        }else {
            getLogger().warning("Could not find PlaceholderAPI, That's Ok");
            getLogger().warning("Remember to edit your config accordingly");
            hasPlaceholderApi = false;
        }
        Bukkit.getPluginManager().registerEvents(this, this);
        Objects.requireNonNull(getCommand("ecowelcomereload")).setExecutor(this);
    }

    @Override
    public void onDisable() {
        getLogger().warning("EcoWelcome Shutting Down :(");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event){
        String regularJoin = getConfigMessage("non-first-time-join","&bWelcome back, %player%!");
        String firstTime = getConfigMessage("first-time-join","&b%player%, Joined for the first time!");

        if (hasPlaceholderApi){
            regularJoin = PlaceholderAPI.setPlaceholders(event.getPlayer(), regularJoin);
            firstTime = PlaceholderAPI.setPlaceholders(event.getPlayer(), firstTime);
        }
        else {
            regularJoin = getConfigMessage("non-first-time-join","&bWelcome back, %player%!").replace("%player%",event.getPlayer().getName());
            firstTime = getConfigMessage("first-time-join","&b%player%, Joined for the first time!").replace("%player%",event.getPlayer().getName());
        }
        if (!event.getPlayer().hasPlayedBefore()){
            if (stringCheck(firstTime)){
                return;
            }
            event.setJoinMessage(firstTime);
        }
        else {
            if (stringCheck(regularJoin)){
                return;
            }
            event.setJoinMessage(regularJoin);
        }
    }

    private String getConfigMessage(String path,String def){
        if (getConfig().getString(path) == null) {
            getLogger().warning("Config Key: " + path  + " is missing or corrupted - Remaking it with default value: " + def);
            getConfig().set(path,def);
            saveConfig();
            return ChatColor.translateAlternateColorCodes('&',def);
        }
        String message = Objects.requireNonNull(getConfig().getString(path));
        if (stringCheck(message)){
            return "";
        }
            return ChatColor.translateAlternateColorCodes('&', getConfig().getString(path,def));
    }
    private boolean stringCheck(String string){
        return string.isBlank();
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getLabel().equalsIgnoreCase("ecowelcomereload")){
            if (sender instanceof org.bukkit.entity.Player player){
                if (!player.hasPermission("ew.reload")){
                    player.sendMessage(ChatColor.RED + "You do not have permission to use this command");
                    return true;
                }
            }
            reloadConfig();
            sender.sendMessage("Config Reloaded");
            return true;
        }
        else {
            return false;
        }
    }
}
