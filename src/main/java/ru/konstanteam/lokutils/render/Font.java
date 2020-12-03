package ru.konstanteam.lokutils.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import ru.konstanteam.lokutils.objects.Color;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.objects.Vector2i;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

class Glyph {
    public final int width;
    public final int height;
    public final int x;
    public final int y;

    public Glyph(int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }
}

public class Font {
    private HashMap<Character, Glyph> glyphs;
    private Texture texture;
    private float fontHeight;
    private float spaceSize;

    public HashMap<Character, Glyph> getGlyphs() {
        return glyphs;
    }

    public Texture getTexture() {
        return texture;
    }

    public float getFontHeight() {
        return fontHeight;
    }

    public float getSpaceSize() {
        return spaceSize;
    }

    public Size getSize(String text, Size maxSize) {
        Vector2f result = new Vector2f(0, fontHeight);

        float drawX = 0;
        float drawY = 0;

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
                if (maxSize.width > 0 && drawX + g.width > maxSize.width) {
                    if (maxSize.height > 0 && drawY + fontHeight + g.height > maxSize.height)
                        break;
                    drawX = 0;
                    drawY += fontHeight;
                }
            }

            float width = drawX + g.width;
            float height = drawY + g.height;

            result.x = Math.max(width, result.x);
            result.y = Math.max(height, result.y);

            spaceSize += g.width;
            spaceSize /= 2f;
            drawX += g.width;
        }

        return new Size(result);
    }

    public void drawText(String text, Rect area, Color color) {
        float drawX = area.getX();
        float drawY = area.getY();

        texture.bind();
        glBegin(GL_QUADS);

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '\n') {
                drawY += fontHeight;
                drawX = area.getX();
                continue;
            }
            if (ch == '\r') continue;

            Glyph g = glyphs.get(ch);

            if (g == null) {
                drawX += spaceSize;
                continue;
            }

            if (area.size != null) {
                Size maxSize = area.size;
                if (maxSize.width > 0 && drawX + g.width > maxSize.width + area.getX()) {
                    if (maxSize.height > 0 && drawY + fontHeight + g.height > maxSize.height + area.getY())
                        break;
                    drawX = area.getX();
                    drawY += fontHeight;
                }
            }

            float width = drawX + g.width;
            float height = drawY + g.height;

            float glTexX = g.x / (float) texture.getSize().getX();
            float glTexY = g.y / (float) texture.getSize().getY();
            float glTexWidth = (g.x + g.width) / (float) texture.getSize().getX();
            float glTexHeight = (g.y + g.height) / (float) texture.getSize().getY();

            glColor4d(color.red, color.green, color.blue, color.alpha);

            glTexCoord2f(glTexX, glTexHeight);
            glVertex2f(drawX, drawY);

            glTexCoord2f(glTexWidth, glTexHeight);
            glVertex2f(width, drawY);

            glTexCoord2f(glTexWidth, glTexY);
            glVertex2f(width, height);

            glTexCoord2f(glTexX, glTexY);
            glVertex2f(drawX, height);

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
            imageWidth += ch.getWidth() + 1;
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

            Glyph glyph = new Glyph(charImage.getWidth(), charHeight, x, image.getHeight() - charHeight);
            g.drawImage(charImage, x, 0, null);
            x += glyph.width + 1;
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
