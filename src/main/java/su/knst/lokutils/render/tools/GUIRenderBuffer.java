package su.knst.lokutils.render.tools;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import su.knst.lokutils.gui.core.GUIShader;
import su.knst.lokutils.objects.*;
import su.knst.lokutils.objects.Color;
import su.knst.lokutils.objects.Point;
import su.knst.lokutils.render.context.GLContext;
import su.knst.lokutils.render.Texture;
import su.knst.lokutils.render.VBO;
import su.knst.lokutils.objects.Rect;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class GUIRenderBuffer {
    protected VBO vertexVbo;
    protected VBO textureVbo;
    protected GUIShader shader;
    protected Texture texture;
    protected boolean ended;

    protected ArrayList<Float> vertexBuffer = new ArrayList<>();
    protected ArrayList<Float> texBuffer = new ArrayList<>();

    public GUIRenderBuffer(GUIShader shader){
        this.shader = shader;
    }

    public GUIShader getDefaultShader(){
        return shader;
    }

    public Texture getCurrentTexture(){
        return texture;
    }

    public void begin(Texture texture){
        if (vertexVbo == null)
            this.vertexVbo = new VBO();

        if (textureVbo == null)
            this.textureVbo = new VBO();

        this.texture = texture;

        this.vertexBuffer.clear();
        this.texBuffer.clear();

        this.ended = false;
    }

    public void begin(){
        begin(null);
    }

    public void addVertex(float f){
        vertexBuffer.add(f);
    }

    public void addVertex(float f1, float f2){
        vertexBuffer.add(f1);
        vertexBuffer.add(f2);
    }

    public void addVertex(Vector2f vector2f){
        addVertex(vector2f.getX());
        addVertex(vector2f.getY());
    }

    public void addVertex(Vector3f vector3f){
        addVertex(vector3f.getX());
        addVertex(vector3f.getY());
        addVertex(vector3f.getZ());
    }

    public void addVertex(Vector4f vector4f){
        addVertex(vector4f.getX());
        addVertex(vector4f.getY());
        addVertex(vector4f.getZ());
        addVertex(vector4f.getZ());
    }

    public void addVertex(Point point){
        addVertex(point.x);
        addVertex(point.y);
    }

    public void addVertex(Rect rect){
        addVertex(rect.getTopLeftPoint());
        addVertex(rect.getTopRightPoint());
        addVertex(rect.getBottomRightPoint());
        addVertex(rect.getBottomLeftPoint());
    }

    public void addRawTexCoord(float f){
        texBuffer.add(f);
    }

    public void addRawTexCoord(float x, float y){
        addRawTexCoord(x);
        addRawTexCoord(y);
    }

    public void addTexCoord(Point point){
        float x = point.x;
        float y = point.y;

        if (texture != null){
            x /= texture.getSize().getX();
            y /= texture.getSize().getX();
        }

        addRawTexCoord(x, y);
    }

    public void addTexCoord(Rect rect){
        addTexCoord(rect.getTopLeftPoint());
        addTexCoord(rect.getTopRightPoint());
        addTexCoord(rect.getBottomRightPoint());
        addTexCoord(rect.getBottomLeftPoint());
    }

    public void end(){
        ended = true;

        if (vertexBuffer.size() == 0)
            return;

        boolean textureActive = texture != null && texBuffer.size() > 0;

        vertexVbo.putData(vertexBuffer);

        if (textureActive)
            textureVbo.putData(texBuffer);
    }

    public void draw(int type, Color color, GUIShader shader){
        if (vertexBuffer.size() == 0)
            return;

        boolean textureActive = texture != null && texBuffer.size() > 0;

        if (!ended)
            end();

        shader.bind();
        shader.setVertexData(vertexVbo);
        shader.setTranslate(GLContext.getCurrent().getViewTools().getCurrentTranslate(), !textureActive);
        shader.setUseTexture(textureActive);

        if (textureActive) {
            texture.bind();
            shader.setUVData(textureVbo);
        }

        if (color == null && texture == null){
            float[] currentColor = new float[4];

            glGetFloatv(GL_CURRENT_COLOR, currentColor);
            shader.setColor(new Color(currentColor[0], currentColor[1], currentColor[2], currentColor[3]));
        }else {
            shader.setColor(color != null ? color : Color.WHITE);
        }

        glDrawArrays(type, 0, vertexVbo.getSize() / 2);

        shader.unbind();

        if (textureActive) texture.unbind();
    }

    public void draw(int type, Color color){
        draw(type, color, shader);
    }

    public void draw(int type){
        draw(type, null);
    }

    public void draw(){
        draw(GL_POLYGON);
    }
}
