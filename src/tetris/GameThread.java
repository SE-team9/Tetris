package tetris;

public class GameThread extends Thread {

	private boolean isPaused = false;

	private GameArea ga;
	private GameForm gf;
	private NextBlockArea nba;
	private int score = 0;
	private int level = 1;
	private int scorePerLevel = 30; // 30������ ���� ���

	private int interval = 1000;
	private int speedupPerLevel = 100;

	// ������ ������ ���õ� ������
	private int clearedLineNum; 			// ���� ������ ��� ������ �� ���� �����ϴ� ����
	private int cumClearedLine; 			// ������ �� ���� ���������ϴ� ����
	private boolean nextIsItemTurn = false; // ���� ���� ������ ������ Ȯ���ϴ� ����
	private boolean isItemTurn = false; 	// ���� ���� ������ ������ Ȯ���ϴ� ����

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

			while (true) {
				int gameLevel = Tetris.getGameLevel();
				
				ga.spawnBlock(); 						
					
				ga.updateNextBlock();					
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

				// ���� �� �����Դµ� ���� ��踦 �Ѿ� �ִ� ���� ���� ����
				if (ga.isBlockOutOfBounds()) {
					Tetris.gameOver(score, gameLevel);
					break;
				}

				// ���� ����ġ ��濡 ����
				ga.moveBlockToBackground();
				
				// �ϼ��� �� ����, ������ �� ���� ���� ���� �߰�
				if(ga.clearLines() > 1) {
					score += 2 * ga.clearLines() + level;
				}
				else {
					score += ga.clearLines() + level;
				}
				// ���� ������Ʈ
				gf.updateScore(score);

				
				// ���̵� ���� �߰�
				if (gameLevel == 0) {
					speedupPerLevel = 80;
				} else if (gameLevel == 2) {
					speedupPerLevel = 120;
				}
				
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
				int gameLevel = Tetris.getGameLevel();
				
				ga.spawnBlock();

				if (nextIsItemTurn) { 		// ���� ���� ������
					ga.updateNextItem(); 	// ���� ������ �� ����
					nba.setIsItem(true); 	// �������� �������� ǥ���ϱ� ���� ������ ������ �˷��ִ� �뵵
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

				// ���� ���� Ȯ��
				if (ga.isBlockOutOfBounds()) {
					Tetris.gameOver(score, gameLevel);
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
						nextIsItemTurn = false; 	// ���� ���� �⺻ ��
						isItemTurn = true; 			// ���� ���� ������
						nba.setIsItem(false);		// ���� ���� �������� �ƴ�
						ga.setIsItem(true); 		// ������� ������
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

				if(clearedLineNum > 1) {
					score += 2* clearedLineNum + level;
				}
				else {
					score += clearedLineNum + level;
				}
				gf.updateScore(score);

				// ���̵� ���� �߰�
				if (gameLevel == 0) {
					speedupPerLevel = 80;
				} else if (gameLevel == 2) {
					speedupPerLevel = 120;
				}
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

	public void pause() {
		this.isPaused = true;
	}

	public void reStart() {
		this.isPaused = false;
	}
}