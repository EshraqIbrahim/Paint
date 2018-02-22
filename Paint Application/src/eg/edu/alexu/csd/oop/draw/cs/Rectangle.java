package eg.edu.alexu.csd.oop.draw.cs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;

public class Rectangle extends MyShape {
	private double x2;
	private double y2;
	private Point coordinates;
	private Color drawColor;
	private Color fillColor;
	private Map<String, Double> getProp;

	public Rectangle() {
		drawColor = Color.BLACK;
		fillColor = Color.WHITE;
		coordinates = new Point(0, 0);
		getProp = new HashMap<String, Double>();
		getProp.put("Width", 0.0);
		getProp.put("Length", 0.0);
	}

	@Override
	public void draw(Graphics canvas) {
		Graphics2D graph2 = (Graphics2D) canvas;
		Map<String, Double> getProp = getProperties();
		x2 = getProp.get("Width");
		y2 = getProp.get("Length");
		coordinates = getPosition();
		drawColor = getColor();
		fillColor = getFillColor();
		if (fillColor != null) {
			graph2.setColor(fillColor);
			graph2.fill(new Rectangle2D.Double(coordinates.getX(), coordinates.getY(), x2, y2));
		}
		if (drawColor != null) {
			graph2.setColor(drawColor);
			graph2.draw(new Rectangle2D.Double(coordinates.getX(), coordinates.getY(), x2, y2));
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Shape r = new Rectangle();
		Map<String, Double> getProp = getProperties();
		Double x2 = getProp.get("Width");
		Double y2 = getProp.get("Length");
		Point coordinates = getPosition();
		Color drawColor = getColor();
		Color fillColor = getFillColor();
		r.setPosition(coordinates);
		r.setColor(drawColor);
		r.setFillColor(fillColor);
		Map<String, Double> newProp = new HashMap<String, Double>();
		newProp.put("Width", x2);
		newProp.put("Length", y2);
		r.setProperties(newProp);
		return r;
	}

}
