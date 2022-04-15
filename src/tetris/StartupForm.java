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
	private JLabel guideText = new JLabel("메뉴 이동: Up/Down, 메뉴 선택: Enter");
	private JButton[] menu = new JButton[3];
	private String[] btnText = { "Start Game", "Leaderboard", "Quit" };
	private int curPos = 0; // 현재 포커스 초기화 

	public StartupForm() {
		initComponents();
		initControls();
		
		// 메뉴 선택 방법 알려주기 
		guideText.setHorizontalAlignment(SwingConstants.CENTER);
		guideText.setBounds(20, 20, 500, 30);
		this.add(guideText);
	}

	// 조작키 바인딩
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
	
	// 위로 이동 
	private void moveUp() {
		menu[curPos].setBackground(Color.white);

		curPos--;
		if (curPos < 0) {
			curPos = 2;
		}

		menu[curPos].setBackground(Color.lightGray);
	}

	// 아래로 이동
	private void moveDown() {
		menu[curPos].setBackground(Color.white);
		
		curPos++;
		if (curPos > 2) {
			curPos = 0;
		}
	
		menu[curPos].setBackground(Color.lightGray);
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
			Tetris.showLeaderboard(); // 점수판 
			break;
		case 2:
			System.exit(0); // 게임 종료 
			break;
		}
	}
	
	private void initComponents() {
		initThisFrame();
		initButtons();
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

	private void initButtons() {
		for (int i = 0; i < menu.length; i++) {
			menu[i] = new JButton(btnText[i]);
			menu[i].setBounds(200, 250 + i * 40, 200, 30);
			
			// 버튼 색상은 흰색으로 초기화
			menu[i].setBackground(Color.white);
			this.add(menu[i]);
		}
		
		// 처음엔 첫번째 버튼에 포커스 두기 
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


