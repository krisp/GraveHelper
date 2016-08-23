package com.krisp.minecraft.gravehelper;

import me.koenn.gravestones.grave.Grave;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.permissions.Permission;
import org.bukkit.ChatColor;

public class Gravehelper extends JavaPlugin implements Listener {
	private Logger log;
	private org.bukkit.Server server; 
	
	public final static Permission GRAVEWARP_PERM = new Permission("gravehelper.command.grave");
	public final static Permission GRAVELIST_PERM = new Permission("gravehelper.command.graves");
	public final static Permission GRAVEPOP_PERM = new Permission("gravehelper.command.gravepop");
	
	public void onEnable() {
		this.server = Bukkit.getServer();
		this.log = server.getLogger();						
		server.getPluginManager().registerEvents(this, this);
		log.info("[GraveHelper] loaded!");
	}
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent e) {
		String msg = e.getMessage();
		Player sender = e.getPlayer();
		String[] args = msg.split(" "); 
						
		if(args[0].equalsIgnoreCase("/graves"))
		{
			if(!sender.hasPermission(GRAVELIST_PERM)) {
				sender.sendMessage(ChatColor.RED + "Sorry, you're not allowed to do that.");
				return;
			}
			
			if(!Grave.gravestones.isEmpty()) {
				sender.sendMessage(ChatColor.BLUE + "List of graves:");				
				((ArrayList<Grave>) Grave.gravestones.clone()).forEach(grave -> {									
					int i = 1;
					sender.sendMessage(" " + (i++) + ChatColor.BLUE + grave.getPlayerName() + " "
							+ (int)grave.getLocation().getX() + " " 
							+ (int)grave.getLocation().getY() + " " 
							+ (int)grave.getLocation().getZ() + " "
							+ grave.getCounter().getTimeLeft());
				});
			} else {
				sender.sendMessage(ChatColor.RED + "No gravestones found");
			}
		} else if(args[0].equalsIgnoreCase("/grave")) {			
			
			if(!sender.hasPermission(GRAVEWARP_PERM)) {
				sender.sendMessage(ChatColor.RED + "Sorry, you're not allowed to do that.");
				return;
			}
			
			if(!Grave.gravestones.isEmpty()) {
				((ArrayList<Grave>) Grave.gravestones.clone()).forEach(grave -> {
					if (grave.getPlayerName() == sender.getName()) {
						sender.teleport(grave.getLocation());
						sender.sendMessage(ChatColor.BLUE + "Warped to grave");
						return;
					}
				});
			} else {
				sender.sendMessage(ChatColor.RED + "No gravestones found");
			}			
		} else if(args[0].equalsIgnoreCase("/gravepop")) {
			if(!sender.hasPermission(GRAVEPOP_PERM)) {
				sender.sendMessage(ChatColor.RED + "Sorry, you're not allowed to do that.");
				return;
			}
			if(!Grave.gravestones.isEmpty()) {
				((ArrayList<Grave>) Grave.gravestones.clone()).forEach(grave -> {
					if (grave.getPlayerName() == sender.getName()) {
						grave.dropContents(sender.getLocation());
						grave.destroy();
						return;
					}
				});			
			} else {
				sender.sendMessage(ChatColor.RED + "No gravestones found");			
			}
		}
	}
	
	public void onDisable() {
		log.info("[gravehelper] unloading");
	}
}