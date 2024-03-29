package su.knst.lokutils.render;

import org.lwjgl.opengl.GL30;
import su.knst.lokutils.render.context.GLContext;

public class VAO extends GLObject {

    public VAO() {
        generate();
    }

    @Override
    public void generate() {
        GLcontext = GLContext.getCurrent();
        if (GLcontext == null) throw new RuntimeException("VBO cannot be created without OpenGL context!");

        id = GL30.glGenVertexArrays();
    }

    @Override
    public void delete() {
        if (!GLContext.check(GLcontext))
            throw new RuntimeException("VBO cannot be binded without or another OpenGL context!");

        GL30.glDeleteVertexArrays(id);
        id = 0;
    }

    public void bind() {
        if (!GLContext.check(GLcontext))
            throw new RuntimeException("VBO cannot be binded without or another OpenGL context!");

        GL30.glBindVertexArray(id);
    }

    public void unbind() {
        if (GLContext.getCurrent() == null) throw new RuntimeException("VBO cannot be binded without OpenGL context!");

        GL30.glBindVertexArray(0);
    }

    public int getID() {
        return id;
    }

}
