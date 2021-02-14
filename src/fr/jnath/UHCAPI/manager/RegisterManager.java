package fr.jnath.UHCAPI.manager;

import fr.jnath.UHCAPI.bukkit.plugin.UhcAPI;
import fr.jnath.UHCAPI.bukkit.plugin.commands.GetConfigItem;
import fr.jnath.UHCAPI.bukkit.plugin.commands.UhcCmd;
import fr.jnath.UHCAPI.bukkit.plugin.commands.goTo;
import fr.jnath.UHCAPI.bukkit.plugin.listeners.*;
import fr.jnath.UHCAPI.bukkit.plugin.tabCompleter.UhcCmdTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class RegisterManager {
    public void register(UhcAPI uhcAPI){
        //set listeners
        PluginManager pm = uhcAPI.getServer().getPluginManager();
        pm.registerEvents(new HubListeners(), uhcAPI);
        pm.registerEvents(new UHCListeners(), uhcAPI);
        pm.registerEvents(new TchatListeners(), uhcAPI);
        pm.registerEvents(new ChangeStatusListeners(), uhcAPI);
        pm.registerEvents(new DeathListeners(), uhcAPI);


        //set commands executor
        uhcAPI.getCommand("config").setExecutor(new GetConfigItem());
        uhcAPI.getCommand("goto").setExecutor(new goTo());
        uhcAPI.getCommand("uhc").setExecutor(new UhcCmd());

        //set tab completer
        uhcAPI.getCommand("uhc").setTabCompleter(new UhcCmdTabCompleter());
    }
}
