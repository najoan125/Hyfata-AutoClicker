package com.hyfata.autoclicker.ui.settings.autoclick;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.hyfata.autoclicker.GlobalKeyListener;
import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.utils.settings.UserSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ACKey {
    private static ACKey instance;

    private JButton changeKeyButton, resetButton;
    private final JLabel keyLabel = new JLabel();
    private JDialog changingKeyDialog;

    public ACKey() {
        instance = this;
    }

    public static ACKey getInstance() {
        return instance;
    }

    protected void setKeyCode(Integer keyCode) {
        boolean keyboard = UserSettings.isKeyboard();

        GlobalKeyListener.keycode = keyCode;
        GlobalKeyListener.isKeyboard = keyboard;

        if (keyCode == null) {
            keyLabel.setText(Locale.getNotSet());
        }
        else {
            setKeyText(keyCode, keyboard);
        }
    }

    public void setKeyText(Integer keycode, boolean keyboard) {
        if (keyboard) {
            String keyChar = NativeKeyEvent.getKeyText(keycode);
            if (keyChar.startsWith(Toolkit.getProperty("AWT.unknown", "Unknown"))) {
                keyLabel.setText(" (" + Locale.getKeyCode()+": " + keycode + ")");
            } else {
                keyLabel.setText(keyChar + " (" + Locale.getKeyCode()+": " + keycode + ")");
            }
        }
        else {
            keyLabel.setText(" (" + Locale.getMouseButtonCode()+": " + keycode + ")");
        }
    }

    protected JButton getResetButton() {
        resetButton = new JButton(Locale.getReset());

        ActionListener buttonListener = e -> {
            keyLabel.setText(Locale.getNotSet());
            GlobalKeyListener.keycode = null;
        };
        resetButton.addActionListener(buttonListener);
        return resetButton;
    }

    protected JLabel getKeyLabel() {
        return keyLabel;
    }

    protected JButton getChangeKeyButton() {
        changeKeyButton = new JButton(Locale.getChangeKey());

        JOptionPane pane = new JOptionPane(Locale.getKeyListening(), JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
        changingKeyDialog = pane.createDialog(Locale.getKeyListen());

        ActionListener changeKey = e -> changeKeyButtonListener();
        changeKeyButton.addActionListener(changeKey);
        return changeKeyButton;
    }

    private void changeKeyButtonListener() {
        changeKeyButton.setEnabled(false);
        GlobalKeyListener.isChanging = true;
        changingKeyDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        changingKeyDialog.setVisible(true);
    }

    protected void setAllEnabled(boolean bool) {
        changeKeyButton.setEnabled(bool);
        resetButton.setEnabled(bool);
    }

    public void onKeyChanged() {
        changeKeyButton.setEnabled(true);
        changingKeyDialog.setVisible(false);
    }
}
