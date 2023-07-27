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

public class ShrinkRay extends AbstractBeamCaster {
    public ShrinkRay(Settings settings) {
        super(settings);
    }

    @Override
    public void onHitEntity(World world, BlockPos position, Entity entity, Vec3d center) {
        ScaleData scaleData = ScaleData.Builder.create().entity(entity).type(ScaleTypes.BASE).build();

        if (scaleData.getScale() <= 0.051) {
            idleEffect(world, position, center);
            return;
        }

        if (!world.isClient()) {
            DustColorTransitionParticleEffect particleEffect = new DustColorTransitionParticleEffect(new Vector3f(0.44f,0.91f,1.00f), new Vector3f(0.74f,0.95f,1.00f), 1);
            ((ServerWorld) world).spawnParticles(particleEffect, center.getX(), center.getY(), center.getZ(), 1, 0.25, 0.25, 0.25, 0);
        }

        playSound(ShrinkRays.SHRINK_SOUND_EVENT, world, position);
        scaleData.setScale(Math.max(scaleData.getScale() - 0.003f, 0.05f));
    }
}
