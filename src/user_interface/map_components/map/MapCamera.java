package user_interface.map_components.map;

import user_interface.map_components.MapPanel;

public class MapCamera {
	
	private MapPanel parentPanel;
	private double updateHertz; // is called often so will keep local reference
	
	/* === Panning === */
	private final int CAM_VEL_TERMINAL = 120; // - Maximum speed of drag panning
	private final int CAM_FRICTION = 15; // - Deceleration of camera movement after mouse released in panning
	private final double CAM_MODIFIER = 1; // - Used to amplify or suppress speed of drag panning
	private final int CAM_VIEW_BORDER = 50; // - Allows camera to leave map bounds by this value on ea. side
	private final double CAM_PAN_TO_TIME = 0.25; // - Time it takes to pan to a given point (in seconds)
	/* === Zooming === */
	private final int ZOOM_MAX = 20;
	private final int ZOOM_MIN = 1;
	private final double ZOOM_INCREMENT = 0.1;
	
	// Camera X,Y position is the point at which 
	// the camera's viewport is centered on...
	private int camera_pos_x;
	private int camera_pos_y;
	// Used in dragPan()
	private double camera_speedSquared = 0; //magnitude, not square rooted to save CPU
	private double camera_direction = 0; //direction
	// Timer for panning to a given point. -1 Timer stops panning update calls
	private double camera_panTimeRemaining = -1;
	// Current location of panning animation. Need this variable double for integer rounding problems
	private double camera_panLocationX = 0;
	private double camera_panLocationY = 0;
	// Saves the point where the panTo is intending to end
	private int camera_panToX;
	private int camera_panToY;
	// Calculates dX/dY from constant dT in updatePanning calls for panTo()
	private double camera_deltaX;
	private double camera_deltaY;
	
	private double zoom = 1;
	
	/**
	 * Controls the panning and zooming of the ST3World entities.
	 * Refactored from HXWorldPanel for clarity and organization.
	 * Will only be created once by a ST3WorldPanel.
	 */
	public MapCamera(MapPanel worldParent, double updateHertz) {
		this.parentPanel = worldParent;
		this.updateHertz = updateHertz;
		this.camera_pos_x = worldParent.getWidth()/2;
		this.camera_pos_y = worldParent.getHeight()/2;
		this.camera_panToX = camera_pos_x;
		this.camera_panToY = camera_pos_y;
	}
	
	// MARK: Panning ====================================
	
	/**
	 * Called only by ST3WorldPanel mouseDragged
	 */
	public void dragPan(int vel_x, int vel_y) {
		// Get velocity vector from passed mouse velocity
		camera_direction = Math.atan2(vel_y, vel_x);
		// Clamp speed to terminal velocity and don't square root to save CPU
		camera_speedSquared = Math.min(Math.pow(vel_x, 2) + Math.pow(vel_y, 2), CAM_VEL_TERMINAL);
		// Interrupts the camera if it is already panning to a point
		camera_panTimeRemaining = -1;
	}
	
	/**
	 * Focuses the center of the camera to a point.
	 * <p>
	 * The input points do not change as the map zooms in. Coordinates out of the bounds of the world
	 * will simply move to a border of the world and stop.
	 * @param x - coordinate 
	 * @param y - coordinate
	 */
	public void panTo(int x, int y) {
		// Subtract half the width and half the height so the panTo point is the top left of viewport
		// Divide by pan time constant to set velocity
		// Multply by dT (1/Hz) to get dX and dY, since dT is constant
		camera_deltaX = (((x*zoom - camera_pos_x) / CAM_PAN_TO_TIME) / updateHertz);
		camera_deltaY = (((camera_pos_y - y*zoom) / CAM_PAN_TO_TIME) / updateHertz);
		// Reduce time remaining by 1 dT call for appropriate number of update calls
		camera_panTimeRemaining = CAM_PAN_TO_TIME - 1 / updateHertz;
		// Need a variable of type double to decrement by dX/dY to avoid rounding in integers
		camera_panLocationX = camera_pos_x;
		camera_panLocationY = camera_pos_y;
		// Set the camera to its destination point when panning animation done to ensure exact point reached
		camera_panToX = (int) (x*zoom);
		camera_panToY = (int) (y*zoom);
	}
	public void panToImmediately(int x, int y) {
		camera_pos_x = (int) (x*zoom);
		camera_pos_y = (int) (y*zoom);
	}
	
	/**
	 * Called only by the updateWorld method in parent panel. Determinde by ST3Clock
	 */
	public void updatePanning() {
		// panTo takes precedence over dragPan
		if (camera_panTimeRemaining >= 0) {
			// Decrement timeRemaining by dT
			camera_panTimeRemaining -= 1 / updateHertz;
			// Move new intended location from pan
			camera_panLocationX += camera_deltaX;
			camera_panLocationY -= camera_deltaY;
			if (camera_panTimeRemaining < 0) {
				// Set the camera position to its intended pan location if this is the last update call
				camera_pos_x = camera_panToX;
				camera_pos_y = camera_panToY;
			} else {
				// Otherwise update the cameras position with current location of pan interval
				camera_pos_x = (int) camera_panLocationX;
				camera_pos_y = (int) camera_panLocationY;
			}
			// Ensure calculations are within world bounds
			borderCheck();
		} else if (camera_speedSquared > 0) {
			// Move camera by velocity vector
			camera_pos_x += camera_speedSquared * Math.cos(camera_direction) * CAM_MODIFIER;
			camera_pos_y -= camera_speedSquared * Math.sin(camera_direction) * CAM_MODIFIER;
			// Apply friction for camera deceleration
			camera_speedSquared -= CAM_FRICTION;
			if (camera_speedSquared < 0) {
				camera_speedSquared = 0;
			}
			// Ensure calculations are within world bounds
			borderCheck();
		}
	}
	
	/**
	 * Class internal use only 
	 * <br>
	 * Refactored out from updatePanning(). Ensures panning methods stay within the given bounds.
	 */
	private void borderCheck() {
		double leftBorder  = parentPanel.getWidth()/2 - CAM_VIEW_BORDER * zoom;
		double topBorder   = parentPanel.getHeight()/2 - CAM_VIEW_BORDER * zoom;
		double rightBorder = (parentPanel.getWorld().getWidth() + CAM_VIEW_BORDER) * zoom - parentPanel.getWidth()/2;
		double botBorder   = (parentPanel.getWorld().getHeight() + CAM_VIEW_BORDER) * zoom - parentPanel.getHeight()/2;
		
		if (camera_pos_x < leftBorder) {
			camera_pos_x = (int) leftBorder;
		} else if (camera_pos_x > rightBorder) {
			camera_pos_x = (int) rightBorder;
		}
		if (camera_pos_y < topBorder) {
			camera_pos_y = (int) topBorder;
		} else if (camera_pos_y > botBorder) {
			camera_pos_y = (int) botBorder;
		}
	}
	
	// Mark: Zooming ====================================
	public void setZoom(double zoom) {
		if (zoom < ZOOM_MAX && zoom > ZOOM_MIN) {
			updateWorldZoom(zoom);
		}
	} 
	/**
	 * Zoom the map in at the current focused coordinate
	 * <p>
	 * @return True if the zoom was successful, false if max zoom has been reached.
	 */
	public Boolean incrementZoom() {
		if (zoom < ZOOM_MAX) {
			updateWorldZoom(zoom + ZOOM_INCREMENT);
		} else {
			updateWorldZoom(ZOOM_MAX);
			return false;
		}
		return true;
	}
	/**
	 * Zoom the map out at the current focused coordinate
	 * <p>
	 * 
	 * @return True if the zoom was successful, false if min zoom has been reached.
	 */
	public Boolean decrementZoom() {
		if (zoom > ZOOM_MIN) {
			updateWorldZoom(zoom - ZOOM_INCREMENT);
		} else {
			updateWorldZoom(ZOOM_MIN);
			return false;
		}
		return true;
	}
	/**
	 * Called only by ST3WorldPanel mouseWheelMoved
	 * @param amount - Scroll wheel change amount
	 */
	public void scrollZoom(int amount) {
		if (zoom + amount * ZOOM_INCREMENT < ZOOM_MAX && zoom + amount * ZOOM_INCREMENT > ZOOM_MIN) {
			updateWorldZoom(zoom + amount * ZOOM_INCREMENT);
		} else if (zoom + amount * ZOOM_INCREMENT >= ZOOM_MAX) {
			updateWorldZoom(ZOOM_MAX);
		} else if (zoom + amount * ZOOM_INCREMENT <= ZOOM_MIN) {
			updateWorldZoom(ZOOM_MIN);
		}
	}
	
	/**
	 * Class internal use only 
	 * <br>
	 * Refactored out from all zoom modifying methods. Ensures camera stays centered as everything scales up or down.
	 */
	private void updateWorldZoom(double z) {
		// Camera has to move to stay centered on point
		camera_pos_x = (int) (camera_pos_x * (z / zoom));
		camera_pos_y = (int) (camera_pos_y * (z / zoom));
		zoom = z;
		// Update all entities' scale
		parentPanel.updateWorldEntitiesScale(zoom);
		// Keep inside bounds
		borderCheck();
	}
	
	public void updateSize() {
		borderCheck();
	}
	
	// ============================   MARK: Getters and Setters ============================ 
	// All getters are communicated explicitly to ST3WorldPanel
	// Classes outside of the ui package will have limited access to map properties
	
	public int getCamera_x() {
		return (int) (camera_pos_x - parentPanel.getWidth()/2);
	}
	public int getCamera_y() {
		return (int) (camera_pos_y - parentPanel.getHeight()/2);
	}
	public double getZoom() {
		return zoom;
	}
	public int convertPanelCoordToWorldCoord_x(int x) {
		int conv = getCamera_x() + x;
		return (int) (conv / zoom);
	}
	public int convertPanelCoordToWorldCoord_y(int y) {
		int conv = getCamera_y() + y;
		return (int) (conv / zoom);
	}
}
