package tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import tetrisItems.FillEmpty;
import tetrisItems.TwoLineDelete;
import tetrisblocks.IShape;
import tetrisblocks.JShape;
import tetrisblocks.LShape;
import tetrisblocks.OShape;
import tetrisblocks.SShape;
import tetrisblocks.ZShape;

public class GameArea extends JPanel {
	private int gridRows;
	private int gridColumns;
	private int gridCellSize;
	private Color[][] background;
	private TetrisBlock[] blocks;
	private TetrisBlock block;
	private TetrisBlock nextBlock;
	
	boolean paused = false;

	private TetrisBlock[] items;
	
	private boolean isItem = false; // ���� ���� ���������� Ȯ���ϴ� ���� 

	public GameArea(int columns) {
		initThisPanel();

		gridColumns = columns;
		gridCellSize = this.getBounds().width / gridColumns;
		gridRows = this.getBounds().height / gridCellSize;

		initBlocks();
		initItems();
		updateNextBlock();
	}

	// --------------------------------------------------------------------- �ʱ�ȭ ���� ����
	private void initThisPanel() {
		this.setBounds(200, 0, 200, 400);
		this.setBackground(new Color(238, 238, 238));
		this.setBorder(LineBorder.createBlackLineBorder());
	}

	// ��� �ʱ�ȭ
	public void initBackgroundArray() {
		background = new Color[gridRows][gridColumns];
	}

	// ������ �� �ʱ�ȭ
	public void initBlocks() {
		blocks = new TetrisBlock[] { new IShape(), new JShape(), new LShape(), new OShape(), new ZShape(),
				new SShape() };
	}

	// ������ ������ �ʱ�ȭ
	public void initItems() {
		items = new TetrisBlock[] { new FillEmpty(), new TwoLineDelete() };
	}

	public int getGridCellSize() {
		return gridCellSize;
	}
	
	public void setIsItem(boolean answer) {
		isItem = answer;
	}

	// --------------------------------------------------------------------- ��� ���� ����
	// ���� �� ����
	public void updateNextBlock() {
		Random r = new Random();
		nextBlock = blocks[r.nextInt(blocks.length)];
		nextBlock.setShape();
	}

	// ���� ���� ������ �߿��� ����
	public void setNextItem() {
		Random r = new Random();
		nextBlock = items[r.nextInt(items.length)];
		nextBlock.setShape();
	}

	public TetrisBlock getNextBlock() {
		return nextBlock;
	}
	
	public TetrisBlock getBlock() {
		return block;
	}

	// ���� ���� ���� ������ ��������
	public void spawnBlock() {
		block = nextBlock;
		block.spawn(gridColumns);
	}

	// --------------------------------------------------------------------- ��� ���� / ��� Ȯ��
	// ���� ���� ��踦 ���� ������ ���� ����
	public boolean isBlockOutOfBounds() {
		if (block.getY() < 0) {
			block = null;
			return true;
		}
		return false;
	}

	public boolean moveBlockDown() {
		// ����� �ٴڿ� ������, ��׶��� ������� ��ȯ
		if (!checkBottom()) {
			return false;
		}
		
		// GameForm���� �Էµ� Ű�� ���� ��� �Ͻ����� �� �簳 
		if(paused) {
			blocking();
		}
		
		block.moveDown();
		repaint(); // ������ �ð� ���ݸ��� ������Ʈ (������ ���)
		// repaint ���� ����! (�� ���ָ� �Է¿� ������ ������)

		return true;
	}
	
	private void blocking() {
		while(paused) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void moveBlockRight() {
		if (block == null)
			return;
		
		if (!checkRight())
			return;

		block.moveRight();
		repaint();
	}

	public void moveBlockLeft() {
		if (block == null)
			return;
		
		if (!checkLeft())
			return;

		block.moveLeft();
		repaint();
	}

	public void dropBlock() { // space bar
		if (block == null)
			return;
		
		// �ٸ� ����� ������ �������� ��� ����
		while (checkBottom()) {
			block.moveDown(); 
		}
		
		repaint();
	}

	public void rotateBlock() { // up
		if (block == null)
			return;
		
		// ���� ��ġ���� Ȯ��
		if (!checkRotate())
			return;

		block.rotate();

		// ȸ�� �� ��ġ �缳��
		if (block.getLeftEdge() < 0)
			block.setX(0);
		
		if (block.getRightEdge() >= gridColumns)
			block.setX(gridColumns - block.getWidth());
		
		if (block.getBottomEdge() >= gridRows)
			block.setY(gridRows - block.getHeight());

		repaint();
	}

	private boolean checkBottom() {
		if (block.getBottomEdge() == gridRows) {
			return false; // stop
		}

		int[][] shape = block.getShape();
		int w = block.getWidth();
		int h = block.getHeight();

		for (int col = 0; col < w; col++) {
			// Ư�� ���� �� �ؿ��� �������� �ö󰡴ٰ�
			for (int row = h - 1; row >= 0; row--) {
				// colored cell�� �߰��߰�
				if (shape[row][col] != 0) {
					int x = col + block.getX();
					int y = row + block.getY() + 1; // �ش� ��� �ٷ� �Ʒ���!

					if (y < 0)
						break; // �����ǿ� ���Ե��� ���� ����� �����ϰ� ���� ���� �̵�

					if (background[y][x] != null) { // ��׶��� ����� ������!
						return false; // stop
					}
					break; // ���� ���� ���̻� �˻��� �ʿ� ����.
				}
			}
		}

		return true; // keep going
	}

	private boolean checkLeft() {
		if (block.getLeftEdge() == 0) {
			return false; // stop
		}

		int[][] shape = block.getShape();
		int w = block.getWidth();
		int h = block.getHeight();

		for (int row = 0; row < h; row++) {
			for (int col = 0; col < w; col++) {
				if (shape[row][col] != 0) { // colored cell
					int x = col + block.getX() - 1; // �ٷ� ���ʿ�!
					int y = row + block.getY();

					if (y < 0)
						break; // �����ǿ� ���Ե��� ���� ����� �����ϰ� ���� ���� �̵�

					if (background[y][x] != null) { // ��׶��� ����� ������!
						return false; // stop
					}

					break; // ���� ���� ���̻� �˻��� �ʿ� ����.
				}
			}
		}
		return true; // keep going
	}

	private boolean checkRight() {
		if (block.getRightEdge() == gridColumns) {
			return false; // stop
		}

		int[][] shape = block.getShape();
		int w = block.getWidth();
		int h = block.getHeight();

		for (int row = 0; row < h; row++) {
			for (int col = w - 1; col >= 0; col--) {
				if (shape[row][col] != 0) { // colored cell
					int x = col + block.getX() + 1; // �ٷ� �����ʿ�!
					int y = row + block.getY();

					if (y < 0)
						break; // �����ǿ� ���Ե��� ���� ����� �����ϰ� ���� ���� �̵�

					if (background[y][x] != null) { // ��׶��� ����� ������!
						return false; // stop
					}

					break; // ���� ���� ���̻� �˻��� �ʿ� ����.
				}
			}
		}
		return true; // keep going
	}

	// ȸ�� �� �ٸ� ��ϰ� ��ġ�� �ʵ��� Ȯ�� (L��� ������ �������� ���� ���߿� LShpae ����� ���� ���� �ʿ�)
	private boolean checkRotate() {
		// ���� ��ü�� �����ϰ� ȸ�����Ѽ� Ȯ���Ѵ�.
		TetrisBlock rotated = new TetrisBlock(block.getShape());
		rotated.setCurrentRotation(block.getCurrentRotation());
		rotated.setX(block.getX());
		rotated.setY(block.getY());
		rotated.rotate();

		if (rotated.getRightEdge() >= gridColumns)
			rotated.setX(gridColumns - rotated.getWidth());

		int[][] shape = rotated.getShape();
		int w = rotated.getWidth();
		int h = rotated.getHeight();

		for (int row = 0; row < h; row++) {
			for (int col = 0; col < w; col++) {
				if (shape[row][col] != 0) {
					// �ش� ĭ Ȯ��
					int x = col + rotated.getX();
					int y = row + rotated.getY();
					if (y < 0)
						break;
					if (background[y][x] != null)
						return false;
				}
			}
		}
		return true;
	}

	// ---------------------------------------------------------------------�����۰��õ���
	// ���⿡ �� �����۵��� ������ �߰��Ͻø� ���� �� �����ϴ�.
	
	// �� ���� ��ĭ�� �ſ��ִ� �������� ����
	public void fillEmpty() {

		int xPos = block.getX();

		int emptyNum = 0;
		int currentR;
		int nextR;

		for (int r = gridRows - 1; r > 0; r--) {
			if (background[r][xPos] == null) {
				emptyNum++;
				nextR = r - 1;
				while (nextR >= 0 && background[nextR][xPos] == null) {
					nextR--;
				}
				if (nextR == -1) {
					return;
				} else {
					currentR = r;
					for (; nextR >= 0; nextR--, currentR--) {
						background[currentR][xPos] = background[nextR][xPos];
						repaint();
					}
				}
			}
		}
		while (emptyNum > 0) {
			block.moveDown();
		}
		repaint();
	}
	// �� �� ���� �������� ����
	public void twoLineDelete() {
		int yPos = block.getY();
		int time = 0;

		for (int r = yPos + 2; r >= yPos+1 && time < 2; r--) {
			clearLine(r);
			shiftDown(r);
			r++;
			time++;

			repaint();
		}
	}

	// ������ �����ۿ� ���� �ش��ϴ� ������ �ϵ��� �ϴ� �Լ�
	public void itemFunction() {

		// ���� ���� ��ĭ�� �ſ��ִ� �������̸� ��ĭ �ſ�� ���� ����
		if (this.block instanceof FillEmpty) {
			fillEmpty();
		} else if(this.block instanceof TwoLineDelete) {
			twoLineDelete();
		}
	}

	// �������� ��¦�Ÿ����� �Ѵ�.
	public void twinkleItem() {

		Color originColor = block.getColor();

		try {
			block.setColor(Color.white);
			repaint();
			Thread.sleep(200);
			block.setColor(originColor);
			repaint();
			Thread.sleep(200);
			block.setColor(Color.white);
			repaint();
			Thread.sleep(200);
			block.setColor(originColor);
			repaint();
			Thread.sleep(200);
		} catch (InterruptedException ex) {
			return;
		}
	}

	// ---------------------------------------------------------------------�����õ���
	public void moveBlockToBackground() {
		// �����̰� �ִ� ��Ͽ� ���� ����
		int[][] shape = block.getShape();
		int h = block.getHeight();
		int w = block.getWidth();

		int xPos = block.getX();
		int yPos = block.getY();

		Color color = block.getColor();

		for (int r = 0; r < h; r++) {
			for (int c = 0; c < w; c++) {
				// ��׶��� ����� �÷��� ����
				if (shape[r][c] == 1) {
					background[r + yPos][c + xPos] = color;
				}
			}
		}
	}

	// �ϼ��� �ٵ� ����
	public int clearLines() {

		boolean lineFilled;
		int linesCleared = 0;

		// �Ʒ�����
		for (int r = gridRows - 1; r >= 0; r--) {
			lineFilled = true;

			for (int c = 0; c < gridColumns; c++) {
				if (background[r][c] == null) {
					lineFilled = false;
					break;
				}
			}
			if (lineFilled) {
				linesCleared++;
				clearLine(r);
				shiftDown(r);
				
				// �� ������ ���� null�̹Ƿ� ���� �����ش�.
				clearLine(0);

				// �Ʒ��� �� �پ� ���������Ƿ� ������ �� ��ġ�������� �ٽ� ����
				r++;

				repaint();
			}
		}
		return linesCleared;
	}

	// ��濡�� r�� ����
	private void clearLine(int r) {
		for (int i = 0; i < gridColumns; i++) {
			background[r][i] = null;
		}
	}

	// ������ r�� ���ٵ��� �����ش�.
	private void shiftDown(int r) {
		for (int row = r; row > 0; row--) {
			for (int col = 0; col < gridColumns; col++) {
				background[row][col] = background[row - 1][col];
			}
		}
	}

	// --------------------------------------------------------------------- �׸���
	private void drawBlock(Graphics g) {

		if (block == null)
			return;

		int h = block.getHeight();
		int w = block.getWidth();
		Color c = block.getColor();
		int[][] shape = block.getShape();

		for (int row = 0; row < h; row++) {
			for (int col = 0; col < w; col++) {

				if (shape[row][col] == 1) { // colored cell
					int x = (block.getX() + col) * gridCellSize;
					int y = (block.getY() + row) * gridCellSize;

					// ���� ���� �������̸� �������� �׷��ְ�, �������� �ƴϸ� �簢������ �ϳ��� �׷��ش�.
					if (isItem) {
						drawGridOval(g, c, x, y);
					} else {
						drawGridSquare(g, c, x, y);
					}
				}
			}
		}
	}

	private void drawBackground(Graphics g) {
		Color color; // ��׶��� ��Ͽ� ���� ����

		for (int r = 0; r < gridRows; r++) {
			for (int c = 0; c < gridColumns; c++) {
				color = background[r][c];

				// moveBlockToBackground �Լ����� �÷��� �����Ǹ� not null
				if (color != null) {
					int x = c * gridCellSize;
					int y = r * gridCellSize;

					drawGridSquare(g, color, x, y);
				}
			}
		}
	}

	// �ݺ��Ǵ� �ڵ�� ���ȭ!
	private void drawGridSquare(Graphics g, Color c, int x, int y) {
		g.setColor(c);
		g.fillRect(x, y, gridCellSize, gridCellSize);
		g.setColor(Color.black);
		g.drawRect(x, y, gridCellSize, gridCellSize);
	}
	
	// �������� ���� �׷��ش�.
	private void drawGridOval(Graphics g, Color color, int x, int y) {
		g.setColor(color);
		g.fillOval(x, y, gridCellSize, gridCellSize);
		g.setColor(Color.black);
		g.drawOval(x, y, gridCellSize, gridCellSize);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		drawBackground(g);
		drawBlock(g);
	}
}
