package com.hyfata.autoclicker;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.hyfata.autoclicker.utils.OSValidator;
import com.hyfata.json.JsonReader;
import com.hyfata.json.exceptions.JsonEmptyException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Main extends JPanel {
    private static final String appVersion = "2.0.0";
    static DesignAndWork work = new DesignAndWork();

    public static void main(String[] args){
        try {
            update(); //update
        } catch (JsonEmptyException | URISyntaxException | IOException ignored) {}
        registerNativeHook();

        work.design();
        //frame
        JFrame frame = new JFrame("HF AutoClick " + appVersion);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image img = toolkit.getImage(Main.class.getResource("img/HF_AutoClickIcon.png"));
        frame.setIconImage(img);
        frame.getContentPane().add(work);// JFrame+JPanel(화면디자인)
        frame.setBounds(300, 300, 300, 320);// x,y,w,h
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// X버튼 클릭시 종료
        frame.setFocusable(true);
        frame.setResizable(false);
        //frame
    } // main

    private static void registerNativeHook() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            JOptionPane.showMessageDialog(null, "NativeHook를 등록하는 과정에서 오류가 발생했습니다!\n제작자에게 디스코드로 문의해주세요!\nDiscord Tag: Najoan#0135",
                    "오류 발생", JOptionPane.INFORMATION_MESSAGE);
            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(new GlobalKeyListener());
        GlobalScreen.addNativeMouseListener(new GlobalKeyListener());
    }

    private static void update() throws URISyntaxException, IOException, JsonEmptyException {
        JSONObject jObject = JsonReader.readFromURL("http://132.226.170.151/file/Autoclicker/autoclicker.json");
        String latest = jObject.getString("latest-version");
        String desc = jObject.getString("description");

        if (!latest.equals(appVersion)) {
            if (OSValidator.isWindows()) {
                int answer = JOptionPane.showConfirmDialog(null, "업데이트가 발견되었습니다! 업데이트 하시겠습니까?\n\n현재 버전: " + appVersion + ", 새로운 버전: " + latest
                                + "\n변경된 내용: " + desc,
                        "업데이트 발견!",
                        JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION) {
                    downloadUpdate(latest);
                    System.exit(1);
                }
            } else {
                int answer = JOptionPane.showConfirmDialog(null, "업데이트가 발견되었습니다! 최신 버전을 다운받으시겠습니까?\n\n현재 버전: " + appVersion + ", 새로운 버전: " + latest
                                + "\n변경된 내용: " + desc,
                        "업데이트 발견!",
                        JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION) {
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        Desktop.getDesktop().browse(new URI("https://github.com/najoan125/Hyfata-AutoClicker/releases/download/" + latest + "/AutoClicker.jar"));
                    } else
                        JOptionPane.showMessageDialog(null, "아쉽게도 이 장치는 지원되지 않습니다. 업데이트를 직접 설치해주세요.",
                                "오류 발생", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(1);
                }
            }
        }
    }

    private static void downloadUpdate(String version) {
        String home = System.getProperty("user.home");
        String file = home + "/Downloads/" + "Hyfata.AutoClick." + version + ".exe";

        String addr = "https://github.com/najoan125/Hyfata-AutoClicker/releases/download/" + version + "/Hyfata.AutoClick.exe";
        try {
            URL url = new URL(addr);
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(file);

            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();

            Runtime.getRuntime().exec(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

} // Main
