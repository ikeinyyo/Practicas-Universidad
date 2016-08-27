package javavis;

import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import javavis.base.JIPToolkit;
import javavis.desktop.gui.GuiDesktop;
import javavis.desktop.gui.MenuBarDesktop;
import javavis.jip2d.base.*;
import javavis.jip2d.gui.Gui2D;
import javavis.jip2d.gui.MenuBarGui2D;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


/**
 *  Main class of the program, here is where everything is started, all menus are loaded.
 */
public class Gui extends JFrame implements ChangeListener {
	private static final long serialVersionUID = 5359289125586625385L;

	private static Logger logger = Logger.getLogger(Gui.class);

	/**
	 * Object managing the tab panel
	 * @uml.property  name="tabPane"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	JTabbedPane tabPane;
	
	public static final int INDEX2D = 0;
	public static final int INDEXDESKTOP = 1;
	
	public String sep;
	
	/**
	 * Guis for the different guis
	 */
	/**
	 * @uml.property  name="g2d"
	 * @uml.associationEnd  multiplicity="(1 1)" inverse="mainGui:javavis.jip2d.gui.Gui2D"
	 */
	Gui2D g2d;
	
	/**
	 * @uml.property  name="gdesk"
	 * @uml.associationEnd  multiplicity="(1 1)" inverse="mainGui:javavis.desktop.gui.GuiDesktop"
	 */
	GuiDesktop gdesk;

	/**
	 * Menu bar for the different tabs
	 */
	/**
	 * @uml.property  name="menuBar2D"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	MenuBarGui2D menuBar2D;
	/**
	 * @uml.property  name="menuBarDesktop"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	MenuBarDesktop menuBarDesktop;
	/**
	 * Property file
	 * @uml.property  name="prop"
	 */
	Properties prop;
	
	/**
	 * Paths file
	 */
	Properties paths;

	/**
	 * Available function list
	 * @uml.property  name="funclist2d"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	FunctionList funclist2d;

	/** 
	 * Class constructor. Here is where list of function, and the menu bar, data panel, etc. 
	 * are created. 
	 */
	public Gui() {
		super("JavaVis");
    	FileInputStream fis = null;

        prop = new Properties();
        try {
        	Locale l = Locale.getDefault();
        	if ("es".equalsIgnoreCase(l.getCountry()))
        		fis = new FileInputStream("resources/spanish.properties");
        	else
        		fis = new FileInputStream("resources/english.properties");
        	prop.load(fis);
        	fis.close();
        }
        catch (IOException e) {
        	System.out.println("Properties file not found");
        	logger.error("Properties file not found");
        	try {if (fis!=null) fis.close();}
        	catch (IOException ex){}
        }
    	paths = new Properties();
        try {
       		fis = new FileInputStream("resources/path.properties");
        	paths.load(fis);
        	fis.close();
        } catch (IOException e) {
        	paths.setProperty("Load2D", ".");
        	try{if (fis!=null) fis.close();}catch(IOException ex){}
        }
        
		funclist2d = new FunctionList();

		getContentPane().setLayout(new BorderLayout());

        tabPane = new JTabbedPane();
        tabPane.addChangeListener(this);
        g2d = new Gui2D(this, prop, paths, funclist2d);
        tabPane.add(g2d);
        tabPane.setTitleAt(INDEX2D,"2D");

        // Add DesktopPane
        gdesk = new GuiDesktop(this, prop, funclist2d);
        tabPane.add(gdesk,INDEXDESKTOP);
        tabPane.setTitleAt(INDEXDESKTOP,"Desktop");


        // Add 2DPane
        menuBar2D = new MenuBarGui2D(g2d, funclist2d, prop);
		menuBarDesktop = new MenuBarDesktop(gdesk, prop);
		setJMenuBar(menuBar2D);

        getContentPane().add(tabPane, BorderLayout.CENTER);

		setIconImage(Commons.getIcon("vg.gif").getImage());
		setResizable(true);
	}
	
	public void savePaths () {
		FileOutputStream fos = null;

    	try
    	{
    		fos = new FileOutputStream("resources/path.properties");
    		paths.store(fos, "");
    	}catch(Exception e)
    	{
    		System.out.println("Paths can not be stored");
    	}
    	finally {
    		if (fos!=null)
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
    	}
	}

	public void stateChanged(ChangeEvent e) {
		switch (tabPane.getSelectedIndex()) {
			case INDEX2D : setJMenuBar(menuBar2D); setVisAdditionalWindows(INDEX2D); break;
			case INDEXDESKTOP : setJMenuBar(menuBarDesktop); setVisAdditionalWindows(INDEXDESKTOP); break;
			default: break;
		}
		toFront();
	}

	/**
	 * @return   the menuBarGui2D
	 * @uml.property  name="menuBarGui2D"
	 */
	public MenuBarGui2D getMenuBarGui2D() {
		return menuBar2D;
	}

	/**
	 * @return   the menuBarDesktop
	 * @uml.property  name="menuBarDesktop"
	 */
	public MenuBarDesktop getMenuBarDesktop() {
		return menuBarDesktop;
	}

	/** This is the main of the application. Here, the main
	 * class is called and it is which creates all the menus system, information
	 * and it receive users orders.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		String lcOSName = System.getProperty("os.name").toLowerCase();
		Boolean IS_MAC = lcOSName.startsWith("mac os x");
		Boolean IS_WIN = lcOSName.startsWith("windows");
		
		if (IS_MAC) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "JavaVis");
		}
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		PropertyConfigurator.configure("resources/log4j.properties");
		final Gui frame = new Gui();
		if (IS_WIN) 
			frame.sep = "\\";
		else
			frame.sep = "/";
		WindowListener l = frame.new MyWindowAdapter(frame);
		frame.addWindowListener(l);
		frame.setDefaultCloseOperation(Gui.DO_NOTHING_ON_CLOSE);
		
		frame.setVisible(true);
		frame.setVisAdditionalWindows(INDEX2D);
		frame.toFront();
	}
	
	public void setVisAdditionalWindows (int index) {
		switch (index) {
			case (INDEX2D): setSize(600,125);
							g2d.setVisAdditionalWindows(true);
							break;
			case (INDEXDESKTOP): setSize(800,600);
     							g2d.setVisAdditionalWindows(false);
     							break;
		}
	}

	/**
	 * @return   Returns the g2d.
	 * @uml.property  name="g2d"
	 */
	public Gui2D getG2d() {
		return g2d;
	}

	/**
	 * @param g2d   The g2d to set.
	 * @uml.property  name="g2d"
	 */
	public void setG2d(Gui2D g2d) {
		this.g2d = g2d;
	}

	/**
	 * @return   Returns the gdesk.
	 * @uml.property  name="gdesk"
	 */
	public GuiDesktop getGdesk() {
		return gdesk;
	}

	/**
	 * @param gdesk   The gdesk to set.
	 * @uml.property  name="gdesk"
	 */
	public void setGdesk(GuiDesktop gd) {
		this.gdesk = gd;
	}

	/**
	 * @return   Returns the tabPane.
	 * @uml.property  name="tabPane"
	 */
	public JTabbedPane getTabPane() {
		return tabPane;
	}

	/**
	 * @param tabPane   The tabPane to set.
	 * @uml.property  name="tabPane"
	 */
	public void setTabPane(JTabbedPane tabPane) {
		this.tabPane = tabPane;
	}

	/**
	 * @return   Returns the prop.
	 * @uml.property  name="prop"
	 */
	public Properties getProp() {
		return prop;
	}

	/**
	 * @param prop   The prop to set.
	 * @uml.property  name="prop"
	 */
	public void setProp(Properties prop) {
		this.prop = prop;
	}


	/**
	 * Auxiliary class to capture the closing window event.
	 */
	class MyWindowAdapter extends WindowAdapter {
		/**
		 * @uml.property  name="gui"
		 * @uml.associationEnd  
		 */
		Gui gui;
		MyWindowAdapter (Gui guii) {
			gui = guii;
		}
		public void windowClosing(WindowEvent e) {
			gui.savePaths();
			JIPToolkit.exit (gui.getProp(), gui);
		}
	};
}
