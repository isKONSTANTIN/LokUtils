package su.knst.lokutils.gui.core;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;
import su.knst.lokutils.objects.Color;
import su.knst.lokutils.objects.Size;
import su.knst.lokutils.render.VBO;
import su.knst.lokutils.render.shader.Shader;
import su.knst.lokutils.render.tools.MatrixTools;
import su.knst.lokutils.render.tools.TranslateState;

import java.io.IOException;

public class GUIShader extends Shader {
    protected Size windowResolution = Size.ZERO;

    public GUIShader(String vertPath, String fragPath) throws IOException {
        super(vertPath, fragPath);

        bind();
        setTranslate(TranslateState.ZERO, true);
        setColor(Color.BLACK);
        unbind();
    }

    public GUIShader() throws IOException {
        this(
                "#/su/knst/lokutils/resources/shaders/gui/default/vert.glsl",
                "#/su/knst/lokutils/resources/shaders/gui/default/frag.glsl"
        );
    }

    public void update(Size windowResolution){
        this.windowResolution = windowResolution;

        bind();
        setUniformData("projection_matrix", MatrixTools.createOrthographicMatrix(-windowResolution.width / 2f, windowResolution.width / 2f, windowResolution.height / 2f, -windowResolution.height / 2f,0.05f,9999));
        unbind();
    }

    public void setTranslate(TranslateState translate, boolean canSmooth){
        float x = translate.global.x;
        float y = translate.global.y;

        setUniformData("view_matrix", ((Matrix4f)new Matrix4f().setIdentity()).translate(
                new Vector2f(
                        (canSmooth ? x : (int)x) -windowResolution.width / 2,
                        (canSmooth ? y : (int)y) -windowResolution.height / 2
                )
        ));
    }

    public void setColor(Color color){
        setUniformData("color", new Vector4f(color.red, color.green, color.blue, color.alpha));
    }

    public void setUseTexture(boolean status){
        setUniformData("useTexture", status);
    }

    public void setVertexData(VBO data){
        setAttributeData("vertex", data, 2);
    }

    public void setUVData(VBO data){
        setAttributeData("frag_uv", data, 2);
    }
}
