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

public class RankSetCommand implements CommandExecutor
{


	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;

			if (PlayerData.players.get(player).rank.getPerm() >= Ranks.DEV.getPerm())
			{
				if (args.length == 3)
				{
					if (args[0].equalsIgnoreCase("set")) 
					{

						Player target = Bukkit.getPlayerExact(args[1]);

						if (target != null)
						{
							try
							{
								Ranks rank = Ranks.valueOf(args[2].toUpperCase());
								PlayerData.setRank(target, rank);
								
								ChatUtil.message(player, "You've set " +
										ChatColor.GREEN + target.getName()
										+ ChatColor.WHITE + " to the rank " + Ranks.getPrefix(false, true, rank), ChatUtil.PERMISSION);

								ChatUtil.message(target, player.getName() + " has updated your rank to " + Ranks.getPrefix(false, true, rank), ChatUtil.PERMISSION);
							} catch (Exception e)
							{
								ChatUtil.message(player, "Invalid rank! Use /ranks list", ChatUtil.PERMISSION);
							}
						} else
						{
							ChatUtil.message(player, "This person is not online.", ChatUtil.PERMISSION);
						}
					}
				} 
				else if (args.length == 1) 
				{
					if (args[0].equalsIgnoreCase("list")) 
					{
						StringBuilder sb = new StringBuilder();

						for (Ranks ranks : Ranks.values()) 
						{
							sb.append(ChatColor.GREEN.toString() + ranks).append(ChatColor.WHITE.toString() + ",");
						}
						
						sb.deleteCharAt(sb.lastIndexOf(","));
						ChatUtil.message(player, sb + ".", ChatUtil.PERMISSION);
					}
				} 
				else 
				{
					ChatUtil.message(player, "/ranks set <name> <rank>", ChatUtil.PERMISSION);
				}
			} 
			else
			{
				ChatUtil.message(player, ChatColor.GREEN + "This command requires the rank " + ChatColor.WHITE + "["
						+ Ranks.getPrefix(true, true, Ranks.DEV) + ChatColor.WHITE + "]", ChatUtil.PERMISSION);
			}
		} 
		else
		{
			if (args.length == 3)
			{
				if (args[0].equalsIgnoreCase("set")) 
				{
					Player target = Bukkit.getPlayerExact(args[1]);

					if (target != null)
					{
						try
						{
							Ranks rank = Ranks.valueOf(args[2].toUpperCase());
							PlayerData.setRank(target, rank);;

							System.out.println("You have set " + ChatColor.GREEN + target.getName() + ChatColor.BLUE
									+ " to " + Ranks.getPrefix(true, true, rank));

								ChatUtil.message(target, "Your rank has been set to " + Ranks.getPrefix(true, true, rank)
									+ ChatColor.RESET + " by " + ChatColor.GREEN + "CONSOLE", ChatUtil.PERMISSION);
							
						} catch (Exception e)
						{
							System.out.println("This rank does not exist.");
						}
					}
					else
					{
						System.out.println("Player is offline.");
					}
				}
			} 
			else
			{
				System.out.println("/ranks set <name> <rank>");
			}
		}
		return false;
	}
}
