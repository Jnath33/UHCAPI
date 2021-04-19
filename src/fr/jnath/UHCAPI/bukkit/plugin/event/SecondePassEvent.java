package fr.jnath.UHCAPI.bukkit.plugin.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SecondePassEvent extends Event {
    private int seconde, minute, episode, totalSeconde;
    public SecondePassEvent(int totalSeconde, int seconde, int minute, int episode) {
        this.totalSeconde=totalSeconde;
        this.seconde=seconde;
        this.minute = minute;
        this.episode = episode;
    }

    public int getTotalSeconde() {
        return totalSeconde;
    }

    public int getSeconde() {
        return seconde;
    }

    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public int getEpisode() {
        return episode;
    }

    public int getMinute() {
        return minute;
    }
}
