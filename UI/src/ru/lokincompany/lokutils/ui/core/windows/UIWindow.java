package ru.lokincompany.lokutils.ui.core.windows;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.windows.POINT;
import org.lwjgl.system.windows.WindowsUtil;
import ru.lokincompany.lokutils.input.Inputs;
import ru.lokincompany.lokutils.objects.*;
import ru.lokincompany.lokutils.render.RenderPart;
import ru.lokincompany.lokutils.render.tools.GLFastTools;
import ru.lokincompany.lokutils.ui.UIStyle;
import ru.lokincompany.lokutils.ui.eventsystem.CustomersContainer;
import ru.lokincompany.lokutils.ui.eventsystem.Event;
import ru.lokincompany.lokutils.ui.eventsystem.events.MouseClickedEvent;
import ru.lokincompany.lokutils.ui.objects.UICanvas;

import java.util.ArrayList;

public class UIWindow<T extends UICanvas> {
    protected Size contentSize;
    protected Point position = Point.ZERO;
    protected float barSize = 14;
    protected float buttonsSize;
    protected T canvas;
    protected VirtualCanvas virtualCanvas;

    protected WindowButton closeButton;
    protected WindowButton minimizeButton;

    protected UIWindowSystem windowSystem;
    protected UIStyle style;

    protected boolean minimized;

    public UIWindow(T canvas) {
        this.canvas = canvas;
        this.virtualCanvas = new VirtualCanvas();

        this.contentSize = new Size(150, 150);
        this.canvas.setPosition(() -> Point.ZERO);
        this.canvas.setSize(() -> contentSize);

        this.buttonsSize = barSize / 1.3f;
    }

    public UIWindowSystem getWindowSystem() {
        return windowSystem;
    }

    public UIStyle getStyle() {
        return style;
    }

    public void setStyle(UIStyle style) {
        this.style = style;
    }

    public T getCanvas() {
        return canvas;
    }

    public Rect getField() {
        return new Rect(position, contentSize.offset(0, barSize));
    }

    public Size getContentSize() {
        return contentSize;
    }

    public void setContentSize(Size contentSize) {
        this.contentSize = contentSize;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void init(UIWindowSystem windowSystem) {
        this.windowSystem = windowSystem;

        if (this.style == null)
            this.style = windowSystem.getStyle();

        this.closeButton = new WindowButton(
                new Circle(new Point(contentSize.width - buttonsSize, barSize / 2f), buttonsSize / 2f), this,
                style.getColor("windowCloseButtonPressed"),
                style.getColor("windowCloseButtonBackground"),
                () -> windowSystem.closeWindow(this)
        );

        this.minimizeButton = new WindowButton(
                new Circle(new Point(contentSize.width - buttonsSize * 2.2f, barSize / 2f), buttonsSize / 2f), this,
                style.getColor("windowMinimizeButtonPressed"),
                style.getColor("windowMinimizeButtonBackground"),
                () -> minimized = !minimized
        );
    }

    public void handleBarEvent(Event event){
        closeButton.getCustomersContainer().handle(event);
        minimizeButton.getCustomersContainer().handle(event);
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
        closeButton.render();
        minimizeButton.render();
    }

    public void renderContent(){
        if (minimized) return;

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
