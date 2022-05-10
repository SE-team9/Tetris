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
		
		sf.initComponents(w, h); // ũ�� ���� 
		sf.setVisible(true); // ���� ȭ�� ����
		sf.getContentPane().repaint();
	}
	
	public static void start() {
		gf.getContentPane().removeAll();
		w = of.getFrameSize().width;
		h = of.getFrameSize().height;
		
		gf.initComponents(w, h); // ũ�� ���� 
		gf.initControls(of.getCurrentKeyMode()); // ����Ű ����
		gf.setVisible(true); // ���� ȭ�� ����
		
		gf.getContentPane().repaint();
		
		gf.startGame(); // ���� ������ ���� 
	}
	
	// �ٸ� ȭ�鿡�� ���� ȭ�� ��� ��, ���� Ȯ���� ���� ������ �����ֱ�
	public static void showOption() {
		of.getContentPane().removeAll();
		w = of.getFrameSize().width;
		h = of.getFrameSize().height;
		
		of.initComponents(w, h); // ũ�� ����, Į�� Ȯ�� 
		
		of.setVisible(true); // ���� ȭ�� ����
		
		of.getContentPane().repaint();
	}
	
	public static void showLeaderboard() {
		lf.getContentPane().removeAll();
		w = of.getFrameSize().width;
		h = of.getFrameSize().height;
		
		lf.initComponents(w, h); // ������ ũ��, Į�� ��ġ
		lf.updateTableWithMode(0); // �Ϲ� ��� ���� �����ֱ� 
		
		lf.setVisible(true);
		lf.getContentPane().repaint();
	}
	
	public static void saveSettings() {
		of.saveSettings();
	}
	
	// ���� ���
	public static int getGameMode() {
		if(sf == null) return 0;
		return sf.getCurrentGameMode();
	}
	
	// ���� Ű 
	public static int getKeyMode() {
		if(of == null) return 0;
		return of.getCurrentKeyMode();
	}
	
	// ���� ���̵�
	public static int getGameLevel() {
		if(of == null) return 0;
		return of.getCurrentGameLevel();
	}
	
	// ���� ���
	public static int getColorMode() {
		if (of == null) return 0;
		return of.getCurrentColorMode();
	}

	// ���� ���� (���� ���, �̸�, ����, ���̵�)
	public static void gameOver(int mode, int score, int levelMode) {
		// ���� �̸� �Է� �ޱ�
		String name = JOptionPane.showInputDialog("Game Over!\n Please enter your name.");
		gf.setVisible(false);
		
		// ���̺� ������ �߰�
		switch(levelMode) {
		case 0:
			lf.addPlayer(mode, name, score, "Easy");
			break;
		case 1:
			lf.addPlayer(mode, name, score, "Normal");
			break;
		case 2:
			lf.addPlayer(mode, name, score, "Hard");
			break;
		}
	}

	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				// ���⼭ ��� Form ��ü ���� (������ ȣ���Ͽ� �ʱ�ȭ)
				sf = new StartupForm();
				of = new OptionForm(); // ��ü�� ���� ����� ���� ���� �ʱ�ȭ �Ź�����! 
				gf = new GameForm();
				lf = new LeaderboardForm();
				
				sf.setVisible(true);
			}
		});
	}
}