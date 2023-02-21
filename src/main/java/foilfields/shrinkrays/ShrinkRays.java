package foilfields.shrinkrays;

import foilfields.shrinkrays.blockentity.BeamCasterBlockEntity;
import foilfields.shrinkrays.blocks.GrowthRay;
import foilfields.shrinkrays.blocks.ReturnRay;
import foilfields.shrinkrays.blocks.ShrinkRay;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ShrinkRays implements ModInitializer {
    public static final ShrinkRay SHRINK_RAY = new ShrinkRay(FabricBlockSettings.copyOf(Blocks.DISPENSER));
    public static final ReturnRay RETURN_RAY = new ReturnRay(FabricBlockSettings.copyOf(Blocks.DISPENSER));
    public static final GrowthRay GROWTH_RAY = new GrowthRay(FabricBlockSettings.copyOf(Blocks.DISPENSER));

    public static final BlockEntityType<BeamCasterBlockEntity> BEAM_CASTER_BLOCK_ENTITY = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            GetIdentifier("beam_caster_block_entity"),
            FabricBlockEntityTypeBuilder.create(BeamCasterBlockEntity::new, GROWTH_RAY, SHRINK_RAY, RETURN_RAY).build()
    );

    private static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(GetIdentifier("shrink_rays")).icon(() -> new ItemStack(SHRINK_RAY)).build();

    @Override
    public void onInitialize() {
        Registry.register(Registry.BLOCK, GetIdentifier("shrink_ray"), SHRINK_RAY);
        Registry.register(Registry.BLOCK, GetIdentifier("return_ray"), RETURN_RAY);
        Registry.register(Registry.BLOCK, GetIdentifier("growth_ray"), GROWTH_RAY);
        Registry.register(Registry.ITEM, GetIdentifier("shrink_ray"), new BlockItem(SHRINK_RAY, new FabricItemSettings().group(ITEM_GROUP)));
        Registry.register(Registry.ITEM, GetIdentifier("return_ray"), new BlockItem(RETURN_RAY, new FabricItemSettings().group(ITEM_GROUP)));
        Registry.register(Registry.ITEM, GetIdentifier("growth_ray"), new BlockItem(GROWTH_RAY, new FabricItemSettings().group(ITEM_GROUP)));
    }

    public static Identifier GetIdentifier(String name) {
        return new Identifier("shrink_rays", name);
    }
}
