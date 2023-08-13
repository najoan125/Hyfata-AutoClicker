package Main;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Main extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final String appVersion = "1.2.7";
    static DesignAndWork work = new DesignAndWork();

    public static void main(String[] args) throws URISyntaxException, IOException {
        customFont(); //기본 폰트 변경
        update(); //update

        //register
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
        //register

        work.design();
        //frame
        JFrame frame = new JFrame("HF AutoClick " + appVersion);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image img = toolkit.getImage(Main.class.getResource("/img/HF_AutoClickIcon.png"));
        frame.setIconImage(img);
        frame.getContentPane().add(work);// JFrame+JPanel(화면디자인)
        frame.setBounds(300, 300, 300, 320);// x,y,w,h
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// X버튼 클릭시 종료
        frame.setFocusable(true);
        frame.setResizable(false);
        //frame
    } // main

    private static void update() throws URISyntaxException, IOException {
        String json = "";
        try {
            URL url = new URL("http://132.226.170.151/file/Autoclicker/autoclicker.json");
            json = stream(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (!json.equals("")) {
            JSONObject jObject = new JSONObject(json);
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
    }

    private static String stream(URL url) {
        try (InputStream input = url.openStream()) {
            InputStreamReader isr = new InputStreamReader(input, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder json = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                json.append((char) c);
            }
            return json.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
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

    private static void customFont() {
        Font customFont = null;
        try {
            //create the font to use. Specify the size!
            customFont = Font.createFont(Font.TRUETYPE_FONT,
                            Objects.requireNonNull(Main.class.getResourceAsStream("/font/NotoSansKR-Regular.otf")))
                    .deriveFont(14f)
                    .deriveFont(Font.BOLD);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        UIManager.put("Button.font", customFont);
        UIManager.put("ToggleButton.font", customFont);
        UIManager.put("RadioButton.font", customFont);
        UIManager.put("CheckBox.font", customFont);
        UIManager.put("ColorChooser.font", customFont);
        UIManager.put("ComboBox.font", customFont);
        UIManager.put("Label.font", customFont);
        UIManager.put("List.font", customFont);
        UIManager.put("MenuBar.font", customFont);
        UIManager.put("MenuItem.font", customFont);
        UIManager.put("RadioButtonMenuItem.font", customFont);
        UIManager.put("CheckBoxMenuItem.font", customFont);
        UIManager.put("Menu.font", customFont);
        UIManager.put("PopupMenu.font", customFont);
        UIManager.put("OptionPane.font", customFont);
        UIManager.put("Panel.font", customFont);
        UIManager.put("ProgressBar.font", customFont);
        UIManager.put("ScrollPane.font", customFont);
        UIManager.put("Viewport.font", customFont);
        UIManager.put("TabbedPane.font", customFont);
        UIManager.put("Table.font", customFont);
        UIManager.put("TableHeader.font", customFont);
        UIManager.put("TextField.font", customFont);
        UIManager.put("PasswordField.font", customFont);
        UIManager.put("TextArea.font", customFont);
        UIManager.put("TextPane.font", customFont);
        UIManager.put("EditorPane.font", customFont);
        UIManager.put("TitledBorder.font", customFont);
        UIManager.put("ToolBar.font", customFont);
        UIManager.put("ToolTip.font", customFont);
        UIManager.put("Tree.font", customFont);
    }

} // Main
