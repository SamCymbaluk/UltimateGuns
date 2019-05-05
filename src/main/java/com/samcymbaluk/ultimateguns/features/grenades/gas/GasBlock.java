package com.samcymbaluk.ultimateguns.features.grenades.gas;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GasBlock {

    private GasManager gm;
    private GasFeatureConfig conf;

    private Block block;
    private int density;
    private ThreadLocalRandom random;

    /**
     * @param density Refers to how much the GasBlock can spread (density halves each time the GasBlock duplicates)
     */
    public GasBlock(GasManager gasManager, Block block, int density) {
        this.gm = gasManager;
        this.conf = gasManager.getConfig();
        this.block = block;
        this.density = density;
        gm.addGasBlock(this);
        random = ThreadLocalRandom.current();
    }

    public void doPhysics(int tick) {
        doEffect(tick);
        doSpreading(tick);
        doGravity(tick);
    }

    private void doEffect(int tick) {
        Location pLoc = block.getLocation().add(0.5, 0.5, 0.5);
        double particlesLeft = conf.getParticleFrequency() + conf.getParticleFrequency() * (density * conf.getParticleDensityMultiplier());
        particlesLeft = Math.min(particlesLeft, conf.getMaxParticleAmount());

        while (particlesLeft > 0) {
            boolean spawn = particlesLeft > 1 || random.nextDouble() <= particlesLeft;

            if (spawn) {
                Location ranLoc = new Location(block.getWorld(),
                        pLoc.getX() + random.nextDouble(-0.5, 0.5),
                        pLoc.getY() + random.nextDouble(-0.5, 0.5),
                        pLoc.getZ() + random.nextDouble(-0.5, 0.5));

                conf.getGasParticle().spawn(ranLoc);
            }

            particlesLeft--;
        }
    }

    private void doSpreading(int tick) {
        if (density > 1) {
            List<Block> blocks = getSpreadBlocks();
            if (blocks.size() == 0) return;
            Block spreadBlock = getSpreadBlocks().get(0);

            //Put out fire
            if (conf.isExtinguishFire() && spreadBlock.getType() == Material.FIRE) {
                if (spreadBlock.getRelative(BlockFace.DOWN).getType() != Material.NETHERRACK) {
                    spreadBlock.setType(Material.AIR);
                }
            }

            setDensity(density / 2);
            GasBlock gb = gm.getGasBlock(spreadBlock);
            if (gb != null) {
                gb.setDensity(gb.getDensity() + density);
            } else {
                new GasBlock(gm, spreadBlock, density);
            }
        } else {
            if (tick % conf.getDissipationPollPeriod() == 0 && random.nextDouble() <= conf.getDissipationChance()) {
                gm.removeGasBlock(this);
            }
        }
    }

    private void doGravity(int tick) {
        if (!conf.hasGasGravity()) return;

        // Dense gas falls quicker
        if (random.nextInt((conf.getGasGravityPeriod() / (density + 1)) + 1) != 0) return;

        if (gm.getGasBlock(block.getRelative(BlockFace.DOWN)) == null && block.getRelative(BlockFace.DOWN).isEmpty()) {
            //block.setType(Material.AIR);
            Block oldBlock = block;
            block = block.getRelative(BlockFace.DOWN);
            //block.setType(Gas.MATERIAL);

            gm.updateGasBlock(this, oldBlock);
        }

    }

    private List<Block> getSpreadBlocks() {
        List<Block> surroundings = Arrays.asList(
                block.getRelative(BlockFace.EAST), block.getRelative(BlockFace.WEST),
                block.getRelative(BlockFace.NORTH), block.getRelative(BlockFace.SOUTH),
                block.getRelative(BlockFace.DOWN), block.getRelative(BlockFace.UP)
        );

        Collections.shuffle(surroundings);
        surroundings.sort(this::compareBlocksAsGas);

        List<Block> spreadBlocks = new ArrayList<>();
        for (Block b : surroundings) {
            if (canSpread(b)) spreadBlocks.add(b);
        }

        return spreadBlocks;
    }

    private boolean canSpread(Block dest) {
        // Passable optimization
        if (block.isPassable() && dest.isPassable()) {
            return true;
        } else {
            // Ray trace from middle to middle
            Location start = block.getLocation().add(0.5, 0.5, 0.5);
            Location end = dest.getLocation().add(0.5, 0.5, 0.5);
            Vector dir = end.subtract(start).toVector();
            double dist = start.distance(end);

            RayTraceResult srcRt = block.rayTrace(start, dir, dist, FluidCollisionMode.NEVER);
            RayTraceResult destRt = dest.rayTrace(start, dir, dist, FluidCollisionMode.NEVER);

            return srcRt == null && destRt == null;
        }
    }

    private int compareBlocksAsGas(Block b1, Block b2) {
        GasBlock thisGb = gm.getGasBlock(b1);
        if (thisGb != null) {
            GasBlock otherGb = gm.getGasBlock(b2);
            if (otherGb != null) {
                return thisGb.getDensity() - otherGb.getDensity();
            } else {
                return 1;
            }
        } else {
            GasBlock otherGb = gm.getGasBlock(b2);
            if (otherGb != null) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public Block getBlock() {
        return block;
    }

    public int getDensity() {
        return density;
    }

    public void setDensity(int density) {
        this.density = density;
    }

}