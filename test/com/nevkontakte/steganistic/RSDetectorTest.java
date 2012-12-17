package com.nevkontakte.steganistic;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.InputStream;

public class RSDetectorTest {
	@Test
	public void testFlip() throws Exception {
		RSDetector d = new RSDetector();
		for (int i = 0; i <= 255; i+=2) {
			Assert.assertEquals(d.flip(i, 1), i+1);
			Assert.assertEquals(d.flip(i+1, 1), i);
		}
		for (int i = 0; i <= 255; i++) {
			Assert.assertEquals(d.flip(i, 0), i);
		}
		for (int i = 0; i <= 256; i+=2) {
			Assert.assertEquals(d.flip(i-1, -1), i);
			Assert.assertEquals(d.flip(i, -1), i-1);
		}
	}

	@Test
	public void testDiscriminator() throws Exception {
		RSDetector d = new RSDetector();

		int[] pattern = {0, 1, -1};

		int[] sample = {0, 0, 4, 8, 12, 0, 0};
		int expected = (9-4) + (11 - 9);

		Assert.assertEquals(d.discriminator(sample, 2, pattern.length, pattern), expected);
		Assert.assertEquals(d.discriminator(sample, 0, sample.length, null), 24);
	}

	@Test
	public void testTower() throws Exception {
		InputStream input = this.getClass().getResourceAsStream("/com/nevkontakte/steganistic/resources/Tower.pgm");
		Assert.assertNotNull(input);
		PGMImage img = new PGMImage(input);
		input.close();

		RSDetector detector = new RSDetector();

		double p = detector.scan(img);
		Assert.assertTrue(p > 0.4, "Detected payload ratio " + p + " <= 0.4");
		Assert.assertTrue(p < 0.6, "Detected payload ratio " + p + " >= 0.6");
	}

	@Test
	public void testBear() throws Exception {
		InputStream input = this.getClass().getResourceAsStream("/com/nevkontakte/steganistic/resources/Bear.pgm");
		Assert.assertNotNull(input);
		PGMImage img = new PGMImage(input);
		input.close();

		RSDetector detector = new RSDetector();

		double p = detector.scan(img);
		Assert.assertTrue(p < 0.01, "Detected payload ratio " + p + " >= 0.01");
		/* Negative values might occur
		Assert.assertTrue(p > -0.01, "Detected payload ratio " + p + " <= -0.01");
		*/
	}
}
