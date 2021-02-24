package fr.dd06.skydefender.game;

import fr.dd06.skydefender.SkyDefenderRun;

import fr.dd06.skydefender.GameTime;
import fr.dd06.skydefender.scoreboards.CustomScoreBoard;
import org.bukkit.*;
import org.bukkit.entity.Player;

public class GameStart {
	
	public static void gameStart(SkyDefenderRun main) {
		
		for (Player allplayers : Bukkit.getOnlinePlayers()) {
		
		
			allplayers.getInventory().clear();
			allplayers.setGameMode(GameMode.SURVIVAL);
			allplayers.setHealth(20);
			allplayers.setFoodLevel(20);
			allplayers.playSound(allplayers.getLocation(), Sound.ENTITY_WITHER_SPAWN, 10, 5);

			if (!SkyDefenderRun.getInstance().players.contains(allplayers.getUniqueId()))
				SkyDefenderRun.getInstance().players.add(allplayers.getUniqueId());

			if (main.attaquants.contains(allplayers.getUniqueId())) {
				main.reloadConfig();
				World w = Bukkit.getServer().getWorld(main.getConfig().getString("skydefendersave.attspawn.world"));
				double x = main.getConfig().getDouble("skydefendersave.attspawn.x");
				double y = main.getConfig().getDouble("skydefendersave.attspawn.y");
				double z = main.getConfig().getDouble("skydefendersave.attspawn.z");
				allplayers.teleport(new Location(w, x, y, z));

				allplayers.setPlayerListName(ChatColor.RED +"[Attaquant] " +  allplayers.getName());
				allplayers.sendMessage("§aVous êtes un attaquant !");

				
				
			} else if (main.defenseurs.contains(allplayers.getUniqueId())) {
				main.reloadConfig();
				World w = Bukkit.getServer().getWorld(main.getConfig().getString("skydefendersave.defspawn.world"));
				double x = main.getConfig().getDouble("skydefendersave.defspawn.x");
				double y = main.getConfig().getDouble("skydefendersave.defspawn.y");
				double z = main.getConfig().getDouble("skydefendersave.defspawn.z");
				allplayers.teleport(new Location(w, x, y, z));

				allplayers.setPlayerListName(ChatColor.AQUA +"[Défenseur] " +  allplayers.getName());

				allplayers.sendMessage("§aVous êtes un défenseur !");
			} else if (main.spectateurs.contains(allplayers.getUniqueId())) {
				allplayers.getInventory().clear();
				if(main.players.contains(allplayers.getUniqueId())) {
					main.players.remove(allplayers.getUniqueId());
				}
				
				allplayers.setGameMode(GameMode.SPECTATOR);
				main.reloadConfig();
				World w = Bukkit.getServer().getWorld(main.getConfig().getString("skydefendersave.spawn.world"));
				double x = main.getConfig().getDouble("skydefendersave.spawn.x");
				double y = main.getConfig().getDouble("skydefendersave.spawn.y");
				double z = main.getConfig().getDouble("skydefendersave.spawn.z");
				allplayers.teleport(new Location(w, x, y, z));

				allplayers.setPlayerListName(ChatColor.LIGHT_PURPLE +"[Spectateur] " +  allplayers.getName());

				allplayers.sendMessage("§aVous êtes un spectateur !");

			}

			else {
				if (main.defenseurs.size() >= main.getConfig()
						.getInt("skydefenderconfig.ingameconfig.teams.defenseurs.maxplayers")) {
					
					main.attaquants.add(allplayers.getUniqueId());
					main.reloadConfig();
					World w = Bukkit.getServer().getWorld(main.getConfig().getString("skydefendersave.attspawn.world"));
					double x = main.getConfig().getDouble("skydefendersave.attspawn.x");
					double y = main.getConfig().getDouble("skydefendersave.attspawn.y");
					double z = main.getConfig().getDouble("skydefendersave.attspawn.z");
					allplayers.teleport(new Location(w, x, y, z));

					allplayers.setPlayerListName(ChatColor.RED +"[Attaquant] " +  allplayers.getName());
					allplayers.sendMessage("§aVous êtes un attaquant !");

				} else {
					main.defenseurs.add(allplayers.getUniqueId());
					main.reloadConfig();
					World w = Bukkit.getServer().getWorld(main.getConfig().getString("skydefendersave.defspawn.world"));
					double x = main.getConfig().getDouble("skydefendersave.defspawn.x");
					double y = main.getConfig().getDouble("skydefendersave.defspawn.y");
					double z = main.getConfig().getDouble("skydefendersave.defspawn.z");
					allplayers.teleport(new Location(w, x, y, z));
					allplayers.setPlayerListName(ChatColor.AQUA +"[Défenseur] " +  allplayers.getName());

					allplayers.sendMessage("§aVous êtes un défenseur !");

				}
			}
			
			
			main.reloadConfig();
			World worldbanner = Bukkit.getServer().getWorld(main.getConfig().getString("skydefendersave.banner.world"));
			double xbanner = main.getConfig().getDouble("skydefendersave.banner.x");
			double ybanner = main.getConfig().getDouble("skydefendersave.banner.y");
			double zbanner = main.getConfig().getDouble("skydefendersave.banner.z");
			Location locbanner = new Location(worldbanner, xbanner, ybanner, zbanner);
			locbanner.getBlock().setType(Material.STANDING_BANNER);
			main.reloadConfig();
			World worldtp1 = Bukkit.getServer().getWorld(main.getConfig().getString("skydefendersave.tp1.world"));
			double xtp1 = main.getConfig().getDouble("skydefendersave.tp1.x");
			double ytp1 = main.getConfig().getDouble("skydefendersave.tp1.y");
			double ztp1 = main.getConfig().getDouble("skydefendersave.tp1.z");
			Location loctp1 = new Location(worldtp1, xtp1, ytp1, ztp1);
			
			loctp1.getBlock().setType(Material.REDSTONE_BLOCK);
			
			
			main.reloadConfig();
			World worldtp2 = Bukkit.getServer().getWorld(main.getConfig().getString("skydefendersave.tp2.world"));
			double xtp2 = main.getConfig().getDouble("skydefendersave.tp2.x");
			double ytp2 = main.getConfig().getDouble("skydefendersave.tp2.y");
			double ztp2 = main.getConfig().getDouble("skydefendersave.tp2.z");
			Location loctp2 = new Location(worldtp2, xtp2, ytp2, ztp2);
			loctp2.getBlock().setType(Material.REDSTONE_BLOCK);



			SkyDefenderRun.setGamestarted(true);

			if(!main.spectateurs.contains(allplayers.getUniqueId())) {
			CustomScoreBoard scoreboard = new CustomScoreBoard(allplayers, "§bSkyDefender");
			scoreboard.destroy();
			scoreboard.create();
			
			main.boards.put(allplayers.getUniqueId(), scoreboard);
			main.updateScoreboards(allplayers.getUniqueId());
			}
			else {
				CustomScoreBoard specboard = new CustomScoreBoard(allplayers, "§bSkyDefender");
				specboard.destroy();
				specboard.create();
				
				main.specboards.put(allplayers.getUniqueId(), specboard);
				main.updateSpectatorsBoards(allplayers.getUniqueId());
			}
			
			

			
		}
		GameTime gameTime = new GameTime(main);
		gameTime.runTaskTimer(main, 0, 20);
	}
}
