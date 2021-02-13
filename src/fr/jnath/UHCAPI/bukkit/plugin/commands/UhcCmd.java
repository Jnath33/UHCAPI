package fr.jnath.UHCAPI.bukkit.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UhcCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
        if(!(args.length>0))notTheGoodArgementSize(sender);
        String command = args[0];
        if(command.equals("help"))
            helpCmd(sender);
        else if(command.equals("scenario"))
            scenarioCmd(sender);
        else if(command.equals("inv"))
            invCmd(sender);
        return false;
    }

    public boolean notTheGoodArgementSize(CommandSender sender) {
        sender.sendMessage(ChatColor.RED+"Vous n'avez pas mis le bon nombre d'arguement /uhc help pour avoir l'aide");
        return false;
    }

    public void helpCmd(CommandSender sender){

    }

    public void scenarioCmd(CommandSender sender){

    }

    public void invCmd(CommandSender sender){

    }
}
