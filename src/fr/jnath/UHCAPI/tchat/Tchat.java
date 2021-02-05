package fr.jnath.UHCAPI.tchat;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tchat {
    private final String tag, tchatName;
    private final TchatType type;
    private final TchatFormat tchatFormat;
    private final List<Player> playerTchatAccess = new ArrayList<>();
    private final static HashMap<String, Tchat> s_all_tchat = new HashMap<>();

    public Tchat(String tchatName, String tag, TchatType type, TchatFormat tchatFormat) {
        this.tag = tag;
        this.tchatName=tchatName;
        this.type = type;
        this.tchatFormat=tchatFormat;
        s_all_tchat.put(tchatName, this);
    }

    public static void sendMessage(List<Player> players, String tchatName, String message, String sender, String otherOne, String otherTwo, String otherThree){
        Tchat tchat = get(tchatName);
        String msg = tchat.tchatFormat.generate(message,tchat.getTag(), sender,otherOne,otherTwo,otherThree);
        for(Player pls : players){
            pls.sendMessage(msg);
        }

    }

    public static void sendMessage(List<Player> players, String tchatName, String message, String sender){
        sendMessage(players, tchatName,message,sender,"","","");
    }

    public static Tchat get(String name) {
        try {
            return s_all_tchat.get(name);
        }catch (NullPointerException e){
            return null;
        }
    }

    public String getTag() {
        return tag;
    }

    public String getTchatName() {
        return tchatName;
    }

    public TchatType getType() {
        return type;
    }

    public TchatFormat getTchatFormat() {
        return tchatFormat;
    }
}
