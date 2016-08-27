package tia;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class Gui extends JFrame {	

	private final static int ANCHO = 800;
	private final static int ALTO = 600;

	private final static int RADIO_PUNTO = 2;

	private static final long serialVersionUID = 737365829727001543L;

	private List<Punto> listaPuntos;
	private List<Double> D;
	private List<Recta> listaRectas;
	private Canvas areaPuntos;

	private int num_lineas;
	private int num_iteraciones;

	//Creo nuevos widgets para controlar la aplicacion
	//Los creo aqui porque se necesitan datos en los eventos de los botones.
	//TextFiel para introducir el numero de lineas.
	private JLabel lb_num_lineas = new JLabel("Nº Lineas");
	private JTextField tf_num_lineas = new JTextField(3);
	private JLabel lb_num_it = new JLabel("Nº Iteraciones");
	private JTextField tf_num_it = new JTextField(3);
	private JTextField lb_error = new JTextField(10);
	public Gui() {
		super("AdaBoost");

		listaPuntos = new ArrayList<Punto>();	
		listaRectas = new ArrayList<Recta>();
		tf_num_lineas.setText("10");
		tf_num_it.setText("1000");

		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Area de dibujo
		areaPuntos = new Canvas();
		areaPuntos.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int tipo = e.getButton()==MouseEvent.BUTTON1?1:-1;
				listaPuntos.add(new Punto(e.getX(),e.getY(),tipo));
				areaPuntos.repaint();			
			}


		});
		this.add(areaPuntos, BorderLayout.CENTER);

		// Area de botones
		JPanel areaBotones = new JPanel();
		areaBotones.setLayout(new FlowLayout());
		this.add(areaBotones, BorderLayout.NORTH);



		//guardamos this para usarlo en el FileChooser
		final JFrame framethis=this;

		// Boton para cargar un fichero con todos los puntos
		areaBotones.add(new JButton(new AbstractAction("Cargar") {

			@Override
			public void actionPerformed(ActionEvent e) {
				FileDialog fd = new FileDialog(framethis, "Load", FileDialog.LOAD);
				fd.setVisible(true);
				String filename=fd.getFile();
				listaPuntos.clear();

				try {
					BufferedReader br = new BufferedReader(new FileReader(filename));
					try {
						StringBuilder sb = new StringBuilder();
						String line = br.readLine();

						while (line != null) {

							String[] flostr = line.split(" ");

							//int tipo = e.getButton()==MouseEvent.BUTTON1?1:-1;
							listaPuntos.add(new Punto(Float.parseFloat(flostr[0]),Float.parseFloat(flostr[1]),Float.parseFloat(flostr[2])));
							line = br.readLine();
						}

					} finally {
						br.close();
					}
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				areaPuntos.repaint();
			}
		}));

		// Boton para guardar un fichero con todos los puntos
		areaBotones.add(new JButton(new AbstractAction("Guardar") {

			@Override
			public void actionPerformed(ActionEvent e) {
				FileDialog fd = new FileDialog(framethis, "Save", FileDialog.SAVE);
				fd.setVisible(true);
				String filename=fd.getFile();
				try {
					PrintWriter out = new PrintWriter(filename);
					String text;
					for (Punto p : listaPuntos) {
						text=p.getX()+" "+p.getY()+" "+p.getTipo();
						out.println(text);
					}
					out.close();

				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				areaPuntos.repaint();
			}
		}));


		// Boton para ejecutar el algoritmo
		JButton botonComenzar = new JButton("Comenzar");
		botonComenzar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				init();
				try
				{
					num_lineas = Integer.parseInt(tf_num_lineas.getText());
				}
				catch(Exception exception)
				{
					num_lineas = 50;
				}
				try
				{
					num_iteraciones = Integer.parseInt(tf_num_it.getText());
				}
				catch(Exception exception)
				{
					num_iteraciones = 1000;
				}

				float mejorError = 1.0f;
				boolean salir = false;
				Recta mejor = null;
				int j = 0;
				double errorGlobal = 1.0;
				while(errorGlobal > 0.0 && j < num_lineas)
				{
					j++;
					mejor = null;
					salir = false;
					mejorError = 1.0f;
					for(int i = 0; i < num_iteraciones && !salir; i++)
					{
						Recta recta = new Recta(ANCHO, ALTO);
	
						float error = recta.calcularError(listaPuntos, D);
						if(error <= mejorError)
						{
							mejor = recta;
							mejorError = error;
							if(mejorError <= 0.0)
							{
								salir = true;
							}
						}
					}
					
					if(mejor != null)
					{
						
						mejor.aprender(listaPuntos, D);
						listaRectas.add(mejor);
					}
					
					errorGlobal = tasaDeError();
				}
				lb_error.setText("Error: " + errorGlobal);
				//System.out.println("Error: " + tasaDeError());
				areaPuntos.repaint();
				
			}
		});
		areaBotones.add(botonComenzar);

		// Boton para borrar todos los puntos
		JButton botonLimpiar = new JButton("Limpiar");
		botonLimpiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listaPuntos.clear();
				listaRectas.clear();
				areaPuntos.repaint();

			}
		});
		areaBotones.add(botonLimpiar);

		//Anyado  los nuevos widgets al area de botones
		areaBotones.add(lb_num_lineas);
		areaBotones.add(tf_num_lineas);
		areaBotones.add(lb_num_it);
		areaBotones.add(tf_num_it);
		lb_error.enableInputMethods(false);
		areaBotones.add(lb_error);
		
		this.setMinimumSize(new Dimension(ANCHO,ALTO));
	}

	public static void main(String[] args) {
		Gui gui = new Gui();
		gui.setVisible(true);
	}

	class Canvas extends JPanel {
		private static final long serialVersionUID = -4449288527123357984L;

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			
			
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			drawFondo(g);
			// Dibuja los puntos
			for(Punto p: listaPuntos) {
				if(p.getTipo()>0) {
					g.setColor(Color.BLUE);
				} else {
					g.setColor(Color.RED);
				}
				
				if(p.isCorrecto(listaRectas))
				{
					g.drawOval((int)p.getX()-RADIO_PUNTO, (int)p.getY()-RADIO_PUNTO, RADIO_PUNTO*2+1, RADIO_PUNTO*2+1);	
				}
				else
				{
					g.fillOval((int)p.getX()-RADIO_PUNTO, (int)p.getY()-RADIO_PUNTO, RADIO_PUNTO*4+1, RADIO_PUNTO*4+1);	
				}
				
				
			}

			for(Recta r : listaRectas)
			{
				r.paint(g);
			}
		}

	}

	private void init()
	{
		D = new ArrayList<Double>();
		listaRectas = new ArrayList<Recta>();
		double def =  1.0f/listaPuntos.size();

		for(int i = 0; i < listaPuntos.size(); i++)
		{
			D.add(def);
		}
	}
	
	private double tasaDeError()
	{
		double error = 0.0;
		
		for(int i = 0; i < listaPuntos.size(); i++)
		{
			if(!listaPuntos.get(i).isCorrecto(listaRectas))
			{
				error += D.get(i);
			}
		}
		return error;
	}
	
	private void drawFondo(Graphics g)
	{
		for(int i = 0; i < ANCHO; i += 5)
		{
			for(int j = 0; j < ALTO; j += 5)
			{
				double clase = 0.0;
				
				Punto p = new Punto(i, j, 1);
				clase = p.clasificar(listaRectas);
				
				if(Math.signum(clase) < 0)
				{
					g.setColor(Color.RED);
					g.drawOval(i, j, 1, 1);
				}
				else if(Math.signum(clase) > 0)
				{
					g.setColor(Color.BLUE);
					g.drawOval(i, j, 1, 1);
				}
			}
		}
	}
}
