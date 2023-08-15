package com.hyfata.autoclicker.locale;

import com.hyfata.json.JsonReader;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Locale {
    private static String updateFound;
    private static String updateDesc;
    private static String currentVersion;
    private static String latestVersion;
    private static String changeLog;
    private static String autoClickSetting;
    private static String presetSetting;
    private static String help;
    private static String about;
    private static String autoClickDelay;
    private static String delayMs;
    private static String delayMicros;
    private static String mouseLeft;
    private static String mouseMiddle;
    private static String mouseRight;
    private static String keyHold;
    private static String keyToggle;
    private static String key;
    private static String changeKey;

    public static void setLocale(String loc) throws IOException {
        URL url = Locale.class.getResource(loc);
        JSONObject locale;
        if (url != null) {
            try {
                File file = new File(url.toURI());
                locale = JsonReader.readFromFile(file.getPath());
                // 이후 처리
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        } else {
            return;
        }
        updateFound = locale.getString("UpdateFound");
        updateDesc = locale.getString("UpdateDesc");
        currentVersion = locale.getString("CurrentVersion");
        latestVersion = locale.getString("LatestVersion");
        changeLog = locale.getString("ChangeLog");
        autoClickSetting = locale.getString("AutoClickSetting");
        presetSetting = locale.getString("PresetSetting");
        help = locale.getString("Help");
        about = locale.getString("About");
        autoClickDelay = locale.getString("AutoClickDelay");
        delayMs = locale.getString("DelayMs");
        delayMicros = locale.getString("DelayMicros");
        mouseLeft = locale.getString("MouseLeft");
        mouseMiddle = locale.getString("MouseMiddle");
        mouseRight = locale.getString("MouseRight");
        keyHold = locale.getString("KeyHold");
        keyToggle = locale.getString("KeyToggle");
        key = locale.getString("Key");
        changeKey = locale.getString("ChangeKey");
    }

    public static String getUpdateFound() {
        return updateFound;
    }

    public static String getUpdateDesc() {
        return updateDesc;
    }

    public static String getCurrentVersion() {
        return currentVersion;
    }

    public static String getLatestVersion() {
        return latestVersion;
    }

    public static String getChangeLog() {
        return changeLog;
    }

    public static String getAutoClickSetting() {
        return autoClickSetting;
    }

    public static String getPresetSetting() {
        return presetSetting;
    }

    public static String getHelp() {
        return help;
    }

    public static String getAbout() {
        return about;
    }

    public static String getAutoClickDelay() {
        return autoClickDelay;
    }

    public static String getDelayMs() {
        return delayMs;
    }

    public static String getDelayMicros() {
        return delayMicros;
    }

    public static String getMouseLeft() {
        return mouseLeft;
    }

    public static String getMouseMiddle() {
        return mouseMiddle;
    }

    public static String getMouseRight() {
        return mouseRight;
    }

    public static String getKeyHold() {
        return keyHold;
    }

    public static String getKeyToggle() {
        return keyToggle;
    }

    public static String getKey() {
        return key;
    }

    public static String getChangeKey() {
        return changeKey;
    }
}
