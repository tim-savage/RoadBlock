package com.winterhaven_mc.roadblock.utilities;

import com.winterhaven_mc.roadblock.PluginMain;

import com.winterhaven_mc.util.LanguageManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;


public final class RoadBlockTool {

	private final static PluginMain plugin = JavaPlugin.getPlugin(PluginMain.class);

	private final static NamespacedKey PERSISTENT_KEY = new NamespacedKey(plugin, "isTool");

	public final static Set<Material> toolTransparentMaterials =
			Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
					Material.AIR,
					Material.SNOW,
					Material.TALL_GRASS
			)));


	/**
	 * Private constructor to prevent instantiation of this utility class
	 */
	private RoadBlockTool() {
		throw new AssertionError();
	}


	/**
	 * Create an item stack with configured tool material, name and lore
	 *
	 * @return RoadBlock tool item stack
	 */
	public static ItemStack create() {

		// initialize material
		Material material = null;

		// get configured material
		String materialString = plugin.getConfig().getString("tool-material");
		if (materialString != null) {
			material = Material.matchMaterial(materialString);
		}

		// if no matching material found, use default GOLDEN_PICKAXE
		if (material == null) {
			material = Material.GOLDEN_PICKAXE;
		}

		// create item stack of configured tool material
		final ItemStack itemStack = new ItemStack(material);

		// get item stack metadata
		final ItemMeta metaData = itemStack.getItemMeta();

		// get language manager instance
		LanguageManager languageManager = LanguageManager.getInstance();

		// set display name to configured tool name
		assert metaData != null;
		metaData.setDisplayName(languageManager.getItemName());

		// set lore to configured tool lore
		metaData.setLore(languageManager.getItemLore());

		// hide item stack attributes and enchants
		metaData.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		metaData.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		metaData.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

		// set persistent data in item metadata
		metaData.getPersistentDataContainer().set(PERSISTENT_KEY, PersistentDataType.BYTE, (byte) 1);

		// set item stack metadata
		itemStack.setItemMeta(metaData);

		// return item stack
		return itemStack;
	}


	/**
	 * Test if an item stack is a RoadBlock tool
	 *
	 * @param itemStack the ItemStack to be tested
	 * @return true if item is a RoadBlock tool, false if it is not
	 */
	public static boolean isTool(final ItemStack itemStack) {

		// if item stack is null, return false
		if (itemStack == null) {
			return false;
		}

		// if item stack does not have meta data, return false
		if (!itemStack.hasItemMeta()) {
			return false;
		}

		// if item stack does not have persistent data tag, return false
		//noinspection RedundantIfStatement
		if (!Objects.requireNonNull(itemStack.getItemMeta())
				.getPersistentDataContainer().has(PERSISTENT_KEY, PersistentDataType.BYTE)) {
			return false;
		}

		// return true
		return true;
	}

}
