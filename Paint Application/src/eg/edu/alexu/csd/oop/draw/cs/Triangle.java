package eg.edu.alexu.csd.oop.draw.cs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.HashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;

public class Triangle extends MyShape {
	private double secondX;
	private double secondY;
	private double thirdX;
	private double thirdY;
	private Point coordinates;
	private Color drawColor;
	private Color fillColor;
	private Map<String, Double> getProp;

	public Triangle() {
		drawColor = Color.BLACK;
		fillColor = Color.WHITE;
		coordinates = new Point(0, 0);
		getProp = new HashMap<String, Double>();
		getProp.put("secondX", 0.0);
		getProp.put("secondY", 0.0);
		getProp.put("thirdX", 0.0);
		getProp.put("thirdY", 0.0);
	}

	@Override
	public void draw(Graphics canvas) {
		// TODO Auto-generated method stub
		Graphics2D graph2 = (Graphics2D) canvas;
		getProp = getProperties();
		secondX = getProp.get("secondX");
		secondY = getProp.get("secondY");
		thirdX = getProp.get("thirdX");
		thirdY = getProp.get("thirdY");
		coordinates = getPosition();
		drawColor = getColor();
		fillColor = getFillColor();

		if (fillColor != null) {
			graph2.setColor(fillColor);
			int xPoly[] = new int[3];
			int yPoly[] = new int[3];
			xPoly[0] = coordinates.x;
			xPoly[1] = (int) secondX;
			xPoly[2] = (int) thirdX;
			yPoly[0] = coordinates.y;
			yPoly[1] = (int) secondY;
			yPoly[2] = (int) thirdY;
			graph2.fill(new Polygon(xPoly, yPoly, 3));
		}
		if (drawColor != null) {
			graph2.setColor(drawColor);
			int xPoly[] = new int[3];
			int yPoly[] = new int[3];
			xPoly[0] = coordinates.x;
			xPoly[1] = (int) secondX;
			xPoly[2] = (int) thirdX;
			yPoly[0] = coordinates.y;
			yPoly[1] = (int) secondY;
			yPoly[2] = (int) thirdY;
			graph2.draw(new Polygon(xPoly, yPoly, 3));
		}

		// int xPoly[] = { 150, 250, 325 };
		// int yPoly[] = { 200, 200, 125 };
		// graphSettings.draw(new Polygon(xPoly, yPoly, 3));
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		Shape t = new Triangle();
		Map<String, Double> getProp = getProperties();
		Double secondX = getProp.get("secondX");
		Double secondY = getProp.get("secondY");
		Double thirdX = getProp.get("thirdX");
		Double thirdY = getProp.get("thirdY");
		Point coordinates = getPosition();
		Color drawColor = getColor();
		Color fillColor = getFillColor();
		t.setPosition(coordinates);
		t.setColor(drawColor);
		t.setFillColor(fillColor);
		Map<String, Double> newProp = new HashMap<String, Double>();
		newProp.put("secondX", secondX);
		newProp.put("secondY", secondY);
		newProp.put("thirdX", thirdX);
		newProp.put("thirdY", thirdY);
		t.setProperties(newProp);
		return t;
	}

}
