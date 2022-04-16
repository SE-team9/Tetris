package tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

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
	private ModeForm mf;

	boolean paused = false;

	public GameArea(int columns) {
		initThisPanel();

		mf = new ModeForm();
		
		gridColumns = columns;
		gridCellSize = this.getBounds().width / gridColumns;
		gridRows = this.getBounds().height / gridCellSize;

		initBlocks();
		updateNextBlock();
	}

	// --------------------------------------------------------------------- 초기화 관련 동작
	private void initThisPanel() {
		this.setBounds(200, 0, 200, 400);
		this.setBackground(new Color(238, 238, 238));
		this.setBorder(LineBorder.createBlackLineBorder());
	}

	// 배경 초기화
	public void initBackgroundArray() {
		background = new Color[gridRows][gridColumns];
	}

	// 각각의 블럭 초기화
	public void initBlocks() {
		blocks = new TetrisBlock[] { new IShape(), new JShape(), new LShape(), new OShape(), new ZShape(),
				new SShape() };
	}

	public int getGridCellSize() {
		return gridCellSize;
	}

	// --------------------------------------------------------------------- 난이도 모드 관련
	

	// weighted random 함수 생성
	public static <E> E getWeightedRandom(Map<E, Double> weights, Random random) {
		E result = null;
		double bestValue = Double.MAX_VALUE;

		for (E element : weights.keySet()) {
			double value = -Math.log(random.nextDouble()) / weights.get(element);
			if (value < bestValue) {
				bestValue = value;
				result = element;
			}
		}
		return result;
	}

	// weighted에 기반하여 random 함수
	public int makeRandom() {
		Map<String, Double> w = new HashMap<String, Double>();
		Random r = new Random();
		
		int mode = mf.getMode();		
		
		double weight, iWeight;
		int blockNum;

		if (mode == 1) {
			weight = 14.0;
			iWeight = 16.0;
			w.put("0", iWeight);
			for (int i = 1; i < blocks.length; i++) {
				w.put(Integer.toString(i), weight);
			}
			blockNum = Integer.parseInt(getWeightedRandom(w, r));
		} else if (mode == 3) {
			weight = 15.0;
			iWeight = 10.0;
			w.put("0", iWeight);
			for (int i = 1; i < blocks.length; i++) {
				w.put(Integer.toString(i), weight);
			}
			blockNum = Integer.parseInt(getWeightedRandom(w, r));
		} else {
			blockNum = r.nextInt(blocks.length);
		}
		return blockNum;
	}
	
	int getMode() {
		return mf.getMode();
	}

	// --------------------------------------------------------------------- 블록 관련 동작
	// 다음 블럭 설정 + 난이도에 따른
	public void updateNextBlock() {
		// Random r = new Random();
		int value = makeRandom();
		nextBlock = blocks[value];
	}

	public TetrisBlock getNextBlock() {
		return nextBlock;
	}

	// 다음 블럭을 현재 블럭으로 가져오기
	public void spawnBlock() {
		block = nextBlock;
		block.spawn(gridColumns);
	}

	// --------------------------------------------------------------------- 블록 조작 /
	// 경계 확인
	// 블럭이 위쪽 경계를 벗어 났으면 게임 종료
	public boolean isBlockOutOfBounds() {
		if (block.getY() < 0) {
			block = null;
			return true;
		}
		return false;
	}

	public boolean moveBlockDown() {
		// 블록이 바닥에 닿으면, 백그라운드 블록으로 전환
		if (!checkBottom()) {
			return false;
		}

		// GameForm에서 입력된 키에 따라 블록 일시정지 및 재개
		if (paused) {
			blocking();
		}

		block.moveDown();
		repaint(); // 일정한 시간 간격마다 업데이트 (스레드 사용)
		// repaint 잊지 말자! (안 해주면 입력에 느리게 반응함)

		return true;
	}

	private void blocking() {
		while (paused) {
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

		// 다른 블록을 만나기 직전까지 계속 낙하
		while (checkBottom()) {
			block.moveDown();
		}

		repaint();
	}

	public void rotateBlock() { // up
		if (block == null)
			return;

		// 배경과 겹치는지 확인
		if (!checkRotate())
			return;

		block.rotate();

		// 회전 시 위치 재설정
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
			// 특정 열의 맨 밑에서 위쪽으로 올라가다가
			for (int row = h - 1; row >= 0; row--) {
				// colored cell을 발견했고
				if (shape[row][col] != 0) {
					int x = col + block.getX();
					int y = row + block.getY() + 1; // 해당 블록 바로 아래에!

					if (y < 0)
						break; // 보드판에 포함되지 않은 블록은 무시하고 다음 열로 이동

					if (background[y][x] != null) { // 백그라운드 블록이 있으면!
						return false; // stop
					}
					break; // 현재 열은 더이상 검사할 필요 없음.
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
					int x = col + block.getX() - 1; // 바로 왼쪽에!
					int y = row + block.getY();

					if (y < 0)
						break; // 보드판에 포함되지 않은 블록은 무시하고 다음 열로 이동

					if (background[y][x] != null) { // 백그라운드 블록이 있으면!
						return false; // stop
					}

					break; // 현재 행은 더이상 검사할 필요 없음.
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
					int x = col + block.getX() + 1; // 바로 오른쪽에!
					int y = row + block.getY();

					if (y < 0)
						break; // 보드판에 포함되지 않은 블록은 무시하고 다음 열로 이동

					if (background[y][x] != null) { // 백그라운드 블록이 있으면!
						return false; // stop
					}

					break; // 현재 행은 더이상 검사할 필요 없음.
				}
			}
		}
		return true; // keep going
	}

	// 회전 시 다른 블록과 겹치지 않도록 확인 (L모양 블럭에서 완전하진 않음 나중에 LShpae 블록은 따로 수정 필요)
	private boolean checkRotate() {
		// 복사 객체를 생성하고 회전시켜서 확인한다.
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
					// 해당 칸 확인
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

	// --------------------------------------------------------------------- 배경 관련 동작
	public void moveBlockToBackground() {
		// 움직이고 있던 블록에 대한 참조
		int[][] shape = block.getShape();
		int h = block.getHeight();
		int w = block.getWidth();

		int xPos = block.getX();
		int yPos = block.getY();

		Color color = block.getColor();

		for (int r = 0; r < h; r++) {
			for (int c = 0; c < w; c++) {
				// 백그라운드 블록의 컬러로 설정
				if (shape[r][c] == 1) {
					background[r + yPos][c + xPos] = color;
				}
			}
		}
	}

	// 완성된 줄들 삭제
	public int clearLines() {

		boolean lineFilled;
		int linesCleared = 0;

		// 아래부터
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

				// 맨 윗줄의 위는 null이므로 따로 지워준다.
				clearLine(0);

				// 아래로 한 줄씩 내려왔으므로 지워진 줄 위치에서부터 다시 시작
				r++;

				repaint();
			}
		}
		return linesCleared;
	}

	// 배경에서 r행 삭제
	private void clearLine(int r) {
		for (int i = 0; i < gridColumns; i++) {
			background[r][i] = null;
		}
	}

	// 삭제된 r행 윗줄들을 내려준다.
	private void shiftDown(int r) {
		for (int row = r; row > 0; row--) {
			for (int col = 0; col < gridColumns; col++) {
				background[row][col] = background[row - 1][col];
			}
		}
	}

	// --------------------------------------------------------------------- 그리기
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

					drawGridSquare(g, c, x, y);
				}
			}
		}
	}

	private void drawBackground(Graphics g) {
		Color color; // 백그라운드 블록에 대한 참조

		for (int r = 0; r < gridRows; r++) {
			for (int c = 0; c < gridColumns; c++) {
				color = background[r][c];

				// moveBlockToBackground 함수에서 컬러가 설정되면 not null
				if (color != null) {
					int x = c * gridCellSize;
					int y = r * gridCellSize;

					drawGridSquare(g, color, x, y);
				}
			}
		}
	}

	// 반복되는 코드는 모듈화!
	private void drawGridSquare(Graphics g, Color c, int x, int y) {
		g.setColor(c);
		g.fillRect(x, y, gridCellSize, gridCellSize);
		g.setColor(Color.black);
		g.drawRect(x, y, gridCellSize, gridCellSize);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		drawBackground(g);
		drawBlock(g);
	}
}
