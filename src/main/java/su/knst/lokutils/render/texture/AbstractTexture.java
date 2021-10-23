package su.knst.lokutils.render.texture;

import su.knst.lokutils.objects.Size;
import su.knst.lokutils.render.GLObject;
import su.knst.lokutils.render.context.GLContext;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.glActiveTexture;

public abstract class AbstractTexture extends GLObject {
    protected Size size;

    @Override
    public void generate() {
        GLcontext = GLContext.getCurrent();
        if (GLcontext == null) throw new RuntimeException("Texture cannot be created without OpenGL context!");

        id = glGenTextures();
    }

    @Override
    public void delete() {
        if (id == 0) return;

        if (!GLContext.check(GLcontext))
            throw new RuntimeException("Texture cannot be deleted without or another OpenGL context!");

        glDeleteTextures(id);
        id = 0;
    }

    public void bind(int position) {
        if (!GLContext.check(GLcontext))
            throw new RuntimeException("Texture cannot be binded without or another OpenGL context!");

        glActiveTexture(GL_TEXTURE0 + position);
        glBindTexture(GL_TEXTURE_2D, id);
    }

    @Override
    public void bind() {
        bind(0);
    }

    @Override
    public void unbind() {
        if (GLContext.getCurrent() == null)
            throw new RuntimeException("Texture cannot be unbinded without OpenGL context!");

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public Size getSize() {
        return size;
    }
}
