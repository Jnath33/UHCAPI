package fr.jnath.UHCAPI.bukkit.plugin.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MinutePassEvent extends Event {
    private int totalMinute;
    private int minute;
    public MinutePassEvent(int totalMinute, int minute) {
        this.totalMinute=totalMinute;
        this.minute=minute;
    }

    public int getTotalMinute() {
        return totalMinute;
    }

    public int getMinute() {
        return minute;
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
