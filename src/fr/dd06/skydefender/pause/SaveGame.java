package fr.dd06.skydefender.pause;

import fr.dd06.skydefender.GameTime;
import fr.dd06.skydefender.SkyDefenderRun;
import fr.dd06.skydefender.event.BlockEvents;
import fr.dd06.skydefender.game.BannerAttack;
import fr.dd06.skydefender.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
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
        World world = Bukkit.getServer().getWorld(main.getConfig().getString("skydefendersave.spawn.world"));
        WorldBorder worldBorder = world.getWorldBorder();
        saveConfig.set("save.border.value",worldBorder.getSize());
        saveConfig.set("save.border.started", main.getGame().isBorderActivated());


        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            if (main.getGame().defenders.contains(uuid)) {
                saveConfig.set("save.players." + uuid + ".defenseurs", true);
            } else {
                saveConfig.set("save.players." + uuid + ".defenseurs", false);
            }
            if (main.getGame().attackers.contains(uuid)) {
                saveConfig.set("save.players." + uuid + ".attaquants", true);
            } else {
                saveConfig.set("save.players." + uuid + ".attaquants", false);
            }
            if (main.getGame().kills.get(uuid) != null) {
                saveConfig.set("save.players." + uuid + ".kills", main.getGame().kills.get(uuid));
            } else {
                saveConfig.set("save.players." + uuid + ".kills", 0);
            }


            if (Kit.hasKit(player)) {
                saveConfig.set("save.players." + uuid + ".kit", Kit.getPlayerKit(player).getId());
            }
            else {
                saveConfig.set("save.players." + uuid + ".kit", "none");

            }

        }

        SaveConfig.saveSaveConfig();
    }
}
