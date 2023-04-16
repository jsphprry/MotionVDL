package motionvdl.display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BorderFactory;
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
import motionvdl.model.data.Point;

@SuppressWarnings("serial")
public class Display extends JFrame {
	
	// properties
	public final int width;
	public final int height;
	
	// values
	private final int holdtime = 500;
	private final int frametime1 = 30;
	private final int frametime2 = 100;
	
	// components
	private Canvas canvas;
	private JPanel controlPanel;
	private JMenuBar menu;
	private JMenu menuFile;
	private JMenuItem menuFileOpen;
	private JMenuItem menuFileSave;
	private JMenuItem menuFileSaveAs;
	private JMenu menuOptions;
	private JRadioButtonMenuItem menuOptionsAuto;
	private JRadioButtonMenuItem menuOptionsCali;
	private JMenu menuDebug;
	private JRadioButtonMenuItem menuDebugTrace;
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
	 * @param w The window width
	 * @param h The window height
	 */
	public Display(int w, int h){
		
		// window title
		super("MotionVDL");
		
		// setup properties
		width = w;
		height = h;
		
		// setup 
		setupComponents();
		setupEvents();
		
		// final setup
		targetInput.requestFocusInWindow();
	}
	
	
	/**
	 * Store a reference to receiving controller
	 * @param r Receiving controller
	 */
	public void sendTo(MainController r) {
		receiver = r;
	}
	
	
	/**
	 * Get value of automatic-next option
	 * @return Option setting represented as boolean
	 */
	public boolean getAuto() {
		return menuOptionsAuto.isSelected();
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
	 * Set image on display canvas
	 * @param frame Frame as image
	 */
	public void setFrame(Image frame) {
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
	
	
	public void setTarget(int value) {
		targetInput.setText(Integer.toString(value));
	}
	
	
	public void showTarget() {
		// TODO Auto-generated method stub
		
	}
	
	
	public void hideTarget() {
		// TODO Auto-generated method stub
		
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
	
	
	private Thread repeatNext(int delay, int frametime) {
		return new Thread(new Runnable() {
			@Override
			public void run() {
				receiver.setNextFrame();
				try {
					Thread.sleep(delay);
					while (true) {
						Thread.sleep(frametime);
						receiver.setNextFrame();
					}
				} catch (InterruptedException e) {
					// do nothing
				}
		    }
		}); 
	}
		
		
	private Thread repeatPrev(int delay, int frametime) {
		return new Thread(new Runnable() {
			@Override
			public void run() {
				receiver.setPrevFrame();
				try {
					Thread.sleep(delay);
					while (true) {
						Thread.sleep(frametime);
						receiver.setPrevFrame();
					}
				} catch (InterruptedException e) {
					// do nothing
				}
		    }
		});
	}
	
	
	private void setupComponents() {
		
		// tree depth
		
		// 0
		setSize(width,height);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocation(100, 100);
		
		// 1
        canvas = new Canvas(height,height);
        
        // 1
        controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel,BoxLayout.Y_AXIS));
		
		// 2
		titleLabel = new JLabel("MotionVDL");
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// 2
		completeButton = new JButton("Commit changes");
		completeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		completeButton.setMaximumSize(new Dimension(171, 20));
		
		// 2
		menu = new JMenuBar();
		menu.setMaximumSize(new Dimension(Short.MAX_VALUE, 20));
		
		// 3
		menuFile = new JMenu("File");
		
		// 4
		menuFileOpen = new JMenuItem("Open");
		menuFileSave = new JMenuItem("Save");
		menuFileSaveAs = new JMenuItem("Save as");
		
		// 3
		menuFile.add(menuFileOpen);
		menuFile.add(menuFileSave);
		menuFile.add(menuFileSaveAs);
		
		// 3
		menuOptions = new JMenu("Options");
		
		// 4
		menuOptionsAuto = new JRadioButtonMenuItem("Automatic next",true);
		menuOptionsCali = new JRadioButtonMenuItem("Calibration mode",false);
		menuOptions.add(menuOptionsAuto);
		menuOptions.add(menuOptionsCali);
		
		// 3
		menuDebug = new JMenu("Debug");
		
		// 4
		menuDebugTrace = new JRadioButtonMenuItem("Trace",true);
		
		// 3
		menuDebug.add(menuDebugTrace);
		
		// 2
		menu.add(menuFile);
		menu.add(menuOptions);
		menu.add(menuDebug);
		
		// 2
		framePanel = new JPanel();
		framePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		framePanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 20));
		
		// 3
		framePrevButton = new JButton("<");
		frameLabel = new JLabel("Frame control");
		frameNextButton = new JButton(">");
		
		// 2
		framePanel.add(framePrevButton);
		framePanel.add(frameLabel);
		framePanel.add(frameNextButton);
		
		// 2
		targetPanel = new JPanel();
		targetPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		targetPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 20));
		
		// 3
		targetLabel = new JLabel("Target resolution:");
		targetInput = new JTextField(3);
		targetInput.setText("0");
		
		// 2
		targetPanel.add(targetLabel);
		targetPanel.add(targetInput);
		
		// 1
		controlPanel.add(menu);
    	controlPanel.add(Box.createRigidArea(new Dimension(0,4)));
    	controlPanel.add(titleLabel);
    	controlPanel.add(Box.createRigidArea(new Dimension(0,4)));
    	controlPanel.add(completeButton);
    	controlPanel.add(Box.createRigidArea(new Dimension(0,2)));
    	controlPanel.add(framePanel);
    	controlPanel.add(targetPanel);
    	controlPanel.add(Box.createVerticalGlue());

		// 0
        getContentPane().add(BorderLayout.WEST, canvas);
        getContentPane().add(BorderLayout.EAST, controlPanel);
		pack();
    	
    	// independent
    	fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	}
	
	
	private void setupEvents() {
		
		// menu > file > open
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
        
        // menu > file > save
        menuFileSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					receiver.save();
				}
			}
		});
        
        // menu > file > save as
        menuFileSaveAs.addMouseListener(new MouseAdapter() {
			@Override
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
		
		// menu > debug > trace
		menuDebugTrace.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Debug.setup(true);
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					Debug.setup(false);
				}
			}
		});
		
		// frame click
        canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (menuOptionsCali.isSelected()) {
					receiver.calibrate(
						e.getX() / (double) canvas.width,
						e.getY() / (double) canvas.height
					);
					menuOptionsCali.setSelected(false);
				} else {
					if (SwingUtilities.isLeftMouseButton(e)) {
						receiver.click(
							e.getX() / (double) canvas.width,
							e.getY() / (double) canvas.height
						);
					} else {
						receiver.undo();
					}
				}
			}
		});
		
		// complete button
		completeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				receiver.complete();
			}
		});
		
		// previous frame button
		framePrevButton.addMouseListener(new MouseAdapter() {
			
			private Thread action;
			private boolean lock;
			
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e) && !lock) {
					lock = true;
					action = repeatPrev(holdtime,frametime1);
					action.start();
				} else if (SwingUtilities.isRightMouseButton(e) && !lock) {
					lock = true;
					action = repeatPrev(0,frametime2);
					action.start();
				} else if (SwingUtilities.isMiddleMouseButton(e) && !lock) {
					receiver.setMinFrame();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (lock) action.interrupt();
				lock = false;
			}
		});
		
		// next frame button
		frameNextButton.addMouseListener(new MouseAdapter() {
			
			private Thread action;
			private boolean lock;
			
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e) && !lock) {
					lock = true;
					action = repeatNext(holdtime,frametime1);
					action.start();
				} else if (SwingUtilities.isRightMouseButton(e) && !lock) {
					lock = true;
					action = repeatNext(0,frametime2);
					action.start();
				} else if (SwingUtilities.isMiddleMouseButton(e) && !lock) {
					receiver.setMaxFrame();
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (lock) action.interrupt();
				lock = false;
			}
		});
	}
	
	
	@SuppressWarnings("unused")
	private void debugBorders() {
		menu.setBorder(BorderFactory.createCompoundBorder(
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
                targetPanel.getBorder()));
	}
}
