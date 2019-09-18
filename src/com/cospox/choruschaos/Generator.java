package com.cospox.choruschaos;

import java.util.SplittableRandom;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public class Generator {
	private static Material flowers[] =
		{Material.ALLIUM,       Material.AZURE_BLUET,
		 Material.BLUE_ORCHID,  Material.CORNFLOWER,
		 Material.DANDELION,    Material.LILY_OF_THE_VALLEY,
		 Material.OXEYE_DAISY,  Material.POPPY,
		 Material.ORANGE_TULIP, Material.WITHER_ROSE};
	
	private static Material tulips[] = 
		{Material.ORANGE_TULIP, Material.PINK_TULIP,
		 Material.RED_TULIP,    Material.WHITE_TULIP};
	
	public static final int ENDERPEARL_CHANCE = 10;
	public static final int STRING_CHANCE     = 20;
	public static final int MUSHROOM_CHANCE   = 15;
	public static final int BOWL_CHANCE       = 5;
	public static final int FLOWER_CHANCE     = 10;
	
	 public static void generate(World w) {	
	    //chorus fruit
    	int[] room = Rooms.all_rooms[new SplittableRandom().nextInt(0, Rooms.all_rooms.length)];
		Location l = new Location(w,
				Game.CORNER.getX() + room[0],
				Game.CORNER.getY() + room[1] + 1,
				Game.CORNER.getZ() + room[2]);
		w.dropItemNaturally(l, new ItemStack(Material.CHORUS_FRUIT));
		
		if (new SplittableRandom().nextInt(0, (int)(1.0 / STRING_CHANCE * 100 - 1)) == 0) {
			room = Rooms.string_rooms[new SplittableRandom().nextInt(0, Rooms.string_rooms.length)];
			l = new Location(w,
					Game.CORNER.getX() + room[0],
					Game.CORNER.getY() + room[1] + 1,
					Game.CORNER.getZ() + room[2]);
			w.dropItemNaturally(l, new ItemStack(Material.STRING));
		}
		
		if (new SplittableRandom().nextInt(0, (int)(1.0 / MUSHROOM_CHANCE * 100 - 1)) == 0) {
			room = Rooms.mushroom_rooms[new SplittableRandom().nextInt(0, Rooms.mushroom_rooms.length)];
			l = new Location(w,
					Game.CORNER.getX() + room[0],
					Game.CORNER.getY() + room[1] + 1,
					Game.CORNER.getZ() + room[2]);
			w.dropItemNaturally(l, new ItemStack(Material.RED_MUSHROOM));
		}
		
		if (new SplittableRandom().nextInt(0, (int)(1.0 / MUSHROOM_CHANCE * 100 - 1)) == 0) {
			room = Rooms.mushroom_rooms[new SplittableRandom().nextInt(0, Rooms.mushroom_rooms.length)];
			l = new Location(w,
					Game.CORNER.getX() + room[0],
					Game.CORNER.getY() + room[1] + 1,
					Game.CORNER.getZ() + room[2]);
			w.dropItemNaturally(l, new ItemStack(Material.BROWN_MUSHROOM));
		}
		
		if (new SplittableRandom().nextInt(0, (int)(1.0 / BOWL_CHANCE * 100 - 1)) == 0) {
			room = Rooms.bowl_rooms[new SplittableRandom().nextInt(0, Rooms.bowl_rooms.length)];
			l = new Location(w,
					Game.CORNER.getX() + room[0],
					Game.CORNER.getY() + room[1] + 1,
					Game.CORNER.getZ() + room[2]);
			w.dropItemNaturally(l, new ItemStack(Material.BOWL));
		}
		
		if (new SplittableRandom().nextInt(0, (int)(1.0 / ENDERPEARL_CHANCE * 100 - 1)) == 0) {
			room = Rooms.pearl_rooms[new SplittableRandom().nextInt(0, Rooms.pearl_rooms.length)];
			l = new Location(w,
					Game.CORNER.getX() + room[0],
					Game.CORNER.getY() + room[1] + 1,
					Game.CORNER.getZ() + room[2]);
			w.dropItemNaturally(l, new ItemStack(Material.ENDER_PEARL));
		}
		
    	
		if (new SplittableRandom().nextInt(0, (int)(1.0 / FLOWER_CHANCE * 100 - 1)) == 0) {
			room = Rooms.flower_rooms[new SplittableRandom().nextInt(0, Rooms.flower_rooms.length - 1)];
			l = new Location(w,
					Game.CORNER.getX() + room[0],
					Game.CORNER.getY() + room[1] + 1,
					Game.CORNER.getZ() + room[2]);
			w.dropItemNaturally(l, new ItemStack(generateRandomFlower()));
		}
    }
 
    public static Material generateRandomFlower() {
    	Material flower = flowers[new SplittableRandom().nextInt(0, flowers.length - 1)];
    	if (flower == Material.ORANGE_TULIP) {
    		flower = tulips[new SplittableRandom().nextInt(0, tulips.length - 1)];
    	}
    	return flower;
    }
}
