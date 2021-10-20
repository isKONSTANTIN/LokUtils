package su.knst.lokutils.tools;

public class RuntimeTools {
    private float deltaTime;
    private int fps;
    private long lastUpdateTime;
    private long lastFPSUpdateTime;

    public float getDeltaTime() {
        return deltaTime / 16.66666f;
    }

    public int getFps() {
        return fps;
    }

    public void init() {
        lastUpdateTime = System.nanoTime();
        lastFPSUpdateTime = System.nanoTime();
    }

    public void update() {
        long timeNow = System.nanoTime();

        deltaTime = (timeNow - lastUpdateTime) / 1000000f;
        lastUpdateTime = timeNow;

        if (timeNow - lastFPSUpdateTime >= 1000000000) {
            lastFPSUpdateTime = timeNow;
            fps = Math.round(1000f / deltaTime);
        }
    }

}
