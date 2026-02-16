package com.mrbysco.armorposer.handler;

import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.land.Area;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class LandsHandler {
    private static LandsIntegration landsAPI;

    public static void initialize(Plugin plugin) {
        landsAPI = LandsIntegration.of(plugin);
    }

    public static boolean canEdit(Player player, Location location) {
        if (landsAPI == null) return true;
        Area area = landsAPI.getArea(location);
        if (area == null) {
            return true;
        }
        return area.isTrusted(player.getUniqueId());
    }
}
