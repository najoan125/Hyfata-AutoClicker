package com.hyfata.autoclicker.ui.settings.autoclick;

import com.hyfata.autoclicker.AutoClickHandler;
import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.utils.JPanelUtil;
import com.hyfata.autoclicker.utils.settings.AutoClickSettingsUtil;
import com.hyfata.autoclicker.utils.settings.UserSettings;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AutoClickSettingsUI extends JFrame {
    private static AutoClickSettingsUI instance;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final JLabel clicks = new JLabel();

    private final ACDelay delayUI = new ACDelay();
    private final ACHotKey hotKeyUI = new ACHotKey();
    private final ACMouseButton mouseButtonUI = new ACMouseButton();
    private final ACHoldToggle holdToggleUI = new ACHoldToggle();

    private final JPanelUtil panelUtil = new JPanelUtil();

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

        delay(Integer.parseInt(UserSettings.getDelay()), AutoClickSettingsUtil.getDelayUnit());
        mouseButton(AutoClickSettingsUtil.getMouseButton());
        holdToggle(AutoClickSettingsUtil.getHoldToggle());

        hotKeyUI.initKeyCode(AutoClickSettingsUtil.getKeycode());
        hotKey();

        changeHotKey();
    }

    public void reload() {
        delayUI.setDelay(Integer.parseInt(UserSettings.getDelay()));
        delayUI.setUnit(AutoClickSettingsUtil.getDelayUnit());
        mouseButtonUI.setSelected(AutoClickSettingsUtil.getMouseButton());
        holdToggleUI.setSelected(AutoClickSettingsUtil.getHoldToggle());

        hotKeyUI.initKeyCode(AutoClickSettingsUtil.getKeycode());
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

    private void limitClicks() {
        JPanel panel = new JPanel();
        panel.add(Box.createHorizontalStrut(10));
        panel.add(new JLabel(Locale.getLimitClicks()));
    }

    private void mouseButton(String button) {
        JPanel panel = new JPanel();
        panel.add(Box.createHorizontalStrut(10));
        panel.add(new JLabel(Locale.getMouseButton()));
        panel.add(mouseButtonUI.createComboBox(button));
        panelUtil.register(panel);
    }

    private void holdToggle(String data) {
        JPanel panel = new JPanel();
        panel.add(Box.createHorizontalStrut(10));
        panel.add(new JLabel(Locale.getKeyHold() + " / " + Locale.getKeyToggle() + ": "));
        panel.add(holdToggleUI.createComboBox(data));
        panelUtil.register(panel);
    }

    private void hotKey() {
        JPanel panel = new JPanel();
        panel.add(Box.createHorizontalStrut(10));
        panel.add(new JLabel(Locale.getKey()));
        panel.add(hotKeyUI.getKeyLabel());
        panel.add(hotKeyUI.getResetButton());

        panelUtil.addHeight(30);
        panelUtil.register(panel);
    }

    private void changeHotKey() {
        JPanel panel = new JPanel();
        panel.add(Box.createHorizontalStrut(10));
        panel.add(hotKeyUI.getChangeKeyButton());
        panelUtil.register(panel);
    }

    public void setAllEnabled(boolean bool) {
        delayUI.setAllEnabled(bool);
        hotKeyUI.setAllEnabled(bool);
        mouseButtonUI.setEnabled(bool);
        holdToggleUI.setEnabled(bool);
    }

    public ACDelay getDelayUI() {
        return delayUI;
    }

    public ACHotKey getHotKeyUI() {
        return hotKeyUI;
    }

    public ACMouseButton getMouseButtonUI() {
        return mouseButtonUI;
    }

    public ACHoldToggle getHoldToggleUI() {
        return holdToggleUI;
    }
}
