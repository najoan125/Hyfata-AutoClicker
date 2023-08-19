package com.hyfata.autoclicker.ui;

import com.hyfata.autoclicker.AutoClicker;
import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.ui.settings.AutoClickSettingsUI;
import com.hyfata.autoclicker.ui.settings.LanguageUI;
import com.hyfata.autoclicker.utils.settings.SettingsUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class Design extends JFrame {
    public static final int WIDTH = 450, HEIGHT = 320;
    private void init(String title) {
        setTitle(title);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image img = toolkit.getImage(AutoClicker.class.getResource("img/HF_AutoClickIcon.png"));
        setIconImage(img);

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(true);
        setResizable(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    SettingsUtil.loadCurrentSettings();
                    SettingsUtil.savePreset(SettingsUtil.getCurrentPreset());
                    SettingsUtil.saveFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
            }
        });
    }

    public Design(String title) {
        init(title);
        design();
        setVisible(true);
    }

    public static JPanel getScrollablePanel(Component view) {
        JScrollPane scrollPane = new JScrollPane(view);
        JPanel scrollablePanel = new JPanel(new BorderLayout());
        scrollablePanel.add(scrollPane, BorderLayout.CENTER);
        return scrollablePanel;
    }

    private void design() {
        JTabbedPane tabbedPane = new JTabbedPane();

        AutoClickSettingsUI autoClickSettingsUI = new AutoClickSettingsUI();
        JPanel tab1 = autoClickSettingsUI.getPanel();

        JPanel tab2 = new JPanel();
        tab2.add(new JLabel("준비 중인 기능입니다."));

        LanguageUI languageUI = new LanguageUI();
        JPanel tab3 = languageUI.getPanel();

        JPanel tab4 = new JPanel();
        tab4.add(new JLabel("Content for Tab 3"));

        JPanel tab5 = getAboutPanel();

        tabbedPane.addTab(Locale.getAutoClickSetting(), tab1);
        tabbedPane.addTab(Locale.getPresetSetting(), tab2);
        tabbedPane.addTab(Locale.getLanguage(), tab3);
        tabbedPane.addTab(Locale.getHelp(), tab4);
        tabbedPane.addTab(Locale.getAbout(), tab5);
        add(tabbedPane);
    }

    private JPanel getAboutPanel() {
        JPanel panel = new JPanel();
        String content =
                "<html>" +
                        "<h1>Hyfata AutoClicker v" + AutoClicker.APP_VERSION + "</h1>" +
                        "Licence: GNU Lesser General Public License v3.0" +
                        "<br>" +
                        "Developer: Najoan" +
                        "<br>" +
                        "후원계좌: SC제일은행 47116134176192" +
                        "<br>" +
                        "<br>" +
                        "<h2>Open Source License</h2>" +
                        "<pre>" +
                        "* FlatLaf https://www.formdev.com/flatlaf/\n\tApache License 2.0\n\n" +
                        "* FlatLaf Arc Theme https://gitlab.com/zlamalp/arc-theme-idea/blob/master/arc-theme-idea-dark/resources/arc_theme_dark.theme.json\n\tMIT License\n\n" +
                        "* JNativeHook https://github.com/kwhat/jnativehook\n\tLGPL\n\n" +
                        "* org.json https://mvnrepository.com/artifact/org.json/json\n\tPublic\n\n" +
                        "* JsonUtility https://github.com/najoan125/JsonUtility\n\tMIT License" +
                        "</pre>" +
                        "</html>";
        panel.add(new JLabel(content));
        return getScrollablePanel(panel);
    }
}