package org.kyj.fx;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;

import org.deepsymmetry.GifSequenceWriter;

public class GifMaker {

	public static void main(String[] args) throws IOException {
		int width = 800;
		int height = 600;
		int delay = 500; // 100ms 지연

		File dir = new File("C:\\Users\\KYJ\\Pictures\\새 폴더");

		File[] listFiles = dir.listFiles();

		BufferedImage[] frames = new BufferedImage[listFiles.length];
		for (int i = 0; i < listFiles.length; i++) {
			File img = listFiles[i];
			BufferedImage inputImage = ImageIO.read(img);
			
			/* resize */
			java.awt.Image outputImage = inputImage.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
			BufferedImage outputBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = outputBufferedImage.createGraphics();
			g2.drawImage(outputImage, 0, 0, null);
			g2.dispose();
			 
			frames[i] = outputBufferedImage;
		}

		try (FileImageOutputStream out = new FileImageOutputStream(new File("animation.gif"))) {
			GifSequenceWriter writer = new GifSequenceWriter(out, frames[0].getType(), delay, true);
			for (BufferedImage image : frames) {
				writer.writeToSequence(image);
			}
			writer.close();
		}

	}

	private static void extracted(int width, int height, BufferedImage[] frames, int i) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, width, height);
		g2.setColor(Color.BLACK);
		g2.drawString("Frame " + (i + 1), width / 2 - 20, height / 2);
		g2.dispose();
		frames[i] = image;
	}

}
