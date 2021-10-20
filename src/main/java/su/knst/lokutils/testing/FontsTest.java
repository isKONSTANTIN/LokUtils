package su.knst.lokutils.testing;

import su.knst.lokutils.applications.Application;
import su.knst.lokutils.gui.style.GUIStyle;
import su.knst.lokutils.gui.core.maincanvas.GUIMainCanvasSystem;
import su.knst.lokutils.gui.layout.Alignment;
import su.knst.lokutils.gui.layout.ListLayout;
import su.knst.lokutils.gui.objects.GUIText;
import su.knst.lokutils.gui.objects.margin.GUIMargin;
import su.knst.lokutils.gui.objects.margin.Margin;
import su.knst.lokutils.gui.panels.scroll.ScrollPanel;
import su.knst.lokutils.objects.Size;
import su.knst.lokutils.render.Window;

import java.awt.*;
import java.util.Locale;

public class FontsTest extends Application<GUIMainCanvasSystem> {
    public FontsTest() {
        super(new GUIMainCanvasSystem(), new Window().setTitle("LokUtils - fonts test"));
    }

    @Override
    public void initEvent() {
        this.window.setWindowCloseCallback((win) -> this.close());

        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();

        Font[] allFonts = ge.getAllFonts();
        ScrollPanel scrollPanel = new ScrollPanel();
        ListLayout listLayout = new ListLayout();
        scrollPanel.layout().addObject(listLayout, Alignment.TOP_LEFT);

        for (Font font : allFonts) {
            String fontName = font.getFontName(Locale.US);
            GUIStyle.getDefault().asset(GUIText.class).font(fontName, new su.knst.lokutils.render.text.Font(font.getName(), 14));
            GUIText text = new GUIText();
            text.setStyleFontName(fontName).string().set(fontName);

            listLayout.addObject(text);
        }

        listLayout.size().track(() -> listLayout.minimumSize().get().setWidth(scrollPanel.size().get().width), listLayout.minimumSize(), scrollPanel.size());

        scrollPanel.size().set(new Size(450,450));
        this.GUIController.getLayout().addObject(new GUIMargin(scrollPanel, new Margin(12)), Alignment.CENTER);
    }
}
