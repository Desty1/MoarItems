package com.dansworld.moaritems.item.items;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
/**
 * The base of all mItems, you can extend this class but I'd recommend mItem instead for its ItemStack functions.
 * Contains all basic MoarItem data (MID,type,handlerlists)
 * @author Daniel
 *
 */
public abstract class mItemBase implements Listener{
	private String MID;
	private mItemType type;
	
	private List<HandlerList> handlerlists = new ArrayList<HandlerList>();
	
	public mItemBase(String MID, mItemType type){
		this.MID = MID;
		this.type = type;
	}
	
	public String getMID(){
		return MID;
	}
	
	public mItemType getType(){
		return type;
	}
	
	public List<HandlerList> getHandlers(){
		return handlerlists;
	}
	
	
	public void addHandler(HandlerList handler){
		handlerlists.add(handler);
	}
	
	public enum mItemType{
		CURRENCY,
		CONSUMABLE,
		TOOL,
		WEAPON,
		ARMOR,
		TRANSPORTATION,
		TRASH;	
	}
}
