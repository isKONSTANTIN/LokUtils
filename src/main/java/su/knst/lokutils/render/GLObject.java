package su.knst.lokutils.render;

import su.knst.lokutils.render.context.GLContext;

public abstract class GLObject {
    protected int id;
    protected GLContext GLcontext;

    public abstract void generate() throws Exception;

    public abstract void delete() throws Exception;

    public abstract void bind() throws Exception;

    public abstract void unbind() throws Exception;
}
