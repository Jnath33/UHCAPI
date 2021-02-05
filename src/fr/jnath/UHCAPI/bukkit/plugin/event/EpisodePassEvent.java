package fr.jnath.UHCAPI.bukkit.plugin.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EpisodePassEvent extends Event {
    private int episode;
    public EpisodePassEvent(int episode) {
        this.episode=episode;
    }

    public int getEpisode() {
        return episode;
    }

    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
