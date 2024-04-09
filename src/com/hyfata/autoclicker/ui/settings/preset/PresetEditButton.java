package com.hyfata.autoclicker.ui.settings.preset;

import com.hyfata.autoclicker.locale.Locale;

import javax.swing.*;
import java.awt.*;

public class PresetEditButton {
    protected static JButton newPlusButton() {
        JButton plus = new JButton("+");
        plus.setPreferredSize(new Dimension(25, 25));
        plus.setFont(plus.getFont().deriveFont(20.0f));
        plus.setMargin(new Insets(0,0,5,0));
        plus.addActionListener(_ -> PresetButtonListener.plusListener());
        return plus;
    }

    protected static JButton newDeleteButton() {
        JButton delete = new JButton("-");
        delete.setPreferredSize(new Dimension(25, 25));
        delete.setFont(delete.getFont().deriveFont(20.0f));
        delete.setMargin(new Insets(0, 0, 7, 0));
        delete.addActionListener(_ -> PresetButtonListener.deleteListener());
        return delete;
    }

    protected static JButton newRenameButton() {
        JButton rename = new JButton(Locale.getRename());
        rename.addActionListener(_ -> PresetButtonListener.renameListener());
        return rename;
    }
}
