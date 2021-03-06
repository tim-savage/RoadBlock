package com.winterhaven_mc.roadblock.commands;

import java.util.*;


public class SubcommandMap {

	SortedMap<String, Subcommand> subcommandMap = new TreeMap<>();
	Map<String, String> aliasMap = new HashMap<>();


	/**
	 * Register a subcommand in the map by name.
	 * @param subcommand an instance of the command
	 */
	void register(final Subcommand subcommand) {

		String name = subcommand.getName();

		subcommandMap.put(name.toLowerCase(), subcommand);

		subcommand.getAliases();

		for (String alias : subcommand.getAliases()) {
			aliasMap.put(alias.toLowerCase(), name.toLowerCase());
		}
	}


	/**
	 * Get command instance from map by name
	 * @param name the command to retrieve from the map
	 * @return Subcommand - the subcommand instance, or null if no matching name
	 */
	Subcommand getCommand(final String name) {

		String key = name;

		if (aliasMap.containsKey(key)) {
			key = aliasMap.get(key);
		}

		return (subcommandMap.get(key));
	}


	/**
	 * Get list of keys (subcommand names) from the subcommand map
	 * @return List of String - keys of the subcommand map
	 */
	List<String> getKeys() {
		return new ArrayList<>(subcommandMap.keySet());
	}
}
