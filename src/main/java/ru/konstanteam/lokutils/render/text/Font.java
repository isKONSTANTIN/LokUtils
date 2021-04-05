package ru.konstanteam.lokutils.render.text;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector2f;
import ru.konstanteam.lokutils.objects.Color;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.objects.Vector2i;
import ru.konstanteam.lokutils.render.Texture;
import ru.konstanteam.lokutils.render.context.GLContext;
import ru.konstanteam.lokutils.render.tools.GUIRenderBuffer;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

public class Font {
    public static final ArrayList<String> ALPHABETS = new ArrayList<>();

    static {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 32; i < 256; i++) { // Basic
            if (i == 127)
                continue;

            stringBuilder.append((char) i);
        }
        ALPHABETS.add(stringBuilder.toString());

        ALPHABETS.add("абвгдеёжзийклмнопрстуфхцчшщъыьэюя"); // Russian
    }

    private final HashMap<Character, Glyph> glyphs = new HashMap<>();
    private Texture texture;
    private float fontHeight;
    private float spaceSize;

    private final String name;
    private final Style style;
    private final int size;
    private final String additionalSymbols;

    public Font(String name, int size, Style style, String additionalSymbols){
        this.name = name;
        this.style = style;
        this.size = size;
        this.additionalSymbols = additionalSymbols;

        load();
    }

    public Font(String name, int size, Style style){
        this(name, size, style, "");
    }

    public Font(String name, int size){
        this(name, size, Style.PLAIN);
    }

    public Font(String name){
        this(name, 14);
    }

    public Font(){
        this("");
    }

    public HashMap<Character, Glyph> getGlyphs() {
        return glyphs;
    }

    public Texture getTexture() {
        return texture;
    }

    public String getName() {
        return name;
    }

    public Style getStyle() {
        return style;
    }

    public int getSize() {
        return size;
    }

    public String getAdditionalSymbols() {
        return additionalSymbols;
    }

    public float getFontHeight() {
        return fontHeight;
    }

    public float getSpaceSize() {
        return spaceSize;
    }

    private Font load() {
        StringBuilder builder = new StringBuilder();

        for (String alps : ALPHABETS)
            builder.append(alps).append(alps.toUpperCase());

        builder.append(additionalSymbols);

        char[] symbols = new char[builder.length()];
        builder.getChars(0, builder.length(), symbols, 0);

        loadBasic(new java.awt.Font(name, style.ordinal(), size), symbols);

        return this;
    }

    private void loadBasic(java.awt.Font font, char[] symbols) {
        HashMap<Character, BufferedImage> bufferedImages = new HashMap<>();

        int imageWidth = 0;
        int imageHeight = 0;

        for (char c : symbols) {
            if (c == 127)
                continue;

            BufferedImage ch = createCharImage(font, c);
            if (ch == null)
                continue;

            bufferedImages.put(c, ch);
            imageWidth += ch.getWidth() + 1;
            imageHeight = Math.max(imageHeight, ch.getHeight());

            spaceSize += ch.getWidth();
            spaceSize /= 2.0f;
        }

        if (imageWidth == 0 || imageHeight == 0)
            return;

        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        int x = 0;

        for (char c : symbols) {
            if (c == 127)
                continue;

            if (!bufferedImages.containsKey(c))
                continue;

            BufferedImage charImage = bufferedImages.get(c);
            int charHeight = charImage.getHeight();

            Glyph glyph = new Glyph(charImage.getWidth(), charHeight, x, image.getHeight() - charHeight);
            g.drawImage(charImage, x, 0, null);
            x += glyph.width + 1;
            glyphs.put(c, glyph);
        }

        AffineTransform transform = AffineTransform.getScaleInstance(1f, -1f);
        transform.translate(0, -image.getHeight());
        AffineTransformOp operation = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
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

        if (charWidth == 0)
            return null;

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
