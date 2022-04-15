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

public class StartupForm extends JFrame {
	
	private JLabel tetrisLabel;  // ��ܿ� ��Ʈ���� �̸� ǥ�� 
	private JLabel[] gameModeLabel = new JLabel[2]; // ���� ��� ���� ǥ��
	private int currentGameMode;  // 0�̸� �Ϲݸ�� 1�̸� ������ ���

	private JButton[] startButtons = new JButton[4];
	private String[] btnNames = { "Start Game", "Leaderboard", "Option", "Quit" };
	private static int currentButton;

	public StartupForm() {
		initThisFrame();
		initLable();
		initButtons();
		initControls();
	}

	// ����Ű ���ε�
	private void initControls() {
		InputMap im = this.getRootPane().getInputMap();
		ActionMap am = this.getRootPane().getActionMap();

		im.put(KeyStroke.getKeyStroke("UP"), "up");
		im.put(KeyStroke.getKeyStroke("DOWN"), "down");
		im.put(KeyStroke.getKeyStroke("ENTER"), "enter");
		im.put(KeyStroke.getKeyStroke("RIGHT"), "right");
		im.put(KeyStroke.getKeyStroke("LEFT"), "left");
		
		am.put("right", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				modeChangeRight();
			}
		});
		am.put("left", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				modeChangeLeft();
			}
		});
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
	
	// ��� ��Ʈ���� ���� ǥ��, ���� ��� ���� ǥ��
	private void initLable() {
		int w = this.getWidth();
		int h = this.getHeight();
		Font tetrisFont = new Font("Arial", Font.BOLD, w / 10);

		tetrisLabel = new JLabel("Tetris");
		tetrisLabel.setFont(tetrisFont);
		tetrisLabel.setBounds(w / 4, h / 10, w / 2, h / 6);
		tetrisLabel.setHorizontalAlignment(JLabel.CENTER);
		this.add(tetrisLabel);

		gameModeLabel[0] = new JLabel("Normal Mode");
		gameModeLabel[0].setBounds(w / 3, h / 2, w / 3, h / 15);
		gameModeLabel[0].setHorizontalAlignment(JLabel.CENTER);
		this.add(gameModeLabel[0]);

		gameModeLabel[1] = new JLabel("Item Mode");
		gameModeLabel[1].setBounds(w / 3, h / 2, w / 3, h / 15);
		gameModeLabel[1].setHorizontalAlignment(JLabel.CENTER);
		this.add(gameModeLabel[1]);
		gameModeLabel[1].setVisible(false);

	}

	// ��ư ����, ��ġ, ���� ���� 
	private void initButtons() {
		int w = this.getWidth();
		int h = this.getHeight();

		for (int i = 0; i < startButtons.length; i++) {
			startButtons[i] = new JButton(btnNames[i]);
			startButtons[i].setBounds(w / 3, h / 2 + (i + 1) * (h / 12), w / 3, h / 15);
			startButtons[i].setBackground(Color.white);
			this.add(startButtons[i]);
		}
		startButtons[currentButton].setBackground(Color.lightGray);
	}
	
	// ��� ���� ������ ����Ű
	private void modeChangeRight() {
		gameModeLabel[currentGameMode].setVisible(false);

		currentGameMode++;
		if (currentGameMode > gameModeLabel.length - 1) {
			currentGameMode = 0;
		}
		gameModeLabel[currentGameMode].setVisible(true);
	}

	// ��� ���� ���� ����Ű
	private void modeChangeLeft() {
		gameModeLabel[currentGameMode].setVisible(false);

		currentGameMode--;
		if (currentGameMode < 0) {
			currentGameMode = gameModeLabel.length - 1;
		}

		gameModeLabel[currentGameMode].setVisible(true);
	}
	
	// ���� ��ư���� �̵� 
	private void moveUpCurrentButton() {
		startButtons[currentButton].setBackground(Color.white);

		currentButton--;
		if (currentButton < 0)
			currentButton = startButtons.length - 1;

		startButtons[currentButton].setBackground(Color.lightGray);
	}

	// �Ʒ��� ��ư���� �̵�
	private void moveDownCurrentButton() {
		startButtons[currentButton].setBackground(Color.white);
		
		currentButton++;
		if (currentButton > startButtons.length - 1) {
			currentButton = 0;
		}
	
		startButtons[currentButton].setBackground(Color.lightGray);
	}

	// ��ư�� ���� ������ �̵�
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
			// �ɼ� ȭ������ �̵�
			break;
		case 3:
			System.exit(0);
			break;
		}
	}
	
	public int getCurrentGameMode() {
		return currentGameMode;
	}

	public static void main(String[] args) {

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new StartupForm().setVisible(true);
			}
		});
	}
}


