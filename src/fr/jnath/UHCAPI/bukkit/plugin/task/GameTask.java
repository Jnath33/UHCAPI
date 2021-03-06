package fr.jnath.UHCAPI.bukkit.plugin.task;

import fr.jnath.UHCAPI.bukkit.plugin.UhcAPI;
import fr.jnath.UHCAPI.bukkit.plugin.event.EpisodePassEvent;
import fr.jnath.UHCAPI.bukkit.plugin.event.MinutePassEvent;
import fr.jnath.UHCAPI.bukkit.plugin.event.SecondePassEvent;
import fr.virthia.utils.GUI.GUI;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTask extends BukkitRunnable {

    private int quarterOfSeconde,seconde, minute = 0,episode=1;

    public void run() {
        quarterOfSeconde++;
        if(quarterOfSeconde>=4){
            seconde++;
            quarterOfSeconde=0;
            Bukkit.getPluginManager().callEvent(new SecondePassEvent(
                    (episode-1)*60*((int) GUI.getGUI("uhc_config_timer").getDisplayerValue("episode_duration"))+minute*60+seconde, seconde, minute, episode));
            if(seconde>=60){
                minute++;
                seconde=0;
                Bukkit.getPluginManager().callEvent(new MinutePassEvent((episode-1)*((int) GUI.getGUI("uhc_config_timer").getDisplayerValue("episode_duration"))+minute, minute));
                if(minute>=GUI.getGUI("uhc_config_timer").getDisplayerValue("episode_duration")){
                    episode++;
                    UhcAPI.getGame().setEpisode(episode);
                    minute=0;
                    Bukkit.getPluginManager().callEvent(new EpisodePassEvent(episode));
                }
            }
        }


    }
}