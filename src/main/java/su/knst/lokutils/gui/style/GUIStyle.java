package su.knst.lokutils.gui.style;

import su.knst.lokutils.gui.core.windows.bar.GUIWindowBar;
import su.knst.lokutils.gui.objects.*;
import su.knst.lokutils.gui.objects.button.GUIButton;
import su.knst.lokutils.gui.objects.slider.GUISlider;
import su.knst.lokutils.gui.objects.slider.SliderHead;
import su.knst.lokutils.gui.panels.scroll.HorizontalScrollBar;
import su.knst.lokutils.gui.panels.scroll.VerticalScrollBar;
import su.knst.lokutils.objects.Color;
import su.knst.lokutils.objects.ColorRGB;
import su.knst.lokutils.render.context.GLContext;
import su.knst.lokutils.render.text.AWTFont;
import su.knst.lokutils.render.text.FileFont;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

public class GUIStyle {
    private static HashMap<GLContext, GUIStyle> defaultStyles = new HashMap<>();
    protected HashMap<Class, GUIObjectAsset> assets = new HashMap<>();

    public static GUIStyle generateDefaultStyle() throws IOException, FontFormatException {
        if (GLContext.getCurrent() == null)
            throw new RuntimeException("Default style cannot be generated without OpenGL context!");

        GUIStyle defaultStyle = new GUIStyle();

        defaultStyle.asset(GUIBlackout.class)
                .color("background", new Color(0.15f, 0.15f, 0.15f, 0.8f))
                .color("brightBackground", new Color(0.2f, 0.2f, 0.2f, 0.8f));

        defaultStyle.asset(GUIButton.class)
                .color("pressed", new Color(0.38f, 0.38f, 0.38f, 0.8f))
                .color("pointed", new Color(0.33f, 0.33f, 0.33f, 0.8f))
                .color("background", new Color(0.2f, 0.2f, 0.2f, 0.8f))
                .color("text", new Color(0.95f, 0.95f, 0.95f, 1));

        defaultStyle.asset(GUIWindowBar.class)
                .color("closeButtonPressed", new ColorRGB(255, 170, 170, 255))
                .color("closeButtonBackground", new ColorRGB(255, 100, 100, 255))
                .color("minimizeButtonPressed", new ColorRGB(255, 255, 170, 255))
                .color("minimizeButtonBackground", new ColorRGB(255, 230, 100, 255))
                .color("contentBackground", new Color(0.3f, 0.3f, 0.3f, 0.9f))
                .color("background", new Color(0.25f, 0.25f, 0.25f, 0.8f));

        defaultStyle.asset(GUIText.class)
                .color("text", new Color(0.15f, 0.15f, 0.15f, 1))
                .color("highlighted", new Color(0.2f, 0.2f, 0.2f, 1))
                .font("default", new FileFont("#/su/knst/lokutils/resources/fonts/Lato-Regular.ttf", 16))
                .font("windowTitle", new AWTFont("Aria", 10))
                .font("textFieldTitle", new AWTFont("TimesRomans", 11));

        defaultStyle.asset(GUITextField.class)
                .color("text", new Color(0.95f, 0.95f, 0.95f, 1))
                .color("background", new Color(0.2f, 0.2f, 0.2f, 0.8f))
                .object("rounded", 0.3f);

        defaultStyle.asset(GUICheckBox.class)
                .color("stroke", new Color(0.7f, 0.7f, 0.7f, 1))
                .color("fill", new Color(0.4f, 0.4f, 0.4f, 1));

        defaultStyle.asset(GUISeparate.class)
                .color("color", new Color(0.6f, 0.6f, 0.6f, 0.9f))
                .object("lineWidth", 1f);

        defaultStyle.asset(GUISlider.class)
                .color("fullness", new Color(0.4f, 0.4f, 0.4f, 1))
                .color("background", new Color(0.2f, 0.2f, 0.2f, 1));

        defaultStyle.asset(SliderHead.class)
                .color("head", new Color(0.5f, 0.5f, 0.5f, 1));

        defaultStyle.asset(VerticalScrollBar.class)
                .color("background", new Color(0.8f, 0.8f, 0.8f, 0.5f))
                .color("head", new Color(0.4f, 0.4f, 0.4f, 0.8f));

        defaultStyle.asset(HorizontalScrollBar.class)
                .color("background", new Color(0.8f, 0.8f, 0.8f, 0.5f))
                .color("head", new Color(0.4f, 0.4f, 0.4f, 0.8f));

        return defaultStyle;
    }

    public static GUIStyle getDefault() {
        GLContext context = GLContext.getCurrent();
        if (context == null)
            return null;

        if (!defaultStyles.containsKey(context)) {
            try {
                setDefault(generateDefaultStyle());
            } catch (IOException | FontFormatException e) {
                e.printStackTrace();

                return null;
            }
        }

        return defaultStyles.get(context);
    }

    public static void setDefault(GUIStyle style) {
        defaultStyles.put(GLContext.getCurrent(), style);
    }

    public <T> GUIObjectAsset<T> asset(Class<T> tClass){
        if (!assets.containsKey(tClass))
            assets.put(tClass, new GUIObjectAsset<>(tClass));

        return assets.get(tClass);
    }
}
