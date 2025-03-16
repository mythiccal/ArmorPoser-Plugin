package com.mrbysco.armorposer.handler;

import com.mrbysco.armorposer.ArmorPoserPlugin;
import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.land.Area;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PermissionHandler {
    private static LandsIntegration landsAPI;
    private static boolean landsAvailable = false;

    public static void initialize(Plugin plugin) {
        if (plugin.getServer().getPluginManager().getPlugin("Lands") != null) {
            landsAPI = LandsIntegration.of(plugin);
            landsAvailable = true;
            plugin.getLogger().info("Lands integration enabled for ArmorPoser.");
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

        Location location = armorStand.getLocation();
        Area area = landsAPI.getArea(location);
        
        if (area == null) {
            return true;
        }
        
        return area.isTrusted(player.getUniqueId());
    }
}