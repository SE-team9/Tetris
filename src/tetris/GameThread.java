package tetris;

public class GameThread extends Thread {
	private GameArea ga;
	private GameForm gf;
	private NextBlockArea nba;
	private int score = 0;
	private int level = 1;
	private int scorePerLevel = 30; // 30������ ���� ���

	private int interval = 1000;
	private int speedupPerLevel = 100;

	// ������ ������ ���õ� ������
	private int clearedLineNum; // ���� ������ ��� ������ �� ���� �����ϴ� ����
	private int cumClearedLine; // ������ �� ���� ���������ϴ� ����
	private boolean nextIsItemTurn = false; // ���� ���� ������ ������ Ȯ���ϴ� ����
	private boolean isItemTurn = false; // ���� ���� ������ ������ Ȯ���ϴ� ����

	public GameThread(GameArea ga, GameForm gf, NextBlockArea nba) {
		this.ga = ga;
		this.gf = gf;
		this.nba = nba;

		gf.updateScore(score);
		gf.updateLevel(level);
	}

	@Override
	public void run() {

		// �Ϲݸ��
		if (Tetris.getGameMode() == 0) {

			// ����� 1�ʸ��� 1ĭ�� ����������
			while (true) {
				ga.spawnBlock(); // ���ο� ��� ����

				// ���� �� ����
				ga.updateNextBlock();
				nba.updateNBA(ga.getNextBlock());

				while (ga.moveBlockDown()) {

					try {
						// ���� ������Ʈ
						score++;
						gf.updateScore(score);

						Thread.sleep(interval);

					} catch (InterruptedException e) {
						return; // ������ ���ͷ�Ʈ �Ǹ� run �Լ� ����
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
					if (interval > 300) {
						interval -= speedupPerLevel;
					}
				}
			}
		} else { // �����۸��

			while (true) {

				ga.spawnBlock();

				if (nextIsItemTurn) { // ���� ���� �������̾�� �ϸ�

					ga.setNextItem(); // ���� ������ �� ����
					nba.setIsItem(true); // �������� �������� ǥ���ϱ� ���� ������ ������ �˷��ִ� �뵵

				} else {
					ga.updateNextBlock(); // ���� �� ����
				}

				nba.updateNBA(ga.getNextBlock()); // ���� ���� ǥ���� �� �ֵ��� ���� �� ���� ����

				while (ga.moveBlockDown()) {

					try {
						// ���� ������Ʈ
						score++;
						gf.updateScore(score);

						Thread.sleep(interval);

					} catch (InterruptedException e) {
						return; // ������ ���ͷ�Ʈ �Ǹ� run �Լ� ����
					}
				}

				// ���� ���� Ȯ��
				if (ga.isBlockOutOfBounds()) {
					Tetris.gameOver(score);
					break;
				}

				// ���� ���� �������̸� �������� ��¦�Ÿ��� �ش� �������� ������ �����Ѵ�.
				if (isItemTurn) {
					ga.twinkleItem();
					ga.itemFunction();

					// ���� ���� ���� �⺻ ������ ��Ÿ���� ���� �Ҹ��� ����
					ga.setIsItem(false);
					isItemTurn = false;

				} else { // ���� ���� �������� �ƴϸ� ���� ���� ������� �ű��.
					ga.moveBlockToBackground();

					// ���� ���� �������̾��ٸ� ���� ���� ���� �������� �ǰ�, ���� ���� �⺻ ���� �Ǿ�� �ϹǷ�,
					// ���� ���� ��������, ���� ���� �簢������ ǥ���ϱ� ���� �� �Ҹ������� �������ش�.
					if (nextIsItemTurn) {
						nextIsItemTurn = false; // ���� ���� �⺻ ��
						isItemTurn = true; // ���� ���� ������
						nba.setIsItem(false); // ���� ���� �������� �ƴ�
						ga.setIsItem(true); // ������� ������
					}
				}

				// ���� ���� �ٴڿ� ����� ��, �ϼ��� ���� �����ϰ�, ������ �� �� ����
				clearedLineNum = ga.clearLines();

				// ���� Ư�� Ƚ�� �����Ǹ� ������ ����
				// 3�� 10���� ��ġ�� 10���� ������ ������ �������� �����˴ϴ�.
				// ������ ���� Ȯ���ϱ� ���� 3�� ���� �������� �������� 3���� �����ص׽��ϴ�.
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
					if (interval > 300) {
						interval -= speedupPerLevel;
					}
				}
			}
		}
	}
}
