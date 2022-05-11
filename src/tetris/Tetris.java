package tetris;

import java.io.File;
import java.io.FileInputStream;

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
	
	// �ٸ� ȭ�鿡�� ���� ȭ���� ��� ��, ���� Ȯ���� ���� ������ �����ֱ�
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
		
		lf.initComponents(w, h); // ������ ũ��
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
				
				// ���⼭ ���� ������ �ҷ��ͼ�, ��� �������� ũ�⸦ �ѹ��� �����غ���! 
				// ���Ͽ��� ���� �� �����ͼ� �迭 �ʱ�ȭ 
				try {
					File file = new File("settings.txt");
					if(!file.exists()) { 
						file.createNewFile(); 
						System.out.println("Create new file.");
					};
					
					FileInputStream fis = new FileInputStream(file);
					int data = fis.read();
					
					// ���Ͽ� ����� ���� ���� ũ�� ���� 
					if(data == 0) {
						w = 600;
						h = 450;
					}else if(data == 1) {
						w = 700;
						h = 550;
					}else {
						w = 800;
						h = 650;
					}
					
					fis.close();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// ���⼭ ��� Form ��ü ���� (������ ȣ���Ͽ� �ʱ�ȭ)
				sf = new StartupForm(w, h);
				of = new OptionForm(w, h); 
				gf = new GameForm(w, h);
				lf = new LeaderboardForm(w, h);
				
				sf.setVisible(true);
			}
		});
	}
}