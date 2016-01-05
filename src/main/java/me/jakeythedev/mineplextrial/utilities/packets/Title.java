package me.jakeythedev.mineplextrial.utilities.packets;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;

public class Title
{
	/*
	 * / ALLOWS YOU TO SEND TITLE MESSAGES.
	 */

	public static void sendTitle(Player player, String title, String subTitle)
	{
		IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
		IChatBaseComponent icbc2 = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subTitle + "\"}");

		PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, icbc);
		PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, icbc2);
		PacketPlayOutTitle timingPackage = new PacketPlayOutTitle(10, 50, 10);

		((CraftPlayer) player).getHandle().playerConnection.sendPacket(titlePacket);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(subtitlePacket);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(timingPackage);
	}
}
