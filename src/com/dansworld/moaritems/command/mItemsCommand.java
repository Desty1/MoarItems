package com.dansworld.moaritems.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.dansworld.moaritems.MoarItems;
/**
 * subcommands: list, unregister, give
 * perms: moaritems.list, moaritems.unregister, moaritems.give
 * @author Daniel
 *
 */
public class mItemsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player))
			return false;
		Player p = (Player)sender;
		
		return true;
	}

}
