package me.jakeythedev.mineplextrial.utilities.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import me.jakeythedev.mineplextrial.Engine;
import me.jakeythedev.mineplextrial.arcade.Arcade;
import me.jakeythedev.mineplextrial.arcade.GameState;
import me.jakeythedev.mineplextrial.utilities.packets.TabTitle;

public class ScoreboardManagers
{
	private static Engine _engine;
	private static Arcade _arcade;

	private static ScoreboardManager manager;
	public static Scoreboard mainScoreboard;

	public ScoreboardManagers(Engine engine, Arcade arcade)
	{
		manager = Bukkit.getScoreboardManager();
		mainScoreboard = manager.getMainScoreboard();
		_engine = engine;
		_arcade = arcade;
	}

	/*
	 * / Quick method for creating a scoreboard.
	 */

	public static Scoreboard createScoreboard()
	{
		Scoreboard scoreboard = manager.getNewScoreboard();
		return scoreboard;
	}

	/*
	 * / Quick method for setting scores on a scoreboard.
	 */

	public static void setScores(Objective obj, String message, int row)
	{

		Score score = obj.getScore(message);
		score.setScore(row);
	}
	/*
	 * / Quick method for creating objectives on a scoreboard.
	 */

	public static Objective createObjective(Scoreboard board, DisplaySlot slot, String name)
	{
		Objective obj = board.registerNewObjective("Board", "dummy");
		obj.setDisplayName(name);
		obj.setDisplaySlot(slot);

		return obj;
	}
	
	public static void loadScoreboards() 
	{
		Bukkit.getScheduler().scheduleSyncRepeatingTask(_engine, new Runnable()
		{
			
			@Override
			public void run()
			{
				for (Player all : Bukkit.getOnlinePlayers()) 
				{
					switch (_arcade.getGameState()) 
					{
					case NOT_READY:
						TabTitle.sendTabTitle(all, 
								ChatColor.GOLD.toString() + ChatColor.BOLD + "MINEPLEX - JR.DEV TRIAL",
								ChatColor.GOLD + "Games will start shortly.");
						ScoreboardUtil.giveWaitingScoreboard(all);
						break;
					case COUNTDOWN:
						TabTitle.sendTabTitle(all, 
								ChatColor.GOLD.toString() + ChatColor.BOLD + "MINEPLEX - JR.DEV TRIAL",
								ChatColor.GOLD + "Games will start shortly.");
						ScoreboardUtil.giveWaitingScoreboard(all);
						break;
					case IN_PROGRESS:
						TabTitle.sendTabTitle(all, 
								ChatColor.GOLD.toString() + ChatColor.BOLD + "MINEPLEX - JR.DEV TRIAL",
								ChatColor.GOLD + "Game: " + _arcade.getSelected().getName() + "\n" + "Map: " + _arcade.getMapName());
						ScoreboardUtil.giveGameScoreboard(all);
						break;
					case ENDING:
						TabTitle.sendTabTitle(all, 
								ChatColor.GOLD.toString() + ChatColor.BOLD + "MINEPLEX - JR.DEV TRIAL",
								ChatColor.GOLD + "Games will start shortly.");
						ScoreboardUtil.giveWaitingScoreboard(all);
						break;
					}
				}
			}
		}, 0L, 2*20);
	}
}