package me.jakeythedev.mineplextrial.utilities.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.plugin.Plugin;

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
}

