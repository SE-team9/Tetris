package tetris;

public class GameThread extends Thread {
	private GameArea ga;
	private GameForm gf;

	private int score = 0;
	private int level = 1;
	private int scorePerLevel = 50;

	private int pause = 1000;
	private int speedupPerLevel = 50;
	
	private boolean isPaused = false;

	public GameThread(GameArea ga, GameForm gf) {
		this.ga = ga;
		this.gf = gf;

		gf.updateScore(score);
		gf.updateLevel(level);
	}

	@Override
	public void run() {

		// 블록이 1초마다 1칸씩 떨어지도록
		while (true) {
			ga.spawnBlock(); // 새로운 블록 생성

			while (ga.moveBlockDown()) {
				try {
					// 점수 업데이트
					score++;
					gf.updateScore(score);

					// 0.1초마다 pause키가 눌렸는지 확인
					// pause키가 눌렸으면 루프를 돌면서 대기 
					int i = 0;
					while(i<pause / 100) {
						Thread.sleep(100);
						i++;
						while(isPaused) {
							if(!isPaused) {
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
	
	// 스레드 pause
	public void pause() {
		this.isPaused = true;
	}
	
	// 스레드 재시작
	public void reStart() {
		this.isPaused = false;
	}
}
