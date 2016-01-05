package me.jakeythedev.mineplextrial.arcade.games;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.jakeythedev.mineplextrial.Engine;
import me.jakeythedev.mineplextrial.arcade.Arcade;
import me.jakeythedev.mineplextrial.arcade.Game;

public class Runner extends Game
{
	private Engine _engine;
	private Arcade _arcade;

	public Runner(Engine engine, Arcade arcade)
	{
		super("Runner", new String[] 
				{ 
						"Welcome to Runner!",
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
		//NONE NEEDED
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) 
	{

		if (e.getEntity() instanceof Player) 
		{
			Player player = (Player) e.getEntity();


			if (_arcade.getSpectators().contains(player))
				e.setCancelled(true);


			if (e.getCause() == DamageCause.VOID) 
			{
				e.setCancelled(true);
				_arcade.setWinners(player);
			}
		}
	}
	
	@EventHandler
	public void move(final PlayerMoveEvent e)
	{

		Block[] blocks = new Block[9];

		blocks[0] = e.getPlayer().getLocation().subtract(0, 1, 0).getBlock();
		blocks[1] = e.getPlayer().getLocation().subtract(0, 1, -0.75).getBlock();
		blocks[2] = e.getPlayer().getLocation().subtract(0, 1, 0.75).getBlock();
		blocks[3] = e.getPlayer().getLocation().subtract(0.75, 1, 0).getBlock();
		blocks[4] = e.getPlayer().getLocation().subtract(-0.75, 1, 0).getBlock();
		blocks[5] = e.getPlayer().getLocation().subtract(-0.75, 1, -0.75).getBlock();
		blocks[6] = e.getPlayer().getLocation().subtract(0.75, 1, 0.75).getBlock();
		blocks[7] = e.getPlayer().getLocation().subtract(-0.75, 1, 0.75).getBlock();
		blocks[8] = e.getPlayer().getLocation().subtract(0.75, 1, -0.75).getBlock();

		for (final Block block : blocks)
		{
			if (block.getType() != Material.AIR)
			{
				if (e.getPlayer().isOnGround())
				{
					if (block.getType() != Material.AIR)
					{
						if (!(block.getType().equals(Material.STAINED_CLAY) && (block != null)))
							block.setType(Material.STAINED_CLAY);
						block.setData((byte) new Random().nextInt(8));

						new BukkitRunnable()
						{
							@Override
							public void run()
							{

								block.setType(Material.AIR);

								final FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(
										block.getLocation().add(0, -1, 0),
										Material.STAINED_CLAY, 
										(byte) 14);

								Bukkit.getScheduler().runTaskLater(_engine, new Runnable()
								{

									@Override
									public void run()
									{
										fallingBlock.remove();

									}
								}, 2 * 20L);
							}
						}.runTaskLater(_engine, 10L);
					}
				}
			}
		}
	}

	@EventHandler
	public void blockChange(EntityChangeBlockEvent e)
	{
		if (e.getEntityType() == EntityType.FALLING_BLOCK)
		{
			FallingBlock fallingBlock = (FallingBlock) e.getEntity();

			if (fallingBlock.getMaterial() == Material.STAINED_CLAY)
			{
				e.setCancelled(true);
				fallingBlock.remove();
			}
		}
	}
}	
