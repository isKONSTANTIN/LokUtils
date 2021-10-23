package su.knst.lokutils.render.text;

import java.awt.*;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileFont extends AWTFont {
    public FileFont(String path, float size, String additionalSymbols) throws IOException, FontFormatException {
        this.awtFont = Font.createFont(java.awt.Font.TRUETYPE_FONT, getStream(path)).deriveFont(size);

        this.name = awtFont.getName();
        this.style = Style.PLAIN;
        this.size = awtFont.getSize();
        this.symbols = BASIC_SYMBOLS + additionalSymbols;
    }

    public FileFont(String path, float size) throws IOException, FontFormatException {
        this(path, size, "");
    }

    protected FileFont(){

    }

    protected static InputStream getStream(String path) throws FileNotFoundException {
        return path.charAt(0) == '#' ? FileFont.class.getResourceAsStream(path.substring(1)) : new FileInputStream(path);
    }
}
