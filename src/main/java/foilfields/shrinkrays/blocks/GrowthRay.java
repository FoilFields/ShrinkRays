package foilfields.shrinkrays.blocks;

import net.minecraft.entity.Entity;
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

    /**
     * Called when the GrowthRay beam hits an entity. Increases the size of the entity's scale.
     *
     * @param entity The entity hit by the GrowthRay beam.
     */
    @Override
    public void onHitEntity(Entity entity) {
        // Get the ScaleData of the entity and set the new scale value.
        ScaleData scaleData = ScaleData.Builder.create().entity(entity).type(ScaleTypes.BASE).build();
        scaleData.setScale(Math.min(scaleData.getScale() + 0.003f, 3));
    }
}