package tetris;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

public class StartupForm extends JFrame {
	private JLabel guideText = new JLabel("�޴� �̵�: Up/Down, �޴� ����: Enter");
	private JButton[] menu = new JButton[3];
	private String[] btnText = { "Start Game", "Leaderboard", "Quit" };
	private int curPos = 0; // ���� ��Ŀ�� �ʱ�ȭ 

	public StartupForm() {
		initComponents();
		initControls();
		
		// �޴� ���� ��� �˷��ֱ� 
		guideText.setHorizontalAlignment(SwingConstants.CENTER);
		guideText.setBounds(20, 20, 500, 30);
		this.add(guideText);
	}

	// ����Ű ���ε�
	private void initControls() {
		InputMap im = this.getRootPane().getInputMap();
		ActionMap am = this.getRootPane().getActionMap();

		im.put(KeyStroke.getKeyStroke("UP"), "up");
		im.put(KeyStroke.getKeyStroke("DOWN"), "down");
		im.put(KeyStroke.getKeyStroke("ENTER"), "enter");

		am.put("up", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveUp();
			}
		});
		
		am.put("down", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveDown();
			}
		});
		
		am.put("enter", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectMenu(curPos);
			}
		});
	}
	
	// ���� �̵� 
	private void moveUp() {
		menu[curPos].setBackground(Color.white);

		curPos--;
		if (curPos < 0) {
			curPos = 2;
		}

		menu[curPos].setBackground(Color.lightGray);
	}

	// �Ʒ��� �̵�
	private void moveDown() {
		menu[curPos].setBackground(Color.white);
		
		curPos++;
		if (curPos > 2) {
			curPos = 0;
		}
	
		menu[curPos].setBackground(Color.lightGray);
	}

	// ������ �޴��� ���� ȭ�� ��ȯ
	private void selectMenu(int curPos) {
		switch (curPos) {
		case 0:
			this.setVisible(false);
			Tetris.start(); // ���� ���� 
			break;
		case 1:
			this.setVisible(false);
			Tetris.showLeaderboard(); // ������ 
			break;
		case 2:
			System.exit(0); // ���� ���� 
			break;
		}
	}
	
	private void initComponents() {
		initThisFrame();
		initButtons();
	}
	
	// todo: �������� ȭ�� ũ�� ����
	private void initThisFrame() {
		this.setSize(600, 450);
		this.setResizable(false);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(false);
	}

	private void initButtons() {
		for (int i = 0; i < menu.length; i++) {
			menu[i] = new JButton(btnText[i]);
			menu[i].setBounds(200, 250 + i * 40, 200, 30);
			
			// ��ư ������ ������� �ʱ�ȭ
			menu[i].setBackground(Color.white);
			this.add(menu[i]);
		}
		
		// ó���� ù��° ��ư�� ��Ŀ�� �α� 
		curPos = 0;
		menu[curPos].setBackground(Color.lightGray);
	}

	public static void main(String[] args) {

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new StartupForm().setVisible(true);
			}
		});
	}
}


