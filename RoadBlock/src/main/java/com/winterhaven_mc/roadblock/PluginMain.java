package com.winterhaven_mc.roadblock;

import com.tchristofferson.configupdater.ConfigUpdater;
import com.winterhaven_mc.roadblock.commands.CommandManager;
import com.winterhaven_mc.roadblock.highlights.HighlightManager;
import com.winterhaven_mc.roadblock.listeners.EventListener;
import com.winterhaven_mc.roadblock.storage.BlockManager;

import com.winterhaven_mc.util.*;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;


public final class PluginMain extends JavaPlugin {

	public BlockManager blockManager;
	public HighlightManager highlightManager;
	public SoundConfiguration soundConfig;
	public WorldManager worldManager;

	public Boolean debug = getConfig().getBoolean("debug");
	public Boolean profile = getConfig().getBoolean("profile");


	@Override
	public void onEnable() {

		// install default config.yml if not present
		saveDefaultConfig();
		//The config needs to exist before using the updater
		File configFile = new File(getDataFolder(), "config.yml");

		try {
			ConfigUpdater.update(this, "config.yml", configFile, Arrays.asList("materials", "disabled-worlds", "enabled-worlds"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		reloadConfig();

		// initialize language manager
		LanguageManager.init();

		// instantiate world manager
		worldManager = new WorldManager(this);

		// instantiate sound configuration
		soundConfig = new YamlSoundConfiguration(this);

		// instantiate block manager
		blockManager = new BlockManager(this);

		// instantiate highlight manager
		highlightManager = new HighlightManager(this);

		// instantiate command manager
		new CommandManager(this);

		// instantiate event listener
		new EventListener(this);
	}


	@Override
	public void onDisable() {

		// close datastore
		blockManager.close();
	}

}
