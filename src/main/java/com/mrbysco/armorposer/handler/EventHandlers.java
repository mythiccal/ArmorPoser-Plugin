package com.mrbysco.armorposer.handler;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mrbysco.armorposer.ArmorPoserPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class EventHandlers implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractAtEntityEvent event) {
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();

		boolean isArmorStand = entity instanceof ArmorStand;
		boolean isSneaking = player.isSneaking();
		boolean canUse = canUseGUI(player);

		if (isArmorStand && isSneaking && canUse) {
			ArmorStand armorStand = (ArmorStand) entity;

			// Prevent editing marker armor stands / AngelChest holograms 
			boolean isAngelChestHologram = false;
			PersistentDataContainer pdc = armorStand.getPersistentDataContainer();
			NamespacedKey hologramKey = new NamespacedKey("angelchest", "ishologram"); 

			if (pdc.has(hologramKey, PersistentDataType.STRING)) {
				isAngelChestHologram = "true".equals(pdc.get(hologramKey, PersistentDataType.STRING));
			}

			if (armorStand.isMarker() || isAngelChestHologram) {
				event.setCancelled(true);
				return;
			}

			if (!PermissionHandler.canEditArmorStand(player, armorStand)) {
				player.sendMessage("§cYou don't have permission to edit armor stands in this area.");
				event.setCancelled(true);
				return;
			}
			
			if (event.getHand() == EquipmentSlot.HAND) {
				ByteArrayDataOutput lockedOut = ByteStreams.newDataOutput();
				lockedOut.writeInt(armorStand.getEntityId());
				lockedOut.writeBoolean(armorStand.isInvulnerable());
				player.sendPluginMessage(ArmorPoserPlugin.Plugin, "armorposer:locked_packet", lockedOut.toByteArray());

				ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeInt(armorStand.getEntityId());
				player.sendPluginMessage(ArmorPoserPlugin.Plugin, "armorposer:screen_packet", out.toByteArray());
			}
			event.setCancelled(true);
		}
	}

	private boolean canUseGUI(Player player) {
		if (!ArmorPoserPlugin.enableConfigGui) return false;
		if (!ArmorPoserPlugin.requirePermissions) return true;
		return player.hasPermission(ArmorPoserPlugin.USE_PERMISSION);
	}
}
