package com.dansworld.moaritems.item.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import com.google.common.base.CaseFormat;
/**
 * The most basic mItem. 
 * Contains all basic ItemStack data (amount,data,type,ect.) and functions essential to making an ItemStack.
 * A tag is generated from the MID of mItemBase and placed in the ItemStack lore to identify it
 * Lastly there is a boolean check to see if a given ItemStack is this mItem
 * @author Daniel
 *
 */
public class mItem extends mItemBase{
	private Material material;
	private int amount = 1;
	private MaterialData data = null;
	private short durability = 0;
	private String displayName;
	private List<String> lore = new ArrayList<String>();
	private List<ItemFlag> flags = new ArrayList<ItemFlag>();
	private Map<Enchantment,Integer> enchantments = new HashMap<Enchantment,Integer>();
	
	public mItem(String MID, Material material , mItemType type){
		super(MID,type);
		this.material = material;
		setDisplayName(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, MID));
		addLore(getTag());
	}
	
	public Material getMaterial(){
		return material;
	}
	
	public int getAmount(){
		return amount;
	}
	
	public MaterialData getData(){
		return data;
	}
	
	public short getDurability(){
		return durability;
	}
	
	public String getDisplayName(){
		return displayName;
	}
	
	public List<String> getLore(){
		return lore;
	}
	
	public List<ItemFlag> getFlags(){
		return flags;
	}
	
	public boolean hasFlag(ItemFlag flag){
		return flags.contains(flag);
	}
	
	public Map<Enchantment, Integer> getEnchantments(){
		return enchantments;
	}

	/**
	 * Get the encoded MID to place in ItemStack lore
	 * @return tag A string made by placing '§' between each char of mItem MID, and '§m' at the start
	 */
	public String getTag(){
		String tag = "§m";
		for(char c: getMID().toCharArray()){
			tag = tag + "§" + c;
		}
		return tag;
	}
	
	/**
	 * Check the ItemStack lore for the this mItem tag
	 * @param item
	 * @return MID Decoded tag (MID) or empty if not found
	 */
	public static String getHiddenTag(ItemStack item){
		if(item == null)
			return "";
		
		ItemMeta im = item.getItemMeta();
		List<String> lore = im.getLore();
		String MID = "";
		if(lore != null && lore.size() > 0){
			for(String line: lore){
				if(line.startsWith("§m")){
					MID = line.replaceAll("§", "").replaceFirst("m", "");
				}
			}
		}
		return MID;
	}
	
	/**
	 * Instantiates an ItemStack Object from this mItem.
	 * @return ItemStack
	 */
	public ItemStack getItemStack(){
		ItemStack item = new ItemStack(getMaterial(),getAmount(),getDurability());
		MaterialData data = getData();
		if(data != null)
			item.setData(data);
		
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(getDisplayName());
		addLore("§6" + getType());
		im.setLore(getLore());	
		
		List<ItemFlag> flags = getFlags();
		if(flags.size() > 0){
			ItemFlag[] f = flags.toArray(new ItemFlag[flags.size() -1]);
			im.addItemFlags(f);
		}	
		
		for(Entry<Enchantment, Integer> entry: getEnchantments().entrySet()){
			im.addEnchant(entry.getKey(), entry.getValue(), true);
		}
		item.setItemMeta(im);
		
		return item;
	}
	
	/**
	 * Check if the ItemStack in question has this mItem tag, making it equivalent to this mItem
	 * @param item
	 * @return boolean is the same as this mItem
	 */
	public boolean isItem(ItemStack item){
		String tag = getHiddenTag(item);
		if(tag != null && !tag.isEmpty() && tag.equalsIgnoreCase(getMID())){
			return true;
		}
		return false;
	}
	
	public void setAmount(int amount){
		this.amount = amount;
	}
	
	public void setDisplayName(String displayName){
		this.displayName = displayName;
	}
	
	public void setMeterialData(MaterialData data){
		this.data = data;
	}
	
	public void setDurability(short durability){
		this.durability = durability;
	}
	
	public void addLore(String lore){
		this.lore.add(lore);
	}
	
	public void addLore(int line, String lore){
		this.lore.add(line, lore);
	}
	
	public void addFlag(ItemFlag flag){
		if(!hasFlag(flag))
			flags.add(flag);
	}
	
	public void removeFlag(ItemFlag flag){
		if(hasFlag(flag))
			flags.remove(flag);
	}
	
	public void addEnchantment(Enchantment enchantment, int level){
		enchantments.put(enchantment, level);
	}

}
