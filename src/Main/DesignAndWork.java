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

    //ª�� �ð� ���� �����̸� �ֱ� ���� ���̺귯��
    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    //Robot ��ü ����
    Robot r;

    {
        try {
            r = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    //GUI ���� ������ - start
    JTextField auto; //���� ���� �Է¶�
    JButton changeKey; //���� Ű ���� ��ư
    JButton help; //���� ��ư
    JComboBox<String> CBmenu = new JComboBox<>();
    JComboBox<String> CBmenu2 = new JComboBox<>();
    JComboBox<String> CBmenu3 = new JComboBox<>();
    JOptionPane pane = new JOptionPane("Ű �ν� ���Դϴ�...\nESC�� ���� ����� �� �ֽ��ϴ�.", JOptionPane.INFORMATION_MESSAGE,
            JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
    JDialog dialog = pane.createDialog("Ű �ν� ��...");
    JLabel keycode_l = new JLabel("�������� ����"); //GUI Ű�ڵ� ��
    //GUI ���� ������ - end

    boolean start = false; //�ڵ� Ŭ�� ��ũ�� �۵� ����
    boolean changing = false; //��ũ�� Ű ���� �� ����
    Integer keycode = null; //����� Ű�ڵ�

    //��ũ�� ���콺 ��ư 3��
    boolean left = false;
    boolean right = false;
    boolean middle = false;

    //GUI �� GUI ��ư ������
    public void design() {
        //������ ����
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel AutoPn = new JPanel(new GridLayout(6, 1));

        AutoPn.setBorder(new TitledBorder(new TitledBorder("���� Ŭ�� ���� ����"), "", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.ABOVE_TOP));
        JPanel cPn0 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel cPn1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel cPn2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel cPn3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel cPn4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel cPn5 = new JPanel(new FlowLayout(FlowLayout.LEFT));

        String[] CBmenu_1 = {"����", "���", "������"};
        CBmenu = new JComboBox<>(CBmenu_1);
        CBmenu.setPreferredSize(new Dimension(80, 20));

        String[] CBmenu_2 = {"�и���(ms)", "����ũ����(��s)"};
        CBmenu2 = new JComboBox<>(CBmenu_2);
        CBmenu2.setPreferredSize(new Dimension(150, 20));

        String[] CBmenu_3 = {"������", "���"};
        CBmenu3 = new JComboBox<>(CBmenu_3);
        CBmenu3.setPreferredSize(new Dimension(80, 20));

        auto = new JTextField("100", 5);
        changeKey = new JButton("���� Ű ����");
        help = new JButton("����");

        cPn0.add(new JLabel("���� ���� ���� : "));
        cPn0.add(CBmenu2);

        cPn1.add(new JLabel("���� Ŭ�� ���� :"));
        cPn1.add(auto);

        cPn2.add(new JLabel("���콺 ��ư :"));
        cPn2.add(CBmenu);

        cPn3.add(new JLabel("������ / ��� :"));
        cPn3.add(CBmenu3);

        cPn4.add(new JLabel("���� Ű :"));
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
        //������ ��
        
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
                "������ / ��� ����\n \"������\" �� ������ ���, \n������ ���� Ű�� ������ �ִ� ���� �ڵ� Ŭ���� �۵��ϸ�, \nŰ�� ���� ���� �ڵ� Ŭ���� ����ϴ�." +
                        "\n\n\"���\"�� ������ ���, \n���� Ű�� �ѹ� ������ �ڵ� Ŭ�� ����, \n�ѹ� �� ������ �ڵ� Ŭ���� ����ϴ�.",
                "����", JOptionPane.INFORMATION_MESSAGE);
        this.help.addActionListener(help);
    } // design()
    public void start() {
        //��ȿ�� �˻� ����
        String text = auto.getText();
        long delay = Long.parseLong(text);
        //��ȿ�� �˻� ��

        //�ʱ�ȭ ����
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
        //�ʱ�ȭ ��

        String Mouse = Objects.requireNonNull(CBmenu.getSelectedItem()).toString(); //����ڰ� ������ �ڵ�Ŭ�� �� ���콺 ��ư
        String delayAuto = Objects.requireNonNull(CBmenu2.getSelectedItem()).toString(); //����ڰ� ������ ���� ���� ����(ms, micros)

        if (Objects.equals(Mouse, "����"))
            left = true;
        else if (Objects.equals(Mouse, "���"))
            middle = true;
        else if (Objects.equals(Mouse, "������"))
            right = true;

        //�ڵ� Ŭ�� ��ũ�� ����
        if (Objects.equals(delayAuto, "�и���(ms)"))
            executorService.scheduleAtFixedRate(this::startMacro, 0, delay,
                    TimeUnit.MILLISECONDS);
        else if (Objects.equals(delayAuto, "����ũ����(��s)"))
            executorService.scheduleAtFixedRate(this::startMacro, 0, delay,
                    TimeUnit.MICROSECONDS);
    } // start()

    //�ڵ� Ŭ�� ��ũ�� ����(�޼��� �ݺ�)
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
