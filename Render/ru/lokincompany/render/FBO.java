package ru.lokincompany.render;

import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.lwjgl.util.vector.Matrix4f;
import ru.lokincompany.objects.Vector2i;
import ru.lokincompany.objects.Vector4i;
import ru.lokincompany.render.tools.MatrixTools;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.ARBFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL15.glIsQuery;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

public class FBO extends GLObject {
    
    protected int frameBuffer;
    protected int depthBuffer;
    protected int textureBuffer;

    protected int lastFrameBuffer;
    protected Vector4i lastViewport = new Vector4i();
    protected Matrix4f lastProjectionMatrix;
    protected Matrix4f lastModelMatrix;

    protected int multisampledColorRenderBuffer;
    protected int multisampledDepthRenderBuffer;
    protected int multisampledFrameBuffer;

    protected int multisampleSamples;
    protected boolean multisampled;

    protected Vector2i resolution = new Vector2i();
    protected Vector4i viewport;

    protected boolean generated;

    public FBO(){

    }

    public Vector4i getViewport() {
        return viewport;
    }

    public void setViewport(Vector4i viewport) {
        this.viewport = viewport;
    }

    public Vector2i getResolution() {
        return resolution;
    }

    public FBO setResolution(Vector2i resolution) {
        this.resolution = resolution;

        return this;
    }

    public int getMultisampleSamples() {
        return multisampleSamples;
    }

    public FBO setMultisampleSamples(int multisampleSamples) {
        this.multisampleSamples = multisampleSamples;

        return this;
    }

    public boolean isMultisampled() {
        return multisampled;
    }

    public FBO setMultisampled(boolean multisampled) {
        this.multisampled = multisampled;

        return this;
    }

    public int getTextureBuffer() {
        if (multisampled)
            resolveMultisampled();
        return textureBuffer;
    }

    public FBO applyChanges(){
        delete();
        generate();

        return this;
    }

    @Override
    public void generate() {
        GLcontext = GLContext.getCorrect();
        if (GLcontext == null) throw new RuntimeException("FBO cannot be created without OpenGL context!");

        initialiseFrameBuffer();
        if (multisampled)
            initialiseMultisampledFrameBuffer();

        generated = true;
    }

    @Override
    public void delete() {
        if (!generated) return;

        if (!GLContext.check(GLcontext))
            throw new RuntimeException("FBO cannot be deleted without or another OpenGL context!");

        glDeleteFramebuffers(frameBuffer);
        glDeleteTextures(textureBuffer);
        glDeleteTextures(depthBuffer);

        if (!multisampled) {
            generated = false;
            return;
        }

        glDeleteFramebuffers(multisampledFrameBuffer);
        glDeleteRenderbuffers(multisampledColorRenderBuffer);
        glDeleteRenderbuffers(multisampledDepthRenderBuffer);

        generated = false;
    }

    @Override
    public void bind() {
        if (!GLContext.check(GLcontext)) throw new RuntimeException("FBO cannot be binded without or another OpenGL context!");

        lastFrameBuffer = glGetInteger(GL_FRAMEBUFFER_BINDING);

        int[] view = new int[4];
        glGetIntegerv(GL_VIEWPORT, view);

        lastViewport.setX(view[0]);
        lastViewport.setY(view[1]);
        lastViewport.setZ(view[2]);
        lastViewport.setW(view[3]);

        lastProjectionMatrix = MatrixTools.getBindedMatrix(GL_PROJECTION_MATRIX);
        lastModelMatrix = MatrixTools.getBindedMatrix(GL_MODELVIEW_MATRIX);

        glBindFramebuffer(GL_FRAMEBUFFER, multisampled ? multisampledFrameBuffer : frameBuffer);

        if (viewport != null)
            glViewport(viewport.getX(), viewport.getY(), viewport.getZ(), viewport.getW());
        else
            glViewport(0,0, resolution.getX(), resolution.getY());

        gluOrtho2D(-1,1,-1,1);
    }

    @Override
    public void unbind() {
        if (!GLContext.check(GLcontext)) throw new RuntimeException("FBO cannot be unbinded without or another OpenGL context!");

        glBindFramebuffer(GL_FRAMEBUFFER, lastFrameBuffer);
        glViewport(lastViewport.getX(), lastViewport.getY(), lastViewport.getZ(), lastViewport.getW());
        MatrixTools.bindMatrix(lastProjectionMatrix, GL_PROJECTION);
        MatrixTools.bindMatrix(lastModelMatrix, GL_MODELVIEW);
    }

    private void initialiseFrameBuffer() {
        frameBuffer = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
        glDrawBuffer(GL_COLOR_ATTACHMENT0);

        textureBuffer = createTextureAttachment();
        depthBuffer = createDepthAttachment();

        glBindFramebuffer(GL_FRAMEBUFFER, lastFrameBuffer);
    }

    private void initialiseMultisampledFrameBuffer() {
        multisampledColorRenderBuffer = glGenRenderbuffers();
        multisampledDepthRenderBuffer = glGenRenderbuffers();
        multisampledFrameBuffer = glGenFramebuffers();

        glBindFramebuffer(GL_FRAMEBUFFER, multisampledFrameBuffer);
        glBindRenderbuffer(GL_RENDERBUFFER, multisampledColorRenderBuffer);

        glRenderbufferStorageMultisample(GL_RENDERBUFFER, multisampleSamples, GL_RGBA8, resolution.getX(), resolution.getY());
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_RENDERBUFFER, multisampledColorRenderBuffer);

        glBindRenderbuffer(GL_RENDERBUFFER, multisampledDepthRenderBuffer);
        glRenderbufferStorageMultisample(GL_RENDERBUFFER, multisampleSamples, ARBFramebufferObject.GL_DEPTH24_STENCIL8, resolution.getX(), resolution.getY());
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, multisampledDepthRenderBuffer);

        int fboStatus = glCheckFramebufferStatus(GL_FRAMEBUFFER);
        if (fboStatus != GL_FRAMEBUFFER_COMPLETE) {
            throw new AssertionError("Could not create FBO: " + fboStatus);
        }

        glBindRenderbuffer(GL_RENDERBUFFER, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    private void resolveMultisampled() {
        glBindFramebuffer(GL_READ_FRAMEBUFFER, multisampledFrameBuffer);
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, frameBuffer);
        glBlitFramebuffer(0, 0, resolution.getX(), resolution.getY(), 0, 0, resolution.getX(), resolution.getY(), GL_COLOR_BUFFER_BIT, GL_NEAREST);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    private int createDepthAttachment() {
        int depth = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, depth);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH24_STENCIL8, resolution.getX(), resolution.getY(), 0, GL_DEPTH_STENCIL, GL_UNSIGNED_INT_24_8, (ByteBuffer) null);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, depth, 0);
        glBindTexture(GL_TEXTURE_2D, 0);

        return depth;
    }

    private int createTextureAttachment() {
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, resolution.getX(), resolution.getY(), 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, texture, 0);
        glBindTexture(GL_TEXTURE_2D, 0);

        return texture;
    }
}
