package fr.dd06.skydefender.pause;

import fr.dd06.skydefender.SkyDefenderRun;

import fr.dd06.skydefender.GameTime;
import fr.dd06.skydefender.event.EventSkyDefender;
import fr.dd06.skydefender.game.BannerAttack;
import fr.dd06.skydefender.scoreboards.CustomScoreBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RestoreGame {
	private SkyDefenderRun main;
	public RestoreGame(SkyDefenderRun main) {
		this.main = main;
	}
	public void restoreGame() {

		SaveConfig.reloadSaveConfig();
		FileConfiguration saveConfig = SaveConfig.getSaveConfig();

		GameTime time = new GameTime(main);
		time.setTime(saveConfig.getInt("save.time.days"), saveConfig.getInt("save.time.minutes"),
				saveConfig.getInt("save.time.secondes"));

		time.runTaskTimer(main, 0, 20);

		if(saveConfig.getBoolean("save.assault.enabled")) {
			BannerAttack.attacking =true;
			BannerAttack attack = new BannerAttack(main);
			attack.setTime(saveConfig.getInt("save.assault.minutes"), saveConfig.getInt("save.assault.secondes"));
			attack.runTaskTimer(main, 0, 20);
		}



		SkyDefenderRun.setGamestarted(true);
		main.setPaused(false);
		for (String strUUID : saveConfig.getConfigurationSection("save.players").getKeys(false)) {
			UUID uuid = UUID.fromString(strUUID);
			Player player = Bukkit.getPlayer(uuid);

			if(EventSkyDefender.kills.containsKey(uuid)) {
				EventSkyDefender.kills.remove(uuid);
			}
			if(main.players.contains(uuid)) {
				main.players.remove(uuid);
			}
			if(main.defenseurs.contains(uuid)) {
				main.defenseurs.remove(uuid);
			}
			if(main.attaquants.contains(uuid)) {
				main.attaquants.remove(uuid);
			}
			main.players.add(uuid);
			if (saveConfig.getBoolean("save.players." + uuid + ".defenseurs") == true) {
				if(!main.defenseurs.contains(uuid)) {
					main.defenseurs.add(uuid);

				}
				player.setPlayerListName(ChatColor.AQUA +"[Défenseur] " +  player.getName());

			} else if (saveConfig.getBoolean("save.players." +uuid + ".attaquants") == true) {
				if(!main.attaquants.contains(uuid)) {
					main.attaquants.add(uuid);
				}
				player.setPlayerListName(ChatColor.RED +"[Attaquant] " +  player.getName());


			}
			EventSkyDefender.kills.put(uuid, saveConfig.getInt("save.players." + uuid+".kills"));
			
			

			CustomScoreBoard oldscoreboard = main.boards.get(uuid);
			if(oldscoreboard != null) {
				oldscoreboard.destroy();
			}
			if(main.boards.containsKey(uuid)) {
				main.boards.remove(uuid);
			}


			CustomScoreBoard scoreboard = new CustomScoreBoard(player, "§bSkyDefender");
			scoreboard.create();
			main.boards.put(player.getUniqueId(), scoreboard);
			main.updateScoreboards(player.getUniqueId());
			

		}
		if(saveConfig.getInt("save.time.days") >= SkyDefenderRun.getInstance().getDaysbeforepvp()) {
			SkyDefenderRun.setPvp(true);
		}
		if(saveConfig.getInt("save.time.days") >= SkyDefenderRun.getInstance().getDaysBeforeAssault()) {
			SkyDefenderRun.setAssault(true);
		}

		SaveConfig.saveSaveConfig();

		
	}
}
