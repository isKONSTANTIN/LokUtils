package ru.lokincompany.lokutils.applications;

import ru.lokincompany.lokutils.render.Window;
import ru.lokincompany.lokutils.ui.core.UIController;

public class ApplicationPreference<T extends UIController> {
    public Window window;
    public Class<T>  uiController;

    public ApplicationPreference(Class<T> uiController){
        this.uiController = uiController;
    }

    public ApplicationPreference<T> setWindow(Window window){
        this.window = window;

        return this;
    }

}
