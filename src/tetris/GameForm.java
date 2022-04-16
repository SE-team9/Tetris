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
	private boolean toggle = false;

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
				if(!ga.getPaused())
					ga.moveBlockRight();
			}
		});

		am.put("left", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!ga.getPaused())
					ga.moveBlockLeft();
			}
		});

		am.put("up", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!ga.getPaused())
					ga.rotateBlock();
			}
		});

		am.put("downOneLine", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!ga.getPaused())
					ga.moveBlockDown();
			}
		});

		am.put("downToEnd", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!ga.getPaused())
					ga.dropBlock();
			}
		});
		
		
		am.put("quit", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// GameArea에서 block.moveDown() 함수 호출하기 전에
				// paused 상태에 따라 블록 움직임 제어하기
				toggle = !toggle;
				ga.setPaused(toggle);
			}
		});
		
		am.put("exit", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//interrupt
				
				interruptThread(); // 게임 스레드 인터럽트
			}
		});
		
		am.put("back", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				interruptThread(); // 게임 스레드 인터럽트 
			}
		});
	}
	
	// 게임 종료하고 시작 메뉴로 이동
	private void interruptThread() {
		// 게임 스레드 인터럽트 
		gt.interrupt();
		
		// pause 전용 스레드 인터럽트
		
		
		
		this.setVisible(false);
		Tetris.showStartup();
	}

	// 게임 스레드 시작
	public void startGame() {
		// 시작할 때마다 배경 초기화
		ga.initBackgroundArray();
		
		// 다음 블럭 초기화
		ga.updateNextBlock();
		
		// 게임 스레드 시작
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
		
		ga = new GameArea(10, gt);
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
