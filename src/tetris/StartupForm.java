package tetris;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

public class StartupForm extends JFrame {

	private JButton[] startButtons = new JButton[3];
	private String[] btnNames = { "Start Game", "Leaderboard", "Quit" };
	private static int currentButton;

	public StartupForm() {
		initThisFrame();
		initButtons();

		initControls();
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
				moveUpCurrentButton();
			}
		});
		am.put("down", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveDownCurrentButton();
			}
		});
		am.put("enter", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enterCurrentButton(currentButton);
			}
		});
	}

	private void initThisFrame() {
		this.setSize(600, 450);
		this.setResizable(false);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(false);
	}

	// 버튼 생성, 위치, 배경색 설정 
	private void initButtons() {
		for (int i = 0; i < startButtons.length; i++) {
			startButtons[i] = new JButton(btnNames[i]);
			startButtons[i].setBounds(200, 250 + i * 40, 200, 30);
			startButtons[i].setBackground(Color.white);
			this.add(startButtons[i]);
		}

		startButtons[currentButton].setBackground(Color.lightGray);
	}

	// 위쪽 버튼으로 이동 
	private void moveUpCurrentButton() {
		startButtons[currentButton].setBackground(Color.white);

		currentButton--;
		if (currentButton < 0)
			currentButton = 2;

		startButtons[currentButton].setBackground(Color.lightGray);
	}

	// 아래쪽 버튼으로 이동
	private void moveDownCurrentButton() {
		startButtons[currentButton].setBackground(Color.white);
		
		currentButton++;
		if (currentButton > 2) {
			currentButton = 0;
		}
	
		startButtons[currentButton].setBackground(Color.lightGray);
	}

	// 버튼에 따라 프레임 이동
	private void enterCurrentButton(int currentButton) {
		switch (currentButton) {
		case 0:
			this.setVisible(false);
			Tetris.start();
			break;
		case 1:
			this.setVisible(false);
			Tetris.showLeaderboard();
			break;
		case 2:
			System.exit(0);
			break;
		}
	}

	public static void main(String[] args) {

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new StartupForm().setVisible(true);
			}
		});
	}
}


