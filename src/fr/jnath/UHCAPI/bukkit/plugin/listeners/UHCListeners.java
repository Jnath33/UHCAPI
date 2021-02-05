package fr.jnath.UHCAPI.bukkit.plugin.listeners;

import fr.jnath.UHCAPI.bukkit.plugin.UhcAPI;
import fr.jnath.UHCAPI.game.Status;
import fr.jnath.UHCAPI.game.win.WinCondition;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class UHCListeners implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof Player){
            Player player = (Player) entity;
            if(UhcAPI.getGame().statusEqual(new Status[] {Status.WAITING, Status.STARTING, Status.INVULERABILITY})){
                event.setCancelled(true);
            }
            if(event instanceof EntityDamageByEntityEvent) {
                EntityDamageByEntityEvent byEntityEvent = (EntityDamageByEntityEvent) event;
                Entity damager = byEntityEvent.getDamager();
                if(damager instanceof Player){
                    Player playerDamager = (Player) damager;
                    if(UhcAPI.getGame().statusEqual(new Status[] {Status.POSTPVP})){
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        WinCondition.playerDie(event.getEntity());
    }
}
