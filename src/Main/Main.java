package Main;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import lc.kra.system.keyboard.event.GlobalKeyListener;
import lc.kra.system.mouse.GlobalMouseHook;
import lc.kra.system.mouse.event.GlobalMouseAdapter;
import lc.kra.system.mouse.event.GlobalMouseEvent;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;

public class Main extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final String appVersion = "1.2.1";
	DesignAndWork work = new DesignAndWork();
	boolean keyboard = true;

	boolean isPressed = false;

	public Main() {
		work.design();
	}


	public static String stream(URL url) {
		try (InputStream input = url.openStream()) {
			InputStreamReader isr = new InputStreamReader(input);
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

	public static void downloadUpdate(String version){
		String home = System.getProperty("user.home");
		String file = home+"/Downloads/" + "Hyfata.AutoClick "+version+".exe";

		String addr = "https://github.com/najoan125/Hyfata-AutoClicker/releases/download/"+version+"/Hyfata.AutoClick.exe";
		try{
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

	public static void main(String[] args) {
		//update - start
		String json = "";
		try{
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
				int answer = JOptionPane.showConfirmDialog(null, "업데이트가 발견되었습니다! 업데이트 하시겠습니까?\n\n현재 버전: " + appVersion + ", 새로운 버전: " + latest
								+ "\n변경된 내용: " + desc,
						"업데이트 발견!",
						JOptionPane.YES_NO_OPTION);
				if (answer == JOptionPane.YES_OPTION) {
					downloadUpdate(latest);
					return;
				}
			}
		}
		//update - end

		Main macro = new Main();
		//frame
		JFrame frame = new JFrame("HF AutoClick "+appVersion);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image img = toolkit.getImage(Main.class.getResource("/img/HF_AutoClickIcon.png"));
		frame.setIconImage(img);
		frame.getContentPane().add(macro.work);// JFrame+JPanel(화면디자인)
		frame.setBounds(300, 300, 300, 300);// x,y,w,h
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// X버튼 클릭시 종료
		frame.setFocusable(true);
		frame.setResizable(false);
		//frame
		
		GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook();
		keyboardHook.addKeyListener(new GlobalKeyListener() {
			@Override
			public void keyPressed(GlobalKeyEvent arg0) {
				int key = arg0.getVirtualKeyCode();
				String pt = Objects.requireNonNull(macro.work.CBmenu3.getSelectedItem()).toString();
				if (Objects.equals(pt, "누르기") && !macro.isPressed && macro.work.keycode != null && key == macro.work.keycode && !macro.work.changing && macro.keyboard) {
					startOrStop(macro);
					macro.isPressed = true;
				}
			} // KeyPressed

			@Override
			public void keyReleased(GlobalKeyEvent arg0) {
				int key = arg0.getVirtualKeyCode();
				if (macro.work.keycode != null && key == macro.work.keycode && !macro.work.changing && macro.keyboard) {
					startOrStop(macro);
					macro.isPressed = false;
				}

				if (macro.work.changing && key != 27 && key != 32) {
					changeKeyCode(macro, key, arg0.getKeyChar(), "키코드", true);
				}

				if (macro.work.changing && (key == 27 || key == 32)) {
					cancelChangeKeyCode(macro);
				}
			} // KeyReleaed
		}); // addkeylistener
		
		GlobalMouseHook mouseHook = new GlobalMouseHook();
		mouseHook.addMouseListener(new GlobalMouseAdapter() {
			@Override
			public void mousePressed(GlobalMouseEvent arg0) {
				int key = arg0.getButton();
				String pt = Objects.requireNonNull(macro.work.CBmenu3.getSelectedItem()).toString();
				if (Objects.equals(pt, "누르기") && macro.work.keycode != null && key == macro.work.keycode && !macro.work.changing && !macro.keyboard) {
					startOrStop(macro);
				}
			} // mousePressed()
			
			@Override
			public void mouseReleased(GlobalMouseEvent arg0) {
				int key = arg0.getButton();
				if (macro.work.keycode != null && key == macro.work.keycode && !macro.work.changing && !macro.keyboard) {
					startOrStop(macro);
				}

				if (macro.work.changing && key != 1) {
					changeKeyCode(macro, key, ' ',"마우스 버튼 코드", false);
				}

				if (macro.work.changing && key == 1) {
					cancelChangeKeyCode(macro);
				}
			} //mouseReleased()
		}); //mouseListener
	} // main

	private static void startOrStop(Main macro) {
		if (macro.work.start) {
			macro.work.start = false;
			macro.work.changeKey.setEnabled(true);
			macro.work.auto.setEnabled(true);
			macro.work.help.setEnabled(true);
			macro.work.CBmenu.setEnabled(true);
			macro.work.CBmenu2.setEnabled(true);
			macro.work.CBmenu3.setEnabled(true);
			macro.work.executorService.shutdown();
		} else {
			macro.work.start();
		}
	}

	private static void changeKeyCode(Main macro, int keycode, char key, String label, boolean keyboard) {
		macro.work.keycode = keycode;
		macro.work.changing = false;
		macro.work.changeKey.setEnabled(true);
		macro.work.keycode_l.setText(key + " (" + label+": " + keycode + ")");
		macro.keyboard = keyboard;
		macro.work.dialog.setVisible(false);
	}

	private static void cancelChangeKeyCode(Main macro){
		macro.work.changing = false;
		macro.work.changeKey.setEnabled(true);
		macro.work.dialog.setVisible(false);
	}
} // Main
