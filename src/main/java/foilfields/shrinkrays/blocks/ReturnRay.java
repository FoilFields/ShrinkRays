package foilfields.shrinkrays.blocks;

import foilfields.shrinkrays.ShrinkRays;
import net.minecraft.entity.Entity;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3f;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;

/**
 * A ReturnRay block that extends the AbstractBeamCaster class. It can produce and control beams that return entities to their original size.
 */
public class ReturnRay extends AbstractBeamCaster {
    /**
     * Creates a new ReturnRay instance.
     *
     * @param settings The settings for the ReturnRay block.
     */
    public ReturnRay(Settings settings) {
        super(settings);
    }

    @Override
    public void onHitEntity(World world, BlockPos position, Entity entity, Vec3d center) {
        ScaleData scaleData = ScaleData.Builder.create().entity(entity).type(ScaleTypes.BASE).build();
        float scale = scaleData.getScale();

        if (Math.abs(1 - scale) < 0.003f) {
            scaleData.setScale(1);
            idleEffect(world, position, center);
            return;
        }

        if (!world.isClient()) {
            DustColorTransitionParticleEffect particleEffect = new DustColorTransitionParticleEffect(new Vector3f(0.212f, 1, 0), new Vector3f(0.714f, 1, 0.631f), 1);
            ((ServerWorld) world).spawnParticles(particleEffect, center.getX(), center.getY(), center.getZ(), 1, 0.25, 0.25, 0.25, 0);
        }

        if (scale < 1) {
            scaleData.setScale(scale + 0.003f);
            playSound(ShrinkRays.GROW_SOUND_EVENT, world, position);
        } else {
            scaleData.setScale(scale - 0.003f);
            playSound(ShrinkRays.SHRINK_SOUND_EVENT, world, position);
        }
    }
}
