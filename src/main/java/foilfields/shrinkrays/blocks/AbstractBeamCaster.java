package foilfields.shrinkrays.blocks;

import foilfields.shrinkrays.ShrinkRays;
import foilfields.shrinkrays.blockentity.BeamCasterBlockEntity;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.VibrationParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.*;
import net.minecraft.world.Vibration;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public abstract class AbstractBeamCaster extends BlockWithEntity implements BlockEntityProvider {
    public static final BooleanProperty POWERED = Properties.POWERED;
    public static final DirectionProperty FACING = FacingBlock.FACING;

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BeamCasterBlockEntity(pos, state);
    }

    public AbstractBeamCaster(Settings settings) {
        super(settings);

        this.setDefaultState(this.getDefaultState().with(POWERED, false).with(FACING, Direction.NORTH));
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ShrinkRays.BEAM_CASTER_BLOCK_ENTITY, BeamCasterBlockEntity::tick);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(POWERED, ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos())).with(FACING, ctx.getPlayerLookDirection().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED, FACING);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (world.isClient) {
            return;
        }

        boolean powered = state.get(POWERED);
        if (powered != world.isReceivingRedstonePower(pos)) {
            if (powered) {
                world.createAndScheduleBlockTick(pos, this, 4);
            } else {
                world.setBlockState(pos, state.cycle(POWERED), Block.NOTIFY_LISTENERS);
            }
        }
    }

    public abstract void onHitEntity(Entity entity);

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(POWERED) && !world.isReceivingRedstonePower(pos)) {
            world.setBlockState(pos, state.cycle(POWERED), Block.NOTIFY_LISTENERS);
        }
    }

//    public void onTick(World world, BlockPos pos, BlockState state) {
//        if (!state.isOf(this) || !state.get(POWERED)) return;
//        Direction direction = state.get(FACING);
//
//        Box area = new Box(pos.add(direction.getVector()), pos.add(direction.getVector().add(1,1,1)));
//
//        Vec3d center = area.getCenter();
//
//        if (Math.random() < 0.2f) world.playSound(null, pos, SoundEvents.BLOCK_SLIME_BLOCK_STEP, SoundCategory.BLOCKS, 1.0f, 1.0f);
//
//        if (!world.isClient()) {
//            ((ServerWorld) world).spawnParticles(ParticleTypes.POOF, center.getX(), center.getY(), center.getZ(), 1, 0.25, 0.25, 0.25, 0);
//        }
//
//        List<Entity> entities = world.getOtherEntities(null, area);
//
//        entities.forEach(entity -> {
//            if (Math.random() < 0.2f) world.playSound(null, pos, SoundEvents.ENTITY_SLIME_SQUISH, SoundCategory.BLOCKS, 1.0f, 1.0f);
//            onHitEntity(entity);
//        });
//    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }
}
