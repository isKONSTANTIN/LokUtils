package ru.lokincompany.lokutils.applications;

import ru.lokincompany.lokutils.render.Window;

public class ApplicationPreference {
    public Window window;

    public ApplicationPreference(){}

    public ApplicationPreference setWindow(Window window){
        this.window = window;

        return this;
    }

}
