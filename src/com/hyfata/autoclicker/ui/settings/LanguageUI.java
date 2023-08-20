package com.hyfata.autoclicker.ui.settings;

import com.hyfata.autoclicker.AutoClicker;
import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.ui.Design;
import com.hyfata.autoclicker.utils.settings.SettingsUtil;
import com.hyfata.json.exceptions.JsonEmptyException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class LanguageUI {
    static JList<String> list;
    static JButton OK;
    ArrayList<JPanel> panels = new ArrayList<>();
    HashMap<Integer,Integer> addedHeights = new HashMap<>(); //index, height
    public JPanel getPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        initPanels();

        int height = 35;

        int i = 0;
        for (JPanel p : panels) {
            p.setLayout(new FlowLayout(FlowLayout.CENTER));
            p.setPreferredSize(new Dimension(Design.WIDTH, height));
            if (addedHeights.containsKey(i)){
                p.setPreferredSize(new Dimension(Design.WIDTH,height+addedHeights.get(i)));
            }
            panel.add(p);

            i++;
        }
        return panel;
    }

    private void initPanels() {
        selectLanguageLabel();
        languageList();
        okButton();
    }

    private void selectLanguageLabel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Select Language:"));
        panels.add(panel);
    }

    private void languageList() {
        JPanel panel = new JPanel();
        list = new JList<>(new String[]{"한국어","English"});
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(list);
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.getViewport().setPreferredSize(new Dimension(140, 90));
        JPanel scrollPanel = Design.getScrollablePanel(scrollPane);
        panels.add(scrollPanel);
    }

    private void okButton() {
        JPanel panel = new JPanel();
        OK = new JButton("OK");

        OK.addActionListener(e -> {
            String language = list.getSelectedValue();
            if (language != null) {
                if (language.equals("한국어")) {
                    SettingsUtil.setLang("ko.json");
                } else if (language.equals("English")) {
                    SettingsUtil.setLang("en.json");
                }
                setLanguage();
            }
        });
        panel.add(OK);
        addHeight(80);
        panels.add(panel);
    }

    private void setLanguage() {
        SettingsUtil.loadCurrentSettings();
        SettingsUtil.savePreset(SettingsUtil.getCurrentPreset());
        try {
            Locale.setLocale(SettingsUtil.getLang());
        } catch (IOException | JsonEmptyException ex) {
            AutoClicker.showErrorDialog("Error loading language file. Contact to developer on discord!\nDiscord Tag: "+AutoClicker.DISCORD_TAG+"\n\n"+ex.getMessage(), "Error loading language file");
            System.exit(-1);
        }
        AutoClicker.reload();
    }

    public static void setAllEnabled(boolean bool) {
        list.setEnabled(bool);
        OK.setEnabled(bool);
    }

    private void addHeight(int height) {
        addedHeights.put(panels.size()-1, height);
    }
}
