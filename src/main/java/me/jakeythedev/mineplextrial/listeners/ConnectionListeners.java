package me.jakeythedev.mineplextrial.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.jakeythedev.mineplextrial.Engine;
import me.jakeythedev.mineplextrial.arcade.Arcade;
import me.jakeythedev.mineplextrial.arcade.GameState;
import me.jakeythedev.mineplextrial.arcade.guis.Prefrences;
import me.jakeythedev.mineplextrial.ranks.Ranks;
import me.jakeythedev.mineplextrial.utilities.ChatUtil;
import me.jakeythedev.mineplextrial.utilities.mysql.playerdata.PlayerData;

public class ConnectionListeners implements Listener
{
	
	private Engine _engine;
	private Arcade _arcade;
	private Prefrences _prefs;
	
	public ConnectionListeners(Engine engine, Arcade arcade, Prefrences prefrences)
	{
		_engine = engine;
		_arcade = arcade;
		_prefs = prefrences;
	}

	@EventHandler
	public void onConnect(PlayerJoinEvent e) 
	{
		
		final Player player = e.getPlayer();
		
		e.setJoinMessage(ChatUtil.JOIN.getColour() + ChatUtil.JOIN.getPrefix() + ChatColor.WHITE + "> "+ player.getName());
		
		PlayerData.players.put(player, new PlayerData(_engine, player));
		player.getInventory().clear();
		
		switch (_arcade.getGameState()) 
		{
		
		case NOT_READY:
			player.teleport(Bukkit.getWorld("world").getSpawnLocation());
			
			if (Bukkit.getOnlinePlayers().size() == 2) 
			{
				if (_prefs.gameEnabled) 
				{
					_arcade.load();
				} 
			}
			break;
		case COUNTDOWN:
			_arcade.addSpectator(player);
			player.teleport(Bukkit.getWorld("GameWorld").getSpawnLocation());
			break;
		case IN_PROGRESS:
			_arcade.addSpectator(player);
			break;
		case ENDING:
			break;
		}
	}

	@EventHandler
	public void onDisconnect(PlayerQuitEvent e) 
	{
		Player player = e.getPlayer();
		
		e.setQuitMessage(ChatUtil.LEAVE.getColour() + ChatUtil.LEAVE.getPrefix() + ChatColor.WHITE + "> "+ player.getName());
		
		PlayerData.players.get(player).saveData(player);
		player.getInventory().clear();
		
		switch (_arcade.getGameState()) 
		{
		case NOT_READY:
			if (Bukkit.getOnlinePlayers().size() <= 1 && _arcade.getGameState() == GameState.IN_PROGRESS) 
			{
				_arcade.stop("Not enough players.");
			}
			break;
		case COUNTDOWN:
			break;
		case IN_PROGRESS:
			
			_arcade.removeAlive(player);
			
			if (Bukkit.getOnlinePlayers().size() <= 1 && _arcade.getGameState() == GameState.IN_PROGRESS) 
			{
				_arcade.setWinners(player);
			}
			break;
		case ENDING:
			break;
		}
	}
} 
