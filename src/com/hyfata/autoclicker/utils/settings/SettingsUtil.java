package com.hyfata.autoclicker.utils.settings;

import com.hyfata.autoclicker.GlobalKeyListener;
import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.ui.settings.autoclick.ACDelay;
import com.hyfata.autoclicker.ui.settings.autoclick.AutoClickSettingsUI;
import com.hyfata.json.JsonReader;
import com.hyfata.json.JsonUtil;
import com.hyfata.json.JsonWriter;
import com.hyfata.json.exceptions.JsonEmptyException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SettingsUtil {
    private static ArrayList<String> presetNames;
    private static JSONObject settings;
    private static final String FILE_PATH = "Hyfata.AutoClicker.Settings.json";

    public static void init() throws IOException {
        File f = new File(FILE_PATH);
        if (f.exists()) {
            try {
                settings = JsonReader.readFromFile(FILE_PATH);
            } catch (JsonEmptyException e) {
                settings = new JSONObject();
            } finally {
                read();
            }
        } else {
            settings = new JSONObject();
            if (f.createNewFile()) {
                read();
            } else {
                throw new IOException("File already exists");
            }
        }
    }

    private static void read() {
        setLang(getLang());
        if (!settings.has("preset")) {
            settings.put("presets", new JSONObject());
            setCurrentPreset("default");
            loadDefault();
            savePreset("default");
            loadAllPresetNames();
        } else if (!settings.has("presets")) {
            settings.put("presets", new JSONObject());
            loadLegacy();
        } else if (settings.has("default")) {
            settings.put("presets", new JSONObject());
            setCurrentPreset("default");
            loadDefault();
            savePreset("default");
            loadAllPresetNames();
        } else {
            loadAllPresetNames();
            loadPreset(getCurrentPreset());
        }
    }

    // DO NOT CHANGE
    private static void loadLegacy() {
        presetNames = new ArrayList<>();
        presetNames.add("default");
        ArrayList<String> removeKeys = new ArrayList<>();
        for (String key : settings.keySet()) {
            if (settings.get(key) instanceof JSONObject) {
                if (key.equals("presets")) {
                    continue;
                }
                if (!key.equals("default")) {
                    presetNames.add(key);
                }
                JSONObject jsonObject = settings.getJSONObject(key);
                UserSettings.setDelay(jsonObject.optString("delay", "100"));
                UserSettings.setDelayUnit(jsonObject.optString("delayUnit", "ms"));
                UserSettings.setMouseButton(jsonObject.optString("mouseButton", "left"));
                UserSettings.setToggle(jsonObject.optBoolean("toggle", false));
                UserSettings.setKeycode(jsonObject.optInt("keycode", -1));
                UserSettings.setKeyboard(jsonObject.optBoolean("keyboard", false));
                savePreset(key);
                removeKeys.add(key);
            }
        }
        for (String key : removeKeys) {
            settings.remove(key);
        }
        loadPreset(getCurrentPreset());
    }

    // able to change
    private static void loadDefault() {
        UserSettings.setDelay("100");
        UserSettings.setDelayUnit("ms");
        UserSettings.setMouseButton("left");
        UserSettings.setToggle(false);
        UserSettings.setKeycode(-1);
        UserSettings.setKeyboard(false);
    }

    // able to change
    public static void loadPreset(String preset) {
        JSONObject jsonObject = settings.getJSONObject("presets").getJSONObject(preset);
        UserSettings.setDelay(jsonObject.optString("delay", "100"));
        UserSettings.setDelayUnit(jsonObject.optString("delayUnit", "ms"));
        UserSettings.setMouseButton(jsonObject.optString("mouseButton", "left"));
        UserSettings.setToggle(jsonObject.optBoolean("toggle", false));
        UserSettings.setKeycode(jsonObject.optInt("keycode", -1));
        UserSettings.setKeyboard(jsonObject.optBoolean("keyboard", false));
    }


    //must be loaded!
    // able to change
    public static void savePreset(String preset) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("delay", UserSettings.getDelay());
        jsonObject.put("delayUnit", UserSettings.getDelayUnit());
        jsonObject.put("mouseButton", UserSettings.getMouseButton());
        jsonObject.put("toggle", UserSettings.isToggle());
        jsonObject.put("keycode", UserSettings.getKeycode());
        jsonObject.put("keyboard", UserSettings.isKeyboard());
        settings.getJSONObject("presets").put(preset, jsonObject);
    }

    // able to change
    public static void loadCurrentSettings() {
        UserSettings.setDelay(ACDelay.getInstance().getDelay());

        if (ACDelay.getInstance().getSelectedUnit().equals(Locale.getDelayMs()))
            UserSettings.setDelayUnit("ms");
        else
            UserSettings.setDelayUnit("micros");

        if (AutoClickSettingsUI.getInstance().getSelectedMouseButton().equals(Locale.getMouseLeft())) {
            UserSettings.setMouseButton("left");
        } else if (AutoClickSettingsUI.getInstance().getSelectedMouseButton().equals(Locale.getMouseMiddle())) {
            UserSettings.setMouseButton("middle");
        } else if (AutoClickSettingsUI.getInstance().getSelectedMouseButton().equals(Locale.getMouseRight())) {
            UserSettings.setMouseButton("right");
        }

        UserSettings.setToggle(AutoClickSettingsUI.getInstance().getHoldToggle().equals(Locale.getKeyToggle()));

        if (GlobalKeyListener.keycode == null) {
            UserSettings.setKeycode(-1);
        } else {
            UserSettings.setKeycode(GlobalKeyListener.keycode);
        }

        UserSettings.setKeyboard(GlobalKeyListener.isKeyboard);
    }

    public static void saveFile() throws IOException {
        JsonWriter.writeToFile(settings, FILE_PATH);
    }

    public static String getCurrentPreset() {
        return settings.getString("preset");
    }

    public static void setCurrentPreset(String preset) {
        settings.put("preset", preset);
    }

    public static String getLang() {
        return settings.optString("lang", "en.json");
    }

    public static void setLang(String lang) {
        settings.put("lang", lang);
    }

    private static void loadAllPresetNames() {
        presetNames = new ArrayList<>();
        presetNames.add("default");
        for (String key : settings.getJSONObject("presets").keySet()) {
            if (settings.getJSONObject("presets").get(key) instanceof JSONObject) {
                if (!key.equals("default"))
                    presetNames.add(key);
            }
        }
    }

    public static void addPreset(String preset) {
        loadCurrentSettings();
        savePreset(preset);
        presetNames.add(preset);
    }

    public static void renamePreset(String preset, String name) {
        JSONObject renamed = JsonUtil.renameKey(settings.getJSONObject("presets"), preset, name);

        settings.getJSONObject("presets").clear();
        settings.put("presets", renamed);
        loadAllPresetNames();
    }

    public static void removePreset(String preset) {
        presetNames.remove(preset);
        settings.getJSONObject("presets").remove(preset);
    }

    public static ArrayList<String> getPresetNames() {
        return presetNames;
    }
}
