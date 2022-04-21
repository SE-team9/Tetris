package form;
import tetris.*;

import java.awt.EventQueue;
import java.awt.TextArea;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

// TODO: 議곗옉 �궎 �꽕�젙 

public class GameForm extends JFrame {
	private int w, h;
	
	private GameArea ga;
	private GameThread gt;
	private NextBlockArea nba;
	private JLabel lblScore, lblLevel;
	private JTextArea keyManual;
	private boolean isPaused = false;

	// 泥섏쓬�뿉 �깮�꽦�옄 �샇異쒗븷 �븣�뒗 紐⑤몢 湲곕낯 媛믪쑝濡� 
	public GameForm() {
		this.w = 600;
		this.h = 450;
		
		initComponents(w, h);
		initControls(0); 
	}
	
	// Tetris�뿉�꽌 �쟾�떖 諛쏆� �씤�옄 媛믪뿉 �뵲�씪 �겕湲� 議곗젙 
	public void initComponents(int w, int h) {
		this.w = w;
		this.h = h;
		
		initThisFrame();
		initDisplay();
		
		ga = new GameArea(w, h, 10);
		this.add(ga);

		nba = new NextBlockArea(w, h, ga);
		this.add(nba);
	}

	private void initThisFrame() {
		this.setSize(w, h);
		this.setResizable(false);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(false);
	}

	private void initDisplay() {
		lblScore = new JLabel("Score: 0");
		lblLevel = new JLabel("Level: 0");
		lblScore.setBounds(w - (w/5), h / 20, 100, 30);
		lblLevel.setBounds(w - (w/5), h / 20 + 20, 100, 30);
		this.add(lblScore);
		this.add(lblLevel);
	}

	// Tetris�뿉�꽌 �쟾�떖 諛쏆� �씤�옄 媛믪뿉 �뵲�씪 議곗옉 �궎 蹂�寃쏀븯湲�
	public void initControls(int keyMode) {
		InputMap im = this.getRootPane().getInputMap();
		ActionMap am = this.getRootPane().getActionMap();
	
		if(keyMode == 0) {
			im.clear();
			
			im.put(KeyStroke.getKeyStroke("RIGHT"), "right");
			im.put(KeyStroke.getKeyStroke("LEFT"), "left");
			im.put(KeyStroke.getKeyStroke("UP"), "up");
			im.put(KeyStroke.getKeyStroke("DOWN"), "downOneLine");
			im.put(KeyStroke.getKeyStroke("SPACE"), "downToEnd");
			
			keyManual = new JTextArea("�쇊履� �씠�룞: �넀 \n"
					+ "�삤瑜몄そ �씠�룞: �넂 \n"
					+ "�븳移� �븘�옒濡� �씠�룞: �넃 \n"
					+ "釉붾윮 �쉶�쟾: �넁 \n"
					+ "�븳踰덉뿉 諛묒쑝濡� �씠�룞: SPACE \n"
					+ "寃뚯엫 �젙吏�/�옱媛�: q \n"
					+ "寃뚯엫 醫낅즺: e  \n");
		}
		else {
			im.clear(); // �떎瑜� �궎紐⑤뱶�뿉�꽌 �꽕�젙�뻽�뜕 嫄� 珥덇린�솕
			
			im.put(KeyStroke.getKeyStroke("D"), "right");
			im.put(KeyStroke.getKeyStroke("A"), "left");
			im.put(KeyStroke.getKeyStroke("W"), "up");
			im.put(KeyStroke.getKeyStroke("S"), "downOneLine");
			im.put(KeyStroke.getKeyStroke("ENTER"), "downToEnd");
			
			keyManual = new JTextArea("�쇊履� �씠�룞: a \n"
					+ "�삤瑜몄そ �씠�룞: d \n"
					+ "�븳移� �븘�옒濡� �씠�룞: s \n"
					+ "釉붾윮 �쉶�쟾: w \n"
					+ "�븳踰덉뿉 諛묒쑝濡� �씠�룞: ENTER \n"
					+ "寃뚯엫 �젙吏�/�옱媛�: q \n"
					+ "寃뚯엫 醫낅즺: e  \n");
		}
		
		// 怨듯넻 (以묒�, 醫낅즺, �뮘濡쒓�湲�)
		im.put(KeyStroke.getKeyStroke("Q"), "quit");
		im.put(KeyStroke.getKeyStroke("E"), "exit");
		im.put(KeyStroke.getKeyStroke("ESCAPE"), "back");
		
		keyManual.setBounds(w/30, h-300, 160, 140);
		keyManual.setFocusable(false);
		this.add(keyManual);
		
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
				gt.interrupt(); // 寃뚯엫 �뒪�젅�뱶 醫낅즺 

				setVisible(false);
				Tetris.showStartup();

			}
		});
		
		am.put("back", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gt.interrupt(); // 寃뚯엫 �뒪�젅�뱶 醫낅즺 

				setVisible(false);
				Tetris.showStartup();
			}
		});
	}

	// 寃뚯엫 �뒪�젅�뱶 �떆�옉
	public void startGame() {
		// 寃뚯엫�씠 �떎�떆 �떆�옉�맆 �븣留덈떎 珥덇린�솕 �릺�뼱�빞 �븯�뒗 寃껊뱾�쓣 珥덇린�솕�븳�떎. 
		ga.initGameArea(); 
		nba.initNextBlockArea(); 
		
		// 寃뚯엫 �뒪�젅�뱶 �떆�옉
		gt = new GameThread(ga, this, nba);
		gt.start();
	}

	public void updateScore(int score) {
		lblScore.setText("Score: " + score);
	}

	public void updateLevel(int level) {
		lblLevel.setText("Level: " + level);
	}
	
	// GameForm �봽�젅�엫 �떎�뻾
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new GameForm().setVisible(true);
			}
		});
	}

}