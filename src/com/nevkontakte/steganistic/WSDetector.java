package com.nevkontakte.steganistic;

public class WSDetector {
	public static double[][] filter = {
			{-1.0d/4,   1.0d/2, -1.0d/4},
			{1.0d/2,    0,      1.0d/2},
			{-1.0d/4,   1.0d/2, -1.0d/4},
	};

	public double scan(PGMImage image) {
		double ratio = 0;
		double weightNorm = 0;
		for (int i = 0; i < image.raster.length; i++) {
			double pixelWeight = this.weightPixel(image, i);
			weightNorm+=pixelWeight;
			ratio += pixelWeight * this.estimatePixel(image, i);
		}
		ratio*=2;
		ratio/=weightNorm;
		return ratio;
	}

	public double scanCorrected(PGMImage image, double expected) {
		double p = this.scan(image);
		p -= this.getBias(image, expected);
		return p;
	}

	private double getBias(PGMImage image, double expected) {
		double bias = 0;
		double weightNorm = 0;

		for (int i = 0; i < image.raster.length; i++) {
			double pixelWeight = this.weightPixel(image, i);
			weightNorm+=pixelWeight;
			bias += pixelWeight * this.filterCoverPixel(image, i) * (image.raster[i] - (image.raster[i] ^ 1));
		}
		bias /= weightNorm;

		return bias * expected;
	}

	private double estimatePixel(PGMImage image, int pixel) {
		return (image.raster[pixel] - filterPixel(image, pixel)) * (image.raster[pixel] - (image.raster[pixel]^1));
	}

	private double filterPixel(PGMImage image, int pixel) {
		int x = pixel % image.width - 1;
		int y = pixel / image.width - 1;

		double value = 0;

		for (int fx = 0; fx < 3; fx++) {
			for (int fy = 0; fy < 3; fy++) {
				value += image.getPixel(x + fx, y + fy)*filter[fx][fy];
			}
		}

		return value;
	}

	private double filterCoverPixel(PGMImage image, int pixel) {
		int x = pixel % image.width - 1;
		int y = pixel / image.width - 1;

		double value = 0;

		for (int fx = 0; fx < 3; fx++) {
			for (int fy = 0; fy < 3; fy++) {
				int pixelValue = image.getPixel(x + fx, y + fy);
				value += ((pixelValue ^ 1) - pixelValue) * filter[fx][fy];
			}
		}

		return value;
	}

	private double weightPixel(PGMImage image, int pixel) {
		int x = pixel % image.width;
		int y = pixel / image.width;

		double variance = this.getVariance(
				image.getPixel(x - 1, y),
				image.getPixel(x + 1, y),
				image.getPixel(x, y - 1),
				image.getPixel(x, y + 1)
		);

		return 1.0d / (1.0d + variance);
	}

	private double getVariance(double a, double b, double c, double d) {
		double Ex = (a + b + c + d) / 4.0;
		double Ex_squared = (a * a + b * b + c * c + d * d) / 4.0d;

		return Ex_squared - Ex*Ex;
	}
}
