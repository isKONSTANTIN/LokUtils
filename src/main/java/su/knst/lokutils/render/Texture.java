package su.knst.lokutils.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import su.knst.lokutils.objects.Size;
import su.knst.lokutils.objects.Vector2i;
import su.knst.lokutils.render.context.GLContext;
import su.knst.lokutils.render.texture.AbstractTexture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.glActiveTexture;

public class Texture extends AbstractTexture {
    protected String path;

    public Texture() {

    }

    public Texture load(String path) throws IOException {
        BufferedImage image;
        if (path.charAt(0) == '#') {
            try {
                image = ImageIO.read(Texture.class.getResource(path.substring(1)));
            } catch (Exception e) {
                image = ImageIO.read(new File(path.substring(2)));
            }
        } else {
            image = ImageIO.read(new File(path));
        }
        this.size = new Size(image.getWidth(), image.getHeight());

        int texture_size = image.getWidth() * image.getHeight() * 4;
        int[] pixels = new int[image.getWidth() * image.getHeight()];

        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
        ByteBuffer textureBuffer = BufferUtils.createByteBuffer(texture_size);

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = pixels[y * image.getWidth() + x];
                textureBuffer.put((byte) ((pixel >> 16) & 0xFF));
                textureBuffer.put((byte) ((pixel >> 8) & 0xFF));
                textureBuffer.put((byte) (pixel & 0xFF));
                textureBuffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        textureBuffer.flip();

        this.path = path;

        return load(textureBuffer, size);
    }

    public Texture load(ByteBuffer byteBuffer, Size size) {
        delete();
        generate();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, (int)size.width, (int)size.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, byteBuffer);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        this.size = size;

        return this;
    }

    public String getPath() {
        return path;
    }

}
