package fr.dd06.skydefender;


import fr.dd06.skydefender.commands.CommandCoords;
import fr.dd06.skydefender.commands.CommandKits;
import fr.dd06.skydefender.commands.CommandSkyDefender;
import fr.dd06.skydefender.event.BlockEvents;
import fr.dd06.skydefender.event.InventoryEvents;
import fr.dd06.skydefender.event.LivingEvents;
import fr.dd06.skydefender.event.PlayerEvents;
import fr.dd06.skydefender.game.BannerAttack;
import fr.dd06.skydefender.game.GameData;
import fr.dd06.skydefender.kits.Kit;
import fr.dd06.skydefender.pause.SaveConfig;
import fr.dd06.skydefender.pause.SaveGame;
import fr.dd06.skydefender.scoreboards.FastBoard;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkyDefenderRun extends JavaPlugin implements Listener {
	public static SkyDefenderRun instance;
	private GameData game;


	public static SkyDefenderRun getInstance() {
		return instance;
	}
	public GameData getGame() {
		return game;
	}





	@Override
	public void onEnable() {
		instance = this;
		game = new GameData(this);
		saveDefaultConfig();
		
		SaveConfig.reloadSaveConfig();
		SaveConfig.getSaveConfig().options().copyDefaults(true);
		SaveConfig.saveSaveConfig();
	
		super.onEnable();
		game.setPaused(SaveConfig.getSaveConfig().getBoolean("save.paused"));
		SaveConfig.saveSaveConfig();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new BlockEvents(this), this);
		pm.registerEvents(new LivingEvents(this), this);
		pm.registerEvents(new InventoryEvents(this), this);
		pm.registerEvents(new PlayerEvents(this), this);


		getCommand("sd").setExecutor(new CommandSkyDefender(this));
		getCommand("coords").setExecutor(new CommandCoords());
		getCommand("kits").setExecutor(new CommandKits(this));

	}

	@Override
	public void onDisable() {
		if(game.isGameStarted()) {
			SaveGame saveGame = new SaveGame(this);
			saveGame.saveGame();
			game.setPaused(true);
		}
		super.onDisable();
	}






}
