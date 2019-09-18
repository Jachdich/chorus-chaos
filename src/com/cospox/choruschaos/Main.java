//TODO
//Waiting list and countdown
//Auto-start (if configured)
//Doesn't affect spectators
//Autoclear bowls (if needed)
//Auto asign colours & detect wins
//Autoclear other items too?
//Finalise gen ammounts
//Fully automate everything - no player interaction

package com.cospox.choruschaos;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Main extends JavaPlugin implements Listener {
	
	private Game game;
	public static World w;
	
    // Fired when plugin is first enabled
    @Override
    public void onEnable() {
    	w = Bukkit.getWorlds().get(0);
    	
    	this.setupConfig();
    	this.game = new Game(w, this.getServer(), this);
    	this.setupSchedulers();
    	this.getServer().getPluginManager().registerEvents(this, this);
    }
    
    public void setupConfig() {
    	this.getConfig().addDefault("world.lobbyspawn", new double[]{0.5, 4.0, 1000.5});
		this.getConfig().addDefault("world.hubworld", "world_the_end");
		this.saveDefaultConfig();
    }
    
    // Fired when plugin is disabled
    @Override
    public void onDisable() {
    	
    }
    
    private void setupSchedulers() {
    	BukkitScheduler scheduler = this.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                game.dispenseLava(w);
            }
        }, 0L, 4L);
        
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                game.addResources(w);
            }
        }, 0L, 10L);
        
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                game.handlePlayers();
            }
        }, 0L, 10L);
        
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                game.countDown();
            }
        }, 0L, 20L);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	return this.game.onCommand(sender, cmd, label, args);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        if (event.getCause().equals(DamageCause.FIRE) || event.getCause().equals(DamageCause.FIRE) || event.getCause() == DamageCause.FIRE_TICK) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
    	e.getPlayer().sendMessage("Testing...");
    	this.game.onPlayerJoin(e);
    }
    
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
    	this.game.onItemDrop(e);
    }
}
