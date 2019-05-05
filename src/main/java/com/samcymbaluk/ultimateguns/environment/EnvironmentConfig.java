package com.samcymbaluk.ultimateguns.environment;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class EnvironmentConfig {

    private BlockProperties defaultBlockProperties = new BlockProperties(Integer.MAX_VALUE, 0.4, false, Integer.MAX_VALUE);

    private EntityProperties defaultEntityProperties = new EntityProperties(10, 0.1);

    private Map<Material, BlockProperties> blockProperties = new HashMap<Material, BlockProperties>(){{
        put(Material.GLASS, new BlockProperties(1, 0.5, true, 5));
    }};

    private Map<EntityType, EntityProperties> entityProperties = new HashMap<EntityType, EntityProperties>(){{

    }};

    public double getPenetrationCost(Material blockType) {
        return blockProperties.getOrDefault(blockType, defaultBlockProperties).getPenetrationCost();
    }

    public double getPenetrationCost(EntityType entityType) {
        return entityProperties.getOrDefault(entityType, defaultEntityProperties).getPenetrationCost();
    }

    public double getRestitution(Material blockType) {
        return blockProperties.getOrDefault(blockType, defaultBlockProperties).getRestitution();
    }

    public double getRestitution(EntityType entityType) {
        return entityProperties.getOrDefault(entityType, defaultEntityProperties).getRestitution();
    }

    public boolean isDestructible(Material blockType) {
        return blockProperties.getOrDefault(blockType, defaultBlockProperties).isDestructible();
    }

    public double getDestructionThreshold(Material blockType) {
        return blockProperties.getOrDefault(blockType, defaultBlockProperties).getDestructionThreshold();
    }
}
