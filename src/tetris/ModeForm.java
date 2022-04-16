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

public class ModeForm extends JFrame {

	private JLabel title = new JLabel("Select a Mode");
	private JLabel guide = new JLabel("모드 이동: Up/Down, 모드 선택: Enter");
	private static int mode=2;

	private JButton[] menu = new JButton[4];
	private String[] btnText = { "Easy", "Normal", "Hard", "Item"};
	private static int curPos;

	public ModeForm() {
		initComponents();
		initControls();
	}

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

	// 선택한 메뉴에 따라 화면 전환
	private void selectMenu(int curPos) {
		switch (curPos) {
		case 0: // easy 선택
			this.setVisible(false);
			this.mode = 1;
			Tetris.showStartup(); 
			break;
		case 1: // normal 선택
			this.setVisible(false);
			this.mode = 2;
			Tetris.showStartup(); 
			break;
		case 2: // hard 선택
			this.setVisible(false);
			this.mode = 3;
			Tetris.showStartup(); 
			break;
		case 3: // item mode 선택
			this.setVisible(false);
			this.mode = 4;
			Tetris.showStartup(); 
			break;
		}
	}

	public int getMode() {
		return this.mode;
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
		guide.setBounds(20, 20, 500, 30);
		this.add(guide);

		int w = this.getWidth();
		int h = this.getHeight();
		Font tetrisFont = new Font("Arial", Font.BOLD, w / 20);

		// 게임 제목
		title.setFont(tetrisFont);
		title.setBounds(w / 4, h / 10, w / 2, h / 6);
		title.setHorizontalAlignment(JLabel.CENTER);
		this.add(title);

	}

	private void initButtons() {
		int w = this.getWidth();
		int h = this.getHeight();

		for (int i = 0; i < menu.length; i++) {
			menu[i] = new JButton(btnText[i]);
			menu[i].setBounds(w / 3, h / 2 + (i + 1) * (h / 12), w / 3, h / 15);
			menu[i].setBackground(Color.white);
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
				new ModeForm().setVisible(true);
			}
		});
	}

}
