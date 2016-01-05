package me.jakeythedev.mineplextrial.ranks.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jakeythedev.mineplextrial.ranks.Ranks;
import me.jakeythedev.mineplextrial.utilities.ChatUtil;
import me.jakeythedev.mineplextrial.utilities.mysql.playerdata.PlayerData;

public class AdminCommand implements CommandExecutor
{


	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;

			boolean isOnline = false;
			
			PlayerData pd = PlayerData.players.get(player);
			
			if (args.length > 0)
			{
				StringBuilder sb = new StringBuilder();

				for (int i = 0; i < args.length; i++)
				{
					sb.append(args[i]).append(" ");
				}
				
				for (Player staff : Bukkit.getOnlinePlayers())
				{
					PlayerData staffPD = PlayerData.players.get(staff);
					
					if (staffPD.rank.getPerm() >= Ranks.TRAINEE.getPerm()) 
					{
						
						if (!staff.equals(player)) 
						{						
							ChatUtil.message(staff, pd.rank.getColour() + pd.rank.getName() + " " + player.getName() + ChatColor.GREEN + " " + sb, ChatUtil.NONE);
							staff.playSound(staff.getLocation(), Sound.ITEM_PICKUP, (float) 10.0, (float) 10.0);
							isOnline = true;
						}
					}
				}
				
				if (!isOnline) 
				{
					ChatUtil.message(player, "No staff are online right now.", ChatUtil.ADMIN);
					return false;
				}
				
				ChatUtil.message(player, pd.rank.getColour() + pd.rank.getName() + " " + player.getName() + ChatColor.GREEN + " " + sb, ChatUtil.NONE);
				
			}
		} 
		else 
		{
			System.out.println("Online players can use this.");
		}
		return false;
	}
}
