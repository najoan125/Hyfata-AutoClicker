package Main;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import lc.kra.system.keyboard.event.GlobalKeyListener;
import lc.kra.system.mouse.GlobalMouseHook;
import lc.kra.system.mouse.event.GlobalMouseAdapter;
import lc.kra.system.mouse.event.GlobalMouseEvent;

public class Main extends JPanel {
	private static final long serialVersionUID = 1L;
	DesignAndWork work = new DesignAndWork();
	boolean keyboard = true;

	public Main() {
		work.design();
	}

	public static void main(String[] args) {
		Main macro = new Main();
		//frame
		JFrame frame = new JFrame("HF AutoClick 1.1.1");
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image img = toolkit.getImage(Main.class.getResource("/img/HF_AutoClickIcon.png"));
		frame.setIconImage(img);
		frame.getContentPane().add(macro.work);// JFrame+JPanel(화면디자인)
		frame.setBounds(100, 300, 300, 255);// x,y,w,h
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
				if (macro.work.keycode != null && key == macro.work.keycode && !macro.work.changing && macro.keyboard) {
					if (macro.work.start) {
						macro.work.start = false;

						macro.work.changeKey.setEnabled(true);
						macro.work.auto.setEnabled(true);
						macro.work.CBmenu.setEnabled(true);
						macro.work.CBmenu2.setEnabled(true);
						macro.work.executorService.shutdown();
					} else {
						macro.work.start();
					}
				}

				if (macro.work.changing && key != 27 && key != 32) {
					macro.work.keycode = key;
					macro.work.changing = false;
					macro.work.changeKey.setEnabled(true);
					macro.work.keycode_l.setText(arg0.getKeyChar() + " (키코드: " + key + ")");
					macro.keyboard = true;
					macro.work.dialog.setVisible(false);
				}

				if (macro.work.changing && (key == 27 || key == 32)) {
					macro.work.changing = false;
					macro.work.changeKey.setEnabled(true);
					macro.work.dialog.setVisible(false);
				}
			} // KeyPressed

			@Override
			public void keyReleased(GlobalKeyEvent arg0) {

			} // KeyReleaed
		}); // addkeylistener
		
		GlobalMouseHook mouseHook = new GlobalMouseHook();
		mouseHook.addMouseListener(new GlobalMouseAdapter() {
			@Override
			public void mousePressed(GlobalMouseEvent arg0) {
				int key = arg0.getButton();
				if (macro.work.keycode != null && key == macro.work.keycode && !macro.work.changing && !macro.keyboard) {
					if (macro.work.start) {
						macro.work.start = false;

						macro.work.changeKey.setEnabled(true);
						macro.work.auto.setEnabled(true);
						macro.work.CBmenu.setEnabled(true);
						macro.work.CBmenu2.setEnabled(true);
						macro.work.executorService.shutdown();
					} else {
						macro.work.start();
					}
				}

				if (macro.work.changing && key != 1) {
					macro.work.keycode = key;
					macro.work.changing = false;
					macro.work.changeKey.setEnabled(true);
					macro.work.keycode_l.setText(" (마우스 버튼 코드: " + key + ")");
					macro.keyboard = false;
					macro.work.dialog.setVisible(false);
				}

				if (macro.work.changing && key == 1) {
					macro.work.changing = false;
					macro.work.changeKey.setEnabled(true);
					macro.work.dialog.setVisible(false);
				}
			} // mousePressed()
			
			@Override
			public void mouseReleased(GlobalMouseEvent arg0) {
				
			} //mouseReleased()
		}); //mouseListener
	} // main
} // Main
