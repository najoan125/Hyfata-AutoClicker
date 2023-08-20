package com.hyfata.autoclicker.utils.settings;

import com.hyfata.autoclicker.GlobalKeyListener;
import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.ui.settings.AutoClickSettingsUI;
import com.hyfata.json.JsonReader;
import com.hyfata.json.JsonWriter;
import com.hyfata.json.exceptions.JsonEmptyException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class SettingsUtil {
    private static ArrayList<String> presets;
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
        }
        else {
            settings = new JSONObject();
            if (f.createNewFile()) {
                read();
            }
            else {
                throw new IOException("File already exists");
            }
        }
    }

    private static void read() {
        setLang(getLang());
        if (!settings.has("preset")) {
            setCurrentPreset("default");
            loadDefault();
            savePreset("default");
            loadAllPresets();
        }
        else {
            loadAllPresets();
            loadPreset(getCurrentPreset());
        }
    }
    private static void loadDefault() {
        UserSettings.setDelay("100");
        UserSettings.setDelayUnit("ms");
        UserSettings.setMouseButton("left");
        UserSettings.setToggle(false);
        UserSettings.setKeycode(-1);
        UserSettings.setKeyboard(false);
    }

    public static void loadPreset(String preset) {
        JSONObject jsonObject = settings.getJSONObject(preset);
        UserSettings.setDelay(jsonObject.optString("delay","100"));
        UserSettings.setDelayUnit(jsonObject.optString("delayUnit","ms"));
        UserSettings.setMouseButton(jsonObject.optString("mouseButton","left"));
        UserSettings.setToggle(jsonObject.optBoolean("toggle",false));
        UserSettings.setKeycode(jsonObject.optInt("keycode",-1));
        UserSettings.setKeyboard(jsonObject.optBoolean("keyboard",false));
    }


    //must be loaded!
    public static void savePreset(String preset) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("delay", UserSettings.getDelay());
        jsonObject.put("delayUnit", UserSettings.getDelayUnit());
        jsonObject.put("mouseButton", UserSettings.getMouseButton());
        jsonObject.put("toggle", UserSettings.isToggle());
        jsonObject.put("keycode", UserSettings.getKeycode());
        jsonObject.put("keyboard",UserSettings.isKeyboard());
        settings.put(preset, jsonObject);
    }

    public static void loadCurrentSettings() {
        UserSettings.setDelay(AutoClickSettingsUI.delay.getValue().toString());
        if (Objects.equals(AutoClickSettingsUI.delayUnits.getSelectedItem(), Locale.getDelayMs())){
            UserSettings.setDelayUnit("ms");
        }
        else {
            UserSettings.setDelayUnit("micros");
        }

        if (Objects.equals(AutoClickSettingsUI.mouseButtons.getSelectedItem(), Locale.getMouseLeft())) {
            UserSettings.setMouseButton("left");
        }
        else if (Objects.equals(AutoClickSettingsUI.mouseButtons.getSelectedItem(), Locale.getMouseMiddle())) {
            UserSettings.setMouseButton("middle");
        }
        else if (Objects.equals(AutoClickSettingsUI.mouseButtons.getSelectedItem(), Locale.getMouseRight())) {
            UserSettings.setMouseButton("right");
        }

        UserSettings.setToggle(Objects.equals(AutoClickSettingsUI.holdToggles.getSelectedItem(), Locale.getKeyToggle()));

        if (GlobalKeyListener.keycode == null) {
            UserSettings.setKeycode(-1);
        }
        else {
            UserSettings.setKeycode(GlobalKeyListener.keycode);
        }

        UserSettings.setKeyboard(GlobalKeyListener.isKeyboard);
    }

    public static void saveFile() throws IOException {
        JsonWriter.writeToFile(settings,FILE_PATH);
    }

    public static String getCurrentPreset() {
        return settings.getString("preset");
    }
    public static void setCurrentPreset(String preset) {
        settings.put("preset",preset);
    }
    public static String getLang() {
        return settings.optString("lang","en.json");
    }
    public static void setLang(String lang) {
        settings.put("lang",lang);
    }
    private static void loadAllPresets() {
        presets = new ArrayList<>();
        presets.add("default");
        for (String key : settings.keySet()) {
            if (settings.get(key) instanceof JSONObject) {
                if (!key.equals("default"))
                    presets.add(key);
            }
        }
    }

    public static void addPreset(String preset) {
        loadCurrentSettings();
        savePreset(preset);
        presets.add(preset);
    }

    public static void renamePreset(String preset, String name) {
        loadPreset(preset);
        removePreset(preset);
        savePreset(name);
        presets.add(name);
    }

    public static void removePreset(String preset) {
        presets.remove(preset);
        settings.remove(preset);
    }

    public static ArrayList<String> getPresets() {
        return presets;
    }
}
