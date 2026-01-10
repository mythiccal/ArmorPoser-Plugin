package com.mrbysco.armorposer.handler;

import com.mrbysco.armorposer.ArmorPoserPlugin;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PermissionHandler {
    private static boolean landsAvailable = false;

    public static void initialize(Plugin plugin) {
        if (plugin.getServer().getPluginManager().getPlugin("Lands") != null) {
            try {
                LandsHandler.initialize(plugin);
                landsAvailable = true;
                plugin.getLogger().info("Lands integration enabled for ArmorPoser.");
            } catch (NoClassDefFoundError | Exception e) {
                plugin.getLogger().warning("Failed to initialize Lands integration even though the plugin was found.");
                landsAvailable = false;
            }
        } else {
            plugin.getLogger().info("Lands not found, land claim checks will be disabled.");
        }
    }

    /**
     * Checks if a player has permission to edit an armor stand in a land claim
     * @param player The player attempting to edit
     * @param armorStand The armor stand being edited
     * @return true if the player has permission, false otherwise
     */
    public static boolean canEditArmorStand(Player player, ArmorStand armorStand) {
        if (ArmorPoserPlugin.requirePermissions && !player.hasPermission(ArmorPoserPlugin.USE_PERMISSION)) {
            return false;
        }

        if (!landsAvailable) {
            return true;
        }

        return LandsHandler.canEdit(player, armorStand.getLocation());
    }

    /**
     * Checks if a player has permission to make armor stands invisible
     * @param player The player attempting to make the stand invisible
     * @return true if the player has permission, false otherwise
     */
    public static boolean canMakeInvisible(Player player) {
        if (!ArmorPoserPlugin.extraPermissions) {
            return true;
        }
        
        return player.hasPermission(ArmorPoserPlugin.INVISIBLE_PERMISSION);
    }

    /**
     * Checks if a player has permission to make armor stand names visible
     * @param player The player attempting to make the name visible
     * @return true if the player has permission, false otherwise
     */
    public static boolean canMakeNameVisible(Player player) {
        if (!ArmorPoserPlugin.extraPermissions) {
            return true;
        }
        
        return player.hasPermission(ArmorPoserPlugin.NAMEVISIBLE_PERMISSION);
    }
}