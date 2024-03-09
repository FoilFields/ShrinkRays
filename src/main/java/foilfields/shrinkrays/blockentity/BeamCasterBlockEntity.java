package foilfields.shrinkrays.blockentity;

import foilfields.shrinkrays.ShrinkRays;
import foilfields.shrinkrays.blocks.AbstractBeamCaster;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
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
     * @param position   The position of the block entity.
     * @param state The state of the block entity.
     * @param be    The instance of the BeamCasterBlockEntity.
     */
    public static void tick(World world, BlockPos position, BlockState state, BeamCasterBlockEntity be) {
        if (!state.get(AbstractBeamCaster.POWERED))
            return;

        Direction direction = state.get(AbstractBeamCaster.FACING);

        Vec3d center = position.add(direction.getVector()).toCenterPos();
        Box area = new Box(center.add(-0.5, -0.5, -0.5), center.add(0.5, 0.5, 0.5));
        AbstractBeamCaster caster = ((AbstractBeamCaster) state.getBlock());

        caster.tickCounter();

        List<Entity> entities = world.getOtherEntities(null, area);
        if (entities.size() == 0) caster.idleEffect(world, position, area.getCenter());

        entities.forEach(entity -> caster.onHitEntity(world, position, entity, area.getCenter()));
    }
}