package fr.jnath.UHCAPI.bukkit.plugin.listeners;

import fr.jnath.UHCAPI.GUI.GUI;
import fr.jnath.UHCAPI.bukkit.plugin.UhcAPI;
import fr.jnath.UHCAPI.bukkit.plugin.event.EpisodePassEvent;
import fr.jnath.UHCAPI.bukkit.plugin.event.MinutePassEvent;
import fr.jnath.UHCAPI.bukkit.plugin.event.SecondePassEvent;
import fr.jnath.UHCAPI.game.Game;
import fr.jnath.UHCAPI.game.Status;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChangeStatusListeners implements Listener {

    private final Game game;

    public ChangeStatusListeners() {
        game = UhcAPI.getGame();
    }

    @EventHandler
    public void onSecondePass(SecondePassEvent event){
        if(game.getStatus()== Status.INVULERABILITY&&event.getTotalSeconde()==(int) GUI.getGUI("uhc_config_timer").getDisplayerValue("invulnerability_duration")){
            game.setStatus(Status.POSTPVP);
        }
    }

    @EventHandler
    public void onEpisodePass(EpisodePassEvent event){
        if(event.getEpisode()==(int) GUI.getGUI("uhc_config_time").getDisplayerValue("pvp_start_episode")){
            game.setStatus(Status.POSTPVP);
        }
    }
}
