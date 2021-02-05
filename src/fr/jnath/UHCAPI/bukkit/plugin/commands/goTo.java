package fr.jnath.UHCAPI.bukkit.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class goTo implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
		if(!(sender instanceof Player))return false;
		Player player = (Player) sender;
		for(World world : Bukkit.getWorlds()) {
			if(world.getName().contains(args[0])) {
				player.teleport(new Location(Bukkit.getWorld(args[0]), 0, 150, 0));
			}
		}
		return false;
	}

}
