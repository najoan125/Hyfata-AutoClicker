package com.hyfata.autoclicker.ui.settings;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.hyfata.autoclicker.GlobalKeyListener;
import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.ui.Design;
import com.hyfata.autoclicker.utils.settings.UserSettings;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AutoClickSettingsUI extends JFrame {
    public static JFormattedTextField delay;
    public static JComboBox<String> delayUnits, mouseButtons, holdToggles;
    public static JButton changeKeyButton, resetButton;
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
        String delayUnit;
        if (Objects.equals(UserSettings.getDelayUnit(), "ms")) {
            delayUnit = Locale.getDelayMs();
        } else {
            delayUnit = Locale.getDelayMicros();
        }
        delay(Integer.parseInt(UserSettings.getDelay()), delayUnit);

        String mouseButton;
        if (Objects.equals(UserSettings.getMouseButton(),"left")) {
            mouseButton = Locale.getMouseLeft();
        }
        else if (Objects.equals(UserSettings.getMouseButton(), "middle")) {
            mouseButton = Locale.getMouseMiddle();
        }
        else {
            mouseButton = Locale.getMouseRight();
        }
        mouseButton(mouseButton);

        String holdToggle;
        if (UserSettings.isToggle()) holdToggle = Locale.getKeyToggle();
        else holdToggle = Locale.getKeyHold();
        holdToggle(holdToggle);

        Integer keycode;
        boolean keyboard = UserSettings.isKeyboard();
        if (UserSettings.getKeycode() == -1)
            keycode = null;
        else
            keycode = UserSettings.getKeycode();
        GlobalKeyListener.keycode = keycode;
        GlobalKeyListener.isKeyboard = keyboard;
        key = new JLabel();
        if (keycode == null) {
            key.setText(Locale.getNotSet());
        }
        else {
            setKeyText(keycode, keyboard);
        }
        key();

        changeKey();
    }

    public static void reload() {
        delay.setValue(Integer.parseInt(UserSettings.getDelay()));

        String delayUnit;
        if (Objects.equals(UserSettings.getDelayUnit(), "ms")) {
            delayUnit = Locale.getDelayMs();
        } else {
            delayUnit = Locale.getDelayMicros();
        }
        delayUnits.setSelectedItem(delayUnit);

        String mouseButton;
        if (Objects.equals(UserSettings.getMouseButton(),"left")) {
            mouseButton = Locale.getMouseLeft();
        }
        else if (Objects.equals(UserSettings.getMouseButton(), "middle")) {
            mouseButton = Locale.getMouseMiddle();
        }
        else {
            mouseButton = Locale.getMouseRight();
        }
        mouseButtons.setSelectedItem(mouseButton);

        String holdToggle;
        if (UserSettings.isToggle()) holdToggle = Locale.getKeyToggle();
        else holdToggle = Locale.getKeyHold();
        holdToggles.setSelectedItem(holdToggle);

        Integer keycode;
        boolean keyboard = UserSettings.isKeyboard();
        if (UserSettings.getKeycode() == -1)
            keycode = null;
        else
            keycode = UserSettings.getKeycode();
        GlobalKeyListener.keycode = keycode;
        GlobalKeyListener.isKeyboard = keyboard;
        if (keycode == null) {
            key.setText(Locale.getNotSet());
        }
        else {
            AutoClickSettingsUI.setKeyText(keycode, keyboard);
        }
    }

    private static void setKeyText(Integer keycode, boolean keyboard) {
        if (keyboard) {
            String keyChar = NativeKeyEvent.getKeyText(keycode);
            if (keyChar.startsWith(Toolkit.getProperty("AWT.unknown", "Unknown"))) {
                key.setText(" (" + Locale.getKeyCode()+": " + keycode + ")");
            } else {
                key.setText(keyChar + " (" + Locale.getKeyCode()+": " + keycode + ")");
            }
        }
        else {
            key.setText(" (" + Locale.getMouseButtonCode()+": " + keycode + ")");
        }
    }

    private void delay(int defaultDelay, String unit) {
        JPanel panel = new JPanel();
        panel.add(new JLabel(Locale.getAutoClickDelay()));

        delay = getIntTextField();
        delay.setPreferredSize(new Dimension(80, 23));
        delay.setValue(defaultDelay);
        panel.add(delay);

        String[] menu = {Locale.getDelayMs(), Locale.getDelayMicros()};
        delayUnits = new JComboBox<>(menu);
        delayUnits.setPreferredSize(new Dimension(160, 23));
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
        resetButton = new JButton(Locale.getReset());
        ActionListener buttonListener = e -> {
            key.setText(Locale.getNotSet());
            GlobalKeyListener.keycode = null;
        };
        resetButton.addActionListener(buttonListener);
        panel.add(resetButton);
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
                    return 0L;
                }
                return super.stringToValue(text);
            }
        };

        formatter.setValueClass(Long.class);
        formatter.setMinimum(0L);
        formatter.setMaximum(Long.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        return formatter;
    }

    public static void setAllEnabled(boolean bool) {
        delayUnits.setEnabled(bool);
        mouseButtons.setEnabled(bool);
        holdToggles.setEnabled(bool);
        changeKeyButton.setEnabled(bool);
        delay.setEnabled(bool);
        resetButton.setEnabled(bool);
    }

    private void addHeight(int height) {
        addedHeights.put(panels.size()-1, height);
    }
}
