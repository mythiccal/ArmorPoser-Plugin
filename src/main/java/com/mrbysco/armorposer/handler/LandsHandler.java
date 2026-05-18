package com.mrbysco.armorposer.handler;

import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.flags.type.RoleFlag;
import me.angeschossen.lands.api.land.Area;
import me.angeschossen.lands.api.player.LandPlayer;
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

        LandPlayer landPlayer = landsAPI.getLandPlayer(player.getUniqueId());
        if (landPlayer != null) {
            RoleFlag breakFlag = landsAPI.getFlagRegistry().getRole("BLOCK_BREAK");
            RoleFlag placeFlag = landsAPI.getFlagRegistry().getRole("BLOCK_PLACE");
            if (breakFlag != null && area.hasRoleFlag(landPlayer, breakFlag, null, false)) {
                return true;
            }
            if (placeFlag != null && area.hasRoleFlag(landPlayer, placeFlag, null, false)) {
                return true;
            }
        }

        return area.isTrusted(player.getUniqueId());
    }
}
