package com.hyfata.autoclicker;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.ui.settings.autoclick.AutoClickSettingsUI;

public class GlobalKeyListener implements NativeKeyListener, NativeMouseListener {
    private boolean isPressed = false;

    public static boolean isKeyboard = true;
    public static boolean isChanging = false;
    public static boolean shouldBlocked = false;
    public static boolean blockReleaseOnce = false;
    public static Integer keycode = null;

    //keyboard
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        int key = e.getKeyCode();
        onPressed(key, true);
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        int key = e.getKeyCode();
        onReleased(key, true);

        if (isChanging) {
            if (key == NativeKeyEvent.VC_ESCAPE) {
                cancelChangeKeyCode();
            } else {
                changeKeyCode(key, true);
            }
        }
    }

    //mouse
    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        int key = e.getButton();
        onPressed(key, false);
    }


    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        int key = e.getButton();
        onReleased(key, false);

        if (isChanging) {
            if (key == 1) {
                cancelChangeKeyCode();
            } else {
                changeKeyCode(key, false);
            }
        }
    }

    private void onPressed(int key, boolean keyboard) {
        if (!isPressed && !isChanging && isKeyboard == keyboard && keycode != null && key == keycode && !shouldBlocked) {
            isPressed = true;
            blockReleaseOnce = false;

            String holdToggle = AutoClickSettingsUI.getInstance().getHoldToggleUI().getSelected();
            if (holdToggle.equals(Locale.getKeyHold())) { // hold only
                AutoClickHandler.toggleAutoClick();
            }
        }
    }

    private void onReleased(int key, boolean keyboard) {
        if (keycode != null && key == keycode && !isChanging && isKeyboard == keyboard && !shouldBlocked) {
            isPressed = false;
            if (blockReleaseOnce) {
                blockReleaseOnce = false;
                return;
            }
            AutoClickHandler.toggleAutoClick();
        }
    }

    private void changeKeyCode(int keycode, boolean keyboard) {
        GlobalKeyListener.isChanging = false;
        GlobalKeyListener.keycode = keycode;
        GlobalKeyListener.isKeyboard = keyboard;
        AutoClickSettingsUI.getInstance().getHotKeyUI().setKeyText(keycode, keyboard);
        AutoClickSettingsUI.getInstance().getHotKeyUI().onKeyChanged();
    }

    private void cancelChangeKeyCode() {
        GlobalKeyListener.isChanging = false;
        AutoClickSettingsUI.getInstance().getHotKeyUI().onKeyChanged();
    }
}
