//TODO
//Lobby, teleported there on join, also remove items on join.
//Quit to hubworld on game finish
//Join from hubworld
//IDK something about integrating Multiverse, error if it doesn't exist
//config for name of hubworld
//MAKE IT IDIOTPROOF
//Make sure player is healed/gm adventured before game starts
//Test with multiple people
//Handle multiple games at once?
//Doesn't work if joining again IDK


package com.cospox.choruschaos;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.SplittableRandom;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class Game {
	private  boolean  gameIsRunning = false;
	private  int      countingDown  = 60;
	
	public static Location CORNER;
	
	private HashMap<String, PlayerData> playerData = new HashMap<String, PlayerData>();
	private HashMap<String, int[]> rooms = new HashMap<String, int[]>();
	
	private World w;
	private Server s;
	
	private Location lobbySpawnPoint;
	
	public Game(World w, Server s, Main plugin) {
		this.w = w;
		this.s = s;
    	this.rooms.put("yellow", new int[]{12, 13, 2});
    	this.rooms.put("blue", new int[]{2, 13, 7});
    	this.rooms.put("light blue", new int[]{7, 13, 2});
    	this.rooms.put("magenta", new int[]{2, 13, 12});
    	this.rooms.put("pink", new int[]{7, 13, 12});
    	this.rooms.put("orange", new int[]{2, 13, 2});
    	
    	CORNER = new Location(this.w, 81, 102, 96);
    	
    	double[] loc = (double[])plugin.getConfig().get("world.lobbyspawn");
    	this.lobbySpawnPoint = new Location(this.w, loc[0], loc[1], loc[2]);
	}
	
	public void start(CommandSender sender) {
		this.gameIsRunning = true;
    	this.countingDown = -1;
    	sender.sendMessage("Game is now running!");
	}
	
	public void reset() {
		//what should this method do??
		for (Entity e: this.w.getEntities()) {
			if (e.getType() == EntityType.DROPPED_ITEM) {
				e.remove();
			}
		}
    	//this.s.dispatchCommand(this.s.getConsoleSender(), "minecraft:kill @e[type=!minecraft:player]");
		
		for (Player p: this.w.getPlayers()) {
			if (p.getGameMode() == GameMode.ADVENTURE) {
				p.getInventory().clear();
			}
		}
		
    	//this.s.dispatchCommand(this.s.getConsoleSender(), "minecraft:clear @a[gamemode=adventure]");
    	this.s.dispatchCommand(this.s.getConsoleSender(), "minecraft:gamemode adventure @a[gamemode=!spectator]");
    	
    	//Set health/food?
    	
    	this.countingDown = -1; //IDK
    }
	
	public void countDown() { //HELP
		if (this.gameIsRunning) { return; }
		
    	if (this.countingDown < 0) { return; }
    	if (this.s.getOnlinePlayers().size() > 0) {
    		countingDown -= 1;
    		if (countingDown < 0) {
    			this.gameIsRunning = true;
    			this.assignTeams();
    			Player p = this.s.getPlayer("KingJellyfishII");
    			p.sendTitle("GO!", null, 10, 70, 20);
    			//this.s.dispatchCommand(this.s.getConsoleSender(), "title @a title \"GO!\"");
    		}
    	} else {
    		this.countingDown = 60;
    	}
    	
    	if (this.s.getOnlinePlayers().size() == 4) { this.countingDown = 5; }
    	for (Player player: this.s.getOnlinePlayers()) {
    		player.setLevel(this.countingDown);
    	}
    }
	
	public void handlePlayers() {
		
	}
	
	public void addResources(World w) {
		if (!this.gameIsRunning) { return; }
		Generator.generate(w);
	}
	
	public void dispenseLava(World w) {
		if (!this.gameIsRunning) { return; }
		LavaHandler.dispenseLava(w);
	}
	
	//TeamHandler?
	public void assignTeams() {
    	ArrayList<String> unused_colours = new ArrayList<String>(6);
    	String[] colours = {"yellow", "blue", "light blue", "magenta", "pink", "orange"};
    	
    	for (String c: colours) {
    		unused_colours.add(c);
    	}
    	
    	for (Player player: this.s.getOnlinePlayers()) {
    		String colour = unused_colours.get(new SplittableRandom().nextInt(0, unused_colours.size() - 1));
    		unused_colours.remove(colour);
    		this.playerData.get(player.getName()).colour = colour;
    		int[] coords = rooms.get(colour);
    		player.teleport(new Location(w, coords[0] + Game.CORNER.getX(),
    										coords[1] + Game.CORNER.getY(),
    										coords[2] + Game.CORNER.getZ()));
    		
    		player.setBedSpawnLocation(new Location(this.w, 
    		  coords[0] + Game.CORNER.getX(),
			  coords[1] + Game.CORNER.getY(),
			  coords[2] + Game.CORNER.getZ()));
    		
//    		this.s.dispatchCommand(this.s.getConsoleSender(), "spawnpoint " + player.getName() 
//    		   + " " + Integer.toString((int)(coords[0] + Game.CORNER.getX()))
//			   + " " + Integer.toString((int)(coords[1] + Game.CORNER.getY()))
//			   + " " + Integer.toString((int)(coords[2] + Game.CORNER.getZ())));
    		player.sendMessage("Your colour is " + Color.getColor(colour) + colour + "!");
    	}
    }
	
	public void onPlayerJoin(PlayerJoinEvent e) {
		System.out.println("testing...");
		e.getPlayer().teleport(this.lobbySpawnPoint);
		this.playerData.put(e.getPlayer().getName(), new PlayerData());
	}
	
	public void onItemDrop(PlayerDropItemEvent e) {
		if (!this.gameIsRunning) { return; }
    	String colour = this.playerData.get(e.getPlayer().getName()).colour;
    	Item item = e.getItemDrop();
    	this.s.getPlayer("KingJellyfishII").sendMessage("Item is  :" + item.getName().toLowerCase());
    	this.s.getPlayer("KingJellyfishII").sendMessage("Colour is:" + colour + " wool");
    	if (item.getName().toLowerCase().equals(colour + " wool")) { 
    		this.playerData.get(e.getPlayer().getName()).pointsScored += 1;
    		if (this.playerData.get(e.getPlayer().getName()).pointsScored > 4) {
    			this.s.broadcastMessage(colour + " Has won!");
    			this.gameIsRunning = false;
    		}
    	}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equals("pausegame")) { 
        	this.gameIsRunning = false; 
        	sender.sendMessage("Game has been paused!");
        }
    	 
    	if (cmd.getName().equals("startgame")) { 
        	this.start(sender);
        }
    	
    	if (cmd.getName().equals("stopgame")) { 
        	gameIsRunning = false;
        	this.reset();
        	sender.sendMessage("Game has been stopped & reset!");
        }
    	
    	if (cmd.getName().equals("respawn")) {
    		 Material m = this.s.getPlayer(sender.getName()).getLocation().getBlock().getType();
    		 if (m == Material.WATER) {
    			 sender.sendMessage("Teleporting...");
    			 this.s.getPlayer(sender.getName()).teleport(new Location(this.w, 82.5, 115, 103.5));
    		 } else {
    			 sender.sendMessage("You are not outside of the building!");
    		 }
    	}
    	
        return true;
	}
}
