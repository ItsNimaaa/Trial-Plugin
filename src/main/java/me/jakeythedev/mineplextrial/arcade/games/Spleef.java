package me.jakeythedev.mineplextrial.arcade.games;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.jakeythedev.mineplextrial.Engine;
import me.jakeythedev.mineplextrial.arcade.Arcade;
import me.jakeythedev.mineplextrial.arcade.Game;
import me.jakeythedev.mineplextrial.utilities.ItemUtil;

public class Spleef extends Game
{
	private Engine _engine;
	private Arcade _arcade;

	public Spleef(Engine engine, Arcade arcade)
	{
		super("Spleef", new String[] 
				{ 
						"Welcome to Spleef!",
						"To win all players must fall to their death.",
						"Last person standing wins!" 
				});
		_engine = engine;
		_arcade = arcade;
	}

	@Override
	public void initalizeGame()
	{
		Bukkit.getPluginManager().registerEvents(this, _engine);

	}

	@Override
	public void giveItems()
	{
		for (Player all : Bukkit.getOnlinePlayers())
			all.getInventory().addItem(ItemUtil.createUnbreakableItem(
					Material.IRON_SPADE, 
					1, 	
					(byte) 0,
					ChatColor.GREEN + "Spade", 
					Arrays.asList(ChatColor.GREEN + "Mining snow gives you " + ChatColor.LIGHT_PURPLE + "💕" + ChatColor.GREEN + " hearts.")));

	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) 
	{

		if (e.getEntity() instanceof Player) 
		{
			Player player = (Player) e.getEntity();


			if (_arcade.getSpectators().contains(player))
				return;


			if (e.getCause() == DamageCause.VOID) 
			{
				e.setCancelled(true);
				_arcade.setWinners(player);
			}
		}
	}

	@EventHandler
	public void onMine(BlockDamageEvent e) 
	{
		Player player = e.getPlayer();

		if (_arcade.getSpectators().contains(player))
			return;

		if (player.getItemInHand().getType() == Material.IRON_SPADE) 
		{
			e.setInstaBreak(true);
			e.getBlock().getDrops().clear();
		}
	}
}
