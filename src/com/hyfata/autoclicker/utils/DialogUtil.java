package com.hyfata.autoclicker.utils;

import com.hyfata.autoclicker.AutoClicker;

import javax.swing.*;

public class DialogUtil {
    public static void showErrorDialog(Exception e, String content, String title) {
        JOptionPane.showMessageDialog(null, content+ "\nDiscord Tag: "+ AutoClicker.DISCORD_TAG+"\n\n"+e.getMessage(), title, JOptionPane.ERROR_MESSAGE);
    }

    public static void showErrorDialog(String content, String title) {
        JOptionPane.showMessageDialog(null, content, title, JOptionPane.ERROR_MESSAGE);
    }
}
