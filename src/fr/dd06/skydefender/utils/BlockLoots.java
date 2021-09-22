package fr.dd06.skydefender.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class BlockLoots {

    public static ArrayList<Material> blocks = new ArrayList<>();
    public static ArrayList<EntityType> entities = new ArrayList<>();



    public static void dropLootFromBlock(Block block, Player breaker) {

        if(block.getType().equals(Material.IRON_ORE)) {
            block.getWorld().dropItemNaturally(breaker.getLocation(), new ItemStack(Material.IRON_INGOT, 2));
        }
        else if(block.getType().equals(Material.GOLD_ORE)) {
            block.getWorld().dropItemNaturally(breaker.getLocation(), new ItemStack(Material.GOLD_INGOT, 2));

        }
        else if(block.getType().equals(Material.LAPIS_ORE)) {
            block.getWorld().dropItemNaturally(breaker.getLocation(), new ItemStack(Material.LAPIS_BLOCK, 2));

        }
        else if(block.getType().equals(Material.DIAMOND_ORE)) {
            block.getWorld().dropItemNaturally(breaker.getLocation(), new ItemStack(Material.DIAMOND, 2));

        }
        else if((block.getType().equals(Material.ACACIA_LEAVES)) || (block.getType().equals(Material.BIRCH_LEAVES)) || (block.getType().equals(Material.OAK_LEAVES) ) || (block.getType().equals(Material.DARK_OAK_LEAVES)) || (block.getType().equals(Material.JUNGLE_LEAVES)) || (block.getType().equals(Material.SPRUCE_LEAVES)) ) {
            Random random = new Random();
            if(random.nextInt(15) == 1) {
                block.getWorld().dropItemNaturally(breaker.getLocation(), new ItemStack(Material.APPLE, 1));

            }

        }
        else if(block.getType().equals(Material.SAND)) {
            block.getWorld().dropItemNaturally(breaker.getLocation(), new ItemStack(Material.GLASS, 1));

        }
        else if(block.getType().equals(Material.COAL_ORE)) {
            block.getWorld().dropItemNaturally(breaker.getLocation(), new ItemStack(Material.TORCH, 4));

        }
        else if(block.getType().equals(Material.NETHER_QUARTZ_ORE)) {

            Random rnd = new Random();

            if(rnd.nextInt(2) == 1)  {
                block.getWorld().dropItemNaturally(breaker.getLocation(), new ItemStack(Material.EXPERIENCE_BOTTLE, 2));

            }
            else {
                block.getWorld().dropItemNaturally(breaker.getLocation(), new ItemStack(Material.EXPERIENCE_BOTTLE, 1));

            }

        }
        else if(block.getType().equals(Material.GRAVEL)) {
            block.getWorld().dropItemNaturally(breaker.getLocation(), new ItemStack(Material.FLINT, 1));

        }
        else if(block.getType().equals(Material.SUGAR_CANE)) {
            block.getWorld().dropItemNaturally(breaker.getLocation(), new ItemStack(Material.SUGAR_CANE, 2));

        }

    }


    public static void dropLootFromEntity(LivingEntity entity) {
        if(entity.getType().equals(EntityType.SHEEP)) {
            entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.COOKED_MUTTON));
            entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.WHITE_WOOL));
        }
        else if(entity.getType().equals(EntityType.COW)) {
            entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.COOKED_BEEF));

            Random rnd = new Random();
            int result  = rnd.nextInt(2);
            if(result == 1) {
                entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.LEATHER, 1));

            }
            else if( result == 0) {
                entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.LEATHER, 2));

            }

        }
        else if(entity.getType().equals(EntityType.CHICKEN)) {
            entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.COOKED_CHICKEN));


            Random rnd = new Random();

            if(rnd.nextInt(3) == 1)  {
                entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.FEATHER, 4));

            }
            else {
                entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.FEATHER, 2));

            }
        }
        else if(entity.getType().equals(EntityType.PIG)) {
            entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.COOKED_PORKCHOP));
        }
        else if(entity.getType().equals(EntityType.BLAZE)) {

            Random rnd = new Random();
            int result = rnd.nextInt(5);
            if(result == 1) {
                entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.BLAZE_ROD, 2));

            }
            else if(result == 2 ||result == 3) {
                entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.BLAZE_ROD, 1));

            }
        }

    }
    static {
        blocks.add(Material.IRON_ORE);
        blocks.add(Material.GOLD_ORE);
        blocks.add(Material.LAPIS_ORE);
        blocks.add(Material.DIAMOND_ORE);
        blocks.add(Material.ACACIA_LEAVES);
        blocks.add(Material.BIRCH_LEAVES);
        blocks.add(Material.SPRUCE_LEAVES);
        blocks.add(Material.JUNGLE_LEAVES);
        blocks.add(Material.OAK_LEAVES);
        blocks.add(Material.DARK_OAK_LEAVES);
        blocks.add(Material.SAND);
        blocks.add(Material.COAL_ORE);
        blocks.add(Material.NETHER_QUARTZ_ORE);
        blocks.add(Material.GRAVEL);
        blocks.add(Material.SUGAR_CANE);


        entities.add(EntityType.SHEEP);
        entities.add(EntityType.COW);
        entities.add(EntityType.CHICKEN);
        entities.add(EntityType.PIG);
        entities.add(EntityType.BLAZE);


    }
}
