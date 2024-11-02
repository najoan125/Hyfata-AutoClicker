package com.hyfata.autoclicker;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.ui.settings.autoclick.ACDelay;
import com.hyfata.autoclicker.ui.settings.autoclick.ACKey;
import com.hyfata.autoclicker.ui.settings.autoclick.AutoClickSettingsUI;
import com.hyfata.autoclicker.ui.settings.LanguageUI;
import com.hyfata.autoclicker.ui.settings.preset.PresetUI;

import java.awt.*;

public class GlobalKeyListener implements NativeKeyListener, NativeMouseListener {
    private boolean isPressed = false;

    public static boolean isKeyboard = true;
    public static boolean isChanging = false;
    public static boolean shouldBlocked = false;
    public static Integer keycode = null;

    //keyboard
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        int key = e.getKeyCode();
        if (!isPressed && !isChanging && isKeyboard && keycode != null && key == keycode && !shouldBlocked) {
            String holdToggle = AutoClickSettingsUI.getInstance().getHoldToggle();
            if (holdToggle.equals(Locale.getKeyHold())) {
                toggleAutoClick();
                isPressed = true;
            }
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        int key = e.getKeyCode();
        if (keycode != null && key == keycode && !isChanging && isKeyboard && !shouldBlocked) {
            toggleAutoClick();
            isPressed = false;
        }

        if (isChanging) {
            if (key == NativeKeyEvent.VC_ESCAPE) {
                ACKey.getInstance().cancelChangeKeyCode();
            } else {
                String keyChar = NativeKeyEvent.getKeyText(key);
                if (keyChar.startsWith(Toolkit.getProperty("AWT.unknown", "Unknown"))) {
                    ACKey.getInstance().changeKeyCode(key, "", Locale.getKeyCode(), true);
                } else {
                    ACKey.getInstance().changeKeyCode(key, keyChar, Locale.getKeyCode(), true);
                }
            }
        }
    }
    //keyboard

    //mouse
    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        int key = e.getButton();
        if (!isPressed && !isChanging && !isKeyboard && keycode != null && key == keycode && !shouldBlocked) {
            String holdToggle = AutoClickSettingsUI.getInstance().getHoldToggle();
            if (holdToggle.equals(Locale.getKeyHold())) {
                toggleAutoClick();
                isPressed = true;
            }
        }
    }


    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        int key = e.getButton();
        if (keycode != null && key == keycode && !isChanging && !isKeyboard && !shouldBlocked) {
            toggleAutoClick();
            isPressed = false;
        }

        if (isChanging) {
            if (key == 1) {
                ACKey.getInstance().cancelChangeKeyCode();
            } else {
                ACKey.getInstance().changeKeyCode(key, "", Locale.getMouseButtonCode(), false);
            }
        }
    }

    //mouse
    private void toggleAutoClick() {
        if (AutoClickHandler.isStart) {
            AutoClickHandler.isStart = false;
            AutoClickSettingsUI.getInstance().setAllEnabled(true);
            LanguageUI.getInstance().setAllEnabled(true);
            PresetUI.getInstance().setAllEnabled(true);
            AutoClickHandler.stop();
        } else if (!ACDelay.getInstance().getDelay().equals("0")) {
            AutoClickHandler.start();
        }
    }
}
