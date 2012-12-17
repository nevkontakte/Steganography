package com.nevkontakte.steganistic;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.InputStream;

public class LSBReplaceTest {
	@Test
	public void testEmbed() throws Exception {
		InputStream input = this.getClass().getResourceAsStream("/com/nevkontakte/steganistic/resources/Fall.pgm");
		Assert.assertNotNull(input);
		PGMImage img = new PGMImage(input);
		input.close();

		PGMImage embedded = new PGMImage(img);

		LSBReplace replace = new LSBReplace(0.5);
		replace.embed(embedded);

		Assert.assertFalse(embedded.equals(img));
	}
}
