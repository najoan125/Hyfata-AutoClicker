package com.hyfata.autoclicker.ui.settings.preset;

import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.ui.Design;
import com.hyfata.autoclicker.utils.settings.SettingsUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class PresetUI {
    protected static JList<String> presetList;
    protected static JScrollPane presetScrollPane;
    protected static DefaultListModel<String> presetListModel;
    protected static JButton OK, plus, delete, rename;
    protected static JLabel currentPresetLabel;

    private final ArrayList<JPanel> panels = new ArrayList<>();
    private final HashMap<Integer, Integer> addedHeights = new HashMap<>(); //index, height

    public JPanel getPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        initPanels();

        int height = 35;

        int i = 0;
        for (JPanel p : panels) {
            p.setLayout(new FlowLayout(FlowLayout.CENTER));
            p.setPreferredSize(new Dimension(Design.WIDTH, height));
            if (addedHeights.containsKey(i)) {
                p.setPreferredSize(new Dimension(Design.WIDTH, height + addedHeights.get(i)));
            }
            panel.add(p);

            i++;
        }
        return panel;
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
        panels.add(panel);
    }

    private void presets() {
        presetListModel = new DefaultListModel<>();
        presetList = new JList<>(presetListModel);
        presetList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        for (String s : SettingsUtil.getPresets()) {
            presetListModel.addElement(s);
        }

        presetScrollPane = new JScrollPane(presetList);
        presetScrollPane.getViewport().setPreferredSize(new Dimension(PresetUtils.getPresetsWidth(), 90));

        JPanel scrollPanel = Design.getScrollablePanel(presetScrollPane);
        panels.add(scrollPanel);
        addHeight(80);
    }


    private void editPreset() {
        JPanel panel = new JPanel();
        plus = PresetEditButton.newPlusButton();
        delete = PresetEditButton.newDeleteButton();
        rename = PresetEditButton.newRenameButton();

        panel.add(plus);
        panel.add(delete);
        panel.add(rename);
        panels.add(panel);
    }

    private void okButton() {
        JPanel panel = new JPanel();
        OK = new JButton("OK");

        OK.addActionListener(e -> {
            String preset = presetList.getSelectedValue();
            if (preset != null) {
                PresetUtils.loadPreset(preset);
            }
        });
        panel.add(OK);
        panels.add(panel);
    }

    private void addHeight(int height) {
        addedHeights.put(panels.size() - 1, height);
    }
}
