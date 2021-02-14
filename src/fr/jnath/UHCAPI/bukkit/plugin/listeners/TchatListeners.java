package fr.jnath.UHCAPI.bukkit.plugin.listeners;

import fr.jnath.UHCAPI.bukkit.plugin.UhcAPI;
import fr.jnath.UHCAPI.game.Game;
import fr.jnath.UHCAPI.team.Team;
import fr.virthia.utils.tchat.Tchat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class TchatListeners implements Listener {
    @EventHandler
    public void onMessageSend(AsyncPlayerChatEvent event){
        event.setCancelled(true);
        Player player = event.getPlayer();
        String message = event.getMessage();
        Game game = UhcAPI.getGame();
        if(message.startsWith("!")){
            if(Team.haveTeam(player) && game.isUseTeam()){
                Tchat.sendMessage(game.getPlayers(),
                        game.getTchatNameGlobal(),
                        message.substring(1),
                        player.getDisplayName(),
                        Team.get(player).getTag(),
                        "","");
            }
        } else if(message.startsWith("*")){
            Tchat.sendMessage(game.getPlayers(), game.getTchatNameGlobal(), message.substring(1), player.getDisplayName(), "","","");
        }
    }
}
