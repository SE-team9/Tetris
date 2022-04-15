package tetris;

public class GameThread extends Thread {
	private GameArea ga;
	private GameForm gf;
	private NextBlockArea nba;
	private int score = 0;
	private int level = 1;
	private int scorePerLevel = 3; // 3���� ���� �����Ǹ� ���� ���
	private int pause = 1000;
	private int speedupPerLevel = 50;
	
	private boolean isPaused = false;

	public GameThread(GameArea ga, GameForm gf, NextBlockArea nba) {
		this.ga = ga;
		this.gf = gf;
		this.nba = nba;

		gf.updateScore(score);
		gf.updateLevel(level);
	}

	@Override
	public void run() {

		// ����� 1�ʸ��� 1ĭ�� ����������
		while (true) {
			ga.spawnBlock(); // ���ο� ��� ����
			
			// ���� �� ����
			ga.setNextBlock(); 
			nba.setNextBlock(ga.getNextBlock());
			
			while (ga.moveBlockDown()) {
				try {
					// Thread.sleep(pause);

					// 0.1�ʸ��� pauseŰ�� ���ȴ��� Ȯ��
					// pauseŰ�� �������� ������ ���鼭 ��� 
					int i = 0;
					while(i < pause / 100) {
						Thread.sleep(100);
						i++;
						while(isPaused) {
							if(!isPaused) {
								break;
							}
						}
					}
				} catch (InterruptedException e) {
					// ���� �޴� ��ư�� ������ GameThread�� ���ͷ�Ʈ �Ǹ� 
					// �� run �Լ��� ������ ����ǵ���!
					return; 
				}
			}

			// ���� ��ϵ��� ������ ��踦 �Ѿ�� ���� ���� 
			if (ga.isBlockOutOfBounds()) {
				Tetris.gameOver(score);
				break;
			}

			// ������ ��踦 ���� ���� ���, ��׶��� ������� ��ȯ 
			ga.moveBlockToBackground();
			
			// ������ ���� ���� ��ŭ ���� ����
			score += ga.clearLines();
			gf.updateScore(score);

			// scorePerLevel ��ŭ ���� ������ ���� ��� 
		 	int lvl = score / scorePerLevel + 1;
		 	if(lvl > level) {
		 		level = lvl;
		 		gf.updateLevel(level);
		 		pause -= speedupPerLevel; // �ӵ� ����
		 	}
		}
	}
	
	// ������ pause
	public void pause() {
		this.isPaused = true;
	}
	
	// ������ �����
	public void reStart() {
		this.isPaused = false;
	}
}
