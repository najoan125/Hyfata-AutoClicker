package com.hyfata.autoclicker.ui.settings;

import com.hyfata.autoclicker.AutoClicker;
import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.ui.Design;
import com.hyfata.autoclicker.utils.settings.SettingsUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class PresetUI {
    JLabel currentPreset;
    static JList<String> presets;
    static JScrollPane presetScroll;
    static DefaultListModel<String> presetsModel;
    static JButton OK, plus, delete, rename;
    ArrayList<JPanel> panels = new ArrayList<>();
    HashMap<Integer,Integer> addedHeights = new HashMap<>(); //index, height
    public JPanel getPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        initPanels();

        int height = 35;

        int i = 0;
        for (JPanel p : panels) {
            p.setLayout(new FlowLayout(FlowLayout.CENTER));
            p.setPreferredSize(new Dimension(Design.WIDTH, height));
            if (addedHeights.containsKey(i)){
                p.setPreferredSize(new Dimension(Design.WIDTH,height+addedHeights.get(i)));
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
        currentPreset = new JLabel(Locale.getCurrentPreset() + SettingsUtil.getCurrentPreset());
        panel.add(currentPreset);
        panels.add(panel);
    }

    private void presets() {
        presetsModel = new DefaultListModel<>();
        presets = new JList<>(presetsModel);
        presets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        for (String s : SettingsUtil.getPresets()) {
            presetsModel.addElement(s);
        }

        presetScroll = new JScrollPane(presets);
        presetScroll.getViewport().setPreferredSize(new Dimension(getPresetsWidth(), 90));

        JPanel scrollPanel = Design.getScrollablePanel(presetScroll);
        panels.add(scrollPanel);
        addHeight(80);
    }

    private int getPresetsWidth() {
        int width = presets.getPreferredSize().width;
        if (width >= 400) width = 400;
        return Math.max(width, 140);
    }

    private void editPreset() {
        JPanel panel = new JPanel();
        plus = new JButton("+");
        plus.setPreferredSize(new Dimension(25, 25));
        plus.setFont(plus.getFont().deriveFont(20.0f));
        plus.setMargin(new Insets(0,0,5,0));
        plus.addActionListener(e -> {
            String preset = JOptionPane.showInputDialog(Locale.getInputPreset());
            if (preset == null) {
                return;
            }
            preset = preset.trim();
            if (!preset.isEmpty() && !SettingsUtil.getPresets().contains(preset)) {
                SettingsUtil.addPreset(preset);
                presetsModel.addElement(preset);
                presetScroll.getViewport().setPreferredSize(new Dimension(getPresetsWidth(), 90));
                loadPreset(preset);
            }
        });

        delete = new JButton("-");
        delete.setPreferredSize(new Dimension(25,25));
        delete.setFont(delete.getFont().deriveFont(20.0f));
        delete.setMargin(new Insets(0,0,7,0));
        delete.addActionListener(e -> {
            String selectedPreset = presets.getSelectedValue();
            if (selectedPreset == null)
                return;
            if (selectedPreset.equals("default")) {
                AutoClicker.showErrorDialog(Locale.getCantRemoveDefault(), "Error removing preset");
            }
            else if (selectedPreset.equals(SettingsUtil.getCurrentPreset())) {
                loadPreset("default");
                presetsModel.removeElement(selectedPreset);
                presetScroll.getViewport().setPreferredSize(new Dimension(getPresetsWidth(), 90));
                SettingsUtil.removePreset(selectedPreset);
            }
            else {
                presetsModel.removeElement(selectedPreset);
                presetScroll.getViewport().setPreferredSize(new Dimension(getPresetsWidth(), 90));
                SettingsUtil.removePreset(selectedPreset);
            }
        });

        rename = new JButton(Locale.getRename());
        rename.addActionListener(e -> {
            String selectedPreset = presets.getSelectedValue();
            if (selectedPreset == null)
                return;
            if (selectedPreset.equals("default")) {
                AutoClicker.showErrorDialog(Locale.getCantRenameDefault(), "Error renaming preset");
                return;
            }

            String renamedPreset = JOptionPane.showInputDialog(Locale.getInputPreset());
            if (renamedPreset == null) {
                return;
            }
            renamedPreset = renamedPreset.trim();
            if (!renamedPreset.isEmpty() && !SettingsUtil.getPresets().contains(renamedPreset)) {
                if (selectedPreset.equals(SettingsUtil.getCurrentPreset())) {
                    SettingsUtil.addPreset(renamedPreset);
                    presetsModel.addElement(renamedPreset);
                    loadPreset(renamedPreset);
                    presetsModel.removeElement(selectedPreset);
                    presetScroll.getViewport().setPreferredSize(new Dimension(getPresetsWidth(), 90));
                    SettingsUtil.removePreset(selectedPreset);
                }
                else {
                    SettingsUtil.renamePreset(selectedPreset, renamedPreset);
                    presetsModel.addElement(renamedPreset);
                    presetsModel.removeElement(selectedPreset);
                    presetScroll.getViewport().setPreferredSize(new Dimension(getPresetsWidth(), 90));
                }
            }
        });

        panel.add(plus);
        panel.add(delete);
        panel.add(rename);
        panels.add(panel);
    }

    private void okButton() {
        JPanel panel = new JPanel();
        OK = new JButton("OK");

        OK.addActionListener(e -> {
            String preset = presets.getSelectedValue();
            if (preset != null) {
                loadPreset(preset);
            }
        });
        panel.add(OK);
        panels.add(panel);
    }

    private void loadPreset(String preset) {
        SettingsUtil.loadCurrentSettings();
        SettingsUtil.savePreset(SettingsUtil.getCurrentPreset());
        SettingsUtil.setCurrentPreset(preset);
        currentPreset.setText(Locale.getCurrentPreset() + SettingsUtil.getCurrentPreset());
        SettingsUtil.loadPreset(preset);
        AutoClickSettingsUI.reload();
    }

    public static void setAllEnabled(boolean bool) {
        presets.setEnabled(bool);
        OK.setEnabled(bool);
        plus.setEnabled(bool);
        delete.setEnabled(bool);
        rename.setEnabled(bool);
    }

    private void addHeight(int height) {
        addedHeights.put(panels.size()-1, height);
    }
}
