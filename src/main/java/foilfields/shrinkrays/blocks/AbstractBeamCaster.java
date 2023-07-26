package foilfields.shrinkrays.blocks;

import foilfields.shrinkrays.ShrinkRays;
import foilfields.shrinkrays.blockentity.BeamCasterBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * An abstract base class for beam caster blocks that can produce and control beams.
 */
public abstract class AbstractBeamCaster extends BlockWithEntity implements BlockEntityProvider {
    public static final BooleanProperty POWERED = Properties.POWERED;
    public static final DirectionProperty FACING = FacingBlock.FACING;

    /**
     * Creates a new AbstractBeamCaster instance.
     *
     * @param settings The settings for the block.
     */
    public AbstractBeamCaster(Settings settings) {
        super(settings);

        this.setDefaultState(this.getDefaultState().with(POWERED, false).with(FACING, Direction.NORTH));
    }

    /**
     * Creates a BlockEntity for this block at the given position and state.
     *
     * @param pos   The position of the block entity.
     * @param state The state of the block entity.
     * @return A new BlockEntity instance.
     */
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BeamCasterBlockEntity(pos, state);
    }

    /**
     * Gets the BlockEntityTicker for this block.
     *
     * @param world The world where the block exists.
     * @param state The state of the block.
     * @param type  The type of the block entity.
     * @param <T>   The type of the block entity.
     * @return A BlockEntityTicker for this block.
     */
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ShrinkRays.BEAM_CASTER_BLOCK_ENTITY, BeamCasterBlockEntity::tick);
    }

    /**
     * Gets the render type for this block.
     *
     * @param state The state of the block.
     * @return The BlockRenderType.
     */
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    /**
     * Called when the block is about to be placed in the world. Returns the state of the block to be placed.
     *
     * @param ctx The ItemPlacementContext for the placement.
     * @return The BlockState to be placed.
     */
    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(POWERED, ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos())).with(FACING, ctx.getPlayerLookDirection().getOpposite());
    }

    /**
     * Appends block properties to the given StateManager.Builder.
     *
     * @param builder The StateManager.Builder to append properties to.
     */
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED, FACING);
    }

    /**
     * Called when a neighboring block changes, schedules a block update if needed.
     *
     * @param state       The current state of the block.
     * @param world       The world where the block exists.
     * @param pos         The position of the block.
     * @param sourceBlock The neighboring block that changed.
     * @param sourcePos   The position of the neighboring block that changed.
     * @param notify      True if this block should send a change notification to observers.
     */
    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!world.isClient) {
            boolean powered = state.get(POWERED);
            if (powered != world.isReceivingRedstonePower(pos)) {
                if (powered) {
                    world.scheduleBlockTick(pos, this, 4);
                } else {
                    world.setBlockState(pos, state.cycle(POWERED), 2);
                }
            }
        }
    }

    /**
     * Called during a scheduled block update.
     *
     * @param state  The current state of the block.
     * @param world  The world where the block exists.
     * @param pos    The position of the block.
     * @param random A random number generator.
     */
    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, net.minecraft.util.math.random.Random random) {
        if (state.get(POWERED) && !world.isReceivingRedstonePower(pos)) {
            world.setBlockState(pos, state.cycle(POWERED), Block.NOTIFY_LISTENERS);
        }
    }

    /**
     * Rotates the block state by the given rotation.
     *
     * @param state    The current state of the block.
     * @param rotation The rotation to apply.
     * @return The rotated block state.
     */
    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    /**
     * Mirrors the block state in the given direction.
     *
     * @param state  The current state of the block.
     * @param mirror The direction of the mirror.
     * @return The mirrored block state.
     */
    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    /**
     * Called when the beam hits an entity.
     *
     * @param entity The entity hit by the beam.
     */
    public abstract void onHitEntity(Entity entity);
}