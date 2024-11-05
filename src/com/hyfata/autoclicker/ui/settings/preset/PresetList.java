package com.hyfata.autoclicker.ui.settings.preset;

import com.hyfata.autoclicker.utils.SwingUtil;
import com.hyfata.autoclicker.utils.settings.SettingsUtil;

import javax.swing.*;
import java.awt.*;

public class PresetList {
    private static PresetList instance;

    private JList<String> presetList;
    private DefaultListModel<String> presetListModel;
    private JScrollPane presetScrollPane;

    private final int height = 120;

    public PresetList() {
        instance = this;
    }

    public static PresetList getInstance() {
        if (instance == null)
            new PresetList();
        return instance;
    }

    public JPanel createPanel() {
        presetListModel = new DefaultListModel<>();
        presetList = new JList<>(presetListModel);
        presetList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        for (String s : SettingsUtil.getPresetNames()) {
            presetListModel.addElement(s);
        }

        presetScrollPane = new JScrollPane(presetList);
        presetScrollPane.getViewport().setPreferredSize(new Dimension(getWidth(), height));

        return SwingUtil.getScrollablePanel(presetScrollPane);
    }

    public int getWidth() {
        int maxWidth = 400;
        int minWidth = 140;
        int width = presetList.getPreferredSize().width;
        if (width >= maxWidth) width = maxWidth;
        return Math.max(width, minWidth);
    }

    public String getSelectedPreset() {
        return presetList.getSelectedValue();
    }

    public void addPreset(String name) {
        presetListModel.addElement(name);
    }

    public void removePreset(String name) {
        presetListModel.removeElement(name);
    }

    public void refresh() {
        presetScrollPane.getViewport().setPreferredSize(new Dimension(getWidth(), height));
        presetListModel.clear();
        for (String s : SettingsUtil.getPresetNames()) {
            presetListModel.addElement(s);
        }
    }

    public void setEnabled(boolean enabled) {
        presetList.setEnabled(enabled);
    }
}
