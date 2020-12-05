package ru.konstanteam.lokutils.render;

import ru.konstanteam.lokutils.render.context.GLContext;

import java.io.IOException;

public abstract class GLObject {
    protected int id;
    protected GLContext GLcontext;

    public abstract void generate() throws Exception;

    public abstract void delete() throws Exception;

    public abstract void bind() throws Exception;

    public abstract void unbind() throws Exception;
}
