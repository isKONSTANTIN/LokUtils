package su.knst.lokutils.gui.core.shaders;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;
import su.knst.lokutils.objects.Color;
import su.knst.lokutils.objects.Size;
import su.knst.lokutils.objects.Vector2i;

import java.io.IOException;

public class BlurShader extends GUIShader {
    public BlurShader() throws IOException {
        super(
                "#/su/knst/lokutils/resources/shaders/gui/blur/vert.glsl",
                "#/su/knst/lokutils/resources/shaders/gui/blur/frag.glsl"
        );
    }

    @Override
    public void update(Size windowResolution) {
        super.update(windowResolution);

        bind();
        setUniformData("iResolution", new Vector2f(windowResolution.width, windowResolution.height));
        unbind();
    }
}
