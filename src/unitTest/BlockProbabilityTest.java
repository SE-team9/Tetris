package unitTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import tetris.GameArea;
import tetris.Tetris;

class BlockProbabilityTest {

	@RepeatedTest(10000)
	void testMakeRandom() {

		final GameArea ga = new GameArea(600, 450, 10);
		ga.testLevel = 0; // Easy mode일 때, Failures 수가 10000번 실행했을 때 I형 block이 나오는 횟수
		// ga.testLevel = 1; // Normal mode일 때
		// ga.testLevel = 2; // Hard mode일 때
		
		// when
		final int r = ga.makeRandom();
		
		// then
		assertTrue(r != 0);
	}

}
