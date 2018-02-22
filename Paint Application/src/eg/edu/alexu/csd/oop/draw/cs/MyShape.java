package eg.edu.alexu.csd.oop.draw.cs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;



public abstract class MyShape implements Shape {
	private Point myPosition = new Point();
	private Map<String, Double> myProperties;
	private Color drawColor = null;
	private Color fillColor = null;

	@Override
	public void setPosition(Point position) {
		// TODO Auto-generated method stub
		myPosition = position;
	}

	@Override
	public Point getPosition() {
		// TODO Auto-generated method stub
		return myPosition;
	}

	@Override
	public void setProperties(Map<String, Double> properties) {
		// TODO Auto-generated method stub
		myProperties = properties;
	}

	@Override
	public Map<String, Double> getProperties() {
		// TODO Auto-generated method stub
		return myProperties;
	}

	@Override
	public void setColor(Color color) {
		// TODO Auto-generated method stub
		drawColor = color;
	}

	@Override
	public Color getColor() {
		// TODO Auto-generated method stub
		return drawColor;
	}

	@Override
	public void setFillColor(Color color) {
		// TODO Auto-generated method stub
		fillColor = color;
	}

	@Override
	public Color getFillColor() {
		// TODO Auto-generated method stub
		return fillColor;
	}

	@Override
	public abstract void draw(Graphics canvas);

	@Override
	public abstract Object clone() throws CloneNotSupportedException;

}
