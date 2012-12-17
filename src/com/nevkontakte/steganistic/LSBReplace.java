package com.nevkontakte.steganistic;

public class LSBReplace {
	private double percent;

	public LSBReplace(double percent) {
		this.percent = percent;
	}

	public void embed(PGMImage image) {
		for (int i = 0; i < image.raster.length; i++) {
			if (Math.random() < this.percent / 2) {
				image.raster[i] ^= 1;
			}
		}
	}
}
