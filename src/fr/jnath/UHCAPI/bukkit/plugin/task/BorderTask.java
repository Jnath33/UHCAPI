package fr.jnath.UHCAPI.bukkit.plugin.task;

import fr.jnath.UHCAPI.bukkit.plugin.UhcAPI;
import org.bukkit.WorldBorder;
import org.bukkit.scheduler.BukkitRunnable;

public class BorderTask extends BukkitRunnable {

    private final int blockPerSeconde;
    private final double minimumBorderSize;
    private final WorldBorder world;
    private final WorldBorder netherWorld;

    public BorderTask(int blockPerSeconde, double minimumBorderSize) {
        this.blockPerSeconde = blockPerSeconde;
        this.minimumBorderSize = minimumBorderSize;
        world= UhcAPI.getWorld().getWorldBorder();
        netherWorld = UhcAPI.getNetherWorld().getWorldBorder();
    }

    @Override
    public void run() {
        if(world.getSize()-blockPerSeconde<minimumBorderSize){
            world.setSize(minimumBorderSize);
        }
        if(netherWorld.getSize()-blockPerSeconde<minimumBorderSize){
            netherWorld.setSize(minimumBorderSize);
        }
    }
}
