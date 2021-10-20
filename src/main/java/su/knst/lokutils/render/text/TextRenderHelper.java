package su.knst.lokutils.render.text;

import org.lwjgl.util.vector.Vector2f;
import su.knst.lokutils.objects.Color;
import su.knst.lokutils.objects.Rect;
import su.knst.lokutils.objects.Size;
import su.knst.lokutils.render.Texture;
import su.knst.lokutils.render.context.GLContext;
import su.knst.lokutils.render.tools.GUIRenderBuffer;

import static org.lwjgl.opengl.GL11.GL_QUADS;

public class TextRenderHelper {
    public static Size getSize(Font font, String text, Size maxSize) {
        float fontHeight = font.getFontHeight();
        float fontSpaceSize = font.getSpaceSize();

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

            if (ch == '\r')
                continue;

            Glyph g = font.getGlyphs().get(ch);

            if (g == null) {
                drawX += fontSpaceSize;
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

            drawX += g.width;
        }

        return new Size(result);
    }

    public static void drawText(Font font, String text, Rect area, Color color) {
        Texture fontTexture = font.getTexture();

        if (fontTexture == null)
            return;

        float fontHeight = font.getFontHeight();
        float fontSpaceSize = font.getSpaceSize();

        float drawX = area.getX();
        float drawY = area.getY();

        GUIRenderBuffer buffer = GLContext.getCurrent().getViewTools().getGuiRenderBuffer();

        buffer.begin(font.getTexture());

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '\n') {
                drawY += fontHeight;
                drawX = area.getX();
                continue;
            }
            if (ch == '\r') continue;

            Glyph g = font.getGlyphs().get(ch);

            if (g == null) {
                drawX += fontSpaceSize;
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

            float glTexX = g.x / (float) fontTexture.getSize().getX();
            float glTexY = g.y / (float) fontTexture.getSize().getY();
            float glTexWidth = (g.x + g.width) / (float) fontTexture.getSize().getX();
            float glTexHeight = (g.y + g.height) / (float) fontTexture.getSize().getY();

            buffer.addRawTexCoord(glTexX, glTexHeight);
            buffer.addVertex(drawX, drawY);

            buffer.addRawTexCoord(glTexWidth, glTexHeight);
            buffer.addVertex(width, drawY);

            buffer.addRawTexCoord(glTexWidth, glTexY);
            buffer.addVertex(width, height);

            buffer.addRawTexCoord(glTexX, glTexY);
            buffer.addVertex(drawX, height);

            drawX += g.width;
        }

        buffer.draw(GL_QUADS, color);
    }
}
