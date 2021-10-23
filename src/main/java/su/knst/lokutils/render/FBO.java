package su.knst.lokutils.render;

import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.util.vector.Matrix4f;
import su.knst.lokutils.objects.Size;
import su.knst.lokutils.objects.Vector2i;
import su.knst.lokutils.objects.Vector4i;
import su.knst.lokutils.render.context.GLContext;
import su.knst.lokutils.render.text.AWTFont;
import su.knst.lokutils.render.texture.AbstractTexture;
import su.knst.lokutils.render.tools.MatrixTools;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.ARBFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

public class FBO extends GLObject{
    protected int frameBuffer;
    protected int depthBuffer;
    protected int textureBuffer;

    protected int lastFrameBuffer;

    protected int multisampledColorRenderBuffer;
    protected int multisampledDepthRenderBuffer;
    protected int multisampledFrameBuffer;

    protected int multisampleSamples;
    protected boolean multisampled;

    protected Size resolution = Size.ZERO;

    protected boolean generated;
    protected boolean needAlpha;

    public FBO() {

    }

    public boolean isNeedAlpha() {
        return needAlpha;
    }

    public void setNeedAlpha(boolean needAlpha) {
        this.needAlpha = needAlpha;
    }

    public Size getResolution() {
        return resolution;
    }

    public FBO setResolution(Size resolution) {
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

    public AbstractTexture getTextureBuffer() {
        if (multisampled)
            resolveMultisampled();

        AbstractTexture abstractTexture = new AbstractTexture() {
            @Override
            public void generate() {
                GLcontext = GLContext.getCurrent();
                if (GLcontext == null) throw new RuntimeException("Texture cannot be created without OpenGL context!");

                this.id = textureBuffer;
            }

            @Override
            public Size getSize() {
                return resolution;
            }
        };

        abstractTexture.generate();

        return abstractTexture;
    }

    public FBO applyChanges() {
        delete();
        generate();

        return this;
    }

    @Override
    public void generate() {
        GLcontext = GLContext.getCurrent();
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
        if (!GLContext.check(GLcontext))
            throw new RuntimeException("FBO cannot be binded without or another OpenGL context!");

        lastFrameBuffer = glGetInteger(GL_FRAMEBUFFER_BINDING);

        glBindFramebuffer(GL_FRAMEBUFFER, multisampled ? multisampledFrameBuffer : frameBuffer);
    }

    @Override
    public void unbind() {
        if (!GLContext.check(GLcontext))
            throw new RuntimeException("FBO cannot be unbinded without or another OpenGL context!");

        glBindFramebuffer(GL_FRAMEBUFFER, lastFrameBuffer);
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

        glRenderbufferStorageMultisample(GL_RENDERBUFFER, multisampleSamples, GL_RGBA8, (int)resolution.width, (int)resolution.height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_RENDERBUFFER, multisampledColorRenderBuffer);

        glBindRenderbuffer(GL_RENDERBUFFER, multisampledDepthRenderBuffer);
        glRenderbufferStorageMultisample(GL_RENDERBUFFER, multisampleSamples, ARBFramebufferObject.GL_DEPTH24_STENCIL8, (int)resolution.width, (int)resolution.height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, multisampledDepthRenderBuffer);

        int fboStatus = glCheckFramebufferStatus(GL_FRAMEBUFFER);
        if (fboStatus != GL_FRAMEBUFFER_COMPLETE) {
            throw new AssertionError("Could not create FBO: " + fboStatus);
        }

        glBindRenderbuffer(GL_RENDERBUFFER, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    private void resolveMultisampled() {
        int lastFrameBuffer = glGetInteger(GL_FRAMEBUFFER_BINDING);

        glBindFramebuffer(GL_READ_FRAMEBUFFER, multisampledFrameBuffer);
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, frameBuffer);

        glBlitFramebuffer(0, 0, (int)resolution.width, (int)resolution.height, 0, 0, (int)resolution.width, (int)resolution.height, GL_COLOR_BUFFER_BIT, GL_NEAREST);

        glBindFramebuffer(GL_FRAMEBUFFER, lastFrameBuffer);
    }

    private int createDepthAttachment() {
        int depth = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, depth);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH24_STENCIL8, (int)resolution.width, (int)resolution.height, 0, GL_DEPTH_STENCIL, GL_UNSIGNED_INT_24_8, (ByteBuffer) null);
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

        glTexImage2D(GL_TEXTURE_2D, 0, needAlpha ? GL_RGBA : GL_RGB, (int)resolution.width, (int)resolution.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, texture, 0);
        glBindTexture(GL_TEXTURE_2D, 0);

        return texture;
    }
}
