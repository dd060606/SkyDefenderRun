package fr.dd06.skydefender;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class GameTime extends BukkitRunnable {

	public static int secondes = 0;

	public static int minutes = 0;
	public static int jour = 1;
	private SkyDefenderRun main;

	public GameTime(SkyDefenderRun main) {
		this.main = main;
	}

	public void setTime(int days, int minutes, int secondes) {
		GameTime.jour = days;
		GameTime.minutes = minutes;
		GameTime.secondes = secondes;
	}

	@Override
	public void run() {

		if (secondes == 59) {
			secondes = 0;
			minutes++;
		}
		if (minutes == main.getGame().getMinutesBeforeNextDay()) {
			secondes = 0;
			minutes = 0;
			jour++;
			Bukkit.broadcastMessage("§e[SkyDefenderRun] : §aDébut du jour " + jour + " !");


			if(jour >= main.getGame().getDaysCoordsMessage()) {
				Bukkit.broadcastMessage("§e[SkyDefenderRun] : §aCoordonnées des joueurs : \n");

				for(UUID uuid : main.getGame().players) {

					Player player = Bukkit.getPlayer(uuid);

					if(player != null) {
						Bukkit.broadcastMessage("§e[SkyDefenderRun] : §a"  +player.getName() + " : X: " + (int) player.getLocation().getX() + " Y: " + (int) player.getLocation().getY() + " Z: " + (int) player.getLocation().getZ() + "\n");
					}
				}
			}


		}

		if(jour == 1 && minutes == 0 && secondes == 10) {
			Bukkit.broadcastMessage("§e[SkyDefenderRun] : §aVous pouvez choisir un kit avec la commande /kits !");
			main.getGame().setGodTime(false);
		}
		if (jour == main.getGame().getDaysBeforePvp() && !main.getGame().isPvpEnabled()) {
			main.getGame().setPvp(true);
			Bukkit.broadcastMessage("§e[SkyDefenderRun] : §aLe PvP est activé !");

		}

		if (jour == main.getGame().getDaysBeforeAssault() && !main.getGame().isAssaultEnabled()) {
			main.getGame().setAssaultEnabled(true);
			Bukkit.broadcastMessage("§e[SkyDefenderRun] : §aL'attaque de château est activée !");

		}
		if(jour == main.getGame().getDaysBeforeBorder() && !main.getGame().isBorderActivated() && main.getConfig().getBoolean("skydefenderconfig.ingameconfig.border.enabled")) {
			main.getGame().setBorderActivated(true);
			Bukkit.broadcastMessage("§e[SkyDefenderRun] : §aLa bordure commence à se rétrécir !");
		}


		for (Player allplayers : Bukkit.getOnlinePlayers()) {
			main.getGame().updateScoreboards(allplayers.getUniqueId());

		}
		for (UUID uuid : main.getGame().spectators) {
			main.getGame().updateSpectatorsBoards(uuid);
		}

		if(main.getGame().isBorderActivated()) {
			World world = Bukkit.getServer().getWorld(main.getConfig().getString("skydefendersave.spawn.world"));
			WorldBorder worldBorder = world.getWorldBorder();

			if(worldBorder.getSize() >= main.getConfig().getDouble("skydefenderconfig.ingameconfig.border.min")) {
				worldBorder.setSize(worldBorder.getSize() - main.getConfig().getDouble("skydefenderconfig.ingameconfig.border.speed"));
			}
		}

		secondes++;

		if (main.getGame().isPaused()) {
			this.cancel();
		}


	}

}
