package io.github.wolfleader116.wolfspawners.bukkit.completers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;

import io.github.wolfleader116.wolfspawners.bukkit.ConfigOptions;

public class WolfSpawnersTC implements TabCompleter {

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> possible = new ArrayList<String>();
		if (args.length == 1) {
			possible.add("help");
			possible.add("reload");
			possible.add("reset");
			possible.add("spawner");
			if (args[0].equalsIgnoreCase("")) {
				return possible;
			} else {
				List<String> checked = new ArrayList<String>();
				for (String check : possible) {
					if (check.toLowerCase().startsWith(args[0].toLowerCase())) {
						checked.add(check);
					}
				}
				return checked;
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("spawner")) {
				for (EntityType et : ConfigOptions.allow) {
					possible.add(ConfigOptions.getName(et));
				}
				if (args[1].equalsIgnoreCase("")) {
					return possible;
				} else {
					List<String> checked = new ArrayList<String>();
					for (String check : possible) {
						if (check.toLowerCase().startsWith(args[1].toLowerCase())) {
							checked.add(check);
						}
					}
					return checked;
				}
			}
		}
		return null;
	}

}
