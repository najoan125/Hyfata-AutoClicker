package com.hyfata.autoclicker.ui.settings.autoclick;

import com.hyfata.autoclicker.AutoClickHandler;
import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.utils.JPanelUtil;
import com.hyfata.autoclicker.utils.SwingUtil;
import com.hyfata.autoclicker.utils.settings.UserSettings;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AutoClickSettingsUI extends JFrame {
    private static AutoClickSettingsUI instance;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final JPanelUtil panelUtil = new JPanelUtil();

    private final ACSettings settings = new ACSettings();
    private final ACDelay delayUI = new ACDelay();
    private final ACKey key = new ACKey();

    private final JLabel clicks = new JLabel();
    private JComboBox<String> mouseButtons, holdToggles;

    public AutoClickSettingsUI() {
        instance = this;
    }

    public static AutoClickSettingsUI getInstance() {
        if (instance == null) {
            new AutoClickSettingsUI();
        }
        return instance;
    }

    public JPanel getPanel() {
        initPanels();
        return panelUtil.createPanel(FlowLayout.LEFT);
    }

    private void initPanels() {
        clicks();
        executorService.scheduleAtFixedRate(this::calculateClicks, 0, 10, TimeUnit.MILLISECONDS); // refresh ui

        delay(Integer.parseInt(UserSettings.getDelay()), settings.getDelayUnit());
        mouseButton(settings.getMouseButton());
        holdToggle(settings.getHoldToggle());

        key.setKeyCode(settings.getKeycode());
        key();

        changeKey();
    }

    public void reload() {
        delayUI.setDelay(Integer.parseInt(UserSettings.getDelay()));
        delayUI.setUnit(settings.getDelayUnit());
        mouseButtons.setSelectedItem(settings.getMouseButton());
        holdToggles.setSelectedItem(settings.getHoldToggle());

        key.setKeyCode(settings.getKeycode());
    }

    private void calculateClicks() {
        clicks.setText(AutoClickHandler.clicks.toString());
    }

    // panel items

    private void clicks() {
        JPanel panel = new JPanel();
        panel.add(Box.createHorizontalStrut(10));
        clicks.setForeground(Color.LIGHT_GRAY);

        panel.add(new JLabel(Locale.getTotalClicks()));
        panel.add(clicks);
        panelUtil.register(panel);
    }

    private void delay(int defaultDelay, String unit) {
        JPanel panel = new JPanel();

        panel.add(Box.createHorizontalStrut(10));
        panel.add(new JLabel(Locale.getAutoClickDelay()));
        panel.add(delayUI.getTextField(defaultDelay));
        panel.add(delayUI.getUnitMenu(unit));
        panelUtil.register(panel);
    }

    private void mouseButton(String button) {
        JPanel panel = new JPanel();
        panel.add(Box.createHorizontalStrut(10));
        panel.add(new JLabel(Locale.getMouseButton()));

        String[] menu = {Locale.getMouseLeft(), Locale.getMouseMiddle(), Locale.getMouseRight()};
        mouseButtons = SwingUtil.getStringComboBox(100, 23, menu, button);
        panel.add(mouseButtons);
        panelUtil.register(panel);
    }

    private void holdToggle(String data) {
        JPanel panel = new JPanel();
        panel.add(Box.createHorizontalStrut(10));
        panel.add(new JLabel(Locale.getKeyHold() + " / " + Locale.getKeyToggle() + ": "));

        String[] menu = {Locale.getKeyHold(), Locale.getKeyToggle()};
        holdToggles = SwingUtil.getStringComboBox(100, 23, menu, data);
        panel.add(holdToggles);
        panelUtil.register(panel);
    }

    private void key() {
        JPanel panel = new JPanel();
        panel.add(Box.createHorizontalStrut(10));
        panel.add(new JLabel(Locale.getKey()));
        panel.add(key.getKeyLabel());
        panel.add(key.getResetButton());

        panelUtil.addHeight(30);
        panelUtil.register(panel);
    }

    private void changeKey() {
        JPanel panel = new JPanel();
        panel.add(Box.createHorizontalStrut(10));
        panel.add(key.getChangeKeyButton());
        panelUtil.register(panel);
    }

    public void setAllEnabled(boolean bool) {
        delayUI.setAllEnabled(bool);
        key.setAllEnabled(bool);
        mouseButtons.setEnabled(bool);
        holdToggles.setEnabled(bool);
    }

    public String getSelectedMouseButton() {
        return Objects.requireNonNull(mouseButtons.getSelectedItem()).toString();
    }

    public String getHoldToggle() {
        return Objects.requireNonNull(holdToggles.getSelectedItem()).toString();
    }
}
