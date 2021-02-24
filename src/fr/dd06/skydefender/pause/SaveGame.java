package fr.dd06.skydefender.pause;

import fr.dd06.skydefender.SkyDefenderRun;

import fr.dd06.skydefender.GameTime;
import fr.dd06.skydefender.event.EventSkyDefender;
import fr.dd06.skydefender.game.BannerAttack;
import fr.dd06.skydefender.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SaveGame {
	private SkyDefenderRun main;

	public SaveGame(SkyDefenderRun main) {
		this.main = main;
	}

	public void saveGame() {
		SaveConfig.reloadSaveConfig();
		FileConfiguration saveConfig = SaveConfig.getSaveConfig();
		saveConfig.set("save.time.days", GameTime.jour);
		saveConfig.set("save.time.minutes", GameTime.minutes);
		saveConfig.set("save.time.secondes", GameTime.secondes);
		saveConfig.set("save.assault.enabled", BannerAttack.attacking);
		saveConfig.set("save.assault.minutes", BannerAttack.minutes);
		saveConfig.set("save.assault.secondes", BannerAttack.secondes);



		for (Player player : Bukkit.getOnlinePlayers()) {
			UUID uuid = player.getUniqueId();
			if (main.defenseurs.contains(uuid)) {
				saveConfig.set("save.players." + uuid + ".defenseurs", true);
			}
			else {
				saveConfig.set("save.players." + uuid + ".defenseurs", false);
			}
			if (main.attaquants.contains(uuid)) {
				saveConfig.set("save.players." + uuid + ".attaquants", true);
			}
			else {
				saveConfig.set("save.players."  + uuid + ".attaquants", false);
			}
			if(EventSkyDefender.kills.get(uuid) != null) {
				saveConfig.set("save.players."+uuid+".kills", EventSkyDefender.kills.get(uuid));
			}
			else {
				saveConfig.set("save.players."+uuid+".kills", 0 );
			}

			for(Kit kit : Kit.values()) {
				if(kit.getPlayersKit().contains(player.getUniqueId())) {
					saveConfig.set("save.players."+uuid+".kit", kit.getId());
				}
			}
		}

		SaveConfig.saveSaveConfig();
	}
}
