package com.nevkontakte.steganistic;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.InputStream;

public class WSDetectorTest {
	@Test
	public void testTower() throws Exception {
		InputStream input = this.getClass().getResourceAsStream("/com/nevkontakte/steganistic/resources/Tower.pgm");
		Assert.assertNotNull(input);
		PGMImage img = new PGMImage(input);
		input.close();

		WSDetector detector = new WSDetector();

		double p = detector.scanCorrected(img, 0.5);
		Assert.assertTrue(p > 0.35, "Detected payload ratio " + p + " <= 0.35");
		Assert.assertTrue(p < 0.65, "Detected payload ratio " + p + " >= 0.65");
	}

	@Test
	public void testBear() throws Exception {
		InputStream input = this.getClass().getResourceAsStream("/com/nevkontakte/steganistic/resources/Bear.pgm");
		Assert.assertNotNull(input);
		PGMImage img = new PGMImage(input);
		input.close();

		WSDetector detector = new WSDetector();

		double p = detector.scanCorrected(img, 0);
		Assert.assertTrue(p < 0.01, "Detected payload ratio " + p + " >= 0.01");
		/* Negative values might occur
		Assert.assertTrue(p > -0.01, "Detected payload ratio " + p + " <= -0.01");
		*/
	}
}
