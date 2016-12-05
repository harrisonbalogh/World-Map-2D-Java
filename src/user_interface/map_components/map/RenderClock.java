package user_interface.map_components.map;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import user_interface.map_components.MapPanel;

public class RenderClock {
	
	public static ArrayList<Long> timings = new ArrayList<Long>();
	public static int numberOfSecondsRun = 0;
	// Update cycle
	public static final double CLOCK_HERTZ = 30;
	
	private MapPanel parentPanel;
	
	final float NANO_SECONDS = 1000000000;
	Boolean appRunning = true;
	Boolean paused = false;
	Object pauseLock = new Object();
	
	/**
	 * Loops the draw and update principle methods of a 2D scene. 
	 * <p>
	 * Also deploys interpolation for smoother rendering. This saves CPU performance without hindering visual quality.
	 * @param parent - HXClock requires access to the panel it is being used on in order to reference update and rendering methods within its parent.
	 */
	public RenderClock(MapPanel parent) {
		this.parentPanel = parent;
		initialize();
	}

	/* === FIXED TIMESTEP LOOP === */
	private void initialize() {
		new Thread() {
			public void run() {
				// Time between repaints
				// Convert to nanoseconds for update cycle value
				final double TIME_BETWEEN_UPDATES = NANO_SECONDS / CLOCK_HERTZ;
				// Terminal number of updates allowed before repaint
				final int MAX_UPDATES_BEFORE_RENDER = 5; // Adjust if there are any visual hiccups
				
				double lastUpdateTime = System.nanoTime();
				double lastRenderTime = System.nanoTime();

				// Limits frame rate to the target constant, saves CPU
				final double TARGET_FPS = 30; //60
				// Convert to nanseconds
				final double TARGET_TIME_BETWEEN_RENDERS = NANO_SECONDS / TARGET_FPS;

				// Only used to track frame rate and display when a real time second has passed
				int lastSecondTime = (int) (lastUpdateTime / NANO_SECONDS);

				while (appRunning) {
					double now = System.nanoTime();
					int updateCount = 0;

					if (!paused) {
						// Attempts to do as many updates as are allowed before a render
						while (now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER) {
							updateWorld();
							lastUpdateTime += TIME_BETWEEN_UPDATES;
							updateCount++;
//							System.out.println("    + update");
						}
						// The follow prevents a really slow update from forcing too many catchups
						// This does not keep exact time
						if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
							lastUpdateTime = now - TIME_BETWEEN_UPDATES;
						}
						// Render. To do so, we need to calculate interpolation for a smooth render.
						float interpolation = Math.min(1.0f, (float) ((now - lastUpdateTime) / TIME_BETWEEN_UPDATES));
						drawWorld(interpolation);
						lastRenderTime = now;
						// FPS and timing code
						int thisSecond = (int) (lastUpdateTime / NANO_SECONDS);
						if (thisSecond > lastSecondTime) {
//							System.out.println("Second has passed " + thisSecond + ": updates -> " + updateCount);
							numberOfSecondsRun++;
//							fps = frameCount;
//							frameCount = 0;
							lastSecondTime = thisSecond;
						}
						//Yield until it has been at least the target time between renders. Saves CPU
			            while ( now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES)
			            {
			               Thread.yield();
			               // Reduces time accuracy but saves CPU. May cause stuttering. Check on different OS's
			               try {Thread.sleep(1);} catch(Exception e) {} 
			               now = System.nanoTime();
			            }
					} else {
						// Allows clock to start back up after a pause
						synchronized (pauseLock) {
							try {
								pauseLock.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
				
				// The following is only used for benchmarking
				List<Long> list = timings;
				BigInteger sum = list.stream().map(BigInteger::valueOf).reduce((x, y) -> x.add(y)).get();
				BigDecimal average = new BigDecimal(sum).divide(BigDecimal.valueOf(list.size()), RoundingMode.HALF_UP);
			    
				System.out.println("Average update timing: \t" + average + " \tns after " + numberOfSecondsRun + " seconds.");
			}
		}.start();
	}
	public void setPaused(Boolean paused) {
		this.paused = paused;
		synchronized (pauseLock) {
			pauseLock.notifyAll();
		}
	}
	public Boolean getPaused() {
		return paused;
	}
	
	public void updateWorld() {
		// Benchmarking updates
		long startTime = System.nanoTime(); 
		
		parentPanel.updateWorld(); //getWorld().updateEntities();
		
		long estimatedTime = System.nanoTime() - startTime;
		timings.add(estimatedTime);
	}
	public void drawWorld(float interpolation) {
		parentPanel.repaintWorld(interpolation);
	}
}