package ru.konstanteam.lokutils.testing;

import ru.konstanteam.lokutils.applications.Application;
import ru.konstanteam.lokutils.gui.core.maincanvas.GUIMainCanvasSystem;
import ru.konstanteam.lokutils.gui.layout.Alignment;
import ru.konstanteam.lokutils.gui.layout.ListLayout;
import ru.konstanteam.lokutils.gui.objects.button.GUIButton;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.objects.Vector2i;
import ru.konstanteam.lokutils.render.Window;
import ru.konstanteam.lokutils.tools.property.PropertyBasic;

import java.util.function.Supplier;

public class Main extends Application<GUIMainCanvasSystem> {
    public Main() {
        super(new GUIMainCanvasSystem(), new Window().setResizable(true).setTitle("LokUtils - testing menu").setResolution(new Vector2i(256,512)));
    }

    public static void main(String[] args) {

        PropertyBasic<Double> propertyBasic = new PropertyBasic<>(0d);

        propertyBasic.addInvalidationListener((i) -> System.out.println("Invalidated " + i));
        propertyBasic.addChangeListener((i, old, newV) -> System.out.println("Changed " + i + " from " + old + " to " + newV));
        propertyBasic.track(Math::random);

        PropertyBasic<Double> propertyBasic2 = new PropertyBasic<>(0d);
        propertyBasic2.addInvalidationListener((i) -> System.out.println("Invalidated " + i));
        propertyBasic2.addChangeListener((i, old, newV) -> System.out.println("Changed " + i + " from " + old + " to " + newV));

        propertyBasic2.track(propertyBasic);

        System.out.println(propertyBasic2.get());

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