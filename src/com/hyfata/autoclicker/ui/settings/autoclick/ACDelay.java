package com.hyfata.autoclicker.ui.settings.autoclick;

import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.utils.SwingUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ACDelay {
    private static ACDelay instance;
    private JFormattedTextField delay;
    private JComboBox<String> delayUnits;

    public ACDelay() {
        instance = this;
    }

    public static ACDelay getInstance() {
        return instance;
    }

    public void setDelay(int delay) {
        this.delay.setValue(delay);
    }

    public String getDelay() {
        return delay.getValue().toString();
    }

    public void setUnit(String unit) {
        this.delayUnits.setSelectedItem(unit);
    }

    public String getSelectedUnit() {
        return Objects.requireNonNull(delayUnits.getSelectedItem()).toString();
    }

    protected JFormattedTextField getTextField(int defaultDelay) {
        delay = SwingUtil.getIntTextField();
        delay.setPreferredSize(new Dimension(80, 23));
        delay.setValue(defaultDelay);
        return delay;
    }

    protected JComboBox<String> getUnitMenu(String unit) {
        String[] menu = {Locale.getDelayMs(), Locale.getDelayMicros()};
        delayUnits = SwingUtil.getStringComboBox(160, 23, menu, unit);
        return delayUnits;
    }

    protected void setAllEnabled(boolean enabled) {
        delay.setEnabled(enabled);
        delayUnits.setEnabled(enabled);
    }
}
