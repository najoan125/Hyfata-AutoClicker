package com.hyfata.autoclicker.utils;

import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.ui.settings.autoclick.AutoClickSettingsUI;
import com.hyfata.autoclicker.ui.settings.preset.PresetUI;
import com.hyfata.autoclicker.utils.settings.SettingsUtil;

public class PresetUtil {
    public static void saveAndLoad(String preset) {
        SettingsUtil.loadCurrentSettings();
        SettingsUtil.savePreset(SettingsUtil.getCurrentPreset());
        loadOnly(preset);
    }

    public static void loadOnly(String preset) {
        SettingsUtil.setCurrentPreset(preset);
        PresetUI.getInstance().setCurrentPresetText(Locale.getCurrentPreset() + SettingsUtil.getCurrentPreset());
        SettingsUtil.loadPreset(preset);
        AutoClickSettingsUI.reload();
    }
}
