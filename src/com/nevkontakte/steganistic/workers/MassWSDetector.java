package com.nevkontakte.steganistic.workers;

import com.nevkontakte.steganistic.PGMImage;
import com.nevkontakte.steganistic.WSDetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MassWSDetector {
	public void run(File dir, double expected) throws IllegalAccessException, IOException {
		if (!dir.isDirectory()) {
			throw new IllegalAccessException("Supplied file is not a directory");
		}

		WSDetector d = new WSDetector();

		///
		/// Output format:
		/// File name <tab> Expected ratio <tab> Detected ratio
		///
		//noinspection ConstantConditions
		for (File f : dir.listFiles()) {
			if (!f.getName().endsWith(".pgm")) {
				continue;
			}
			FileInputStream input = new FileInputStream(f);
			double p = d.scanCorrected(new PGMImage(input), expected);
			input.close();
			System.out.println(
					dir.getName() + '/' + f.getName() + '\t'
					+ expected + '\t'
					+ p
			);
		}
	}

	public static void main(String[] args) throws Exception {
		MassWSDetector d = new MassWSDetector();
		d.run(new File("samples/clean"), 0);
		d.run(new File("samples/embedded"), 0.05);
	}
}
