package ru.lokincompany.render;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL15C;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;

public class VBO extends GLObject {
    protected int size;

    public VBO(){
    }

    @Override
    public void bind() {
        if (!GLContext.check(GLcontext)) throw new RuntimeException("VBO cannot be binded without or another OpenGL context!");

        GL15C.glBindBuffer(GL15C.GL_ARRAY_BUFFER, id);
    }

    @Override
    public void unbind() {
        if (GLContext.getCorrect() == null) throw new RuntimeException("VBO cannot be binded without OpenGL context!");

        GL15C.glBindBuffer(GL15C.GL_ARRAY_BUFFER, 0);
    }

    public int getID() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public VBO putData(float[] points){
        generate();

        bind();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, points, GL15.GL_DYNAMIC_DRAW);
        unbind();

        size = points.length;

        return this;
    }

    public VBO putData(ArrayList<Float> points) {
        float[] pointsArray = new float[points.size()];

        for (int i = 0; i < pointsArray.length; i++) {
            pointsArray[i] = points.get(i);
        }

        return putData(pointsArray);
    }

    public VBO putData(Vector2f[] points) {
        float[] floatPoints = new float[points.length * 2];

        for (int i = 0; i < points.length; i++) {
            floatPoints[i * 2] = points[i].x;
            floatPoints[i * 2 + 1] = points[i].x;
        }

        return putData(floatPoints);
    }

    @Override
    public void generate(){
        GLcontext = GLContext.getCorrect();
        if (GLcontext == null) throw new RuntimeException("VBO cannot be created without OpenGL context!");

        delete();
        id = GL15.glGenBuffers();
    }

    @Override
    public void delete() {
        if (id == 0) return;

        GLcontext = GLContext.getCorrect();
        if (GLcontext == null) throw new RuntimeException("VBO cannot be deleted without OpenGL context!");

        GL15.glDeleteBuffers(id);
        id = 0;
        size = 0;
    }
}
