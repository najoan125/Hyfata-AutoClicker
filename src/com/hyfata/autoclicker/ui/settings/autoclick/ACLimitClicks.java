package com.hyfata.autoclicker.ui.settings.autoclick;

import com.hyfata.autoclicker.utils.SwingUtil;

import javax.swing.*;
import java.awt.*;

public class ACLimitClicks {
    private JFormattedTextField limit;

    protected JFormattedTextField createTextField(int defaultLimit) {
        limit = SwingUtil.getIntTextField();
        limit.setPreferredSize(new Dimension(80, 23));
        limit.setValue(defaultLimit);
        return limit;
    }

    protected void setAllEnabled(boolean enabled) {
        limit.setEnabled(enabled);
    }

    public void setLimit(int limit) {
        this.limit.setValue(limit);
    }

    public String getLimit() {
        return limit.getValue().toString();
    }
}
