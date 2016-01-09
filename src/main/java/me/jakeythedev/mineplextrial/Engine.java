package me.jakeythedev.mineplextrial;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.jakeythedev.mineplextrial.arcade.Arcade;
import me.jakeythedev.mineplextrial.arcade.GameState;
import me.jakeythedev.mineplextrial.arcade.GameType;
import me.jakeythedev.mineplextrial.arcade.guis.Listeners;
import me.jakeythedev.mineplextrial.arcade.guis.Prefrences;
import me.jakeythedev.mineplextrial.arcade.guis.PrefrencesGUI;
import me.jakeythedev.mineplextrial.arcade.guis.SpectatorGUI;
import me.jakeythedev.mineplextrial.listeners.ConnectionListeners;
import me.jakeythedev.mineplextrial.listeners.GlobalListeners;
import me.jakeythedev.mineplextrial.ranks.commands.AdminCommand;
import me.jakeythedev.mineplextrial.ranks.commands.AdminReplyCommand;
import me.jakeythedev.mineplextrial.ranks.commands.GemsCommand;
import me.jakeythedev.mineplextrial.ranks.commands.PreferencesCommand;
import me.jakeythedev.mineplextrial.ranks.commands.RankSetCommand;
import me.jakeythedev.mineplextrial.utilities.mysql.MySql;
import me.jakeythedev.mineplextrial.utilities.mysql.playerdata.PlayerData;
import me.jakeythedev.mineplextrial.utilities.scoreboard.ScoreboardManagers;
import me.jakeythedev.mineplextrial.utilities.scoreboard.ScoreboardUtil;
import me.jakeythedev.mineplextrial.utilities.world.WorldUtil;

public class Engine extends JavaPlugin implements Listener
{

	public MySql sql;

	public void onEnable()
	{

		final FileConfiguration configuration = getConfig();

		getConfig().options().copyDefaults(true);
		saveConfig();

		sql = new MySql(this,
				configuration.getString("IP"),
				configuration.getString("PORT"),
				configuration.getString("DB"),
				configuration.getString("USERNAME"),
				configuration.getString("PASSWORD"));

		openConnection();

		Prefrences prefrences = new Prefrences();
		final Arcade arcade = new Arcade(this, prefrences);
		ConnectionListeners connection = new ConnectionListeners(this, arcade, prefrences);
		SpectatorGUI specs = new SpectatorGUI(arcade);
		PrefrencesGUI adminGUI = new PrefrencesGUI(prefrences);

		new WorldUtil(this);
		new ScoreboardManagers(this, arcade);
		new ScoreboardUtil(this, arcade);

		arcade.gametype = GameType.NONE;
		arcade.gamestate = GameState.NOT_READY;

		arcade.addGames();
		prefrences.gameEnabled = false;

		getCommand("preferences").setExecutor(new PreferencesCommand(adminGUI));
		getCommand("a").setExecutor(new AdminCommand());
		getCommand("ma").setExecutor(new AdminReplyCommand());
		getCommand("ranks").setExecutor(new RankSetCommand());
		getCommand("gems").setExecutor(new GemsCommand());

		ScoreboardManagers.loadScoreboards();

		Bukkit.getPluginManager().registerEvents(connection, this);
		Bukkit.getPluginManager().registerEvents(new GlobalListeners(this, arcade, prefrences), this);
		Bukkit.getPluginManager().registerEvents(new Listeners(arcade, specs, prefrences, adminGUI), this);

		for (Player all : Bukkit.getOnlinePlayers())
			PlayerData.players.put(all, new PlayerData(this, all));

		System.out.println("Mineplex-Trial enabled! Created by JakeyTheDev.");
	}

	public void onDisable()
	{

		for (Player all : Bukkit.getOnlinePlayers())
			PlayerData.players.get(all).saveData(all);

		System.out.println("Mineplex-Trial disabled! Created by JakeyTheDev.");
	}

	@SuppressWarnings("deprecation")
	public void openConnection()
	{
		try
		{
			if (MySql.checkConnection() == false)
			{

				System.out.println("Not Connected to the SQL Servers!");
				try
				{
					MySql.openConnection();
				} catch (ClassNotFoundException e)
				{
					System.out.println("Connection opened between your server and MYSQL database.");
				}
			}
			else
			{

				Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new Runnable()
				{

					@Override
					public void run()
					{

						try
						{
							if (MySql.openConnection().isClosed())
							{
								MySql.openConnection();
							}
							else
							{
								System.out.println("SQL connection still stable!");
							}
						} catch (SQLException e)
						{
							System.out.println("SQL connection disabled by SQL Exception in Main");
							e.printStackTrace();
						} catch (ClassNotFoundException e)
						{
							System.out.println("SQL connection disabled by ClassNotFound Exception in Main");
							e.printStackTrace();
						}
					}
				}, 250 * 20, 250 * 20);
			}
		} catch (SQLException e)
		{
			System.out.println("Could not connect to servers for SQL!");
			e.printStackTrace();
		}
	}
}
