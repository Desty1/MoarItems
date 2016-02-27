package com.dansworld.moaritems.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.dansworld.moaritems.item.items.mItem;

/**
 * mItemRegistry is a simply class containing one variable, namely 'moarItems', a Map<String, mItemRegEntry>. The key is the mItem MID.
 * When creating custom items with MoarItems you can get an ItemStack simply with the mItem object using getItemStack().
 * However to create functional mItem (ones with EventHandlers) you must register the mItem in this class.
 * Being that 'moarItems' is a map there can only be one value (mItemRegEntry) to key (MID), so choose unique MID's!
 * Any plugin can unregister, or override your MID if they want. To lock this to only your plugin use true for locked boolean.
 * <p>
 * To register an mItem, and subsequently its listener, use the registerMoarItem() functions.
 * To unregister an mItem, and subsequently its listener, use the unregisterMoarItem() functions.
 * <p>
 * All item registers and unregisters are logged to console automatically, you cannot turn this off.
 * <p>
 * MID : MoarIdentifier, a unique identifying string for each mItem object
 * 
 * @author Daniel
 *
 */

public class mItemRegistry {
	private Map<String, mItemRegEntry> moarItems = new HashMap<String,mItemRegEntry>();
	
	/**	 * Check if the MID is a registered mItem by looking in 'moarItems' key-map
	 * @param MID
	 * @return boolean If the MID is a registered mItem
	 */
	public boolean isRegisteredMoarItem(String MID){
		if(moarItems.containsKey(MID))
			return true;
		return false;
	}
	
	/**
	 * Check if the ItemStack has a hidden MID, then check if that MID is in 'moarItems' key-map
	 * @param item
	 * @return boolean If the ItemStack is a registered mItem
	 */
	public boolean isRegisteredMoarItem(ItemStack item){
		String MID = mItem.getHiddenTag(item);
		if(MID != null && !MID.isEmpty())
			return isRegisteredMoarItem(MID);
		return false;
	}
	
	/**
	 * Grabs the registered mItem object associated with the MID
	 * @param MID
	 * @return mItem If found, null If not found
	 */
	public mItem getRegisteredMoarItem(String MID){
		if(isRegisteredMoarItem(MID))
			return moarItems.get(MID).getItem();
		return null;
	}
	
	/**
	 * Grabs the registered mItem object associated with the ItemStack
	 * Done with a lore tag of MID, hidden by putting '§' between each char
	 * @param item
	 * @return mItem If found, null If not found
	 */
	public mItem getRegisteredMoarItem(ItemStack item){
		if(isRegisteredMoarItem(item))
			return moarItems.get(mItem.getHiddenTag(item)).getItem();
		return null;
	}
	
	/**
	 * Tries to register a new mItem to 'moarItems' if MID isn't taken
	 * Registers listener using plugin If mItems has HandlerLists
	 * Boolean sets wether or not only the registering plugin can unregister it
	 * @param plugin
	 * @param mitem
	 * @return boolean If the mItem should be locked
	 */
	public boolean registerMoarItem(JavaPlugin plugin, mItem mitem, boolean locked){
		String MID = mitem.getMID();
		
		if(!isRegisteredMoarItem(MID)){
			mItemRegEntry entry = new mItemRegEntry(plugin,mitem,locked);
			moarItems.put(MID, entry);
			
			String a = "";
			if(!mitem.getHandlers().isEmpty()){
				plugin.getServer().getPluginManager().registerEvents(mitem, plugin);
				a = "Listener registered!";
			}
			
			registryNotify(plugin,mitem,true,true,"mItem registered successfully! " + a);
			return true;
		}
		registryNotify(plugin,mitem,true,false,"MID is already registered, it must be unique!");
		return false;
	}
	
	/**
	 * Register a list of new mItem to 'moarItems' if their respective MIDs are available
	 * Registers listeners using plugin If mItem has HandlerLists
	 * @param plugin
	 * @param mitems
	 * @return Map<String,Boolean> A map of registry results for each mItem by MID
	 */
	public Map<String, Boolean> registerMoarItems(JavaPlugin plugin, Map<mItem,Boolean> mitems){
		Map<String,Boolean> results = new HashMap<String,Boolean>();
		
		if(mitems.size() > 0){
			for(Entry<mItem,Boolean> e: mitems.entrySet()){
				String MID = e.getKey().getMID();
				boolean result = registerMoarItem(plugin,e.getKey(),e.getValue());
				results.put(MID, result);
			}
		}
		return results;
	}
	
	/**
	 * Unregister an mItem by its MID
	 * Unregisters listener If mItem has HandlerLists
	 * @param plugin The plugin attempting to unregister the mItem, used for lock
	 * @param MID
	 * @return boolean If mItem was unregistered
	 */
	public boolean unregisterMoarItem(JavaPlugin plugin, String MID){
		if(isRegisteredMoarItem(MID)){
			mItemRegEntry entry = moarItems.get(MID);
			if(entry.getDeclaringPlugin() != plugin.getName()){
				registryNotify(plugin,entry.getItem(),false,false,"This mItem is locked by the declaringPlugin!");
				return false;
			}
			
			mItem item = getRegisteredMoarItem(MID);
			String a = "";
			for(HandlerList handler: item.getHandlers()){
				handler.unregister(item);
				a = "Listener unregistered!";
			}
			moarItems.remove(MID);
			registryNotify(plugin,item,false,true,"mItem unregistered! " + a);
			return true;
		}
		return false;
	}
	
	/**
	 * Unregisters a list of mItem by their respective MIDs
	 * Unregisters mItem listener if has HandlersLists 
	 * @param plugin The plugin attempting to unregister the mItem, used for lock
	 * @param mitems List of mItem MIDs
	 * @return Map<String,Boolean> A map of unregister results by MID
	 */
	public Map<String, Boolean> unregisterMoarItems(JavaPlugin plugin, List<String> mitems){
		Map<String,Boolean> results = new HashMap<String,Boolean>();
		
		if(mitems.size() > 0){
			for(String MID: mitems){
				boolean result = unregisterMoarItem(plugin,MID);
				results.put(MID, result);
			}
		}
		return results;
	}
	
	/**
	 * Uses plugin to get ConsoleCommandSender, which is used to send colored messages to console of each respective mItem registry result
	 * @param plugin The JavPlugin used for the registry transaction
	 * @param mitem	The mItem used in the registry transaction
	 * @param result The final result of trying to register, or unregister, an mItem
	 * @param msg An additional string for more information about the transaction
	 */
	private void registryNotify(JavaPlugin plugin, mItem mitem, boolean request, boolean result, String msg){
		ConsoleCommandSender cs = plugin.getServer().getConsoleSender();
		String newLine = System.getProperty("line.separator");
		StringBuilder sb = new StringBuilder();
		
		String res = result ? "ACCEPTED" : "DENIED";
		String req = request ? " registering mItem" : " unregistering mItem";
		
		sb.append(newLine + "§f--------> [mItemRegistry] <--------");
		sb.append(newLine + "§f----> §rREQUEST : " + plugin.getName() + req + " (" + mitem.getMID() + ")");
		sb.append(newLine + "§f----> §rRESULT : " + res);
		sb.append(newLine + "§f----> §rNOTES : " + msg);
		cs.sendMessage(sb.toString());
	}
	
}
