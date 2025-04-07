package magicbees.bees;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import forestry.api.apiculture.hives.HiveManager;
import forestry.api.apiculture.hives.IHiveGen;
import magicbees.world.HiveGenNether;
import magicbees.world.HiveGenOblivion;
import magicbees.world.HiveGenUnderground;
import thaumcraft.api.blocks.BlocksTC;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;

import java.util.List;
import java.util.Random;

import static magicbees.util.ConfigWorldGen.*;

/**
 * Created by Elec332 on 11-2-2017.
 */
public enum EnumHiveGen {

    CURIOUS(3.0f, HiveManager.genHelper.tree(), BiomeDictionary.Type.FOREST, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.HILLS),
    UNUSUAL(1.0f, HiveManager.genHelper.ground(Blocks.DIRT, Blocks.GRASS), BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.RIVER),
    RESONANT(0.9f, HiveManager.genHelper.ground(Blocks.SAND, Blocks.SANDSTONE), BiomeDictionary.Type.SANDY, BiomeDictionary.Type.MESA, BiomeDictionary.Type.HOT, BiomeDictionary.Type.MAGICAL),
    DEEP(3.8f, new HiveGenUnderground(10, 15, 5), BiomeDictionary.Type.HILLS, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.MAGICAL) {
        @Override
        void postGen(World world, Random random, BlockPos blockPos) {
            if (!postGenRedstone) {
                return;
            }
            for (EnumFacing facing : EnumFacing.VALUES) {
                EnumHiveGen.spawnVein(world, blockPos.offset(facing), 5, Blocks.REDSTONE_BLOCK.getDefaultState(), Blocks.STONE);
            }
        }

    },
    INFERNAL(50.0f, new HiveGenNether(0, 175, 6), BiomeDictionary.Type.NETHER) {
        @Override
        void postGen(World world, Random random, BlockPos blockPos) {
            if (!postGenNetherQuartz) {
                return;
            }
            for (EnumFacing facing : EnumFacing.VALUES) {
                EnumHiveGen.spawnVein(world, blockPos.offset(facing), 4, Blocks.QUARTZ_BLOCK.getDefaultState(), Blocks.NETHERRACK);
            }
        }

    },
    INFERNAL_OVERWORLD(0.95f, new HiveGenUnderground(5, 13, 6), BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.HOT) {
        @Override
        void postGen(World world, Random random, BlockPos blockPos) {
            if (!postGenQuartz) {
                return;
            }
            for (EnumFacing facing : EnumFacing.VALUES) {
                if (facing.getAxis() != EnumFacing.Axis.Y) {
                    EnumHiveGen.spawnVein(world, blockPos.offset(facing), world.rand.nextInt(4) + 1, BlocksTC.oreQuartz.getDefaultState(), Blocks.STONE);
                } else {
                    BlockPos pos = blockPos.offset(facing);
                    if (HiveGenUnderground.isReplaceableOreGen(world.getBlockState(pos), world, pos, Blocks.STONE)) {
                        world.setBlockState(pos, BlocksTC.oreQuartz.getDefaultState(), 2);
                    }
                }
            }
        }

    },
    OBLIVION(20.0f, new HiveGenOblivion(), BiomeDictionary.Type.END) {
        @Override
        void postGen(World world, Random random, BlockPos blockPos) {
            if (!postGenObsidianSpikes) {
                return;
            }
            int obsidianSpikeHeight = world.rand.nextInt(8) + 3;
            for (int i = 1; i < obsidianSpikeHeight && blockPos.getY() - i > 0; ++i) {
                world.setBlockState(blockPos.offset(EnumFacing.DOWN, i), Blocks.OBSIDIAN.getDefaultState(), 2);
            }
        }

    },
    OBLIVION_OVERWORLD(0.87f, new HiveGenUnderground(5, 5, 5), BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.COLD) {
        @Override
        void postGen(World world, Random random, BlockPos blockPos) {
            if (!postGenEndstone) {
                return;
            }
            for (EnumFacing facing : EnumFacing.VALUES) {
                EnumHiveGen.spawnVein(world, blockPos.offset(facing), world.rand.nextInt(facing.getAxis() == EnumFacing.Axis.Y ? 3 : 6) + 1, Blocks.END_STONE.getDefaultState(), Blocks.STONE);
            }
        }

    };

    EnumHiveGen(float chance, IHiveGen hiveGen, BiomeDictionary.Type... biomes) {
        this.chance = chance;
        this.hiveGen = hiveGen;
        this.biomes = Lists.newArrayList(Preconditions.checkNotNull(biomes));
    }

    final float chance;
    final IHiveGen hiveGen;
    final List<BiomeDictionary.Type> biomes;

    void postGen(World world, Random random, BlockPos blockPos) {
    }

    private static void spawnVein(World world, BlockPos pos, int maxSpawnCount, IBlockState replacement, Block toReplace) {
        maxSpawnCount = world.rand.nextInt(maxSpawnCount + 1);
        int spawnAttempts = 0;
        int spawnCount = 0;

        while (spawnCount < maxSpawnCount && spawnAttempts < maxSpawnCount * 1.2) {
            ++spawnAttempts;
            if (world.isBlockLoaded(pos) && !world.isAirBlock(pos) && HiveGenUnderground.isReplaceableOreGen(world.getBlockState(pos), world, pos, toReplace)) {
                world.setBlockState(pos, replacement, 2);
                ++spawnCount;
            }
            pos = pos.offset(EnumFacing.VALUES[world.rand.nextInt(6)]);
        }
    }

}
