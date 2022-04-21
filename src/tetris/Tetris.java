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
	
	// ���� ���� ��� ��ȯ (�⺻, ������) 
	public static int getGameMode() {
		if(sf == null) return 0;
		return sf.getCurrentGameMode();
	}

	// ���� ���� ��� ��ȯ 
	public static int getColorMode() {
		if (of == null) return 0;
		return of.getCurrentColorMode();
	}
	
	// ���� ���� ���̵� ��ȯ
	public static int getGameLevel() {
		if(of == null) return 0;
		return of.getCurrentGameLevel();
	}
	
	// ���� ���� ��, ���� ������ ��� ���ھ�忡 �߰�
	public static void gameOver(int score, int gameLevel) {
		String playerName = JOptionPane.showInputDialog("Game Over!\n Please enter your name.");
		gf.setVisible(false);
		lf.addPlayer(playerName, score, gameLevel);
	}

	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				// ó���� ��ü ������ ���� �⺻ ũ��! 
				// �Լ��� ȣ���Ͽ� ȭ���� ��� ������ �������� ���õ� �ʺ�� ���� �� ��������
				sf = new StartupForm();
				of = new OptionForm();				
				gf = new GameForm();
				lf = new LeaderboardForm();
				
				sf.setVisible(true);
			}
		});
	}
}