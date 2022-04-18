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

	@Override // ���� �������� ������ ���� �ݺ� 
	public void run() { // ���� ������ ���� 
		while (true) {
			ga.spawnBlock(); // ���ο� ��� ����
			ga.updateNextBlock(); // ���� ��� �̸� �����ֱ�
			nba.updateNBA(ga.getNextBlock());
			
			while(ga.moveBlockDown(isPaused)) {	
				try {
					// ���� ������Ʈ
					score++;
					gf.updateScore(score);

					// 0.1�ʸ��� pause Ű�� ���ȴ��� Ȯ��
					int i = 0;
					while (i < interval / 100) {
						Thread.sleep(100);
						i++;
						
						// pause Ű�� ������ ���� ���鼭 ��� 
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
			
			// ���� �߰�: ����� �����ǿ� ���� �������� ���� ���¿��� exit �ϸ�, ���� ������ �ν��ؼ� �������� �������.
			// �ٵ� �����δ� ������ �����ǿ� �� ���� ���ھ�� ��� �Ŵϱ� ������.
			// ���� ��ϵ��� ������ ��踦 �Ѿ�� ���� ���� 
			if (ga.isBlockOutOfBounds()) {
				Tetris.gameOver(score);
				break;
			}

			// ������ ��踦 ���� ���� ���, ��׶��� ������� ��ȯ 
			ga.moveBlockToBackground();
			
			// ������ ���� ������ŭ ���� ����
			score += ga.clearLines();
			gf.updateScore(score);

			// scorePerLevel��ŭ ���� ������ ���� ��� 
		 	int lvl = score / scorePerLevel + 1;
		 	if(lvl > level) {
		 		level = lvl;
		 		gf.updateLevel(level);
		 		interval -= speedupPerLevel; // �ӵ� ����
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
