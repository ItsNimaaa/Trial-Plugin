package me.jakeythedev.mineplextrial.arcade;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class Game implements Listener
{
	public Game(String gameName, String[] gameDescription)
	{
		_name = gameName;
		_description = gameDescription;
	}

    private String _name;
    private String[] _description;
    
    public abstract void initalizeGame();
    public abstract void giveItems();
    
    public void unregister() 
    {
    	HandlerList.unregisterAll(this);
    }
    
    public String getName() 
    {
    	return _name;
    }

    public String[] getDescription() 
    {
    	return _description;
    }
}

