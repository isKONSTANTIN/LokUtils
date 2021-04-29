package ru.konstanteam.lokutils.testing;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import ru.konstanteam.lokutils.applications.Application;
import ru.konstanteam.lokutils.gui.core.GUIShader;
import ru.konstanteam.lokutils.gui.core.maincanvas.GUIMainCanvasSystem;
import ru.konstanteam.lokutils.gui.eventsystem.events.MouseMoveEvent;
import ru.konstanteam.lokutils.gui.eventsystem.events.MouseScrollEvent;
import ru.konstanteam.lokutils.objects.*;
import ru.konstanteam.lokutils.render.Window;
import ru.konstanteam.lokutils.render.context.GLContext;
import ru.konstanteam.lokutils.render.tools.GUIRenderBuffer;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.GLFW_TRANSPARENT_FRAMEBUFFER;
import static org.lwjgl.opengl.GL11.*;

public class ShaderTest extends Application<GUIMainCanvasSystem> {
    MarbleShader shader;

    public ShaderTest() {
        super(new GUIMainCanvasSystem(), new Window().setResizable(true).setHint(GLFW_TRANSPARENT_FRAMEBUFFER, 1));
    }

    @Override
    public void renderEvent() {
        shader.update(GUIController.getLayout().size().get());

        glClearColor(0,0,0, 0);
        glClear(GL_COLOR_BUFFER_BIT);

        GUIRenderBuffer renderBuffer = GLContext.getCurrent().getViewTools().getGuiRenderBuffer();

        renderBuffer.begin();

        renderBuffer.addVertex(new Rect(GUIController.getLayout().size().get()));

        renderBuffer.draw(GL_QUADS, Color.WHITE, shader);
    }

    @Override
    public void initEvent() {
        window.setWindowCloseCallback((e) -> this.close());

        GUIController.getLayout().getCustomersContainer().setCustomer(MouseMoveEvent.class, event -> {
            Point delta = event.lastPosition.relativeTo(event.endPosition);

            shader.addForce(new Vector2f(delta.x, delta.y), 0.4f);
        });

        GUIController.getLayout().getCustomersContainer().setCustomer(MouseScrollEvent.class, event -> {
            shader.addZoomForce(event.scrollDelta.y / 20f);
        });

        try {
            shader = new MarbleShader();
        } catch (IOException e) {
            e.printStackTrace();
            this.close();
        }
    }
}

class MarbleShader extends GUIShader {

    protected Vector2f rotation = new Vector2f();
    protected Vector2f movement = new Vector2f();
    protected float zoom = 1.3f;
    protected float zoomMovement;

    public MarbleShader() throws IOException {
        super("#/ru/konstanteam/lokutils/resources/shaders/testing/vert.glsl", "#/ru/konstanteam/lokutils/resources/shaders/testing/frag.glsl");
    }

    public void addForce(Vector2f direction, float strength){
        movement.x += direction.x * strength;
        movement.y += direction.y * strength;
    }

    public void addForce(Vector2f direction){
        addForce(direction, 1);
    }

    public void addZoomForce(float strength){
        zoomMovement += strength;
    }

    @Override
    public void update(Size windowResolution) {
        super.update(windowResolution);

        rotation.x += movement.x;
        rotation.y += movement.y;

        movement.x /= 1.2f;
        movement.y /= 1.2f;

        zoom += zoomMovement;
        zoom = Math.max(zoom, 0.4f);

        zoomMovement /= 1.1f;

        bind();
        setUniformData("time", System.nanoTime() / 1000000000f);
        setUniformData("iMouse", new Vector3f(rotation.x, rotation.y, 1));
        setUniformData("zoom", zoom);
        setUniformData("iResolution", new Vector2f(windowResolution.width, windowResolution.height));
        unbind();
    }
}