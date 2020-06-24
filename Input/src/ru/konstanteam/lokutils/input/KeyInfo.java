package ru.konstanteam.lokutils.input;

public class KeyInfo {
    public char aChar;
    public int buttonID;
    public int action;
    public int mods;

    public KeyInfo(char aChar, int buttonID, int action, int mods) {
        this.aChar = aChar;
        this.buttonID = buttonID;
        this.action = action;
        this.mods = mods;
    }
}
