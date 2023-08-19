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
        JPanel panel = new JPanel(null);
        initPanels();

        int y = 10;
        int height = 35;

        int i = 0;
        for (JPanel p : panels) {
            p.setLayout(new FlowLayout(FlowLayout.CENTER));
            p.setBounds(0,y, Design.WIDTH,height);
            if (addedHeights.containsKey(i)){
                p.setBounds(0,y, Design.WIDTH,height+addedHeights.get(i));
                y += addedHeights.get(i);
            }
            panel.add(p);

            y+=height;
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
        panels.add(panel);
    }

    private void okButton() {
        JPanel panel = new JPanel();
        OK = new JButton("OK");

        OK.addActionListener(e -> {
            String language = list.getSelectedValue();
            if (language.equals("한국어")) {
                SettingsUtil.setLang("ko.json");
                setLanguage();
            }
            else if (language.equals("English")) {
                SettingsUtil.setLang("en.json");
                setLanguage();
            }
        });
        panel.add(OK);
        addHeight(50);
        panels.add(panel);
    }

    private void setLanguage() {
        try {
            SettingsUtil.loadCurrentSettings();
            SettingsUtil.savePreset(SettingsUtil.getCurrentPreset());
            SettingsUtil.saveFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            Locale.setLocale(SettingsUtil.getLang());
        } catch (IOException | JsonEmptyException ex) {
            JOptionPane.showMessageDialog(null, "언어 파일을 등록하는 과정에서 오류가 발생했습니다!\n제작자에게 디스코드로 문의해주세요!\nDiscord Tag: Najoan#0135\n\n" +
                            ex.getMessage(),
                    "오류 발생", JOptionPane.INFORMATION_MESSAGE);
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
