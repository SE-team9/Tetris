package form;
import tetris.*;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

public class GameForm extends JFrame {
	private GameArea ga;
	private GameThread gt;
	private NextBlockArea nba;
	private JLabel scoreDisplay;
	private JLabel levelDisplay;
	private boolean isPaused = false;

	public GameForm() {
		initComponents(600, 450);
		initControls();
	}

	private void initControls() {
		InputMap im = this.getRootPane().getInputMap();
		ActionMap am = this.getRootPane().getActionMap();

		im.put(KeyStroke.getKeyStroke("RIGHT"), "right"); // d
		im.put(KeyStroke.getKeyStroke("LEFT"), "left"); // a
		im.put(KeyStroke.getKeyStroke("UP"), "up"); // w
		im.put(KeyStroke.getKeyStroke("DOWN"), "downOneLine"); // s
		im.put(KeyStroke.getKeyStroke("SPACE"), "downToEnd"); // enter
		im.put(KeyStroke.getKeyStroke("Q"), "quit"); // q
		im.put(KeyStroke.getKeyStroke("E"), "exit"); // e
		
		im.put(KeyStroke.getKeyStroke("ESCAPE"), "back");

		am.put("right", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isPaused)
					ga.moveBlockRight();
			}
		});

		am.put("left", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isPaused)
					ga.moveBlockLeft();
			}
		});

		am.put("up", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isPaused)
					ga.rotateBlock();
			}
		});

		am.put("downOneLine", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isPaused)
					ga.moveBlockDown();
			}
		});

		am.put("downToEnd", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isPaused)
					ga.dropBlock();
			}
		});

		am.put("quit", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isPaused) {
					isPaused = true;
					gt.pause();
				} else {
					isPaused = false;
					gt.reStart();
				}
			}
		});
		
		am.put("exit", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gt.interrupt(); // 게임 스레드 종료 

				setVisible(false);
				Tetris.showStartup();

			}
		});
		
		am.put("back", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gt.interrupt(); // 게임 스레드 종료 

				setVisible(false);
				Tetris.showStartup();
			}
		});
	}

	// 게임 스레드 시작
	public void startGame() {
		// 게임이 다시 시작될 때마다 초기화 되어야 하는 것들을 초기화한다. 
		ga.initGameArea(); 
		nba.initNextBlockArea(); 
		
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

	public void initComponents(int w, int h) {
		initThisFrame(w, h);
		initDisplay(w, h);
		
		ga = new GameArea(w, h, 10);
		this.add(ga);

		nba = new NextBlockArea(w, h, ga);
		this.add(nba);
	}

	// 이 판넬 화면 설정
	private void initThisFrame(int w, int h) {
		this.setSize(w, h);
		this.setResizable(false);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(false);
	}

	private void initDisplay(int w, int h) {
		scoreDisplay = new JLabel("Score: 0");
		levelDisplay = new JLabel("Level: 0");
		scoreDisplay.setBounds(w - (w/5), h / 20, 100, 30);
		levelDisplay.setBounds(w - (w/5), h / 20 + 20, 100, 30);
		this.add(scoreDisplay);
		this.add(levelDisplay);
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