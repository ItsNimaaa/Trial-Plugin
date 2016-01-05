package me.jakeythedev.mineplextrial.utilities.packets;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class ActionBar
{
	/*
	 * / ALLOWS YOU TO SEND ACTION BAR MESSAGES.
	 */

	public static void sendActionbar(Player player, String arguments)
	{
		IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + arguments + "\"}");

		PacketPlayOutChat bar = new PacketPlayOutChat(icbc, (byte) 2);

		((CraftPlayer) player).getHandle().playerConnection.sendPacket(bar);
	}
	
    public static String getProgressbar(String symbol, ChatColor beginColour, ChatColor endColour, float amount, float percent)
    {
        String bar = ""; 
        // ♥ █ ▐

        for (int x = 0; x < amount; x++)
        {
            if ((float) (x / amount) < (float) percent)
            {
                bar = bar + beginColour + ChatColor.BOLD + symbol;
            } 
            else
            {
                bar = bar + endColour + ChatColor.BOLD + symbol;
            }
        }

        bar = bar + ChatColor.RESET;

        return bar;
    }
}
