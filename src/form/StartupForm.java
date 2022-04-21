package form;
import tetris.*;

import java.awt.Color;
import java.awt.Font;
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
	private JLabel title = new JLabel("Tetris");
	private JLabel guide = new JLabel("�޴� �̵�: Up/Down, ���� ��� ����: Right/Left, �޴� ����: Enter");
	private JLabel[] mode = new JLabel[2];
	private int curMode; // 0�̸� �Ϲ� ���, 1�̸� ������ ���

	private JButton[] menu = new JButton[4];
	private String[] btnText = { "Start Game", "Settings", "ScoreBoard", "Quit" };
	private static int curPos;

	public StartupForm() {
		initComponents();
		initControls();
	}

	private void initControls() {
		InputMap im = this.getRootPane().getInputMap();
		ActionMap am = this.getRootPane().getActionMap();

		im.put(KeyStroke.getKeyStroke("UP"), "up");
		im.put(KeyStroke.getKeyStroke("DOWN"), "down");
		im.put(KeyStroke.getKeyStroke("ENTER"), "enter");
		im.put(KeyStroke.getKeyStroke("RIGHT"), "right");
		im.put(KeyStroke.getKeyStroke("LEFT"), "left");

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

		am.put("right", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveRight();
			}
		});

		am.put("left", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveLeft();
			}
		});
	}

	// ���� �̵�
	private void moveUp() {
		menu[curPos].setBackground(Color.white);

		curPos--;
		if (curPos < 0) {
			curPos = menu.length - 1;
		}

		menu[curPos].setBackground(Color.lightGray);
	}

	// �Ʒ��� �̵�
	private void moveDown() {
		menu[curPos].setBackground(Color.white);

		curPos++;
		if (curPos > menu.length - 1) {
			curPos = 0;
		}

		menu[curPos].setBackground(Color.lightGray);
	}

	// ��� ���� ������ ����Ű
	private void moveRight() {
		mode[curMode].setVisible(false);

		curMode++;
		if (curMode > mode.length - 1) {
			curMode = 0;
		}

		mode[curMode].setVisible(true);
	}

	// ��� ���� ���� ����Ű
	private void moveLeft() {
		mode[curMode].setVisible(false);

		curMode--;
		if (curMode < 0) {
			curMode = mode.length - 1;
		}

		mode[curMode].setVisible(true);
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
			Tetris.showOption();
			break;
		case 2:
			this.setVisible(false);
			Tetris.showLeaderboard(); // ���ھ� ����
			break;
		case 3:
			System.exit(0); // ���� ����
			break;
		}
	}

	public int getCurrentGameMode() {
		return curMode;
	}
	
	private void initComponents() {
		initThisFrame();
		initButtons();
		initLable();
	}

//	public void initComponents(int w, int h) {
//		this.setSize(w, h);
//		initButtons();
//		initLable();
//	}

	// todo: �������� ȭ�� ũ�� ����
	private void initThisFrame() {
		this.setSize(600, 450);
		this.setResizable(false);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(false);
	}

	private void initLable() {
		// Ű ���� ���
		guide.setHorizontalAlignment(SwingConstants.CENTER);
		guide.setBounds(50, 20, 500, 30);
		//this.add(guide); 

		int w = this.getWidth();
		int h = this.getHeight();
		Font tetrisFont = new Font("Arial", Font.BOLD, w / 10);

		// ���� ����
		title.setFont(tetrisFont);
		title.setBounds(w / 4, h / 10, w / 2, h / 6);
		title.setHorizontalAlignment(JLabel.CENTER);
		this.add(title);

		// ���� ���
		mode[0] = new JLabel("Normal Mode");
		mode[0].setBounds(w / 3, h / 2, w / 3, h / 15);
		mode[0].setHorizontalAlignment(JLabel.CENTER);
		this.add(mode[0]);

		mode[1] = new JLabel("Item Mode");
		mode[1].setBounds(w / 3, h / 2, w / 3, h / 15);
		mode[1].setHorizontalAlignment(JLabel.CENTER);
		this.add(mode[1]);
		mode[1].setVisible(false);
	}

	private void initButtons() {
		int w = this.getWidth();
		int h = this.getHeight();

		for (int i = 0; i < menu.length; i++) {
			menu[i] = new JButton(btnText[i]);
			menu[i].setBounds(w / 3, h / 2 + (i + 1) * (h / 12), w / 3, h / 15);
			menu[i].setBackground(Color.white);
			menu[i].setFocusable(false);
			this.add(menu[i]);
		}
		menu[curPos].setBackground(Color.lightGray);
	}

	// StartupForm ������ ����
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new StartupForm().setVisible(true);
			}
		});
	}
}