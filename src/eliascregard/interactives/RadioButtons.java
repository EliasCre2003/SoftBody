package eliascregard.interactives;

import eliascregard.input.MouseButtonHandler;
import eliascregard.input.MouseMovementHandler;

public class RadioButtons {

    public Switch[] switches;
    public Switch activeSwitch;

    public RadioButtons(Switch[] switches) {
        this.switches = switches;
        this.activeSwitch = null;
    }

    public void update(MouseButtonHandler mouseButton, MouseMovementHandler mousePosition) {
        if (!mouseButton.leftIsPressed) {
            return;
        }
        for (Switch s : this.switches) {
            boolean previousState = s.isOn;
            s.update(mouseButton, mousePosition);
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

}
