package com.winterhaven_mc.roadblock.commands;

import com.winterhaven_mc.roadblock.PluginMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import javax.annotation.Nonnull;
import java.util.*;

import static com.winterhaven_mc.roadblock.messages.MessageId.COMMAND_FAIL_INVALID_COMMAND;
import static com.winterhaven_mc.roadblock.sounds.SoundId.COMMAND_INVALID;


public final class CommandManager implements CommandExecutor, TabCompleter {

	// reference to main class instance
	private final PluginMain plugin;

	// instantiate subcommand map
	private final SubcommandMap subcommandMap = new SubcommandMap();


	/**
	 * Class constructor
	 *
	 * @param plugin reference to main class
	 */
	public CommandManager(final PluginMain plugin) {

		// reference to main class
		this.plugin = plugin;

		// register this class as command executor
		Objects.requireNonNull(plugin.getCommand("roadblock")).setExecutor(this);

		// register subcommands
		for (SubcommandType subcommandType : SubcommandType.values()) {
			subcommandType.register(plugin, subcommandMap);
		}
	}


	/**
	 * Tab completer for RoadBlock commands
	 */
	@Override
	public List<String> onTabComplete(final @Nonnull CommandSender sender,
									  final @Nonnull Command command,
									  final @Nonnull String alias,
									  final String[] args) {

		// if more than one argument, use tab completer of subcommand
		if (args.length > 1) {

			// get subcommand from map
			Subcommand subcommand = subcommandMap.getCommand(args[0]);

			// if no subcommand returned from map, return empty list
			if (subcommand == null) {
				return Collections.emptyList();
			}

			// return subcommand tab completer output
			return subcommand.onTabComplete(sender, command, alias, args);
		}

		// return list of subcommands for which sender has permission
		return matchingCommands(sender, args[0]);

	}


	@Override
	public boolean onCommand(final @Nonnull CommandSender sender,
	                         final @Nonnull Command command,
	                         final @Nonnull String label,
	                         final String[] args) {

		// convert args array to list
		List<String> argsList = new ArrayList<>(Arrays.asList(args));

		String subcommandName;

		// get subcommand, remove from front of list
		if (argsList.size() > 0) {
			subcommandName = argsList.remove(0);
		}

		// if no arguments, set command to help
		else {
			subcommandName = "help";
		}

		// get subcommand from map by name
		Subcommand subcommand = subcommandMap.getCommand(subcommandName);

		// if subcommand is null, get help command from map
		if (subcommand == null) {
			subcommand = subcommandMap.getCommand("help");
			plugin.messageBuilder.build(sender, COMMAND_FAIL_INVALID_COMMAND).send(plugin.languageHandler);
			plugin.soundConfig.playSound(sender, COMMAND_INVALID);
		}

		// execute subcommand
		return subcommand.onCommand(sender, argsList);
	}


	/**
	 * Get matching list of subcommands for which sender has permission
	 * @param sender the command sender
	 * @param matchString the string prefix to match against command names
	 * @return List of String - command names that match prefix and sender has permission
	 */
	private List<String> matchingCommands(CommandSender sender, String matchString) {

		List<String> returnList = new ArrayList<>();

		for (String subcommand : subcommandMap.getKeys()) {
			if (sender.hasPermission("roadblock." + subcommand)
					&& subcommand.startsWith(matchString.toLowerCase())) {
				returnList.add(subcommand);
			}
		}
		return returnList;
	}

}
