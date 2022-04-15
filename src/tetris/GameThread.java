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

		// ����� 1�ʸ��� 1ĭ�� ����������
		while (true) {
			ga.spawnBlock(); // ���ο� ��� ����

			while (ga.moveBlockDown()) {
				try {
					// ���� ������Ʈ
					score++;
					gf.updateScore(score);

					// 0.1�ʸ��� pauseŰ�� ���ȴ��� Ȯ��
					// pauseŰ�� �������� ������ ���鼭 ��� 
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
					// �����尡 ���� �Ǿ ���� �޼����� ������� ����
					return;
				}
			}

			// ���� �� �����Դµ� ���� ��踦 �Ѿ� �ִ� ���� ���� ����
			if (ga.isBlockOutOfBounds()) {
				Tetris.gameOver(score);
				break;
			}

			// ���� ����ġ ��濡 ����
			ga.moveBlockToBackground();
			// �ϼ��� �� ����, ���� �߰�
			score += ga.clearLines();
			// ���� ������Ʈ
			gf.updateScore(score);

			// ���� ������Ʈ ������ �����Ҽ��� ���� �������� �ӵ� ����
			int lvl = score / scorePerLevel + 1;
			if (lvl > level) {
				level = lvl;
				gf.updateLevel(level);
				pause -= speedupPerLevel;
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
