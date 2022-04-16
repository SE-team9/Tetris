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

	// ������ ������ ���õ� ������  
	private int clearedLineNum;  // ���� ������ ��� ������ �� ���� �����ϴ� ���� 
	private int cumClearedLine;  // ������ �� ���� ���������ϴ� ���� 
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
				ga.setNextBlock();
				nba.setNextBlock(ga.getNextBlock());

				while (ga.moveBlockDown()) {
					try {
						// ���� ������Ʈ
						score++;
						gf.updateScore(score);

						// 0.1�ʸ��� pauseŰ�� ���ȴ��� Ȯ��
						// pauseŰ�� �������� ������ ���鼭 ���
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
		else {  // �����۸��
			
			while (true) {

				ga.spawnBlock();

				if (nextIsItemTurn) {  // ���� ���� �������̾�� �ϸ� 
					
					ga.setNextItem();	// ���� ������ �� ����
					nba.setIsItem(true);  // �������� �������� ǥ���ϱ� ���� ������ ������ �˷��ִ� �뵵
					
				} else {
					ga.setNextBlock();  // ���� �� ����
				}
				
				nba.setNextBlock(ga.getNextBlock());  // ���� ���� ǥ���� �� �ֵ��� ���� �� ���� ����

				while (ga.moveBlockDown()) {
					try {
						// ���� �߰�
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
						isItemTurn = true;  // ���� ���� ������
						nba.setIsItem(false); // ���� ���� �������� �ƴ�
						ga.setIsItem(true);  // ������� ������
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
					if (pause > 300) {
						pause -= speedupPerLevel;
					}
				}
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
