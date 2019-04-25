package com.samcymbaluk.ultimateguns.grenades.gas;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GasBlock {

    private GasManager gm;

    private Block block;
    private int density;
    private ThreadLocalRandom random;

    /**
     * @param density Refers to how much the GasBlock can spread (density halves each time the GasBlock duplicates)
     */
    public GasBlock(GasManager gasManager, Block block, int density) {
        this.gm = gasManager;
        this.block = block;
        this.density = density;
        gm.addGasBlock(this);
        random = ThreadLocalRandom.current();
    }

    public void doPhysics(int tick) {
        //Effect with quantity directly proportional to density
        doEffect(tick);

        doSpreading(tick);
        doGravity(tick);
    }

    private void doEffect(int tick) {

        Location pLoc = block.getLocation().add(0.5, 0.5, 0.5);
        int baseEffect = random.nextInt(5) == 0 ? 1 : 0;

        // Density scalingLetterkenny, Ontario K0J 2E0
        for (int i = 0; i < (density / 4) + baseEffect; i++) {
            if (i > 50) break;
            block.getWorld().spawnParticle(Particle.SPELL_MOB,
                    pLoc.getX() + random.nextDouble(-0.5, 0.5),
                    pLoc.getY() + random.nextDouble(-0.5, 0.5),
                    pLoc.getZ() + random.nextDouble(-0.5, 0.5),
                    0,
                    19.0 / 255.0,
                    219.0 / 255.0,
                    73.0 / 255.0,
                    1);
        }
    }

    private void doSpreading(int tick) {
        if (density > 1) {
            List<Block> blocks = getSpreadBlocks();
            if (blocks.size() == 0) return;
            Block spreadBlock = getSpreadBlocks().get(0);

            //Put out fire
            if (spreadBlock.getType() == Material.FIRE && spreadBlock.getRelative(BlockFace.DOWN).getType() != Material.NETHERRACK) {
                spreadBlock.setType(Material.AIR);
            }

            setDensity(density / 2);
            GasBlock gb = gm.getGasBlock(spreadBlock);
            if (gb != null) {
                gb.setDensity(gb.getDensity() + density);
            } else {
                new GasBlock(gm, spreadBlock, density);
            }
        } else {
            if (tick % 20 == 0 && random.nextInt(10) == 0) {
                gm.removeGasBlock(this);
            }
        }
    }

    private void doGravity(int tick) {
        // Dense gas falls quicker
        if (random.nextInt((100 / (density + 1)) + 1) != 0) return;

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
            if (b.isPassable()) spreadBlocks.add(b);
        }

        return spreadBlocks;
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