package org.hcraid.com.backpack;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BackPack extends JavaPlugin{
	
	public static String PREFIX = ChatColor.GOLD + "[" + ChatColor.YELLOW + "BackPack" + ChatColor.GOLD + "] " 
			+ ChatColor.GRAY;
	
	public static HashMap<String, Integer> rankSlots = new HashMap<String, Integer>();
	private static HashMap<String, PlayerPack> packs = new HashMap<String, PlayerPack>();
	
	public static String directory;
	public static String logDir;
	
	public static BackPack m;
	
	public void onEnable(){
		m = this;
		loadConfig();
	
		directory = getDataFolder().getAbsolutePath() + File.separator + "Players" + File.separator;
		
		File f = new File(directory);
		
		if(!f.exists()){
			f.mkdirs();
		}
		
		for(Player p : Bukkit.getOnlinePlayers()){
			
			packs.put(p.getUniqueId().toString(), Serial.loadPlayer(p.getUniqueId().toString()));
			
		}
		
		getServer().getPluginCommand("backpack").setExecutor(new BackPackCL());
		getServer().getPluginManager().registerEvents(new BackPackCL(), this);
		
		
		logDir = getDataFolder().getAbsolutePath() + File.separator + "Logs" + File.separator;
		
		 f = new File(logDir);
		
		if(!f.exists()){
			f.mkdir();
		}
	}
	
	public void loadConfig(){
		getConfig().addDefault("BackPack.Default", 0);
		getConfig().addDefault("BackPack.Tracker", 18);
		getConfig().addDefault("BackPack.Hunter", 27);
		getConfig().addDefault("BackPack.Executioner", 36);
		
		getConfig().options().copyDefaults(true);
		
		saveConfig();
		
		rankSlots.put("default", getConfig().getInt("BackPack.Default"));
		rankSlots.put("tracker", getConfig().getInt("BackPack.Tracker"));
		rankSlots.put("hunter", getConfig().getInt("BackPack.Hunter"));
		rankSlots.put("exterminator", getConfig().getInt("BackPack.Executioner"));
	}
	
	public void onDisable(){
		
		for(PlayerPack pp : packs.values()){
			Serial.savePlayer(pp);
		}
		
	}
	
	public static void log(String msg){
		System.out.println("[BackPack] " + msg);
	}
	
	public static int getSize(String id){

		return rankSlots.get(id);
		
	}
	
	public static PlayerPack getPack(Player p){
		PlayerPack pp = packs.get(p.getUniqueId().toString());
		
		if(pp == null){
			Serial.loadPlayer(p.getUniqueId().toString());
		}else{
			return pp;
		}
		return pp;
		
	}
	
	public static PlayerPack getPack(String id){
		PlayerPack pp = packs.get(id);
		
		if(pp == null){
			Serial.loadPlayer(id);
		}else{
			return pp;
		}
		return pp;
	}

	public static void add(PlayerPack pp) {
		packs.put(pp.getId(), pp);
	}
	
	public static void logToFile(String string) {
		
		final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String date = sdf.format(new Date(System.currentTimeMillis()));
		
		File f = new File(logDir + date + ".txt");

		final SimpleDateFormat sdf3 = new SimpleDateFormat("h:mm a");
		String prefix = sdf3.format(new Date(System.currentTimeMillis()));
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
			bw.write(prefix + " === " + string);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			log("Failed to write out the item achieved from the case.");
			e.printStackTrace();
		}
		
	}

}
