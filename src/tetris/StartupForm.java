package tetris;

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
	private JLabel guide = new JLabel("메뉴 이동: Up/Down, 게임 모드 선택: Right/Left, 메뉴 선택: Enter");
	private JLabel[] mode = new JLabel[2];
	private int curMode; // 0이면 일반 모드, 1이면 아이템 모드

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

		// 키보드의 키들을 guide 동작과 연결 
		for (int i = 0; i < 125; i++) {
			String text = java.awt.event.KeyEvent.getKeyText(i);
			if (!text.contains("Unknown keyCode: ")) {
				im.put(KeyStroke.getKeyStroke(text), "guide");
			}
		}

		im.put(KeyStroke.getKeyStroke("UP"), "up");
		im.put(KeyStroke.getKeyStroke("DOWN"), "down");
		im.put(KeyStroke.getKeyStroke("ENTER"), "enter");
		im.put(KeyStroke.getKeyStroke("RIGHT"), "right");
		im.put(KeyStroke.getKeyStroke("LEFT"), "left");

		am.put("guide", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayGuide();
			}
		});
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

	// 위로 이동
	private void moveUp() {
		menu[curPos].setBackground(Color.white);

		curPos--;
		if (curPos < 0) {
			curPos = menu.length - 1;
		}

		menu[curPos].setBackground(Color.lightGray);
	}

	// 아래로 이동
	private void moveDown() {
		menu[curPos].setBackground(Color.white);

		curPos++;
		if (curPos > menu.length - 1) {
			curPos = 0;
		}

		menu[curPos].setBackground(Color.lightGray);
	}

	// 모드 선택 오른쪽 방향키
	private void moveRight() {
		mode[curMode].setVisible(false);

		curMode++;
		if (curMode > mode.length - 1) {
			curMode = 0;
		}

		mode[curMode].setVisible(true);
	}

	// 모드 선택 왼쪽 방향키
	private void moveLeft() {
		mode[curMode].setVisible(false);

		curMode--;
		if (curMode < 0) {
			curMode = mode.length - 1;
		}

		mode[curMode].setVisible(true);
	}
	
	// 다른 키를 눌렀을 경우 사용가능한 키를 표시 
	// (알파벳, 숫자, F1~F12 에는 작동하지만 그 외의 키는 작동하지 않음. 수정필요
	private void displayGuide() {
		guide.setVisible(true);
	}

	// 선택한 메뉴에 따라 화면 전환
	private void selectMenu(int curPos) {
		switch (curPos) {
		case 0:
			this.setVisible(false);
			Tetris.start(); // 게임 시작
			break;
		case 1:
			this.setVisible(false);
			Tetris.showOption();
			break;
		case 2:
			this.setVisible(false);
			Tetris.showLeaderboard(); // 스코어 보드
			break;
		case 3:
			System.exit(0); // 게임 종료
			break;
		}
	}

	public int getCurrentGameMode() {
		return curMode;
	}

	// todo: 설정에서 화면 크기 선택
	private void initThisFrame() {
		this.setSize(600, 450);
		this.setResizable(false);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(false);
	}

	private void initLable() {
		// 키 조작 방법
		guide.setHorizontalAlignment(SwingConstants.CENTER);
		guide.setBounds(50, 20, 500, 30);
		this.add(guide);
		guide.setVisible(false);

		int w = this.getWidth();
		int h = this.getHeight();
		Font tetrisFont = new Font("Arial", Font.BOLD, w / 10);

		// 게임 제목
		title.setFont(tetrisFont);
		title.setBounds(w / 4, h / 10, w / 2, h / 6);
		title.setHorizontalAlignment(JLabel.CENTER);
		this.add(title);

		// 게임 모드
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

	private void initComponents() {
		initThisFrame();
		initButtons();
		initLable();
	}

	// StartupForm 프레임 실행
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new StartupForm().setVisible(true);
			}
		});
	}
}
