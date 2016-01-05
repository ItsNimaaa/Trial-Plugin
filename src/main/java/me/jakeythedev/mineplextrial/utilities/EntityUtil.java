package me.jakeythedev.mineplextrial.utilities;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;

import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class EntityUtil
{

	public static void setNoAI(Entity ent)
	{
		net.minecraft.server.v1_8_R3.Entity nmsEnt = ((CraftEntity) ent).getHandle();
		NBTTagCompound tag = nmsEnt.getNBTTag();
		if (tag == null)
		{
			tag = new NBTTagCompound();
		}
		nmsEnt.c(tag);
		tag.setInt("NoAI", 1);
		nmsEnt.f(tag);
	}
}
