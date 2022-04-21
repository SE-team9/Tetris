package unitTest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import form.GameForm;
import form.LeaderboardForm;
import form.OptionForm;
import form.StartupForm;
import tetris.Tetris;

class TetrisTest {
	private static GameForm gf;
	private static Tetris tetris;
	private static LeaderboardForm lf;
	
	@Test 
	void tetrisTimeout() { // 비기능적 요구사항
		assertTimeout(Duration.ofMillis(1000), () -> {
			new Tetris();
			Thread.sleep(300);
		});
	}
	
	@Test
	@Disabled
	void testStart() {
		fail("Not yet implemented");
	}

	@Test
	void testShowLeaderboard() {
		assertTimeout(Duration.ofMillis(5000),() ->{
			tetris.showLeaderboard();
			Thread.sleep(300);
		});
		
	}

	@Test
	void testShowStartup() {
		fail("Not yet implemented");
	}

	@Test
	void testShowOption() {
		fail("Not yet implemented");
	}

	@Test
	void testGetColorMode() {
		int r = tetris.getColorMode();
		assertTrue(r == 0 || r == 1);
	}

	@Test
	void testGetGameLevel() {
		int l = tetris.getGameLevel();
		assertTrue(l == 0 || l == 1 || l == 2);
	}

	@Test
	void testGetGameMode() {
		int m = tetris.getGameMode();
		assertTrue(m == 0 || m == 1);
	}

	@Test
	void testGameOver() {
		fail("Not yet implemented");
	}

	@Test
	void testMain() {
		fail("Not yet implemented");
	}

}
