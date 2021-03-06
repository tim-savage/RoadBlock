package com.winterhaven_mc.roadblock.highlights;

import com.winterhaven_mc.roadblock.PluginMain;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;


final class ShowHighlightTask extends BukkitRunnable {

	private final PluginMain plugin;
	private final Player player;
	private final Collection<Location> locationSet;
	private final Material material;

	ShowHighlightTask(final PluginMain plugin,
					  final Player player,
					  final Collection<Location> locationSet,
					  final Material material) {

		this.plugin = plugin;
		this.player = player;
		this.locationSet = locationSet;
		this.material = material;
	}


	@Override
	public void run() {

		// highlight blocks
		plugin.highlightManager.showHighlight(player, locationSet, material);

		// create task to unhighlight locationSet in 30 seconds
		final BukkitTask task = new RemoveHighlightTask(plugin, player).runTaskLaterAsynchronously(plugin, 30 * 20L);

		// if pending remove highlight task exists, cancel and replace with this task
		final BukkitTask previousTask = plugin.highlightManager.getPendingRemoveTask(player);

		if (previousTask != null) {
			previousTask.cancel();
		}

		// put taskId in pending remove map
		plugin.highlightManager.setPendingRemoveTask(player, task);
	}

}
