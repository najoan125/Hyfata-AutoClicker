package com.hyfata.autoclicker.ui.settings.preset;

import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.ui.settings.AutoClickSettingsUI;
import com.hyfata.autoclicker.utils.settings.SettingsUtil;

public class PresetUtils {
    public static void setAllEnabled(boolean bool) {
        PresetUI.presetList.setEnabled(bool);
        PresetUI.OK.setEnabled(bool);
        PresetUI.plus.setEnabled(bool);
        PresetUI.delete.setEnabled(bool);
        PresetUI.rename.setEnabled(bool);
    }

    protected static int getPresetsWidth() {
        int width = PresetUI.presetList.getPreferredSize().width;
        if (width >= 400) width = 400;
        return Math.max(width, 140);
    }

    protected static void loadPreset(String preset) {
        SettingsUtil.loadCurrentSettings();
        SettingsUtil.savePreset(SettingsUtil.getCurrentPreset());
        SettingsUtil.setCurrentPreset(preset);
        PresetUI.currentPresetLabel.setText(Locale.getCurrentPreset() + SettingsUtil.getCurrentPreset());
        SettingsUtil.loadPreset(preset);
        AutoClickSettingsUI.reload();
    }
}
