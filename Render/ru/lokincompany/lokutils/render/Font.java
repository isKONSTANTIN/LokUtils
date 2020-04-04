package ru.lokincompany.lokutils.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import ru.lokincompany.lokutils.objects.Color;
import ru.lokincompany.lokutils.objects.Vector2i;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.opengl.GL11.*;

class Glyph {

    public final int width;
    public final int height;
    public final int x;
    public final int y;
    public final float advance;

    public Glyph(int width, int height, int x, int y, float advance) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.advance = advance;
    }
}

public class Font {
    private HashMap<Character, Glyph> glyphs;
    private Texture texture;
    private int fontHeight;
    private float spaceSize;

    public HashMap<Character, Glyph> getGlyphs() {
        return glyphs;
    }

    public Texture getTexture() {
        return texture;
    }

    public int getFontHeight() {
        return fontHeight;
    }

    public float getSpaceSize() {
        return spaceSize;
    }

    public Vector2f getSize(String text, Vector2f maxSize) {
        Vector2f result = new Vector2f(0, fontHeight);

        int drawX = 0;
        int drawY = 0;

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '\n') {
                drawY += fontHeight;
                result.y = Math.max(drawY, result.y);
                drawX = 0;
                continue;
            }
            if (ch == '\r') continue;

            Glyph g = glyphs.get(ch);

            if (g == null) {
                drawX += spaceSize;
                result.x = Math.max(drawX, result.x);
                continue;
            }

            if (maxSize != null) {
                if (maxSize.x > 0 && drawX + g.width > maxSize.x) {
                    if (maxSize.y > 0 && drawY + fontHeight + g.height > maxSize.y)
                        break;
                    drawX = 0;
                    drawY += fontHeight;
                }
            }

            int width = drawX + g.width;
            int height = drawY + g.height;

            result.x = Math.max(width, result.x);
            result.y = Math.max(height, result.y);

            spaceSize += g.width;
            spaceSize /= 2f;
            drawX += g.width;
        }

        return result;
    }

    public void inversionDrawText(String text, Vector2f position, Vector2f maxSize, Color color){
        float drawX = position.x + maxSize.x;
        float drawY = position.y;

        texture.bind();
        glBegin(GL_QUADS);

        for (int i = text.length()-1; i >= 0 ; i--) {
            char ch = text.charAt(i);

            if (ch == '\r') continue;

            Glyph g = glyphs.get(ch);

            if (g == null) {
                drawX -= spaceSize;
                continue;
            }

            drawX -= g.width;

            if (drawX < position.x)
                break;

            float width = drawX + g.width;
            float height = drawY + g.height;

            float glTexX = g.x / (float) texture.getSize().getX();
            float glTexY = g.y / (float) texture.getSize().getY();
            float glTexWidth = (g.x + g.width) / (float) texture.getSize().getX();
            float glTexHeight = (g.y + g.height) / (float) texture.getSize().getY();

            glColor4d(color.getRawRed(), color.getRawGreen(), color.getRawBlue(), color.getRawAlpha());

            glTexCoord2f(glTexX, glTexHeight);
            glVertex3f(drawX, drawY, 0);

            glTexCoord2f(glTexWidth, glTexHeight);
            glVertex3f(width, drawY, 0);

            glTexCoord2f(glTexWidth, glTexY);
            glVertex3f(width, height, 0);

            glTexCoord2f(glTexX, glTexY);
            glVertex3f(drawX, height, 0);

            spaceSize += g.width;
            spaceSize /= 2f;
        }
        glEnd();
        GL11.glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void drawText(String text, Vector2f position, Vector2f maxSize, Color color) {
        float drawX = position.x;
        float drawY = position.y;

        texture.bind();
        glBegin(GL_QUADS);

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '\n') {
                drawY += fontHeight;
                drawX = position.x;
                continue;
            }
            if (ch == '\r') continue;

            Glyph g = glyphs.get(ch);

            if (g == null) {
                drawX += spaceSize;
                continue;
            }

            if (maxSize != null) {
                if (maxSize.x > 0 && drawX + g.width > maxSize.x + position.x) {
                    if (maxSize.y > 0 && drawY + fontHeight + g.height > maxSize.y + position.y)
                        break;
                    drawX = position.x;
                    drawY += fontHeight;
                }
            }

            float width = drawX + g.width;
            float height = drawY + g.height;

            float glTexX = g.x / (float) texture.getSize().getX();
            float glTexY = g.y / (float) texture.getSize().getY();
            float glTexWidth = (g.x + g.width) / (float) texture.getSize().getX();
            float glTexHeight = (g.y + g.height) / (float) texture.getSize().getY();

            glColor4d(color.getRawRed(), color.getRawGreen(), color.getRawBlue(), color.getRawAlpha());

            glTexCoord2f(glTexX, glTexHeight);
            glVertex3f(drawX, drawY, 0);

            glTexCoord2f(glTexWidth, glTexHeight);
            glVertex3f(width, drawY, 0);

            glTexCoord2f(glTexWidth, glTexY);
            glVertex3f(width, height, 0);

            glTexCoord2f(glTexX, glTexY);
            glVertex3f(drawX, height, 0);

            spaceSize += g.width;
            spaceSize /= 2f;
            drawX += g.width;
        }
        glEnd();
        GL11.glBindTexture(GL_TEXTURE_2D, 0);
    }

    public Font load() {
        return load(new java.awt.Font(null, java.awt.Font.PLAIN, 14), "");
    }

    public Font load(java.awt.Font font) {
        return load(font, "");
    }

    public Font load(java.awt.Font font, String additionalSymbols) {
        String russianAlphabet = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
        String symbols = russianAlphabet.toUpperCase() + russianAlphabet + additionalSymbols;

        loadBasic(font, symbols);

        return this;
    }

    private void loadBasic(java.awt.Font font, String symbols) {
        long context = glfwGetCurrentContext();

        HashMap<Character, Glyph> glyphs = new HashMap<>();
        HashMap<Character, BufferedImage> bufferedImages = new HashMap<>();

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 32; i < 256; i++) {
            if (i == 127) {
                continue;
            }
            stringBuilder.append((char) i);
        }

        symbols = stringBuilder.append(symbols).toString();

        char[] chars = symbols.toCharArray();

        int imageWidth = 0;
        int imageHeight = 0;

        for (char c : chars) {
            if (c == 127) {
                continue;
            }
            BufferedImage ch = createCharImage(font, c);
            if (ch == null) {
                continue;
            }

            bufferedImages.put(c, ch);
            imageWidth += ch.getWidth();
            imageHeight = Math.max(imageHeight, ch.getHeight());
        }

        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        int x = 0;

        for (char c : chars) {
            if (c == 127) {
                continue;
            }
            if (!bufferedImages.containsKey(c)) continue;

            BufferedImage charImage = bufferedImages.get(c);
            int charHeight = charImage.getHeight();

            Glyph glyph = new Glyph(charImage.getWidth(), charHeight, x, image.getHeight() - charHeight, 0f);
            g.drawImage(charImage, x, 0, null);
            x += glyph.width;
            glyphs.put(c, glyph);
        }

        AffineTransform transform = AffineTransform.getScaleInstance(1f, -1f);
        transform.translate(0, -image.getHeight());
        AffineTransformOp operation = new AffineTransformOp(transform,
                AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = operation.filter(image, null);

        int width = image.getWidth();
        int height = image.getHeight();

        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = pixels[i * width + j];
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        buffer.flip();

        this.texture = new Texture().load(buffer, new Vector2i(width, height));
        this.glyphs = glyphs;
        this.fontHeight = imageHeight;
    }

    private BufferedImage createCharImage(java.awt.Font font, char c) {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        g.dispose();

        int charWidth = metrics.charWidth(c);
        int charHeight = metrics.getHeight();

        if (charWidth == 0) {
            return null;
        }

        image = new BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);
        g.setPaint(java.awt.Color.WHITE);
        g.drawString(String.valueOf(c), 0, metrics.getAscent());
        g.dispose();

        return image;
    }

    public void dispose() {
        texture.delete();
    }
}
