package com.cospox.choruschaos;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class LavaHandler {
	public static void dispenseLava(World w) {
    	for (int[] offset: Rooms.lava_rooms) {
    		Location l = new Location(w,
    				Game.CORNER.getX() + offset[0],
    				Game.CORNER.getY() + offset[1],
    				Game.CORNER.getZ() + offset[2]);
    		
    		Block b = l.getBlock();
    		if (b.isBlockPowered()) {
    			Location lavaloc = new Location(w,
    					Game.CORNER.getX() + offset[0],
    					Game.CORNER.getY() + offset[1] - 1,
    					Game.CORNER.getZ() + offset[2]);
    			Block lava = lavaloc.getBlock();
    			lava.setType(Material.LAVA);
    			
    		} else {
    			Location lavaloc = new Location(w,
    					Game.CORNER.getX() + offset[0],
    					Game.CORNER.getY() + offset[1] - 1,
    					Game.CORNER.getZ() + offset[2]);
    			Block lava = lavaloc.getBlock();
    			lava.setType(Material.AIR);
    		}
    	}
    }
}
