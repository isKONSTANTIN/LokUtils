package ru.konstanteam.lokutils.render;

import ru.konstanteam.lokutils.render.context.GLContext;

public abstract class GLObject {
    protected int id;
    protected GLContext GLcontext;

    public abstract void generate();

    public abstract void delete();

    public abstract void bind();

    public abstract void unbind();
}
