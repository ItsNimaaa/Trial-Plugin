package me.jakeythedev.mineplextrial.ranks.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jakeythedev.mineplextrial.arcade.guis.PrefrencesGUI;
import me.jakeythedev.mineplextrial.ranks.Ranks;
import me.jakeythedev.mineplextrial.utilities.ChatUtil;
import me.jakeythedev.mineplextrial.utilities.mysql.playerdata.PlayerData;

public class PreferencesCommand implements CommandExecutor
{
	
	private PrefrencesGUI _prefs;

	public PreferencesCommand(PrefrencesGUI prefs)
	{
		_prefs = prefs;
	}


	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			PlayerData playerData = PlayerData.players.get(player);

			if (playerData.rank.getPerm() >= Ranks.PLAYER.getPerm()) 
			{

				_prefs.build(player);

			} 
			else 
			{
				ChatUtil.message(player, ChatColor.GREEN + "This command requires the rank " + ChatColor.WHITE + "["
						+ Ranks.getPrefix(true, true, Ranks.PLAYER) + ChatColor.WHITE + "]", ChatUtil.PERMISSION);
			}
		}
		else 
		{
			System.out.println("Online players can use this.");
		}
		return false;
	}
}