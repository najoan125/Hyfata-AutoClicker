package com.hyfata.autoclicker.ui.settings.autoclick;

import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.utils.SwingUtil;

import javax.swing.*;
import java.util.Objects;

public class ACHoldToggle {
    private JComboBox<String> holdToggles;

    public void setSelected(String hold) {
        holdToggles.setSelectedItem(hold);
    }

    public String getSelected() {
        return Objects.requireNonNull(holdToggles.getSelectedItem()).toString();
    }

    protected JComboBox<String> createComboBox(String selected) {
        String[] menu = {Locale.getKeyHold(), Locale.getKeyToggle()};
        holdToggles = SwingUtil.getStringComboBox(100, 23, menu, selected);
        return holdToggles;
    }

    public void setEnabled(boolean enabled) {
        holdToggles.setEnabled(enabled);
    }
}
