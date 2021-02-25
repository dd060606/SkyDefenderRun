package fr.dd06.skydefender.pause;

import fr.dd06.skydefender.SkyDefenderRun;

import fr.dd06.skydefender.GameTime;
import fr.dd06.skydefender.game.BannerAttack;
import fr.dd06.skydefender.kits.Kit;
import fr.dd06.skydefender.scoreboards.CustomScoreBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldBorder;
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


		main.getGame().setBorderActivated(saveConfig.getBoolean("save.border.enabled"));
		World world = Bukkit.getServer().getWorld(main.getConfig().getString("skydefendersave.spawn.world"));
		WorldBorder worldBorder = world.getWorldBorder();
		worldBorder.setSize(saveConfig.getDouble("save.border.value"));
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



		main.getGame().setGameStarted(true);
		main.getGame().setPaused(false);
		for (String strUUID : saveConfig.getConfigurationSection("save.players").getKeys(false)) {
			UUID uuid = UUID.fromString(strUUID);
			Player player = Bukkit.getPlayer(uuid);

			if(main.getGame().kills.containsKey(uuid)) {
				main.getGame().kills.remove(uuid);
			}
			if(main.getGame().players.contains(uuid)) {
				main.getGame().players.remove(uuid);
			}
			if(main.getGame().defenders.contains(uuid)) {
				main.getGame().defenders.remove(uuid);
			}
			if(main.getGame().attackers.contains(uuid)) {
				main.getGame().attackers.remove(uuid);
			}

			if (saveConfig.getBoolean("save.players." + uuid + ".defenseurs") == true) {
				if(!main.getGame().defenders.contains(uuid)) {
					main.getGame().defenders.add(uuid);
					main.getGame().players.add(uuid);

				}
				player.setPlayerListName(ChatColor.AQUA +"[Défenseur] " +  player.getName());

			} else if (saveConfig.getBoolean("save.players." +uuid + ".attaquants") == true) {
				if(!main.getGame().attackers.contains(uuid)) {
					main.getGame().attackers.add(uuid);
					main.getGame().players.add(uuid);
				}
				player.setPlayerListName(ChatColor.RED +"[Attaquant] " +  player.getName());


			}
			main.getGame().kills.put(uuid, saveConfig.getInt("save.players." + uuid+".kills"));
			if(saveConfig.getString("save.players."+uuid+".kit") != null) {

				if(!saveConfig.getString("save.players."+uuid+".kit").equals("none")) {
					Kit.selectKit(Bukkit.getPlayer(uuid), Kit.getKitFromId(saveConfig.getString("save.players."+uuid+".kit")));
					Kit.addEffectsToPlayer(player);
				}


			}



			CustomScoreBoard oldscoreboard = main.getGame().boards.get(uuid);
			if(oldscoreboard != null) {
				oldscoreboard.destroy();
			}
			if(main.getGame().boards.containsKey(uuid)) {
				main.getGame().boards.remove(uuid);
			}


			CustomScoreBoard scoreboard = new CustomScoreBoard(player, "§bSkyDefender");
			scoreboard.create();
			main.getGame().boards.put(player.getUniqueId(), scoreboard);
			main.getGame().updateScoreboards(player.getUniqueId());
			

		}
		if(saveConfig.getInt("save.time.days") >= main.getGame().getDaysBeforePvp()) {
			main.getGame().setPvp(true);
		}
		if(saveConfig.getInt("save.time.days") >= main.getGame().getDaysBeforeAssault()) {
			main.getGame().setAssaultEnabled(true);
		}

		SaveConfig.saveSaveConfig();

		
	}
}
