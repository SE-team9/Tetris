package tetris;

public class GameThread extends Thread {
	private GameArea ga;
	private GameForm gf;
	private NextBlockArea nba;
	private int score = 0;
	private int level = 1;
	private int scorePerLevel = 3;
	private int interval = 1000;
	private int speedupPerLevel = 50;
	private boolean isPaused = false;

	public GameThread(GameArea ga, GameForm gf, NextBlockArea nba) {
		this.ga = ga;
		this.gf = gf;
		this.nba = nba;

		gf.updateScore(score);
		gf.updateLevel(level);
	}

	@Override // 블럭이 떨어지는 동작을 무한 반복 
	public void run() { // 게임 스레드 시작 
		while (true) {
			ga.spawnBlock(); // 새로운 블록 생성
			ga.updateNextBlock(); // 다음 블록 미리 보여주기
			nba.updateNBA(ga.getNextBlock());
			
			while(ga.moveBlockDown(isPaused)) {	
				try {
					// 점수 업데이트
					score++;
					gf.updateScore(score);

					// 0.1초마다 pause 키가 눌렸는지 확인
					int i = 0;
					while (i < interval / 100) {
						Thread.sleep(100);
						i++;
						
						// pause 키가 눌리면 루프 돌면서 대기 
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
			
			// 버그 발견: 블록이 보드판에 아직 진입하지 않은 상태에서 exit 하면, 게임 오버로 인식해서 점수판을 띄워버림.
			// 근데 앞으로는 점수가 순위권에 들 때만 스코어보드 띄울 거니까 괜찮음.
			// 쌓인 블록들이 보드판 경계를 넘어가면 게임 종료 
			if (ga.isBlockOutOfBounds()) {
				Tetris.gameOver(score);
				break;
			}

			// 보드판 경계를 넘지 않은 경우, 백그라운드 블록으로 전환 
			ga.moveBlockToBackground();
			
			// 삭제된 행의 개수만큼 점수 증가
			score += ga.clearLines();
			gf.updateScore(score);

			// scorePerLevel만큼 점수 얻으면 레벨 상승 
		 	int lvl = score / scorePerLevel + 1;
		 	if(lvl > level) {
		 		level = lvl;
		 		gf.updateLevel(level);
		 		interval -= speedupPerLevel; // 속도 증가
		 	}
		}
	}

	public void pause() {
		this.isPaused = true;
	}

	public void reStart() {
		this.isPaused = false;
	}
}
