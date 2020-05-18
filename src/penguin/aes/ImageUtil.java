package penguin.aes;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtil {
	private int width;
	private int height;
	private int[] pixels; 
	BufferedImage image;
	
	public ImageUtil(String imageName) {
		try {
			image = ImageIO.read(new File(imageName));
		}
		catch (IOException ex) {
			throw new RuntimeException("Could not open file: " + imageName + ": " + ex.getMessage());
		}
		if (image == null) {
			throw new RuntimeException("Invalid image file: " + imageName);
		}
		width = image.getWidth();
		height = image.getHeight();
		pixels = image.getRGB(0, 0, width, height, null, 0, width);
	}
	
	public byte[] readImage2Byte() {
		byte[] imageBytes = new byte[width * height * 3];
		int index = 0;
		for (int y = 0; y < height; y++) { 
			for (int x = 0; x < width; x++) {
				int rgb = pixels[y * width + x];
				imageBytes[index++] = (byte)((rgb >> 16) & 0xFF);
				imageBytes[index++] = (byte)((rgb >> 8) & 0xFF);
				imageBytes[index++] = (byte)(rgb & 0xFF);
			}
		}
		return imageBytes;
	}
	
	private BufferedImage newImage(byte[] newImageBytes) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		pixels = image.getRGB(0, 0, width, height, null, 0, width);	
		int index = 0;
		for (int y = 0; y < height; y++) {    
			for (int x = 0; x < width; x++) {
				int red = newImageBytes[index++];
				int green = newImageBytes[index++];
				int blue = newImageBytes[index++];
				Color color = new Color(red, green, blue);
				image.setRGB(x, y, color.getRGB());
			}
		}
		return image;
	}
	
	public void saveNewImage(String imageName, String encryptMode, byte[] newImageBytes) {
		final String newName;
		BufferedImage newImage = newImage(newImageBytes);
		
		int splitIndex = imageName.lastIndexOf(".");
		if(imageName.endsWith(".bmp")) {
			newName = imageName.substring(0, splitIndex) + "_" + encryptMode;
		}else {
			System.err.println("WARNING: could not save " + imageName + ".  Must be .bmp");
			return;
		}
		try {
			ImageIO.write(newImage, "jpg", new File(newName));
		} catch (IOException ex) {
			System.err.println("WARNING: IO error while saving " + newName + ": " + ex.getMessage());
		}
	}

}
