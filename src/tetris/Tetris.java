package tetris;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import form.GameForm;
import form.LeaderboardForm;
import form.OptionForm;
import form.StartupForm;

public class Tetris {
	private static GameForm gf;
	private static StartupForm sf;
	private static LeaderboardForm lf;
	private static OptionForm of;
	
	public static void start() {
		gf.setVisible(true);
		gf.startGame();
	}
	
	public static void showLeaderboard() {
		lf.setVisible(true);
	}

	public static void showStartup() {
		sf.setVisible(true);
	}
	
	public static void showOption() {
		of.setVisible(true);
	}
	
	// 현재 게임 모드 반환
	public static int getColorMode() {
		if (of == null)
			return 0;
		return of.getCurrentColorMode();
	}
	
	// 현재 게임 난이도 반환
	public static int getGameLevel() {
		if(of==null)
			return 0;
		return of.getCurrentGameLevel();
	}
	
	// 현재 블럭 색 모드 반환
	public static int getGameMode() {
		if(sf == null)
			return 0;
		
		return sf.getCurrentGameMode();
	}
	
	public static void gameOver(int score, int gameLevel) {
		String playerName = JOptionPane.showInputDialog("Game Over!\n Please enter your name.");
		gf.setVisible(false);
		lf.addPlayer(playerName, score, gameLevel);
	}

	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				sf = new StartupForm();
				gf = new GameForm();
				lf = new LeaderboardForm();
				of = new OptionForm();
				
				sf.setVisible(true);
			}
		});
	}
}