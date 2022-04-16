package tetris;

public class GameThread extends Thread {
	private GameArea ga;
	private GameForm gf;
	private NextBlockArea nba;
	private int score = 0;
	private int level = 1;
	private int scorePerLevel = 3; // 3���� ���� �����Ǹ� ���� ���
	private int interval = 1000;
	private int speedupPerLevel = 100;

	public GameThread(GameArea ga, GameForm gf, NextBlockArea nba) {
		this.ga = ga;
		this.gf = gf;
		this.nba = nba;
		
		gf.updateScore(score);
		gf.updateLevel(level);
	}

	@Override
	public void run() {
		while (true) {
			ga.spawnBlock(); // ���ο� ��� ����
			ga.updateNextBlock(); // ���� ��� �̸� �����ֱ�
			nba.updateNBA(ga.getNextBlock());
			
			// ��ĭ�� ������ ������ sleep���� ���� �ð� �ɱ� 
			while(ga.moveBlockDown()) {
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					return; // ������ ���ͷ�Ʈ �Ǹ� run �Լ� ���� 
				}
			}

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
}
