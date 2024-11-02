package com.hyfata.autoclicker.ui;

import com.hyfata.autoclicker.AutoClicker;
import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.ui.settings.autoclick.AutoClickSettingsUI;
import com.hyfata.autoclicker.ui.settings.LanguageUI;
import com.hyfata.autoclicker.ui.settings.preset.PresetUI;
import com.hyfata.autoclicker.utils.DialogUtil;
import com.hyfata.autoclicker.utils.UpdateUtil;
import com.hyfata.autoclicker.utils.settings.SettingsUtil;
import com.hyfata.json.exceptions.JsonEmptyException;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;

public class Design extends JFrame {
    public static final int WIDTH = 450, HEIGHT = 340;
    private static Component aboutPanel = null;

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
                    DialogUtil.showErrorDialog(ex, Locale.getSavingSettingsError(), "Error saving settings");
                }
                System.exit(0);
            }
        });
    }

    public Design(String title) {
        init(title);
        design();
        setVisible(true);
        UpdateUtil updateUtil;
        try {
            updateUtil = new UpdateUtil();
            updateUtil.showUpdateDialog();
        } catch (JsonEmptyException | IOException | URISyntaxException ignored) {
        }
    }

    public static JPanel getScrollablePanel(Component view) {
        JScrollPane scrollPane = new JScrollPane(view);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        JPanel scrollablePanel = new JPanel(new BorderLayout());
        scrollablePanel.add(scrollPane, BorderLayout.CENTER);
        return scrollablePanel;
    }

    private void design() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // 1
        AutoClickSettingsUI autoClickSettingsUI = new AutoClickSettingsUI();
        tabbedPane.addTab(Locale.getAutoClickSetting(), autoClickSettingsUI.getPanel());

        // 2
        PresetUI presetUI = new PresetUI();
        tabbedPane.addTab(Locale.getPresetSetting(), presetUI.getPanel());

        // 3
        LanguageUI languageUI = new LanguageUI();
        tabbedPane.addTab(Locale.getLanguage(), languageUI.getPanel());

        // 4
        JPanel tab4 = new JPanel();
        tab4.add(new JLabel(Locale.getHelpDesc()));
        tabbedPane.addTab(Locale.getHelp(), tab4);

        // 5
        if (aboutPanel == null) {
            aboutPanel = getAboutPanel();
        }
        tabbedPane.addTab(Locale.getAbout(), aboutPanel);

        //listener
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            Component selectedComponent = tabbedPane.getComponentAt(selectedIndex);
            if (selectedComponent != null) {
                selectedComponent.requestFocusInWindow();
            }
        });

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
                        "<a href=\"https://github.com/sponsors/najoan125\">Github Sponsors</a>" +
                        "<br>" +
                        "<br>" +
                        "<h2>Open Source License</h2>" +
                        "<pre>" +
                        "* FlatLaf <a href=\"https://www.formdev.com/flatlaf\">https://www.formdev.com/flatlaf</a>\n\tApache License 2.0\n\n" +
                        "* FlatLaf Arc Theme <a href=\"https://gitlab.com/zlamalp/arc-theme-idea/blob/master/arc-theme-idea-dark/resources/arc_theme_dark.theme.json\">https://gitlab.com/zlamalp/arc-theme-idea/blob/master/arc-theme-idea-dark/resources/arc_theme_dark.theme.json</a>\n\tMIT License\n\n" +
                        "* JNativeHook <a href=\"https://github.com/kwhat/jnativehook\">https://github.com/kwhat/jnativehook</a>\n\tLGPL\n\n" +
                        "* org.json <a href=\"https://mvnrepository.com/artifact/org.json/json\">https://mvnrepository.com/artifact/org.json/json</a>\n\tPublic\n\n" +
                        "* JsonUtility <a href=\"https://github.com/najoan125/JsonUtility\">https://github.com/najoan125/JsonUtility</a>\n\tMIT License" +
                        "</pre>" +
                        "</html>";
        JEditorPane editorPane = new JEditorPane("text/html", content);
        editorPane.setEditable(false);

        // 하이퍼링크를 클릭했을 때의 동작을 정의하는 리스너 추가
        editorPane.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (IOException | URISyntaxException ignored) {}
                }
            }
        });

        panel.add(editorPane);
        return getScrollablePanel(panel);
    }
}
