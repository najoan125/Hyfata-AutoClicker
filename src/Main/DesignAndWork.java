package Main;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    JComboBox<String> CBmenu3 = new JComboBox<>();
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

        JPanel AutoPn = new JPanel(new GridLayout(6, 1));

        AutoPn.setBorder(new TitledBorder(new TitledBorder("오토 클릭 지연 설정"), "", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.ABOVE_TOP));
        JPanel cPn0 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel cPn1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel cPn2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel cPn3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel cPn4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel cPn5 = new JPanel(new FlowLayout(FlowLayout.LEFT));

        String[] CBmenu_1 = {"왼쪽", "가운데", "오른쪽"};
        CBmenu = new JComboBox<>(CBmenu_1);
        CBmenu.setPreferredSize(new Dimension(80, 20));

        String[] CBmenu_2 = {"밀리초(ms)", "마이크로초(μs)"};
        CBmenu2 = new JComboBox<>(CBmenu_2);
        CBmenu2.setPreferredSize(new Dimension(150, 20));

        String[] CBmenu_3 = {"누르기", "토글"};
        CBmenu3 = new JComboBox<>(CBmenu_3);
        CBmenu3.setPreferredSize(new Dimension(80, 20));

        auto = new JTextField("100", 5);
        changeKey = new JButton("조작 키 변경");
        help = new JButton("도움말");

        cPn0.add(new JLabel("지연 단위 설정 : "));
        cPn0.add(CBmenu2);

        cPn1.add(new JLabel("오토 클릭 지연 :"));
        cPn1.add(auto);

        cPn2.add(new JLabel("마우스 버튼 :"));
        cPn2.add(CBmenu);

        cPn3.add(new JLabel("누르기 / 토글 :"));
        cPn3.add(CBmenu3);

        cPn4.add(new JLabel("조작 키 :"));
        cPn4.add(keycode_l);

        cPn5.add(changeKey);
        cPn5.add(help);

        AutoPn.add(cPn0);
        AutoPn.add(cPn1);
        AutoPn.add(cPn2);
        AutoPn.add(cPn3);
        AutoPn.add(cPn4);
        AutoPn.add(cPn5);
        this.add(AutoPn);
        //디자인 끝
        
        // listener
        ActionListener changeKey = e -> {
            DesignAndWork.this.changeKey.setEnabled(false);
            changing = true;
            dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            dialog.setVisible(true);
        };
        this.changeKey.addActionListener(changeKey);

        KeyListener keyType = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
            @Override
            public void keyPressed(KeyEvent e) {
                String text = auto.getText();
                if (text.startsWith("0")){
                    auto.setText("1");
                }
                if (text.isEmpty()){
                    auto.setText("1");
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                String text = auto.getText();
                if (text.startsWith("0")){
                    auto.setText("1");
                }
                if (text.isEmpty()){
                    auto.setText("1");
                }
            }
        };
        this.auto.addKeyListener(keyType);

        // listener -----------------
        ActionListener help = e -> JOptionPane.showMessageDialog(null,
                "누르기 / 토글 에서\n \"누르기\" 로 설정한 경우, \n설정한 조작 키를 누르고 있는 동안 자동 클릭이 작동하며, \n키를 때는 순간 자동 클릭이 멈춥니다." +
                        "\n\n\"토글\"로 설정한 경우, \n조작 키를 한번 누르면 자동 클릭 시작, \n한번 더 누르면 자동 클릭이 멈춥니다.",
                "도움말", JOptionPane.INFORMATION_MESSAGE);
        this.help.addActionListener(help);
    } // design()
    public void start() {
        //유효성 검사 시작
        String text = auto.getText();
        long delay = Long.parseLong(text);
        //유효성 검사 끝

        //초기화 시작
        CBmenu.setEnabled(false);
        CBmenu2.setEnabled(false);
        CBmenu3.setEnabled(false);
        changeKey.setEnabled(false);
        auto.setEnabled(false);
        help.setEnabled(false);
        start = true;
        left = false;
        middle = false;
        right = false;
        executorService = Executors.newSingleThreadScheduledExecutor();
        //초기화 끝

        String Mouse = Objects.requireNonNull(CBmenu.getSelectedItem()).toString(); //사용자가 선택한 자동클릭 될 마우스 버튼
        String delayAuto = Objects.requireNonNull(CBmenu2.getSelectedItem()).toString(); //사용자가 선택한 지연 단위 설정(ms, micros)

        if (Objects.equals(Mouse, "왼쪽"))
            left = true;
        else if (Objects.equals(Mouse, "가운데"))
            middle = true;
        else if (Objects.equals(Mouse, "오른쪽"))
            right = true;

        //자동 클릭 매크로 시작
        if (Objects.equals(delayAuto, "밀리초(ms)"))
            executorService.scheduleAtFixedRate(this::startMacro, 0, delay,
                    TimeUnit.MILLISECONDS);
        else if (Objects.equals(delayAuto, "마이크로초(μs)"))
            executorService.scheduleAtFixedRate(this::startMacro, 0, delay,
                    TimeUnit.MICROSECONDS);
    } // start()

    //자동 클릭 매크로 실행(메서드 반복)
    public void startMacro() {
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
