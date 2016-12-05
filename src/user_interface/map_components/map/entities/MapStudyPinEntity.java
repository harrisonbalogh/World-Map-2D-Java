package user_interface.map_components.map.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import main.ImageLoader;
import user_interface.map_components.map.Clickable;
import user_interface.map_components.map.MapEntity;

public class MapStudyPinEntity extends MapEntity implements Clickable{

	private final int DEFAULT_WIDTH = 20, DEFAULT_HEIGHT = 40;// 40;
	private final int CLICK_RECT_W = 20, CLICK_RECT_H = 40;//40;
	
	private ArrayList<MapDataPinEntity> dataPins = new ArrayList<MapDataPinEntity>();
	
	public static MapStudyPinEntity pinSelected = null;
	
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
	public MapStudyPinEntity(Point[] dataPoints, double zoom) {
		double xAvg = 0;
		double yAvg = 0;
		for (int e = 0; e < dataPoints.length; e++) {
			xAvg += dataPoints[e].x;
			yAvg += dataPoints[e].y;
			dataPins.add(new MapDataPinEntity(dataPoints[e].x, dataPoints[e].y, zoom));
		}
		xAvg /= dataPoints.length;
		yAvg /= dataPoints.length;
		
		init(ImageLoader.image_pin, xAvg, yAvg, DEFAULT_WIDTH, DEFAULT_HEIGHT, zoom);
		
		this.getRect().setBounds(
				(int) xAvg - DEFAULT_WIDTH/2 - (CLICK_RECT_W - DEFAULT_WIDTH)/2, 
				(int) yAvg - DEFAULT_HEIGHT - (CLICK_RECT_H - DEFAULT_HEIGHT)/2, 
				CLICK_RECT_W, 
				CLICK_RECT_H);
	}

	@Override
	public void update() {
		super.update();
		
		this.getRect().setLocation(
				(int) (getDraw_xPos() - DEFAULT_WIDTH/2 - (CLICK_RECT_W - DEFAULT_WIDTH)/2), 
				(int) (getDraw_yPos() - DEFAULT_HEIGHT - (CLICK_RECT_H - DEFAULT_HEIGHT)/2));
	}

	@Override
	public void draw(Graphics g, float interpolation) { 
		this.setDraw_xPos((int) (((this.getxPos() - this.getxPos_Prev()) * interpolation + this.getxPos_Prev()) * this.getScale()));
		this.setDraw_yPos((int) (((this.getyPos() - this.getyPos_Prev()) * interpolation + this.getyPos_Prev()) * this.getScale()));
		
		if (pinSelected == this) {
			for (MapDataPinEntity d : dataPins) {
				d.draw(g, interpolation);
			}
			
			for (MapDataPinEntity d : dataPins) {
				int ind = dataPins.indexOf(d);
				int nextLocX = d.getDraw_xPos();
				int nextLocY = d.getDraw_yPos();
				if (ind != dataPins.size()-1) {
					nextLocX = dataPins.get(ind+1).getDraw_xPos();
					nextLocY = dataPins.get(ind+1).getDraw_yPos();
				}
				g.setColor(Color.lightGray);
				g.drawLine(
						(int) (d.getDraw_xPos()), 
						(int) (d.getDraw_yPos()),
						nextLocX, 
						nextLocY);
				
				g.setColor(Color.white);
				g.fillOval(d.getDraw_xPos() - 2, d.getDraw_yPos() - 13, 14, 14);
				g.setColor(Color.black);
				g.drawString(ind + 1 + "", d.getDraw_xPos(), d.getDraw_yPos());
			}
		} else {
			// Draw image of this entity, does nothing if no img is set			
			g.drawImage( this.getImg(),
					(int) (this.getDraw_xPos()) - DEFAULT_WIDTH/2, 
					(int) (this.getDraw_yPos()) - DEFAULT_HEIGHT, 
					DEFAULT_WIDTH, 
					DEFAULT_HEIGHT, 
					null);
		}
	}
	
	/**
	 * From the Clickable interface. Add functionality for what happens when the object is clicked on in world
	 */
	@Override
	public void mouseIntersection() {
		if (pinSelected == this) {
			pinSelected = null;
			this.setImg(ImageLoader.image_pin);
		} else {
			if (pinSelected != null)
				pinSelected.setImg(ImageLoader.image_pin);
			pinSelected = this;
			this.setImg(ImageLoader.image_pin_selected);
		}
	}
	
	@Override
	public void setScale(double s) {
		super.setScale(s);
		for (MapDataPinEntity d : dataPins) {
			d.setScale(this.getScale());
		}
	}
}