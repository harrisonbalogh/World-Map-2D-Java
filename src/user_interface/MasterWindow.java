package user_interface;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.awt.Color;

import main.ImageLoader;
import main.Startup;
import user_interface.data_components.SearchBoundary;
import user_interface.data_components.StudyBoundary;
import user_interface.map_components.MapPanel;

import java.awt.BorderLayout;

public class MasterWindow extends JFrame {
	
	// The dimensions of the window
	private final int WINDOW_WIDTH = 900;
	private final int WINDOW_HEIGHT = 500;
	// The dimensions of the world inside the world panel
	private final int WORLD_WIDTH = 1425;
	private final int WORLD_HEIGHT = 742;
	
	private JPanel panel_overlayUIPanel = new JPanel();
	private JLabel label_mouseX = new JLabel("-");
	private JLabel label_mouseY = new JLabel("-");
	private JTextField txtField_goToX = new JTextField(4);
	private JTextField txtField_goToY = new JTextField(4);
	private MapPanel worldPanel;
	
	/**
	 * The primary window for the application.
	 */
	public MasterWindow() {
		initializeWindow();
		initializeMapPanel();
		initializeOverlayUI();
	}

	/**
	 * Initialize the parent JFrame. 
	 * <p>
	 * The initialization of these attributes is split into 
	 * this method to signify that the window is more than 
	 * likely to change later in development while the map 
	 * panel's initializaiton stays more or less the same.
	 */
	private void initializeWindow() {
		// Attributes of the parent window
		getContentPane().setBackground(Color.WHITE);
		setTitle("Window Title");
		setBounds(100, 100, WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		getContentPane().setLayout(new BorderLayout(0, 0));
		this.addComponentListener(new ComponentListener() {
			@Override
			public void componentResized(ComponentEvent e) {
				// Keep overlayUIPanel in same spot as window resizes
				panel_overlayUIPanel.setBounds(
						-1, (int)getSize().getHeight() - panel_overlayUIPanel.getHeight() - 30, 
						(int)getSize().getWidth()+2, 50);
			}
			@Override
			public void componentMoved(ComponentEvent e) { }
			@Override
			public void componentShown(ComponentEvent e) { }
			@Override
			public void componentHidden(ComponentEvent e) { }
		});
	}
	/**
	 * Initialize the map panel itself. 
	 * <p>
	 * This panel draws the world map from
	 * the JPanel subclass ui.WorldPanel.
	 */
	private void initializeMapPanel() {
		worldPanel = new MapPanel(WORLD_WIDTH, WORLD_HEIGHT, 0, 0);
		getContentPane().add(worldPanel);
	}
	
	/**
	 * Initialize added UI components for map control.
	 * <p>
	 * Any tester UI components should be added here. These
	 * components are split into their own initialization to
	 * signify that they could be removed or relocated as the
	 * map controls are modified.
	 */
	private void initializeOverlayUI() {
		// Parent panel for all overlay UI =======
		panel_overlayUIPanel.setBounds(-1, (int)getSize().getHeight() - 50 - 30, worldPanel.getWidth()+2, 50);
		panel_overlayUIPanel.setBackground(new Color(255,255,255,200));
		panel_overlayUIPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		this.getLayeredPane().add(panel_overlayUIPanel, 1, 0);
		// Handy map control UI components ========
		JButton button_zoomOut = new JButton(new ImageIcon(ImageLoader.image_zoom_out));
		JButton button_zoomIn = new JButton(new ImageIcon(ImageLoader.image_zoom_in));
		JButton button_addRandomPins = new JButton(new ImageIcon(ImageLoader.image_pins_add));
		JButton button_clearAllPins = new JButton(new ImageIcon(ImageLoader.image_pins_clear));
		JCheckBox checkBox_toggleGridLines = new JCheckBox("Grid");
		JButton button_goTo = new JButton("Go");
		JButton button_createStudy = new JButton("Create Study");
		JButton button_loadStudy = new JButton("Load Study");
		JButton button_search = new JButton("Search");
		JButton button_uploadStudy = new JButton("Upload Study");
		// Tooltips
		button_clearAllPins.setToolTipText("Clear all data pins on the map.");
		button_zoomOut.setToolTipText("Zoom map out.");
		button_zoomIn.setToolTipText("Zoom map in.");
		button_addRandomPins.setToolTipText("Randomly add data pins across map.");
		checkBox_toggleGridLines.setToolTipText("Toggle between the world map's grid lines being visible or not.");
		button_goTo.setToolTipText("Scroll map to input coordinates,");
		button_createStudy.setToolTipText("Open a dialog for creating a new study.");
		button_loadStudy.setToolTipText("Load a study.");
		// Visual modifications
		button_zoomOut.setContentAreaFilled(false);
		button_zoomOut.setBorderPainted(false);
		button_zoomIn.setContentAreaFilled(false);
		button_zoomIn.setBorderPainted(false);
		button_clearAllPins.setEnabled(false);
		checkBox_toggleGridLines.setSelected(true);
		button_clearAllPins.setContentAreaFilled(false);
		button_clearAllPins.setBorderPainted(false);
		button_addRandomPins.setContentAreaFilled(false);
		button_addRandomPins.setBorderPainted(false);
		// Action listeners
		button_addRandomPins.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				worldPanel.testDataPins();
				button_clearAllPins.setEnabled(true);
			}
		});
		button_clearAllPins.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				worldPanel.clearDataPins();
				button_clearAllPins.setEnabled(false);
			}
		});
		button_goTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				worldPanel.testPanTo(getGoToX(), getGoToY());
			}
		});
		button_zoomOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				worldPanel.zoomOut();
			}
		});
		checkBox_toggleGridLines.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				worldPanel.gridLinesVisibleToggle();
			}
		});
		button_zoomIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				worldPanel.zoomIn();
			}
		});
		button_createStudy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StudyBoundary studyBoundary = new StudyBoundary();
				studyBoundary.pack();
				studyBoundary.setVisible(true);
			}
		});
		button_loadStudy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser jf = new JFileChooser();
				int returnVal = jf.showOpenDialog(worldPanel);
				if (returnVal == JFileChooser.APPROVE_OPTION){
					File file = jf.getSelectedFile();
					StudyBoundary studyBoundary = new StudyBoundary(file.getAbsolutePath());
					studyBoundary.pack();
					studyBoundary.setVisible(true);
				}
			}
		});
		button_search.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SearchBoundary searchBoundary = new SearchBoundary();
				searchBoundary.pack();
				searchBoundary.setVisible(true);
			}
		});
		button_uploadStudy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		// Label attributes
		label_mouseX.setHorizontalAlignment(SwingConstants.CENTER);
		label_mouseX.setPreferredSize(new Dimension(36, 10));
		label_mouseY.setHorizontalAlignment(SwingConstants.CENTER);
		label_mouseY.setPreferredSize(new Dimension(36, 10));
		// Add to parent panel
		panel_overlayUIPanel.add(button_addRandomPins);
		panel_overlayUIPanel.add(button_clearAllPins);
		panel_overlayUIPanel.add(txtField_goToX);
		panel_overlayUIPanel.add(label_mouseX);
		panel_overlayUIPanel.add(txtField_goToY);
		panel_overlayUIPanel.add(label_mouseY);
		panel_overlayUIPanel.add(button_goTo);
		panel_overlayUIPanel.add(button_zoomOut);
		panel_overlayUIPanel.add(button_zoomIn);
		panel_overlayUIPanel.add(checkBox_toggleGridLines);
		panel_overlayUIPanel.add(button_createStudy);
		panel_overlayUIPanel.add(button_loadStudy);
		panel_overlayUIPanel.add(button_search);
		panel_overlayUIPanel.add(button_uploadStudy);
		// Fix runtime resize glitch for overlayUIPanel.
		panel_overlayUIPanel.setBounds(
				-1, (int)getSize().getHeight() - panel_overlayUIPanel.getHeight() - 30, 
				(int)getSize().getWidth()+2, 50);
	}
	
	/**
	 * Gets the value in the 'Go To' button's x coordinate textfield
	 * <p>
	 * This getter will attempt to parse an integer from the x coordinate textfield.
	 * @return The input integer or random if failed to parse an integer.
	 */
	public int getGoToX() {
		int x = 0;
		try {
			x = Integer.parseInt(txtField_goToX.getText());
		} catch (NumberFormatException e) {
			x = Startup.rand.nextInt(WORLD_WIDTH);
		}
		return x;
	}
	
	/**
	 * Gets the value in the 'Go To' button's y coordinate textfield
	 * <p>
	 * This getter will attempt to parse an integer from the y coordinate textfield.
	 * @return The input integer or random if failed to parse an integer.
	 */
	public int getGoToY() {
		int y = 0;
		try {
			y = Integer.parseInt(txtField_goToY.getText());
		} catch (NumberFormatException e) {
			y = Startup.rand.nextInt(WORLD_HEIGHT);
		}
		return y;
	}
	
	public void setTxtFieldMouseX(int x) {
		label_mouseX.setText("" + x);
	}
	
	public void setTxtFieldMouseY(int y) {
		label_mouseY.setText("" + y);
	}
}
