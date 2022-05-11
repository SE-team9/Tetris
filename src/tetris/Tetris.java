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
	
	// 다른 화면에서 설정 화면을 띄울 때, 현재 확정된 설정 값으로 보여주기
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
		
		lf.initComponents(w, h); // 프레임 크기
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
				
				// 여기서 설정 파일을 불러와서, 모든 프레임의 크기를 한번에 조절해보자! 
				// 파일에서 설정 값 가져와서 배열 초기화 
				try {
					File file = new File("settings.txt");
					if(!file.exists()) { 
						file.createNewFile(); 
						System.out.println("Create new file.");
					};
					
					FileInputStream fis = new FileInputStream(file);
					int data = fis.read();
					
					// 파일에 저장된 값에 따라 크기 조절 
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
				
				// 여기서 모든 Form 객체 생성 (생성자 호출하여 초기화)
				sf = new StartupForm(w, h);
				of = new OptionForm(w, h); 
				gf = new GameForm(w, h);
				lf = new LeaderboardForm(w, h);
				
				sf.setVisible(true);
			}
		});
	}
}