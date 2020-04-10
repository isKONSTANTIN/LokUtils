package ru.lokincompany.lokutils.ui.eventsystem;

import ru.lokincompany.lokutils.input.Inputs;
import ru.lokincompany.lokutils.ui.UIObject;

public interface EventDetector {
    boolean detect(UIObject object, Inputs inputs);
}
