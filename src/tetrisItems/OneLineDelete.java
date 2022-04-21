package tetrisItems;

import java.awt.Color;

import tetris.TetrisBlock;

public class OneLineDelete extends TetrisBlock {

	public OneLineDelete() {
		super(new int[][] { { 1, 1 }, { 1, 1 } });
		setColor(Color.orange);
	}
}
