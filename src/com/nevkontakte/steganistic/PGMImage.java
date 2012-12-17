package com.nevkontakte.steganistic;

import java.io.*;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Scanner;

public class PGMImage {
	public final int width;
	public final int height;
	public final int depth;

	public final int[] raster;

	public PGMImage(int width, int height, int depth) {
		this.width = width;
		this.height = height;
		this.depth = depth;

		this.raster = new int[width * height];
	}

	public PGMImage(InputStream source) throws IllegalFormatException, IOException {
		DataInputStream dis = new DataInputStream(source);

		String magic = dis.readLine();

		if (!magic.equals("P5")) {
			throw new IllegalArgumentException("Magic sequence mismatch");
		}

		String line;
		do {
			line = dis.readLine();
		} while (line.startsWith("#"));

		Scanner s = new Scanner(line);
		this.width = s.nextInt();
		this.height = s.nextInt();

		line = dis.readLine();
		s = new Scanner(line);
		this.depth = s.nextInt();

		if (this.depth >= 256) {
			throw new IllegalArgumentException("Color depth larger than 255 isn't supported.");
		}

		this.raster = new int[width * height];

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				this.raster[i * width + j] = dis.readUnsignedByte();
			}
		}
	}

	public PGMImage(PGMImage other) {
		this.width = other.width;
		this.height = other.height;
		this.depth = other.depth;

		this.raster = Arrays.copyOf(other.raster, other.raster.length);;
	}

	public void save(OutputStream destination) throws IOException {
		DataOutputStream dos = new DataOutputStream(destination);

		// Write heander
		dos.writeBytes("P5\n");
		dos.writeBytes(Integer.toString(this.width));
		dos.write(' ');
		dos.writeBytes(Integer.toString(this.height));
		dos.write('\n');
		dos.writeBytes(Integer.toString(this.depth));
		dos.write('\n');

		for (int pixel : this.raster) {
			dos.writeByte(pixel);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PGMImage)) {
			return false;
		}
		PGMImage other = (PGMImage) obj;

		return other.width == this.width &&
				other.height == this.height &&
				other.depth == this.depth &&
				Arrays.equals(other.raster, this.raster);
	}

	public int getPixel(int x, int y) {
		x = Math.min(Math.max(x, 0), width-1);
		y = Math.min(Math.max(y, 0), height-1);
		int pixel = y * width + x;
		return this.raster[pixel];
	}
}
