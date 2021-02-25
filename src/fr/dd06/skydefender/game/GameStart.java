package fr.dd06.skydefender.game;

import fr.dd06.skydefender.SkyDefenderRun;

import fr.dd06.skydefender.GameTime;
import fr.dd06.skydefender.kits.Kit;
import fr.dd06.skydefender.scoreboards.CustomScoreBoard;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.Random;

public class GameStart {
	
	public static void gameStart(SkyDefenderRun main) {
		
		for (Player allplayers : Bukkit.getOnlinePlayers()) {
		
		
			allplayers.getInventory().clear();
			allplayers.setGameMode(GameMode.SURVIVAL);
			AttributeInstance attribute = allplayers.getAttribute(Attribute.GENERIC_MAX_HEALTH);
			attribute.setBaseValue(20.0D);
			allplayers.setHealth(20);
			allplayers.setFoodLevel(20);
			for(PotionEffect effect : allplayers.getActivePotionEffects()) {
				allplayers.removePotionEffect(effect.getType());
			}

			allplayers.playSound(allplayers.getLocation(), Sound.ENTITY_WITHER_SPAWN, 10, 5);

			if (!main.getGame().players.contains(allplayers.getUniqueId()))
				main.getGame().players.add(allplayers.getUniqueId());

			if (main.getGame().attackers.contains(allplayers.getUniqueId())) {
				main.reloadConfig();
				World w = Bukkit.getServer().getWorld(main.getConfig().getString("skydefendersave.attspawn.world"));
				double x = main.getConfig().getDouble("skydefendersave.attspawn.x");
				double y = main.getConfig().getDouble("skydefendersave.attspawn.y");
				double z = main.getConfig().getDouble("skydefendersave.attspawn.z");
				allplayers.teleport(new Location(w, x, y, z));

				allplayers.setPlayerListName(ChatColor.RED +"[Attaquant] " +  allplayers.getName());
				allplayers.sendMessage("§aVous êtes un attaquant !");

				
				
			} else if (main.getGame().defenders.contains(allplayers.getUniqueId())) {
				main.reloadConfig();
				World w = Bukkit.getServer().getWorld(main.getConfig().getString("skydefendersave.defspawn.world"));
				double x = main.getConfig().getDouble("skydefendersave.defspawn.x");
				double y = main.getConfig().getDouble("skydefendersave.defspawn.y");
				double z = main.getConfig().getDouble("skydefendersave.defspawn.z");
				allplayers.teleport(new Location(w, x, y, z));

				allplayers.setPlayerListName(ChatColor.AQUA +"[Défenseur] " +  allplayers.getName());

				allplayers.sendMessage("§aVous êtes un défenseur !");
			} else if (main.getGame().spectators.contains(allplayers.getUniqueId())) {
				allplayers.getInventory().clear();
				if(main.getGame().players.contains(allplayers.getUniqueId())) {
					main.getGame().players.remove(allplayers.getUniqueId());
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
				if (main.getGame().defenders.size() >= main.getConfig()
						.getInt("skydefenderconfig.ingameconfig.teams.defenseurs.maxplayers")) {

					main.getGame().attackers.add(allplayers.getUniqueId());
					main.reloadConfig();
					World w = Bukkit.getServer().getWorld(main.getConfig().getString("skydefendersave.attspawn.world"));
					double x = main.getConfig().getDouble("skydefendersave.attspawn.x");
					//double y = main.getConfig().getDouble("skydefendersave.attspawn.y");
					double z = main.getConfig().getDouble("skydefendersave.attspawn.z");
					int range = main.getConfig().getInt("skydefenderconfig.ingameconfig.teams.attaquants.spawnrange");

					Random random = new Random();
					int result = random.nextInt(range);
					allplayers.teleport(new Location(w, x + result, 150, z + result));

					allplayers.setPlayerListName(ChatColor.RED +"[Attaquant] " +  allplayers.getName());
					allplayers.sendMessage("§aVous êtes un attaquant !");

				} else {
					main.getGame().defenders.add(allplayers.getUniqueId());
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

			if(main.getConfig().getBoolean("skydefenderconfig.ingameconfig.border.enabled")) {
				World world = Bukkit.getServer().getWorld(main.getConfig().getString("skydefendersave.spawn.world"));
				WorldBorder worldBorder = world.getWorldBorder();
				worldBorder.setCenter(0,0);
				worldBorder.setSize(main.getConfig().getDouble("skydefenderconfig.ingameconfig.border.max"));
				worldBorder.setDamageAmount(5);

			}


			main.getGame().setGameStarted(true);
			main.getGame().setGodTime(true);


			if(!main.getGame().spectators.contains(allplayers.getUniqueId())) {
			CustomScoreBoard scoreboard = new CustomScoreBoard(allplayers, "§bSkyDefender");
			scoreboard.destroy();
			scoreboard.create();
			
			main.getGame().boards.put(allplayers.getUniqueId(), scoreboard);
			main.getGame().updateScoreboards(allplayers.getUniqueId());
			}
			else {
				CustomScoreBoard specboard = new CustomScoreBoard(allplayers, "§bSkyDefender");
				specboard.destroy();
				specboard.create();
				
				main.getGame().specboards.put(allplayers.getUniqueId(), specboard);
				main.getGame().updateSpectatorsBoards(allplayers.getUniqueId());
			}
			



		}
		GameTime gameTime = new GameTime(main);
		gameTime.runTaskTimer(main, 0, 20);
	}
}
