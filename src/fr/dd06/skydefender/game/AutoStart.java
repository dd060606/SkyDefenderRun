package fr.dd06.skydefender.game;

import fr.dd06.skydefender.SkyDefenderRun;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoStart extends BukkitRunnable {
	private SkyDefenderRun main;
	
	public AutoStart(SkyDefenderRun main) {
		this.main = main;
	}

	@Override
	public void run() {

		for (Player pls : Bukkit.getOnlinePlayers()) {
			pls.setLevel(main.getTimer());

		}
		if (main.getTimer() == 10 || main.getTimer() == 5 || main.getTimer() == 4 || main.getTimer() == 3
				|| main.getTimer() == 2) {
			Bukkit.broadcastMessage(
					"§e[SkyDefenderRun] : §b Lancement du SkyDefenderRun dans " + main.getTimer() + " secondes");
			for (Player pls : Bukkit.getOnlinePlayers()) {
				pls.playSound(pls.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 5);

			}

		}
		if (main.getTimer() == 1) {
			Bukkit.broadcastMessage(
					"§e[SkyDefenderRun] : §b Lancement du SkyDefenderRun dans " + main.getTimer() + " seconde");
			for (Player pls : Bukkit.getOnlinePlayers()) {
				pls.playSound(pls.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 5);

			}
		}
		if (main.getTimer() == 0) {
			Bukkit.broadcastMessage("§e[SkyDefenderRun] : §bLancement du SkyDefenderRun");
			GameStart.gameStart(main);

			cancel();
		}
		main.setTimer(main.getTimer() - 1);
	}

}
