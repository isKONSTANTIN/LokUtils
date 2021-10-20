package su.knst.lokutils.input;

public class KeyInfo {
    public final char aChar;
    public final int buttonID;
    public final KeyAction action;
    public final int mods;

    public KeyInfo(char aChar, int buttonID, int action, int mods) {
        this.aChar = aChar;
        this.buttonID = buttonID;
        this.action = KeyAction.values()[action];
        this.mods = mods;
    }
}
