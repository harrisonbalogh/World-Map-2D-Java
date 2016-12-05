package user_interface.map_components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import main.Startup;
import user_interface.MasterWindow;
import user_interface.map_components.map.Map;
import user_interface.map_components.map.MapCamera;
import user_interface.map_components.map.RenderClock;

public class MapPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, ComponentListener {
	private static final long serialVersionUID = 1L;
	
	/* === Drawing === */
	private float interpolation = 0;
	
	/* === 2D render canvas === */
	private Image environment;
	private Graphics graphics;
	
	/* === User Controls === */
	private MapCamera camera;
	private int mouse_x_last = 0;
	private int mouse_y_last = 0;
	
	/* === world === */
	private Map world;

	/**
	 * Extension of a JPanel that creates a map which draws map entities.
	 */
	public MapPanel(int worldWidth, int worldHeight, int worldStartX, int worldStartY) {
		setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setBackground(Color.DARK_GRAY);
		setLayout(null);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addComponentListener(this);
		
		this.camera = new MapCamera(this, RenderClock.CLOCK_HERTZ);
		this.world = new Map(worldWidth, worldHeight);
		new RenderClock(this);
	}
	
	public void paint(Graphics g) {
		// - Render setup
		environment = createImage(getWidth(), getHeight());
		graphics = environment.getGraphics();
		
		paintComponent(graphics);
		g.drawImage(environment, 0, 0, null);
		
		g.setColor(Color.lightGray);
		g.drawLine(0, 0, this.getWidth(), 0);
	}
	
	/**
	 * Called by repaintWorld(), which is called by the HXClock
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
//		Graphics2D g2d = (Graphics2D) g;
		// - Panel background
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		// Applies panning from camera
		g.translate(-camera.getCamera_x(), -camera.getCamera_y());
//		g2d.scale(camera.getZoom(), camera.getZoom());
		// Iterate backwards to draw in appropriate order
		world.draw(g, interpolation);
	}
	
	/**
	 * Marks visual pins on the map at the array of given locations.
	 * <p>
	 * The x and y coordinates should be a pixel coordinate from zero to the
	 * bounds of the ST3WorldPanel component, regardless of zoom.
	 * @param locs - The java.awt Point class location of marked points
	 */
	public void markStudyWithDataPoints(Point[] locs) {
		world.markDataLocations(locs, camera.getZoom());
	}
	
	/**
	 * Removes all the current pins marked on the map.
	 */
	public void clearPins() {
		world.clearPins();
	}
	
	/**
	 * Controls the drawing of grid lines over the world
	 * <p>
	 * @param v - If true, grid lines will be drawn on the
	 * world map. If false, grid lines will not be drawn.
	 */
	public void gridLinesVisibleToggle() {
		world.backgroundMapGridLinesVisibleToggle();
	}
	
	public void testPanTo(int x, int y) {
		camera.panTo(x, y);
	}
	
	public void testDataPins() {
		new Thread() {
			public void run() {
				for (int x = 0; x < 5; x++) {
					int xLoc = Startup.rand.nextInt(world.getWidth());
					int yLoc = Startup.rand.nextInt(world.getHeight());
					camera.panTo(xLoc, yLoc);
					try { Thread.sleep(320); } catch (InterruptedException e) { }
					Point[] p = new Point[1];
					p[0] = new Point(xLoc, yLoc);
					world.markDataLocations(p, camera.getZoom());
					try { Thread.sleep(100); } catch (InterruptedException e) { }
				}
			}
		}.start();
	}
	
	public void zoomIn() {
		camera.decrementZoom();
	}
	
	public void zoomOut() {
		camera.incrementZoom();
	}
	
	public void clearDataPins() {
		world.clearPins();
	}
	
	public void updateWorld() {
		camera.updatePanning();
		world.update();
	}
	
	public Map getWorld() {
		return world;
	}

	public void repaintWorld(float withInterpolation) {
		interpolation = withInterpolation;
		repaint();
	}
	
	public void updateWorldEntitiesScale(double s) {
		world.updateEntitiesScale(s);
	}

	/* ===================== Mouse Listener Methods ===================== */
	
	@Override
	public void mouseClicked(MouseEvent e) {
		world.mousePress(
				e.getX() + camera.getCamera_x(), 
				e.getY() + camera.getCamera_y());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouse_x_last = e.getX();
		mouse_y_last = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		setCursor(Cursor.getDefaultCursor());
	}

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

	@Override
	public void mouseDragged(MouseEvent e) {
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		camera.dragPan(mouse_x_last - e.getX(), e.getY() - mouse_y_last);
		mouse_x_last = e.getX();
		mouse_y_last = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		MasterWindow mw = (MasterWindow) SwingUtilities.getRoot(this);
		mw.setTxtFieldMouseX(camera.convertPanelCoordToWorldCoord_x(e.getX()));
		mw.setTxtFieldMouseY(camera.convertPanelCoordToWorldCoord_y(e.getY()));
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		camera.scrollZoom(e.getUnitsToScroll());		
	}
	@Override
	public boolean isOptimizedDrawingEnabled() {
		return false;
	}

	@Override
	public void componentResized(ComponentEvent e) {
		camera.updateSize();
	}

	@Override
	public void componentMoved(ComponentEvent e) { }

	@Override
	public void componentShown(ComponentEvent e) { }

	@Override
	public void componentHidden(ComponentEvent e) { }
}
