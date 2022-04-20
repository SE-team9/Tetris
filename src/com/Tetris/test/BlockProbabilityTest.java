package com.Tetris.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import tetris.GameArea;

class BlockProbabilityTest {

	@RepeatedTest(10000)
	void testMakeRandom() {
		// given
		final GameArea ga = new GameArea(10);
		ga.level = 0; // Easy mode일 때, Failures 수가 10000번 실행했을 때 I형 block이 나오는 횟수
		// ga.level = 1; // Normal mode일 때
		// ga.level = 2; // Hard mode일 때
		
		// when
		final int r = ga.makeRandom();
		
		// then
		assertTrue(r != 0);
	}

}
