package su.knst.lokutils.gui.objects.button;

import su.knst.lokutils.gui.animation.Animation;
import su.knst.lokutils.gui.eventsystem.EventTools;
import su.knst.lokutils.gui.eventsystem.events.ClickType;
import su.knst.lokutils.gui.eventsystem.events.MouseClickedEvent;
import su.knst.lokutils.gui.layout.FreeLayout;
import su.knst.lokutils.gui.layout.GUIAbstractLayout;
import su.knst.lokutils.gui.panels.GUIPanel;
import su.knst.lokutils.gui.objects.GUIText;
import su.knst.lokutils.gui.objects.margin.GUIMargin;
import su.knst.lokutils.gui.objects.margin.Margin;
import su.knst.lokutils.objects.Color;
import su.knst.lokutils.objects.Point;
import su.knst.lokutils.objects.Rect;
import su.knst.lokutils.gui.layout.Alignment;

public class GUIButton extends GUIPanel<FreeLayout> {
    protected GUIText text;
    protected GUIMargin margin;
    protected ButtonAction action;

    public GUIButton(GUIText text) {
        super(new FreeLayout());

        this.text = text;
        this.margin = new GUIMargin(text, new Margin(10, 10, 7,7));
        this.getRootLayout().addObject(margin, Alignment.CENTER);

        customersContainer.setCustomer(MouseClickedEvent.class, event -> {
            if (event.clickType == ClickType.CLICKED && new Rect(Point.ZERO, size().get()).inside(event.position)) {
                getAnimations().stopAll();
                getAnimations().startAnimation("pressed");
            } else {
                getAnimations().addAnimationToTaskList("unpressed");

                if (EventTools.realized(event, customersContainer.getLastEvent(MouseClickedEvent.class), new Rect(Point.ZERO, size().get())) && action != null)
                    action.clicked();
            }
        });

        this.getAnimations().addAnimation(new Animation("pressed") {
            @Override
            public void update(double speed) {
                Color source = overrideColor;
                Color end = asset.color("pressed");

                overrideColor = softColorChange(source, end, (float) speed * 2f);

                isRun = !softColorChangeDone(source, end);
            }
        });

        this.getAnimations().addAnimation(new Animation("unpressed") {
            @Override
            public void update(double speed) {
                Color source = overrideColor;
                Color end = asset.color("background");

                overrideColor = softColorChange(source, end, (float) speed * 2);

                isRun = !softColorChangeDone(source, end);
            }
        });

        minimumSize().track(margin.minimumSize());
        size().track(minimumSize());
    }

    public GUIButton(String text){
        this(new GUIText());
        this.text.string().set(text);
    }

    public GUIButton(){
        this("Button");
    }

    public GUIButton setAction(ButtonAction action) {
        this.action = action;

        return this;
    }

    public GUIText getText() {
        return text;
    }

    @Override
    public void init(GUIAbstractLayout owner) {
        super.init(owner);

        overrideColor = asset.color("background");
        text.overrideColor = asset.color("text");
    }

}
