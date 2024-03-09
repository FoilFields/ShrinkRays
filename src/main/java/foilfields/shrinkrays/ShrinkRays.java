package foilfields.shrinkrays;

import foilfields.shrinkrays.blockentity.BeamCasterBlockEntity;
import foilfields.shrinkrays.blocks.GrowthRay;
import foilfields.shrinkrays.blocks.ReturnRay;
import foilfields.shrinkrays.blocks.ShrinkRay;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * The main mod initializer for ShrinkRays.
 */
public class ShrinkRays implements ModInitializer {
    public static final foilfields.shrinkrays.ShrinkRaysConfig CONFIG = foilfields.shrinkrays.ShrinkRaysConfig.createAndLoad();
    public static final ShrinkRay SHRINK_RAY = new ShrinkRay(FabricBlockSettings.copyOf(Blocks.DISPENSER));
    public static final ReturnRay RETURN_RAY = new ReturnRay(FabricBlockSettings.copyOf(Blocks.DISPENSER));
    public static final GrowthRay GROWTH_RAY = new GrowthRay(FabricBlockSettings.copyOf(Blocks.DISPENSER));

    public static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, GetIdentifier("shrink_rays_group"));

    public static Identifier SHRINK_SOUND_ID = GetIdentifier("shrink");
    public static SoundEvent SHRINK_SOUND_EVENT = SoundEvent.of(SHRINK_SOUND_ID);
    public static Identifier GROW_SOUND_ID = GetIdentifier("grow");
    public static SoundEvent GROW_SOUND_EVENT = SoundEvent.of(GROW_SOUND_ID);
    public static Identifier IDLE_SOUND_ID = GetIdentifier("idle");
    public static SoundEvent IDLE_SOUND_EVENT = SoundEvent.of(IDLE_SOUND_ID);

    public static final BlockEntityType<BeamCasterBlockEntity> BEAM_CASTER_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            GetIdentifier("beam_caster_block_entity"),
            FabricBlockEntityTypeBuilder.create(BeamCasterBlockEntity::new, GROWTH_RAY, SHRINK_RAY, RETURN_RAY).build()
    );

    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP, FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup.shrink_rays.shrink_rays"))
                .icon(() -> new ItemStack(SHRINK_RAY))
                .build());

        Registry.register(Registries.SOUND_EVENT, SHRINK_SOUND_ID, SHRINK_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, GROW_SOUND_ID, GROW_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, IDLE_SOUND_ID, IDLE_SOUND_EVENT);

        Registry.register(Registries.BLOCK, GetIdentifier("shrink_ray"), SHRINK_RAY);
        Registry.register(Registries.BLOCK, GetIdentifier("return_ray"), RETURN_RAY);
        Registry.register(Registries.BLOCK, GetIdentifier("growth_ray"), GROWTH_RAY);
        Registry.register(Registries.ITEM, GetIdentifier("shrink_ray"), new BlockItem(SHRINK_RAY, new FabricItemSettings()));
        Registry.register(Registries.ITEM, GetIdentifier("return_ray"), new BlockItem(RETURN_RAY, new FabricItemSettings()));
        Registry.register(Registries.ITEM, GetIdentifier("growth_ray"), new BlockItem(GROWTH_RAY, new FabricItemSettings()));

        ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP).register((entries -> {
            entries.add(SHRINK_RAY);
            entries.add(GROWTH_RAY);
            entries.add(RETURN_RAY);
        }));
    }

    /**
     * Helper method to get a namespaced Identifier for the given name.
     *
     * @param name The name of the Identifier.
     * @return A namespaced Identifier with the mod's namespace and the given name.
     */
    public static Identifier GetIdentifier(String name) {
        return new Identifier("shrink_rays", name);
    }
}
