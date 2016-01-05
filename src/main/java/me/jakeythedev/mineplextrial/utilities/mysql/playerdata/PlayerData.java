package me.jakeythedev.mineplextrial.utilities.mysql.playerdata;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.jakeythedev.mineplextrial.Engine;
import me.jakeythedev.mineplextrial.ranks.Ranks;
import me.jakeythedev.mineplextrial.utilities.mysql.MySql;

public class PlayerData
{
	public static HashMap<Player, PlayerData> players = new HashMap<Player, PlayerData>();

	private Engine _engine;

	protected String _name, _uuid;
	public Ranks rank = Ranks.PLAYER;
	public String colour = ChatColor.GRAY.name().toUpperCase();
	public int level = 0, gems;

	public PlayerData(Engine engine)
	{
		_engine = engine;
	}

	public PlayerData(Engine engine, final Player player)
	{
		_engine = engine;
		_name = player.getName();
		_uuid = player.getUniqueId().toString();

		Bukkit.getScheduler().runTaskAsynchronously(_engine, new Runnable()
		{
			public void run()
			{

				ResultSet set = _engine.sql.query("SELECT * FROM `" + "MPTRIAL" + "` WHERE UUID = '" + player.getUniqueId().toString() + "';");

				try
				{
					if (set.next())
					{

						PlayerData.this._uuid = set.getString("UUID");
						PlayerData.this._name = set.getString("NAME");
						
						PlayerData.this.rank = Ranks.valueOf(set.getString("RANK"));
						
						PlayerData.this.gems = set.getInt("GEMS");
						PlayerData.this.level = set.getInt("LEVEL");
						PlayerData.this.colour = set.getString("COLOUR");
						
						
					}
					else
					{

						PlayerData.this._name = player.getName();
						PlayerData.this._uuid = player.getUniqueId().toString();
						
						PlayerData.this.rank = Ranks.PLAYER;
						
						PlayerData.this.gems = 50;
						PlayerData.this.level = 0;
						PlayerData.this.colour = ChatColor.GRAY.name().toUpperCase();

					}
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public void saveData(Player player)
	{

		final String s = player.getUniqueId().toString();
		final String s1 = player.getName();

		ResultSet set = _engine.sql.query("SELECT * FROM `" + "MPTRIAL" + "` WHERE UUID = '" + s + "';");

		try
		{
			if (set.next())
			{

				MySql.update("UPDATE `" + "MPTRIAL" + "` SET `NAME` = '" + s1 + 
						"', `RANK` = '" + this.rank + "', `GEMS` = '" + this.gems + 
						"', `COLOUR` = '" + this.colour + "', `LEVEL` = '" + this.level + "' WHERE `UUID` = '" + s + "';");

			}
			else
			{

				MySql.update("INSERT INTO `" + "MPTRIAL" + "` (`UUID`, `NAME`, `RANK`, `GEMS`, `COLOUR`, `LEVEL`) "
						+ "VALUES "
						+ "('" + s + "', '" + s1 + "', '" + this.rank + "', '" + this.gems + "', '" + this.colour + "', '" + this.level + "');");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void saveDataAsync(final Player player)
	{
		Bukkit.getScheduler().runTaskAsynchronously(_engine, new Runnable()
		{
			@Override
			public void run()
			{
				saveData(player);
			}
		});
	}
	
	public static void setRank(Player player, Ranks rank) 
	{
		if (players.containsKey(player)) 
			players.get(player).rank = rank;
		
	}
}