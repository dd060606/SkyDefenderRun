package fr.dd06.skydefender.utils;

import org.bukkit.Location;

public class BlockLocationChecker {
	public static boolean blockLocationCheck(Location location, Location locationToCheck) {
		if (location.getBlockX() == locationToCheck.getBlockX() && location.getBlockY() == locationToCheck.getBlockY()
				&& location.getBlockZ() == locationToCheck.getBlockZ() && location.getWorld().equals(locationToCheck.getWorld())) {
			return true;
		} else {
			return false;
		}

	}
}
