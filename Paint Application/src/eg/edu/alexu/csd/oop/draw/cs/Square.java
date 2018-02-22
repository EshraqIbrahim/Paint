package eg.edu.alexu.csd.oop.draw.cs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;

public class Square extends MyShape {
	private double x2;
	private Point coordinates;
	private Color drawColor;
	private Color fillColor;
	private Map<String, Double> getProp;

	public Square() {
		drawColor = Color.BLACK;
		fillColor = Color.WHITE;
		coordinates = new Point(0, 0);
		getProp = new HashMap<String, Double>();
		getProp.put("Width", 0.0);
	}

	@Override
	public void draw(Graphics canvas) {
		Graphics2D graph2 = (Graphics2D) canvas;
		getProp = getProperties();
		x2 = getProp.get("Width");
		coordinates = getPosition();
		drawColor = getColor();
		fillColor = getFillColor();
		if (fillColor != null) {
			graph2.setColor(fillColor);
			graph2.fill(new Rectangle2D.Double(coordinates.getX(), coordinates.getY(), x2, x2));
		}
		if (drawColor != null) {
			graph2.setColor(drawColor);
			graph2.draw(new Rectangle2D.Double(coordinates.getX(), coordinates.getY(), x2, x2));
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Shape s = new Square();
		Map<String, Double> getProp = getProperties();
		Double x2 = getProp.get("Width");
		Point coordinates = getPosition();
		Color drawColor = getColor();
		Color fillColor = getFillColor();
		s.setPosition(coordinates);
		s.setColor(drawColor);
		s.setFillColor(fillColor);
		Map<String, Double> newProp = new HashMap<String, Double>();
		newProp.put("Width", x2);
		s.setProperties(newProp);
		return s;
	}

}
