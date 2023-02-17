package Main;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

public class DesignAndWork extends JPanel {
    private static final long serialVersionUID = 1L;

    //짧은 시간 동안 딜레이를 주기 위한 라이브러리
    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    //Robot 객체 생성
    Robot r;

    {
        try {
            r = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    //GUI 관련 변수들 - start
    JTextField auto; //지연 설정 입력란
    JButton changeKey; //조작 키 변경 버튼
    JButton help; //도움말 버튼
    JComboBox<String> CBmenu = new JComboBox<>();
    JComboBox<String> CBmenu2 = new JComboBox<>();
    JOptionPane pane = new JOptionPane("키 인식 중입니다...\nESC를 눌러 취소할 수 있습니다.", JOptionPane.INFORMATION_MESSAGE,
            JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
    JDialog dialog = pane.createDialog("키 인식 중...");
    JLabel keycode_l = new JLabel("지정되지 않음"); //GUI 키코드 라벨
    //GUI 관련 변수들 - end

    boolean start = false; //자동 클릭 매크로 작동 여부
    boolean changing = false; //매크로 키 변경 중 여부
    Integer keycode = null; //저장된 키코드

    //매크로 마우스 버튼 3개
    boolean left = false;
    boolean right = false;
    boolean middle = false;

    //GUI 및 GUI 버튼 리스너
    public void design() {
        //디자인 시작
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel AutoPn = new JPanel(new GridLayout(5, 1));

        AutoPn.setBorder(new TitledBorder(new TitledBorder("오토 클릭 지연 설정"), "", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.LEFT));
        JPanel cPn0 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel cPn1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel cPn2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel cPn3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel cPn4 = new JPanel(new FlowLayout(FlowLayout.LEFT));

        String CBmenu_1[] = {"왼쪽", "가운데", "오른쪽"};
        CBmenu = new JComboBox<>(CBmenu_1);
        CBmenu.setPreferredSize(new Dimension(80, 20));

        String CBmenu_2[] = {"밀리초(ms)", "마이크로초(μs)"};
        CBmenu2 = new JComboBox<>(CBmenu_2);
        CBmenu2.setPreferredSize(new Dimension(150, 20));

        auto = new JTextField("100", 5);
        changeKey = new JButton("조작 키 변경");
        help = new JButton("도움말");

        cPn0.add(new JLabel("지연 단위 설정 : "));
        cPn0.add(CBmenu2);

        cPn1.add(new JLabel("오토 클릭 지연 :"));
        cPn1.add(auto);

        cPn2.add(new JLabel("마우스 버튼 :"));
        cPn2.add(CBmenu);

        cPn3.add(new JLabel("조작 키 :"));
        cPn3.add(keycode_l);

        cPn4.add(changeKey);
        cPn4.add(help);

        AutoPn.add(cPn0);
        AutoPn.add(cPn1);
        AutoPn.add(cPn2);
        AutoPn.add(cPn3);
        AutoPn.add(cPn4);
        this.add(AutoPn);
        //디자인 끝
        
        // listener
        ActionListener changeKey = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DesignAndWork.this.changeKey.setEnabled(false);
                changing = true;
                dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                dialog.setVisible(true);
            }
        };
        this.changeKey.addActionListener(changeKey);

        // listener -----------------
        ActionListener help = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "지연 설정, 버튼 설정, 조작 키 설정을 마치고 \"설정한 조작 키\"를 누르면 \n그 즉시 자동 클릭이 시작됩니다.\n\n그리고 설정한 조작키를 다시 누르면 자동클릭이 종료됩니다.",
                        "도움말", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        this.help.addActionListener(help);
    } // design()

    public void start() {
        //유효성 검사 시작
        final String REGEX = "[0-9]+";
        String text = auto.getText();
        if (!text.matches(REGEX)) {
            JOptionPane.showMessageDialog(null, "지연 설정에 숫자만 입력하세요.", "Mouse Delay Setting Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else if (text.startsWith("0")) {
            JOptionPane.showMessageDialog(null, "지연 설정이 0으로 시작해서는 안됩니다.", "Mouse Delay Setting Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        long delay = Long.parseLong(text);
        if (delay >= 5000) {
            JOptionPane.showMessageDialog(null, "지연 설정이 5000보다 작아야 합니다.", "Mouse Delay Setting Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        //유효성 검사 끝

        //초기화 시작
        CBmenu.setEnabled(false);
        CBmenu2.setEnabled(false);
        changeKey.setEnabled(false);
        auto.setEnabled(false);
        start = true;
        left = false;
        middle = false;
        right = false;
        executorService = Executors.newSingleThreadScheduledExecutor();
        //초기화 끝

        String Mouse = CBmenu.getSelectedItem().toString(); //사용자가 선택한 자동클릭 될 마우스 버튼
        String delayAuto = CBmenu2.getSelectedItem().toString(); //사용자가 선택한 지연 단위 설정(ms, micros)

        if (Mouse == "왼쪽")
            left = true;
        else if (Mouse == "가운데")
            middle = true;
        else if (Mouse == "오른쪽")
            right = true;

        //자동 클릭 매크로 시작
        if (delayAuto == "밀리초(ms)")
            executorService.scheduleAtFixedRate(() -> theWorldFastestAutoclickerFuck(), 0, delay,
                    TimeUnit.MILLISECONDS);
        else if (delayAuto == "마이크로초(μs)")
            executorService.scheduleAtFixedRate(() -> theWorldFastestAutoclickerFuck(), 0, delay,
                    TimeUnit.MICROSECONDS);
    } // start()

    //자동 클릭 매크로 실행(메서드 반복)
    public void theWorldFastestAutoclickerFuck() {
        if (left) {
            r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        } else if (middle) {
            r.mousePress(InputEvent.BUTTON2_DOWN_MASK);
            r.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
        } else if (right) {
            r.mousePress(InputEvent.BUTTON3_DOWN_MASK);
            r.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        }
    }
}
