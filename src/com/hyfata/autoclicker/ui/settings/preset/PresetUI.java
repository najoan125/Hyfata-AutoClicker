package com.hyfata.autoclicker.ui.settings.preset;

import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.utils.SwingUtil;
import com.hyfata.autoclicker.utils.JPanelUtil;
import com.hyfata.autoclicker.utils.PresetUtil;
import com.hyfata.autoclicker.utils.settings.SettingsUtil;

import javax.swing.*;
import java.awt.*;

public class PresetUI {
    private static PresetUI instance;

    private final JPanelUtil panelUtil = new JPanelUtil();
    private JButton OK, plus, delete, rename;
    private JLabel currentPresetLabel;

    public PresetUI() {
        instance = this;
    }

    public static PresetUI getInstance() {
        if (instance == null) {
            instance = new PresetUI();
        }
        return instance;
    }

    public JPanel getPanel() {
        initPanels();
        return panelUtil.createPanel(FlowLayout.CENTER);
    }

    private void initPanels() {
        currentPreset();
        presets();
        editPreset();
        okButton();
    }

    private void currentPreset() {
        JPanel panel = new JPanel();
        currentPresetLabel = new JLabel(Locale.getCurrentPreset() + SettingsUtil.getCurrentPreset());
        panel.add(currentPresetLabel);
        panelUtil.register(panel);
    }

    private void presets() {
        PresetList presetList = new PresetList();
        JPanel panel = presetList.createPanel();
        panelUtil.register(panel);
        panelUtil.addHeight(130);
    }

    private void editPreset() {
        JPanel panel = new JPanel();
        PresetEditButton presetEditButton = new PresetEditButton();

        plus = presetEditButton.newPlusButton();
        delete = presetEditButton.newDeleteButton();
        rename = presetEditButton.newRenameButton();

        panel.add(plus);
        panel.add(delete);
        panel.add(rename);
        panelUtil.register(panel);
    }

    private void okButton() {
        JPanel panel = new JPanel();
        OK = new JButton("OK");

        OK.addActionListener(e -> {
            String preset = PresetList.getInstance().getSelectedPreset();
            if (preset != null) {
                PresetUtil.saveAndLoad(preset);
            } else {
                SwingUtil.showErrorDialog(Locale.getPresetNotSelected(), "Preset not selected error");
            }
        });
        panel.add(OK);
        panelUtil.register(panel);
    }

    public void setAllEnabled(boolean bool) {
        PresetList.getInstance().setEnabled(bool);
        OK.setEnabled(bool);
        plus.setEnabled(bool);
        delete.setEnabled(bool);
        rename.setEnabled(bool);
    }

    public void setCurrentPresetText(String text) {
        currentPresetLabel.setText(text);
    }
}
