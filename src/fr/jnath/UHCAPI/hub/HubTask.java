package fr.jnath.UHCAPI.hub;

import fr.jnath.UHCAPI.bukkit.plugin.UhcAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class HubTask extends BukkitRunnable {
    public void run(){
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getLocation().getY() <= 200 && player.getLocation().getWorld().equals(UhcAPI.getWorldHub())) {
                player.teleport(new Location(UhcAPI.getWorldHub(), 0, 250, 0));
            }
            player.setHealth(20);
            player.setFoodLevel(20);
        }
        UhcAPI.getWorldHub().setTime(0);
    }
}
