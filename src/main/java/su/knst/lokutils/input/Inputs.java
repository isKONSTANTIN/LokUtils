package su.knst.lokutils.input;

public class Inputs {
    public final Keyboard keyboard;
    public final Mouse mouse;

    public Inputs() {
        keyboard = new Keyboard();
        mouse = new Mouse();
    }

    public void update() {
        mouse.update();
    }

}
