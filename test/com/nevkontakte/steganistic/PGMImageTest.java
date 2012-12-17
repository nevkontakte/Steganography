package com.nevkontakte.steganistic;

import org.apache.sanselan.Sanselan;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class PGMImageTest {
	@Test
	public void testLoad() throws Exception {
		{
		InputStream input = this.getClass().getResourceAsStream("/com/nevkontakte/steganistic/resources/Fall.pgm");
		Assert.assertNotNull(input);

		PGMImage img = new PGMImage(input);
		Assert.assertEquals(img.width, 512);
		Assert.assertEquals(img.height, 512);
		Assert.assertEquals(img.depth, 255);
		Assert.assertEquals(img.raster[0], 92);
		Assert.assertEquals(img.raster[1], 100);
		Assert.assertEquals(img.raster[2], 103);
		}

		{
		InputStream input = this.getClass().getResourceAsStream("/com/nevkontakte/steganistic/resources/Bear.pgm");
		Assert.assertNotNull(input);

		PGMImage img = new PGMImage(input);
		Assert.assertEquals(img.width, 512);
		Assert.assertEquals(img.height, 512);
		Assert.assertEquals(img.depth, 255);
		Assert.assertEquals(img.raster[0], 0x42);
		Assert.assertEquals(img.raster[1], 0x3a);
		Assert.assertEquals(img.raster[2], 0x1c);
		}
	}

	@Test
	public void testCopy() throws Exception {
		InputStream input = this.getClass().getResourceAsStream("/com/nevkontakte/steganistic/resources/Fall.pgm");
		Assert.assertNotNull(input);
		PGMImage img = new PGMImage(input);
		input.close();

		PGMImage img2 = new PGMImage(img);

		Assert.assertEquals(img2, img);
	}

	@Test
	public void testSave() throws Exception {
		InputStream input = this.getClass().getResourceAsStream("/com/nevkontakte/steganistic/resources/Fall.pgm");
		Assert.assertNotNull(input);
		PGMImage img = new PGMImage(input);
		input.close();

		File tempFile = File.createTempFile("steganistic", ".pgm");
		FileOutputStream destination = new FileOutputStream(tempFile);
		img.save(destination);
		destination.close();

		FileInputStream source = new FileInputStream(tempFile);
		PGMImage newImage = new PGMImage(source);
		source.close();

		Assert.assertEquals(img, newImage);
	}
}
