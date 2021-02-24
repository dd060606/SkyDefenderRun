package fr.dd06.skydefender;


import fr.dd06.skydefender.commands.CommandCoords;
import fr.dd06.skydefender.commands.CommandKits;
import fr.dd06.skydefender.commands.CommandSkyDefender;
import fr.dd06.skydefender.event.EventSkyDefender;
import fr.dd06.skydefender.game.BannerAttack;
import fr.dd06.skydefender.kits.Kit;
import fr.dd06.skydefender.pause.SaveConfig;
import fr.dd06.skydefender.scoreboards.CustomScoreBoard;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkyDefenderRun extends JavaPlugin implements Listener {
	public static SkyDefenderRun instance;
	public ArrayList<UUID> players = new ArrayList<>();

	public static SkyDefenderRun getInstance() {
		return instance;
	}

	
	public Map<UUID, CustomScoreBoard> boards = new HashMap<>();
	public Map<UUID, CustomScoreBoard> specboards = new HashMap<>();
	
	public ArrayList<UUID> defenseurs = new ArrayList<>();
	public ArrayList<UUID> attaquants = new ArrayList<>();
	public ArrayList<UUID> spectateurs = new ArrayList<>();



	private static boolean gamestarted = false;
	private boolean paused;
	private static boolean pvp = false;
	private static boolean assault = false;
	private static boolean coordsMessage = false;

	
	private int timer = this.getConfig().getInt("skydefenderconfig.starting.cooldown");
	private int minutesbeforenextday = this.getConfig().getInt("skydefenderconfig.ingameconfig.minutesbeforenextday");
	private int daysbeforepvp = this.getConfig().getInt("skydefenderconfig.ingameconfig.daysbeforepvp");
	private int daysCoordsMessage = this.getConfig().getInt("skydefenderconfig.ingameconfig.dayscoordsmessage");
	private  int daysBeforeAssault = this.getConfig().getInt("skydefenderconfig.ingameconfig.daysbeforeassault");
	private int minutesBannerCooldown = this.getConfig().getInt("skydefenderconfig.ingameconfig.minutesbannercooldown");

	@Override
	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		
		SaveConfig.reloadSaveConfig();
		SaveConfig.getSaveConfig().options().copyDefaults(true);
		SaveConfig.saveSaveConfig();
	
		super.onEnable();
		paused = SaveConfig.getSaveConfig().getBoolean("save.paused");
		SaveConfig.saveSaveConfig();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new EventSkyDefender(this), this);

		getCommand("sd").setExecutor(new CommandSkyDefender(this));
		getCommand("coords").setExecutor(new CommandCoords());
		getCommand("kits").setExecutor(new CommandKits(this));

	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	public static boolean getGamestarted() {
		return gamestarted;
	}

	public static void setGamestarted(boolean gamestarted) {
		SkyDefenderRun.gamestarted = gamestarted;
	}

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public int getMinutesbeforenextday() {
		return minutesbeforenextday;
	}

	public static boolean getPvp() {
		return pvp;
	}

	public static void setPvp(boolean pvp) {
		SkyDefenderRun.pvp = pvp;
	}

	public int getDaysbeforepvp() {
		return daysbeforepvp;
	}


	public static void setAssault(boolean assault) {
		SkyDefenderRun.assault = assault;
	}

	public static boolean isAssaultEnabled() {
		return assault;
	}

	public int getDaysBeforeAssault() {
		return daysBeforeAssault;
	}

	public int getMinutesBannerCooldown() { return minutesBannerCooldown; }


	public int getDaysCoordsMessage() { return daysCoordsMessage;}

	public void updateScoreboards(UUID uuid) {
		if (boards.containsKey(uuid)) {
			CustomScoreBoard scoreboard = boards.get(uuid);
			scoreboard.setLine(0, "§e ");
			scoreboard.setLine(1, "§e ");
			scoreboard.setLine(2, "§9Joueurs : " + this.players.size());

			scoreboard.setLine(3, "§eJour : " + GameTime.jour);
			scoreboard.setLine(4, "§6Temps : " + GameTime.minutes + ":" + GameTime.secondes);
			if (EventSkyDefender.kills.get(uuid) == null) {
				EventSkyDefender.kills.put(uuid, 0);
				scoreboard.setLine(5, "§cKills : " + EventSkyDefender.kills.get(uuid));
			} else {
				scoreboard.setLine(5, "§cKills : " + EventSkyDefender.kills.get(uuid));
			}
			if(pvp) {
				scoreboard.setLine(6, "§6PvP : §aOn");

			}
			else {
				scoreboard.setLine(6, "§6PvP : §cOff");

			}

			if(assault) {
				scoreboard.setLine(7, "§6Assaut : §aOn");

			}
			else {
				scoreboard.setLine(7, "§6Assaut : §cOff");

			}
			if (this.defenseurs.contains(uuid)) {
				scoreboard.setLine(8, "§7Equipe : §bDéfenseurs");
			} else if (this.attaquants.contains(uuid)) {
				scoreboard.setLine(8, "§7Equipe : §cAttaquants");
			} else if (this.spectateurs.contains(uuid)) {
				scoreboard.setLine(8, "§7Equipe : §5Spectateurs");
			}


			if(BannerAttack.attacking) {
				scoreboard.setLine(9, "§cAttaque : " + BannerAttack.minutes + "mins | "+BannerAttack.secondes + "secs/" +SkyDefenderRun.getInstance().getMinutesBannerCooldown() + "mins");
			}

			if(Kit.hasKit(Bukkit.getPlayer(uuid))) {

				scoreboard.setLine(10, "§dKit : §c" + Kit.getPlayerKit(Bukkit.getPlayer(uuid)).getName());
			}
		}
	}

	public void updateSpectatorsBoards(UUID uuid) {
		if (specboards.containsKey(uuid)) {
			CustomScoreBoard scoreboard = specboards.get(uuid);
			scoreboard.setLine(0, "§e ");
			scoreboard.setLine(1, "§e ");
			scoreboard.setLine(2, "§9Joueurs : " + this.players.size());
			scoreboard.setLine(3, "§eJour : " + GameTime.jour);
			scoreboard.setLine(4, "§6Temps : " + GameTime.minutes + ":" + GameTime.secondes);

			if(pvp) {
				scoreboard.setLine(5, "§6PvP : §aOn");

			}
			else {
				scoreboard.setLine(5, "§6PvP : §cOff");

			}

			if(assault) {
				scoreboard.setLine(6, "§6Assaut : §aOn");

			}
			else {
				scoreboard.setLine(6, "§6Assaut : §cOff");

			}
			scoreboard.setLine(7, "§7Equipe : §5Spectateurs");


			if(BannerAttack.attacking) {
				scoreboard.setLine(8, "§cAttaque : " + BannerAttack.minutes + "mins | "+BannerAttack.secondes + "secs/" +SkyDefenderRun.getInstance().getMinutesBannerCooldown() + "mins");
			}

		}
	}
	public boolean isPaused() {
		return paused;
	}
	public void setPaused(boolean enabled) {
		SaveConfig.reloadSaveConfig();
		SaveConfig.getSaveConfig().set("save.paused", enabled);
		SaveConfig.saveSaveConfig();
		
		paused = enabled;
	}



}
