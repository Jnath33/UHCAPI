package fr.jnath.UHCAPI.game;

import fr.jnath.UHCAPI.bukkit.plugin.UhcAPI;
import fr.jnath.UHCAPI.game.win.WinCondition;
import fr.jnath.UHCAPI.tchat.Tchat;
import fr.jnath.UHCAPI.tchat.TchatContentType;
import fr.jnath.UHCAPI.tchat.TchatFormat;
import fr.jnath.UHCAPI.tchat.TchatType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Status status;
    private final String tchatNameInfo, tchatNameGlobal;
    private final List<Player> players = new ArrayList<>(), playersAlive = new ArrayList<>();
    private boolean useTeam;

    public Game(String tag) {
        tchatNameInfo="generalInfo";
        tchatNameGlobal="generalGlobal";
        TchatFormat tchatFormatInfo= new TchatFormat();
        tchatFormatInfo.setNext(TchatContentType.TAG);
        tchatFormatInfo.setNext(TchatContentType.MESSAGE);
        new Tchat(tchatNameInfo,tag, TchatType.INFORMATION,tchatFormatInfo);
        TchatFormat tchatFormatGlobal= new TchatFormat();
        tchatFormatGlobal.setNext(TchatContentType.TAG);
        tchatFormatGlobal.setNext(TchatContentType.OTHER_1);
        tchatFormatGlobal.setNext(TchatContentType.PLAYER);
        tchatFormatGlobal.setNext(TchatContentType.MESSAGE);
        new Tchat(tchatNameGlobal, "§c[§4Global§c]§7", TchatType.GLOBAL,tchatFormatGlobal);
        this.status=Status.WAITING;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void info(String message){
        List<Player> plss = new ArrayList<>();
        for(Player pls : Bukkit.getOnlinePlayers()){
            plss.add(pls);
        }
        Tchat.sendMessage(plss, tchatNameInfo, message, "");
    }

    public boolean statusEqual(Status[] statuses) {
        for(Status status : statuses){
            if(status == this.status){
                return true;
            }
        }
        return false;
    }

    public void setUseTeam(boolean useTeam) {
        this.useTeam = useTeam;
    }

    public boolean isUseTeam() {
        return useTeam;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public static void win(WinCondition winCondition){
        if(winCondition==null){
            UhcAPI.getGame().info("Égaliter");
        }else {
            UhcAPI.getGame().info("Victoire de l'équipe "+winCondition.getDisplayName());
        }
    }

    public String getTchatNameInfo() {
        return tchatNameInfo;
    }

    public String getTchatNameGlobal() {
        return tchatNameGlobal;
    }

    public List<Player> getPlayersAlive() {
        return playersAlive;
    }
}
