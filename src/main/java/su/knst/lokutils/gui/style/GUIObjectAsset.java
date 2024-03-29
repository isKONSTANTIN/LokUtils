package su.knst.lokutils.gui.style;

import su.knst.lokutils.objects.Color;
import su.knst.lokutils.render.text.AbstractFont;

import java.util.HashMap;

public class GUIObjectAsset<T> {
    protected final Class<T> guiObjectClass;
    protected final HashMap<String, Object> parameters = new HashMap<>();

    public GUIObjectAsset(Class<T> guiObjectClass){
        this.guiObjectClass = guiObjectClass;
    }

    public Class<T> getGuiObjectClass() {
        return guiObjectClass;
    }

    protected <X> X validOrDefault(Object object, X defaultValue){
        if (!defaultValue.getClass().isInstance(object))
            return defaultValue;

        return (X)object;
    }

    protected <X> X validOrNull(Object object, Class<X> xClass){
        if (xClass.isInstance(object))
            return (X)object;

        return null;
    }

    public Object object(String key){
        return parameters.getOrDefault(key, null);
    }

    public GUIObjectAsset<T> object(String key, Object value){
        parameters.put(key, value);

        return this;
    }

    public Color color(String key){
        return validOrDefault(object(key), Color.BLACK);
    }

    public GUIObjectAsset<T> color(String key, Color value){
        return object(key, value);
    }

    public AbstractFont font(String key){
        AbstractFont result = validOrNull(object(key), AbstractFont.class);

        if (result == null)
            result = validOrNull(object("default"), AbstractFont.class);

        return result;
    }

    public GUIObjectAsset<T> font(String key, AbstractFont value){
        return object(key, value);
    }
}
