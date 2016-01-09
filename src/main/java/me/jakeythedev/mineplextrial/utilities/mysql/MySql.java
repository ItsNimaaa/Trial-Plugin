package me.jakeythedev.mineplextrial.utilities.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import me.jakeythedev.mineplextrial.Engine;

public class MySql extends Database
{

	private static String _user;
	private static String _database;
	private static String _password;
	private static String _port;
	private static String _hostname;

	private static Engine _engine;

	public MySql(Engine engine, String hostname, String port, String database, String username, String password)
	{
		super(engine);
		MySql._engine = engine;
		MySql._hostname = hostname;
		MySql._port = port;
		MySql._database = database;
		MySql._user = username;
		MySql._password = password;

		connect(hostname, port, database, username, password);
	}

	public static boolean connect(String host, String port, String db, String user, String pass)
	{
		try
		{
			openConnection();
			return checkConnection();
		} catch (Exception e)
		{
			e.printStackTrace();
			plugin.getLogger().log(Level.SEVERE, "Could not connect to DB");
			return false;
		}
	}

	public static void createTable(final String tableName, final String vars)
	{
		Bukkit.getScheduler().runTaskAsynchronously(_engine, new Runnable()
		{
			public void run()
			{
				try
				{
					updateDB("CREATE TABLE IF NOT EXISTS " + tableName + "(" + vars + ");");
				} catch (ClassNotFoundException | SQLException e)
				{
					e.printStackTrace();
				}
			}
		});

	}

	public static void update(final String update)
	{

		try
		{
			updateDB(update);
		} catch (SQLException e)
		{
			e.printStackTrace();
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}

	}

	public ResultSet query(String query)
	{
		try
		{
			return queryDB(query);
		} catch (SQLException e)
		{
			e.printStackTrace();
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static Connection openConnection() throws SQLException, ClassNotFoundException
	{
		if (checkConnection())
		{
			return connection;
		}
		connection = DriverManager.getConnection("jdbc:mysql://" + _hostname + ":" + _port + "/" + _database, _user, _password);
		return connection;
	}
}
