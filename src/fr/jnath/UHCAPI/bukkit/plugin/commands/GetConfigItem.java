package fr.jnath.UHCAPI.bukkit.plugin.commands;

import fr.virthia.utils.item.ItemCreator;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class GetConfigItem implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(!(arg0 instanceof Player)) {
			return false;
		}
		((Player) arg0).getInventory().addItem(new ItemCreator("§5Config", Material.STICK, 1).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).make());
		return true;
	}

}
