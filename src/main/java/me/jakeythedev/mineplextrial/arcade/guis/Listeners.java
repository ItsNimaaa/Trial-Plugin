package me.jakeythedev.mineplextrial.arcade.guis;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.jakeythedev.mineplextrial.arcade.Arcade;
import me.jakeythedev.mineplextrial.arcade.GameState;
import me.jakeythedev.mineplextrial.utilities.ChatUtil;
import me.jakeythedev.mineplextrial.utilities.mysql.playerdata.PlayerData;

public class Listeners implements Listener
{
	private SpectatorGUI _spec;
	private PrefrencesGUI _prefsGUI;
	private Prefrences _prefs;
	private Arcade _arcade;

	public Listeners(Arcade arcade, SpectatorGUI specs, Prefrences prefrences, PrefrencesGUI prefGUI)
	{
		_arcade = arcade;
		_spec = specs;
		_prefs = prefrences;
		_prefsGUI = prefGUI;
	}

	private GameState _gamestate;

	@EventHandler
	public void onCompass(PlayerInteractEvent e)
	{
		Player player = e.getPlayer();
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if (e.getItem().getType() == Material.COMPASS && e.getItem() != null)
			{
				_spec.build(player);
			}
			else
			{
				return;
			}
		}
	}

	@EventHandler
	public void onCompassInventoryClick(InventoryClickEvent e)
	{
		Player player = (Player) e.getWhoClicked();

		ItemStack item = e.getCurrentItem();

		if (e.getInventory() == null)
			return;

		if (e.getInventory() != null || e.getInventory().getName().equals(_spec.getInventory().getName()))
		{
			if (e.getInventory().getType() == InventoryType.PLAYER)
				return;

			if (item != null && item.getType() != null && item.getType() != Material.AIR)
			{
				if (_arcade.getGameState() == GameState.IN_PROGRESS)
				{

					Player target = Bukkit.getPlayer(ChatColor.stripColor(item.getItemMeta().getDisplayName()));

					ChatColor.stripColor(item.getItemMeta().getDisplayName());

					e.setCancelled(true);

					if (item.getType() == Material.SKULL_ITEM)
					{
						player.teleport(target);
						player.closeInventory();
					}
				}
			}
		}
	}

	@EventHandler
	public void onAdminInventoryClick(InventoryClickEvent e)
	{
		Player player = (Player) e.getWhoClicked();

		ItemStack item = e.getCurrentItem();

		if (e.getInventory() == null)
			return;

		if (e.getInventory() != null || e.getInventory().getName().equals(_prefsGUI.getInventory().getName()))
		{
			if (item != null && item.getType() != null && item.getType() != Material.AIR)
			{

				e.setCancelled(true);
				switch (item.getType())
				{
				/*
				 * / PLAYER
				 */

				case BLAZE_ROD:
					_prefs.inLevelChoose.put(player, true);
					ChatUtil.message(player, "What level would you like to be? ", ChatUtil.GAME);
					player.closeInventory();
					break;

				/*
				 * / STAFF
				 */

				case EMERALD:
					_prefs.canBuild = true;
					ChatUtil.message(player, "Building enabled!", ChatUtil.GAME);
					player.closeInventory();
					break;
				case REDSTONE:
					_prefs.canBuild = false;
					ChatUtil.message(player, "Building disabled!", ChatUtil.GAME);
					player.closeInventory();
					break;
				case EMERALD_BLOCK:
					_arcade.enableGame(player);
					player.closeInventory();
					break;
				case REDSTONE_BLOCK:
					_arcade.disableGame(player);
					player.closeInventory();
					break;
				case APPLE:

					for (int i = 0; i < 50; i++)
					{
						ChatUtil.broadcast(" ", ChatUtil.NONE);
					}

					ChatUtil.broadcast(player.getName() + " cleared the chat.", ChatUtil.ANNOUNCE);
					player.closeInventory();
					break;
				default:
					break;
				}
			}
		}
	}

	@EventHandler
	public void onTalk(AsyncPlayerChatEvent e)
	{
		Player player = e.getPlayer();

		if (_prefs.inLevelChoose.containsKey(player))
		{
			e.setCancelled(true);

			if (_prefs.inLevelChoose.get(player) == true)
			{
				try
				{
					int level = Integer.parseInt(e.getMessage());

					if (level > 100 || level < 0)
					{
						ChatUtil.message(player,
								"You have been removed from the level choosing system due to an error (levels cannot be over 100 or below 0!).",
								ChatUtil.GAME);
						_prefs.inLevelChoose.remove(player);
						return;
					}

					PlayerData.players.get(player).level = level;

					_prefsGUI.buildColours(player);
					_prefs.inLevelChoose.remove(player);
				} catch (Exception exe)
				{
					ChatUtil.message(player, "Not an Integer.", ChatUtil.GAME);
				}
			}
		}
	}

	@EventHandler
	public void onColourInventoryClick(InventoryClickEvent e)
	{
		Player player = (Player) e.getWhoClicked();

		ItemStack item = e.getCurrentItem();

		if (e.getInventory() == null)
			return;

		if (e.getInventory() != null || e.getInventory().getName().equals(_prefsGUI.getColourInventory().getName()))
		{
			if (item != null && item.getType() != null && item.getType() != Material.AIR)
			{

				e.setCancelled(true);

				if (item.getType() == Material.STAINED_GLASS)
				{
					PlayerData.players.get(player).colour = ChatColor.stripColor(item.getItemMeta().getDisplayName());
					ChatUtil.message(player,
							"You have selected the colour, " + item.getItemMeta().getDisplayName() + "!",
							ChatUtil.GAME);

					player.closeInventory();
				}
			}
		}
	}
}
