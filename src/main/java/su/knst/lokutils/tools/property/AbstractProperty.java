package su.knst.lokutils.tools.property;

import java.util.function.Supplier;

public abstract class AbstractProperty<T> extends AbstractValue<T> implements Property<T> {
    protected boolean valid;

    protected final InvalidationListener invalidationListener = (value) -> invalidate();

    protected Supplier<T> trackingFunction;
    protected Value<T>[] trackingFunctionSources;

    protected Value<T> trackingValue;

    @Override
    public void set(T value) {
        T oldValue = this.value;
        this.value = value;
        this.valid = true;

        this.trackingValue = null;
        this.trackingFunction = null;

        fireInvalidationListeners();
        fireChangeListeners(oldValue, value);
    }

    @Override
    public T get() {
        T trackedValue = getTrackedValue();

        if (!trackedValue.equals(value)) {
            T oldValue = this.value;
            this.value = trackedValue;

            fireInvalidationListeners();
            fireChangeListeners(oldValue, trackedValue);
        }

        this.valid = true;


        return value;
    }

    @Override
    public void track(Supplier<T> function) {
        if (trackingFunctionSources != null){
            for (Value<T> value : trackingFunctionSources){
                value.removeInvalidationListener(invalidationListener);
            }

            trackingFunctionSources = null;
        }

        if (trackingValue != null)
            trackingValue.removeInvalidationListener(invalidationListener);

        this.trackingFunction = function;
        this.trackingValue = null;

        invalidate();
    }

    @Override
    public void track(Supplier<T> function, Value... sources) {
        if (trackingValue != null)
            trackingValue.removeInvalidationListener(invalidationListener);

        if (trackingFunctionSources != null){
            for (Value<T> value : trackingFunctionSources){
                value.removeInvalidationListener(invalidationListener);
            }
        }

        trackingFunctionSources = sources;

        for (Value value : sources){
            value.addInvalidationListener(invalidationListener);
        }

        this.trackingFunction = function;
        this.trackingValue = null;

        invalidate();
    }

    @Override
    public void track(Value<T> value) {
        if (trackingValue != null)
            trackingValue.removeInvalidationListener(invalidationListener);

        if (trackingFunctionSources != null){
            for (Value<T> sValue : trackingFunctionSources){
                sValue.removeInvalidationListener(invalidationListener);
            }

            trackingFunctionSources = null;
        }

        this.trackingValue = value;
        this.trackingFunction = null;

        trackingValue.addInvalidationListener(invalidationListener);

        invalidate();
    }

    protected T getTrackedValue(){
        return trackingFunction != null ? trackingFunction.get() : (trackingValue != null ? trackingValue.get() : value);
    }

    @Override
    public void invalidate() {
        if (!valid)
            return;

        valid = false;

        fireInvalidationListeners();
    }
}
