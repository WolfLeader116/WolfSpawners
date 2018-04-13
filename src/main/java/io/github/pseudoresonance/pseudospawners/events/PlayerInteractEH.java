package io.github.pseudoresonance.pseudospawners.events;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

import io.github.pseudoresonance.pseudoapi.bukkit.Message.Errors;
import io.github.pseudoresonance.pseudospawners.ConfigOptions;
import io.github.pseudoresonance.pseudospawners.PseudoSpawners;

public class PlayerInteractEH implements Listener {
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
			Block b = p.getTargetBlock(new HashSet<Material>(), 5);
			if (b.getType() == Material.MOB_SPAWNER) {
				ItemStack is = e.getItem();
				if (isEgg(is)) {
					ItemMeta im = is.getItemMeta();
					SpawnEggMeta sem = (SpawnEggMeta) im;
					EntityType entity = sem.getSpawnedType();
					if (p.hasPermission("pseudospawners.override")) {
						if (p.hasPermission("pseudospawners.modify")) {
							if (e.getHand() == EquipmentSlot.HAND) {
								p.getInventory().setItemInMainHand(null);
								CreatureSpawner s = (CreatureSpawner) b.getState();
								s.setSpawnedType(entity);
								s.update();
								e.setCancelled(true);
							} else if (e.getHand() == EquipmentSlot.OFF_HAND) {
								p.getInventory().setItemInOffHand(null);
								CreatureSpawner s = (CreatureSpawner) b.getState();
								s.setSpawnedType(entity);
								s.update();
								e.setCancelled(true);
							}
						} else {
							PseudoSpawners.message.sendPluginError(p, Errors.NO_PERMISSION, "set a spawner type!");
						}
					} else {
						for (EntityType et : ConfigOptions.spawnable) {
							if (et.equals(entity)) {
								if (p.hasPermission("pseudospawners.spawner." + entity.toString().toLowerCase())) {
									if (p.hasPermission("pseudospawners.modify")) {
										if (e.getHand() == EquipmentSlot.HAND) {
											p.getInventory().setItemInMainHand(null);
											CreatureSpawner s = (CreatureSpawner) b.getState();
											s.setSpawnedType(entity);
											s.update();
											e.setCancelled(true);
										} else if (e.getHand() == EquipmentSlot.OFF_HAND) {
											p.getInventory().setItemInOffHand(null);
											CreatureSpawner s = (CreatureSpawner) b.getState();
											s.setSpawnedType(entity);
											s.update();
											e.setCancelled(true);
										}
									} else {
										PseudoSpawners.message.sendPluginError(p, Errors.NO_PERMISSION, "set a spawner type!");
									}
								} else {
									PseudoSpawners.message.sendPluginError(p, Errors.NO_PERMISSION, "set spawner to that type!");
								}
							}
						}
					}
					if (!e.isCancelled()) {
						PseudoSpawners.message.sendPluginError(p, Errors.CUSTOM, "That entity type is disabled!");
					}
				}
			}
		}
	}
	
	public boolean isEgg(ItemStack is) {
		Material m = is.getType();
		if (m == Material.MONSTER_EGG) {
			return true;
		} else {
			return false;
		}
	}

}