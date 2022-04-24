package tetris;
import form.GameForm;

public class GameThread extends Thread {
	private GameArea ga;
	private GameForm gf;
	private NextBlockArea nba;
	private int score = 0; // 한 단위씩 계속 증가 
	private int level = 1; // 삭제한 줄 개수에 따라 레벨 상승
	private int linePerLevel = 7;
	private int interval = 1000; // sleep 시간 
	private int speedupPerLevel = 100;
	private boolean isPaused = false;
	private int levelMode; // 설정 화면에서 정한 게임 난이도

	// 아이템 생성과 관련된 변수들
	private int clearedLineNum; 	    // 줄이 삭제된 경우 삭제된 줄 수를 저장하는 변수
	private int totalClearedLine; 	    // 삭제된 줄 수를 누적 저장하는 변수
	private boolean nextIsItem = false; // 다음 블럭이 아이템 블럭인지 확인하는 변수
	private boolean isItem = false; 	// 현재 블럭이 아이템 블럭인지 확인하는 변수

	public GameThread(GameArea ga, GameForm gf, NextBlockArea nba) {
		this.ga = ga;
		this.gf = gf;
		this.nba = nba;

		gf.updateScore(score);
		gf.updateLevel(level);

		levelMode = Tetris.getGameLevel();
		
		// 난이도 조절 추가
		if (levelMode == 0) {
			speedupPerLevel = 80;
		} else if (levelMode == 2) {
			speedupPerLevel = 120;
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

			while (ga.moveBlockDown()) {
				try {
					score++;
					gf.updateScore(score);

					int i = 0;
					while (i < interval / 100) {
						Thread.sleep(100); // 0.1초마다 pause키가 눌렸는지 확인
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

			// 블럭이 다 내려왔는데 위쪽 경계를 넘어 있는 경우는 게임 종료
			if (ga.isBlockOutOfBounds()) {
				int gameMode = Tetris.getGameMode();
				Tetris.gameOver(gameMode, score, levelMode);
				break;
			}

			// 현재 블럭위치 배경에 저장
			ga.moveBlockToBackground();
			
			// 두 줄 이상 삭제되면 추가 점수 획득 
			if(ga.clearLines() > 1) {
				score += 2 * ga.clearLines() + level;
			}
			else {
				// 기본 점수 (+ 레벨에 따라 추가 점수 획득)
				score += ga.clearLines() + level;
			}
			
			// 점수 업데이트
			gf.updateScore(score);
			
			// 레벨 업데이트, 레벨이 증가할수록 블럭이 내려오는 속도 증가
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

	private void startItemMode() {
		while (true) {
			ga.spawnBlock();

			if (nextIsItem) { 		// 다음 블럭이 아이템
				ga.updateNextItem(); 	// 다음 아이템 블럭 설정
				nba.setIsItem(true); 	// 아이템은 원형으로 표시하기 위해 아이템 블럭임을 알려주는 용도
			} else {
				ga.updateNextBlock(); 	
			}

			nba.updateNBA(ga.getNextBlock()); 

			while (ga.moveBlockDown()) {

				try {
					score++;
					gf.updateScore(score);

					int i = 0;
					while (i < interval / 100) {
						Thread.sleep(100);
						i++;
						while (isPaused) {
							if (!isPaused) {
								break;
							}
						}
					}

				} catch (InterruptedException ex) {
					return;
				}
			}

			// 게임 종료 확인
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
					nextIsItem = false; 	// 다음 블럭은 기본 블럭
					isItem = true; 			// 현재 블럭은 아이템
					nba.setIsItem(false);		// 다음 블럭은 아이템이 아님
					ga.setIsItem(true); 		// 현재블럭은 아이템
				}
			}

			// 현재 블럭이 바닥에 닿았을 때, 완성된 줄을 삭제하고, 삭제된 줄 수 저장
			//clearedLineNum = ga.clearLines() + ga.oneLineDelte();
			clearedLineNum = ga.clearLines();

			// 줄이 특정 횟수 삭제되면 아이템 생성
			// 3을 10으로 고치면 10줄이 삭제될 때마다 아이템이 생성됩니다.
			// 동작을 쉽게 확인하기 위해 3줄 마다 아이템이 나오도록 3으로 설정해뒀습니다.
			if (totalClearedLine / 3 != (totalClearedLine + clearedLineNum) / 3) {
				nextIsItem = true;
			}

			totalClearedLine += clearedLineNum;

			if(clearedLineNum > 1) {
				score += 2* clearedLineNum + level;
			}
			else {
				score += clearedLineNum + level;
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

	public void pause() {
		this.isPaused = true;
	}

	public void reStart() {
		this.isPaused = false;
	}
	
	public boolean getIsPaused() {
		return this.isPaused;
	}
	
	public void scorePlus1() {
		score++;
		gf.updateScore(score);
	}
	
	public void scorePlus15() {
		score+=15;
		gf.updateScore(score);
	}
}