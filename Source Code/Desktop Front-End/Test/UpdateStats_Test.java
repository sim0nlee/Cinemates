package Test;

import GUI.MainFrame;
import control.Controller;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UpdateStats_Test {

	@Test
	//3A
	void testUpdateStats() {

		MainFrame frame = new MainFrame(true);

		Controller.setStatisticsAccess(new RTDBStatisticsAccess_Stub());

		Controller.updateStats(frame);

		for (int i = 0; i < frame.fields.length; i++) {
			Assertions.assertEquals(i, Integer.valueOf(frame.fields[i].getText().trim()));
		}

	}

}
