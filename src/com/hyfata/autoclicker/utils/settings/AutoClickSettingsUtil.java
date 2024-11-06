package com.hyfata.autoclicker.utils.settings;

import com.hyfata.autoclicker.locale.Locale;

public class AutoClickSettingsUtil {
    public static String getDelayUnit() {
        if (UserSettings.getDelayUnit().equals("ms"))
            return Locale.getDelayMs();
        return Locale.getDelayMicros();
    }

    public static String getMouseButton() {
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

    public static String getHoldToggle() {
        if (UserSettings.isToggle())
            return Locale.getKeyToggle();
        return Locale.getKeyHold();
    }

    public static Integer getKeycode() {
        if (UserSettings.getKeycode() == -1)
            return null;
        return UserSettings.getKeycode();
    }
}
