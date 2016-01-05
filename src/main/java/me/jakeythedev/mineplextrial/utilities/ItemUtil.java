package me.jakeythedev.mineplextrial.utilities;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemUtil
{

	public static ItemStack createItem(Material material, int amount, byte byteData, String name, List<String> lore)
	{
		ItemStack item = new ItemStack(material, amount, byteData);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack createEnchantItem(Material material, int amount, byte byteData, Enchantment enchant, int enchantLevel, String name, List<String> lore)
	{
		ItemStack item = new ItemStack(material, amount, byteData);
		ItemMeta meta = item.getItemMeta();
		meta.addEnchant(enchant, enchantLevel, true);
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack createUnbreakableItem(Material material, int amount, byte byteData, String name, List<String> lore)
	{
		ItemStack item = new ItemStack(material, amount, byteData);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		meta.spigot().setUnbreakable(true);
		item.setItemMeta(meta);
		
		return item;
	}

	public static ItemStack createSkull(String owner, String name, List<String> lore)
	{
		ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		meta.setOwner(owner);
		item.setItemMeta(meta);
		return item;
		
	}
}
