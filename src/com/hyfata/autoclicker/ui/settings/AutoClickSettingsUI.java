package com.hyfata.autoclicker.ui.settings;

import com.hyfata.autoclicker.Design;
import com.hyfata.autoclicker.GlobalKeyListener;
import com.hyfata.autoclicker.locale.Locale;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class AutoClickSettingsUI extends JFrame {
    public static JFormattedTextField autoDelay;
    public static JComboBox<String> delayUnits, mouseButtons, holdToggles;
    public static JButton changeKeyButton;
    public static JLabel key = null;
    public static JDialog changingKeyDialog;

    ArrayList<JPanel> panels = new ArrayList<>();
    HashMap<Integer,Integer> addedHeights = new HashMap<>(); //index, height
    public JPanel getPanel() {
        JPanel panel = new JPanel(null);
        initPanels();

        int y = 10;
        int height = 35;

        int i = 0;
        for (JPanel p : panels) {
            p.setLayout(new FlowLayout(FlowLayout.LEFT));
            p.setBounds(10,y,Design.WIDTH,height);
            panel.add(p);

            if (addedHeights.containsKey(i)){
                y += addedHeights.get(i);
            }

            y+=height;
            i++;
        }
        return panel;
    }


    private void initPanels() {
        delay(100, Locale.getDelayMs());
        mouseButton(Locale.getMouseLeft());
        holdToggle(Locale.getKeyHold());

        if (key == null) key = new JLabel(Locale.getNotSet());
        key();

        changeKey();
    }

    private void delay(int defaultDelay, String unit) {
        JPanel panel = new JPanel();
        panel.add(new JLabel(Locale.getAutoClickDelay()));

        autoDelay = getIntTextField();
        autoDelay.setPreferredSize(new Dimension(80, 23));
        autoDelay.setValue(defaultDelay);
        panel.add(autoDelay);

        String[] menu = {Locale.getDelayMs(), Locale.getDelayMicros()};
        delayUnits = new JComboBox<>(menu);
        delayUnits.setPreferredSize(new Dimension(120, 23));
        delayUnits.setSelectedItem(unit);
        panel.add(delayUnits);
        panels.add(panel);
    }

    private void mouseButton(String button) {
        JPanel panel = new JPanel();
        panel.add(new JLabel(Locale.getMouseButton()));

        String[] menu = {Locale.getMouseLeft(), Locale.getMouseMiddle(), Locale.getMouseRight()};
        mouseButtons = new JComboBox<>(menu);
        mouseButtons.setPreferredSize(new Dimension(100,23));
        mouseButtons.setSelectedItem(button);
        panel.add(mouseButtons);
        panels.add(panel);
    }

    private void holdToggle(String data) {
        JPanel panel = new JPanel();
        panel.add(new JLabel(Locale.getKeyHold() + " / " + Locale.getKeyToggle() + ": "));

        String[] menu = {Locale.getKeyHold(), Locale.getKeyToggle()};
        holdToggles = new JComboBox<>(menu);
        holdToggles.setPreferredSize(new Dimension(100,23));
        holdToggles.setSelectedItem(data);
        panel.add(holdToggles);
        panels.add(panel);
    }

    private void key() {
        JPanel panel = new JPanel();
        panel.add(new JLabel(Locale.getKey()));
        panel.add(key);
        addHeight(30);
        panels.add(panel);
    }

    private void changeKey() {
        JPanel panel = new JPanel();
        changeKeyButton = new JButton(Locale.getChangeKey());

        JOptionPane pane = new JOptionPane(Locale.getKeyListening(), JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
        changingKeyDialog = pane.createDialog(Locale.getKeyListen());

        ActionListener changeKey = e -> {
            changeKeyButton.setEnabled(false);
            GlobalKeyListener.isChanging = true;
            changingKeyDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            changingKeyDialog.setVisible(true);
        };
        changeKeyButton.addActionListener(changeKey);

        panel.add(changeKeyButton);
        panels.add(panel);
    }

    private JFormattedTextField getIntTextField() {
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Long.class);
        formatter.setMinimum(1L);
        formatter.setMaximum(Long.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        return new JFormattedTextField(formatter);
    }
    public static void setAllEnabled(boolean bool) {
        delayUnits.setEnabled(bool);
        mouseButtons.setEnabled(bool);
        holdToggles.setEnabled(bool);
        changeKeyButton.setEnabled(bool);
        autoDelay.setEnabled(bool);
    }

    private void addHeight(int height) {
        addedHeights.put(panels.size()-1, height);
    }
}
