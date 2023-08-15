package com.hyfata.autoclicker;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;

import java.awt.*;
import java.util.Objects;

public class GlobalKeyListener implements NativeKeyListener, NativeMouseListener {
    static boolean isPressed = false;
    static boolean keyboard = true;

    //keyboard
    @Override
    public void nativeKeyPressed(NativeKeyEvent e){
        int key = e.getKeyCode();
        String pt = Objects.requireNonNull(AutoClicker.work.CBmenu3.getSelectedItem()).toString();
        if (Objects.equals(pt, "누르기") && !isPressed && AutoClicker.work.keycode != null && key == AutoClicker.work.keycode && !AutoClicker.work.changing && keyboard) {
            startOrStop();
            isPressed = true;
        }
    }
    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        int key = e.getKeyCode();
        if (AutoClicker.work.keycode != null && key == AutoClicker.work.keycode && !AutoClicker.work.changing && keyboard) {
            startOrStop();
            isPressed = false;
        }

        if (AutoClicker.work.changing && key != NativeKeyEvent.VC_ESCAPE) {
            String keychar = NativeKeyEvent.getKeyText(key);
            if (keychar.startsWith(Toolkit.getProperty("AWT.unknown", "Unknown"))){
                changeKeyCode(key, "", "키코드", true);
            }
            else {
                changeKeyCode(key, keychar, "키코드", true);
            }
        }

        if (AutoClicker.work.changing && key == NativeKeyEvent.VC_ESCAPE) {
            cancelChangeKeyCode();
        }
    }
    //keyboard

    //mouse
    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        int key = e.getButton();
        String pt = Objects.requireNonNull(AutoClicker.work.CBmenu3.getSelectedItem()).toString();
        if (Objects.equals(pt, "누르기") && AutoClicker.work.keycode != null && key == AutoClicker.work.keycode && !AutoClicker.work.changing && !keyboard) {
            startOrStop();
        }
    }
    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        int key = e.getButton();
        if (AutoClicker.work.keycode != null && key == AutoClicker.work.keycode && !AutoClicker.work.changing && !keyboard) {
            startOrStop();
        }

        if (AutoClicker.work.changing && key != 1) {
            changeKeyCode(key, "","마우스 버튼 코드", false);
        }

        if (AutoClicker.work.changing && key == 1) {
            cancelChangeKeyCode();
        }
    }
    //mouse

    private void startOrStop() {
        if (AutoClicker.work.start) {
            AutoClicker.work.start = false;
            AutoClicker.work.changeKey.setEnabled(true);
            AutoClicker.work.auto.setEnabled(true);
            AutoClicker.work.help.setEnabled(true);
            AutoClicker.work.CBmenu.setEnabled(true);
            AutoClicker.work.CBmenu2.setEnabled(true);
            AutoClicker.work.CBmenu3.setEnabled(true);
            AutoClicker.work.executorService.shutdown();
        } else {
            AutoClicker.work.start();
        }
    }

    private void changeKeyCode(int keycode, String key, String label, boolean keyboard) {
        AutoClicker.work.keycode = keycode;
        AutoClicker.work.changing = false;
        AutoClicker.work.changeKey.setEnabled(true);
        AutoClicker.work.keycode_l.setText(key + " (" + label+": " + keycode + ")");
        GlobalKeyListener.keyboard = keyboard;
        AutoClicker.work.dialog.setVisible(false);
    }

    private void cancelChangeKeyCode(){
        AutoClicker.work.changing = false;
        AutoClicker.work.changeKey.setEnabled(true);
        AutoClicker.work.dialog.setVisible(false);
    }
}
