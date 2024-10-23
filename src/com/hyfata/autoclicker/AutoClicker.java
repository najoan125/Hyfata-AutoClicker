package com.hyfata.autoclicker;

import com.formdev.flatlaf.IntelliJTheme;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.ui.Design;
import com.hyfata.autoclicker.utils.DialogUtil;
import com.hyfata.autoclicker.utils.settings.SettingsUtil;
import com.hyfata.json.exceptions.JsonEmptyException;

import javax.swing.*;
import java.io.IOException;

public class AutoClicker extends JPanel {
    public static final String APP_VERSION = "2.0.5";
    public static final String DISCORD_TAG = "najoan";
    public static final String CHECK_URL = "http://132.226.170.151/file/Autoclicker/autoclicker.json";
    public static final String UPDATE_JAR_URL = "https://github.com/najoan125/Hyfata-AutoClicker/releases/download/%s/AutoClicker.jar";
    public static final String UPDATE_EXE_URL = "https://github.com/najoan125/Hyfata-AutoClicker/releases/download/%s/Hyfata.AutoClick.exe";
    static Design design;

    public static void main(String[] args){
        IntelliJTheme.setup(AutoClicker.class.getResourceAsStream("theme/arc_theme_dark.theme.json"));

        try {
            SettingsUtil.init();
        } catch (IOException e) {
            DialogUtil.showErrorDialog(e, "Error loading Settings file. Contact to developer on discord!", "Error loading settings file");
            System.exit(-1);
        }

        try {
            Locale.setLocale(SettingsUtil.getLang());
        } catch (IOException | JsonEmptyException e) {
            DialogUtil.showErrorDialog(e,"Error loading language file. Contact to developer on discord!", "Error loading language file");
            System.exit(-1);
        }

        registerNativeHook();
        startUI();
    } // main

    public static void reload() {
        design.dispose();
        startUI();
    }

    private static void startUI() {
        design = new Design("Hyfata AutoClicker v" + APP_VERSION);
    }

    private static void registerNativeHook() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            DialogUtil.showErrorDialog(ex, Locale.getNativeHookError(), "Error registering NativeHook");
            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(new GlobalKeyListener());
        GlobalScreen.addNativeMouseListener(new GlobalKeyListener());
    }

} // Main
