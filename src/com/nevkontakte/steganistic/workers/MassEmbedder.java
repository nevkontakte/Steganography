package com.nevkontakte.steganistic.workers;

import com.nevkontakte.steganistic.LSBReplace;
import com.nevkontakte.steganistic.PGMImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MassEmbedder {
	public void run(File srcDir, File destDir) throws IOException {
		if (!srcDir.isDirectory() || !destDir.isDirectory()) {
			throw new IllegalArgumentException("Source or destination is not a directory!");
		}

		LSBReplace replacer = new LSBReplace(0.05);

		//noinspection ConstantConditions
		for (File f : srcDir.listFiles()) {
			if (!f.getName().endsWith(".pgm")) {
				continue;
			}
			System.out.println(f);

			FileInputStream originalStream = new FileInputStream(f);
			PGMImage image = new PGMImage(originalStream);
			originalStream.close();

			replacer.embed(image);

			File resultFile = new File(destDir, f.getName());
			FileOutputStream resultStream = new FileOutputStream(resultFile);
			image.save(resultStream);
			resultStream.close();
		}
	}

	public static void main(String[] args) throws IOException {
		MassEmbedder worker = new MassEmbedder();
		worker.run(new File("samples/clean"), new File("samples/embedded"));
	}
}
