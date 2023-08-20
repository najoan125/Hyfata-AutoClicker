package com.hyfata.autoclicker;

import com.formdev.flatlaf.IntelliJTheme;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.ui.Design;
import com.hyfata.autoclicker.utils.OSValidator;
import com.hyfata.autoclicker.utils.settings.SettingsUtil;
import com.hyfata.json.JsonReader;
import com.hyfata.json.exceptions.JsonEmptyException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class AutoClicker extends JPanel {
    public static final String APP_VERSION = "2.0.0";
    public static final String DISCORD_TAG = "Najoan#1111";
    static Design design;

    public static void main(String[] args){
        IntelliJTheme.setup(AutoClicker.class.getResourceAsStream("theme/arc_theme_dark.theme.json"));

        try {
            SettingsUtil.init();
        } catch (IOException e) {
            showErrorDialog("Error loading Settings file. Contact to developer on discord!\nDiscord Tag: "+DISCORD_TAG+"\n\n"+e.getMessage(), "Error loading settings file");
            System.exit(-1);
        }

        try {
            Locale.setLocale(SettingsUtil.getLang());
        } catch (IOException | JsonEmptyException e) {
            showErrorDialog("Error loading language file. Contact to developer on discord!\nDiscord Tag: "+DISCORD_TAG+"\n\n"+e.getMessage(), "Error loading language file");
            System.exit(-1);
        }

        try {
            update(); //update
        } catch (JsonEmptyException | URISyntaxException | IOException ignored) {} finally {
            registerNativeHook();
            startUI();
        }

    } // main

    public static void reload() {
        design.dispose();
        startUI();
    }

    public static void showErrorDialog(String content, String title) {
        JOptionPane.showMessageDialog(null, content, title, JOptionPane.ERROR_MESSAGE);
    }

    private static void startUI() {
        design = new Design("Hyfata AutoClicker v" + APP_VERSION);
    }

    private static void registerNativeHook() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            JOptionPane.showMessageDialog(null, "NativeHook를 등록하는 과정에서 오류가 발생했습니다!\n제작자에게 디스코드로 문의해주세요!\nDiscord Tag: Najoan#0135",
                    "오류 발생", JOptionPane.INFORMATION_MESSAGE);
            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(new GlobalKeyListener());
        GlobalScreen.addNativeMouseListener(new GlobalKeyListener());
    }

    private static void update() throws URISyntaxException, IOException, JsonEmptyException {
        JSONObject jObject = JsonReader.readFromURL("http://132.226.170.151/file/Autoclicker/autoclicker.json");
        String latest = jObject.getString("latest-version");
        String desc = jObject.getString("description");

        if (!latest.equals(APP_VERSION)) {
            if (OSValidator.isWindows()) {
                int answer = JOptionPane.showConfirmDialog(null, Locale.getUpdateDesc()+"\n\n" + Locale.getCurrentVersion() + APP_VERSION + ", " + Locale.getLatestVersion() + latest
                                + "\n" + Locale.getChangeLog() + desc,
                        Locale.getUpdateFound(),
                        JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION) {
                    downloadUpdate(latest);
                    System.exit(1);
                }
            } else {
                int answer = JOptionPane.showConfirmDialog(null, Locale.getUpdateDesc()+"\n\n" + Locale.getCurrentVersion() + APP_VERSION + ", " + Locale.getLatestVersion() + latest
                                + "\n" + Locale.getChangeLog() + desc,
                        Locale.getUpdateFound(),
                        JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION) {
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        Desktop.getDesktop().browse(new URI("https://github.com/najoan125/Hyfata-AutoClicker/releases/download/" + latest + "/AutoClicker.jar"));
                    } else
                        JOptionPane.showMessageDialog(null, "아쉽게도 이 장치는 지원되지 않습니다. 업데이트를 직접 설치해주세요.",
                                "오류 발생", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(1);
                }
            }
        }
    }

    private static void downloadUpdate(String version) {
        String home = System.getProperty("user.home");
        String file = home + "/Downloads/" + "Hyfata.AutoClick." + version + ".exe";

        String addr = "https://github.com/najoan125/Hyfata-AutoClicker/releases/download/" + version + "/Hyfata.AutoClick.exe";
        try {
            URL url = new URL(addr);
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(file);

            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();

            Runtime.getRuntime().exec(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

} // Main
