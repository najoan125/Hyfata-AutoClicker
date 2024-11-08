package com.hyfata.autoclicker.utils;

import com.hyfata.autoclicker.ui.UIController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class JPanelUtil {
    private final ArrayList<JPanel> panels = new ArrayList<>();
    private final HashMap<Integer,Integer> addedHeights = new HashMap<>(); //index, height

    public JPanel createPanel(int flowLayout) {
        return createPanel(flowLayout, 30);
    }

    public JPanel createPanel(int flowLayout, int height) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        int i = 0;
        for (JPanel p : panels) {
            p.setLayout(new FlowLayout(flowLayout));
            p.setPreferredSize(new Dimension(UIController.WIDTH, height));
            if (addedHeights.containsKey(i)){
                p.setPreferredSize(new Dimension(UIController.WIDTH,height+addedHeights.get(i)));
            }
            panel.add(p);

            i++;
        }
        return panel;
    }

    public void register(JPanel panel) {
        panels.add(panel);
    }

    public void addHeight(int height) {
        addedHeights.put(panels.size()-1, height);
    }
}
