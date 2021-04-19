package fr.jnath.UHCAPI.game.win;

import fr.jnath.UHCAPI.bukkit.plugin.UhcAPI;
import fr.jnath.UHCAPI.game.Game;
import fr.jnath.UHCAPI.game.Status;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WinCondition {
    private final List<Player> players = new ArrayList<>();
    private final static List<WinCondition> winConditions= new ArrayList<>();
    private final String name, displayName;
    private final WinConditionType winConditionType;
    private static WinMessageType winMessageType = WinMessageType.MAIN_SCENARIO;
    private final static HashMap<Player, WinCondition> playerWinCondition = new HashMap<>();
    private final static HashMap<String, WinCondition> nameWinCondition = new HashMap<>();
    private final static HashMap<Player, WinCondition> playerDeath = new HashMap<>();

    public WinCondition(String name, String displayName, WinConditionType winConditionType) {
        this.name = name;
        this.displayName = displayName;
        this.winConditionType = winConditionType;
        winConditions.add(this);
        nameWinCondition.put(name, this);
    }

    public static void setWinMessageType(WinMessageType winMessageType) {
        WinCondition.winMessageType = winMessageType;
    }

    public static void playerDie(Player player){
        if(playerWinCondition.containsKey(player)) {
            WinCondition winCondition = playerWinCondition.get(player);
            winCondition.getPlayers().remove(player);
            if(winCondition.getPlayers().size()<=0){
                winConditions.remove(winCondition);
            }
            playerWinCondition.remove(player);
            if(UhcAPI.getGame().statusEqual(new Status[]{Status.PREPVP, Status.STARTING, Status.INVULERABILITY, Status.WAITING}))
            if(winConditions.size()<=1){
                if(winConditions.size()==1){
                    Game.win(winConditions.get(0));
                }else {
                    Game.win(null);
                }
            }
        }
    }

    public static void addPlayer(String name, Player player){
        if(playerWinCondition.containsKey(player)){
            WinCondition winCondition=playerWinCondition.get(player);
            winCondition.getPlayers().remove(player);
            if(winCondition.getPlayers().size()<=0){
                winConditions.remove(winCondition);
            }
            playerWinCondition.remove(player);
        }
        if(nameWinCondition.containsKey(name)){
            nameWinCondition.get(name).getPlayers().add(player);
            playerWinCondition.put(player, nameWinCondition.get(name));
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public static List<WinCondition> getWinConditions() {
        return winConditions;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public WinConditionType getWinConditionType() {
        return winConditionType;
    }

    public static WinMessageType getWinMessageType() {
        return winMessageType;
    }

    public static HashMap<Player, WinCondition> getPlayerWinCondition() {
        return playerWinCondition;
    }

    public static HashMap<String, WinCondition> getNameWinCondition() {
        return nameWinCondition;
    }

    public static boolean revive(Player player){
        if(playerDeath.containsKey(player)){
            WinCondition winCondition = playerDeath.get(player);
            if(!winConditions.contains(winCondition)){
                winConditions.add(winCondition);
            }
            addPlayer(winCondition.getName(), player);
            return true;
        }
        return false;
    }
}
