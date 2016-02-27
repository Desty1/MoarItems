package com.dansworld.moaritems.item;

import org.bukkit.plugin.java.JavaPlugin;

import com.dansworld.moaritems.item.items.mItem;
/**
 * The mItemRegEntry is used to store an mItem, String and boolean all together for the mItemRegistry.moarItems
 * String is the name of the plugin that registered the mItem
 * Boolean is if mItem is locked, only allowing declaringPlugin to unregister mItem
 * @author Daniel
 *
 */
public class mItemRegEntry {
	private String declaringPlugin;
	private mItem mitem;
	private boolean locked;
	
	public mItemRegEntry(JavaPlugin declaringPlugin, mItem mitem, boolean locked){
		this.declaringPlugin = declaringPlugin.getName();
		this.mitem = mitem;
		this.locked = locked;
	}
	
	public String getDeclaringPlugin(){
		return declaringPlugin;
	}
	
	public mItem getItem(){
		return mitem;
	}
	
	public boolean getLocked(){
		return locked;
	}
}
