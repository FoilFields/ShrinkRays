package foilfields.shrinkrays.blocks;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;

public class ShrinkRay extends AbstractBeamCaster {
    public ShrinkRay(Settings settings) {
        super(settings);
    }

    @Override
    public void onHitEntity(Entity entity) {
        ScaleData scaleData = ScaleData.Builder.create().entity(entity).type(ScaleTypes.BASE).build();

        scaleData.setScale(Math.max(scaleData.getScale() - 0.003f, 0.05f));
    }
}
