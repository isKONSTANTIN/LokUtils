package su.knst.lokutils.render.text;

import su.knst.lokutils.render.Texture;

import java.util.HashMap;
import java.util.Locale;

public abstract class AbstractFont {
    public static final String BASIC_SYMBOLS;

    static {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 32; i < 256; i++) { // basic
            if (i == 127)
                continue;

            stringBuilder.append((char) i);
        }

        String ru = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";

        stringBuilder.append(ru).append(ru.toUpperCase(Locale.ROOT));

        BASIC_SYMBOLS = stringBuilder.toString();
    }

    protected final HashMap<Character, Glyph> glyphs = new HashMap<>();
    protected Texture texture;
    protected float spaceSize;
    protected String name;
    protected Style style;
    protected int size;
    protected int fontHeight;
    protected String symbols;
    protected boolean loaded;

    public HashMap<Character, Glyph> getGlyphs() {
        loadIfNeeded();

        return glyphs;
    }

    public Texture getTexture() {
        loadIfNeeded();

        return texture;
    }

    public float getSpaceSize() {
        loadIfNeeded();

        return spaceSize;
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

    public int getFontHeight() {
        loadIfNeeded();

        return fontHeight;
    }

    public String getSymbols() {
        return symbols;
    }

    public boolean isLoaded() {
        return loaded;
    }

    protected void loadIfNeeded(){
        if (!isLoaded())
            load();

        loaded = true;
    }

    public abstract void load();
}
