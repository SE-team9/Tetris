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
		ga.level = 0; // Easy mode�� ��, Failures ���� 10000�� �������� �� I�� block�� ������ Ƚ��
		// ga.level = 1; // Normal mode�� ��
		// ga.level = 2; // Hard mode�� ��
		
		// when
		final int r = ga.makeRandom();
		
		// then
		assertTrue(r != 0);
	}

}
