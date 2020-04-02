package ru.lokincompany.lokutils.render;

import org.lwjgl.util.vector.Vector3f;

public class Camera {

    protected Vector3f position = new Vector3f(0, 0, 0);
    protected Vector3f rotation = new Vector3f(0, 0, 0);
    protected float fov = 90;

    public void movePosition(float offsetX, float offsetY, float offsetZ) {
        if ( offsetZ != 0 ) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
            position.z += (float)Math.cos(Math.toRadians(rotation.y)) * offsetZ;
        }
        if ( offsetX != 0) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            position.z += (float)Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
        }
        position.y += offsetY;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Camera setPosition(Vector3f position) {
        this.position = position;

        return this;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Camera setRotation(Vector3f rotation) {
        this.rotation = rotation;

        return this;
    }

    public float getFov() {
        return fov;
    }

    public Camera setFov(float fov) {
        this.fov = fov;

        return this;
    }
}
