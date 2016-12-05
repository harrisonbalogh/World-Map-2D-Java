package user_interface.map_components.map.entities;

import java.awt.Graphics;

import main.ImageLoader;
import user_interface.map_components.map.MapEntity;

public class MapDataPinEntity extends MapEntity {

	private final int DEFAULT_WIDTH = 20, DEFAULT_HEIGHT = 20;// 40;

	public MapDataPinEntity(int xPos, int yPos, double zoom) {

		init(ImageLoader.image_pin_data, xPos, yPos, DEFAULT_WIDTH, DEFAULT_HEIGHT, zoom);

	}

	@Override
	public void update() {
		super.update();

	}

	@Override
	public void draw(Graphics g, float interpolation) { 
		this.setDraw_xPos((int) (((this.getxPos() - this.getxPos_Prev()) * interpolation + this.getxPos_Prev()) * this.getScale()));
		this.setDraw_yPos((int) (((this.getyPos() - this.getyPos_Prev()) * interpolation + this.getyPos_Prev()) * this.getScale()));

		// Draw image of this entity, does nothing if no img is set			
		g.drawImage( this.getImg(),
				(int) (this.getDraw_xPos()) - DEFAULT_WIDTH/2, 
				(int) (this.getDraw_yPos()) - DEFAULT_HEIGHT, 
				DEFAULT_WIDTH, 
				DEFAULT_HEIGHT, 
				null);
	}
}