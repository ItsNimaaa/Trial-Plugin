package me.jakeythedev.mineplextrial.ranks.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jakeythedev.mineplextrial.ranks.Ranks;
import me.jakeythedev.mineplextrial.utilities.ChatUtil;
import me.jakeythedev.mineplextrial.utilities.mysql.playerdata.PlayerData;

public class AdminReplyCommand implements CommandExecutor
{

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			
			PlayerData pd = PlayerData.players.get(player);

			if (pd.rank.getPerm() >= Ranks.TRAINEE.getPerm())
			{
				if (args.length > 1)
				{
					Player target = Bukkit.getPlayer(args[0]);

					if (target == null)
					{
						ChatUtil.message(player, args[0] + " is offline", ChatUtil.ADMIN);
						
					} 
					else if (target == player)
					{
						ChatUtil.message(player, "You're not allowed to respond to yourself.", ChatUtil.ADMIN);
					}
					else 
					{

						StringBuilder sb = new StringBuilder();

						for (int i = 1; i < args.length; i++)
						{
							sb.append(args[i]).append(" ");
						}
						
						PlayerData targetPD = PlayerData.players.get(target);
						
						ChatUtil.message(player, ChatColor.GREEN + "-> " + targetPD.rank.getColour() + 
								targetPD.rank.getName() + " " + target.getName() + ChatColor.GREEN + " " + sb, ChatUtil.NONE);
						
						ChatUtil.message(target, ChatColor.GREEN + 
								"<- " + pd.rank.getColour() + pd.rank.getName() + " " + player.getName() +
								ChatColor.GREEN + " " + sb, ChatUtil.NONE);
					
						for (Player staff : Bukkit.getOnlinePlayers())
						{
							PlayerData staffPD = PlayerData.players.get(staff);
							
							if (staffPD.rank.getPerm() >= Ranks.TRAINEE.getPerm()) 
							{
								
								if (!staff.equals(player) && !staff.equals(target)) 
								{						
									ChatUtil.message(staff, pd.rank.getColour() + pd.rank.getName() + " " + player.getName()
									   + ChatColor.GREEN + " -> " + targetPD.rank.getColour() + targetPD.rank.getName() + 
									   		" " + target.getName() + ChatColor.GREEN + " " + sb, ChatUtil.NONE);
								
								}
							}
						}
					}
				}
				else if (args.length < 1)
				{
					ChatUtil.message(player, "Correct command, /ma <player> <reply>", ChatUtil.PERMISSION);
				}
			} 
			else
			{
				ChatUtil.message(player, ChatColor.GREEN + "This command requires the rank " + ChatColor.WHITE + "["
						+ Ranks.getPrefix(true, true, Ranks.TRAINEE) + ChatColor.WHITE + "]", ChatUtil.PERMISSION);
			}
		} 
		else
		{
			System.out.println("Only players can run this command.");
		}
		return false;
	}
}
