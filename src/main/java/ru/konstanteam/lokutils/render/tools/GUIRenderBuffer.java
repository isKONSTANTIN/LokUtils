package ru.konstanteam.lokutils.render.tools;

import org.lwjgl.opengl.GL20C;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import ru.konstanteam.lokutils.gui.core.GUIShader;
import ru.konstanteam.lokutils.objects.*;
import ru.konstanteam.lokutils.render.VAO;
import ru.konstanteam.lokutils.render.context.GLContext;
import ru.konstanteam.lokutils.render.shader.Shader;
import ru.konstanteam.lokutils.render.Texture;
import ru.konstanteam.lokutils.render.VBO;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class GUIRenderBuffer {
    protected VBO vbo;
    protected GUIShader shader;
    protected ArrayList<Float> buffer = new ArrayList<>();

    public GUIRenderBuffer(GUIShader shader){
        this.shader = shader;
    }

    public GUIShader getDefaultShader(){
        return shader;
    }

    public void begin(){
        if (vbo != null)
            return;

        this.vbo = new VBO();
    }

    public void addVertex(float f){
        buffer.add(f);
    }

    public void addVertex(float f1, float f2){
        buffer.add(f1);
        buffer.add(f2);
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

    public void draw(int type, Color color, Texture texture, GUIShader shader){
        buffer.add(buffer.get(0));
        buffer.add(buffer.get(1));

        vbo.putData(buffer);
        vbo.bind();
        GL20C.glEnableVertexAttribArray(0);
        GL20C.glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);

        shader.unbind();
        shader.setTranslate(GLContext.getCurrent().getViewTools().getCurrentTranslate());
        if (color == null){
            float[] currentColor = new float[4];

            glGetFloatv(GL_CURRENT_COLOR, currentColor);
            shader.setColor(new Color(currentColor[0], currentColor[1], currentColor[2], currentColor[3]));
        }else
            shader.setColor(color);

        if (texture != null) texture.bind();

        glDrawArrays(type, 0, vbo.getSize());

        GL20C.glDisableVertexAttribArray(0);
        vbo.unbind();
        shader.unbind();
        if (texture != null) texture.unbind();

        buffer.clear();
    }

    public void draw(int type, Color color, Texture texture){
        draw(type, color, texture, shader);
    }

    public void draw(int type, Color color){
        draw(type, color, null);
    }

    public void draw(int type){
        draw(type, null);
    }

    public void draw(){
        draw(GL_POLYGON);
    }
}
