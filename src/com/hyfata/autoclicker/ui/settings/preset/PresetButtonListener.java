package com.hyfata.autoclicker.ui.settings.preset;

import com.hyfata.autoclicker.AutoClicker;
import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.utils.settings.SettingsUtil;

import javax.swing.*;
import java.awt.*;

public class PresetButtonListener {
    protected static void plusListener() {
        String preset = JOptionPane.showInputDialog(Locale.getInputPreset());
        if (preset == null) {
            return;
        }
        preset = preset.trim();
        if (!preset.isEmpty() && !SettingsUtil.getPresets().contains(preset)) {
            SettingsUtil.addPreset(preset);
            PresetUI.presetListModel.addElement(preset);
            PresetUI.presetScrollPane.getViewport().setPreferredSize(new Dimension(PresetUtils.getPresetsWidth(), 90));
            PresetUtils.loadPreset(preset);
        }
    }

    protected static void deleteListener() {
        String selectedPreset = PresetUI.presetList.getSelectedValue();
        if (selectedPreset == null)
            return;
        if (selectedPreset.equals("default")) {
            AutoClicker.showErrorDialog(Locale.getCantRemoveDefault(), "Error removing preset");
        } else if (selectedPreset.equals(SettingsUtil.getCurrentPreset())) {
            PresetUtils.loadPreset("default");
            PresetUI.presetListModel.removeElement(selectedPreset);
            PresetUI.presetScrollPane.getViewport().setPreferredSize(new Dimension(PresetUtils.getPresetsWidth(), 90));
            SettingsUtil.removePreset(selectedPreset);
        } else {
            PresetUI.presetListModel.removeElement(selectedPreset);
            PresetUI.presetScrollPane.getViewport().setPreferredSize(new Dimension(PresetUtils.getPresetsWidth(), 90));
            SettingsUtil.removePreset(selectedPreset);
        }
    }

    protected static void renameListener() {
        String selectedPreset = PresetUI.presetList.getSelectedValue();
        if (selectedPreset == null)
            return;
        if (selectedPreset.equals("default")) {
            AutoClicker.showErrorDialog(Locale.getCantRenameDefault(), "Error renaming preset");
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
                PresetUI.presetScrollPane.getViewport().setPreferredSize(new Dimension(PresetUtils.getPresetsWidth(), 90));
                SettingsUtil.removePreset(selectedPreset);
            } else {
                SettingsUtil.renamePreset(selectedPreset, renamedPreset);
                PresetUI.presetListModel.addElement(renamedPreset);
                PresetUI.presetListModel.removeElement(selectedPreset);
                PresetUI.presetScrollPane.getViewport().setPreferredSize(new Dimension(PresetUtils.getPresetsWidth(), 90));
            }
        }
    }
}
