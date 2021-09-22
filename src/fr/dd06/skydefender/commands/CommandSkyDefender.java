package fr.dd06.skydefender.commands;

import fr.dd06.skydefender.SkyDefenderRun;

import fr.dd06.skydefender.pause.RestoreGame;
import fr.dd06.skydefender.pause.SaveConfig;
import fr.dd06.skydefender.pause.SaveGame;
import fr.dd06.skydefender.scoreboards.CustomScoreBoard;
import fr.dd06.skydefender.game.AutoStart;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.UUID;

public class CommandSkyDefender implements CommandExecutor, Listener {
	public static CommandSkyDefender exeinstance;

	public static CommandSkyDefender getexeInstance() {
		return exeinstance;
	}

	private SkyDefenderRun main;

	public CommandSkyDefender(SkyDefenderRun skyDefender) {
		this.main = skyDefender;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

		if (sender instanceof Player) {
			Player p = (Player) sender;

			if (cmd.getName().equalsIgnoreCase("sd")) {
				if (p.hasPermission("fr.dd06.skydefender.sd") || p.hasPermission("fr.dd06.skydefender.*")) {
					if (args.length == 0) {
						p.sendMessage(
								"§e[SkyDefenderRun] :\n §bCréateur : §9dd_06 \n §bDescription : §9Le plugin est un plugin SkyDefender rapide\n §e Pour afficher la liste des commandes faite /sd help");
					}
				} else {
					p.sendMessage("§e[SkyDefenderRun] : §cVous n'avez pas les permissions !");
				}
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("help")) {
						if (p.hasPermission("fr.dd06.skydefender.sd") || p.hasPermission("fr.dd06.skydefender.*")) {
							p.sendMessage(
									"§bListe des commandes : §f\n/sd help : §eAfficher la liste des commandes du plugin SkyDefenderRun"
											+ "§f\n/sd start : §eDémarrer le SkyDefender"
											+ "§f\n/sd stop : §eStopper le SkyDefender"
											+ "§f\n/sd info : §eAfficher les infos du plugin SkyDefender"
											+ "§f\n/sd setdefspawn : §eDéfinir le spawn des défenseurs"
											+ "§f\n/sd setattspawn : §eDéfinir le spawn des attaquants"
											+ "§f\n/sd setbanner : §eDéfinir l'emplacement de la bannière"
											+ "§f\n/sd setspawn : §e Définir le spawn du SkyDefender"
											+ "§f\n/sd setteleporter1 : §eDéfinir le téléporteur 1"
											+ "§f\n/sd setteleporter2 : §eDéfinir le téléporteur 2");
						} else {
							p.sendMessage("§e[SkyDefenderRun] : §cVous n'avez pas les permissions !");
						}
					}
					if (args[0].equalsIgnoreCase("info")) {
						if (p.hasPermission("fr.dd06.skydefender.sd") || p.hasPermission("fr.dd06.skydefender.*")) {

							p.sendMessage(
									"§e[SkyDefenderRun] :\n §bCréateur : §9dd_06 \n §bDescription : §9Le plugin est un plugin SkyDefender\n §e Pour afficher la liste des commandes faite /sd help");
						} else {
							p.sendMessage("§e[SkyDefenderRun] : §cVous n'avez pas les permissions !");
						}
					}
					if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
						if (p.hasPermission("fr.dd06.skydefender.sdreload") || p.hasPermission("fr.dd06.skydefender.*")) {
							Bukkit.reload();
							main.reloadConfig();
							sender.sendMessage("§e[SkyDefenderRun] : §bLe plugin vient d'être rechargé !");
						} else {
							p.sendMessage("§e[SkyDefenderRun] : §cVous n'avez pas les permissions !");
						}
					}

					if (args[0].equalsIgnoreCase("start")) {
						if (p.hasPermission("fr.dd06.skydefender.sdstart") || p.hasPermission("fr.dd06.skydefender.*")) {
							if (!main.getGame().isGameStarted()) {
								exeinstance = this;
								if (Bukkit.getOnlinePlayers().size() >= main.getConfig()
										.getInt("skydefenderconfig.starting.minplayers")) {
									Bukkit.broadcastMessage(
											"§e[SkyDefenderRun] : §bUn SkyDefender va bientôt commencer !");
									main.getGame().setPaused(false);

									for (Player allplayers : Bukkit.getOnlinePlayers()) {
										UUID uuid = allplayers.getUniqueId();

										CustomScoreBoard oldscoreboard = main.getGame().boards.get(uuid);
										if(oldscoreboard != null) {
											oldscoreboard.delete();

										}
										if(main.getGame().boards.containsKey(uuid)) {
											main.getGame().boards.remove(uuid);
										}

									}
									AutoStart autoStart = new AutoStart(main);
									autoStart.runTaskTimer(main, 0, 20);

								} else {
									sender.sendMessage(
											"§e[SkyDefenderRun] : §cIl n'y a pas assez de joueurs pour commencer !");
								}

							} else {
								sender.sendMessage("§4Il y a déjà un SkyDefender démarré !");
							}
						} else {
							p.sendMessage("§e[SkyDefenderRun] : §cVous n'avez pas les permissions !");
						}
					}
					if (args[0].equalsIgnoreCase("stop")) {
						if (p.hasPermission("fr.dd06.skydefender.sdstop") || p.hasPermission("fr.dd06.skydefender.*")) {
							if (main.getGame().isGameStarted()) {

								Bukkit.broadcastMessage("§e[SkyDefenderRun] : §bLe SkyDefender vient de s'arrêter !");
								for (Player allplayer : Bukkit.getServer().getOnlinePlayers()) {
									allplayer.setGameMode(GameMode.ADVENTURE);

									allplayer.getInventory().clear();
									ItemStack teamchooser = new ItemStack(Material.WHITE_BANNER, 1);
									ItemMeta teamchoosermeta = teamchooser.getItemMeta();
									teamchoosermeta.addEnchant(Enchantment.DAMAGE_ALL, 0, true);
									teamchoosermeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
									teamchoosermeta.setDisplayName("§eChoisir une équipe");
									teamchooser.setItemMeta(teamchoosermeta);
									allplayer.getInventory().setItem(4, teamchooser);
									allplayer.updateInventory();

									allplayer.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);

									allplayer.setFoodLevel(20);
									allplayer.setHealth(20);

									if (main.getConfig().getConfigurationSection("skydefenderconfig.teamchooser")
											.getBoolean("enabled")) {

										teamchooser.setItemMeta(teamchoosermeta);

										allplayer.getInventory().setItem(4, teamchooser);
										allplayer.updateInventory();

										allplayer.sendMessage("§aVeuillez choisir une équipe !");

									}
									allplayer.kickPlayer("SkyDefenderRun arrêté !");
								}

								World worldBanner = main.getServer()
										.getWorld(main.getConfig().getString("skydefendersave.banner.world"));
								double xBanner = main.getConfig().getDouble("skydefendersave.banner.x");
								double yBanner = main.getConfig().getDouble("skydefendersave.banner.y");
								double zBanner = main.getConfig().getDouble("skydefendersave.banner.z");
								Location bannerLoc = new Location(worldBanner, xBanner, yBanner, zBanner);
								bannerLoc.getBlock().setType(Material.AIR);
								World worldTP1 = main.getServer()
										.getWorld(main.getConfig().getString("skydefendersave.tp1.world"));
								double xTP1 = main.getConfig().getDouble("skydefendersave.tp1.x");
								double yTP1 = main.getConfig().getDouble("skydefendersave.tp1.y");
								double zTP1 = main.getConfig().getDouble("skydefendersave.tp1.z");
								Location TP1Loc = new Location(worldTP1, xTP1, yTP1, zTP1);
								TP1Loc.getBlock().setType(Material.AIR);
								World worldTP2 = main.getServer()
										.getWorld(main.getConfig().getString("skydefendersave.tp2.world"));
								double xTP2 = main.getConfig().getDouble("skydefendersave.tp2.x");
								double yTP2 = main.getConfig().getDouble("skydefendersave.tp2.y");
								double zTP2 = main.getConfig().getDouble("skydefendersave.tp2.z");
								Location TP2Loc = new Location(worldTP2, xTP2, yTP2, zTP2);
								TP2Loc.getBlock().setType(Material.AIR);
								main.getGame().setGameStarted(false);
								main.getGame().setPaused(false);

								Bukkit.reload();
								main.reloadConfig();

							} else {
								sender.sendMessage("§4Il n'y a pas de SkyDefenderRun démarré !");
							}

						} else {
							p.sendMessage("§e[SkyDefenderRun] : §cVous n'avez pas les permissions !");
						}
					}

					if (args[0].equalsIgnoreCase("setdefspawn") && p.hasPermission("fr.dd06.skydefender.sdset")) {
						if (p.hasPermission("fr.dd06.skydefender.sdset") || p.hasPermission("fr.dd06.skydefender.*")) {
							main.reloadConfig();
							main.getConfig().set("skydefendersave.defspawn.world",
									p.getLocation().getWorld().getName());
							main.getConfig().set("skydefendersave.defspawn.x", p.getLocation().getX());
							main.getConfig().set("skydefendersave.defspawn.y", p.getLocation().getY() + 1);
							main.getConfig().set("skydefendersave.defspawn.z", p.getLocation().getZ());
							main.saveConfig();
							sender.sendMessage("§bVous venez de définir le point de spawn des défenseurs !");
						} else {
							p.sendMessage("§e[SkyDefenderRun] : §cVous n'avez pas les permissions !");
						}
					}

					if (args[0].equalsIgnoreCase("setattspawn")) {
						if (p.hasPermission("fr.dd06.skydefender.sdset") || p.hasPermission("fr.dd06.skydefender.*")) {
							main.reloadConfig();
							main.getConfig().set("skydefendersave.attspawn.world",
									p.getLocation().getWorld().getName());
							main.getConfig().set("skydefendersave.attspawn.x", p.getLocation().getX());
							main.getConfig().set("skydefendersave.attspawn.y", p.getLocation().getY() + 1);
							main.getConfig().set("skydefendersave.attspawn.z", p.getLocation().getZ());
							main.saveConfig();
							sender.sendMessage("§bVous venez de définir le point de spawn des attaquants !");
						} else {
							p.sendMessage("§e[SkyDefenderRun] : §cVous n'avez pas les permissions !");
						}
					}
					if (args[0].equalsIgnoreCase("setbanner")) {
						if (p.hasPermission("fr.dd06.skydefender.sdset") || p.hasPermission("fr.dd06.skydefender.*")) {
							main.reloadConfig();
							main.getConfig().set("skydefendersave.banner.world", p.getLocation().getWorld().getName());
							main.getConfig().set("skydefendersave.banner.x", p.getLocation().getX());
							main.getConfig().set("skydefendersave.banner.y", p.getLocation().getY());
							main.getConfig().set("skydefendersave.banner.z", p.getLocation().getZ());
							main.saveConfig();
							sender.sendMessage("§bVous venez de définir l'emplacement de la bannière !");
						} else {
							p.sendMessage("§e[SkyDefenderRun] : §cVous n'avez pas les permissions !");
						}

					}
					if (args[0].equalsIgnoreCase("setteleporter1") || args[0].equalsIgnoreCase("settp1")) {
						if (p.hasPermission("fr.dd06.skydefender.sdset") || p.hasPermission("fr.dd06.skydefender.*")) {
							main.reloadConfig();
							main.getConfig().set("skydefendersave.tp1.world", p.getLocation().getWorld().getName());
							main.getConfig().set("skydefendersave.tp1.x", p.getLocation().getX());
							main.getConfig().set("skydefendersave.tp1.y", p.getLocation().getY());
							main.getConfig().set("skydefendersave.tp1.z", p.getLocation().getZ());
							main.saveConfig();
							sender.sendMessage("§bVous venez de définir l'emplacement du téléporteur 1 !");
						} else {
							p.sendMessage("§e[SkyDefenderRun] : §cVous n'avez pas les permissions !");
						}

					}
					if (args[0].equalsIgnoreCase("setteleporter2") || args[0].equalsIgnoreCase("settp2")) {
						if (p.hasPermission("fr.dd06.skydefender.sdset") || p.hasPermission("fr.dd06.skydefender.*")) {
							main.reloadConfig();
							main.getConfig().set("skydefendersave.tp2.world", p.getLocation().getWorld().getName());
							main.getConfig().set("skydefendersave.tp2.x", p.getLocation().getX());
							main.getConfig().set("skydefendersave.tp2.y", p.getLocation().getY());
							main.getConfig().set("skydefendersave.tp2.z", p.getLocation().getZ());
							main.saveConfig();
							sender.sendMessage("§bVous venez de définir l'emplacement du téléporteur 2 !");
						} else {
							p.sendMessage("§e[SkyDefenderRun] : §cVous n'avez pas les permissions !");
						}

					}
					if (args[0].equalsIgnoreCase("setspawn")) {
						if (p.hasPermission("fr.dd06.skydefender.sdset") || p.hasPermission("fr.dd06.skydefender.*")) {
							main.reloadConfig();
							main.getConfig().set("skydefendersave.spawn.world", p.getLocation().getWorld().getName());
							main.getConfig().set("skydefendersave.spawn.x", p.getLocation().getX());
							main.getConfig().set("skydefendersave.spawn.y", p.getLocation().getY() + 1);
							main.getConfig().set("skydefendersave.spawn.z", p.getLocation().getZ());

							World w = Bukkit.getServer()
									.getWorld(main.getConfig().getString("skydefendersave.spawn.world"));
							double x = main.getConfig().getDouble("skydefendersave.spawn.x");
							double y = main.getConfig().getDouble("skydefendersave.spawn.y");
							double z = main.getConfig().getDouble("skydefendersave.spawn.z");

							Location spawn = new Location(w, x, y, z);
							p.getWorld().setSpawnLocation(spawn);

							main.saveConfig();
							sender.sendMessage("§bVous venez de définir l'emplacement du spawn !");
						} else {
							p.sendMessage("§e[SkyDefenderRun] : §cVous n'avez pas les permissions !");
						}

					}
					if (args[0].equalsIgnoreCase("pause")) {
						if (sender.hasPermission(new Permission("fr.dd06.skydefender.sdpause"))) {
							SaveConfig.reloadSaveConfig();
							if (!main.getGame().isPaused()) {
								if (main.getGame().isGameStarted()) {
									Bukkit.broadcastMessage(
											"§b[SkyDefenderRun] : §a Sauvegarde de la partie de SkyDefender");
									SaveGame saveGame = new SaveGame(main);
									saveGame.saveGame();
									main.getGame().setPaused(true);

									Bukkit.broadcastMessage("§b[SkyDefenderRun] : §bLe jeu est en pause !");
								} else {
									sender.sendMessage("§cLe jeu n'est pas lancé !");
								}
							} else {

								Bukkit.broadcastMessage(
										"§b[SkyDefenderRun] : §aRécupération de la sauvegarde de la partie de SkyDefenderRun");
								main.getGame().setPaused(false);
								RestoreGame restoreGame = new RestoreGame(main);
								restoreGame.restoreGame();


								Bukkit.broadcastMessage("§b[SkyDefenderRun] : §bLe jeu peut reprendre !");

							}
						} else {
							sender.sendMessage("§cVous n'avez pas les permissions !");
						}
					}

				}
				return true;
			}
		} else {
			sender.sendMessage("§4La commande doit etre faite par un joueur !");
		}

		return false;
	}

}
