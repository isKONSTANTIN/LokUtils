package su.knst.lokutils.render.text;

import org.lwjgl.BufferUtils;
import su.knst.lokutils.objects.Size;
import su.knst.lokutils.render.Texture;

import java.awt.*;
import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class AWTFont extends AbstractFont {
    protected Font awtFont;

    public AWTFont(Font font, Style style, String additionalSymbols){
        this.name = font.getName();
        this.style = style;
        this.size = font.getSize();
        this.symbols = BASIC_SYMBOLS + additionalSymbols;
        this.awtFont = font;
    }

    public AWTFont(Font font, Style style){
        this(font, style, "");
    }

    public AWTFont(String name, int size, Style style, String additionalSymbols) {
        this(new Font(name, style.ordinal(), size), style, additionalSymbols);
    }

    public AWTFont(String name, int size, Style style) {
        this(new Font(name, style.ordinal(), size), style);
    }

    public AWTFont(String name, int size) {
        this(new Font(name, Style.PLAIN.ordinal(), size), Style.PLAIN);
    }

    protected AWTFont(){

    }

    @Override
    public void load() {
        HashMap<Character, BufferedImage> bufferedImages = new HashMap<>();

        int imageWidth = 0;
        int imageHeight = 0;

        char[] chars = symbols.toCharArray();

        for (char c : chars) {
            if (c == 127)
                continue;

            BufferedImage ch = createCharImage(c);
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

        for (char c : chars) {
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

        int[] pixels = new int[imageWidth * imageHeight];
        image.getRGB(0, 0, imageWidth, imageHeight, pixels, 0, imageWidth);

        ByteBuffer buffer = BufferUtils.createByteBuffer(pixels.length * 4);
        for (int i = 0; i < imageHeight; i++) {
            for (int j = 0; j < imageWidth; j++) {
                int pixel = pixels[i * imageWidth + j];

                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        buffer.flip();

        this.texture = new Texture().load(buffer, new Size(imageWidth, imageHeight));
        this.fontHeight = imageHeight;
    }

    protected BufferedImage createCharImage(char c) {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setFont(awtFont);
        FontMetrics metrics = g.getFontMetrics();
        g.dispose();

        int charWidth = metrics.charWidth(c);
        int charHeight = metrics.getHeight();

        if (charWidth == 0)
            return null;

        image = new BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(awtFont);
        g.setPaint(java.awt.Color.WHITE);
        g.drawString(String.valueOf(c), 0, metrics.getAscent());
        g.dispose();

        return image;
    }
}
