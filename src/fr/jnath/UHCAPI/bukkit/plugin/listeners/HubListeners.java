package fr.jnath.UHCAPI.bukkit.plugin.listeners;

import fr.jnath.UHCAPI.game.Status;
import fr.jnath.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.jnath.UHCAPI.GUI.GUI;
import fr.jnath.UHCAPI.GUI.Type;
import fr.jnath.UHCAPI.bukkit.plugin.UhcAPI;


public class HubListeners implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if(UhcAPI.getGame().getStatus() == Status.WAITING) {
			Player player = event.getPlayer();
			player.teleport(new Location(Bukkit.getWorld("hub"), UhcAPI.getXSpawn(), UhcAPI.getYSpawn(), UhcAPI.getZSpawn()));
			player.getInventory().clear();
			player.getInventory().addItem(Utils.createItem("§5Team", Material.BANNER, 1));

		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if(UhcAPI.getGame().getStatus() == Status.WAITING) {
			try {
				if (event.getItem().getItemMeta().getDisplayName().contains("§5Config")) {
					GUI.openGUI("main_menu", event.getPlayer());
				}
			} catch (NullPointerException e) {}
			event.setCancelled(true);
		}
	}
	
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if(UhcAPI.getGame().getStatus() == Status.WAITING) {
			try {
				Type invType = Type.inventoryType(event.getClickedInventory());
				if (invType != null) {
					if (!invType.isPickableItem()) {
						event.setCancelled(true);
					}
					GUI.clickInInventory(event.getInventory(), event.getSlot(), (Player) event.getWhoClicked(), event.getClick());
				}
				if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§1"))
					event.setCancelled(true);
			} catch (NullPointerException e) {
			}
		}
	}
}
