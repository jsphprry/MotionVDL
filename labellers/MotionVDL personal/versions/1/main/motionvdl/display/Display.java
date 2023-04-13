package motionvdl.display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import motionvdl.Debug;
import motionvdl.controller.MainController;
import motionvdl.display.component.Canvas;
import motionvdl.model.util.Point;

@SuppressWarnings("serial")
public class Display extends JFrame {
	
	// properties
	public final int width;
	public final int height;
	
	// components
	private Canvas canvas;
	private JPanel controlPanel;
	private JMenuBar menu;
	private JMenu menuFile;
	private JMenuItem menuFileOpen;
	private JMenuItem menuFileSave;
	private JMenuItem menuFileSaveAs;
	private JMenu menuSettings;
	private JRadioButtonMenuItem menuSettingsAuto;
	private JRadioButtonMenuItem menuSettingsDebug;
	private JLabel titleLabel;
	private JButton completeButton;
	private JPanel framePanel;
	private JButton frameNextButton;
	private JLabel frameLabel;
	private JButton framePrevButton;
	private JPanel targetPanel;
	private JLabel targetLabel;
	private JTextField targetInput;
	private JFileChooser fileChooser;
	
	// receiving controller
	private MainController receiver;
	
	/**
	 * Construct display
	 * @param width The window width
	 * @param height The window height
	 */
	public Display(int width, int height){

		// setup window
		super("MotionVDL");
		setSize(width,height);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocation(100, 100);

		// setup properties
		this.width = width;
		this.height = height;

		// setup components
        canvas = new Canvas(height,height);
        controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel,BoxLayout.Y_AXIS));

		titleLabel = new JLabel("MotionVDL");
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		completeButton = new JButton("Commit changes");
		completeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		completeButton.setMaximumSize(new Dimension(171, 20));
		
		menu = new JMenuBar();
		menu.setMaximumSize(new Dimension(Short.MAX_VALUE, 20));
		
		menuFile = new JMenu("File");
		menuFileOpen = new JMenuItem("Open");
		menuFileSave = new JMenuItem("Save");
		menuFileSaveAs = new JMenuItem("Save as");
		menuFile.add(menuFileOpen);
		menuFile.add(menuFileSave);
		menuFile.add(menuFileSaveAs);

		menuSettings = new JMenu("Options");
		menuSettingsAuto = new JRadioButtonMenuItem("Automatic next",true);
		menuSettingsDebug = new JRadioButtonMenuItem("Debug trace",true);
		menuSettings.add(menuSettingsAuto);
		menuSettings.add(menuSettingsDebug);

		menu.add(menuFile);
		menu.add(menuSettings);

		framePanel = new JPanel();
		framePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		framePanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 20));
		framePrevButton = new JButton("<");
		frameLabel = new JLabel("Frame control");
		frameNextButton = new JButton(">");
		framePanel.add(framePrevButton);
		framePanel.add(frameLabel);
		framePanel.add(frameNextButton);

		targetPanel = new JPanel();
		targetPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		targetPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 20));
		targetLabel = new JLabel("Target resolution:");
		targetInput = new JTextField(5);
		targetInput.setText("50");
		targetPanel.add(targetLabel);
		targetPanel.add(targetInput);

		controlPanel.add(menu);
    	controlPanel.add(Box.createRigidArea(new Dimension(0,4)));
    	controlPanel.add(titleLabel);
    	controlPanel.add(Box.createRigidArea(new Dimension(0,4)));
    	controlPanel.add(completeButton);
    	controlPanel.add(Box.createRigidArea(new Dimension(0,2)));
    	controlPanel.add(framePanel);
    	controlPanel.add(targetPanel);
    	controlPanel.add(Box.createVerticalGlue());

    	fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        getContentPane().add(BorderLayout.WEST, canvas);
        getContentPane().add(BorderLayout.EAST, controlPanel);
		pack();

    	/*menu.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.red),
                menu.getBorder()));
    	titleLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.red),
                titleLabel.getBorder()));
    	targetPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.red),
                targetPanel.getBorder()));
		completeButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.red),
                completeButton.getBorder()));
		framePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.red),
                framePanel.getBorder()));
		targetPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.red),
                targetPanel.getBorder()));*/

		// menu event listeners
        menuFileOpen.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					int action = fileChooser.showOpenDialog(null);
					if (action == JFileChooser.APPROVE_OPTION) {
						String location = fileChooser.getSelectedFile().getAbsolutePath();
						receiver.open(location);
					}
				}
			}
		});
        menuFileSave.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					receiver.save();
				}
			}
		});
        menuFileSaveAs.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					int action = fileChooser.showSaveDialog(null);
					if (action == JFileChooser.APPROVE_OPTION) {
						String location = fileChooser.getSelectedFile().getAbsolutePath();
						receiver.saveAs(location);
					}
				}
			}
		});
		menuSettingsDebug.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Debug.setup(true);
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					Debug.setup(false);
				}
			}
		});

    	// control panel event listeners
        canvas.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					receiver.click(
						e.getX() / (double) canvas.width,
						e.getY() / (double) canvas.height
					);
				} else {
					receiver.undo();
				}
			}
		});
		completeButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				receiver.complete();
			}
		});
		framePrevButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				receiver.prevFrame();
			}
		});
		frameNextButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				receiver.nextFrame();
			}
		});

		// set focus to target input
		targetInput.requestFocusInWindow();
	}




	/**
	 * Store a reference to the receiving controller
	 * @param controller The receiving controller
	 */
	public void sendTo(MainController controller) {
		this.receiver = controller;
	}




	/**
	 * Set image on display canvas
	 * @param frame Image as 2d Color array
	 */
	public void setFrame(Color[][] frame) {
		canvas.set(frame);
	}

	/**
	 * Clear image on display canvas
	 */
	public void clearFrame() {
		canvas.setDefault();
	}

	/**
	 * Set text in title area
	 * @param title Title string
	 */
	@Override
	public void setTitle(String title) {
		titleLabel.setText(title);
	}

	/**
	 * Set text in message area
	 * @param message
	 */
	public void setMessage(String message) {
		// TODO Auto-generated method stub

	}




	/**
	 * Get value of automatic-next option
	 * @return Option setting represented as boolean
	 */
	public boolean getAuto() {
		return menuSettingsAuto.isSelected();
	}

	/**
	 * Get target resolution from input field
	 * @return int value of text input, -1 if input is not numerical
	 */
	public int getTarget() {
		try {
			return Integer.parseInt(targetInput.getText());
		} catch (NumberFormatException e) {
			return -1;
		}
	}



	/**
	 * Draw a point on display canvas
	 * @param x Normalised x axis
	 * @param y Normalised y axis
	 */
	public void drawPoint(double x, double y) {
		canvas.drawPoint(x, y);
	}

	/**
	 * Draw a rectangle between points a and b on display canvas
	 * @param ax Normalised x axis of point a
	 * @param ay Normalised y axis of point a
	 * @param bx Normalised x axis of point b
	 * @param by Normalised y axis of point b
	 */
	public void drawRectangle(double ax, double ay, double bx, double by) {
		canvas.drawRectangle(ax, ay, bx, by);
	}

	/**
	 * Draw a line between points a and b on display canvas
	 * @param ax Normalised x axis of point a
	 * @param ay Normalised y axis of point a
	 * @param bx Normalised x axis of point b
	 * @param by Normalised y axis of point b
	 */
	public void drawLine(double ax, double ay, double bx, double by) {
		canvas.drawLine(ax, ay, bx, by);
	}

	/**
	 * Clear all geometry (points, rectangles and lines) on display canvas
	 */
	public void clearGeometry() {
		canvas.clearGeometry();
	}




	/**
	 * Draw body wireframe from frame label
	 * @param pts The body points
	 * @param seq The body point connection sequence
	 */
	public void drawBody(Point[] pts, int[] seq) {
		for (int i=0; i < pts.length; i++) {
			Point p0 = pts[i];
			Point p1 = pts[seq[i]];
			drawLine(p0.getX(),p0.getY(),p1.getX(),p1.getY());
			drawPoint(p0.getX(),p0.getY());
		}
	}

	/**
	 * Draw left leaning (m=1) diagonal line passing through (x,y)
	 * @param x Normalised x axis coordinate
	 * @param y Normalised y axis coordinate
	 */
	public void drawDiagonalLeft(double x, double y) {
		double c = y - x;
		double ax,ay,bx,by;
		if (c > 0.0) {
			ax = 0.0;
			ay = 0.0+c;
			bx = 1.0-c;
			by = 1.0;
		} else {
			ax = 0.0-c;
			ay = 0.0;
			bx = 1.0;
			by = 1.0+c;
		}
		drawLine(ax,ay,bx,by);
	}

	/**
	 * Draw right leaning (m=-1) diagonal line passing through (x,y)
	 * @param x Normalised x axis coordinate
	 * @param y Normalised y axis coordinate
	 */
	public void drawDiagonalRight(double x, double y) {
		double c = y + x;
		double ax,ay,bx,by;
		if (c > 1.0) {
			ax = c-1.0;
			ay = 1.0;
			bx = 1.0;
			by = c-1.0;
		} else {
			ax = 0.0;
			ay = c;
			bx = c;
			by = 0.0;
		}
		drawLine(ax,ay,bx,by);
	}
}
