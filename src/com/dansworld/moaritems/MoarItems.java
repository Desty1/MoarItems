package com.dansworld.moaritems;

import org.bukkit.plugin.java.JavaPlugin;

import com.dansworld.moaritems.command.mItemsCommand;
import com.dansworld.moaritems.item.mItemRegistry;
/**
 * MoarItems is an API for creating custom items of all sorts. 
 * You do this by creating objects that extend mItem, or one of its subclasses, then registering the object in mItemRegistry. 
 * The mItemRegistry is used so many plugins can use MoarItems all at once.
 * All mItem objects require a unique MID, or MoarItemIdentifier. Plugins access their own mItem objects and others mItems using this
 * MID in the mItemRegistry. This way nobody is stepping on eachothers toes!
 * 
 * The most basic type of MoarItem is the mItem. It contains all the data needed to make an ItemStack (itemmeta, type, amount, flags, ect.) as
 * well as any HandlerLists used to Listen to events. Subclasses can be used to make coding easier, they'll contain functions specific to
 * certain item functionality. Like mInteractiveItem has onLeftClick(), onRightClick(), onMiddleClick(), onDropClick().
 * Alternatively you can create your own subclasses of mItem or mItemBase and use addHandler() with your own eventhandlers instead!
 * 
 * @author Daniel
 *
 */

public class MoarItems extends JavaPlugin {
	private static mItemRegistry itemRegistry = new mItemRegistry();
	
	public void onEnable(){
		getCommand("moaritems").setExecutor(new mItemsCommand());
	}
	
	public static mItemRegistry getItemRegistry(){
		return itemRegistry;
	}
}
