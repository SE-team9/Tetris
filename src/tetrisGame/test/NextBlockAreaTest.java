package tetrisGame.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import tetris.GameArea;
import tetris.NextBlockArea;
import tetris.TetrisBlock;

class NextBlockAreaTest {
	//private TetrisBlock nextBlock = ga.getNextBlock();
	final GameArea ga = new GameArea(600, 450, 10);
	private NextBlockArea nba = new NextBlockArea(600, 450, ga);
	private TetrisBlock nextBlock = ga.getNextBlock();
	//nextBlock = ga.getNextBlock();

	@Test
	@Disabled
	void testNextBlockArea() {
		fail("Not yet implemented");
	}

	@Test
	void testInitNextBlockArea() {
		nba.initNextBlockArea();
		assertFalse(nba.getIsItem());
		
	}

	@Test
	void testUpdateNBA() {
		nba.updateNBA(nextBlock);
		assertNotNull(nextBlock);
	}

	@Test
	void testSetIsItem() {
		nba.setIsItem(true);
		assertTrue(nba.getIsItem() == true );
	}
	
	@Test
	void testGetIsItem() {
		assertNotNull(nba.getIsItem());
	}

	@Test
	@Disabled
	void testPaintComponentGraphics() {
		fail("Not yet implemented");
	}

}
