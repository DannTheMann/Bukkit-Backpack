package org.hcraid.com.backpack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class Serial {
	
	public static PlayerPack loadPlayer(String id){
		
	      try
	      {
	    	  if(!new File(BackPack.directory + id + ".ser").exists())
	    		  return new PlayerPack(id);
	    	  
	         FileInputStream fileIn = new FileInputStream(BackPack.directory + id + ".ser");
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         PlayerPack data = (PlayerPack) in.readObject();
	         in.close();
	         fileIn.close();
	         BackPack.log("Succesfully loaded '" + id + "' file.");
	         
	         if(data == null){
	        	 return new PlayerPack(id);
	         }
	         
	         return data;
	      }catch(IOException i){
	         i.printStackTrace();
	         return null;
	      }catch(ClassNotFoundException c){
	    	  BackPack.log("Failed to load '" + id + "'.");
	    	 c.printStackTrace();
	         return null;
	      }
		
	}
	
	public static void savePlayer(PlayerPack pp){
	      try
	      	{
	         FileOutputStream fileOut =
	         new FileOutputStream(BackPack.directory + pp.getId() + ".ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(pp);
	         out.close();
	         fileOut.close();
	         BackPack.log("Succesfully saved 'RaidData' file.");
	      }catch(IOException i){
	    	  BackPack.log("An error occured while trying to serialize the object 'RaidData'.");
	          i.printStackTrace();
	      	}	
	}

}
