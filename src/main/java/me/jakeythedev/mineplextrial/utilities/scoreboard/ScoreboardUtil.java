package me.jakeythedev.mineplextrial.utilities.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import me.jakeythedev.mineplextrial.Engine;
import me.jakeythedev.mineplextrial.arcade.Arcade;
import me.jakeythedev.mineplextrial.arcade.GameState;
import me.jakeythedev.mineplextrial.utilities.mysql.playerdata.PlayerData;

public class ScoreboardUtil
{
	private static Engine _engine;
	private static Arcade _arcade;
	
	public ScoreboardUtil(Engine engine, Arcade arcade)
	{
		_engine = engine;
		_arcade = arcade;
	}

	/*
	 * / Generates the waiting in lobby scoreboard!
	 */

	private static Objective object;
	
	private static int _task;
	private static int _num;
	
	public static void giveWaitingScoreboard(final Player player)
	{

		player.setScoreboard(ScoreboardManagers.createScoreboard());
		
		object = ScoreboardManagers.createObjective(player.getScoreboard(), DisplaySlot.SIDEBAR,
				ChatColor.GOLD.toString() + ChatColor.BOLD + "MINEPLEX");

		if (_arcade.getGameState() != GameState.COUNTDOWN) 
		{
			ScoreboardManagers.setScores(object, "     ", 8);
			ScoreboardManagers.setScores(object, "" + ChatColor.YELLOW.toString() + ChatColor.BOLD + "Players" + ChatColor.WHITE + ":", 7);
			ScoreboardManagers.setScores(object, "" + ChatColor.WHITE.toString() + Bukkit.getOnlinePlayers().size() + "/" + "12", 6);
			ScoreboardManagers.setScores(object, "    ", 5);
			ScoreboardManagers.setScores(object, "" + ChatColor.GOLD + ChatColor.BOLD + "Rank" + ChatColor.WHITE + ":", 4);
			ScoreboardManagers.setScores(object, "" + ChatColor.WHITE.toString() + PlayerData.players.get(player).rank.getName(), 3);
			ScoreboardManagers.setScores(object, "  ", 2);
			ScoreboardManagers.setScores(object, "" + ChatColor.AQUA + ChatColor.BOLD + "Server" + ChatColor.WHITE + ":", 1);
			ScoreboardManagers.setScores(object, _engine.getConfig().getString("ServerName"), 0);

		} 
		else 
		{
			
			_task = Bukkit.getScheduler().scheduleSyncRepeatingTask(_engine, new Runnable()
			{
				
				@Override
				public void run()
				{
					if (_num == 1) 
					{
						object.setDisplayName(ChatColor.GOLD.toString() + ChatColor.BOLD + "Loading.");
						_num++;
					}
					else if (_num == 2) 
					{
						object.setDisplayName(ChatColor.AQUA.toString() + ChatColor.BOLD + "Loading..");
						_num++;
					} 
					else if (_num == 3) 
					{
						object.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Loading...");
						_num++;
					} 
					else if (_num == 4) 
					{
						if (_arcade.getGameState() == GameState.IN_PROGRESS) 
						{
							Bukkit.getScheduler().cancelTask(_task);
						} 
						else 
						{
							_num = 1;
						}
					}
				}
			}, 0L, 1*20);
			
			ScoreboardManagers.setScores(object, "      ", 14);
			ScoreboardManagers.setScores(object, "" + ChatColor.YELLOW.toString() + ChatColor.BOLD + "Players" + ChatColor.WHITE + ":", 13);
			ScoreboardManagers.setScores(object, "" + ChatColor.WHITE.toString() + Bukkit.getOnlinePlayers().size() + "/" + "12", 12);
			ScoreboardManagers.setScores(object, "     ", 11);
			ScoreboardManagers.setScores(object, "" + ChatColor.GOLD + ChatColor.BOLD + "Rank" + ChatColor.WHITE + ":", 10);
			ScoreboardManagers.setScores(object, "" + ChatColor.WHITE.toString() + PlayerData.players.get(player).rank.getName(), 9);
			ScoreboardManagers.setScores(object, "    ", 8);
			ScoreboardManagers.setScores(object, "" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Game" + ChatColor.WHITE + ":", 7);
			ScoreboardManagers.setScores(object, "" + ChatColor.WHITE.toString() + _arcade.getSelected().getName(), 6);
			ScoreboardManagers.setScores(object, "   ", 5);
			ScoreboardManagers.setScores(object, "" + ChatColor.GREEN + ChatColor.BOLD + "Map" + ChatColor.WHITE + ":", 4);
			ScoreboardManagers.setScores(object, "" + ChatColor.WHITE.toString() + _arcade.getMapName(), 3);
			ScoreboardManagers.setScores(object, "  ", 2);
			ScoreboardManagers.setScores(object, "" + ChatColor.AQUA + ChatColor.BOLD + "Server" + ChatColor.WHITE + ":", 1);
			ScoreboardManagers.setScores(object, _engine.getConfig().getString("ServerName"), 0);

		}
	}

	/*
	 * / Generates the Game (spectator) scoreboard with the specs and non specs
	 * on.
	 */

	private static Objective _objective;

	public static void giveGameScoreboard(Player player)
	{

		player.setScoreboard(ScoreboardManagers.createScoreboard());

		_objective = ScoreboardManagers.createObjective(player.getScoreboard(), DisplaySlot.SIDEBAR,
				"" + ChatColor.WHITE + ChatColor.BOLD + _arcade.getSelected().getName().toUpperCase()); 

		int integer = 0;
		for (Player all : Bukkit.getOnlinePlayers())
		{
			if (!_arcade.getSpectators().contains(all))
			{
				ScoreboardManagers.setScores(_objective, ChatColor.AQUA + all.getName(), integer);
				integer++;

			} 
			else
			{
				ScoreboardManagers.setScores(_objective, "" + ChatColor.RED + ChatColor.ITALIC + all.getName(), integer);
				integer++;
			}
		}
	}
}
