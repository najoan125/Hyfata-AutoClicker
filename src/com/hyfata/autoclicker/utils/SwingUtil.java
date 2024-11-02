package com.hyfata.autoclicker.utils;

import com.hyfata.autoclicker.AutoClicker;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.text.ParseException;

public class SwingUtil {
    public static void showErrorDialog(Exception e, String content, String title) {
        JOptionPane.showMessageDialog(null, content+ "\nDiscord Tag: "+ AutoClicker.DISCORD_TAG+"\n\n"+e.getMessage(), title, JOptionPane.ERROR_MESSAGE);
    }

    public static void showErrorDialog(String content, String title) {
        JOptionPane.showMessageDialog(null, content, title, JOptionPane.ERROR_MESSAGE);
    }

    public static JFormattedTextField getIntTextField() {
        NumberFormatter formatter = getNumberFormatter();
        JFormattedTextField textField = new JFormattedTextField(formatter);

        textField.addCaretListener(e -> {
            int caretPosition = textField.getCaretPosition();
            int textLength = textField.getText().length();

            if (caretPosition < textLength && textField.getText().equals("0")) {
                textField.setCaretPosition(textLength);
            }
        });

        textField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                SwingUtilities.invokeLater(() -> {
                    JTextField tf = (JTextField)e.getSource();
                    int offset = tf.viewToModel(e.getPoint());
                    tf.setCaretPosition(offset);
                });
            }
        });

        return textField;
    }

    private static NumberFormatter getNumberFormatter() {
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format) {
            @Override
            public Object stringToValue(String text) throws ParseException {
                if (text != null && text.isEmpty()) {
                    return 0;
                }
                return super.stringToValue(text);
            }
        };

        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        return formatter;
    }

    public static JPanel getScrollablePanel(Component view) {
        JScrollPane scrollPane = new JScrollPane(view);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        JPanel scrollablePanel = new JPanel(new BorderLayout());
        scrollablePanel.add(scrollPane, BorderLayout.CENTER);
        return scrollablePanel;
    }

    public static JComboBox<String> getStringComboBox(int width, int height, String[] items, String selected) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setPreferredSize(new Dimension(width, height));
        comboBox.setSelectedItem(selected);
        return comboBox;
    }
}
