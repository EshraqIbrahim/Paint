package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import controller.control;

public class GUI {
	/**
	 *
	 */
	private JFrame frame = new JFrame();
	private static JPanel left = new JPanel();
	private JPanel top = new JPanel();
	private JPanel bottom = new JPanel();
	private JPanel btnPanel = new JPanel();
	private Box theBox = Box.createHorizontalBox();
	private Box LowerBox = Box.createHorizontalBox();
	private Box one = Box.createVerticalBox();
	private Box two = Box.createVerticalBox();
	private Box four = Box.createVerticalBox();
	public static JButton brushBtn;
	public static JButton lineBtn;
	public static JButton ellipseBtn;
	public static JButton rectBtn;
	public static JButton strokeBtn;
	public static JButton fillBtn;
	public static JButton circleBtn;
	public static JButton squareBtn;
	public static JButton triangleBtn;
	public static JButton undoBtn;
	public static JButton redoBtn;
	public static JButton copyBtn;
	public static JButton deleteBtn;
	public static JButton moveBtn;
	public static JButton resizeBtn;
	public static JButton drawColorBtn;
	public static JButton fillColorBtn;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenu editMenu;
	private JMenu extra;
	public static JMenuItem save;
	public static JMenuItem load;
	public static JMenuItem neww;
	public static JMenuItem move;
	public static JMenuItem resize;
	public static JMenuItem delete;
	public static JMenuItem copy;
	public static JMenuItem addExtra;

	public GUI() {
		initialize();
	}
	/* add photos to buttons */
	public JButton makeMeButton(String iconFile) {
		JButton theBut = new JButton();
		Icon butIcon = null;
		try {
			butIcon = new ImageIcon(ImageIO.read(Resources.resourceLoader(iconFile)));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		theBut.setIcon(butIcon);
		return theBut;

	}
	/* initialize the menu bar contents */
	public void initializeMenuBar() {
		menuBar = new JMenuBar();
		menu = new JMenu("File");
		save = new JMenuItem("Save");
		load = new JMenuItem("Load");
		neww = new JMenuItem("New");
		editMenu = new JMenu("Edit");
		copy = new JMenuItem("Copy");
		move = new JMenuItem("Move");
		resize = new JMenuItem("Resize");
		delete = new JMenuItem("Delete");
		extra = new JMenu("Extra");
		addExtra = new JMenuItem("Add Extra Shape");
		menu.add(save);
		menu.add(load);
		menu.add(neww);
		editMenu.add(copy);
		editMenu.add(delete);
		editMenu.add(move);
		editMenu.add(resize);
		extra.add(addExtra);
		menuBar.add(menu);
		menuBar.add(editMenu);
		menuBar.add(extra);

	}
	/* initialize our frame contents */
	public void initialize() {

		frame.setTitle("Paint");
		initializeMenuBar();
		frame.setJMenuBar(menuBar);
		frame.setLayout(new BorderLayout());
		frame.setSize(1100, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		brushBtn = makeMeButton("images/Webp.net-resizeimage (8).png");
		circleBtn = makeMeButton("images/Webp.net-resizeimage (4).png");
		squareBtn = makeMeButton("images/Webp.net-resizeimage.png");
		triangleBtn = makeMeButton("images/Webp.net-resizeimage (3).png");
		ellipseBtn = makeMeButton("images/Webp.net-resizeimage (6).png");
		rectBtn = makeMeButton("images/Webp.net-resizeimage (1).png");
		lineBtn = makeMeButton("images/Webp.net-resizeimage (5).png");
		strokeBtn = makeMeButton("images/Webp.net-resizeimage (9).png");
		fillBtn = makeMeButton("images/Webp.net-resizeimage (10).png");
		undoBtn = makeMeButton("images/undo1.png");
		redoBtn = makeMeButton("images/redo1.png");
		copyBtn = makeMeButton("images/rubber.png");
		deleteBtn = makeMeButton("images/extra.png");
		deleteBtn.setEnabled(false);
		drawColorBtn = makeMeButton("images/c.png");
		fillColorBtn = makeMeButton("images/fc.png");
		one.add(undoBtn);
		one.add(deleteBtn);
		two.add(redoBtn);
		two.add(copyBtn);
		four.add(drawColorBtn);
		four.add(fillColorBtn);
		theBox.add(brushBtn);
		theBox.add(circleBtn);
		theBox.add(squareBtn);
		theBox.add(triangleBtn);
		theBox.add(lineBtn);
		theBox.add(ellipseBtn);
		theBox.add(rectBtn);
		theBox.add(strokeBtn);
		theBox.add(fillBtn);
		theBox.add(one);
		theBox.add(two);
		theBox.add(four);
		btnPanel.add(theBox);
		btnPanel.setSize(1100, 300);
		top.add(btnPanel);
		top.setVisible(true);
		bottom.add(LowerBox);
		control t = new control();
		t.setBackground(Color.WHITE);
		t.setBounds(100, 10, 1150, 600);
		t.doActions();
		left.setLayout(null);
		left.add(t);
		frame.add(left, BorderLayout.CENTER);
		frame.add(top, BorderLayout.NORTH);
		frame.add(bottom, BorderLayout.SOUTH);
	}
	/* main function */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
