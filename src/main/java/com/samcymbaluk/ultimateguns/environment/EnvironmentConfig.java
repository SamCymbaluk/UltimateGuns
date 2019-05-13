package com.samcymbaluk.ultimateguns.environment;

import com.samcymbaluk.ultimateguns.config.util.ConfigParticle;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class EnvironmentConfig {

    private BlockProperties defaultBlockProperties = new BlockProperties(
            500D,
            0.4D,
            true,
            500D
    );

    private EntityProperties defaultEntityProperties = new EntityProperties(
            10D,
            0.1D,
            coloredDustParticle(3, Color.RED, 1)
    );

    private Map<Material, BlockProperties> blockProperties = new LinkedHashMap<Material, BlockProperties>(){{
        put(Material.WATER, new BlockProperties(25D, null, false, null));
        put(Material.LAVA, new BlockProperties(50D, null, false, null));
        put(Material.GLASS, new BlockProperties(1D, 0.5, true, 5D));
        put(Material.GLASS_PANE, new BlockProperties(1D, 0.5, true, 5D));
        put(Material.FLOWER_POT, new BlockProperties(5D, 0.5, true, 5D));
        put(Material.GRASS_BLOCK, new BlockProperties(200D, null, null, 200D));
        put(Material.DIRT, new BlockProperties(200D, null, null, 200D));
        put(Material.GRASS_PATH, new BlockProperties(200D, null, null, 200D));
        put(Material.PODZOL, new BlockProperties(200D, null, null, 200D));
        put(Material.SAND, new BlockProperties(400D, null, null, 400D));
        put(Material.RED_SAND, new BlockProperties(400D, null, null, 400D));
        put(Material.SOUL_SAND, new BlockProperties(400D, null, null, 400D));
        put(Material.GRAVEL, new BlockProperties(400D, null, null, 400D));
        put(Material.ACACIA_LOG, new BlockProperties(25D, null, null, 200D));
        put(Material.BIRCH_LOG, new BlockProperties(25D, null, null, 200D));
        put(Material.DARK_OAK_LOG, new BlockProperties(25D, null, null, 200D));
        put(Material.JUNGLE_LOG, new BlockProperties(25D, null, null, 200D));
        put(Material.OAK_LOG, new BlockProperties(25D, null, null, 200D));
        put(Material.SPRUCE_LOG, new BlockProperties(25D, null, null, 200D));
        put(Material.STRIPPED_ACACIA_LOG, new BlockProperties(25D, null, null, 200D));
        put(Material.STRIPPED_BIRCH_LOG, new BlockProperties(25D, null, null, 200D));
        put(Material.STRIPPED_DARK_OAK_LOG, new BlockProperties(25D, null, null, 200D));
        put(Material.STRIPPED_JUNGLE_LOG, new BlockProperties(25D, null, null, 200D));
        put(Material.STRIPPED_OAK_LOG, new BlockProperties(25D, null, null, 200D));
        put(Material.STRIPPED_SPRUCE_LOG, new BlockProperties(25D, null, null, 200D));
        put(Material.ACACIA_WOOD, new BlockProperties(25D, null, null, 200D));
        put(Material.BIRCH_WOOD, new BlockProperties(25D, null, null, 200D));
        put(Material.DARK_OAK_WOOD, new BlockProperties(25D, null, null, 200D));
        put(Material.JUNGLE_WOOD, new BlockProperties(25D, null, null, 200D));
        put(Material.OAK_WOOD, new BlockProperties(25D, null, null, 200D));
        put(Material.SPRUCE_WOOD, new BlockProperties(25D, null, null, 200D));
        put(Material.SPRUCE_LEAVES, new BlockProperties(1D, 0.2, null, 50D));
        put(Material.ACACIA_LEAVES, new BlockProperties(1D, 0.2, null, 50D));
        put(Material.OAK_LEAVES, new BlockProperties(1D, 0.2, null, 50D));
        put(Material.JUNGLE_LEAVES, new BlockProperties(1D, 0.2, null, 50D));
        put(Material.DARK_OAK_LEAVES, new BlockProperties(1D, 0.2, null, 50D));
        put(Material.BIRCH_LEAVES, new BlockProperties(1D, 0.2, null, 50D));
        put(Material.ACACIA_PLANKS, new BlockProperties(20D, null, null, 200D));
        put(Material.BIRCH_PLANKS, new BlockProperties(20D, null, null, 200D));
        put(Material.DARK_OAK_PLANKS, new BlockProperties(20D, null, null, 200D));
        put(Material.JUNGLE_PLANKS, new BlockProperties(25D, null, null, 200D));
        put(Material.OAK_PLANKS, new BlockProperties(25D, null, null, 200D));
        put(Material.SPRUCE_PLANKS, new BlockProperties(25D, null, null, 200D));
        put(Material.ACACIA_STAIRS, new BlockProperties(20D, null, null, 200D));
        put(Material.BIRCH_STAIRS, new BlockProperties(20D, null, null, 200D));
        put(Material.DARK_OAK_STAIRS, new BlockProperties(20D, null, null, 200D));
        put(Material.JUNGLE_STAIRS, new BlockProperties(25D, null, null, 200D));
        put(Material.OAK_STAIRS, new BlockProperties(25D, null, null, 200D));
        put(Material.SPRUCE_STAIRS, new BlockProperties(25D, null, null, 200D));
        put(Material.ACACIA_DOOR, new BlockProperties(20D, null, null, 200D));
        put(Material.BIRCH_DOOR, new BlockProperties(20D, null, null, 200D));
        put(Material.DARK_OAK_DOOR, new BlockProperties(20D, null, null, 200D));
        put(Material.JUNGLE_DOOR, new BlockProperties(25D, null, null, 200D));
        put(Material.OAK_DOOR, new BlockProperties(25D, null, null, 200D));
        put(Material.SPRUCE_DOOR, new BlockProperties(25D, null, null, 200D));
        put(Material.ACACIA_FENCE, new BlockProperties(20D, null, null, 200D));
        put(Material.BIRCH_FENCE, new BlockProperties(20D, null, null, 200D));
        put(Material.DARK_OAK_FENCE, new BlockProperties(20D, null, null, 200D));
        put(Material.JUNGLE_FENCE, new BlockProperties(25D, null, null, 200D));
        put(Material.OAK_FENCE, new BlockProperties(25D, null, null, 200D));
        put(Material.SPRUCE_FENCE, new BlockProperties(25D, null, null, 200D));
        put(Material.ACACIA_FENCE_GATE, new BlockProperties(20D, null, null, 200D));
        put(Material.BIRCH_FENCE_GATE, new BlockProperties(20D, null, null, 200D));
        put(Material.DARK_OAK_FENCE_GATE, new BlockProperties(20D, null, null, 200D));
        put(Material.JUNGLE_FENCE_GATE, new BlockProperties(25D, null, null, 200D));
        put(Material.OAK_FENCE_GATE, new BlockProperties(25D, null, null, 200D));
        put(Material.SPRUCE_FENCE_GATE, new BlockProperties(25D, null, null, 200D));
        put(Material.BEDROCK, new BlockProperties(100000000D, null, false, 100000000D));
        put(Material.SPONGE, new BlockProperties(10D, null, null, 25D));
        put(Material.WET_SPONGE, new BlockProperties(15D, null, null, 50D));
        put(Material.WHITE_WOOL, new BlockProperties(15D, null, null, 50D));
        put(Material.BLACK_WOOL, new BlockProperties(15D, null, null, 50D));
        put(Material.BLUE_WOOL, new BlockProperties(15D, null, null, 50D));
        put(Material.BROWN_WOOL, new BlockProperties(15D, null, null, 50D));
        put(Material.CYAN_WOOL, new BlockProperties(15D, null, null, 50D));
        put(Material.GRAY_WOOL, new BlockProperties(15D, null, null, 50D));
        put(Material.GREEN_WOOL, new BlockProperties(15D, null, null, 50D));
        put(Material.LIGHT_BLUE_WOOL, new BlockProperties(15D, null, null, 50D));
        put(Material.LIGHT_GRAY_WOOL, new BlockProperties(15D, null, null, 50D));
        put(Material.LIME_WOOL, new BlockProperties(15D, null, null, 50D));
        put(Material.MAGENTA_WOOL, new BlockProperties(15D, null, null, 50D));
        put(Material.ORANGE_WOOL, new BlockProperties(15D, null, null, 50D));
        put(Material.PINK_WOOL, new BlockProperties(15D, null, null, 50D));
        put(Material.PURPLE_WOOL, new BlockProperties(15D, null, null, 50D));
        put(Material.RED_WOOL, new BlockProperties(15D, null, null, 50D));
        put(Material.YELLOW_WOOL, new BlockProperties(15D, null, null, 50D));
        put(Material.IRON_DOOR, new BlockProperties(100D, null, null, 500D));
        put(Material.IRON_BARS, new BlockProperties(10D, null, null, 500D));
        put(Material.IRON_BLOCK, new BlockProperties(800D, null, null, 800D));
        put(Material.GOLD_BLOCK, new BlockProperties(800D, null, null, 800D));
        put(Material.DIAMOND_BLOCK, new BlockProperties(800D, null, null, 800D));
        put(Material.EMERALD_BLOCK, new BlockProperties(800D, null, null, 800D));
        put(Material.OBSIDIAN, new BlockProperties(10000D, null, false, 10000D));
        put(Material.ICE, new BlockProperties(5D, null, false, 10D));
        put(Material.PACKED_ICE, new BlockProperties(15D, null, false, 25D));
        put(Material.BLUE_ICE, new BlockProperties(15D, null, false, 25D));
        put(Material.SNOW, new BlockProperties(15D, null, false, 25D));
        put(Material.GLOWSTONE, new BlockProperties(1D, null, true, 5D));
        put(Material.HAY_BLOCK, new BlockProperties(5D, null, null, 100D));
        put(Material.GRAY_STAINED_GLASS, new BlockProperties(1D, null, true, 5D));
        put(Material.BLACK_STAINED_GLASS, new BlockProperties(1D, null, true, 5D));
        put(Material.BLUE_STAINED_GLASS, new BlockProperties(1D, null, true, 5D));
        put(Material.BROWN_STAINED_GLASS, new BlockProperties(1D, null, true, 5D));
        put(Material.CYAN_STAINED_GLASS, new BlockProperties(1D, null, true, 5D));
        put(Material.GREEN_STAINED_GLASS, new BlockProperties(1D, null, true, 5D));
        put(Material.LIGHT_BLUE_STAINED_GLASS, new BlockProperties(1D, null, true, 5D));
        put(Material.LIGHT_GRAY_STAINED_GLASS, new BlockProperties(1D, null, true, 5D));
        put(Material.LIME_STAINED_GLASS, new BlockProperties(1D, null, true, 5D));
        put(Material.MAGENTA_STAINED_GLASS, new BlockProperties(1D, null, true, 5D));
        put(Material.ORANGE_STAINED_GLASS, new BlockProperties(1D, null, true, 5D));
        put(Material.PINK_STAINED_GLASS, new BlockProperties(1D, null, true, 5D));
        put(Material.PURPLE_STAINED_GLASS, new BlockProperties(1D, null, true, 5D));
        put(Material.RED_STAINED_GLASS, new BlockProperties(1D, null, true, 5D));
        put(Material.WHITE_STAINED_GLASS, new BlockProperties(1D, null, true, 5D));
        put(Material.YELLOW_STAINED_GLASS, new BlockProperties(1D, null, true, 5D));
        put(Material.GRAY_STAINED_GLASS_PANE, new BlockProperties(1D, null, true, 5D));
        put(Material.BLACK_STAINED_GLASS_PANE, new BlockProperties(1D, null, true, 5D));
        put(Material.BLUE_STAINED_GLASS_PANE, new BlockProperties(1D, null, true, 5D));
        put(Material.BROWN_STAINED_GLASS_PANE, new BlockProperties(1D, null, true, 5D));
        put(Material.CYAN_STAINED_GLASS_PANE, new BlockProperties(1D, null, true, 5D));
        put(Material.GREEN_STAINED_GLASS_PANE, new BlockProperties(1D, null, true, 5D));
        put(Material.LIGHT_BLUE_STAINED_GLASS_PANE, new BlockProperties(1D, null, true, 5D));
        put(Material.LIGHT_GRAY_STAINED_GLASS_PANE, new BlockProperties(1D, null, true, 5D));
        put(Material.LIME_STAINED_GLASS_PANE, new BlockProperties(1D, null, true, 5D));
        put(Material.MAGENTA_STAINED_GLASS_PANE, new BlockProperties(1D, null, true, 5D));
        put(Material.ORANGE_STAINED_GLASS_PANE, new BlockProperties(1D, null, true, 5D));
        put(Material.PINK_STAINED_GLASS_PANE, new BlockProperties(1D, null, true, 5D));
        put(Material.PURPLE_STAINED_GLASS_PANE, new BlockProperties(1D, null, true, 5D));
        put(Material.RED_STAINED_GLASS_PANE, new BlockProperties(1D, null, true, 5D));
        put(Material.WHITE_STAINED_GLASS_PANE, new BlockProperties(1D, null, true, 5D));
        put(Material.YELLOW_STAINED_GLASS_PANE, new BlockProperties(1D, null, true, 5D));
        put(Material.SEA_LANTERN, new BlockProperties(5D, null, true, 10D));
    }};

    private Map<EntityType, EntityProperties> entityProperties = new HashMap<EntityType, EntityProperties>(){{
        put(EntityType.BLAZE, new EntityProperties(null, null, coloredDustParticle(3, Color.YELLOW, 1)));
        put(EntityType.CAVE_SPIDER, new EntityProperties(null, null, coloredDustParticle(3, Color.BLUE, 0.5F)));
        put(EntityType.CREEPER, new EntityProperties(null, null, coloredDustParticle(3, Color.GREEN, 1)));
        put(EntityType.ENDER_DRAGON, new EntityProperties(null, null, coloredDustParticle(3, Color.FUCHSIA, 1)));
        put(EntityType.ENDERMAN, new EntityProperties(null, null, coloredDustParticle(3, Color.FUCHSIA, 1)));
        put(EntityType.ENDERMITE, new EntityProperties(null, null, coloredDustParticle(3, Color.FUCHSIA, 1)));
        put(EntityType.GHAST, new EntityProperties(null, null, coloredDustParticle(3, Color.WHITE, 1)));
        put(EntityType.IRON_GOLEM, new EntityProperties(50D, null, coloredDustParticle(1, Color.WHITE, 2)));
        put(EntityType.MAGMA_CUBE, new EntityProperties(null, null, coloredDustParticle(3, Color.YELLOW, 1)));
        put(EntityType.PHANTOM, new EntityProperties(null, null, coloredDustParticle(3, Color.FUCHSIA, 1)));
        put(EntityType.SKELETON, new EntityProperties(null, null, coloredDustParticle(3, Color.GRAY, 1)));
        put(EntityType.SKELETON_HORSE, new EntityProperties(null, null, coloredDustParticle(3, Color.GRAY, 1)));
        put(EntityType.SLIME, new EntityProperties(null, null, coloredDustParticle(3, Color.LIME, 3)));
        put(EntityType.SPIDER, new EntityProperties(null, null, coloredDustParticle(3, Color.BLUE, 0.5F)));
        put(EntityType.WITHER, new EntityProperties(null, null, coloredDustParticle(3, Color.BLACK, 1)));
        put(EntityType.WITHER_SKELETON, new EntityProperties(null, null, coloredDustParticle(3, Color.BLACK, 1)));
    }};

    public double getPenetrationCost(Material blockType) {
        Double penetrationCost = blockProperties.getOrDefault(blockType, defaultBlockProperties).getPenetrationCost();
        return penetrationCost != null ? penetrationCost : defaultBlockProperties.getPenetrationCost();
    }

    public double getPenetrationCost(EntityType entityType) {
        Double penetrationCost = entityProperties.getOrDefault(entityType, defaultEntityProperties).getPenetrationCost();
        return penetrationCost != null ? penetrationCost : defaultEntityProperties.getPenetrationCost();
    }

    public double getRestitution(Material blockType) {
        Double restitution = blockProperties.getOrDefault(blockType, defaultBlockProperties).getRestitution();
        return restitution != null ? restitution : defaultBlockProperties.getRestitution();
    }

    public double getRestitution(EntityType entityType) {
        Double restitution = entityProperties.getOrDefault(entityType, defaultEntityProperties).getRestitution();
        return restitution != null ? restitution : defaultEntityProperties.getRestitution();
    }

    public boolean isDestructible(Material blockType) {
        Boolean destructible = blockProperties.getOrDefault(blockType, defaultBlockProperties).isDestructible();
        return destructible != null ? destructible : defaultBlockProperties.isDestructible();
    }

    public double getDestructionThreshold(Material blockType) {
        Double destructionThreshold = blockProperties.getOrDefault(blockType, defaultBlockProperties).getDestructionThreshold();
        return destructionThreshold != null ? destructionThreshold : defaultBlockProperties.getDestructionThreshold();
    }

    public ConfigParticle getGibsParticle(EntityType entityType) {
        ConfigParticle particle = entityProperties.getOrDefault(entityType, defaultEntityProperties).getGibsParticle();
        return particle != null ? particle : defaultEntityProperties.getGibsParticle();
    }

    private ConfigParticle coloredDustParticle(int count, Color color, float size) {
        return new ConfigParticle(Particle.REDSTONE, count, 0, 0, 0, 0, new Particle.DustOptions(color, size), true);
    }
}
