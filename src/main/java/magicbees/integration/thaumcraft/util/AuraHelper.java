package magicbees.integration.thaumcraft.util;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeModifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import thaumcraft.common.world.aura.AuraChunk;
import thaumcraft.common.world.aura.AuraHandler;

import java.util.Random;
import java.util.function.Consumer;

/**
 * Created by Elec332 on 3-8-2019
 */
public class AuraHelper {

    public static void handleRandomChunk(IBeeGenome genome, IBeeHousing housing, Consumer<AuraChunk> handler) {
        World world = housing.getWorldObj();
        Random random = world.rand;
        if (BeeManager.beeRoot != null) {
            IBeeModifier modifier = BeeManager.beeRoot.createBeeHousingModifier(housing);
            Vec3d range = new Vec3d(genome.getTerritory()).scale(modifier.getTerritoryModifier(genome, 1));
            range.scale(1.5);
            double r1 = random.nextGaussian(), r2 = random.nextGaussian();
            if (r1 <= 1 && r2 <= 1 && r1 >= -1 && r2 >= -1) {
                int x = ((int) (housing.getCoordinates().getX() + r1 * range.x)) >> 4;
                int z = ((int) (housing.getCoordinates().getZ() + r2 * range.z)) >> 4;
                AuraChunk auraChunk = AuraHandler.getAuraChunk(world.provider.getDimension(), x, z);
                if(auraChunk != null && handler != null) {
                    handler.accept(auraChunk);
                    world.getChunk(x, z).markDirty();
                }
            }
        }
    }

}
