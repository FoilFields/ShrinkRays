package foilfields.shrinkrays.blockentity;

import foilfields.shrinkrays.ShrinkRays;
import foilfields.shrinkrays.blocks.AbstractBeamCaster;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

/**
 * The BlockEntity class for the Beam Caster block. Responsible for handling the behavior and interactions of the block.
 */
public class BeamCasterBlockEntity extends BlockEntity {
    /**
     * Creates a new BeamCasterBlockEntity instance.
     *
     * @param pos   The position of the block entity.
     * @param state The state of the block entity.
     */
    public BeamCasterBlockEntity(BlockPos pos, BlockState state) {
        super(ShrinkRays.BEAM_CASTER_BLOCK_ENTITY, pos, state);
    }

    /**
     * The tick method called periodically for the Beam Caster block.
     *
     * @param world The world where the block entity exists.
     * @param pos   The position of the block entity.
     * @param state The state of the block entity.
     * @param be    The instance of the BeamCasterBlockEntity.
     */
    public static void tick(World world, BlockPos pos, BlockState state, BeamCasterBlockEntity be) {
        if (!state.get(AbstractBeamCaster.POWERED))
            return;

        Direction direction = state.get(AbstractBeamCaster.FACING);
        Box area = new Box(pos.add(direction.getVector()), pos.add(direction.getVector().add(1, 1, 1)));
        Vec3d center = area.getCenter();

        if (Math.random() < 0.1f)
            world.playSound(null, pos, SoundEvents.ITEM_CROSSBOW_LOADING_START, SoundCategory.BLOCKS, 0.5f, 1.0f);

        if (!world.isClient()) {
            ((ServerWorld) world).spawnParticles(ParticleTypes.SMOKE, center.getX(), center.getY(), center.getZ(), 1, 0.25, 0.25, 0.25, 0);
        }

        List<Entity> entities = world.getOtherEntities(null, area);

        entities.forEach(entity -> {
            if (Math.random() < 0.5f)
                world.playSound(null, pos, SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS, 0.5f, 1.0f);

            ((AbstractBeamCaster) state.getBlock()).onHitEntity(entity);

            if (!world.isClient()) {
                ((ServerWorld) world).spawnParticles(ParticleTypes.CRIT, center.getX(), center.getY(), center.getZ(), 1, 0.25, 0.25, 0.25, 0);
            }
        });
    }
}