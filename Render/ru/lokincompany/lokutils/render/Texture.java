package ru.lokincompany.lokutils.render;

import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

public class Texture extends GLObject{
    protected String path;

    @Override
    public void generate() {
        GLcontext = GLContext.getCorrect();
        if (GLcontext == null) throw new RuntimeException("VBO cannot be created without OpenGL context!");

        id = GL30.glGenVertexArrays();
    }

    @Override
    public void delete() {
        if (!GLContext.check(GLcontext)) throw new RuntimeException("VBO cannot be binded without or another OpenGL context!");

        GL30.glDeleteTextures(id);
        id = 0;
    }

    public void bind() {
        if (!GLContext.check(GLcontext)) throw new RuntimeException("VBO cannot be binded without or another OpenGL context!");

        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void unbind() {
        if (GLContext.getCorrect() == null) throw new RuntimeException("VBO cannot be binded without OpenGL context!");

        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
