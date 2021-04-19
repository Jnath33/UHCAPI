package fr.jnath.UHCAPI.bukkit.plugin.listeners;

import fr.jnath.UHCAPI.bukkit.plugin.UhcAPI;
import fr.jnath.UHCAPI.game.Status;
import fr.virthia.utils.GUI.GUI;
import fr.virthia.utils.GUI.Type;
import fr.virthia.utils.bukkit.plugin.Utils;
import fr.virthia.utils.item.ItemCreator;
import fr.virthia.utils.scoreboard.PersonalScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;


public class HubListeners implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if(UhcAPI.getGame().getStatus() == Status.WAITING) {
			Player player = event.getPlayer();
			player.teleport(new Location(Bukkit.getWorld("hub"), UhcAPI.getXSpawn(), UhcAPI.getYSpawn(), UhcAPI.getZSpawn()));
			player.getInventory().clear();
			player.getInventory().addItem(new ItemCreator("§5Team", Material.BANNER, 1).make());
			Bukkit.getScheduler().runTaskLater(UhcAPI.getInstance(), new Runnable() {
				@Override
				public void run() {
					PersonalScoreboard pScoreboard = Utils.INSTANCE.getScoreboardManager().getScoreboards().get(event.getPlayer().getUniqueId());
					pScoreboard.setLine(1, "§cWaiting for the game");
					pScoreboard.setLine(2, "");
				}
			}, 10);
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
