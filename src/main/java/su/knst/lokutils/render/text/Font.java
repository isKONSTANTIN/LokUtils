package su.knst.lokutils.render.text;

import org.lwjgl.BufferUtils;
import su.knst.lokutils.objects.Vector2i;
import su.knst.lokutils.render.Texture;
import su.knst.lokutils.render.shader.Shader;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

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

    private String name;
    private Style style;
    private int size;
    private String additionalSymbols;

    public Font(String name, int size, Style style, String additionalSymbols){
        this.name = name;
        this.style = style;
        this.size = size;
        this.additionalSymbols = additionalSymbols;

        StringBuilder builder = new StringBuilder();

        for (String alps : ALPHABETS)
            builder.append(alps).append(alps.toUpperCase());

        builder.append(additionalSymbols);

        char[] symbols = new char[builder.length()];
        builder.getChars(0, builder.length(), symbols, 0);

        loadBasic(new java.awt.Font(name, style.ordinal(), size), symbols);
    }

    public Font(String name, int size, Style style){
        this(name, size, style, "");
    }

    public Font(String name, int size){
        this(name, size, Style.PLAIN);
    }

    public Font(String path, String additionalSymbols) {
        InputStream in;

        if (path.charAt(0) == '#') {
            in = Font.class.getResourceAsStream(path.substring(1));
        } else {
            try {
                in = new FileInputStream(path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();

                return;
            }
        }

        StringBuilder builder = new StringBuilder();

        for (String alps : ALPHABETS)
            builder.append(alps).append(alps.toUpperCase());

        this.additionalSymbols = additionalSymbols;

        builder.append(additionalSymbols);

        char[] symbols = new char[builder.length()];
        builder.getChars(0, builder.length(), symbols, 0);

        java.awt.Font jFont = null;
        try {
            jFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, in).deriveFont(16f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return;
        }

        this.name = jFont.getName();
        this.style = Style.PLAIN;
        this.size = jFont.getSize();

        loadBasic(jFont, symbols);
    }

    public Font(){
        this("", 14);
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
