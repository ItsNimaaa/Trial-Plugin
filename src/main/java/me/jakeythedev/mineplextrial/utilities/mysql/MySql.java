package me.jakeythedev.mineplextrial.utilities.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

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

abstract class Database
{
	public static Connection connection;
	protected static Plugin plugin;

	protected Database(Plugin plugin)
	{
		Database.plugin = plugin;
		Database.connection = null;
	}

	public static Connection openConnection() throws SQLException, ClassNotFoundException
	{
		return null;
	}

	public static boolean checkConnection() throws SQLException
	{
		return (Database.connection != null) && (!Database.connection.isClosed());
	}

	public Connection getConnection()
	{
		return Database.connection;
	}

	public static boolean closeConnection() throws SQLException
	{
		if (Database.connection == null)
		{
			return false;
		}
		Database.connection.close();
		return true;
	}

	protected static ResultSet queryDB(String query) throws SQLException, ClassNotFoundException
	{
		if (!checkConnection())
		{
			openConnection();
		}
		ResultSet result = Database.connection.prepareStatement(query).executeQuery();
		return result;
	}

	protected static PreparedStatement updateDB(String update) throws SQLException, ClassNotFoundException
	{
		if (!checkConnection())
		{
			openConnection();
		}
		
		PreparedStatement statement = connection.prepareStatement(update);
		return statement;
	}

	public static Object getValue(UUID uuid, String key)
	{
		try
		{
			if (!checkConnection())
			{
				openConnection();
			}
			PreparedStatement sql = connection.prepareStatement("SELECT `" + key + "` FROM `" + "MPTRIAL" + "` WHERE UUID=?;");
			sql.setString(1, uuid.toString());
			ResultSet resultSet = sql.executeQuery();
			boolean exists = resultSet.next();

			if (exists == true)
			{
				return resultSet.getObject(key);
			}

			sql.close();
			resultSet.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public static void setValue(UUID uuid, String key, Object value, int i)
	{
		try
		{
			PreparedStatement updateData = connection.prepareStatement("UPDATE `" + "MPTRIAL" + "` SET " + key + "=? WHERE UUID=?;");
			updateData.setObject(1, value);
			updateData.setString(2, uuid.toString());
			updateData.executeUpdate();

			updateData.close();

		} catch (Exception e)
		{ }
	}

	public static boolean accountExists(UUID uuid, String string)
	{
		try
		{
			if (!checkConnection())
			{
				openConnection();
			}
			
			PreparedStatement sql = Database.connection.prepareStatement("SELECT * FROM `" + "MPTRIAL" + "` WHERE UUID=?;");
			sql.setString(1, uuid.toString());
			ResultSet resultSet = sql.executeQuery();
			boolean containsPlayer = resultSet.next();

			sql.close();
			resultSet.close();

			return containsPlayer;

		} catch (Exception e)
		{
			e.printStackTrace();

			return false;
		}
	}
}