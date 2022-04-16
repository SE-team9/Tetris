package tetris;

public class GameThread extends Thread {
	private GameArea ga;
	private GameForm gf;
	private NextBlockArea nba;

	private int score = 0;
	private int level = 1;
	private int scorePerLevel = 50;

	private int pause = 1000;
	private int speedupPerLevel = 50;

	private boolean isPaused = false;

	// 아이템 생성과 관련된 변수들  
	private int clearedLineNum;  // 줄이 삭제된 경우 삭제된 줄 수를 저장하는 변수 
	private int cumClearedLine;  // 삭제된 줄 수를 누적저장하는 변수 
	private boolean nextIsItemTurn = false; // 다음 블럭이 아이템 블럭인지 확인하는 변수 
	private boolean isItemTurn = false; // 현재 블럭이 아이템 블럭인지 확인하는 변수
	
	public GameThread(GameArea ga, GameForm gf, NextBlockArea nba) {
		this.ga = ga;
		this.gf = gf;
		this.nba = nba;

		gf.updateScore(score);
		gf.updateLevel(level);
	}

	@Override
	public void run() {

		// 일반모드
		if (Tetris.getGameMode() == 0) {

			// 블록이 1초마다 1칸씩 떨어지도록
			while (true) {
				ga.spawnBlock(); // 새로운 블록 생성

				// 다음 블럭 설정
				ga.setNextBlock();
				nba.setNextBlock(ga.getNextBlock());

				while (ga.moveBlockDown()) {
					try {
						// 점수 업데이트
						score++;
						gf.updateScore(score);

						// 0.1초마다 pause키가 눌렸는지 확인
						// pause키가 눌렸으면 루프를 돌면서 대기
						int i = 0;
						while (i < pause / 100) {
							Thread.sleep(100);
							i++;
							while (isPaused) {
								if (!isPaused) {
									break;
								}
							}
						}

					} catch (InterruptedException e) {
						// 스레드가 종료 되어도 예외 메세지를 출력하지 않음
						return;
					}
				}

				// 블럭이 다 내려왔는데 위쪽 경계를 넘어 있는 경우는 게임 종료
				if (ga.isBlockOutOfBounds()) {
					Tetris.gameOver(score);
					break;
				}

				// 현재 블럭위치 배경에 저장
				ga.moveBlockToBackground();
				// 완성된 줄 삭제, 점수 추가
				score += ga.clearLines();
				// 점수 업데이트
				gf.updateScore(score);

				// 레벨 업데이트 레벨이 증가할수록 블럭이 내려오는 속도 증가
				int lvl = score / scorePerLevel + 1;
				if (lvl > level) {
					level = lvl;
					gf.updateLevel(level);
					pause -= speedupPerLevel;
				}
			}
		}
		else {  // 아이템모드
			
			while (true) {

				ga.spawnBlock();

				if (nextIsItemTurn) {  // 다음 블럭이 아이템이어야 하면 
					
					ga.setNextItem();	// 다음 아이템 블럭 선택
					nba.setIsItem(true);  // 아이템은 원형으로 표시하기 위해 아이템 블럭임을 알려주는 용도
					
				} else {
					ga.setNextBlock();  // 다음 블럭 선택
				}
				
				nba.setNextBlock(ga.getNextBlock());  // 다음 블럭을 표시할 수 있도록 다음 블럭 정보 전달

				while (ga.moveBlockDown()) {
					try {
						// 점수 추가
						score++;
						gf.updateScore(score);

						int i = 0;
						while (i < pause / 100) {
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
					Tetris.gameOver(score);
					break;
				}

				// 현재 블럭이 아이템이면 아이템을 반짝거리고 해당 아이템의 동작을 수행한다.
				if (isItemTurn) {
					ga.twinkleItem();
					ga.itemFunction();

					// 이제 현재 블럭이 기본 블럭임을 나타내기 위해 불린값 조정 
					ga.setIsItem(false); 
					isItemTurn = false; 
					
				} else { // 현재 블럭이 아이템이 아니면 현재 블럭을 배경으로 옮긴다.
					ga.moveBlockToBackground();

					// 다음 블럭이 아이템이었다면 이제 현재 블럭이 아이템이 되고, 다음 블럭은 기본 블럭이 되어야 하므로, 
					// 현재 블럭은 원형으로, 다음 블럭은 사각형으로 표시하기 위해 각 불린값들을 조정해준다.
					if (nextIsItemTurn) { 
						nextIsItemTurn = false; // 다음 블럭은 기본 블럭
						isItemTurn = true;  // 현재 블럭은 아이템
						nba.setIsItem(false); // 다음 블럭은 아이템이 아님
						ga.setIsItem(true);  // 현재블럭은 아이템
					}
				}

				// 현재 블럭이 바닥에 닿았을 때, 완성된 줄을 삭제하고, 삭제된 줄 수 저장
				clearedLineNum = ga.clearLines();

				// 줄이 특정 횟수 삭제되면 아이템 생성
				// 3을 10으로 고치면 10줄이 삭제될 때마다 아이템이 생성됩니다.
				// 동작을 쉽게 확인하기 위해 3줄 마다 아이템이 나오도록 3으로 설정해뒀습니다.
				if (cumClearedLine / 3 != (cumClearedLine + clearedLineNum) / 3) {
					nextIsItemTurn = true; 
				}

				cumClearedLine += clearedLineNum;

				score += clearedLineNum;
				gf.updateScore(score);

				int lvl = score / scorePerLevel + 1;
				if (lvl > level) {
					level = lvl;
					gf.updateLevel(level);
					if (pause > 300) {
						pause -= speedupPerLevel;
					}
				}
			}
		}
	}

	// 스레드 pause
	public void pause() {
		this.isPaused = true;
	}

	// 스레드 재시작
	public void reStart() {
		this.isPaused = false;
	}
}
