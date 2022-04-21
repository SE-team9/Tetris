package tetris;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import form.GameForm;
import form.LeaderboardForm;
import form.OptionForm;
import form.StartupForm;

public class Tetris {
	private static int w, h;
	private static StartupForm sf;
	private static GameForm gf;
	private static OptionForm of;
	private static LeaderboardForm lf;
	
	public static void showStartup() {
		sf.getContentPane().removeAll();
		w = of.getFrameSize().width;
		h = of.getFrameSize().height;
		
		sf.initComponents(w, h);
		
		sf.setVisible(true);
		sf.getContentPane().repaint();
	}
	
	public static void start() {
		gf.getContentPane().removeAll();
		w = of.getFrameSize().width;
		h = of.getFrameSize().height;
		
		gf.initComponents(w, h);
		
		gf.setVisible(true);
		gf.getContentPane().repaint();
		
		gf.startGame();
	}
	
	public static void showOption() {
		of.getContentPane().removeAll();
		w = of.getFrameSize().width;
		h = of.getFrameSize().height;
		
		of.initComponents(w, h);
		
		of.setVisible(true);
		of.getContentPane().repaint();
	}
	
	public static void showLeaderboard() {
		lf.getContentPane().removeAll();
		w = of.getFrameSize().width;
		h = of.getFrameSize().height;
		
		lf.initComponents(w, h);
		
		lf.setVisible(true);
		lf.getContentPane().repaint();
	}
	
	// 현재 게임 모드 반환 (기본, 아이템) 
	public static int getGameMode() {
		if(sf == null) return 0;
		return sf.getCurrentGameMode();
	}

	// 현재 색상 모드 반환 
	public static int getColorMode() {
		if (of == null) return 0;
		return of.getCurrentColorMode();
	}
	
	// 현재 게임 난이도 반환
	public static int getGameLevel() {
		if(of == null) return 0;
		return of.getCurrentGameLevel();
	}
	
	// 게임 종료 시, 현재 유저의 기록 스코어보드에 추가
	public static void gameOver(int score, int gameLevel) {
		String playerName = JOptionPane.showInputDialog("Game Over!\n Please enter your name.");
		gf.setVisible(false);
		lf.addPlayer(playerName, score, gameLevel);
	}

	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				// 처음에 객체 생성할 때는 기본 크기! 
				// 함수를 호출하여 화면을 띄울 때마다 설정에서 세팅된 너비와 높이 값 가져오기
				sf = new StartupForm();
				of = new OptionForm();				
				gf = new GameForm();
				lf = new LeaderboardForm();
				
				sf.setVisible(true);
			}
		});
	}
}