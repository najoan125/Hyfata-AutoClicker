package com.hyfata.autoclicker.utils.settings;

public class UserSettings {
    private static String delay;
    private static String delayUnit;
    private static String mouseButton;
    private static boolean toggle;
    private static int keycode;
    private static boolean keyboard;

    public static boolean isKeyboard() {
        return keyboard;
    }

    public static void setKeyboard(boolean keyboard) {
        UserSettings.keyboard = keyboard;
    }

    public static String getDelayUnit() {
        return delayUnit;
    }

    public static void setDelayUnit(String delayUnit) {
        UserSettings.delayUnit = delayUnit;
    }

    public static String getDelay() {
        return delay;
    }

    public static void setDelay(String delay) {
        UserSettings.delay = delay;
    }

    public static String getMouseButton() {
        return mouseButton;
    }

    public static void setMouseButton(String mouseButton) {
        UserSettings.mouseButton = mouseButton;
    }

    public static boolean isToggle() {
        return toggle;
    }

    public static void setToggle(boolean toggle) {
        UserSettings.toggle = toggle;
    }

    public static int getKeycode() {
        return keycode;
    }

    public static void setKeycode(int keycode) {
        UserSettings.keycode = keycode;
    }
}
