package foilfields.shrinkrays.client;

import foilfields.shrinkrays.ShrinkRays;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class ShrinkRaysClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ShrinkRays.SHRINK_RAY, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ShrinkRays.GROWTH_RAY, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ShrinkRays.RETURN_RAY, RenderLayer.getTranslucent());
    }
}
