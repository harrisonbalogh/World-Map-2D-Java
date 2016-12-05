package main;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {
	
	public static BufferedImage image_map;
	public static BufferedImage image_pin;
	public static BufferedImage image_pin_data;
	public static BufferedImage image_pin_selected;
	public static BufferedImage image_zoom_in;
	public static BufferedImage image_zoom_out;
	public static BufferedImage image_pins_add;
	public static BufferedImage image_pins_clear;
	
	public ImageLoader() {
		 try 
		    {  
				 image_map = ImageIO.read(ImageLoader.class.getResourceAsStream("/resources/map.png")); 
				 image_pin = ImageIO.read(ImageLoader.class.getResourceAsStream("/resources/pin2.png"));
				 image_pin_data = ImageIO.read(ImageLoader.class.getResourceAsStream("/resources/pin_data.png"));
				 image_pin_selected = ImageIO.read(ImageLoader.class.getResourceAsStream("/resources/pin_selected2.png"));
				 image_zoom_in = ImageIO.read(ImageLoader.class.getResourceAsStream("/resources/pinRemove.png"));
				 image_zoom_out = ImageIO.read(ImageLoader.class.getResourceAsStream("/resources/pinAdd.png"));
				 image_pins_add = ImageIO.read(ImageLoader.class.getResourceAsStream("/resources/zoomIn.png"));
				 image_pins_clear = ImageIO.read(ImageLoader.class.getResourceAsStream("/resources/zoomOut.png"));
		    } 
		    catch (IOException e) 
		    { 
		      //Not handled. 
		    } 
	}
	
	private static BufferedImage scaleRetinaImage(BufferedImage img) {
		BufferedImage before = img;
		int w = before.getWidth() / 2;
		int h = before.getHeight() / 2;
		BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(0.5, 0.5);
		AffineTransformOp scaleOp = 
		   new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		after = scaleOp.filter(before, after);
		return after;
	}
}
