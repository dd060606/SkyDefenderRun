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
			pls.setLevel(main.getGame().getTimer());

		}
		if (main.getGame().getTimer() == 10 || main.getGame().getTimer() == 5 || main.getGame().getTimer() == 4 || main.getGame().getTimer() == 3
				|| main.getGame().getTimer() == 2) {
			Bukkit.broadcastMessage(
					"§e[SkyDefenderRun] : §b Lancement du SkyDefenderRun dans " + main.getGame().getTimer() + " secondes");
			for (Player pls : Bukkit.getOnlinePlayers()) {
				pls.playSound(pls.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 5);

			}

		}
		if (main.getGame().getTimer() == 1) {
			Bukkit.broadcastMessage(
					"§e[SkyDefenderRun] : §b Lancement du SkyDefenderRun dans " + main.getGame().getTimer() + " seconde");
			for (Player pls : Bukkit.getOnlinePlayers()) {
				pls.playSound(pls.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 5);

			}
		}
		if (main.getGame().getTimer() == 0) {
			Bukkit.broadcastMessage("§e[SkyDefenderRun] : §bLancement du SkyDefenderRun");
			GameStart.gameStart(main);

			cancel();
		}
		main.getGame().setTimer(main.getGame().getTimer() - 1);
	}

}
