package fr.jnath.UHCAPI.bukkit.plugin.listeners;

import fr.jnath.UHCAPI.bukkit.plugin.UhcAPI;
import fr.jnath.UHCAPI.bukkit.plugin.event.MinutePassEvent;
import fr.jnath.UHCAPI.bukkit.plugin.event.SecondePassEvent;
import fr.jnath.UHCAPI.bukkit.plugin.task.BorderTask;
import fr.jnath.UHCAPI.game.Game;
import fr.jnath.UHCAPI.game.Status;
import fr.virthia.utils.GUI.GUI;
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
            game.setStatus(Status.PREPVP);
        }
    }

    @EventHandler
    public void onEpisodePass(MinutePassEvent event){
        if(game.getStatus() == Status.PREPVP &&
                event.getTotalMinute() >= (int) GUI.getGUI("uhc_config_timer").getDisplayerValue("pvp_start_episode"))
            game.setStatus(Status.GAME);
        if(game.getStatus() == Status.GAME &&
                event.getTotalMinute() >= (int) GUI.getGUI("uhc_config_border").getDisplayerValue("border_start")) {
            BorderTask border = new BorderTask((int) GUI.getGUI("uhc_config_border").getDisplayerValue("border_speed"),
                    GUI.getGUI("uhc_config_border").getDisplayerValue("border_size_end"));
            border.runTaskTimer(UhcAPI.getInstance(), 0,20);
        }
    }
}
