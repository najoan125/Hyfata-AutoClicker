package com.hyfata.autoclicker.ui.settings.preset;

import com.hyfata.autoclicker.GlobalKeyListener;
import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.utils.DialogUtil;
import com.hyfata.autoclicker.utils.PresetUtil;
import com.hyfata.autoclicker.utils.settings.SettingsUtil;

import javax.swing.*;

public class PresetButtonListener {
    protected static void plusListener() {
        GlobalKeyListener.shouldBlocked = true;
        String input = JOptionPane.showInputDialog(Locale.getInputPreset());
        GlobalKeyListener.shouldBlocked = false;
        if (input == null)
            return; //cancel

        input = input.trim();
        if (input.isEmpty()) { // re-input
            DialogUtil.showErrorDialog(Locale.getInputPresetEmpty(), "Preset name error");
            plusListener();
        } else if (SettingsUtil.getPresetNames().contains(input)) { // overwrite because it is already exists
            int answer = JOptionPane.showConfirmDialog(null, Locale.getInputPresetOverwrite().replace("%s", input),
                    "Preset already exists", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                SettingsUtil.removePreset(input);
                SettingsUtil.addPreset(input);
                PresetUtil.saveAndLoad(input);
            }
        } else { // add preset
            SettingsUtil.addPreset(input);
            PresetList.getInstance().addPreset(input);
            PresetList.getInstance().refresh();
            PresetUtil.saveAndLoad(input);
        }
    }

    protected static void deleteListener() {
        String selectedPreset = PresetList.getInstance().getSelectedPreset();
        if (selectedPreset == null) {
            DialogUtil.showErrorDialog(Locale.getPresetNotSelected(), "Preset not selected error");
            return;
        }
        if (selectedPreset.equals("default")) {
            DialogUtil.showErrorDialog(Locale.getCantRemoveDefault(), "Error removing preset");
            return;
        }

        int answer = JOptionPane.showConfirmDialog(null, Locale.getRemovePresetDialog().replace("%s", selectedPreset),
                "Remove preset", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.NO_OPTION) return;

        if (selectedPreset.equals(SettingsUtil.getCurrentPreset())) {
            PresetUtil.saveAndLoad("default");
        }
        PresetList.getInstance().removePreset(selectedPreset);
        PresetList.getInstance().refresh();
        SettingsUtil.removePreset(selectedPreset);
    }

    protected static void renameListener() {
        String selectedPreset = PresetList.getInstance().getSelectedPreset();
        if (selectedPreset == null) {
            DialogUtil.showErrorDialog(Locale.getPresetNotSelected(), "Preset not selected error");
            return;
        }
        if (selectedPreset.equals("default")) {
            DialogUtil.showErrorDialog(Locale.getCantRenameDefault(), "Error renaming preset");
            return;
        }

        GlobalKeyListener.shouldBlocked = true;
        String renamedPreset = JOptionPane.showInputDialog(Locale.getInputPreset(), selectedPreset);
        GlobalKeyListener.shouldBlocked = false;

        if (renamedPreset == null) {
            return; // cancel
        }
        renamedPreset = renamedPreset.trim();

        if (renamedPreset.isEmpty()) {
            DialogUtil.showErrorDialog(Locale.getInputPresetEmpty(), "Preset name error");
            renameListener();
        } else if (SettingsUtil.getPresetNames().contains(renamedPreset)) {
            DialogUtil.showErrorDialog(Locale.getPresetAlreadyExists().replace("%s", renamedPreset), "Preset already exists");
        } else {
            if (selectedPreset.equals(SettingsUtil.getCurrentPreset())) {
                SettingsUtil.renamePreset(selectedPreset, renamedPreset);

                SettingsUtil.loadCurrentSettings();
                SettingsUtil.savePreset(renamedPreset); // save current setting on renamed preset

                PresetUtil.loadOnly(renamedPreset);

                PresetList.getInstance().refresh();
            } else {
                SettingsUtil.renamePreset(selectedPreset, renamedPreset);
                PresetList.getInstance().refresh();
            }
        }
    }
}
