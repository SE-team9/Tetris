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
		
		sf.initComponents(w, h); // 크기 조절 
		sf.setVisible(true); // 시작 화면 띄우기
		sf.getContentPane().repaint();
	}
	
	public static void start() {
		gf.getContentPane().removeAll();
		w = of.getFrameSize().width;
		h = of.getFrameSize().height;
		
		gf.initComponents(w, h); // 크기 조절 
		gf.initControls(of.getCurrentKeyMode()); // 조작키 설정
		gf.setVisible(true); // 게임 화면 띄우기
		
		gf.getContentPane().repaint();
		
		gf.startGame(); // 게임 스레드 시작 
	}
	
	// 다른 화면에서 설정 화면 띄울 때, 현재 확정된 설정 값으로 보여주기
	public static void showOption() {
		of.getContentPane().removeAll();
		w = of.getFrameSize().width;
		h = of.getFrameSize().height;
		
		of.initComponents(w, h); // 크기 조절, 칼럼 확정 
		
		of.setVisible(true); // 설정 화면 띄우기
		
		of.getContentPane().repaint();
	}
	
	public static void showLeaderboard() {
		lf.getContentPane().removeAll();
		w = of.getFrameSize().width;
		h = of.getFrameSize().height;
		
		lf.initComponents(w, h); // 프레임 크기, 칼럼 위치
		lf.updateTableWithMode(0); // 일반 모드 먼저 보여주기 
		
		lf.setVisible(true);
		lf.getContentPane().repaint();
	}
	
	public static void saveSettings() {
		of.saveSettings();
	}
	
	// 게임 모드
	public static int getGameMode() {
		if(sf == null) return 0;
		return sf.getCurrentGameMode();
	}
	
	// 조작 키 
	public static int getKeyMode() {
		if(of == null) return 0;
		return of.getCurrentKeyMode();
	}
	
	// 게임 난이도
	public static int getGameLevel() {
		if(of == null) return 0;
		return of.getCurrentGameLevel();
	}
	
	// 색상 모드
	public static int getColorMode() {
		if (of == null) return 0;
		return of.getCurrentColorMode();
	}

	// 게임 종료 (현재 모드, 이름, 점수, 난이도)
	public static void gameOver(int mode, int score, int levelMode) {
		// 유저 이름 입력 받기
		String name = JOptionPane.showInputDialog("Game Over!\n Please enter your name.");
		gf.setVisible(false);
		
		// 테이블에 데이터 추가
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
				
				// 여기서 모든 Form 객체 생성 (생성자 호출하여 초기화)
				sf = new StartupForm();
				of = new OptionForm(); // 객체를 새로 만들면 설정 값이 초기화 돼버린다! 
				gf = new GameForm();
				lf = new LeaderboardForm();
				
				sf.setVisible(true);
			}
		});
	}
}