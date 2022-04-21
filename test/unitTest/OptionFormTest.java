package unitTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import form.OptionForm;

class OptionFormTest {

	final OptionForm of = new OptionForm();
	
	@Test
	@Disabled
	void testOptionForm() {
		fail("Not yet implemented");
	}

	@Test
	void testShowConfirmedOption() {
		fail("Not yet implemented");
	}

	@Test
	void testInitComponents() {
		fail("Not yet implemented");
	}

	@Test
	void testGetFrameSize() {
		assertNotNull(of.getFrameSize());
	}

	@Test
	void testGetCurrentKeyMode() {
		int km = of.getCurrentColorMode();
		assertTrue(km == 0 || km == 1);
	}

	@Test
	void testGetCurrentGameLevel() {
		int l = of.getCurrentColorMode();
		assertTrue(l == 0 || l == 1 || l == 2);
	}

	@Test
	void testGetCurrentColorMode() {
		int cm = of.getCurrentColorMode();
		assertTrue(cm == 0 || cm == 1);
	}

	@Test
	void testMain() {
		fail("Not yet implemented");
	}

}
