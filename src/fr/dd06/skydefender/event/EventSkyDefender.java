package fr.dd06.skydefender.event;


import fr.dd06.skydefender.SkyDefenderRun;
import fr.dd06.skydefender.game.BannerAttack;
import fr.dd06.skydefender.game.SkyDefenderEnd;
import fr.dd06.skydefender.scoreboards.CustomScoreBoard;
import fr.dd06.skydefender.game.AutoStart;
import fr.dd06.skydefender.utils.BlockLocationChecker;
import fr.dd06.skydefender.utils.BlockLoots;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class EventSkyDefender implements Listener {
	private SkyDefenderRun main;

	public EventSkyDefender(SkyDefenderRun skyDefenderRun) {
		this.main = skyDefenderRun;
	}

	public EventSkyDefender() {

	}

	public static HashMap<UUID, Integer> kills = new HashMap<>();

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if (SkyDefenderRun.getGamestarted() == true) {
			if (e.getEntityType() == EntityType.PLAYER) {

				Player player = e.getEntity();

				e.setDeathMessage("§1" + player.getName() + "§9 est mort !");
				if(main.attaquants.contains(player.getUniqueId())) {
					main.attaquants.remove(player.getUniqueId());
				}
				else if(main.defenseurs.contains(player.getUniqueId())) {
					main.defenseurs.remove(player.getUniqueId());
				}
				if(!main.spectateurs.contains(player.getUniqueId())) {
					main.spectateurs.add(player.getUniqueId());
				}
				SkyDefenderRun.getInstance().players.remove(player.getUniqueId());

				World w = player.getWorld();
				player.setGameMode(GameMode.SPECTATOR);
				w.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 10, 1);

				if(main.attaquants.size() == 0) {
					SkyDefenderEnd end = new SkyDefenderEnd(main);

					int num = 0;
					while(main.defenseurs.get(num) != null)   {

						if(Bukkit.getPlayer(main.defenseurs.get(num)) != null) {
							end.winTheGame(Bukkit.getPlayer(main.defenseurs.get(num)));
							break;
						}
						num++;
					}

				}

			}

		}

	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {

		if (SkyDefenderRun.getGamestarted() == false) {

			Player player = e.getPlayer();
			player.getInventory().clear();

			ItemStack teamchooser = new ItemStack(Material.BANNER, 1, (byte) 15);
			ItemMeta teamchoosermeta = teamchooser.getItemMeta();
			teamchoosermeta.addEnchant(Enchantment.DAMAGE_ALL, 0, true);
			teamchoosermeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			teamchoosermeta.setDisplayName("§eChoisir une équipe");
			teamchoosermeta.setLore(Arrays.asList("§cVeuillez choisir une équipe"));

			teamchooser.setItemMeta(teamchoosermeta);

			player.getInventory().setItem(4, teamchooser);
			player.updateInventory();
			World w = Bukkit.getServer().getWorld(main.getConfig().getString("skydefendersave.spawn.world"));
			double x = main.getConfig().getDouble("skydefendersave.spawn.x");
			double y = main.getConfig().getDouble("skydefendersave.spawn.y");
			double z = main.getConfig().getDouble("skydefendersave.spawn.z");
			player.teleport(new Location(w, x, y, z));

		} else {
			Player player = e.getPlayer();
			World w = Bukkit.getServer().getWorld(main.getConfig().getString("skydefendersave.spawn.world"));
			double x = main.getConfig().getDouble("skydefendersave.spawn.x");
			double y = main.getConfig().getDouble("skydefendersave.spawn.y");
			double z = main.getConfig().getDouble("skydefendersave.spawn.z");
			player.teleport(new Location(w, x, y, z));

		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		if (SkyDefenderRun.getGamestarted() == true) {
			if (main.players.contains(player.getUniqueId())) {

				event.setJoinMessage("§e[SkyDefenderRun] : §a" + player.getName() + " a rejoint le jeu !");

				if(main.defenseurs.contains(player.getUniqueId())) {
					player.setPlayerListName(ChatColor.AQUA +"[Défenseur] " +  player.getName());

				}
				else if (main.attaquants.contains(player.getUniqueId())) {
					player.setPlayerListName(ChatColor.RED +"[Attaquant] " +  player.getName());

				}
				CustomScoreBoard board = new CustomScoreBoard(player, "§bSkyDefender");
				board.destroy();
				board.create();
				main.boards.put(player.getUniqueId(), board);
				main.updateScoreboards(player.getUniqueId());
			} else {

				event.setJoinMessage(
						"§e[SkyDefenderRun] : §a" + player.getName() + " a rejoint le jeu en tant que spectateur !");
				if (!main.spectateurs.contains(player.getUniqueId())) {
					main.spectateurs.add(player.getUniqueId());
				}

				CustomScoreBoard specboard = new CustomScoreBoard(player, "§bSkyDefender");
				specboard.destroy();
				specboard.create();
				main.specboards.put(player.getUniqueId(), specboard);
				main.updateSpectatorsBoards(player.getUniqueId());

				player.setPlayerListName(ChatColor.LIGHT_PURPLE +"[Spectateur] " +  player.getName());

				player.setGameMode(GameMode.SPECTATOR);
				World w = Bukkit.getServer().getWorld(main.getConfig().getString("skydefendersave.spawn.world"));
				double x = main.getConfig().getDouble("skydefendersave.spawn.x");
				double y = main.getConfig().getDouble("skydefendersave.spawn.y");
				double z = main.getConfig().getDouble("skydefendersave.spawn.z");
				player.teleport(new Location(w, x, y, z));
				return;
			}

		} else {

			if (event.getPlayer() instanceof Player) {
				Player p = event.getPlayer();
				if (Bukkit.getOnlinePlayers().size() > main.getConfig()
						.getInt("skydefenderconfig.starting.maxplayers")) {
					p.kickPlayer("Server is full !");
					return;
				}
			}

			if (main.isPaused() == false) {

				EventSkyDefender.kills.put(player.getUniqueId(), 0);

				player.setGameMode(GameMode.ADVENTURE);
				player.getInventory().clear();
				player.setFoodLevel(20);
				player.setHealth(20);

				if (main.getConfig().getConfigurationSection("skydefenderconfig.teamchooser")
						.getBoolean("enabled") == true) {
					ItemStack teamchooser = new ItemStack(Material.BANNER, 1, (byte) 15);
					ItemMeta teamchoosermeta = teamchooser.getItemMeta();
					teamchoosermeta.addEnchant(Enchantment.DAMAGE_ALL, 0, true);
					teamchoosermeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					teamchoosermeta.setDisplayName("§eChoisir une équipe");

					teamchooser.setItemMeta(teamchoosermeta);

					player.getInventory().setItem(4, teamchooser);
					player.updateInventory();

					player.sendMessage("§aVeuillez choisir une équipe !");
				}
				event.setJoinMessage("§e[SkyDefenderRun] : §a" + player.getName() + " a rejoint la partie ! ("
						+ Bukkit.getOnlinePlayers().size() + "/"
						+ main.getConfig().getInt("skydefenderconfig.starting.maxplayers") + ")");

				if (main.getConfig().getConfigurationSection("skydefenderconfig.autostart")
						.getBoolean("enabled") == true) {
					if (Bukkit.getOnlinePlayers().size() >= main.getConfig()
							.getConfigurationSection("skydefenderconfig.starting").getInt("minplayers")) {
						if (main.defenseurs.size() >= 1 && main.attaquants.size() >= 1) {
							AutoStart autostart = new AutoStart(main);
							autostart.runTaskTimer(main, 0, 20);
						}

					}
				}

				double x, y, z;
				String world = main.getConfig().getString("skydefendersave.spawn.world");
				x = main.getConfig().getDouble("skydefendersave.spawn.x");
				y = main.getConfig().getDouble("skydefendersave.spawn.y");
				z = main.getConfig().getDouble("skydefendersave.spawn.z");
				Location spawn = new Location(Bukkit.getWorld(world), x, y, z);
				player.teleport(spawn);

			}

		}
	}

	@EventHandler
	public void OnQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (SkyDefenderRun.getGamestarted() == false) {
			if (SkyDefenderRun.getInstance().players.contains(player.getUniqueId()))
				SkyDefenderRun.getInstance().players.remove(player.getUniqueId());
			if (main.attaquants.contains(player.getUniqueId())) {
				main.attaquants.remove(player.getUniqueId());
			}
			if (main.defenseurs.contains(player.getUniqueId())) {
				main.defenseurs.remove(player.getUniqueId());
			}
			if (main.spectateurs.contains(player.getUniqueId())) {
				main.spectateurs.remove(player.getUniqueId());
			}
			event.setQuitMessage("§e[SkyDefenderRun] : §c" + player.getName() + " a quitté la partie ! ("
					+ (Bukkit.getOnlinePlayers().size() - 1) + "/"
					+ main.getConfig().getInt("skydefenderconfig.starting.maxplayers") + ")");

		} else {
			if (SkyDefenderRun.getInstance().players.contains(player.getUniqueId())) {

				event.setQuitMessage("§e[SkyDefenderRun] : §c" + player.getName() + " a quitté le SkyDefender !");

			}
		}

	}

	@EventHandler
	public void onWalk(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		Location ploc = e.getPlayer().getLocation();

		if (SkyDefenderRun.getGamestarted() == true) {
			if (ploc.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.REDSTONE_BLOCK)) {
				if (main.defenseurs.contains(p.getUniqueId())) {
					main.reloadConfig();
					Location locTP1 = new Location(
							Bukkit.getWorld(main.getConfig().getString("skydefendersave.tp1.world")),
							main.getConfig().getDouble("skydefendersave.tp1.x"),
							main.getConfig().getDouble("skydefendersave.tp1.y"),
							main.getConfig().getDouble("skydefendersave.tp1.z"));
					Location locTP2 = new Location(
							Bukkit.getWorld(main.getConfig().getString("skydefendersave.tp2.world")),
							main.getConfig().getDouble("skydefendersave.tp2.x"),
							main.getConfig().getDouble("skydefendersave.tp2.y"),
							main.getConfig().getDouble("skydefendersave.tp2.z"));

					Location playerLocationModified = new Location(ploc.getWorld(), ploc.getX(), ploc.getY() - 1,
							ploc.getZ());

					if (BlockLocationChecker.blockLocationCheck(playerLocationModified, locTP1)) {

						Location locTP2modified = new Location(locTP2.getWorld(), locTP2.getX() + 1.0,
								locTP2.getY() + 1.0, locTP2.getZ());
						p.teleport(locTP2modified);
						p.sendMessage("§aVous avez été téléporté(e) !");
					}
					if (BlockLocationChecker.blockLocationCheck(playerLocationModified, locTP2)) {

						Location locTP1modified = new Location(locTP1.getWorld(), locTP1.getX() + 1.0,
								locTP1.getY() + 1.0, locTP1.getZ());
						p.teleport(locTP1modified);
						p.sendMessage("§aVous avez été téléporté(e) !");
					}

				} else {
					p.sendMessage("§cSeulement les défenseurs peuvent utiliser le téléporteur !");

				}
			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action action = event.getAction();
		ItemStack it = event.getItem();
		if (SkyDefenderRun.getGamestarted() == false) {
			if (main.getConfig().getConfigurationSection("skydefenderconfig.teamchooser")
					.getBoolean("enabled") == true) {
				if (it == null)
					return;

				if (it.getType() == Material.BANNER && it.hasItemMeta()
						&& it.getItemMeta().getDisplayName().equals("§eChoisir une équipe")) {
					if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
						Inventory teamchoosergui = Bukkit.createInventory(null, 18, "§8Choisir une équipe");

						teamchoosergui.setItem(3, getItem(Material.BANNER, "§9Défenseur", (byte) 4));
						teamchoosergui.setItem(4, getItem(Material.BANNER, "§cAttaquant", (byte) 1));
						teamchoosergui.setItem(5, getItem(Material.BANNER, "§fAléatoire", (byte) 15));
						teamchoosergui.setItem(13, getItem(Material.WOOL, "§4Fermer", (byte) 14));
						teamchoosergui.setItem(8, getItem(Material.BANNER, "§5Spectateur", (byte) 5));

						player.openInventory(teamchoosergui);

					}
				}

			}
		}
		if(main.isPaused() ==true) {
			event.setCancelled(true);
		}

	}

	@EventHandler
	public void ondrop(PlayerDropItemEvent e) {

		if (SkyDefenderRun.getGamestarted() == false || main.isPaused() == true) {
			e.setCancelled(true);

		}

	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Inventory inv = e.getInventory();
		Player player = (Player) e.getWhoClicked();
		ItemStack current = e.getCurrentItem();

		if (current == null)
			return;

		if (current.getType() == Material.BANNER && SkyDefenderRun.getGamestarted() == false) {
			e.setCancelled(true);
		}

		if (inv.getName().equals("§8Choisir une équipe")) {
			if (current.getType() == Material.WOOL && SkyDefenderRun.getGamestarted() == false) {
				e.setCancelled(true);
			}

			if (current.getType() == Material.WOOL && current.getItemMeta().getDisplayName().equals("§4Fermer")) {
				player.closeInventory();
				player.updateInventory();
			}
			if (current.getType() == Material.BANNER && current.getItemMeta().getDisplayName().equals("§9Défenseur")) {

				player.closeInventory();
				player.updateInventory();
				if (main.defenseurs.contains(player.getUniqueId())) {
					player.sendMessage("§cVous êtes déjà dans cette équipe !");
					return;
				}
				if (main.defenseurs.size() >= main.getConfig()
						.getInt("skydefenderconfig.ingameconfig.teams.defenseurs.maxplayers")) {
					player.sendMessage("§cL'équipe est complète");
					return;
				}
				if (main.attaquants.contains(player.getUniqueId())) {
					main.attaquants.remove(player.getUniqueId());
				}
				if (main.spectateurs.contains(player.getUniqueId())) {
					main.spectateurs.remove(player.getUniqueId());
				}
				main.defenseurs.add(player.getUniqueId());
				player.sendMessage("§aVous avez rejoint l'équipe des §9Défenseurs !");

			}
			if (current.getType() == Material.BANNER && current.getItemMeta().getDisplayName().equals("§cAttaquant")) {

				player.closeInventory();
				player.updateInventory();
				if (main.attaquants.contains(player.getUniqueId())) {
					player.sendMessage("§cVous êtes déjà dans cette équipe !");
					return;
				}
				if (main.attaquants.size() >= main.getConfig()
						.getInt("skydefenderconfig.ingameconfig.teams.attaquants.maxplayers")) {
					player.sendMessage("§cL'équipe est complète");
					return;
				}
				if (main.defenseurs.contains(player.getUniqueId())) {
					main.defenseurs.remove(player.getUniqueId());
				}
				if (main.spectateurs.contains(player.getUniqueId())) {
					main.spectateurs.remove(player.getUniqueId());
				}
				main.attaquants.add(player.getUniqueId());
				player.sendMessage("§aVous avez rejoint l'équipe des §cAttaquants !");

			}
			if (current.getType() == Material.BANNER && current.getItemMeta().getDisplayName().equals("§fAléatoire")) {

				player.closeInventory();
				player.updateInventory();
				if (main.spectateurs.contains(player.getUniqueId())) {
					main.spectateurs.remove(player.getUniqueId());
				}
				if (main.attaquants.contains(player.getUniqueId())) {
					main.attaquants.remove(player.getUniqueId());
				}
				if (main.defenseurs.contains(player.getUniqueId())) {
					main.defenseurs.remove(player.getUniqueId());
				}

				Random randomTeam = new Random();
				int result = randomTeam.nextInt(100);
				if (result >= 50) {
					if (main.defenseurs.size() >= main.getConfig()
							.getInt("skydefenderconfig.ingameconfig.teams.defenseurs.maxplayers")) {
						main.attaquants.add(player.getUniqueId());
					} else {
						main.defenseurs.add(player.getUniqueId());
					}
				} else {
					if (main.attaquants.size() >= main.getConfig()
							.getInt("skydefenderconfig.ingameconfig.teams.attaquants.maxplayers")) {
						main.defenseurs.add(player.getUniqueId());
					} else {
						main.attaquants.add(player.getUniqueId());
					}
				}

				player.sendMessage("§aVotre équipe sera aléatoire !");

			}

			if (current.getType() == Material.BANNER && current.getItemMeta().getDisplayName().equals("§5Spectateur")) {

				player.closeInventory();
				player.updateInventory();
				if (main.spectateurs.contains(player.getUniqueId())) {
					player.sendMessage("§cVous êtes déjà dans cette équipe !");
					return;
				}

				if (main.defenseurs.contains(player.getUniqueId())) {
					main.defenseurs.remove(player.getUniqueId());
				}
				if (main.attaquants.contains(player.getUniqueId())) {
					main.attaquants.remove(player.getUniqueId());
				}
				main.spectateurs.add(player.getUniqueId());
				player.sendMessage("§aVous avez rejoint l'équipe des §5Spectateurs !");

			}
		}
	}

	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event) {

		if (SkyDefenderRun.getGamestarted() == false || main.isPaused() == true) {
			Player player = event.getPlayer();
			if (player.getInventory().getItemInHand().getType() == Material.BANNER)
				event.setCancelled(true);
			if (player.getGameMode().equals(GameMode.CREATIVE)) {
				if (event.getBlock().getType() == Material.BANNER) {
					event.setCancelled(true);
				}
				event.setCancelled(false);
			} else {
				event.setCancelled(true);
			}

		}

	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (SkyDefenderRun.getGamestarted() == false || main.isPaused() == true) {
			Entity victime = e.getEntity();
			if (victime instanceof Player) {

				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPvP(EntityDamageByEntityEvent e) {

		if (SkyDefenderRun.getGamestarted() == false || main.isPaused() == true) {
			Entity victime = e.getEntity();
			Entity damager = e.getDamager();
			if (victime instanceof Player) {
				e.setCancelled(true);
			}
			if (damager instanceof Player) {
				e.setCancelled(true);
			}
		} else {
			Entity victime = e.getEntity();
			Entity damager = e.getDamager();

			if (SkyDefenderRun.getPvp() == true) {
				if (victime instanceof Player) {
					Player player = (Player) victime;

					if (damager instanceof Player) {
						Player pDamager = (Player) damager;
						if (main.defenseurs.contains(pDamager.getUniqueId())
								&& main.defenseurs.contains(victime.getUniqueId())) {
							pDamager.sendMessage("§cVous ne pouvez pas frapper un autre défenseur !");
							e.setCancelled(true);
						}
					}

					if (player.getHealth() <= e.getDamage()) {
						if (damager instanceof Player) {
							Player killer = (Player) damager;

							if (!kills.containsKey(killer.getUniqueId())) {
								kills.put(killer.getUniqueId(), 0);
							}

							kills.put(killer.getUniqueId(), kills.get(killer.getUniqueId()) + 1);

						}
						if (damager instanceof Arrow) {
							Arrow arrow = (Arrow) damager;
							if (arrow.getShooter() instanceof Player) {
								Player killer = (Player) arrow.getShooter();
								if (!kills.containsKey(killer.getUniqueId())) {
									kills.put(killer.getUniqueId(), 0);
								}

								kills.put(killer.getUniqueId(), kills.get(killer.getUniqueId()) + 1);

							}
						}
						e.getDamager().sendMessage("§bVous avez éliminé(e) §c" + victime.getName());
					}
				}
			} else {
				if(victime instanceof Player) {
					if(damager instanceof Player) {
						e.setCancelled(true);
						damager.sendMessage("§cLe PvP n'est pas encore activé !");
					}

				}

			}
		}

	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Player breaker = event.getPlayer();
		Block blockbreaked = event.getBlock();
		Location locBlockBreaked = blockbreaked.getLocation();

		if (SkyDefenderRun.getGamestarted()) {
			if(main.isPaused()) {
				event.setCancelled(true);
			}
			if (blockbreaked.getType().equals(Material.STANDING_BANNER)) {
				Location bannerLoc = new Location(
						Bukkit.getWorld(main.getConfig().getString("skydefendersave.banner.world")),
						main.getConfig().getDouble("skydefendersave.banner.x"),
						main.getConfig().getDouble("skydefendersave.banner.y"),
						main.getConfig().getDouble("skydefendersave.banner.z"));

				if (BlockLocationChecker.blockLocationCheck(locBlockBreaked, bannerLoc)) {
					if (main.defenseurs.contains(breaker.getUniqueId())) {
						breaker.sendMessage("§cSeulement les attaquants peuvent détruire la bannière !");
						event.setCancelled(true);

					} else if (main.attaquants.contains(breaker.getUniqueId())) {
						if (main.defenseurs.size() <= 0) {
							breaker.sendMessage(
									"§e[SkyDefenderRun] : §a" + breaker.getName() + " vient de détruire la bannière !");
							SkyDefenderEnd skydefenderEnd = new SkyDefenderEnd(main);
							skydefenderEnd.winTheGame(breaker);

						} else {

							if(BannerAttack.attacking) {
								if(BannerAttack.minutes == SkyDefenderRun.getInstance().getMinutesBannerCooldown()) {
									breaker.sendMessage(
											"§e[SkyDefenderRun] : §a" + breaker.getName() + " vient de détruire la bannière !");
									SkyDefenderEnd skydefenderEnd = new SkyDefenderEnd(main);
									skydefenderEnd.winTheGame(breaker);
									event.setCancelled(false);
								}
								else {
									breaker.sendMessage("§cLe compteur est à " + BannerAttack.minutes + " mins et " + BannerAttack.secondes + " secs sur " + SkyDefenderRun.getInstance().getMinutesBannerCooldown() +" minutes");
									event.setCancelled(true);

								}
							}
							else {

								if(SkyDefenderRun.isAssaultEnabled()) {
									BannerAttack attack = new BannerAttack(main);
									attack.startAttack();
									event.setCancelled(true);

								}
								else {
									breaker.sendMessage("§cLes assauts ne sont pas encore activés");
									event.setCancelled(true);


								}

							}
						}
					} else {
						event.setCancelled(false);
					}
				}
			}
			if (blockbreaked.getType().equals(Material.REDSTONE_BLOCK)) {
				Location tp1Loc = new Location(Bukkit.getWorld(main.getConfig().getString("skydefendersave.tp1.world")),
						main.getConfig().getDouble("skydefendersave.tp1.x"),
						main.getConfig().getDouble("skydefendersave.tp1.y"),
						main.getConfig().getDouble("skydefendersave.tp1.z"));
				Location tp2Loc = new Location(Bukkit.getWorld(main.getConfig().getString("skydefendersave.tp2.world")),
						main.getConfig().getDouble("skydefendersave.tp2.x"),
						main.getConfig().getDouble("skydefendersave.tp2.y"),
						main.getConfig().getDouble("skydefendersave.tp2.z"));

				if (BlockLocationChecker.blockLocationCheck(locBlockBreaked, tp1Loc)
						|| BlockLocationChecker.blockLocationCheck(locBlockBreaked, tp2Loc)) {
					breaker.sendMessage("§cVous ne pouvez pas casser le téléporteur !");
					event.setCancelled(true);
				} else {
					event.setCancelled(false);
				}

			}

			if(blockbreaked.getType().equals(Material.LOG)) {
				Location blockLoc = blockbreaked.getLocation();
				World world = blockbreaked.getWorld();
				BlockLoots.breakTree(blockbreaked);


			}
			if(BlockLoots.blocks.contains(blockbreaked.getType())) {

				event.setDropItems(false);
				BlockLoots.dropLootFromBlock(blockbreaked);
			}
		} else {
			
			if(main.isPaused() == true ) {
				event.setCancelled(true);
			}
			if (breaker.getGameMode().equals(GameMode.CREATIVE)) {
				event.setCancelled(false);
			} else {
				event.setCancelled(true);
			}
		}
	}

	/*
	@EventHandler
	public void onCraft(CraftItemEvent e) {
		HumanEntity p = e.getWhoClicked();
		ItemStack item = e.getCurrentItem();
		if (p instanceof Player) {
			Player player = (Player) p;
			if (SkyDefenderRun.getGamestarted() == true) {
				if (item.getType().equals(Material.BANNER)) {
					e.setCancelled(true);
				}
				if (item.getType().equals(Material.REDSTONE_BLOCK)) {
					e.setCancelled(true);
				}
			}

		}

	}

	 */

	@EventHandler
	public void onEntityKill(EntityDeathEvent event) {

		if(SkyDefenderRun.getGamestarted()) {
			if(BlockLoots.entities.contains(event.getEntityType())) {
				event.getDrops().clear();
				BlockLoots.dropLootFromEntity(event.getEntity());


			}
		}


	}

	public ItemStack getItem(Material material, String customName, byte num) {
		ItemStack it = new ItemStack(material, 1, num);
		ItemMeta itM = it.getItemMeta();
		if (customName != null)
			itM.setDisplayName(customName);
		it.setItemMeta(itM);

		it.setItemMeta(itM);
		return it;

	}

}
