package me.jakeythedev.mineplextrial.arcade.guis;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.jakeythedev.mineplextrial.arcade.Arcade;
import me.jakeythedev.mineplextrial.utilities.ItemUtil;

public class SpectatorGUI
{
	private Arcade _arcade;

	public SpectatorGUI(Arcade arcade)
	{
		_arcade = arcade;
	}

	private Inventory _inv;

	public void build(Player player)
	{
		_inv = Bukkit.createInventory(null, 9 * 3, ChatColor.GREEN.toString() + ChatColor.BOLD + "Choose a spectator!");

		for (Player alive : _arcade.getAlive())
		{
			_inv.addItem(ItemUtil.createSkull(alive.getName(), ChatColor.GREEN + alive.getName(),
					Arrays.asList("", "Click to teleport!")));
		}

		player.openInventory(_inv);
	}

	public Inventory getInventory()
	{
		return _inv;

	}
}
