package com.hyfata.autoclicker;

import javax.swing.*;
import java.awt.*;

public class Design extends JFrame {
    public Design(String title) {
        setTitle(title);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image img = toolkit.getImage(AutoClicker.class.getResource("img/HF_AutoClickIcon.png"));
        setIconImage(img);

        setSize(450, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(true);
        setResizable(false);
        design();
        setVisible(true);
    }

    private JPanel getScrollablePanel(Component view) {
        JScrollPane scrollPane = new JScrollPane(view);
        JPanel scrollablePanel = new JPanel(new BorderLayout());
        scrollablePanel.add(scrollPane, BorderLayout.CENTER);
        return scrollablePanel;
    }


    private void design() {
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel tab1 = new JPanel();
        tab1.add(new JLabel("Content for Tab 1"));

        JPanel tab2 = new JPanel();
        tab2.add(new JLabel("Content for Tab 2"));

        JPanel tab3 = about();

        tabbedPane.addTab("오토클릭 설정", tab1);
        tabbedPane.addTab("프리셋 설정", tab2);
        tabbedPane.addTab("정보", tab3);
        add(tabbedPane);
    }

    private JPanel about() {
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
                        "<h2>오픈소스 라이선스</h2>" +
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
