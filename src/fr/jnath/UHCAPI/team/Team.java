package fr.jnath.UHCAPI.team;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Team {

    private final String name, displayName, tag;
    private List<Player> teamMember = new ArrayList<>();
    private static Map<Player, Team> s_all_player_team = new HashMap<>();

    public Team(String name, String tag, String displayName) {
        this.name = name;
        this.tag = tag;
        this.displayName=displayName;
    }

    public void addMemberOnTeam(Player player){
        if(haveTeam(player)){
            get(player).getTeamMember().remove(player);
        }
        teamMember.add(player);
        s_all_player_team.put(player, this);
    }

    public static Team get(Player player){
        if(!haveTeam(player)) return null;
        return s_all_player_team.get(player);
    }

    public static boolean haveTeam(Player player){
        return s_all_player_team.containsKey(player);
    }

    public String getTag(){
        return tag;
    }

    public void setTeamMember(List<Player> players){
        teamMember=players;
        for(Player pls : players){
            s_all_player_team.put(pls, this);
        }
    }

    public String getName() {
        return name;
    }

    public List<Player> getTeamMember() {
        return teamMember;
    }
}
