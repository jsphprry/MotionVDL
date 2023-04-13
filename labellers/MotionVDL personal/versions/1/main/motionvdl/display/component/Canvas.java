package motionvdl.display.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Canvas extends JPanel {

	// constants
	private static final int POINT_DIAMETER = 10;
	private static final int RECTANGLE_WIDTH = 4;
	private static final int LINE_WIDTH = 2;

	// properties
	public final int width;
	public final int height;

	// components
	private BufferedImage image;         // canvas image
	private ArrayList<int[]> points;     // list of 3d points
	private ArrayList<int[]> rectangles; // list of 4d points
	private ArrayList<int[]> lines;      // list of 4d points

	/**
	 * Construct canvas
	 * @param directory
	 */
	public Canvas(int width, int height){

		// setup properties
		this.width = width;
		this.height = height;

		// initialise components
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		points = new ArrayList<>();
		rectangles = new ArrayList<>();
		lines = new ArrayList<>();

		// setup panel
		setDefault();
	}


	/**
	 * Set default canvas image
	 */
	public void setDefault() {
		int c0 = Color.gray.getRGB();
		int c1 = Color.darkGray.getRGB();
		for (int i=0; i < height; i++){
			for (int j=0; j < width; j++){
				image.setRGB(j,i,((i%20==0)^(j%20==0)) ? c0 : c1);
			}
		}
		repaint();
	}


	/**
	 * Set the canvas image
	 * @param frame Video frame as 2d Color array
	 */
	public void set(Color[][] frame) {

		// convert Color[][] to BufferedImage
		BufferedImage bufferedImage = new BufferedImage(frame[0].length,frame.length,BufferedImage.TYPE_INT_ARGB);
		for (int i=0; i < bufferedImage.getHeight(); i++){
			for (int j=0; j < bufferedImage.getWidth(); j++){
				bufferedImage.setRGB(j,i,frame[i][j].getRGB());
			}
		}

		// draw scaled Image onto the canvas BufferedImage
		Image scaledImage = bufferedImage.getScaledInstance(width, -1, Image.SCALE_FAST);
		image.getGraphics().drawImage(scaledImage,0,0,null);
		repaint();
	}




	public void drawPoint(double x, double y) {
		points.add(new int[] {
			(int) (x*width - 0.5*POINT_DIAMETER),
			(int) (y*height - 0.5*POINT_DIAMETER),
			POINT_DIAMETER
		});
		repaint();
	}

	public void drawRectangle(double ax, double ay, double bx, double by) {
		rectangles.add(new int[] {
			(int) (ax*width),
			(int) (ay*height),
			(int) ((bx-ax)*width),
			(int) ((by-ay)*height)
		});
		repaint();
	}

	public void drawLine(double ax, double ay, double bx, double by) {
		lines.add(new int[] {
			(int) (ax*width),
			(int) (ay*height),
			(int) (bx*width),
			(int) (by*height)
		});
		repaint();
	}

	public void clearGeometry() {
		points.clear();
		rectangles.clear();
		lines.clear();
		repaint();
	}




	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}


	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		BasicStroke lineBrush = new BasicStroke(LINE_WIDTH);
		BasicStroke rectBrush = new BasicStroke(RECTANGLE_WIDTH);

		g2.drawImage(image, null, null);

		g2.setColor(Color.red);
		g2.setStroke(lineBrush);
		for (int[] l : lines) {
			g2.drawLine(l[0],l[1],l[2],l[3]);
		}

		g2.setColor(Color.black);
		g2.setStroke(rectBrush);
		for (int[] r : rectangles) {
			g2.drawRect(r[0],r[1],r[2],r[3]);
		}

		g2.setColor(Color.black);
		for (int[] p : points) {
			g2.fillOval(p[0],p[1],p[2],p[2]);
		}

		g2.dispose();
	}
}
