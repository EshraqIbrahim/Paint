package eg.edu.alexu.csd.oop.draw.cs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import eg.edu.alexu.csd.oop.draw.Shape;


public class Draw implements DrawingEngine {
	ArrayList<Shape> myshapes = new ArrayList<Shape>();
	ArrayList<ArrayList<Shape>> undo = new ArrayList<ArrayList<Shape>>();
	ArrayList<ArrayList<Shape>> redo = new ArrayList<ArrayList<Shape>>();
	private Shape loadShape;
	List<Class<? extends Shape>> shapesList = new ArrayList<Class<? extends Shape>>();
	/* redraw all the shapes */
	@Override
	public void refresh(Graphics canvas) {
		for (int i = 0; i < myshapes.size(); i++) {
			Shape temp = myshapes.get(i);
			temp.draw(canvas);
		}
	}
	/* add shapes */
	@Override
	public void addShape(Shape shape) {
		// TODO Auto-generated method stub
		ArrayList<Shape> temp = new ArrayList<Shape>();
		for (int i = 0; i < myshapes.size(); i++) {
			temp.add(myshapes.get(i));
		}
		if (undo.size() > 19) {
			undo.remove(0);
			undo.add(temp);
		} else {
			undo.add(temp);
		}
		myshapes.add(shape);
	}
	/* remove shapes */
	@Override
	public void removeShape(Shape shape) {
		// TODO Auto-generated method stub
		ArrayList<Shape> temp = new ArrayList<Shape>();
		for (int j = 0; j < myshapes.size(); j++) {
			temp.add(myshapes.get(j));
		}
		if (undo.size() > 19) {
			undo.remove(0);
			undo.add(temp);
		} else {
			undo.add(temp);
		}
		for (int i = 0; i < myshapes.size(); i++) {
			if (myshapes.get(i) == shape) {
				myshapes.remove(i);
			}
		}

	}
	/* remove shapes without adding to undo */
	public void removeTemp(Shape shape) {
		for (int i = 0; i < myshapes.size(); i++) {
			if (myshapes.get(i) == shape) {
				myshapes.remove(i);
			}
		}
	}
	/*add shapes without adding to undo */
	public void addTemp(Shape shape) {
		myshapes.add(shape);
	}
	/* update shapes */
	@Override
	public void updateShape(Shape oldShape, Shape newShape) {
		// TODO Auto-generated method stub
		ArrayList<Shape> temp = new ArrayList<Shape>();
		for (int j = 0; j < myshapes.size(); j++) {
			temp.add(myshapes.get(j));
		}
		//add to undo
		if (undo.size() > 19) {
			undo.remove(0);
			undo.add(temp);
		} else {
			undo.add(temp);
		}
		for (int i = 0; i < myshapes.size(); i++) {
			if (myshapes.get(i) == oldShape) {
				myshapes.remove(i);
				myshapes.add(i, newShape);
			}
		}

	}
	/* get all the shapes */
	@Override
	public Shape[] getShapes() {
		Shape[] getShape = new Shape[myshapes.size()];
		for (int i = 0; i < myshapes.size(); i++) {
			getShape[i] = myshapes.get(i);
		}
		return getShape;
	}
	/*import an external jar to our application*/
	@SuppressWarnings({ "rawtypes", "resource", "unchecked", "unused" })
	public Class<? extends Shape> installPlugin(String path) throws ClassNotFoundException, IOException {
		JarFile jarFile = new JarFile(path);
		Enumeration<JarEntry> e = jarFile.entries();
		File file = new File(path);
		//to get the class name
		String className = null;

		URL[] urls = { new URL("jar:file:" + path + "!/") };
		URLClassLoader cl = URLClassLoader.newInstance(urls);

		while (e.hasMoreElements()) {
			JarEntry je = e.nextElement();
			if (je.isDirectory() || !je.getName().endsWith(".class")) {
				continue;
			}
			// -6 because of .class
			className = je.getName().substring(0, je.getName().length() - 6);
			className = className.replace('/', '.');
			if (!className.equals("model.Shape")) {
				break;
			}
		}
		Class c = null;
		URL url;
		String h = "file:///" + path;

		//
		url = new URL(h);
		URLClassLoader ucl = new URLClassLoader(new URL[] { url });
		c = ucl.loadClass(className);
		shapesList.add(c);
		return c;
	}
	/* get the supported shapes in the application */
	@SuppressWarnings("unchecked")
	@Override
	public List<Class<? extends Shape>> getSupportedShapes() {
		// TODO Auto-generated method stub
		try {
			Class<? extends Shape> rectangle = (Class<? extends Shape>) Class.forName("model.Rectangle");
			shapesList.add(rectangle);
			Class<? extends Shape> square = (Class<? extends Shape>) Class.forName("model.Square");
			shapesList.add(square);
			Class<? extends Shape> circle = (Class<? extends Shape>) Class.forName("model.Circle");
			shapesList.add(circle);
			Class<? extends Shape> line = (Class<? extends Shape>) Class.forName("model.Line");
			shapesList.add(line);
			Class<? extends Shape> ellipse = (Class<? extends Shape>) Class.forName("model.Ellipse");
			shapesList.add(ellipse);
			Class<? extends Shape> triangle = (Class<? extends Shape>) Class.forName("model.Triangle");
			shapesList.add(triangle);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return shapesList;
	}
	/* clear the shapes */
	public void clear() {
		myshapes.clear();
		undo.clear();
		redo.clear();
	}
	/* undo the last step (limit to 20 steps) */
	@Override
	public void undo() {
		// TODO Auto-generated method stub
		Shape[] ourShapes = getShapes();
		if (undo.size() <= 0) {
			return;
		}
		else if (ourShapes.length == 1) {
			//add to redo
			if (redo.size() > 19) {
				redo.remove(0);
			}
			ArrayList<Shape> temp = new ArrayList<Shape>();
			for (int j = 0; j < myshapes.size(); j++) {
				temp.add(myshapes.get(j));
			}
			redo.add(temp);
			myshapes.clear();
		} else {
			if (redo.size() > 19) {
				redo.remove(0);
			}
			ArrayList<Shape> temp = new ArrayList<Shape>();
			for (int j = 0; j < myshapes.size(); j++) {
				temp.add(myshapes.get(j));
			}
			redo.add(temp);
			myshapes = undo.get(undo.size() - 1);
			undo.remove(undo.size() - 1);
		}

	}
	/* redo the last step (limit to 20 step) */
	@Override
	public void redo() {
		// TODO Auto-generated method stub

		if (redo.size() != 0) {
			ArrayList<Shape> temp = new ArrayList<Shape>();
			for (int j = 0; j < myshapes.size(); j++) {
				temp.add(myshapes.get(j));
			}
			if (undo.size() > 19) {
				undo.remove(0);
				undo.add(temp);
			} else {
				undo.add(temp);
			}
			myshapes = redo.get(redo.size() - 1);
			redo.remove(redo.size() - 1);
		}
	}


	/*
	 * use the file extension to determine the type, or throw runtime exception when
	 * unexpected extension
	 */
	@Override
	public final void save(String path) {
		if (path.toLowerCase().contains(".xml")) {
			saveXML(path);
		} else if (path.toLowerCase().contains(".json")) {
			saveJSON(path);
		} else {
			throw new RuntimeException();
		}
	}

	/* xml */
	public final void saveXML(String path) {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element save = doc.createElement("save");
			doc.appendChild(save);
			Element myshapes = doc.createElement("myshapes");
			save.appendChild(myshapes);
			saveShapeXML(docBuilder, doc, this.myshapes, myshapes);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult streamResult = new StreamResult(new File(path));
			transformer.transform(source, streamResult);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	/* xml */
	public final void saveShapeXML(DocumentBuilder docBuilder, Document doc, ArrayList<Shape> array, Element element) {
		for (int i = 0; i < array.size(); i++) {
			Element shape = doc.createElement("shape");
			Attr attr = doc.createAttribute("ID");
			attr.setValue(Integer.toString(i));
			shape.setAttributeNode(attr);
			Shape sh = array.get(i);
			Element type = doc.createElement("type");
			type.appendChild(doc.createTextNode(sh.toString()));
			shape.appendChild(type);
			Element positionX = doc.createElement("positionX");
			if (sh.getPosition() != null) {
				Double x = sh.getPosition().getX();
				positionX.appendChild(doc.createTextNode(Double.toString(x)));
				shape.appendChild(positionX);
			}

			Element positionY = doc.createElement("positionY");
			if (sh.getPosition() != null) {
				Double y = sh.getPosition().getY();
				positionY.appendChild(doc.createTextNode(Double.toString(y)));
				shape.appendChild(positionY);
			}

			Element properties = doc.createElement("properties");
			if (sh.getProperties() != null) {
				String p = sh.getProperties().toString();
				properties.appendChild(doc.createTextNode(p));
				shape.appendChild(properties);
			}

			Element color = doc.createElement("color");
			if (sh.getColor() != null) {
				String c = String.valueOf(sh.getColor().getRGB());
				color.appendChild(doc.createTextNode(c));
				shape.appendChild(color);
			}

			Element fillcolor = doc.createElement("fillcolor");
			if (sh.getFillColor() != null) {
				String fc = String.valueOf(sh.getFillColor().getRGB());
				fillcolor.appendChild(doc.createTextNode(fc));
				shape.appendChild(fillcolor);
			}
			element.appendChild(shape);
		}
	}

	/*
	 * use the file extension to determine the type, or throw runtime exception when
	 * unexpected extension
	 */
	@Override
	public final void load(String path) {
		if (path.toLowerCase().contains(".xml")) {
			loadXML(path);
		} else if (path.toLowerCase().contains(".json")) {
			loadJSON(path);
		} else {
			throw new RuntimeException();
		}
	}

	/* xml */
	@SuppressWarnings("unchecked")
	public final void loadXML(String path) {
		myshapes = new ArrayList<>();
		undo = new ArrayList<>();
		redo = new ArrayList<>();
		File xmlFile = new File(path);
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(xmlFile);
			NodeList list = doc.getElementsByTagName("myshapes");
			for (int j = 0; j < list.getLength(); j++) {
				Node nodelist = list.item(j);
				if (nodelist.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) nodelist;
					NodeList listshape = element.getElementsByTagName("shape");
					myshapes = loadShapeXML(listshape, myshapes);
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (DOMException e) {
			e.printStackTrace();
		}
	}

	/* xml */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public final ArrayList loadShapeXML(NodeList listshape, ArrayList array) {
		for (int i = 0; i < listshape.getLength(); i++) {
			Node nodeshape = listshape.item(i);
			if (nodeshape.getNodeType() == Node.ELEMENT_NODE) {
				Element elements = (Element) nodeshape;
				if (elements.getElementsByTagName("type").item(0) != null) {
					String str = elements.getElementsByTagName("type").item(0).getTextContent();
					if (str.contains("Circle")) {
						loadShape = new Circle();
					} else if (str.contains("Square")) {
						loadShape = new Square();
					} else if (str.contains("Ellipse")) {
						loadShape = new Ellipse();
					} else if (str.contains("Rectangle")) {
						loadShape = new Rectangle();
					} else if (str.contains("Line")) {
						loadShape = new Line();
					}else if (str.contains("Triangle")) {
						loadShape = new Triangle();
					}
				}
				if (elements.getElementsByTagName("positionX").item(0) != null
						&& elements.getElementsByTagName("positionY").item(0) != null) {
					double dx = Double.valueOf(elements.getElementsByTagName("positionX").item(0).getTextContent());
					double dy = Double.valueOf(elements.getElementsByTagName("positionY").item(0).getTextContent());
					Point point = new Point((int) dx, (int) dy);
					loadShape.setPosition(point);
				}
				if (elements.getElementsByTagName("properties").item(0) != null) {
					String pro = elements.getElementsByTagName("properties").item(0).getTextContent();
					String key = new String();
					String value = new String();
					Map<String, Double> properties = new HashMap<>();
					for (int j = 0; j < pro.length(); j++) {
						char ch = pro.charAt(j);
						if (Character.isLetter(ch)) {
							key += pro.charAt(j);
						} else if (Character.isDigit(ch) || ch == '.') {
							value += ch;
						} else if (ch == ',') {
							properties.put(key, Double.valueOf(value));
							key = new String();
							value = new String();
						}
					}
					properties.put(key, Double.valueOf(value));
					loadShape.setProperties(properties);
				}

				if (elements.getElementsByTagName("color").item(0) != null) {
					Color c = new Color(
							Integer.parseInt(elements.getElementsByTagName("color").item(0).getTextContent()));
					loadShape.setColor(c);
				}
				if (elements.getElementsByTagName("fillcolor").item(0) != null) {
					Color fc = new Color(
							Integer.parseInt(elements.getElementsByTagName("fillcolor").item(0).getTextContent()));
					loadShape.setFillColor(fc);
				}
			}
			array.add(loadShape);
		}
		return array;
	}

	/* json */
	@SuppressWarnings("unchecked")
	public final void saveJSON(String path) {
		JSONObject jsonObject = new JSONObject();
		for (int i = 0; i < myshapes.size(); i++) {
			Shape shape = myshapes.get(i);
			@SuppressWarnings("unused")
			JSONObject object = new JSONObject();
			JSONArray list = new JSONArray();
			JSONObject obj = new JSONObject();
			obj.put("type", shape.toString());
			if (shape.getPosition() != null) {
				obj.put("positionX", Double.toString(shape.getPosition().getX()));
			}
			if (shape.getPosition() != null) {
				obj.put("positionY", Double.toString(shape.getPosition().getY()));
			}
			if (shape.getProperties() != null) {
				obj.put("properties", shape.getProperties().toString());
			}
			if (shape.getColor() != null) {
				obj.put("Color", String.valueOf(shape.getColor().getRGB()));
			}
			if (shape.getFillColor() != null) {
				obj.put("fillColor", String.valueOf(shape.getFillColor().getRGB()));
			}
			list.add(obj);
			jsonObject.put("Shape" + i, list);
		}
		try {
			@SuppressWarnings("resource")
			FileWriter file = new FileWriter(path);
			file.write(jsonObject.toJSONString());
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(jsonObject);
	}

	/* json */
	@SuppressWarnings("unchecked")
	public final void loadJSON(String path) {
		myshapes = new ArrayList<>();
		undo = new ArrayList<>();
		redo = new ArrayList<>();
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(path));
			JSONObject object = (JSONObject) obj;
			for (int i = 0;; i++) {
				if (object.get("Shape" + i) != null) {
					JSONArray shapes = (JSONArray) object.get("Shape" + i);
					Iterator<JSONObject> iterator = shapes.iterator();
					while (iterator.hasNext()) {
						JSONObject objectShape = iterator.next();
						String str = (String) objectShape.get("type");
						if (str.contains("Circle")) {
							loadShape = new Circle();
						} else if (str.contains("Square")) {
							loadShape = new Square();
						} else if (str.contains("Ellipse")) {
							loadShape = new Ellipse();
						} else if (str.contains("Rectangle")) {
							loadShape = new Rectangle();
						} else if (str.contains("Line")) {
							loadShape = new Line();
						}else if (str.contains("Triangle")) {
							loadShape = new Triangle();
						}
						if (objectShape.get("positionX") != null && objectShape.get("positionY") != null) {
							double x = Double.valueOf((String) objectShape.get("positionX"));
							double y = Double.valueOf((String) objectShape.get("positionY"));
							Point p = new Point((int) x, (int) y);
							loadShape.setPosition(p);
						}
						if (objectShape.get("properties") != null) {
							String pro = (String) objectShape.get("properties");
							String key = new String();
							String value = new String();
							Map<String, Double> properties = new HashMap<>();
							for (int j = 0; j < pro.length(); j++) {
								char ch = pro.charAt(j);
								if (Character.isLetter(ch)) {
									key += pro.charAt(j);
								} else if (Character.isDigit(ch) || ch == '.') {
									value += ch;
								} else if (ch == ',') {
									properties.put(key, Double.valueOf(value));
									key = new String();
									value = new String();
								}
							}
							properties.put(key, Double.valueOf(value));
							loadShape.setProperties(properties);
						}
						if (objectShape.get("Color") != null) {
							Color c = new Color(Integer.parseInt((String) objectShape.get("Color")));
							loadShape.setColor(c);
						}
						if (objectShape.get("fillColor") != null) {
							loadShape
									.setFillColor((new Color(Integer.parseInt((String) objectShape.get("fillColor")))));
						}
						myshapes.add(loadShape);
					}
				} else {
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
