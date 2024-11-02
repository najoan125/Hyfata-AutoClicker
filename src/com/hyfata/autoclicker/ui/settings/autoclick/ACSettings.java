package com.hyfata.autoclicker.ui.settings.autoclick;

import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.utils.settings.UserSettings;

public class ACSettings {
    protected String getDelayUnit() {
        if (UserSettings.getDelayUnit().equals("ms"))
            return Locale.getDelayMs();
        return Locale.getDelayMicros();
    }

    protected String getMouseButton() {
        switch (UserSettings.getMouseButton()) {
            case "left":
                return Locale.getMouseLeft();
            case "middle":
                return Locale.getMouseMiddle();
            case "default":
                return Locale.getMouseRight();
        }
        return "";
    }

    protected String getHoldToggle() {
        if (UserSettings.isToggle())
            return Locale.getKeyToggle();
        return Locale.getKeyHold();
    }

    protected Integer getKeycode() {
        if (UserSettings.getKeycode() == -1)
            return null;
        return UserSettings.getKeycode();
    }
}
