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
	
	// 0이면 일반 모드, 1이면 아이템 모드 
	private JLabel[] gameMode = new JLabel[2];
	private int curGameMode; 

	// 시작 메뉴, 설정 화면, 스코어 보드, 게임 종료 
	private JButton[] menu = new JButton[4];
	private String[] btnText = { "Start Game", "Settings", "ScoreBoard", "Quit" };
	private int curPos;

	private int w, h;
	
	public StartupForm() {
		this.w = 600;
		this.h = 450;
		
		// 객체를 처음 생성할 때는 기본 값 (다른 곳에서 이 form을 띄울 때 이 함수로 크기 초기화)
		initComponents(w, h); 
		initControls();
	}
	
	// up-down으로 메뉴 선택, right-left로 게임 모드 선택
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

	// 선택한 메뉴에 따라 화면 전환
	private void selectMenu(int curPos) {
		switch (curPos) {
		case 0:
			this.setVisible(false);
			Tetris.start(); // 게임 시작
			break;
		case 1:
			this.setVisible(false);
			Tetris.showOption(); // 설정 화면 
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
	
	// 다른 곳에서 이 form을 띄울 때 이 함수로 크기 초기화
	public void initComponents(int w, int h) {
		// 멤버 변수 값 업데이트
		this.w = w;
		this.h = h;
		
		initThisFrame();
		initLable();
		initButtons();
	}
	
	// todo: 설정에서 화면 크기 선택
	private void initThisFrame() {
		this.setSize(w, h);
		this.setResizable(false);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null); // 프레임 창을 모니터 가운데에 띄운다.
		this.setVisible(false);
	}
	
	// 조작키 안내, 게임 제목, 게임 모드 텍스트 초기화
	private void initLable() {
		title.setFont(new Font("Arial", Font.BOLD, 30));
		title.setBounds(w / 4, h / 20, w / 2, h / 6);
		title.setHorizontalAlignment(JLabel.CENTER);
		this.add(title);

		// TODO: 설정 화면처럼 <> 이 모양을 표시했으면 좋겠는데, 프레임 크기에 따라 또 위치를 바꿔줘야 하니까 일단 보류!
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

	// StartupForm 프레임 실행
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new StartupForm().setVisible(true);
			}
		});
	}
}