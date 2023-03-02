package motionvdl.display;

import java.awt.Color;
import java.awt.Point;

import motionvdl.controller.Controller;
import motionvdl.controller.MainController;

public class Display {
	
	//private Label title;
	//private Frame videoFrame;
	//private Label message;
	
	private int height;
	private int width;
	private Controller receiver;
	
	public Display(int h, int w) {
		this.height = h;
		this.width = w;
		
		//this.videoFrame = new Frame();
		
	}
	
	
	/**
	 * Store a reference to the receiving controller
	 * @param controller The controller reference
	 */
	public void sendTo(Controller controller) {
		receiver = controller;
	}
	
	
	
	
	
	public void setTitle(String string) {
		// TODO Auto-generated method stub
		//this.title.set(string);
		
	}
	
	public void setFrame(Color[][] frame) {
		// TODO Auto-generated method stub
		
		//this.videoFrame.setImage(frame);
		
	}
	
	public void setMessage(String string) {
		// TODO Auto-generated method stub
		//this.message.set(string);
		
	}
	
	
	
	
	
	public void drawPoint(int x, int y) {
		// TODO Auto-generated method stub
		
	}
	
	public void drawPoints(Point[] points) {
		// TODO Draw multiple points on the display - commit test
		
	}
	
	public void drawDiagonal(int ax, int ay) {
		// TODO Auto-generated method stub
		
	}
	
	public void drawRectangle(int ax, int ay, int bx, int by) {
		// TODO Auto-generated method stub
		
	}
	
	public void clearGeometry() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	public Point getTarget() {
		// TODO Auto-generated method stub
		return null;
	}
}
