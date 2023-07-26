package foilfields.shrinkrays.blocks;

import net.minecraft.entity.Entity;
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

    /**
     * Called when the ReturnRay beam hits an entity. Adjusts the size of the entity to its original scale or slightly changes it to approach the original scale.
     *
     * @param entity The entity hit by the ReturnRay beam.
     */
    @Override
    public void onHitEntity(Entity entity) {
        // Get the ScaleData of the entity and retrieve its current scale value.
        ScaleData scaleData = ScaleData.Builder.create().entity(entity).type(ScaleTypes.BASE).build();
        float scale = scaleData.getScale();

        // Check if the entity is close to the original scale (1). If so, set the scale to exactly 1; otherwise, slightly adjust the scale.
        if (Math.abs(1 - scale) < 0.003f) {
            scaleData.setScale(1);
        } else {
            scaleData.setScale(scale < 1 ? scale + 0.003f : scale - 0.003f);
        }
    }
}
