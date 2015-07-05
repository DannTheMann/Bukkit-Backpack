package org.hcraid.com.backpack;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerPack implements Serializable{
	
	private static final long serialVersionUID = 1912138686740317014L;
	private HashMap<Integer, SavedItem> items = new HashMap<Integer, SavedItem>();
	private String id;
	private transient boolean inPack;
	private transient PlayerPack inUsersPack;
	private int size;
	
	public PlayerPack(String id){
		this.id = id; 
	}

	public String getId() {
		return id;
	}
	
	public Inventory createPack(){
		
		inPack = true;
		
		Player op = Bukkit.getPlayer(UUID.fromString(id));
		
		size = getPlayerPackSize(op);
		
		Inventory inv = Bukkit.createInventory(null, size, ChatColor.DARK_RED + op.getName());
		
		for(Integer i : items.keySet()){
			if(inv.getSize() > i){
				inv.setItem(i, items.get(i).toBukkitItemStack());
			}
		}
		
		return inv;
		
	}
	
	public void dropBackPack(Location loc){
		
		for(SavedItem si : items.values()){
			loc.getWorld().dropItem(loc, si.toBukkitItemStack());
		}
		
		items.clear();
		
	}
	
	public boolean addItem(ItemStack is){
		Inventory inv = createPack();
		
		for(SavedItem si : items.values()){
			if(si.getItemType() == is.getType() && (si.getAmount() + is.getAmount() <= 64)){
				si.setAmount(si.getAmount() + is.getAmount());
				return true;
			}
		}
		
		for(int i = 0; i < inv.getSize(); i++){
			if(inv.getItem(i) == null){
				items.put(i, new SavedItem(is));
				return true;
			}
		}
		
		return false;
	}
	
	public void updatePack(Inventory inv){
		
		inPack = false;
		
		items.clear();
		
		for(int i = 0; i < inv.getSize(); i++){
			if(inv.getItem(i) != null){
				items.put(i, new SavedItem(inv.getItem(i)));
			}
		}
		
	}

	private int getPlayerPackSize(Player op) {
		
		if(op.hasPermission("BackPack.Exterminator")){
			return BackPack.getSize("exterminator");
		}else if(op.hasPermission("BackPack.Hunter")){
			return BackPack.getSize("hunter");
		}else if(op.hasPermission("BackPack.Tracker")){
			return BackPack.getSize("tracker");
		}else{
			return BackPack.getSize("default");
		}
	}

	public boolean isInPack() {
		return inPack;
	}

	public boolean isInUsersPack() {
		return inUsersPack != null;
	}

	public PlayerPack getUserPack() {
		return inUsersPack;
	}
}
