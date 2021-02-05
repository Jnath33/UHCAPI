package fr.jnath.UHCAPI.bukkit.plugin.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SecondePassEvent extends Event {
    private int totalSeconde;
    private int seconde;
    public SecondePassEvent(int totalSeconde, int seconde) {
        this.totalSeconde=totalSeconde;
        this.seconde=seconde;
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
}
