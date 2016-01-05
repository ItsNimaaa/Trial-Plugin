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

public class GemsCommand implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{

		if (sender instanceof Player)
		{

			Player player = (Player) sender;

			if (PlayerData.players.get(player).rank.getPerm() >= Ranks.DEV.getPerm())
			{
				if (args.length == 3)
				{
					Player target = Bukkit.getPlayerExact(args[1]);

					if (target != null)
					{
						PlayerData targetData = PlayerData.players.get(target);

						if (args[0].equalsIgnoreCase("set"))
						{
							try
							{
								targetData.gems = Integer.parseInt(args[2]);
								ChatUtil.message(player, "Set " + target.getName() + " to " + Integer.parseInt(args[2]), ChatUtil.GEMS);
							} catch (Exception e)
							{
								ChatUtil.message(player, "Integers only!", ChatUtil.GEMS);
							}
						} 
						else if (args[0].equalsIgnoreCase("add"))
						{
							try
							{
								targetData.gems = targetData.gems + Integer.parseInt(args[2]);

								ChatUtil.message(player,
										"Added " + Integer.parseInt(args[2]) + " to " + target.getName(),
										ChatUtil.GEMS);
							} catch (Exception e)
							{
								ChatUtil.message(player, "Integers only!", ChatUtil.GEMS);
							}
						} 
						else if (args[0].equalsIgnoreCase("remove"))
						{
							if (targetData.gems - Integer.parseInt(args[2]) < 0)
							{
								ChatUtil.message(player, "You cannot remove to below 0!", ChatUtil.GEMS);
							} else
							{
								try
								{

									targetData.gems = targetData.gems - Integer.parseInt(args[2]);
									ChatUtil.message(player,
											"Removed " + Integer.parseInt(args[2]) + " from " + target.getName(), ChatUtil.GEMS);
								} catch (Exception e)
								{
									ChatUtil.message(player, "Integers only!", ChatUtil.GEMS);
								}
							}
						}
					} 
					else
					{
						ChatUtil.message(player, "That player is offline.", ChatUtil.GEMS);
					}
				} 
				else if (args.length == 1)
				{
					Player getTarget = Bukkit.getPlayerExact(args[0]);

					if (getTarget != null)
					{
						ChatUtil.message(player,
								getTarget.getName() + "'s coins are " + PlayerData.players.get(getTarget).gems,
								ChatUtil.GEMS);
					} 
					else
					{
						ChatUtil.message(player, "That player is offline.", ChatUtil.GEMS);
					}
				} 
				else
				{
					ChatUtil.message(player, "/Gems <name> - Displays players Gems", ChatUtil.GEMS);
					ChatUtil.message(player, "/Gems set <name> <Integer> - Sets players Gems", ChatUtil.GEMS);
					ChatUtil.message(player, "/Gems add <name> <Integer> - Adds players Gems", ChatUtil.GEMS);
					ChatUtil.message(player, "/Gems remove <name> <Integer> - Takes players Gems", ChatUtil.GEMS);
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
				Player target = Bukkit.getPlayerExact(args[1]);

				if (target != null)
				{
					PlayerData targetData = PlayerData.players.get(target);

					if (args[0].equalsIgnoreCase("set"))
					{
						targetData.gems = Integer.parseInt(args[2]);
						System.out.println("Set " + target.getName() + " to " + Integer.parseInt(args[2]));
					} 
					else if (args[0].equalsIgnoreCase("add"))
					{
						targetData.gems = targetData.gems + Integer.parseInt(args[1]);
						System.out.println("Added " + Integer.parseInt(args[2]) + " to " + target.getName());
					} 
					else if (args[0].equalsIgnoreCase("remove"))
					{
						if (targetData.gems - Integer.parseInt(args[2]) < 0)
						{
							System.out.println("You cannot remove to below 0!");
						} 
						else
						{
							targetData.gems = targetData.gems - Integer.parseInt(args[2]);
							System.out.println("Remove " + Integer.parseInt(args[2]) + " from " + target.getName());
						}
					}
				}
				else
				{
					System.out.println("The player is offline.");
				}
			}
			else if (args.length == 1)
			{
				Player getTarget = Bukkit.getPlayerExact(args[0]);

				if (getTarget != null)
				{
					System.out.println(getTarget + "'s stars are " + PlayerData.players.get(getTarget).gems);
				} 
				else
				{
					System.out.println("Player is offline.");
				}
			}
		}
		return false;
	}
}
