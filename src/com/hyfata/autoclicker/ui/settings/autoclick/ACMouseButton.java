package com.hyfata.autoclicker.ui.settings.autoclick;

import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.utils.SwingUtil;

import javax.swing.*;
import java.util.Objects;

public class ACMouseButton {
    private JComboBox<String> mouseButtons;

    protected JComboBox<String> createComboBox(String selected) {
        String[] menu = {Locale.getMouseLeft(), Locale.getMouseMiddle(), Locale.getMouseRight()};
        mouseButtons = SwingUtil.getStringComboBox(100, 23, menu, selected);
        return mouseButtons;
    }

    public String getSelected() {
        return Objects.requireNonNull(mouseButtons.getSelectedItem()).toString();
    }

    public void setSelected(String selected) {
        mouseButtons.setSelectedItem(selected);
    }

    public void setEnabled(boolean enabled) {
        mouseButtons.setEnabled(enabled);
    }
}
