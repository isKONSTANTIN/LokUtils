package su.knst.lokutils.testing;

import su.knst.lokutils.applications.Application;
import su.knst.lokutils.gui.core.maincanvas.GUIMainCanvasSystem;
import su.knst.lokutils.gui.layout.Alignment;
import su.knst.lokutils.gui.layout.ListLayout;
import su.knst.lokutils.gui.objects.button.GUIButton;
import su.knst.lokutils.objects.Size;
import su.knst.lokutils.objects.Vector2i;
import su.knst.lokutils.render.Window;
import su.knst.lokutils.tools.property.PropertyBasic;

import java.util.function.Supplier;

public class Main extends Application<GUIMainCanvasSystem> {
    public Main() {
        super(new GUIMainCanvasSystem(), new Window().setResizable(true).setTitle("LokUtils - testing menu").setResolution(new Vector2i(256,512)));
    }

    public static void main(String[] args) {
        new Main().open();
    }

    @Override
    public void initEvent() {
        this.window.setWindowCloseCallback((win) -> this.close());
        ListLayout list = new ListLayout();

        GUIController.getLayout().addObject(list, Alignment.CENTER);
        list.size().track(() -> GUIController.getLayout().size().get().relativeTo(10, 15), GUIController.getLayout().size());

        GUIButton fonts = new GUIButton("Fonts").setAction(() -> new FontsTest().open());
        GUIButton shader = new GUIButton("Shader").setAction(() -> new ShaderTest().open());
        GUIButton dialog = new GUIButton("Dialog").setAction(() -> new DialogTest().open());

        Supplier<Size> buttonsSize = () -> {
            Size size = list.size().get();

            return size.setHeight(size.height / 3);
        };

        fonts.size().track(buttonsSize, list.size());
        shader.size().track(buttonsSize, list.size());
        dialog.size().track(buttonsSize, list.size());

        list.addObject(fonts, Alignment.CENTER);
        list.addObject(shader, Alignment.CENTER);
        list.addObject(dialog, Alignment.CENTER);
    }
}