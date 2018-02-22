package eg.edu.alexu.csd.oop.draw.cs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;

public class Line extends MyShape {
	private double x2;
	private double y2;
	private Point coordinates;
	private Color drawColor;
	private Color fillColor;
	private Map<String, Double> getProp;

	public Line() {
		drawColor = Color.BLACK;
		fillColor = Color.WHITE;
		coordinates = new Point(0, 0);
		getProp = new HashMap<String, Double>();
		getProp.put("x", 0.0);
		getProp.put("y", 0.0);
	}

	@Override
	public void draw(Graphics canvas) {
		Graphics2D graph2 = (Graphics2D) canvas;
		getProp = getProperties();
		x2 = getProp.get("x");
		y2 = getProp.get("y");
		coordinates = getPosition();
		drawColor = getColor();
		fillColor = getFillColor();
		if (fillColor != null) {
			graph2.setColor(fillColor);
			graph2.draw(new Line2D.Double(coordinates.getX(), coordinates.getY(), x2, y2));
		}
		if (drawColor != null) {
			graph2.setColor(drawColor);
			graph2.draw(new Line2D.Double(coordinates.getX(), coordinates.getY(), x2, y2));
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Shape l = new Line();
		Map<String, Double> getProp = getProperties();
		Double x2 = getProp.get("x");
		Double y2 = getProp.get("y");
		Point coordinates = getPosition();
		Color drawColor = getColor();
		Color fillColor = getFillColor();
		l.setPosition(coordinates);
		l.setColor(drawColor);
		l.setFillColor(fillColor);
		Map<String, Double> newProp = new HashMap<String, Double>();
		newProp.put("x", x2);
		newProp.put("y", y2);
		l.setProperties(newProp);
		return l;
	}

}
