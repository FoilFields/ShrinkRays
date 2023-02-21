package foilfields.shrinkrays.blocks;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;

public class ReturnRay extends AbstractBeamCaster {
    public ReturnRay(Settings settings) {
        super(settings);
    }

    @Override
    public void onHitEntity(Entity entity) {
        ScaleData scaleData = ScaleData.Builder.create().entity(entity).type(ScaleTypes.BASE).build();

        float scale = scaleData.getScale();

        if (Math.abs(1 - scale) < 0.003f) {
            scaleData.setScale(1);
        } else {
            scaleData.setScale(scale < 1 ? scale + 0.003f : scale - 0.003f);
        }
    }
}
