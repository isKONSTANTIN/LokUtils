package ru.lokincompany.lokutils.ui.core.windows;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.windows.POINT;
import ru.lokincompany.lokutils.input.Inputs;
import ru.lokincompany.lokutils.objects.Color;
import ru.lokincompany.lokutils.objects.Point;
import ru.lokincompany.lokutils.objects.Rect;
import ru.lokincompany.lokutils.objects.Size;
import ru.lokincompany.lokutils.render.RenderPart;
import ru.lokincompany.lokutils.render.tools.GLFastTools;
import ru.lokincompany.lokutils.ui.eventsystem.Event;
import ru.lokincompany.lokutils.ui.objects.UICanvas;

import java.util.ArrayList;

public class UIWindow<T extends UICanvas> {
    protected Size contentSize;
    protected Point position = Point.ZERO;
    protected float barSize;
    protected T canvas;
    protected VirtualCanvas virtualCanvas;

    public UIWindow(T canvas){
        this.canvas = canvas;
        this.virtualCanvas = new VirtualCanvas();

        this.contentSize = new Size(150,150);
        this.canvas.setPosition(() -> Point.ZERO);
        this.canvas.setSize(() -> contentSize);
    }

    public T getCanvas() {
        return canvas;
    }

    public void handleBarEvent(Event event){

    }

    public void handleContentEvent(Event event){
        canvas.getCustomersContainer().handle(event);
    }

    public void renderBar(){
        Color background = canvas.getStyle().getColor("brightBackground");
        float glRadius = GLFastTools.getOptimalGlRadius(new Rect(Point.ZERO, contentSize),0.1f);
        barSize = Math.max(barSize, glRadius);

        if (barSize == glRadius)
            barSize = (float)Math.ceil(barSize);

        GL11.glColor4f(background.red, background.green, background.blue, background.alpha);

        GLFastTools.drawRoundedSquare(new Rect(Point.ZERO, contentSize.setHeight(barSize)), glRadius, GLFastTools.getOptimalRoundingPieces(glRadius), new boolean[]{true, true, false, false});
    }

    public void renderContent(){
        Rect contentField = new Rect(Point.ZERO, contentSize);
        Color background = canvas.getStyle().getColor("background");
        float glRadius = GLFastTools.getOptimalGlRadius(contentField,0.1f);

        GL11.glColor4f(background.red, background.green, background.blue, background.alpha);
        GLFastTools.drawRoundedSquare(contentField, glRadius, GLFastTools.getOptimalRoundingPieces(glRadius), new boolean[]{false, false, true, true});

        canvas.update(virtualCanvas);

        for (RenderPart renderPart : virtualCanvas.renderParts){
            renderPart.render();
        }

        virtualCanvas.renderParts.clear();
    }

    private static class VirtualCanvas extends UICanvas {
        public ArrayList<RenderPart> renderParts = new ArrayList<>();

        @Override
        public void addRenderPart(RenderPart renderPart) {
            renderParts.add(renderPart);
        }
    }
}
