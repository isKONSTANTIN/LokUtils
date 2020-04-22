package ru.lokincompany.lokutils.render;

public abstract class GLObject {
    protected int id;
    protected GLContext GLcontext;

    public abstract void generate();

    public abstract void delete();

    public abstract void bind();

    public abstract void unbind();
}
