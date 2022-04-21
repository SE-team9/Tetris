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
	private JLabel title = new JLabel("SE Team9 Tetris");
	
	// 0�̸� �Ϲ� ���, 1�̸� ������ ��� 
	private JLabel[] gameMode = new JLabel[2];
	private int curGameMode; 

	// ���� �޴�, ���� ȭ��, ���ھ� ����, ���� ���� 
	private JButton[] menu = new JButton[4];
	private String[] btnText = { "Start Game", "Settings", "ScoreBoard", "Quit" };
	private int curPos;

	private int w, h;
	
	public StartupForm() {
		this.w = 600;
		this.h = 450;
		
		// ��ü�� ó�� ������ ���� �⺻ �� (�ٸ� ������ �� form�� ��� �� �� �Լ��� ũ�� �ʱ�ȭ)
		initComponents(w, h); 
		initControls();
	}
	
	// up-down���� �޴� ����, right-left�� ���� ��� ����
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
		
		am.put("enter", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectMenu(curPos);
			}
		});
	}

	private void moveUp() {
		menu[curPos].setBackground(Color.white);
		curPos--;
		if (curPos < 0) {
			curPos = menu.length - 1;
		}
		menu[curPos].setBackground(Color.lightGray);
	}

	private void moveDown() {
		menu[curPos].setBackground(Color.white);
		curPos++;
		if (curPos > menu.length - 1) {
			curPos = 0;
		}
		menu[curPos].setBackground(Color.lightGray);
	}

	private void moveRight() {
		gameMode[curGameMode].setVisible(false);
		curGameMode++;
		if (curGameMode > gameMode.length - 1) {
			curGameMode = 0;
		}
		gameMode[curGameMode].setVisible(true);
	}

	private void moveLeft() {
		gameMode[curGameMode].setVisible(false);
		curGameMode--;
		if (curGameMode < 0) {
			curGameMode = gameMode.length - 1;
		}
		gameMode[curGameMode].setVisible(true);
	}

	public int getCurrentGameMode() {
		return curGameMode;
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
			Tetris.showOption(); // ���� ȭ�� 
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
	
	// �ٸ� ������ �� form�� ��� �� �� �Լ��� ũ�� �ʱ�ȭ
	public void initComponents(int w, int h) {
		// ��� ���� �� ������Ʈ
		this.w = w;
		this.h = h;
		
		initThisFrame();
		initLable();
		initButtons();
	}
	
	// todo: �������� ȭ�� ũ�� ����
	private void initThisFrame() {
		this.setSize(w, h);
		this.setResizable(false);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null); // ������ â�� ����� ����� ����.
		this.setVisible(false);
	}
	
	// ����Ű �ȳ�, ���� ����, ���� ��� �ؽ�Ʈ �ʱ�ȭ
	private void initLable() {
		title.setFont(new Font("Arial", Font.BOLD, 30));
		title.setBounds(w / 4, h / 20, w / 2, h / 6);
		title.setHorizontalAlignment(JLabel.CENTER);
		this.add(title);

		// TODO: ���� ȭ��ó�� <> �� ����� ǥ�������� ���ڴµ�, ������ ũ�⿡ ���� �� ��ġ�� �ٲ���� �ϴϱ� �ϴ� ����!
		gameMode[0] = new JLabel("Normal Mode");
		gameMode[1] = new JLabel("Item Mode");
		for(int i = 0; i < 2; i++) {
			gameMode[i].setFont(new Font("Arial", Font.BOLD, 15));
			gameMode[i].setBounds(w / 3, h / 3, w / 3, h / 15);
			gameMode[i].setHorizontalAlignment(JLabel.CENTER);
			this.add(gameMode[i]);
		}
		gameMode[1].setVisible(false);
	}

	private void initButtons() {
		for (int i = 0; i < menu.length; i++) {
			menu[i] = new JButton(btnText[i]);
			menu[i].setBounds(w / 3, h / 3 + (h / 10) * (i + 1), w / 3, h / 15);
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