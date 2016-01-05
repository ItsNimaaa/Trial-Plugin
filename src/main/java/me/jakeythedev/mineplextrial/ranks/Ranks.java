package me.jakeythedev.mineplextrial.ranks;

import org.bukkit.ChatColor;

public enum Ranks
{

	/*/
	 *  I got "JNR" & "SNR" from the old JR.DEV trial chat, apparently you guys used it so I thought
	 *  it was better to use your formatting.
	 */

	PLAYER("Member", ChatColor.GREEN, 0),
	
	ULTRA("Ultra", ChatColor.AQUA, 0),
	HERO("Hero", ChatColor.DARK_PURPLE, 0),
	LEGEND("Legend", ChatColor.GREEN, 0),
	TITAN("Titan", ChatColor.RED, 0),
	
	YT("YT", ChatColor.DARK_PURPLE, 0),
	TWITCH("Twitch", ChatColor.DARK_PURPLE, 0),
	YOUTUBER("YouTube", ChatColor.RED, 0),
	
	TRAINEE("Trainee", ChatColor.DARK_AQUA, 1),
	MOD("Mod", ChatColor.GOLD, 2),
	SNR_MOD("Sr.Mod", ChatColor.GOLD, 3),
	ADMIN("Admin", ChatColor.GOLD, 4),
	JNR_DEV("Jr.Dev", ChatColor.GOLD, 5),
	DEV("Dev", ChatColor.GOLD, 6),
	
	LEADER("Leader", ChatColor.GOLD, 7),
	OWNER("Owner", ChatColor.GOLD, 8);
	
	private String _name;
	private ChatColor _colour;
	private int _perm;
	
	private Ranks(String name, ChatColor colour, int perm)
	{
		this._name = name;
		this._colour = colour;
		this._perm = perm;
	}
	
	public String getName() 
	{
		return _name;
	}

	public ChatColor getColour() 
	{
		return _colour;
	}
	
	public int getPerm() 
	{
		return _perm;
	}
	
	public static String getPrefix(boolean isBold, boolean isUppercase, Ranks rank) 
	{
		if (isUppercase && isBold) 
		{
			return rank.getColour().toString() + ChatColor.BOLD + rank.getName().toUpperCase();

		} 
		else if (isBold) 
		{
			return rank.getColour().toString() + ChatColor.BOLD + rank.getName();
			
		} 
		else if (isUppercase) 
		{
			return rank.getColour().toString() + rank.getName().toUpperCase();
		}
		
		return rank.getColour().toString() + rank.getName();
	}
}
