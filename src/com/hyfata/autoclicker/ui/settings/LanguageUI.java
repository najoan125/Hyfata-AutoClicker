package com.hyfata.autoclicker.ui.settings;

import com.hyfata.autoclicker.AutoClicker;
import com.hyfata.autoclicker.locale.Languages;
import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.utils.SwingUtil;
import com.hyfata.autoclicker.utils.JPanelUtil;
import com.hyfata.autoclicker.utils.settings.SettingsUtil;
import com.hyfata.json.exceptions.JsonEmptyException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LanguageUI {
    private static LanguageUI instance;
    private JList<String> list;
    private JButton OK;
    private final JPanelUtil panelUtil = new JPanelUtil();
    private final Languages languages = new Languages();

    public LanguageUI() {
        instance = this;
    }

    public static LanguageUI getInstance() {
        if (instance == null) {
            instance = new LanguageUI();
        }
        return instance;
    }

    public JPanel getPanel() {
        initPanels();
        return panelUtil.createPanel(FlowLayout.CENTER);
    }

    private void initPanels() {
        selectLanguageLabel();
        languageList();
        okButton();
    }

    private void selectLanguageLabel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Select Language:"));
        panelUtil.register(panel);
    }

    private void languageList() {
        list = new JList<>(languages.getLanguageNames());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.getViewport().setPreferredSize(new Dimension(140, 120));

        JPanel scrollPanel = SwingUtil.getScrollablePanel(scrollPane);
        panelUtil.register(scrollPanel);
    }

    private void okButton() {
        JPanel panel = new JPanel();
        OK = new JButton("OK");
        OK.addActionListener(e -> okButtonAction());

        panel.add(OK);
        panelUtil.addHeight(110);
        panelUtil.register(panel);
    }

    private void okButtonAction() {
        String language = list.getSelectedValue();
        if (language == null) {
            return;
        }

        for (String name : languages.getLanguages().keySet()) {
            if (name.equals(language)) {
                SettingsUtil.setLang(languages.getLanguages().get(name));
                break;
            }
        }
        setLanguage();
    }

    private void setLanguage() {
        SettingsUtil.loadCurrentSettings();
        SettingsUtil.savePreset(SettingsUtil.getCurrentPreset());
        try {
            Locale.setLocale(SettingsUtil.getLang());
        } catch (IOException | JsonEmptyException ex) {
            SwingUtil.showErrorDialog("Error loading language file. Contact to developer on discord!\nDiscord Tag: " + AutoClicker.DISCORD_TAG + "\n\n" + ex.getMessage(), "Error loading language file");
            System.exit(-1);
        }
        AutoClicker.reload();
    }

    public void setAllEnabled(boolean bool) {
        list.setEnabled(bool);
        OK.setEnabled(bool);
    }
}
