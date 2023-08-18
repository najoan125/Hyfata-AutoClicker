package com.hyfata.autoclicker;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.ui.settings.AutoClickSettingsUI;

import java.awt.*;
import java.util.Objects;

public class GlobalKeyListener implements NativeKeyListener, NativeMouseListener {
    static boolean isPressed = false;
    static boolean isKeyboard = true;
    public static boolean isChanging = false;
    static Integer keycode = null;

    //keyboard
    @Override
    public void nativeKeyPressed(NativeKeyEvent e){
        int key = e.getKeyCode();
        if (!isPressed && !isChanging && isKeyboard && keycode != null && key == keycode) {
            String holdToggle = Objects.requireNonNull(AutoClickSettingsUI.holdToggles.getSelectedItem()).toString();
            if (holdToggle.equals(Locale.getKeyHold())){
                startOrStop();
                isPressed = true;
            }
        }
    }
    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        int key = e.getKeyCode();
        if (keycode != null && key == keycode && !isChanging && isKeyboard) {
            startOrStop();
            isPressed = false;
        }

        if (isChanging) {
            if (key == NativeKeyEvent.VC_ESCAPE) {
                cancelChangeKeyCode();
            }
            else {
                String keyChar = NativeKeyEvent.getKeyText(key);
                if (keyChar.startsWith(Toolkit.getProperty("AWT.unknown", "Unknown"))){
                    changeKeyCode(key, "", Locale.getKeyCode(), true);
                }
                else {
                    changeKeyCode(key, keyChar, Locale.getKeyCode(), true);
                }
            }
        }
    }
    //keyboard

    //mouse
    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        int key = e.getButton();
        if (!isPressed && !isChanging && !isKeyboard && keycode != null && key == keycode) {
            String holdToggle = Objects.requireNonNull(AutoClickSettingsUI.holdToggles.getSelectedItem()).toString();
            if (holdToggle.equals(Locale.getKeyHold())){
                startOrStop();
                isPressed = true;
            }
        }
    }


    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        int key = e.getButton();
        if (keycode != null && key == keycode && !isChanging && !isKeyboard) {
            startOrStop();
            isPressed = false;
        }

        if (isChanging) {
            if (key == 1) {
                cancelChangeKeyCode();
            }
            else {
                changeKeyCode(key, "",Locale.getMouseButtonCode(), false);
            }
        }
    }
    //mouse
    private static void startOrStop() {
        if (AutoClickHandler.isStart) {
            AutoClickHandler.isStart = false;
            AutoClickSettingsUI.setAllEnabled(true);
            AutoClickHandler.executorService.shutdown();
        } else {
            AutoClickHandler.start();
        }
    }

    private static void changeKeyCode(int keycode, String key, String label, boolean keyboard) {
        isChanging = false;
        GlobalKeyListener.keycode = keycode;
        GlobalKeyListener.isKeyboard = keyboard;
        AutoClickSettingsUI.key.setText(key + " (" + label+": " + keycode + ")");
        AutoClickSettingsUI.changeKeyButton.setEnabled(true);
        AutoClickSettingsUI.changingKeyDialog.setVisible(false);
    }

    private static void cancelChangeKeyCode(){
        isChanging = false;
        AutoClickSettingsUI.changeKeyButton.setEnabled(true);
        AutoClickSettingsUI.changingKeyDialog.setVisible(false);
    }
}
