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
	private int speedupPerLevel;
	private boolean isPaused = false;
	private int levelMode; // ���� ȭ�鿡�� ���� ���� ���̵�

	// ������ ������ ���õ� ������
	private int curClearedLines; 	    // ���� ������ ��� ������ �� ���� �����ϴ� ����
	private int totalClearedLine; 	    // ������ �� ���� ���� �����ϴ� ����
	private boolean nextIsItem = false; // ���� ���� ������ ������ Ȯ���ϴ� ����
	private boolean isItem = false; 	// ���� ���� ������ ������ Ȯ���ϴ� ����
	private int linePerItem = 10;

	public GameThread(GameArea ga, GameForm gf, NextBlockArea nba) {
		this.ga = ga;
		this.gf = gf;
		this.nba = nba;

		gf.updateScore(score);
		gf.updateLevel(level);

		levelMode = Tetris.getGameLevel();
		switch(levelMode) {
		case 0: 
			speedupPerLevel = 80;
			break;
		case 1:
			speedupPerLevel = 100;
			break;
		case 2:
			speedupPerLevel = 120;
			break;
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

			// ���� ��ĭ�� ������ ������ �� ������ ���� ����
			while (ga.moveBlockDown()) {
				score+=level;
				gf.updateScore(score);
				checkPauseKey(); // 0.1�ʸ��� pauseŰ Ȯ��
			}

			// ���� ������ �� ���̸� ���� ����
			if (ga.isBlockOutOfBounds()) {
				int gameMode = Tetris.getGameMode();
				Tetris.gameOver(gameMode, score, levelMode);
				break; // ���� Ż��
			}

			// ���� �� ��ġ�� ��濡 ����
			ga.moveBlockToBackground();
			
			// �� �� �̻� �����Ǹ� ���ʽ� ���� ȹ��
			if(curClearedLines > 1) {
				score += 2* curClearedLines + level;
			}
			else {
				// �⺻ ����������, ���� ��¿� ���� ���ʽ� ���� ȹ�� ���� 
				score += curClearedLines + level;
			}
			gf.updateScore(score);
			
			// linePerLevel ��ŭ �� �����ϸ� ���� �� �� ���� �ӵ� ����
			int lvl = totalClearedLine / linePerLevel + 1;
			if (lvl > level) {
				level = lvl;
				gf.updateLevel(level);
				
				// �ð� ������ 300���� Ŭ ���� ������Ʈ (�� ���Ϸδ� �������� �ʵ���)
				if (interval > 300) {
					interval -= speedupPerLevel;
				}
			}
		}
	}

	private void startItemMode() {
		while (true) {
			ga.spawnBlock();
			
			if (nextIsItem) {
				ga.updateNextItem(); // ������ ������ ����
				nba.setIsItem(true); // �������� �������� ǥ���ϱ� ���� �÷���
			} else {
				ga.updateNextBlock(); // �Ϲ� ������ ���� 
			}
			nba.updateNBA(ga.getNextBlock());

			// ���� ��ĭ�� ������ ������ �� ������ ���� ����
			while (ga.moveBlockDown()) {
				score+=level;
				gf.updateScore(score);
				checkPauseKey(); // 0.1�ʸ��� pauseŰ Ȯ��
			}

			// ���� ������ �� ���̸� ���� ����
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
					nextIsItem = false; // ���� ���� �⺻ ��
					nba.setIsItem(false);
					
					isItem = true; // ���� ���� ������
					ga.setIsItem(true); 
				}
			}

			// ���� ������ ���� �� ���� 
			curClearedLines = ga.clearLines();

			// linePerItem ��ŭ ���� �����Ǹ� ������ ���� (������ 10���� �ؾ� ��)
			if (totalClearedLine / linePerItem != (totalClearedLine + curClearedLines) / linePerItem) {
				nextIsItem = true;
			}
			totalClearedLine += curClearedLines;
			
			if(curClearedLines > 1) {
				score += 2* curClearedLines + level;
			}
			else {
				score += curClearedLines + level;
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
	
	private void checkPauseKey() {
		try {
			int i = 0;
			while (i < interval / 100) {
				Thread.sleep(100); 
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

	public void pause() {
		this.isPaused = true;
	}

	public void reStart() {
		this.isPaused = false;
	}
	
	public boolean getIsPaused() {
		return this.isPaused;
	}
	
	// �� ��ĭ �Ʒ��� ������ �� 1�� ���� 
	public void scorePlus_level() {
		score+=level;
		gf.updateScore(score);
	}
	
	// �ѹ��� ������� �� 15�� ����
	public void scorePlus15() {
		score += 15;
		gf.updateScore(score);
	}
}