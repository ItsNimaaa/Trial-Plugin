package me.jakeythedev.mineplextrial.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.server.ServerListPingEvent;

import me.jakeythedev.mineplextrial.Engine;
import me.jakeythedev.mineplextrial.arcade.Arcade;
import me.jakeythedev.mineplextrial.arcade.GameState;
import me.jakeythedev.mineplextrial.arcade.guis.Prefrences;
import me.jakeythedev.mineplextrial.ranks.Ranks;
import me.jakeythedev.mineplextrial.utilities.mysql.playerdata.PlayerData;

public class GlobalListeners implements Listener
{
	private Engine _engine;
	private Arcade _arcade;
	private Prefrences _prefs;

	public GlobalListeners(Engine engine, Arcade arcade, Prefrences prefrences)
	{
		_engine = engine;
		_arcade = arcade;
		_prefs = prefrences;
	}

	@EventHandler
	public void onWhitelist(ServerListPingEvent e) 
	{
		e.setMotd(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + _engine.getConfig().getString("ServerName") + ChatColor.AQUA + " JakeyTheDev's Mineplex Trial Server");
	}

	@EventHandler
	public void onFood(FoodLevelChangeEvent e) 
	{
		e.setCancelled(true);
		e.setFoodLevel(Integer.MAX_VALUE);
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onItemMove(InventoryInteractEvent e) 
	{
		if (e.getInventory().getType() == InventoryType.PLAYER) 
		{
			e.setResult(Result.ALLOW);
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) 
	{
		if (e.getEntity() instanceof Player) 
		{
			if (e.getCause() == DamageCause.FALL || e.getCause() == DamageCause.CUSTOM)  
			{
				e.setCancelled(true);
			} 
			else if (e.getCause() == DamageCause.FALLING_BLOCK) 
			{
				e.setDamage(3.0);
			}
			else if (e.getCause() == DamageCause.VOID && _arcade.getGameState() != GameState.IN_PROGRESS) 
			{
				e.getEntity().teleport(e.getEntity().getWorld().getSpawnLocation());
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onCrouch(PlayerToggleSneakEvent e) 
	{
		Player player = e.getPlayer();
		
		if (player.getName().equalsIgnoreCase("chiss")) 
		{
			player.playSound(player.getLocation(), Sound.CAT_MEOW, (float) 10.0, (float) 1.0);
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent e) 
	{
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) 
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlaceBlock(BlockPlaceEvent e) 
	{

		if (_arcade.getGameState() != GameState.IN_PROGRESS) 
		{
			if (!_prefs.canBuild) 
			{
				e.setCancelled(true);
			}
			else 
			{
				e.setBuild(true);
			}
		}
	}

	@EventHandler
	public void onBreakBlock(BlockBreakEvent e) 
	{

		if (_arcade.getGameState() != GameState.IN_PROGRESS) 
		{
			if (!_prefs.canBuild) 
			{
				e.setCancelled(true);
			}
			else 
			{
				e.setCancelled(false);
			}
		}
	}

	@EventHandler
	public void onTalk(AsyncPlayerChatEvent e)
	{
		Player player = e.getPlayer();
		PlayerData playerData = PlayerData.players.get(player);

		e.setFormat(ChatColor.valueOf(playerData.colour) + "" + playerData.level + " " + 
				Ranks.getPrefix(true, true, playerData.rank) + " " + ChatColor.YELLOW + player.getName() + " " + ChatColor.RESET + e.getMessage());
	}
}