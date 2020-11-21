package ru.konstanteam.lokutils.gui.animation;

import ru.konstanteam.lokutils.gui.GUIObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Animations {

    protected HashMap<String, Animation> animationHashMap = new HashMap<>();
    protected ArrayList<String> animationsTasks = new ArrayList<>();
    protected GUIObject source;
    protected long lastUpdate = System.nanoTime();

    public Animations(GUIObject source) {
        this.source = source;
    }

    public Animation getAnimation(String name) {
        return animationHashMap.getOrDefault(name, null);
    }

    public Animations addAnimation(Animation animation) {
        animationHashMap.put(animation.getName(), animation);
        animation.init(source);

        return this;
    }

    public Animations stopAnimation(String name) {
        if (!animationHashMap.containsKey(name)) return this;

        animationHashMap.get(name).stop();
        return this;
    }

    public Animations stopAll() {
        for (Animation animation : animationHashMap.values())
            animation.stop();

        return this;
    }

    public boolean animationIsRun(String name) {
        if (!animationHashMap.containsKey(name)) return false;

        return animationHashMap.get(name).isRun;
    }

    public boolean somethingIsRun() {
        for (Animation animation : animationHashMap.values())
            if (animation.isRun) return true;

        return false;
    }

    public Animations addAnimationToTaskList(String name) {
        animationsTasks.add(name);

        return this;
    }

    public Animations startAnimation(String name) {
        if (!animationHashMap.containsKey(name)) return this;

        animationHashMap.get(name).start();
        return this;
    }

    public void update(int refreshRate) {
        long nanoTime = System.nanoTime();
        for (Animation animation : animationHashMap.values()) {
            if (animation.isRun())
                animation.update((nanoTime - lastUpdate) / 1000000000d / (1d / refreshRate));
        }

        lastUpdate = nanoTime;

        if (animationsTasks.size() > 0 && !somethingIsRun()) {
            startAnimation(animationsTasks.get(0));
            animationsTasks.remove(0);
        }
    }
}
