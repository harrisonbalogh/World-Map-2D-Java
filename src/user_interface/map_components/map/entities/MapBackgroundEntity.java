package user_interface.map_components.map.entities;

import java.awt.Color;
import java.awt.Graphics;

import main.ImageLoader;
import user_interface.map_components.map.Map;
import user_interface.map_components.map.MapEntity;

public class MapBackgroundEntity extends MapEntity {

	private final int DEFAULT_WIDTH = 1425, DEFAULT_HEIGHT = 742;
	private final int GRID_SPACE = 25;
	private Boolean visibleGridLines = true;
	
	/**
	 * Template for creating a new entiity. Should always override drawing, to have
	 * some sort of visual appearence in the world. But should still call super.draw()
	 * Constructor should always call init() method of HXEntity
	 * @param xPos
	 * @param yPos
	 * @param xVel
	 * @param yVel
	 * @param w
	 */
	public MapBackgroundEntity(Map w) {
		init(ImageLoader.image_map, 0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT, 1);
	}
	
	/**
	 * Unless it has no visuals, it should override draw() but still call super.draw()
	 */
	@Override
	public void draw(Graphics g, float interpolation) {
		super.draw(g, interpolation);
		
		// The following adds the grid lines over the map
		g.setColor(Color.gray);
		// Check scale and adjust cut grid spacing in half accordingly
		// Have to use type double for rounding issues with integers
		if (visibleGridLines) {
			double gridSpacing = GRID_SPACE;
			if (this.getScale() >= 5 && this.getScale() < 14) {
				gridSpacing = 12.5;
			} else if (this.getScale() >= 14) {
				gridSpacing = 6.25;
			}
			// Draw lines from border to border
			for (double x = gridSpacing; x < DEFAULT_WIDTH; x += gridSpacing) {
				g.drawLine((int) (x * this.getScale()), 0, (int) (x * this.getScale()), (int) (DEFAULT_HEIGHT * this.getScale()));
			}
			for (double y = gridSpacing; y < DEFAULT_HEIGHT; y += gridSpacing) {
				g.drawLine(0, (int) (y * this.getScale()), (int) (DEFAULT_WIDTH * this.getScale()), (int) (y * this.getScale()));
			}
		}
	}
	
	public void setGridLinesVisibleToggle() {
		this.visibleGridLines = !this.visibleGridLines;
	}
}
