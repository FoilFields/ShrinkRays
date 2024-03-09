package foilfields.shrinkrays;

import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.SectionHeader;

@Modmenu(modId = "shrink_rays")
@io.wispforest.owo.config.annotation.Config(name = "shrink_rays", wrapperName = "ShrinkRaysConfig")
public class Config {
    @SectionHeader("scale")
    public float minScale = 0.0625f;
    public float maxScale = 3.0f;
    @SectionHeader("speed")
    public float growthSpeed = 1.01f;
    public float returnSpeed = 1.01f;
    public float shrinkSpeed = 1.01f;
}
