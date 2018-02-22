package controller;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;

import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.cs.Circle;
import eg.edu.alexu.csd.oop.draw.cs.Draw;
import eg.edu.alexu.csd.oop.draw.cs.Ellipse;
import eg.edu.alexu.csd.oop.draw.cs.Line;
import eg.edu.alexu.csd.oop.draw.cs.Rectangle;
import eg.edu.alexu.csd.oop.draw.cs.Square;
import eg.edu.alexu.csd.oop.draw.cs.Triangle;
import view.GUI;

public class control extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<Rectangle2D.Float> myshapes = new ArrayList<Rectangle2D.Float>();
	ArrayList<Line2D.Double> myshapes2 = new ArrayList<Line2D.Double>();
	ArrayList<Ellipse2D.Double> myshapes3 = new ArrayList<Ellipse2D.Double>();
	Color strokeColor = Color.BLACK;
	Color fillColor = Color.WHITE;
	Point drawStart, drawEnd, drawMid;
	JSlider transSlider;
	JLabel transLabel;
	DecimalFormat dec = new DecimalFormat("#.##");
	float transparentval = 1.0f;
	int currentAction = 1;
	Draw myDrawing = new Draw();
	boolean checkUndo = false;
	boolean checkRedo = false;
	boolean checktri = false;
	int[] xPoly1 = new int[3];
	int[] xPoly2 = new int[3];
	boolean checkSelect = false;
	boolean isShapeSelected = false;
	Point clickPoint;
	Shape selectedShape = null;
	boolean isResizing = false;
	Shape resizedShape = null;
	Point resizePoint;
	ArrayList<Integer> indexes = new ArrayList<Integer>();
	int indexxx = 0;
	int resizedShapeIndex = 0;
	boolean checkStart = false;
	boolean checkmid = false;
	boolean checkEnd = false;
	Point triStart = null;
	Point triMid = null;
	Point triEnd = null;
	Point dynStart = null;
	boolean checkdyn = false;
	Shape extraShape;
	boolean checkLoad = false;
	String jar;

	public control() {
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (((e.getKeyCode() == KeyEvent.VK_Z) && (e.isControlDown()))) {
					myDrawing.undo();
					repaint();
				}
				if (((e.getKeyCode() == KeyEvent.VK_Y) && (e.isControlDown()))) {
					myDrawing.redo();
					repaint();
				}
				if (((e.getKeyCode() == KeyEvent.VK_N) && (e.isControlDown()))) {
					Shape[] myshapes = myDrawing.getShapes();
					for (int i = 0; i < myshapes.length; i++) {
						myDrawing.removeShape(myshapes[i]);
					}
					repaint();
				}
				if (((e.getKeyCode() == KeyEvent.VK_S) && (e.isControlDown()))) {
					JFileChooser chooser = new JFileChooser();
					chooser.setDialogTitle("Save As ..");
					int result = chooser.showSaveDialog(null);
					if (result == JFileChooser.APPROVE_OPTION) {
						String path = chooser.getSelectedFile().getAbsolutePath();
						if (path.toLowerCase().contains(".xml") || path.toLowerCase().contains(".json")) {
							myDrawing.save(path);
						} else {
							JOptionPane.showMessageDialog(null,
									"INVALID FILE EXTENSION ! Please Enter Valid File Extension (.xml OR .json)");
						}
					}
					repaint();
				}
				if (((e.getKeyCode() == KeyEvent.VK_L) && (e.isControlDown()))) {
					checkLoad = true;
					JFileChooser chooser = new JFileChooser();
					chooser.setDialogTitle("Open A File");
					int result = chooser.showOpenDialog(null);
					if (result == JFileChooser.APPROVE_OPTION) {
						String path = chooser.getSelectedFile().getAbsolutePath();
						if (path.toLowerCase().contains(".xml") || path.toLowerCase().contains(".json")) {
							myDrawing.save(path);
						} else {
							JOptionPane.showMessageDialog(null,
									"INVALID FILE EXTENSION ! Please Enter Valid File Extension (.xml OR .json)");
						}
					}
					@SuppressWarnings("unused")
					Shape[] myshapes = myDrawing.getShapes();
					repaint();
				}

			}

			@Override
			public void keyReleased(KeyEvent e) {

			}

			@Override
			public void keyTyped(KeyEvent e) {

			}
		});

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// set cursor to cross hair cursor
				setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// set cursor outside the drawing area to default
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

			}

			@Override
			public void mousePressed(MouseEvent e) {
				isShapeSelected = false;
				isResizing = false;
				selectedShape = null;
				resizedShape = null;
				/* drawing the extra shape */
				if (currentAction == 15) {
					dynStart = new Point(e.getX(), e.getY());
					checkdyn = true;
				}
				/* drawing triangle */
				if (currentAction == 7) {
					if (!checkStart) {
						triStart = new Point(e.getX(), e.getY());
						checkStart = true;
					} else if (!checkmid) {
						triMid = new Point(e.getX(), e.getY());
						checkmid = true;
					} else if (!checkEnd) {
						triEnd = new Point(e.getX(), e.getY());
						checkEnd = true;
					}

				} else if (currentAction != 1) {
					// drawing any shapes
					drawStart = new Point(e.getX(), e.getY());
					drawEnd = drawStart;
				} else if (currentAction == 1 || currentAction == 16) {
					// the brush or the rubber
					drawStart = new Point(e.getX(), e.getY());
				}
				if (currentAction == 8) {
					// removing shapes
					Point checkPoint = new Point(e.getX(), e.getY());
					Shape temp = null;
					boolean isIn = false;
					Shape[] shapes = myDrawing.getShapes();
					for (int i = 0; i < shapes.length; i++) {
						if (isIn(checkPoint, shapes[i])) {
							isIn = true;
							temp = shapes[i];
						}
					}
					if (isIn) {
						myDrawing.removeShape(temp);
					}

				}
				if (currentAction == 9) {
					// copying shapes
					Point checkPoint = new Point(e.getX(), e.getY());
					Shape temp = null;
					boolean isIn = false;
					Shape[] shapes = myDrawing.getShapes();
					for (int i = 0; i < shapes.length; i++) {
						if (isIn(checkPoint, shapes[i])) {
							isIn = true;
							temp = shapes[i];
						}
					}
					if (isIn) {
						Shape addME = null;
						try {
							addME = (Shape) temp.clone();
						} catch (CloneNotSupportedException e1) {
							e1.printStackTrace();
						}
						Point old = addME.getPosition();
						Point newone = new Point(old.x + 50, old.y + 50);
						Map<String, Double> getProp = temp.getProperties();
						if (getProp.containsKey("x")) {
							Double oldX = getProp.get("x");
							Double oldY = getProp.get("y");
							Map<String, Double> newProp = new HashMap<String, Double>();
							newProp.put("x", oldX + 50);
							newProp.put("y", oldY + 50);
							addME.setProperties(newProp);
						}
						if (getProp.containsKey("secondX")) {
							Double secondX = getProp.get("secondX");
							Double secondY = getProp.get("secondY");
							Double thirdX = getProp.get("thirdX");
							Double thirdY = getProp.get("thirdY");
							Map<String, Double> newProp = new HashMap<String, Double>();
							newProp.put("secondX", secondX + 50);
							newProp.put("secondY", secondY + 50);
							newProp.put("thirdX", thirdX + 50);
							newProp.put("thirdY", thirdY + 50);
							addME.setProperties(newProp);
						}
						addME.setPosition(newone);
						myDrawing.addShape(addME);

					}

				}
				if (currentAction == 12) {
					// moving shapes
					Point checkPoint = new Point(e.getX(), e.getY());
					Shape temp = null;
					boolean isIn = false;
					Shape[] shapes = myDrawing.getShapes();
					for (int i = 0; i < shapes.length; i++) {
						if (isIn(checkPoint, shapes[i])) {
							isIn = true;
							temp = shapes[i];
						}
					}
					if (isIn) {
						Shape addME = null;
						try {
							addME = (Shape) temp.clone();
						} catch (CloneNotSupportedException e1) {
							e1.printStackTrace();
						}
						Color newColor = strokeColor;
						addME.setColor(newColor);
						myDrawing.updateShape(temp, addME);

					}
				}
				if (currentAction == 13) {
					Point checkPoint = new Point(e.getX(), e.getY());
					Shape temp = null;
					boolean isIn = false;
					Shape[] shapes = myDrawing.getShapes();
					for (int i = 0; i < shapes.length; i++) {
						if (isIn(checkPoint, shapes[i])) {
							isIn = true;
							temp = shapes[i];
						}
					}
					if (isIn) {
						Shape addME = null;
						try {
							addME = (Shape) temp.clone();
						} catch (CloneNotSupportedException e1) {
							e1.printStackTrace();
						}
						Color newColor = fillColor;
						addME.setFillColor(newColor);
						myDrawing.updateShape(temp, addME);

					}

				}
				if (currentAction == 10) {
					// getting the first point for moving
					clickPoint = new Point(e.getX(), e.getY());
					boolean isIn = false;

					Shape[] shapes = myDrawing.getShapes();
					for (int i = 0; i < shapes.length; i++) {
						if (isIn(clickPoint, shapes[i])) {
							isIn = true;
							selectedShape = shapes[i];
							isShapeSelected = true;
						}
					}
					if (isIn) {
						setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					} else {
						isShapeSelected = false;
					}
				}
				if (currentAction == 11) {
					// getting the first point for resizing
					resizePoint = new Point(e.getX(), e.getY());
					if (resizePoint != null) {
						boolean isResizable = false;
						Shape[] shapes = myDrawing.getShapes();
						for (int i = 0; i < shapes.length; i++) {
							if (isResizable(resizePoint, shapes[i])) {
								isResizable = true;
								resizedShape = shapes[i];
								isResizing = true;
							}
						}
						if (isResizable) {
							setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
						} else {
							isResizing = false;
						}
					}

				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// drawing line after releasing the mouse
				if (currentAction == 2) {
					Line line = new Line();
					line.setColor(strokeColor);
					Point myPosition = new Point();
					Map<String, Double> myProperties = new HashMap<String, Double>();
					;
					myPosition.x = drawStart.x;
					myPosition.y = drawStart.y;
					line.setPosition(myPosition);
					myProperties.put("x", (double) e.getX());
					myProperties.put("y", (double) e.getY());
					line.setProperties(myProperties);
					myDrawing.addShape(line);
				}
				if (currentAction == 3) {
					// drawing ellipse after releasing the mouse
					Ellipse ellipse = new Ellipse();
					ellipse.setColor(strokeColor);
					ellipse.setFillColor(fillColor);
					Point myPosition = new Point();
					Map<String, Double> myProperties = new HashMap<String, Double>();
					;
					int x1 = drawStart.x;
					int x2 = e.getX();
					int y1 = drawStart.y;
					int y2 = e.getY();
					myPosition.x = Math.min(x1, x2);
					myPosition.y = Math.min(y1, y2);
					ellipse.setPosition(myPosition);
					double width = Math.abs(x1 - x2);
					double length = Math.abs(y1 - y2);
					myProperties.put("Width", width);
					myProperties.put("Length", length);
					ellipse.setProperties(myProperties);
					myDrawing.addShape(ellipse);
				}
				if (currentAction == 5) {
					// drawing circle after releasing the mouse
					Circle circle = new Circle();
					circle.setColor(strokeColor);
					circle.setFillColor(fillColor);
					Point myPosition = new Point();
					Map<String, Double> myProperties = new HashMap<String, Double>();
					int x1 = drawStart.x;
					int x2 = e.getX();
					int y1 = drawStart.y;
					int y2 = e.getY();
					myPosition.x = Math.min(x1, x2);
					myPosition.y = Math.min(y1, y2);
					circle.setPosition(myPosition);
					double width = Math.abs(x1 - x2);
					myProperties.put("Width", width);
					circle.setProperties(myProperties);
					myDrawing.addShape(circle);
				}
				if (currentAction == 4) {
					// drawing rectangle after releasing the mouse
					Rectangle rectangle = new Rectangle();
					rectangle.setColor(strokeColor);
					rectangle.setFillColor(fillColor);
					Point myPosition = new Point();
					Map<String, Double> myProperties = new HashMap<String, Double>();
					int x1 = drawStart.x;
					int x2 = e.getX();
					int y1 = drawStart.y;
					int y2 = e.getY();
					myPosition.x = Math.min(x1, x2);
					myPosition.y = Math.min(y1, y2);
					rectangle.setPosition(myPosition);
					double width = Math.abs(x1 - x2);
					double length = Math.abs(y1 - y2);
					myProperties.put("Width", width);
					myProperties.put("Length", length);
					rectangle.setProperties(myProperties);
					myDrawing.addShape(rectangle);
				}
				if (currentAction == 6) {
					// drawing square after releasing the mouse
					Square square = new Square();
					square.setColor(strokeColor);
					square.setFillColor(fillColor);
					Point myPosition = new Point();
					Map<String, Double> myProperties = new HashMap<String, Double>();
					int x1 = drawStart.x;
					int x2 = e.getX();
					int y1 = drawStart.y;
					int y2 = e.getY();
					myPosition.x = Math.min(x1, x2);
					myPosition.y = Math.min(y1, y2);
					square.setPosition(myPosition);
					double width = Math.abs(x1 - x2);
					myProperties.put("Width", width);
					square.setProperties(myProperties);
					myDrawing.addShape(square);
				}
				// resetting the drawStart and drawEnd points
				drawStart = null;
				drawEnd = null;
				repaint();

			}
		});

		this.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				drawEnd = new Point(e.getX(), e.getY());
				Shape temp = null;
				Shape temp2 = null;
				// moving shapes
				if (isShapeSelected) {
					Point new_point = new Point(e.getX(), e.getY());
					setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					int xShift = new_point.x - clickPoint.x;
					int yShift = new_point.y - clickPoint.y;
					System.out.println(xShift);

					try {
						temp = (Shape) selectedShape.clone();
						Point Start_point = temp.getPosition();
						System.out.println(Start_point.x);
						Point update_point = new Point(Start_point.x + xShift, Start_point.y + yShift);
						temp.setPosition(update_point);
						Map<String, Double> getProp = temp.getProperties();
						if (getProp.containsKey("x")) {
							Double oldX = getProp.get("x");
							Double oldY = getProp.get("y");
							Map<String, Double> newProp = new HashMap<String, Double>();
							newProp.put("x", oldX + xShift);
							newProp.put("y", oldY + yShift);
							temp.setProperties(newProp);
						}
						if (getProp.containsKey("secondX")) {
							Double secondX = getProp.get("secondX");
							Double secondY = getProp.get("secondY");
							Double thirdX = getProp.get("thirdX");
							Double thirdY = getProp.get("thirdY");
							Map<String, Double> newProp = new HashMap<String, Double>();
							newProp.put("secondX", secondX + xShift);
							newProp.put("secondY", secondY + yShift);
							newProp.put("thirdX", thirdX + xShift);
							newProp.put("thirdY", thirdY + yShift);
							temp.setProperties(newProp);
						}
						myDrawing.updateShape(selectedShape, temp);
						// selectedShape = (Shape) temp.clone();
					} catch (CloneNotSupportedException e1) {
						e1.printStackTrace();
					}
					selectedShape = temp;
					clickPoint = new Point(new_point.x, new_point.y);

					repaint();
				}
				if (isResizing) {
					// resizing shapes
					Point new_point = new Point(e.getX(), e.getY());
					int xShift = new_point.x - resizePoint.x;
					int yShift = new_point.y - resizePoint.y;
					Map<String, Double> getProp = new HashMap<String, Double>();

					try {
						temp2 = (Shape) resizedShape.clone();
						getProp = temp2.getProperties();
						if (getProp.containsKey("x")) {
							Double oldX = getProp.get("x");
							Double oldY = getProp.get("y");
							Map<String, Double> newProp = new HashMap<String, Double>();
							newProp.put("x", oldX + xShift);
							newProp.put("y", oldY + yShift);
							temp2.setProperties(newProp);
						} else if (getProp.containsKey("secondX")) {
							Double secondX = getProp.get("secondX");
							Double secondY = getProp.get("secondY");
							Double thirdX = getProp.get("thirdX");
							Double thirdY = getProp.get("thirdY");
							Map<String, Double> newProp = new HashMap<String, Double>();
							newProp.put("secondX", secondX + xShift);
							newProp.put("secondY", secondY + yShift);
							newProp.put("thirdX", thirdX + xShift);
							newProp.put("thirdY", thirdY + yShift);
							temp2.setProperties(newProp);
						} else if (getProp.containsKey("Length")) {
							Double length = getProp.get("Length");
							Double width = getProp.get("Width");
							Map<String, Double> newProp = new HashMap<String, Double>();
							newProp.put("Width", width + xShift);
							newProp.put("Length", length + yShift);
							temp2.setProperties(newProp);
							Object[] myProp = getProp.keySet().toArray();
							for (int l = 0; l < myProp.length; l++) {
								String prop = (String) getProp.keySet().toArray()[l];
								if (!prop.equals("Width") && !prop.equals("Length")) {
									newProp.put(prop, getProp.get(prop));
								}
							}
						} else if (getProp.containsKey("Width") && !getProp.containsKey("Length")) {
							Double width = getProp.get("Width");
							Map<String, Double> newProp = new HashMap<String, Double>();
							newProp.put("Width", width + xShift);
							temp2.setProperties(newProp);
						}

					} catch (CloneNotSupportedException e1) {
						e1.printStackTrace();
					}
					// update the old shape by the new one
					myDrawing.updateShape(resizedShape, temp2);
					resizedShape = temp2;
					resizePoint = new Point(new_point.x, new_point.y);
					repaint();
				}

				else {
					if (currentAction == 1) {
						// using brush
						int x = drawEnd.x;
						int y = drawEnd.y;
						Line brush = new Line();
						brush.setColor(strokeColor);
						brush.setFillColor(strokeColor);
						Point myPosition = new Point();
						myPosition.x = drawStart.x;
						myPosition.y = drawStart.y;
						brush.setPosition(myPosition);
						Map<String, Double> myProperties = new HashMap<String, Double>();
						myProperties.put("x", (double) x);
						myProperties.put("y", (double) y);
						brush.setProperties(myProperties);
						myDrawing.addTemp(brush);
						drawStart = drawEnd;

					}
					if (currentAction == 16) {
						// using the rubber
						setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						Square brush = new Square();
						brush.setColor(Color.WHITE);
						brush.setFillColor(Color.WHITE);
						Point myPosition = new Point();
						myPosition.x = drawStart.x;
						myPosition.y = drawStart.y;
						brush.setPosition(myPosition);
						Map<String, Double> myProperties = new HashMap<String, Double>();
						myProperties.put("Width", (double) 10);
						brush.setProperties(myProperties);
						myDrawing.addTemp(brush);
						drawStart = drawEnd;
					}
				}
				repaint();

			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// setting the cursor to hand cursor for moving and deleting and copying and
				// changing draw and fill color
				if (currentAction == 8 || currentAction == 9 || currentAction == 10 || currentAction == 13
						|| currentAction == 12) {
					boolean isIn = false;
					Shape[] shapes = myDrawing.getShapes();
					for (int i = 0; i < shapes.length; i++) {
						if (isIn(new Point(e.getX(), e.getY()), shapes[i])) {
							isIn = true;
						}
					}
					if (isIn) {
						if (currentAction == 8 || currentAction == 9 || currentAction == 13 || currentAction == 12) {
							setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						}
						if (currentAction == 10) {
							setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
						}

					} else {
						setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

					}

				}
				if (currentAction == 11) {
					// changes cursor for resizing shapes
					boolean isResizable = false;
					Shape[] shapes = myDrawing.getShapes();
					for (int i = 0; i < shapes.length; i++) {
						Point h = new Point(e.getX(), e.getY());
						if (h != null) {
							if (isResizable(h, shapes[i])) {
								isResizable = true;
							}
						}
						if (isResizable) {
							setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
						} else {
							setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
						}
					}

				}
			}

		});
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D graphSettings = (Graphics2D) g;
		graphSettings.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphSettings.setStroke(new BasicStroke(2));
		graphSettings.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		myDrawing.refresh(g);
		// check for adding extra shapes
		if (checkdyn) {
			Point p = new Point(dynStart.x, dynStart.y);
			extraShape.setPosition(p);
			Map<String, Double> prop2 = new HashMap<String, Double>();
			Map<?, ?> shapeProp = extraShape.getProperties();

			Object[] myProp = shapeProp.keySet().toArray();
			for (int l = 0; l < myProp.length; l++) {
				String prop = (String) shapeProp.keySet().toArray()[l];
				String h = JOptionPane.showInputDialog(null, "Enter the value of the " + prop);
				Double d = 0.0;
				try {
					d = Double.parseDouble(h);
				} catch (Exception e) {

				}
				prop2.put(prop, d);
			}
			extraShape.setProperties(prop2);
			extraShape.setFillColor(fillColor);
			extraShape.setColor(strokeColor);
			myDrawing.addShape(extraShape);
			repaint();
			checkdyn = false;
			dynStart = null;
			currentAction = 1;

		}
		// check for triangle drawing action
		if (checkStart && checkmid) {
			graphSettings.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
			graphSettings.setColor(Color.GRAY);
			Line2D.Double line = drawLine(triStart.x, triStart.y, triMid.x, triMid.y);
			graphSettings.draw(line);

			if (checkEnd) {
				Triangle triangle = new Triangle();
				triangle.setColor(strokeColor);
				triangle.setFillColor(fillColor);
				Point myPosition = new Point(triStart.x, triStart.y);
				triangle.setPosition(myPosition);
				Map<String, Double> myProperties = new HashMap<String, Double>();
				myProperties.put("secondX", (double) triMid.x);
				myProperties.put("secondY", (double) triMid.y);
				myProperties.put("thirdX", (double) triEnd.x);
				myProperties.put("thirdY", (double) triEnd.y);
				triangle.setProperties(myProperties);
				myDrawing.addShape(triangle);
				checkStart = false;
				checkmid = false;
				checkEnd = false;
				repaint();
			}
			repaint();

		}
		// check for drawing shapes
		if (drawStart != null && drawEnd != null) {
			graphSettings.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
			graphSettings.setColor(Color.GRAY);
			if (currentAction == 2) {
				Line2D.Double line = drawLine(drawStart.x, drawStart.y, drawEnd.x, drawEnd.y);
				graphSettings.draw(line);
			}
			if (currentAction == 3) {
				Ellipse2D.Double ellipse = drawEllipse(drawStart.x, drawStart.y, drawEnd.x, drawEnd.y);
				graphSettings.draw(ellipse);
			}
			if (currentAction == 4) {
				Rectangle2D.Float rec1 = drawRectangle(drawStart.x, drawStart.y, drawEnd.x, drawEnd.y);
				graphSettings.draw(rec1);
			}
			if (currentAction == 5) {
				Ellipse2D.Double ellipse = drawEllipse(drawStart.x, drawStart.y, drawEnd.x, drawEnd.y);
				graphSettings.draw(ellipse);
			}
			if (currentAction == 6) {
				Rectangle2D.Float rec1 = drawRectangle(drawStart.x, drawStart.y, drawEnd.x, drawEnd.y);
				graphSettings.draw(rec1);
			}

		}
		// check for undo
		if (checkUndo) {
			g.clearRect(0, 0, getWidth(), getHeight());
			myDrawing.refresh(g);
			checkUndo = false;
		}
		// check for redo
		if (checkRedo) {
			g.clearRect(0, 0, getWidth(), getHeight());
			myDrawing.refresh(g);
			checkRedo = false;
		}
		// check for load
		if (checkLoad) {
			g.clearRect(0, 0, getWidth(), getHeight());
			myDrawing.refresh(g);
			checkLoad = false;
		}
		// new button
		GUI.neww.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				myDrawing.clear();
				repaint();
			}
		});

	}

	// drawing dummy rectangle or square
	private Rectangle2D.Float drawRectangle(int x1, int y1, int x2, int y2) {
		int x = Math.min(x1, x2);
		int y = Math.min(y1, y2);
		int width = Math.abs(x1 - x2);
		int length = Math.abs(y1 - y2);
		if (currentAction == 6) {
			length = width;
		}
		return new Rectangle2D.Float(x, y, width, length);
	}

	// drawing dummy ellipse or circle
	private Ellipse2D.Double drawEllipse(int x1, int y1, int x2, int y2) {
		int x = Math.min(x1, x2);
		int y = Math.min(y1, y2);
		int width = Math.abs(x1 - x2);
		int length = Math.abs(y1 - y2);
		if (currentAction == 5) {
			length = width;
		}
		return new Ellipse2D.Double(x, y, width, length);
	}

	// drawing dummy line
	private Line2D.Double drawLine(int x1, int y1, int x2, int y2) {
		return new Line2D.Double(x1, y1, x2, y2);
	}

	// doing action performance for the buttons
	public void doActions() {
		// save button
		GUI.save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("Save As ..");
				int result = chooser.showSaveDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					String path = chooser.getSelectedFile().getAbsolutePath();
					if (path.toLowerCase().contains(".xml") || path.toLowerCase().contains(".json")) {
						myDrawing.save(path);
					} else {
						JOptionPane.showMessageDialog(null,
								"INVALID FILE EXTENSION ! Please Enter Valid File Extension (.xml OR .json)");
					}
				}
				repaint();
			}
		});
		// load button
		GUI.load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkLoad = true;
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("Open A File");
				int result = chooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					String path = chooser.getSelectedFile().getAbsolutePath();
					if (path.toLowerCase().contains(".xml") || path.toLowerCase().contains(".json")) {
						myDrawing.load(path);
					} else {
						JOptionPane.showMessageDialog(null,
								"INVALID FILE EXTENSION ! Please Enter Valid File Extension (.xml OR .json)");
					}
				}
				@SuppressWarnings("unused")
				Shape[] myshapes = myDrawing.getShapes();
				repaint();
			}
		});
		// draw color button
		GUI.strokeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				strokeColor = JColorChooser.showDialog(null, "Pick draw color", strokeColor);
			}
		});
		// brush button
		GUI.brushBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentAction = 1;
			}
		});
		// line button
		GUI.lineBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentAction = 2;
			}
		});
		// circle button
		GUI.circleBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentAction = 5;
			}
		});
		// square button
		GUI.squareBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentAction = 6;
			}
		});
		// ellipse button
		GUI.ellipseBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentAction = 3;
			}
		});
		// rectangle button
		GUI.rectBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentAction = 4;
			}
		});
		// triangle button
		GUI.triangleBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentAction = 7;
			}
		});
		// extra shape button
		GUI.deleteBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentAction = 15;
				try {
					Class<?> c = myDrawing.installPlugin(jar);
					List<Class<? extends Shape>> d = myDrawing.getSupportedShapes();
					System.out.println(d.size());
					Constructor<?> c1 = c.getDeclaredConstructor();
					extraShape = (Shape) c1.newInstance();
					JOptionPane.showMessageDialog(null, "Choose the starting point from the drawing area");
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e8) {
					e8.printStackTrace();
				} catch (NoSuchMethodException e7) {
					e7.printStackTrace();
				} catch (SecurityException e6) {
					e6.printStackTrace();
				} catch (InstantiationException e5) {
					e5.printStackTrace();
				} catch (IllegalAccessException e4) {
					e4.printStackTrace();
				} catch (IllegalArgumentException e3) {
					e3.printStackTrace();
				} catch (InvocationTargetException e2) {
					e2.printStackTrace();
				}
			}
		});
		// rubber button
		GUI.copyBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentAction = 16;
			}
		});
		// change fill color button
		GUI.fillBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fillColor = JColorChooser.showDialog(null, "Pick Fill color", fillColor);
			}
		});
		// undo button
		GUI.undoBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkUndo = true;
				myDrawing.undo();
				repaint();
			}
		});
		// re-do button
		GUI.redoBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkRedo = true;
				myDrawing.redo();
				repaint();
			}
		});
		// change draw color button
		GUI.drawColorBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				currentAction = 12;
			}

		});
		// change fill color button
		GUI.fillColorBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				currentAction = 13;
			}

		});
		// copy menu item
		GUI.copy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				currentAction = 9;
			}
		});
		// delete menu item
		GUI.delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				currentAction = 8;
			}
		});
		// move menu item
		GUI.move.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				currentAction = 10;
			}
		});
		// resize menu item
		GUI.resize.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				currentAction = 11;
			}
		});
		// importing jar menu item
		GUI.addExtra.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("Open Jar ..");
				int result = chooser.showSaveDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					String jarPath = chooser.getSelectedFile().getAbsolutePath();
					if (jarPath.toLowerCase().contains(".jar")) {
						jar = jarPath;
						GUI.deleteBtn.setEnabled(true);
					} else {
						JOptionPane.showMessageDialog(null,
								"INVALID FILE EXTENSION ! Please Enter Valid File Extension (.jar)");
					}
				}

			}
		});

	}

	/* check if its a shape area */
	public boolean isIn(Point p, Shape shape) {
		Map<String, Double> getProp = shape.getProperties();
		if (getProp.containsKey("x")) {
			Double x2 = getProp.get("x");
			Double y2 = getProp.get("y");
			Point coordinates = shape.getPosition();
			if ((p.x > coordinates.x - 20 && p.y > coordinates.y - 20 && p.x < (coordinates.x + 20)
					&& p.y < (coordinates.y + 20))
					|| (p.x > x2 - 20 && p.y > y2 - 20 && p.x < (x2 + 20) && p.y < (y2 + 20))) {

				return true;
			}
			return false;
		} else if (getProp.containsKey("secondX")) {
			Double secondX = getProp.get("secondX");
			Double secondY = getProp.get("secondY");
			Double thirdX = getProp.get("thirdX");
			Double thirdY = getProp.get("thirdY");
			Point coordinates = shape.getPosition();
			if ((p.x > coordinates.x - 20 && p.y > coordinates.y - 20 && p.x < (coordinates.x + 20)
					&& p.y < (coordinates.y + 20))
					|| (p.x > secondX - 20 && p.y > secondY - 20 && p.x < (secondX + 20) && p.y < (secondY + 20))
					|| (p.x > thirdX - 20 && p.y > thirdY - 20 && p.x < (thirdX + 20) && p.y < (thirdY + 20))) {
				return true;
			}
			return false;
		} else if (getProp.containsKey("Length")) {
			Double x2 = getProp.get("Width");
			Double y2 = getProp.get("Length");
			Point coordinates = shape.getPosition();
			if (p.x > coordinates.x && p.y > coordinates.y && p.x < (x2 + coordinates.x)
					&& p.y < (y2 + coordinates.y)) {
				return true;
			}

			return false;
		} else if (getProp.containsKey("Width") && !getProp.containsKey("Length")) {
			Double width = getProp.get("Width");
			Point coordinates = shape.getPosition();
			if (p.x > coordinates.x && p.y > coordinates.y && p.x < (width + coordinates.x)
					&& p.y < (width + coordinates.y)) {
				return true;
			} else {
				return false;
			}

		} else {
			return false;
		}
	}

	// check if it is an end point area
	public boolean isResizable(Point p, Shape shape) {
		Map<String, Double> getProp = shape.getProperties();
		if (getProp.containsKey("x")) {
			Double x2 = getProp.get("x");
			Double y2 = getProp.get("y");
			if (isIn(p, shape)) {
				if ((p.x > x2 - 30 && p.y > y2 - 30)) {
					return true;
				}
			}
			return false;
		} else if (getProp.containsKey("secondX")) {
			Double secondX = getProp.get("secondX");
			Double secondY = getProp.get("secondY");
			Double thirdX = getProp.get("thirdX");
			Double thirdY = getProp.get("thirdY");
			if ((p.x > secondX - 20 && p.y > secondY - 20 && p.x < (secondX + 20) && p.y < (secondY + 20))
					|| (p.x > thirdX - 20 && p.y > thirdY - 20 && p.x < (thirdX + 20) && p.y < (thirdY + 20))) {
				return true;
			}
			return false;
		} else if (getProp.containsKey("Length")) {
			Double x2 = getProp.get("Width");
			Double y2 = getProp.get("Length");
			Point coordinates = shape.getPosition();
			if (isIn(p, shape)) {
				if ((p.x > coordinates.x + x2 - 30 && p.y > coordinates.y + y2 - 30)) {
					return true;
				}
			}

			return false;

		} else if (getProp.containsKey("Width") && !getProp.containsKey("Length")) {
			Double width = getProp.get("Width");
			Point coordinates = shape.getPosition();
			if (isIn(p, shape)) {
				if ((p.x > coordinates.x + width - 30 && p.y > coordinates.y + width - 30)) {
					return true;
				}
			}
			return false;

		} else {
			return false;
		}
	}
}
