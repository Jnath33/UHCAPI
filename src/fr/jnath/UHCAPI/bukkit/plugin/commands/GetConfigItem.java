package fr.jnath.UHCAPI.bukkit.plugin.commands;

import fr.jnath.Utils.Utils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.material.Chest;

public class GetConfigItem implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(!(arg0 instanceof Player)) {
			return false;
		}
		((Player) arg0).getInventory().addItem(Utils.createItem("§5Config", Material.STICK, 1, Enchantment.PROTECTION_ENVIRONMENTAL, 1));
		return true;
	}

}
