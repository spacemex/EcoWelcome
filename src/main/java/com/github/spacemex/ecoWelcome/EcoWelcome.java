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

        getLogger().info("Checking EcoWelcome Config.yml...");
        getConfigMessage("non-first-time-join","&bWelcome back, %player%!");
        getConfigMessage("first-time-join","&b%player%, Joined for the first time!");
        reloadConfig();
        getLogger().info("Finished Checking Config.yml");
    }

    @Override
    public void onDisable() {
        getLogger().warning("EcoWelcome Shutting Down, Goodbye World");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event){

        if (event.getPlayer().hasPermission("ew.silent")){
            event.setJoinMessage(null);
            return;
        }

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

    private String getConfigMessage(String path, String def) {
        // Check if the key/path is missing
        if (!getConfig().isSet(path)) {
            getLogger().warning("Config Key: " + path + " is missing. Regenerating with default: " + def);
            getConfig().set(path, def);
            saveConfig();
            return ChatColor.translateAlternateColorCodes('&', def);
        }

        // Check if the configuration is available
        if (getConfig().getRoot() == null || getConfig().getKeys(false).isEmpty()) {
            getLogger().warning("Configuration is missing or very likely corrupted. Regenerating config.yml from defaults.");
            saveResource("config.yml", true);
            reloadConfig();
            return ChatColor.translateAlternateColorCodes('&', def);
        }

        // Check if the key is missing, without automatically regenerating it
        if (!getConfig().contains(path)) {
            getLogger().warning("Config Key: " + path + " is missing. Regenerating with default: " + def);
            getConfig().set(path, def);
            saveConfig();
            return ChatColor.translateAlternateColorCodes('&', def);
        }

        // At this point, the path exists. Retrieve and verify its content.
        String message = getConfig().getString(path);
        if (message == null) {
            // This means the key exists, but the value is null, add default (not overwriting blank)
            message = def;
            getConfig().set(path, def);
            saveConfig();
            getLogger().info("Config Key: " + path + " was null. Set to default: " + def);
        }

        // A blank value interpreted as an intentional user setting to disable the message
        return ChatColor.translateAlternateColorCodes('&', message);
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
            getConfigMessage("non-first-time-join","&bWelcome back, %player%!");
            getConfigMessage("first-time-join","&b%player%, Joined for the first time!");
            sender.sendMessage("Config Reloaded");
            return true;
        }
        else {
            return false;
        }
    }
}
