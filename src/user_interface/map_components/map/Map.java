package user_interface.map_components.map;

import java.awt.Graphics;
import java.awt.Point;
import java.util.concurrent.CopyOnWriteArrayList;

import main.ImageLoader;
import user_interface.map_components.map.entities.*;

public class Map {
	
	private int width;
	private int height;
	
	/* === Updates and drawing === */
	private final CopyOnWriteArrayList<MapStudyPinEntity> pins = new CopyOnWriteArrayList<MapStudyPinEntity>();
	private final MapBackgroundEntity background;
	
	/**
	 * The HXWorld object owns all entities in a CopyOnWriteArrayList but is drawn in a HXWorldPanel.
	 * <p>
	 * @param parentPanel - The JPanel that draws the world.
	 */
	public Map(int w, int h) {
		this.width = w;
		this.height = h;
		
		// Run anything at start of world...
		background = new MapBackgroundEntity(this);
		// ...
	}
	
	/**
	 * Called by ST3WorldPanel when a mouse intersection is recognized by mouse listener
	 * @param x - location of mouse press in world
	 * @param y - location of mouse press in world
	 */
	public void mousePress(int x, int y) {
//		System.out.println("Mouse press on world! (" + x + ", " + y + ")");
		Boolean intersectedSomething = false;
		for (MapStudyPinEntity p : pins) {
			if (p instanceof Clickable) {
				if (p.getRect().contains(x, y)) {
					intersectedSomething = true;
					((Clickable) p).mouseIntersection();
					break;
				}
			}
		}
		if (!intersectedSomething) {
			if (MapStudyPinEntity.pinSelected != null)
				MapStudyPinEntity.pinSelected.setImg(ImageLoader.image_pin);
			MapStudyPinEntity.pinSelected = null;
		}
	}
	
	public void draw(Graphics g, float interpolation) {
		background.draw(g, interpolation);
		for (MapStudyPinEntity p : pins)
			p.draw(g, interpolation);
	}

	public void update() {
		for (MapStudyPinEntity p : pins) {
			p.update();
		}
	}
	
	public void backgroundMapGridLinesVisibleToggle() {
		background.setGridLinesVisibleToggle();
	}
	
	/**
	 * Update all StudyPins and BackgroundMap
	 * @param scale - The scale of the entities
	 */
	public void updateEntitiesScale(double scale) {
		for (MapStudyPinEntity p : pins) {
			p.setScale(scale);
		}
		background.setScale(scale);
	}
	
	public void markDataLocations(Point[] p, double zoom) {
		pins.add(new MapStudyPinEntity(p, zoom));
	}
	
	public void clearPins() {
		pins.clear();
	}

	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
}