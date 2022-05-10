package tetris;
import form.GameForm;

public class GameThread extends Thread {
	private GameArea ga;
	private GameForm gf;
	private NextBlockArea nba;
	private int score = 0; // �� ������ ��� ���� 
	private int level = 1; // ������ �� ������ ���� ���� ���
	private int linePerLevel = 7;
	private int interval = 1000; // sleep �ð� 
	private int speedupPerLevel = 100;
	private boolean isPaused = false;
	private int levelMode; // ���� ȭ�鿡�� ���� ���� ���̵�

	// ������ ������ ���õ� ������
	private int clearedLineNum; 	    // ���� ������ ��� ������ �� ���� �����ϴ� ����
	private int totalClearedLine; 	    // ������ �� ���� ���� �����ϴ� ����
	private boolean nextIsItem = false; // ���� ���� ������ ������ Ȯ���ϴ� ����
	private boolean isItem = false; 	// ���� ���� ������ ������ Ȯ���ϴ� ����

	public GameThread(GameArea ga, GameForm gf, NextBlockArea nba) {
		this.ga = ga;
		this.gf = gf;
		this.nba = nba;

		gf.updateScore(score);
		gf.updateLevel(level);

		levelMode = Tetris.getGameLevel();
		
		// ���̵� ���� �߰�
		if (levelMode == 0) {
			speedupPerLevel = 80;
		} else if (levelMode == 2) {
			speedupPerLevel = 120;
		}
	}

	@Override
	public void run() {
		if(Tetris.getGameMode() == 0) {
			startDefaultMode(); // �Ϲ� ���
		}else {
			startItemMode(); // ������ ���
		}
	}

	private void startDefaultMode() {
		while (true) {
			ga.spawnBlock(); // ���ο� �� ���� 
			ga.updateNextBlock(); // ���� �� ǥ�� 	
			nba.updateNBA(ga.getNextBlock());

			while (ga.moveBlockDown()) {
				try {
					score++;
					gf.updateScore(score);

					int i = 0;
					while (i < interval / 100) {
						Thread.sleep(100); // 0.1�ʸ��� pauseŰ�� ���ȴ��� Ȯ��
						i++;
						
						// �������� ���� ���鼭 ���
						while (isPaused) {
							if (!isPaused) {
								break;
							}
						}
					}

				} catch (InterruptedException ex) {
					return; // ���� ������ ����
				}
			}

			// ���� �� �����Դµ� ���� ��踦 �Ѿ� �ִ� ���� ���� ����
			if (ga.isBlockOutOfBounds()) {
				int gameMode = Tetris.getGameMode();
				Tetris.gameOver(gameMode, score, levelMode);
				break;
			}

			// ���� ����ġ ��濡 ����
			ga.moveBlockToBackground();
			
			// �� �� �̻� �����Ǹ� �߰� ���� ȹ�� 
			if(ga.clearLines() > 1) {
				score += 2 * ga.clearLines() + level;
			}
			else {
				// �⺻ ���� (+ ������ ���� �߰� ���� ȹ��)
				score += ga.clearLines() + level;
			}
			
			// ���� ������Ʈ
			gf.updateScore(score);
			
			// ���� ������Ʈ, ������ �����Ҽ��� ���� �������� �ӵ� ����
			int lvl = totalClearedLine / linePerLevel + 1;
			if (lvl > level) {
				level = lvl;
				gf.updateLevel(level);
				if (interval > 300) {
					interval -= speedupPerLevel;
				}
			}
		}
		
	}

	private void startItemMode() {
		while (true) {
			ga.spawnBlock();

			if (nextIsItem) { 		// ���� ���� ������
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
				int gameMode = Tetris.getGameMode();
				Tetris.gameOver(gameMode, score, levelMode);
				
				break; // ���� Ż��
			}

			// ���� ���� �������̸� �������� ��¦�Ÿ��� �ش� �������� ������ �����Ѵ�.
			if (isItem) {
				ga.twinkleItem();
				ga.itemFunction();

				// ���� ���� ���� �⺻ ������ ��Ÿ���� ���� �Ҹ��� ����
				ga.setIsItem(false);
				isItem = false;

			} else { // ���� ���� �������� �ƴϸ� ���� ���� ������� �ű��.
				ga.moveBlockToBackground();

				// ���� ���� �������̾��ٸ� ���� ���� ���� �������� �ǰ�, ���� ���� �⺻ ���� �Ǿ�� �ϹǷ�,
				// ���� ���� ��������, ���� ���� �簢������ ǥ���ϱ� ���� �� �Ҹ������� �������ش�.
				if (nextIsItem) {
					nextIsItem = false; 	// ���� ���� �⺻ ��
					isItem = true; 			// ���� ���� ������
					nba.setIsItem(false);		// ���� ���� �������� �ƴ�
					ga.setIsItem(true); 		// ������� ������
				}
			}

			// ���� ���� �ٴڿ� ����� ��, �ϼ��� ���� �����ϰ�, ������ �� �� ����
			//clearedLineNum = ga.clearLines() + ga.oneLineDelte();
			clearedLineNum = ga.clearLines();

			// ���� Ư�� Ƚ�� �����Ǹ� ������ ����
			// 3�� 10���� ��ġ�� 10���� ������ ������ �������� �����˴ϴ�.
			// ������ ���� Ȯ���ϱ� ���� 3�� ���� �������� �������� 3���� �����ص׽��ϴ�.
			if (totalClearedLine / 3 != (totalClearedLine + clearedLineNum) / 3) {
				nextIsItem = true;
			}

			totalClearedLine += clearedLineNum;

			if(clearedLineNum > 1) {
				score += 2* clearedLineNum + level;
			}
			else {
				score += clearedLineNum + level;
			}
			gf.updateScore(score);

			int lvl = totalClearedLine / linePerLevel + 1;
			if (lvl > level) {
				level = lvl;
				gf.updateLevel(level);
				if (interval > 300) {
					interval -= speedupPerLevel;
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
	
	public boolean getIsPaused() {
		return this.isPaused;
	}
	
	public void scorePlus1() {
		score++;
		gf.updateScore(score);
	}
	
	public void scorePlus15() {
		score+=15;
		gf.updateScore(score);
	}
}