package org.hcraid.com.backpack;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class BackPackCL implements CommandExecutor, Listener {

	@Override
	public boolean onCommand(CommandSender s, Command c, String l,
			String[] args) {
		
		
		if(c.getName().equalsIgnoreCase("backpack")){		
			
			if(s.hasPermission("BackPack.Admin") && args.length > 0){
				
				if(args.length <= 0){
					s.sendMessage(ChatColor.RED + "Specify a player /ob <name>");
					return true;
				}
				
				String name = args[0];
				
				if(Bukkit.getOfflinePlayer(name) != null){
					
					PlayerPack pp = BackPack.getPack(Bukkit.getOfflinePlayer(name).getUniqueId().toString());
					
					Player p = (Player)s;
					
					p.openInventory(pp.createPack());
					
				}else{
					s.sendMessage(ChatColor.RED + "Player not found. /ob <name>");
				}
				
			}
			
			PlayerPack pp = BackPack.getPack((Player)s);
			
			
			Player p = (Player)s;
			
			if(p.hasPermission("BackPack.use")){
			
			if(pp == null){
				pp = new PlayerPack(p.getUniqueId().toString());
			}
			
			if(pp.isInPack()){
				BackPack.logToFile(p.getName() + " tried to open back more than once.");
				return true;
			}
			
			p.openInventory(pp.createPack());
			
			}else{
				p.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			}
		}
		
		return false;
	}
	
	@EventHandler
	public void die(PlayerDeathEvent e){
		
		if(e.getEntity() instanceof Player){
			
			Player p = e.getEntity();
					
			PlayerPack pp = BackPack.getPack(p);
			
			if(pp != null){
				pp.dropBackPack(e.getEntity().getLocation());
			}
		}
		
	}
	
	@EventHandler
	public void closeInventory(InventoryCloseEvent e){
		
		PlayerPack pp = BackPack.getPack((Player)e.getPlayer());		
		
		if(pp != null && pp.isInPack()){
		
			pp.updatePack(e.getInventory());
		
		}else if(pp != null && pp.isInUsersPack()){
			
			pp.getUserPack().updatePack(e.getInventory());
			
		}
		
	}
	
	@EventHandler
	public void logOn(PlayerJoinEvent e){
		
		BackPack.add(Serial.loadPlayer(e.getPlayer().getUniqueId().toString()));
		
	}
	
	@EventHandler
	public void logOff(PlayerQuitEvent e){
		save(e.getPlayer());
	}
	
	@EventHandler
	public void kickOff(PlayerKickEvent e){
		save(e.getPlayer());
	}
	
	public void pickUp(PlayerPickupItemEvent e){
		
		if(e.getItem() != null){
			
			if(inventoryIsFull(e.getPlayer().getInventory())){
				
				if(BackPack.getPack(e.getPlayer()).addItem(e.getItem().getItemStack())){
					e.getItem().remove();
				}
				
			}
			
		}
		
	}

	private boolean inventoryIsFull(PlayerInventory inv) {
		
		for(ItemStack i : inv){
			if(i == null || (i != null && i.getType() == Material.AIR)){
				return false;
			}
		}
		
		return true;
		
	}

	private void save(Player p) {
		
		Serial.savePlayer(BackPack.getPack(p));
		
	}

}
