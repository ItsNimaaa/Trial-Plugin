package me.jakeythedev.mineplextrial.arcade;

import java.io.File;
import java.io.IOException;
import java.nio.channels.GatheringByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import me.jakeythedev.mineplextrial.Engine;
import me.jakeythedev.mineplextrial.arcade.games.Runner;
import me.jakeythedev.mineplextrial.arcade.games.Spleef;
import me.jakeythedev.mineplextrial.arcade.guis.Prefrences;
import me.jakeythedev.mineplextrial.utilities.ChatUtil;
import me.jakeythedev.mineplextrial.utilities.ItemUtil;
import me.jakeythedev.mineplextrial.utilities.packets.ActionBar;
import me.jakeythedev.mineplextrial.utilities.packets.Title;
import me.jakeythedev.mineplextrial.utilities.world.WorldUtil;

public class Arcade
{

	private Engine _engine;
	private Prefrences _pref;

	public Arcade(Engine engine, Prefrences prefrences)
	{
		_engine = engine;
		_pref = prefrences;
	}

	private String _mapName = "None";
	private Game _selected;

	public List<Player> _alivePlayers = new ArrayList<>();
	public List<Player> _spectators = new ArrayList<>();
	public ArrayList<Game> games = new ArrayList<>();
	public HashMap<Integer, Player> _positions = new HashMap<>();

	public GameState gamestate;
	public GameType gametype;


	public void addGames() 
	{
		games.add(new Runner(_engine, this));
		games.add(new Spleef(_engine, this));
	}

	public void load() 
	{
		_pref.isLoaded = false;

		gametype = GameType.NONE;
		gamestate = GameState.NOT_READY;

		WorldUtil.deleteWorld(false);

		if (!_pref.isLoaded) 
		{
			_selected = getRandomGame();
			gametype = GameType.valueOf(_selected.getName().toUpperCase());


			_mapName = getRandomMap();

			for (Player all : Bukkit.getOnlinePlayers())
				removeSpectator(all);

			_pref.isLoaded = true;

			Bukkit.getScheduler().runTaskLater(_engine, new Runnable()
			{

				@Override
				public void run()
				{
					if (Bukkit.getOnlinePlayers().size() >= 2) 
					{
						startLobbyCountdown();	
					} 
				}
			}, 5*20);
		} 
	}

	/*/
	 * GAME STOPPER WITH REASON
	 */

	public void stop(String reason) 
	{
		Bukkit.getScheduler().cancelTask(_lobbyTask);
		Bukkit.getScheduler().cancelTask(_gameTask);

		gametype = GameType.NONE;
		gamestate = GameState.NOT_READY;

		for (Player all : Bukkit.getOnlinePlayers()) 
		{
			removeSpectator(all);
			removeAlive(all);
			wipePos();
			all.teleport(Bukkit.getWorld("world").getSpawnLocation());
		}

		_pref.gameEnabled = false;
		_pref.enabledGame = false;

		ChatUtil.broadcast("Game stopped! Reason: " + reason, ChatUtil.GAME);

	}

	/*/
	 * GAME STOPPER WITH NO REASON
	 */

	public void stop() 
	{
		Bukkit.getScheduler().cancelTask(_lobbyTask);
		Bukkit.getScheduler().cancelTask(_gameTask);

		gametype = GameType.NONE;
		gamestate = GameState.NOT_READY;

		for (Player all : Bukkit.getOnlinePlayers()) 
		{
			removeSpectator(all);
			removeAlive(all);
			wipePos();
			all.teleport(Bukkit.getWorld("world").getSpawnLocation());
		}

		_pref.gameEnabled = false;
		_pref.enabledGame = false;

	}

	/*/
	 * COUNTDOWN VARIABLES
	 */

	private int _lobbyTask, _lobbyTime, _gameTask, _gameTime;

	public void startLobbyCountdown() 
	{
		_lobbyTime = 30;

		gamestate = GameState.COUNTDOWN;

		_lobbyTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(_engine, new Runnable()
		{

			@Override
			public void run()
			{
				if (_lobbyTime <= 0) 
				{
					for (Player all : Bukkit.getOnlinePlayers())
					{
						all.teleport(Bukkit.getWorld("GameWorld").getSpawnLocation());
						addAlive(all);
						removeSpectator(all);
					}

					startGameCountdown();
					_pref.started = true;
					Bukkit.getScheduler().cancelTask(_lobbyTask);

				} 
				else if (_lobbyTime >= 1 && _lobbyTime <= 30) 
				{
					for (Player all : Bukkit.getOnlinePlayers())
						ActionBar.sendActionbar(all, ChatColor.GRAY + getSelected().getName() + ChatColor.DARK_AQUA + " is starting in " + ChatColor.GRAY + _lobbyTime + " seconds.");

				} 
				else if (_lobbyTime == 20) 
				{
					ChatUtil.broadcast(ChatColor.DARK_AQUA.toString() + ChatColor.STRIKETHROUGH + "=====================================", ChatUtil.NONE);
					ChatUtil.broadcast(ChatColor.AQUA + "Game - " + ChatColor.WHITE + getSelected().getName(), ChatUtil.NONE);
					ChatUtil.broadcast("", ChatUtil.NONE);

					StringBuilder sb = new StringBuilder();

					for (String desc : getSelected().getDescription()) 
					{
						sb.append(desc).append("\n ");
					}
					
					ChatUtil.broadcast(ChatColor.AQUA + "Description - " + ChatColor.WHITE + sb, ChatUtil.NONE);
					ChatUtil.broadcast(ChatColor.AQUA + "Map - " + ChatColor.WHITE + getMapName(), ChatUtil.NONE);
					ChatUtil.broadcast(ChatColor.DARK_AQUA.toString() + ChatColor.STRIKETHROUGH + "=====================================", ChatUtil.NONE);
					WorldUtil.createWorld("GameWorld");

				} 
				else if (_lobbyTime == 30) 
				{
					ChatUtil.broadcast(getSelected().getName() + " is starting in " + ChatColor.GREEN +_lobbyTime + " seconds.", ChatUtil.GAME);
					WorldUtil.loadWorld(gametype.toString().toLowerCase(), getMapName());
				}
				_lobbyTime--;
			}
		}, 0L, 1*20);
	}

	public void startGameCountdown() 
	{
		_gameTime = 10;

		gamestate = GameState.IN_PROGRESS;

		_gameTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(_engine, new Runnable()
		{

			@Override
			public void run()
			{
				if (_gameTime <= 0) 
				{
					if (Bukkit.getOnlinePlayers().size() < 2) 
					{
						stop("Not enough players!");
						return;
					}

					for (Player all : Bukkit.getOnlinePlayers()) 
					{
						Title.sendTitle(all, ChatColor.GOLD.toString() + getSelected().getName().toUpperCase(), ChatColor.GREEN + "Has started!");
					}

					getSelected().initalizeGame();
					getSelected().giveItems();

					_pref.gameEnabled = true;

					Bukkit.getScheduler().cancelTask(_gameTask);
	
				} 
				else if (_gameTime == 10) 
				{
					int i = 50;

					while (i < 50) 
					{
						ChatUtil.broadcast("", ChatUtil.NONE);
						i++;
					}

					ChatUtil.broadcast("Game starting in " + ChatColor.GREEN +_gameTime + " seconds.", ChatUtil.GAME);
				} 
				else if (_gameTime <= 10) 
				{
					for (Player all : Bukkit.getOnlinePlayers()) 
					{
						ActionBar.sendActionbar(all, 
								"Starting in" + ActionBar.getProgressbar(" ▌", ChatColor.DARK_AQUA, ChatColor.AQUA, 10, _gameTime / (float) 10));
					}//▐ ▌
				}
				_gameTime--;
			}
		}, 0L, 1*20);
	}

	public void finish() 
	{
		getSelected().unregister();

		for (Player all : Bukkit.getOnlinePlayers())
			removeSpectator(all);

		ChatUtil.broadcast(ChatColor.DARK_AQUA.toString() + ChatColor.STRIKETHROUGH + "=====================================", ChatUtil.NONE);

		for (int i : _positions.keySet())
		{
			if (_positions.size() >= 3) 
			{
				String s = ChatColor.BLUE + _positions.get(i).getName();
				switch (i)
				{
				case 1:
					ChatUtil.broadcast(ChatColor.AQUA.toString() + i + "st: " + s, ChatUtil.NONE);
					break;

				case 2:
					ChatUtil.broadcast(ChatColor.AQUA.toString() + i + "nd: " + s, ChatUtil.NONE);
					break;

				case 3:
					ChatUtil.broadcast(ChatColor.AQUA.toString() + i + "rd: " + s, ChatUtil.NONE);
				}
			} 
			else if (_positions.size() == 2)
			{
				String s = ChatColor.BLUE + _positions.get(i).getName();
				switch (i)
				{
				case 1:
					ChatUtil.broadcast(ChatColor.AQUA.toString() + i + "st: " + s, ChatUtil.NONE);
					break;

				case 2:
					ChatUtil.broadcast(ChatColor.AQUA.toString() + i + "nd: " + s, ChatUtil.NONE);
					break;

				}
			} 
		}
		ChatUtil.broadcast(ChatColor.DARK_AQUA.toString() + ChatColor.STRIKETHROUGH + "=====================================", ChatUtil.NONE);
		for (Player all : Bukkit.getOnlinePlayers())
		{

			Title.sendTitle(all, ChatColor.DARK_AQUA + _positions.get(1).getName(), ChatColor.AQUA + "Won the game!");
			removeSpectator(all);
		}

		gamestate = GameState.ENDING;

		_pref.started = false;
		_pref.gameEnabled = false;

		Bukkit.getScheduler().runTaskLater(_engine, new Runnable()
		{

			@Override
			public void run()
			{
				for (Player all : Bukkit.getOnlinePlayers())
					all.teleport(Bukkit.getWorld("world").getSpawnLocation());
			}
		}, 5*20);

		Bukkit.getScheduler().runTaskLater(_engine, new Runnable()
		{

			@Override
			public void run()
			{
				getAlive().clear();
				getPositions().clear();

				if (_pref.enabledGame) 
				{
					load();
				} 
			}
		}, 10*20);
	}

	public void enableGame(Player player) 
	{
		if (_pref.enabledGame) 
		{

			ChatUtil.message(player, "The game is already enabled!", ChatUtil.GAME);
		}
		else 
		{
			load();
			_pref.enabledGame = true;
			ChatUtil.broadcast(player.getName() + " has" + ChatColor.GREEN + " enabled " + ChatColor.RESET + "the game!", ChatUtil.GAME);
		}
	}

	public void disableGame(Player player) 
	{
		if (!_pref.gameEnabled) 
		{
			ChatUtil.broadcast(player.getName() + " has" + ChatColor.RED + " disabled " + ChatColor.RESET + "the game!", ChatUtil.GAME);
			stop();
			_pref.enabledGame = false;
		} 
		else 
		{
			ChatUtil.message(player, "The game has already started and cannot be disabled.", ChatUtil.GAME);
		}
	}

	public void setWinners(Player player)
	{

		if (getAlive().size() == 3)
		{
			setPos(player, 3);

		} 
		else if (getAlive().size() == 2)
		{
			setPos(player, 2);

			for (Player alive : getAlive())
			{
				if (alive != player)
				{
					setPos(alive, 1);
					finish();
				}
			}
		}
		addSpectator(player);
	}

	public void addSpectator(Player player) 
	{
		getSpectators().add(player);

		ChatUtil.message(player, "You've been made a spectator!", ChatUtil.GAME);

		player.setAllowFlight(true);
		player.setFlying(true);
		player.setHealth(player.getMaxHealth());

		player.getInventory().clear();
		player.getInventory().setItem(0, ItemUtil.createItem(
				Material.COMPASS, 
				1,
				(byte) 0,
				ChatColor.RED + "Spectator Compass",
				Arrays.asList("", ChatColor.AQUA + "Right click to list all alive users!")));

		player.teleport(player.getWorld().getSpawnLocation());

		for (Player all : Bukkit.getOnlinePlayers())
			all.hidePlayer(player);

		removeAlive(player);
	}

	public void removeSpectator(Player player) 
	{
		getSpectators().remove(player);

		player.setAllowFlight(false);
		player.setFlying(false);
		player.setHealth(player.getMaxHealth());

		player.getInventory().clear();

		player.teleport(player.getWorld().getSpawnLocation());

		for (Player all : Bukkit.getOnlinePlayers())
			all.showPlayer(player);
	}

	public void setPos(Player player, int pos)
	{
		_positions.put(pos, player);
	}

	public void addAlive(Player player)
	{
		_alivePlayers.add(player);
	}

	public void removeAlive(Player player)
	{
		_alivePlayers.remove(player);
	}

	public HashMap<Integer, Player> getPositions() 
	{
		return _positions;
	}

	public List<Player> getSpectators() 
	{
		return _spectators;
	}

	public List<Player> getAlive() 
	{
		return _alivePlayers;
	}

	public void wipeAlive()
	{
		_alivePlayers.clear();
	}

	public void wipePos()
	{
		_positions.clear();
	}

	public String getMapName() 
	{
		return _mapName;
	}

	public Game getSelected() 
	{
		return _selected;
	}

	public Game getRandomGame()
	{
		int random = new Random().nextInt(games.size());
		return _selected = games.get(random);
	}

	public GameType getGameType() 
	{
		return gametype;
	}

	public GameState getGameState() 
	{
		return gamestate;
	}

	public String getRandomMap()
	{
		File directory = null;
		try
		{
			directory = new File(this._engine.getDataFolder().getCanonicalPath(), "Games/" + getGameType().toString().toLowerCase());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		List<String> worlds = Lists.newArrayList();

		for (File f : directory.listFiles())
		{
			if (!f.isHidden() && f.isDirectory())
			{
				worlds.add(f.getName());
			}
		}
		return worlds.get((new Random()).nextInt(worlds.size()));
	}
}
