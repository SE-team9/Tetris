package tetris;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;

public class GameForm extends JFrame {
	private GameArea ga;
	private GameThread gt;
	private NextBlockArea nba;
	private JLabel scoreDisplay;
	private JLabel levelDisplay;
	private JTextArea keyDisplay;
	private boolean isPaused = false;

	public GameForm() {
		initComponents();
		initControls();
	}

	private void initControls() {
		InputMap im = this.getRootPane().getInputMap();
		ActionMap am = this.getRootPane().getActionMap();

		im.put(KeyStroke.getKeyStroke("RIGHT"), "right");
		im.put(KeyStroke.getKeyStroke("LEFT"), "left");
		im.put(KeyStroke.getKeyStroke("UP"), "up");
		im.put(KeyStroke.getKeyStroke("DOWN"), "downOneLine");
		im.put(KeyStroke.getKeyStroke("SPACE"), "downToEnd");
		im.put(KeyStroke.getKeyStroke("Q"), "quit");
		im.put(KeyStroke.getKeyStroke("E"), "exit");
		im.put(KeyStroke.getKeyStroke("ESCAPE"), "back");

		am.put("right", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!isPaused)
					ga.moveBlockRight();
			}
		});

		am.put("left", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!isPaused)
					ga.moveBlockLeft();
			}
		});

		am.put("up", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!isPaused)
					ga.rotateBlock();
			}
		});

		am.put("downOneLine", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!isPaused)
					ga.moveBlockDown(isPaused);
			}
		});

		am.put("downToEnd", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!isPaused)
					ga.dropBlock();
			}
		});
		
		am.put("quit", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				isPaused = !isPaused;
				
				if(isPaused) {
					gt.pause();
				}else {
					gt.reStart();
				}
			}
		});
		
		am.put("exit", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				goToMainMenu();
			}
		});
		
		am.put("back", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				goToMainMenu();
			}
		});
	}
	
	// 게임 종료하고 시작 메뉴로 이동
	private void goToMainMenu() {
		// 게임 스레드 인터럽트 
		gt.interrupt();
		
		this.setVisible(false);
		Tetris.showStartup();
	}

	public void startGame() {
		ga.initBackgroundArray(); // 배경 초기화 
		ga.updateNextBlock(); // 다음 블럭 표시
		
		// GameThread 시작
		gt = new GameThread(ga, this, nba);
		gt.start();
	}
	
	public void updateScore(int score) {
		scoreDisplay.setText("Score: " + score);
	}

	public void updateLevel(int level) {
		levelDisplay.setText("Level: " + level);
	}
	
	private void initComponents() {
		initThisFrame();
		initDisplay();
		
		ga = new GameArea(10);
		this.add(ga);
		
		nba = new NextBlockArea(ga);
		this.add(nba);
	}

	private void initThisFrame() {
		this.setSize(600, 450);
		this.setResizable(false);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(false);
	}
	
	private void initDisplay() {
		scoreDisplay = new JLabel("Score: 0");
		levelDisplay = new JLabel("Level: 0");
		scoreDisplay.setBounds(420, 10, 100, 30);
		levelDisplay.setBounds(420, 40, 100, 30);
		this.add(scoreDisplay);
		this.add(levelDisplay);

		keyDisplay = new JTextArea(" ← : 블럭 왼쪽 이동 \n → : 블럭 오른쪽 이동 \n"
				+ " ↓ : 블럭 아래 한 칸 이동\n ↑ : 블럭 회전\n Space : 블럭 맨 아래 이동\n" + " q : 게임 정지/재개\n ESC : 뒤로 가기\n");
		keyDisplay.setBounds(20, 210, 160, 150);
		this.add(keyDisplay);
	}

	// GameForm 프레임 실행
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new GameForm().setVisible(true);
			}
		});
	}

}
