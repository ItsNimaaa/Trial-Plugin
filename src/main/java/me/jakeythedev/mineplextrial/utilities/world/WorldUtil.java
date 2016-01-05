package me.jakeythedev.mineplextrial.utilities.world;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import me.jakeythedev.mineplextrial.Engine;

public class WorldUtil
{

	public String GameWorld = "GameWorld";
	public World LobbyWorld = Bukkit.getWorld("world");

	private static Engine _engine;

	public WorldUtil(Engine Engine)
	{
		_engine = Engine;
	}

	public static void loadWorld(String game, String mapName)
	{
		try
		{
			_loadMap(mapName, game, false);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static String _loadMap(String mapName, String game, boolean fallback) throws IOException
	{
		_engine.getLogger().info("Copying template to game world.");
		File templateWorld = new File(_engine.getDataFolder().getCanonicalPath(),
				"Games/" + game + "/" + mapName + "/");
		if (!templateWorld.exists() || !templateWorld.isDirectory())
		{
			_engine.getLogger().severe("Template world is not a directory or does not exist.");
			return null;
		}

		String gameWorldName = "GameWorld";
		File gameWorld = new File(gameWorldName);
		if (gameWorld.exists())
		{
			try
			{
				FileUtils.deleteDirectory(gameWorld);
				if (gameWorld.exists() && gameWorld.isDirectory())
				{
					throw new IOException("gameWorld directory was not deleted.");
				}
			} catch (IOException e)
			{
				_engine.getLogger().log(Level.SEVERE, "Unable to delete game world directory", e);
				if (fallback)
				{
					_engine.getLogger().warning("Failed fallback maps load: stopping");
					return null;
				} 
				else
				{
					return _loadMap(mapName, game, true);
				}
			}
		}

		try
		{
			FileUtils.copyDirectory(templateWorld, gameWorld);
		} catch (IOException e)
		{
			_engine.getLogger().log(Level.SEVERE, "Unable to copy template to game world", e);
			return null;
		}

		_engine.getLogger().info("Finished copying template to game world.");
		return gameWorldName;
	}

	public static void deleteWorld(boolean beforeMap)
	{
		final World gameWorld = Bukkit.getWorld("GameWorld");
		
		if (gameWorld != null)
		{
			if (Bukkit.unloadWorld(gameWorld, false))
			{
				_engine.getLogger().info(gameWorld.getName() + " successfully unloaded.");
				
				if (!beforeMap)
					
					_engine.getServer().getScheduler().runTaskLaterAsynchronously(_engine, new Runnable()
					{
						private int _tiries = 0;

						@Override
						public void run()
						{
							try
							{
								FileUtils.deleteDirectory(new File(gameWorld.getName()));
								_engine.getLogger().info("World folder deleted and gone!");
							} catch (IOException e)
							{
								_engine.getLogger().log(Level.SEVERE, "Unable to delete world directory (try: " + _tiries + ")", e);
								if (_tiries < 2)
								{
									_tiries++;
									_engine.getServer().getScheduler().runTaskLaterAsynchronously(_engine, this, (_tiries + 1) * 20L);
								}
							}
						}
					}, 5L);
			} 
			else
			{
				_engine.getLogger().severe("Unable to unload world " + gameWorld.getName());
			}
		}

	}

	public static void createWorld(String worldName)
	{
		World world = Bukkit.getWorld(worldName);
		if (world == null)
		{
			WorldCreator creator = new WorldCreator(worldName);
			creator.environment(World.Environment.NORMAL);
			creator.generateStructures(false);
			world = creator.createWorld();
			world.setAutoSave(false);
			world.setTime(0);
			world.setStorm(false);
			world.setWeatherDuration(9999);

			_engine.getLogger().info("Gameworld generated and prefrences set.");
		}
	}
}
