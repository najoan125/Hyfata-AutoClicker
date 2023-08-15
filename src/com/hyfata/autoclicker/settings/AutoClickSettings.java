package com.hyfata.autoclicker.settings;

import com.hyfata.autoclicker.locale.Locale;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;

public class AutoClickSettings extends JFrame {
    public static JFormattedTextField autoDelay;
    public static JComboBox<String> delayUnit;

    ArrayList<JPanel> panels = new ArrayList<>();
    public JPanel getPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        initPanels();
        for (JPanel p : panels) {
            p.setLayout(new FlowLayout(FlowLayout.LEFT));
            panel.add(p);
        }
        return panel;
    }

    private void initPanels() {
        delay(100, Locale.getDelayMs());
    }

    private void delay(int defaultDelay, String unit) {
        JPanel panel = new JPanel();
        panel.add(new JLabel(Locale.getAutoClickDelay()));

        autoDelay = getIntTextField();
        autoDelay.setPreferredSize(new Dimension(80, 23));
        autoDelay.setValue(defaultDelay);
        panel.add(autoDelay);

        String[] menu = {Locale.getDelayMs(), Locale.getDelayMicros()};
        delayUnit = new JComboBox<>(menu);
        delayUnit.setPreferredSize(new Dimension(120, 23));
        delayUnit.setSelectedItem(unit);
        panel.add(delayUnit);
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
}
