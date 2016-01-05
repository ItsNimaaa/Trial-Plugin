package me.jakeythedev.mineplextrial.arcade.guis;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class Prefrences
{

	public boolean enabledGame = false;
	public boolean isLoaded = false;
	public boolean canBuild = false;

	public boolean gameEnabled = false;
	public boolean started = false;

	public HashMap<Player, Boolean> inLevelChoose = new HashMap<>();
}
