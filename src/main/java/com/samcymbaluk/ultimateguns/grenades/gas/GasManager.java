package com.samcymbaluk.ultimateguns.grenades.gas;

import com.samcymbaluk.ultimateguns.UltimateGuns;
import com.samcymbaluk.ultimateguns.util.ImmutableBlockVector;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class GasManager {

    private GasFeature gasFeature;

    private Map<ImmutableBlockVector, GasBlock> gasBlocks;
    private Map<Player, Integer> playerEffects = new WeakHashMap<>();
    private int tick;

    public GasManager(GasFeature gasFeature) {
        this.gasFeature = gasFeature;
        gasBlocks = new HashMap<>();
    }

    public void start() {
        if (tick == 0) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(UltimateGuns.getInstance(), () -> {
                tick++;
                physicsTick(tick);
                effectsTick(tick);
            }, 1, 1);
        } else {
            throw new IllegalStateException("Cannot call start more than once");
        }
    }

    private void physicsTick(int tick) {
        // Could be optimized, if necessary, by only doing puts and removes after the tick, so the clone is not needed
        Map<ImmutableBlockVector, GasBlock> gasBlocksClone = new HashMap<>(gasBlocks);
        for (GasBlock gasBlock : gasBlocksClone.values()) {
            gasBlock.doPhysics(tick);
        }
    }

    private void effectsTick(int tick) {
        if (tick % 5 == 0) {
            playerEffectsTick();
            entityEffectsTick();
        }
    }

    private void playerEffectsTick() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            GasBlock gp = getGasBlock(player.getEyeLocation().getBlock());
            //We only care if the player's face is in gas not the rest of their body
            if (gp != null) {
                int effectTime = playerEffects.containsKey(player) ? playerEffects.get(player) + 1 : 1;
                playerEffects.put(player, effectTime);

                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 3, true, false), true);
                player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * effectTime, 2, true, false), true);
            } else {
                playerEffects.remove(player);
            }
        }
    }

    private void entityEffectsTick() {
        for (World world : Bukkit.getWorlds()) {
            if (!gasFeature.isEnabled(world)) continue;

            for (Entity entity : world.getEntities()) {
                if (!(entity instanceof LivingEntity) || (entity instanceof Player)) continue;

                LivingEntity livingEnt = (LivingEntity) entity;
                GasBlock gp = getGasBlock(livingEnt.getEyeLocation().getBlock());

                if (gp != null) {
                    livingEnt.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 2, true, false), true);
                }
            }
        }
    }

    public GasBlock getGasBlock(ImmutableBlockVector vector) {
        return gasBlocks.get(vector);
    }

    public GasBlock getGasBlock(Block b) {
        ImmutableBlockVector vector = new ImmutableBlockVector(b);
        return gasBlocks.get(vector);
    }

    public void addGasBlock(GasBlock gb) {
        ImmutableBlockVector key = new ImmutableBlockVector(gb.getBlock());
        if (gasBlocks.containsKey(key)) {
            // Add to existing block
            GasBlock existingGb = gasBlocks.get(key);
            existingGb.setDensity(existingGb.getDensity() + gb.getDensity());
            gb.setDensity(0);

            gasBlocks.put(key, existingGb);
        } else {
            gasBlocks.put(key, gb);
        }
        //gb.getBlock().setType(Material.SLIME_BLOCK);
    }

    public void removeGasBlock(GasBlock gb) {
        gasBlocks.remove(new ImmutableBlockVector(gb.getBlock()));
        //gb.getBlock().setType(Material.AIR);
    }

    public void updateGasBlock(GasBlock gb, Block oldLoc) {
        //Remove
        ImmutableBlockVector key = new ImmutableBlockVector(oldLoc);
        gasBlocks.remove(key);

        //Add
        ImmutableBlockVector newKey = new ImmutableBlockVector(gb.getBlock());
        gasBlocks.put(newKey, gb);
    }
}
