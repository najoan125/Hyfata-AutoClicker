package com.hyfata.autoclicker.locale;

import com.hyfata.json.JsonReader;
import com.hyfata.json.exceptions.JsonEmptyException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

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
    private static String mouseButton;
    private static String notSet;
    private static String keyListening;
    private static String keyListen;
    private static String language;
    private static String keyCode;
    private static String mouseButtonCode;
    private static String reset;
    private static String currentPreset;
    private static String cantRemoveDefault;
    private static String rename;
    private static String inputPreset;
    private static String cantRenameDefault;
    private static String helpDesc;
    private static String NativeHookError;
    private static String UpdateNotSupported;
    private static String DownloadingUpdateError;
    private static String RunningUpdateFileError;
    private static String SavingSettingsError;

    public static void setLocale(String loc) throws IOException, JsonEmptyException {
        JSONObject locale = JsonReader.readFromInputStream(Objects.requireNonNull(Locale.class.getResourceAsStream(loc)));
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
        mouseButton = locale.getString("mouseButton");
        notSet = locale.getString("NotSet");
        keyListening = locale.getString("KeyListening");
        keyListen = locale.getString("KeyListen");
        language = locale.getString("Language");
        keyCode = locale.getString("KeyCode");
        mouseButtonCode = locale.getString("MouseButtonCode");
        reset = locale.getString("reset");
        currentPreset = locale.getString("currentPreset");
        cantRemoveDefault = locale.getString("cantRemoveDefault");
        rename = locale.getString("rename");
        inputPreset = locale.getString("inputPreset");
        cantRenameDefault = locale.getString("cantRenameDefault");
        helpDesc = locale.getString("helpDesc");
        NativeHookError = locale.getString("NativeHookError");
        UpdateNotSupported = locale.getString("UpdateNotSupported");
        DownloadingUpdateError = locale.getString("DownloadingUpdateError");
        RunningUpdateFileError = locale.getString("RunningUpdateFileError");
        SavingSettingsError = locale.getString("SavingSettingsError");
    }

    public static String getNativeHookError() {
        return NativeHookError;
    }

    public static String getUpdateNotSupported() {
        return UpdateNotSupported;
    }

    public static String getDownloadingUpdateError() {
        return DownloadingUpdateError;
    }

    public static String getRunningUpdateFileError() {
        return RunningUpdateFileError;
    }

    public static String getSavingSettingsError() {
        return SavingSettingsError;
    }

    public static String getHelpDesc() {
        return helpDesc;
    }

    public static String getCantRenameDefault() {
        return cantRenameDefault;
    }

    public static String getInputPreset() {
        return inputPreset;
    }

    public static String getRename() {
        return rename;
    }

    public static String getCurrentPreset() {
        return currentPreset;
    }

    public static String getCantRemoveDefault() {
        return cantRemoveDefault;
    }

    public static String getReset() {
        return reset;
    }
    public static String getKeyCode() {
        return keyCode;
    }

    public static String getMouseButtonCode() {
        return mouseButtonCode;
    }

    public static String getLanguage() {
        return language;
    }

    public static String getKeyListen() {
        return keyListen;
    }

    public static String getNotSet() {
        return notSet;
    }

    public static String getKeyListening() {
        return keyListening;
    }

    public static String getUpdateFound() {
        return updateFound;
    }
    public static String getMouseButton() {
        return mouseButton;
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
