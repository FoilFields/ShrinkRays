package foilfields.shrinkrays.blocks;

import com.mojang.serialization.MapCodec;
import foilfields.shrinkrays.ShrinkRays;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.entity.Entity;
import net.minecraft.particle.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3f;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;

/**
 * A GrowthRay block that extends the AbstractBeamCaster class. It can produce and control beams that can increase the size of entities.
 */
public class GrowthRay extends AbstractBeamCaster {
    /**
     * Creates a new GrowthRay instance.
     *
     * @param settings The settings for the GrowthRay block.
     */
    public GrowthRay(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    public void onHitEntity(World world, BlockPos position, Entity entity, Vec3d center) {
        if (world.isClient()) return;

        ScaleData scaleData = ScaleData.Builder.create().entity(entity).type(ScaleTypes.BASE).build();

        if (scaleData.getScale() == ShrinkRays.CONFIG.maxScale()) {
            idleEffect(world, position, center);
            return;
        }

        DustColorTransitionParticleEffect particleEffect = new DustColorTransitionParticleEffect(new Vector3f(1f, 0.749f, 0f), new Vector3f(1, 0.478f, 0), 1);
        ((ServerWorld) world).spawnParticles(particleEffect, center.getX(), center.getY(), center.getZ(), 1, 0.25, 0.25, 0.25, 0);

        playSound(ShrinkRays.GROW_SOUND_EVENT, world, position);
        scaleData.setScale(Math.min(scaleData.getScale() * ShrinkRays.CONFIG.growthSpeed(), ShrinkRays.CONFIG.maxScale()));
    }
}

