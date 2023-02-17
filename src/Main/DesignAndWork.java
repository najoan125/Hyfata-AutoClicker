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

        JPanel AutoPn = new JPanel(new GridLayout(5, 1));

        AutoPn.setBorder(new TitledBorder(new TitledBorder("���� Ŭ�� ���� ����"), "", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.LEFT));
        JPanel cPn0 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel cPn1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel cPn2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel cPn3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel cPn4 = new JPanel(new FlowLayout(FlowLayout.LEFT));

        String CBmenu_1[] = {"����", "���", "������"};
        CBmenu = new JComboBox<>(CBmenu_1);
        CBmenu.setPreferredSize(new Dimension(80, 20));

        String CBmenu_2[] = {"�и���(ms)", "����ũ����(��s)"};
        CBmenu2 = new JComboBox<>(CBmenu_2);
        CBmenu2.setPreferredSize(new Dimension(150, 20));

        auto = new JTextField("100", 5);
        changeKey = new JButton("���� Ű ����");
        help = new JButton("����");

        cPn0.add(new JLabel("���� ���� ���� : "));
        cPn0.add(CBmenu2);

        cPn1.add(new JLabel("���� Ŭ�� ���� :"));
        cPn1.add(auto);

        cPn2.add(new JLabel("���콺 ��ư :"));
        cPn2.add(CBmenu);

        cPn3.add(new JLabel("���� Ű :"));
        cPn3.add(keycode_l);

        cPn4.add(changeKey);
        cPn4.add(help);

        AutoPn.add(cPn0);
        AutoPn.add(cPn1);
        AutoPn.add(cPn2);
        AutoPn.add(cPn3);
        AutoPn.add(cPn4);
        this.add(AutoPn);
        //������ ��
        
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
                        "���� ����, ��ư ����, ���� Ű ������ ��ġ�� \"������ ���� Ű\"�� ������ \n�� ��� �ڵ� Ŭ���� ���۵˴ϴ�.\n\n�׸��� ������ ����Ű�� �ٽ� ������ �ڵ�Ŭ���� ����˴ϴ�.",
                        "����", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        this.help.addActionListener(help);
    } // design()

    public void start() {
        //��ȿ�� �˻� ����
        final String REGEX = "[0-9]+";
        String text = auto.getText();
        if (!text.matches(REGEX)) {
            JOptionPane.showMessageDialog(null, "���� ������ ���ڸ� �Է��ϼ���.", "Mouse Delay Setting Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else if (text.startsWith("0")) {
            JOptionPane.showMessageDialog(null, "���� ������ 0���� �����ؼ��� �ȵ˴ϴ�.", "Mouse Delay Setting Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        long delay = Long.parseLong(text);
        if (delay >= 5000) {
            JOptionPane.showMessageDialog(null, "���� ������ 5000���� �۾ƾ� �մϴ�.", "Mouse Delay Setting Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        //��ȿ�� �˻� ��

        //�ʱ�ȭ ����
        CBmenu.setEnabled(false);
        CBmenu2.setEnabled(false);
        changeKey.setEnabled(false);
        auto.setEnabled(false);
        start = true;
        left = false;
        middle = false;
        right = false;
        executorService = Executors.newSingleThreadScheduledExecutor();
        //�ʱ�ȭ ��

        String Mouse = CBmenu.getSelectedItem().toString(); //����ڰ� ������ �ڵ�Ŭ�� �� ���콺 ��ư
        String delayAuto = CBmenu2.getSelectedItem().toString(); //����ڰ� ������ ���� ���� ����(ms, micros)

        if (Mouse == "����")
            left = true;
        else if (Mouse == "���")
            middle = true;
        else if (Mouse == "������")
            right = true;

        //�ڵ� Ŭ�� ��ũ�� ����
        if (delayAuto == "�и���(ms)")
            executorService.scheduleAtFixedRate(() -> theWorldFastestAutoclickerFuck(), 0, delay,
                    TimeUnit.MILLISECONDS);
        else if (delayAuto == "����ũ����(��s)")
            executorService.scheduleAtFixedRate(() -> theWorldFastestAutoclickerFuck(), 0, delay,
                    TimeUnit.MICROSECONDS);
    } // start()

    //�ڵ� Ŭ�� ��ũ�� ����(�޼��� �ݺ�)
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
