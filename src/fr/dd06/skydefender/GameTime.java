package fr.dd06.skydefender;

import org.bukkit.Bukkit;
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
		if (minutes == SkyDefenderRun.getInstance().getMinutesbeforenextday()) {
			secondes = 0;
			minutes = 0;
			jour++;
			Bukkit.broadcastMessage("§e[SkyDefenderRun] : §aDébut du jour " + jour + " !");


			if(jour >= SkyDefenderRun.getInstance().getDaysCoordsMessage()) {
				Bukkit.broadcastMessage("§e[SkyDefenderRun] : §aCoordonnées des joueurs : \n");

				for(UUID uuid : main.players) {

					Player player = Bukkit.getPlayer(uuid);

					if(player != null) {
						Bukkit.broadcastMessage("§e[SkyDefenderRun] : §a"  +player.getName() + " : X: " + (int) player.getLocation().getX() + " Y: " + (int) player.getLocation().getY() + " Z: " + (int) player.getLocation().getZ() + "\n");
					}
				}
			}


		}

		if(jour == 1 && minutes == 0 && secondes == 10) {
			Bukkit.broadcastMessage("§e[SkyDefenderRun] : §aVous pouvez choisir un kit avec la commande /kits !");

		}
		if (jour == SkyDefenderRun.getInstance().getDaysbeforepvp() && !SkyDefenderRun.getPvp()) {
			SkyDefenderRun.setPvp(true);
			Bukkit.broadcastMessage("§e[SkyDefenderRun] : §aLe PvP est activé !");

		}

		if (jour == SkyDefenderRun.getInstance().getDaysBeforeAssault() && !SkyDefenderRun.isAssaultEnabled()) {
			SkyDefenderRun.setAssault(true);
			Bukkit.broadcastMessage("§e[SkyDefenderRun] : §aL'attaque de château est activée !");

		}



		for (Player allplayers : Bukkit.getOnlinePlayers()) {
			main.updateScoreboards(allplayers.getUniqueId());

		}
		for (UUID uuid : main.spectateurs) {
			main.updateSpectatorsBoards(uuid);
		}
		secondes++;

		if (main.isPaused() == true) {
			this.cancel();
		}


	}

}
