package tetris;
import form.GameForm;

public class GameThread extends Thread {
	private GameArea ga;
	private GameForm gf;
	private NextBlockArea nba;
	private int score = 0; // 한 단위씩 계속 증가 
	private int level = 1; // 삭제된 줄 개수에 따라 레벨 상승
	private int linePerLevel = 7;
	private int interval = 1000; // sleep 시간 
	private int speedupPerLevel;
	private boolean isPaused = false;
	private int levelMode; // 설정 화면에서 정한 게임 난이도

	// 아이템 생성과 관련된 변수들
	private int curClearedLines; 	    // 줄이 삭제된 경우 삭제된 줄 수를 저장하는 변수
	private int totalClearedLine; 	    // 삭제된 줄 수를 누적 저장하는 변수
	private boolean nextIsItem = false; // 다음 블럭이 아이템 블럭인지 확인하는 변수
	private boolean isItem = false; 	// 현재 블럭이 아이템 블럭인지 확인하는 변수
	private int linePerItem = 10;

	public GameThread(GameArea ga, GameForm gf, NextBlockArea nba) {
		this.ga = ga;
		this.gf = gf;
		this.nba = nba;

		gf.updateScore(score);
		gf.updateLevel(level);

		levelMode = Tetris.getGameLevel();
		switch(levelMode) {
		case 0: 
			speedupPerLevel = 80;
			break;
		case 1:
			speedupPerLevel = 100;
			break;
		case 2:
			speedupPerLevel = 120;
			break;
		}
	}

	@Override
	public void run() {
		if(Tetris.getGameMode() == 0) {
			startDefaultMode(); // 일반 모드
		}else {
			startItemMode(); // 아이템 모드
		}
	}

	private void startDefaultMode() {
		while (true) {
			ga.spawnBlock(); // 새로운 블럭 생성 
			ga.updateNextBlock(); // 다음 블럭 표시 	
			nba.updateNBA(ga.getNextBlock());

			// 블럭이 한칸씩 떨어질 때마다 한 단위씩 점수 증가
			while (ga.moveBlockDown()) {
				score+=level;
				gf.updateScore(score);
				checkPauseKey(); // 0.1초마다 pause키 확인
			}

			// 블럭이 끝까지 다 쌓이면 게임 종료
			if (ga.isBlockOutOfBounds()) {
				int gameMode = Tetris.getGameMode();
				Tetris.gameOver(gameMode, score, levelMode);
				break; // 루프 탈출
			}

			// 현재 블럭 위치를 배경에 저장
			ga.moveBlockToBackground();
			
			// 두 줄 이상 삭제되면 보너스 점수 획득
			if(curClearedLines > 1) {
				score += 2* curClearedLines + level;
			}
			else {
				// 기본 점수이지만, 레벨 상승에 따라 보너스 점수 획득 가능 
				score += curClearedLines + level;
			}
			gf.updateScore(score);
			
			// linePerLevel 만큼 줄 삭제하면 레벨 및 블럭 낙하 속도 증가
			int lvl = totalClearedLine / linePerLevel + 1;
			if (lvl > level) {
				level = lvl;
				gf.updateLevel(level);
				
				// 시간 간격이 300보다 클 때만 업데이트 (그 이하로는 떨어지지 않도록)
				if (interval > 300) {
					interval -= speedupPerLevel;
				}
			}
		}
	}

	private void startItemMode() {
		while (true) {
			ga.spawnBlock();
			
			if (nextIsItem) {
				ga.updateNextItem(); // 아이템 블럭으로 설정
				nba.setIsItem(true); // 아이템은 원형으로 표시하기 위한 플래그
			} else {
				ga.updateNextBlock(); // 일반 블럭으로 설정 
			}
			nba.updateNBA(ga.getNextBlock());

			// 블럭이 한칸씩 떨어질 때마다 한 단위씩 점수 증가
			while (ga.moveBlockDown()) {
				score+=level;
				gf.updateScore(score);
				checkPauseKey(); // 0.1초마다 pause키 확인
			}

			// 블럭이 끝까지 다 쌓이면 게임 종료
			if (ga.isBlockOutOfBounds()) {
				int gameMode = Tetris.getGameMode();
				Tetris.gameOver(gameMode, score, levelMode);
				break; // 루프 탈출
			}

			// 현재 블럭이 아이템이면 아이템을 반짝거리고 해당 아이템의 동작을 수행한다.
			if (isItem) {
				ga.twinkleItem();
				ga.itemFunction();

				// 이제 현재 블럭이 기본 블럭임을 나타내기 위해 불린값 조정
				ga.setIsItem(false);
				isItem = false;

			} else { // 현재 블럭이 아이템이 아니면 현재 블럭을 배경으로 옮긴다.
				ga.moveBlockToBackground();

				// 다음 블럭이 아이템이었다면 이제 현재 블럭이 아이템이 되고, 다음 블럭은 기본 블럭이 되어야 하므로,
				// 현재 블럭은 원형으로, 다음 블럭은 사각형으로 표시하기 위해 각 불린값들을 조정해준다.
				if (nextIsItem) {
					nextIsItem = false; // 다음 블럭은 기본 블럭
					nba.setIsItem(false);
					
					isItem = true; // 현재 블럭은 아이템
					ga.setIsItem(true); 
				}
			}

			// 현재 삭제된 줄의 수 리턴 
			curClearedLines = ga.clearLines();

			// linePerItem 만큼 줄이 삭제되면 아이템 등장 (원래는 10으로 해야 함)
			if (totalClearedLine / linePerItem != (totalClearedLine + curClearedLines) / linePerItem) {
				nextIsItem = true;
			}
			totalClearedLine += curClearedLines;
			
			if(curClearedLines > 1) {
				score += 2* curClearedLines + level;
			}
			else {
				score += curClearedLines + level;
			}
			gf.updateScore(score);
			
			int lvl = totalClearedLine / linePerLevel + 1;
			if (lvl > level) {
				level = lvl;
				gf.updateLevel(level);
				if (interval > 300) {
					interval -= speedupPerLevel;
				}
			}
		}
	}
	
	private void checkPauseKey() {
		try {
			int i = 0;
			while (i < interval / 100) {
				Thread.sleep(100); 
				i++;
				
				// 눌렸으면 루프 돌면서 대기
				while (isPaused) {
					if (!isPaused) {
						break;
					}
				}
			}
		} catch (InterruptedException ex) {
			return; // 게임 스레드 종료
		}
	}

	public void pause() {
		this.isPaused = true;
	}

	public void reStart() {
		this.isPaused = false;
	}
	
	public boolean getIsPaused() {
		return this.isPaused;
	}
	
	// 블럭 한칸 아래로 내렸을 때 1점 증가 
	public void scorePlus_level() {
		score+=level;
		gf.updateScore(score);
	}
	
	// 한번에 드롭했을 때 15점 증가
	public void scorePlus15() {
		score += 15;
		gf.updateScore(score);
	}
}