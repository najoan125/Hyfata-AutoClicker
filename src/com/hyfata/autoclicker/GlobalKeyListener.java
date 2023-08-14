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
        String pt = Objects.requireNonNull(Main.work.CBmenu3.getSelectedItem()).toString();
        if (Objects.equals(pt, "누르기") && !isPressed && Main.work.keycode != null && key == Main.work.keycode && !Main.work.changing && keyboard) {
            startOrStop();
            isPressed = true;
        }
    }
    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        int key = e.getKeyCode();
        if (Main.work.keycode != null && key == Main.work.keycode && !Main.work.changing && keyboard) {
            startOrStop();
            isPressed = false;
        }

        if (Main.work.changing && key != NativeKeyEvent.VC_ESCAPE) {
            String keychar = NativeKeyEvent.getKeyText(key);
            if (keychar.startsWith(Toolkit.getProperty("AWT.unknown", "Unknown"))){
                changeKeyCode(key, "", "키코드", true);
            }
            else {
                changeKeyCode(key, keychar, "키코드", true);
            }
        }

        if (Main.work.changing && key == NativeKeyEvent.VC_ESCAPE) {
            cancelChangeKeyCode();
        }
    }
    //keyboard

    //mouse
    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        int key = e.getButton();
        String pt = Objects.requireNonNull(Main.work.CBmenu3.getSelectedItem()).toString();
        if (Objects.equals(pt, "누르기") && Main.work.keycode != null && key == Main.work.keycode && !Main.work.changing && !keyboard) {
            startOrStop();
        }
    }
    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        int key = e.getButton();
        if (Main.work.keycode != null && key == Main.work.keycode && !Main.work.changing && !keyboard) {
            startOrStop();
        }

        if (Main.work.changing && key != 1) {
            changeKeyCode(key, "","마우스 버튼 코드", false);
        }

        if (Main.work.changing && key == 1) {
            cancelChangeKeyCode();
        }
    }
    //mouse

    private void startOrStop() {
        if (Main.work.start) {
            Main.work.start = false;
            Main.work.changeKey.setEnabled(true);
            Main.work.auto.setEnabled(true);
            Main.work.help.setEnabled(true);
            Main.work.CBmenu.setEnabled(true);
            Main.work.CBmenu2.setEnabled(true);
            Main.work.CBmenu3.setEnabled(true);
            Main.work.executorService.shutdown();
        } else {
            Main.work.start();
        }
    }

    private void changeKeyCode(int keycode, String key, String label, boolean keyboard) {
        Main.work.keycode = keycode;
        Main.work.changing = false;
        Main.work.changeKey.setEnabled(true);
        Main.work.keycode_l.setText(key + " (" + label+": " + keycode + ")");
        GlobalKeyListener.keyboard = keyboard;
        Main.work.dialog.setVisible(false);
    }

    private void cancelChangeKeyCode(){
        Main.work.changing = false;
        Main.work.changeKey.setEnabled(true);
        Main.work.dialog.setVisible(false);
    }
}
