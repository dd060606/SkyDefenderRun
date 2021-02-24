package fr.dd06.skydefender.game;

import fr.dd06.skydefender.SkyDefenderRun;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SkyDefenderEnd {
	private SkyDefenderRun main;
	public SkyDefenderEnd(SkyDefenderRun main) {
		this.main = main;
	}
	public void winTheGame(Player player) {
		if(main.defenseurs.contains(player.getUniqueId())) {
			Bukkit.broadcastMessage("§b[SkyDefenderRun] : Les défenseurs ont gagnés la partie !");
			for(Player allplayers : Bukkit.getOnlinePlayers()) {
				allplayers.sendTitle("§bVictoire des défenseurs !", "");
				allplayers.playSound(allplayers.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 5);
			}
		}
		else if(main.attaquants.contains(player.getUniqueId())) {
			Bukkit.broadcastMessage("§b[SkyDefenderRun] : "+player.getName()+" a gagné(e) la partie !");
			for(Player allplayers : Bukkit.getOnlinePlayers()) {
				allplayers.sendTitle("§bVictoire de "+player.getName()+" !", "");
				allplayers.playSound(allplayers.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 5);
				
			}
		}
		SkyDefenderRun.setGamestarted(false);
		for(Player allplayers : Bukkit.getOnlinePlayers()) {
			allplayers.getInventory().clear();
			
		}
		

		Bukkit.getScheduler().runTaskLater(main, new BukkitRunnable() {
			
			@Override
			public void run() {
				for(Player allplayers : Bukkit.getOnlinePlayers()) {
					allplayers.getInventory().clear();
					allplayers.kickPlayer("Le SkyDefender est terminé !");
					
				}
				Bukkit.reload();
			}
		}, 600L);
		
		
	}
}
