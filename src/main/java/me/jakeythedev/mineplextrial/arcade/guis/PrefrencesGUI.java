package me.jakeythedev.mineplextrial.arcade.guis;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.jakeythedev.mineplextrial.ranks.Ranks;
import me.jakeythedev.mineplextrial.utilities.ItemUtil;
import me.jakeythedev.mineplextrial.utilities.mysql.playerdata.PlayerData;

public class PrefrencesGUI
{

	private Prefrences _prefs;

	public PrefrencesGUI(Prefrences prefrences)
	{
		_prefs = prefrences;
	}

	private Inventory _inv;
	private Inventory _colours;

	public void build(Player player)
	{

		PlayerData pd = PlayerData.players.get(player);

		if (pd.rank.getPerm() >= Ranks.ADMIN.getPerm())
		{
			_inv = Bukkit.createInventory(null, 9 * 3, ChatColor.WHITE + "[" + ChatColor.GREEN + "Admin"
					+ ChatColor.WHITE + "]" + ChatColor.YELLOW.toString() + " Preferences!");

			_inv.setItem(4,
					ItemUtil.createSkull(player.getName(),
							ChatColor.AQUA + player.getName() + ChatColor.GREEN + "'s Preferences Panel",
							Arrays.asList("", ChatColor.AQUA + "Click any of the options to enable and disable.")));

			if (_prefs.started)
			{
				_inv.setItem(13, ItemUtil.createItem(Material.PAPER, 1, (byte) 0,
						ChatColor.GREEN + "Game is already loaded.", Arrays.asList("",
								ChatColor.GRAY + "Whilst a game is loaded no modifications can be processed.")));
			}
			else
			{
				if (_prefs.enabledGame)
				{
					_inv.setItem(12,
							ItemUtil.createItem(Material.REDSTONE_BLOCK, 1, (byte) 0,
									ChatColor.RED + "Disable" + ChatColor.BLUE + " Game",
									Arrays.asList("", ChatColor.RED + " Click to disable")));
				}
				else
				{
					_inv.setItem(12,
							ItemUtil.createItem(Material.EMERALD_BLOCK, 1, (byte) 0,
									ChatColor.GREEN + "Enable" + ChatColor.BLUE + " Game",
									Arrays.asList("", ChatColor.GREEN + " Click to Enable")));
				}

				if (_prefs.canBuild)
				{
					_inv.setItem(14,
							ItemUtil.createItem(Material.REDSTONE, 1, (byte) 0,
									ChatColor.RED + "Disable" + ChatColor.BLUE + " Building",
									Arrays.asList("", ChatColor.RED + " Click to disable")));
				}
				else
				{
					_inv.setItem(14,
							ItemUtil.createItem(Material.EMERALD, 1, (byte) 0,
									ChatColor.GREEN + "Enable" + ChatColor.BLUE + " Building",
									Arrays.asList("", ChatColor.GREEN + " Click to Enable")));
				}
				_inv.setItem(26,
						ItemUtil.createItem(Material.APPLE, 1, (byte) 0,
								ChatColor.GREEN + "Clear" + ChatColor.BLUE + " The Chat",
								Arrays.asList("", ChatColor.GREEN + " Click to Clear")));
			}

			_inv.setItem(17,
					ItemUtil.createItem(Material.BLAZE_ROD, 1, (byte) 0, ChatColor.GREEN + "Change Level preferences",
							Arrays.asList("", ChatColor.GRAY + "Edit your level and level colour!")));

		}
		else
		{

			_inv = Bukkit.createInventory(null, 9 * 3, ChatColor.WHITE + "[" + ChatColor.GREEN + "Player"
					+ ChatColor.WHITE + "]" + ChatColor.YELLOW.toString() + " Preferences!");

			_inv.setItem(13,
					ItemUtil.createItem(Material.BLAZE_ROD, 1, (byte) 0, ChatColor.GREEN + "Change Level preferences",
							Arrays.asList("", ChatColor.GRAY + "Edit your level and level colour!")));

		}

		player.openInventory(_inv);
	}

	public void buildColours(Player player)
	{
		_colours = Bukkit.createInventory(null, 9 * 3, "Your current colour: "
				+ ChatColor.valueOf(PlayerData.players.get(player).colour.toString().toUpperCase()));

		for (ChatColor c : ChatColor.values())
		{

			String character = "" + c.getChar();

			try
			{
				Integer.parseInt(character);
			} catch (Exception e)
			{
				continue;
			}

			String name = "";
			boolean first = true;
			for (char chars : c.toString().toCharArray())
			{
				if (first)
				{
					name = String.valueOf(chars).toUpperCase();
					first = !first;
					continue;
				}

				name += String.valueOf(chars).toLowerCase();
			}

			_colours.addItem(ItemUtil.createItem(Material.STAINED_GLASS, 1, (byte) 2,
					c + ChatColor.getByChar(character).name().toUpperCase(),
					Arrays.asList("", ChatColor.AQUA + "Click to choose this colour!")));
		}

		player.openInventory(_colours);
	}

	public Inventory getInventory()
	{
		return _inv;

	}

	public Inventory getColourInventory()
	{
		return _colours;
	}
}
