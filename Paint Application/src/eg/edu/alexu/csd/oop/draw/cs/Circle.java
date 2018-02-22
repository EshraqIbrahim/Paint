package eg.edu.alexu.csd.oop.draw.cs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;

public class Circle extends MyShape {
	private double radius;
	private Point coordinates;
	private Color drawColor;
	private Color fillColor;
	private Map<String, Double> getProp;

	public Circle() {
		drawColor = Color.BLACK;
		fillColor = Color.WHITE;
		coordinates = new Point(0, 0);
		getProp = new HashMap<String, Double>();
		getProp.put("Width", 0.0);
	}

	@Override
	public void draw(Graphics canvas) {
		Graphics2D graph2 = (Graphics2D) canvas;
		Map<String, Double> getProp = getProperties();
		radius = getProp.get("Width");
		coordinates = getPosition();
		drawColor = getColor();
		fillColor = getFillColor();

		if (fillColor != null) {
			graph2.setColor(fillColor);
			graph2.fill(new Ellipse2D.Double(coordinates.getX(), coordinates.getY(), radius, radius));
		}
		if (drawColor != null) {
			graph2.setColor(drawColor);
			graph2.draw(new Ellipse2D.Double(coordinates.getX(), coordinates.getY(), radius, radius));
		}

	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		Shape c = new Circle();
		Map<String, Double> getProp = getProperties();
		Double radius = getProp.get("Width");
		Point coordinates = getPosition();
		Color drawColor = getColor();
		Color fillColor = getFillColor();
		c.setPosition(coordinates);
		c.setColor(drawColor);
		c.setFillColor(fillColor);
		Map<String, Double> newProp = new HashMap<String, Double>();
		newProp.put("Width", radius);
		c.setProperties(newProp);
		return c;
	}

}
