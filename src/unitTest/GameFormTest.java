package tetrisGame.test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import form.GameForm;
import tetris.GameArea;
import tetris.GameThread;
import tetris.NextBlockArea;
import tetris.Tetris;

class GameFormTest {

	private GameArea ga;
	private GameThread gt;
	private NextBlockArea nba;
	final GameForm gf = new GameForm();;

	@Test
	@Disabled
	void testGameForm() {
		fail("Not yet implemented");
	}

	@Test
	void testStartGame() { // 비기능적 요구사항
		assertTimeout(Duration.ofMillis(1000), () -> {
			gf.startGame();
			Thread.sleep(300);
		});
	}

	@Test
	void testUpdateScore() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdateLevel() {
		fail("Not yet implemented");
	}

	@Test
	void testMain() {
		fail("Not yet implemented");
	}

}
