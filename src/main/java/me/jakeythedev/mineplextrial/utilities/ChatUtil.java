package me.jakeythedev.mineplextrial.utilities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum ChatUtil
{

	NONE("", ChatColor.BLUE.toString()),
	
	JOIN("Join", ChatColor.BLUE.toString()),
	LEAVE("Leave", ChatColor.BLUE.toString()),

	MESSAGE("Message", ChatColor.BLUE.toString()),
	ANNOUNCE("Announce", ChatColor.BLUE.toString()),
	PERMISSION("Permissions", ChatColor.BLUE.toString()),
	COMMAND("Command", ChatColor.BLUE.toString()),
	ADMIN("Admin", ChatColor.BLUE.toString()),
	
	GAME("Game", ChatColor.BLUE.toString()),
	
	GEMS("Gems", ChatColor.GREEN.toString());
	
	private ChatUtil(String prefix, String colour)
	{
		_prefix = prefix;
		_colour = colour;
	}
	
	private String _prefix;
	private String _colour;
	
	public static void broadcast(String message, ChatUtil chat) 
	{
		if (chat == ChatUtil.NONE) 
		{
			Bukkit.broadcastMessage(chat.getColour() + chat.getPrefix() + ChatColor.WHITE + " " + ChatColor.RESET + message);
		} 
		else 
		{
			Bukkit.broadcastMessage(chat.getColour() + chat.getPrefix() + ChatColor.WHITE + "> " + ChatColor.RESET + message);
		}
	}
	
	public static void message(Player player, String message, ChatUtil chat) 
	{
		{
			if (chat == ChatUtil.NONE) 
			{
				player.sendMessage(chat.getColour() + chat.getPrefix() + ChatColor.WHITE + " " + ChatColor.RESET + message);
			} 
			else 
			{
				player.sendMessage(chat.getColour() + chat.getPrefix() + ChatColor.WHITE + "> " + ChatColor.RESET + message);
			}
		}
	}
	
	public String getPrefix() 
	{
		return _prefix;
	}
	
	public String getColour() 
	{
		return _colour;
	}
}
