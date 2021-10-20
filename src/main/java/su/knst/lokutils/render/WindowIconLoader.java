package su.knst.lokutils.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

public class WindowIconLoader {
    public static GLFWImage.Buffer load(String[] paths) {
        GLFWImage.Buffer iconGB = GLFWImage.malloc(paths.length);

        for (String path : paths) {
            try {
                BufferedImage image;
                if (path.charAt(0) == '#') {
                    try {
                        image = ImageIO.read(Texture.class.getResource(path.substring(1)));
                    }catch (Exception e){
                        image = ImageIO.read(new File(path.substring(2)));
                    }
                } else {
                    image = ImageIO.read(new File(path));
                }

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

                GLFWImage GLFWimage = GLFWImage.malloc().set(
                        image.getWidth(), image.getHeight(), textureBuffer
                );

                iconGB.put(GLFWimage);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        iconGB.flip();

        return iconGB;
    }
}
