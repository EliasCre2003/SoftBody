package eliascregard.interactives;

import eliascregard.input.MouseButtonHandler;
import eliascregard.input.MouseHandler;
import eliascregard.input.MouseMovementHandler;

import java.awt.*;

public class RadioButtons {

    public Switch[] switches;
    public Switch activeSwitch;

    public RadioButtons(Switch[] switches) {
        this.switches = switches;
        this.activeSwitch = null;
    }

    public void update(MouseHandler mouse) {
        if (!mouse.leftIsPressed()) {
            return;
        }
        for (Switch s : this.switches) {
            boolean previousState = s.isOn;
            s.update(mouse);
            if (s.isOn != previousState) {
                if (s.isOn) {
                    this.activeSwitch.isOn = false;
                    this.activeSwitch = s;
                } else {
                    if (this.activeSwitch == s) {
                        this.activeSwitch = null;
                    }
                }
                return;
            }
        }
    }

    public void render(Graphics2D g2, double scale) {
        for (Switch s : this.switches) {
            s.render(g2, scale);
        }
    }

}
