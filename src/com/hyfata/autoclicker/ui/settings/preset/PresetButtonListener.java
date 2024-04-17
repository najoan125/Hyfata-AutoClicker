package com.hyfata.autoclicker.ui.settings.preset;

import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.utils.DialogUtil;
import com.hyfata.autoclicker.utils.settings.SettingsUtil;

import javax.swing.*;
import java.awt.*;

public class PresetButtonListener {
    protected static void plusListener() {
        String preset = JOptionPane.showInputDialog(Locale.getInputPreset());
        if (preset == null) {
            return; //cancel
        }
        preset = preset.trim();
        if (preset.isEmpty()) {
            DialogUtil.showErrorDialog(Locale.getInputPresetEmpty(), "Preset name error");
            plusListener();
        }
        else if (SettingsUtil.getPresets().contains(preset)) {
            //TODO: 유효성 검사
        }
        else {
            SettingsUtil.addPreset(preset);
            PresetUI.presetListModel.addElement(preset);
            PresetUI.presetScrollPane.getViewport().setPreferredSize(new Dimension(PresetUtils.getPresetListWidth(), PresetUI.presetScrollPaneHeight));
            PresetUtils.loadPreset(preset);
        }
    }

    protected static void deleteListener() {
        String selectedPreset = PresetUI.presetList.getSelectedValue();
        if (selectedPreset == null)
            return;
        if (selectedPreset.equals("default")) {
            DialogUtil.showErrorDialog(Locale.getCantRemoveDefault(), "Error removing preset");
        } else if (selectedPreset.equals(SettingsUtil.getCurrentPreset())) {
            PresetUtils.loadPreset("default");
            PresetUI.presetListModel.removeElement(selectedPreset);
            PresetUI.presetScrollPane.getViewport().setPreferredSize(new Dimension(PresetUtils.getPresetListWidth(), PresetUI.presetScrollPaneHeight));
            SettingsUtil.removePreset(selectedPreset);
        } else {
            PresetUI.presetListModel.removeElement(selectedPreset);
            PresetUI.presetScrollPane.getViewport().setPreferredSize(new Dimension(PresetUtils.getPresetListWidth(), PresetUI.presetScrollPaneHeight));
            SettingsUtil.removePreset(selectedPreset);
        }
    }

    protected static void renameListener() {
        String selectedPreset = PresetUI.presetList.getSelectedValue();
        if (selectedPreset == null)
            return;
        if (selectedPreset.equals("default")) {
            DialogUtil.showErrorDialog(Locale.getCantRenameDefault(), "Error renaming preset");
            return;
        }

        String renamedPreset = JOptionPane.showInputDialog(Locale.getInputPreset(), selectedPreset);
        if (renamedPreset == null) {
            return;
        }
        renamedPreset = renamedPreset.trim();
        if (!renamedPreset.isEmpty() && !SettingsUtil.getPresets().contains(renamedPreset)) {
            if (selectedPreset.equals(SettingsUtil.getCurrentPreset())) {
                SettingsUtil.addPreset(renamedPreset);
                PresetUI.presetListModel.addElement(renamedPreset);
                PresetUtils.loadPreset(renamedPreset);
                PresetUI.presetListModel.removeElement(selectedPreset);
                PresetUI.presetScrollPane.getViewport().setPreferredSize(new Dimension(PresetUtils.getPresetListWidth(), PresetUI.presetScrollPaneHeight));
                SettingsUtil.removePreset(selectedPreset);
            } else {
                SettingsUtil.renamePreset(selectedPreset, renamedPreset);
                PresetUI.presetListModel.addElement(renamedPreset);
                PresetUI.presetListModel.removeElement(selectedPreset);
                PresetUI.presetScrollPane.getViewport().setPreferredSize(new Dimension(PresetUtils.getPresetListWidth(), PresetUI.presetScrollPaneHeight));
            }
        }
    }
}
