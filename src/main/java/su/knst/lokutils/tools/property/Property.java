package su.knst.lokutils.tools.property;

import java.util.function.Supplier;

public interface Property<T> extends Value<T>{
    void set(T value);

    void track(Supplier<T> function);

    void track(Supplier<T> function, Value... sources);

    void track(Value<T> property);

    void invalidate();
}
