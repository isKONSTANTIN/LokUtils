package ru.lokincompany.lokutils.ui.core.windows;

import org.lwjgl.opengl.GL11;
import ru.lokincompany.lokutils.objects.*;
import ru.lokincompany.lokutils.render.Font;
import ru.lokincompany.lokutils.render.GLContext;
import ru.lokincompany.lokutils.render.tools.GLFastTools;
import ru.lokincompany.lokutils.ui.UIStyle;
import ru.lokincompany.lokutils.ui.eventsystem.Event;
import ru.lokincompany.lokutils.ui.eventsystem.events.MouseMoveEvent;
import ru.lokincompany.lokutils.ui.eventsystem.events.MoveType;
import ru.lokincompany.lokutils.ui.objects.UICanvas;

import java.util.ArrayList;

public class UIWindow<T extends UICanvas> {
    protected Size contentSize;
    protected Point position = Point.ZERO;
    protected Point lastMoveDelta = Point.ZERO;
    protected float barSize = 14;
    protected float buttonsSize;

    protected WindowButton closeButton;
    protected WindowButton minimizeButton;

    protected T canvas;
    protected UIWindowSystem windowSystem;
    protected UIStyle style;
    protected boolean minimized;

    protected String title;

    public UIWindow(T canvas) {
        this.canvas = canvas;

        this.contentSize = new Size(150, 150);
        this.canvas.size().set(() -> contentSize);

        this.buttonsSize = barSize / 1.3f;
    }

    public boolean canClose(){
        return true;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Size getContentSize() {
        return contentSize;
    }

    public void setSize(Size contentSize) {
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
        if (canClose())
            closeButton.getCustomersContainer().handle(event);
        minimizeButton.getCustomersContainer().handle(event);

        if (event instanceof MouseMoveEvent){
            MouseMoveEvent mouseMoveEvent = (MouseMoveEvent) event;

            if (mouseMoveEvent.type == MoveType.STARTED){
                lastMoveDelta = mouseMoveEvent.deltaPositionChange;
            }
            if (lastMoveDelta != Point.ZERO){
                position = position.offset(mouseMoveEvent.deltaPositionChange.relativeTo(lastMoveDelta));
                lastMoveDelta = mouseMoveEvent.deltaPositionChange;
            }
        }else {
            lastMoveDelta = Point.ZERO;
        }
    }

    public void handleContentEvent(Event event){
        canvas.getCustomersContainer().handle(event);
    }

    public void renderBar(){
        Color background = canvas.getStyle().getColor("windowBarBackground");
        float glRadius = GLFastTools.getOptimalGlRadius(new Rect(Point.ZERO, contentSize),0.1f);
        barSize = Math.max(barSize, glRadius);

        if (barSize == glRadius)
            barSize = (float)Math.ceil(barSize);

        GL11.glColor4f(background.red, background.green, background.blue, background.alpha);
        GLFastTools.drawRoundedSquare(new Rect(Point.ZERO, contentSize.setHeight(barSize)), glRadius, GLFastTools.getOptimalRoundingPieces(glRadius), new boolean[]{true, true, false, false});

        if (title != null && title.length() > 0){
            Font font = style.getFont("windowTitle");
            Size titleSize = font.getSize(title, null);
            font.drawText(title, new Rect(5,barSize / 2 - titleSize.height / 2,0,0), Color.WHITE);
        }
        if (canClose())
            closeButton.render();
        minimizeButton.render();
    }

    public void renderContent(){
        if (minimized) return;

        Rect contentField = new Rect(Point.ZERO, contentSize);
        Color background = canvas.getStyle().getColor("windowContentBackground");
        float glRadius = GLFastTools.getOptimalGlRadius(contentField,0.1f);

        GL11.glColor4f(background.red, background.green, background.blue, background.alpha);
        GLFastTools.drawRoundedSquare(contentField, glRadius, GLFastTools.getOptimalRoundingPieces(glRadius), new boolean[]{false, false, true, true});

        canvas.update(null);

        GLContext.getCurrent().getViewTools().pushLook(new Rect(Point.ZERO, contentSize));
        canvas.render();
        GLContext.getCurrent().getViewTools().popLook();
    }

}
