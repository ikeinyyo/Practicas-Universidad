package javavis.jip2d.functions;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

import sun.awt.HorizBagLayout;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Parameter;

import javassist.compiler.ast.Pair;
import javavis.base.ImageType;
import javavis.base.JIPException;
import javavis.base.parameter.ParamFloat;
import javavis.base.parameter.ParamInt;
import javavis.base.parameter.ParamString;
import javavis.jip2d.base.Function2D;
import javavis.jip2d.base.FunctionGroup;
import javavis.jip2d.base.JIPImage;
import javavis.jip2d.base.Sequence;
import javavis.jip2d.base.bitmaps.JIPBmpByte;
import javavis.jip2d.base.bitmaps.JIPBmpColor;
import javavis.jip2d.base.geometrics.Edge;
import javavis.jip2d.base.geometrics.JIPGeomPoint;
import javavis.jip2d.base.geometrics.JIPGeomSegment;
import javavis.jip2d.base.geometrics.Point2D;
import javavis.jip2d.base.geometrics.Segment;

public class FPanorama extends Function2D {

	private ArrayList<Ventana> ventanas1, ventanas2;
	private ArrayList<Pareja> puntos_similares;
	final int HORIZONTAL = 1;
	final int VERTICAL = 2;

	public FPanorama()
	{
		super();
		name = "FPanorama";
		description = "Recibe una secuencia de imágenes y construye un panorama";
		groupFunc = FunctionGroup.Applic;

		ParamFloat p1 = new ParamFloat("thres", false, true);
		p1.setDefault(100f);
		p1.setDescription("thres");

		ParamInt p2 = new ParamInt("tamano", false, true);
		p2.setDefault(20);
		p2.setDescription("Tamaño");

		ParamFloat p3 = new ParamFloat("lambda", false, true);
		p3.setDefault(0.6f);
		p3.setDescription("Lambda");
		
		ParamInt p4 = new ParamInt("degradado", false, true);
		p4.setDefault(40);
		p4.setDescription("Ancho del degradado");

		addParam(p1);
		addParam(p2);
		addParam(p3);
		addParam(p4);


	}


	@Override
	public JIPImage processImg(JIPImage img) throws JIPException {
		throw new JIPException("Esta función sólo se puede aplicar a una secuencia.");	
	}

	@Override
	public Sequence processSeq(Sequence seq) throws JIPException {
		
		Sequence res = new Sequence();

		//Recuperar los parámetros
		float thres = getParamValueFloat("thres");
		int tamano = getParamValueInt("tamano");
		float lambda = getParamValueFloat("lambda");
		int degradado = getParamValueInt("degradado");


		//Crear la función Nitzberg
		Nitzberg nitz = new Nitzberg();
		nitz.setParamValue("thres", thres);



		while(seq.getNumFrames() > 1)
		{
			puntos_similares = new ArrayList<Pareja>();
			int i = 0;

			//Recupero las dos primeras imagenes y las guardo en img1 e img2
			JIPImage img1 = seq.getFrame(i);
			JIPImage img2 = seq.getFrame(i+1);

			//Aplicon Nitzberg a las dos imagenes
			JIPGeomPoint n1 = (JIPGeomPoint)nitz.processImg(img1);
			JIPGeomPoint n2 = (JIPGeomPoint)nitz.processImg(img2);

			//Paso las imagenes a escala de grises
			JIPImage byte1 = myColorToGrey(img1);
			JIPImage byte2 = myColorToGrey(img2);

			//Generos todas de cada imagen para cada punto característico.
			ventanas1 = getVentanas(byte1, n1, tamano);
			ventanas2 = getVentanas(byte2, n2, tamano);

			//Calculo los puntos similares con la correlacion cruzada
			calcularPuntosSimilares(lambda);



			//Genero la imagen de las dos imagenes juntas una debajo de la otra.
			JIPBmpColor juntas = generarImagenJuntas(img1, img2);

			//Crear la imágen geometrica que relaciona las dos imagenes.
			JIPImage puntos = crearImagenGeometrica(img1, img2);

			//Calcula los desplazamiento X e Y (mediana).
			Point2D des = getDesplazamientos();

			//Con el desplazamiento calculo la dirección del panorama.
			int direccion = calcularDireccionPanorama(des);


			//Intercambio las dos imagenes si es necesario para que la img1 sea la primera,
			//es decir, la imagen de la izquierda en panorama horizontal o la imagen de arriba
			//en panorama vertical.
			if((direccion == HORIZONTAL && des.getX() > 0) || (direccion == VERTICAL && des.getY() > 0))
			{
				if(des.getX() > 0)
				{
					//Intercambio las imagenes en color.
					JIPImage aux = img1;
					img1 = img2;
					img2 = aux;

					//Intercambio las imagens en escala de grises.
					aux = byte1;
					byte1 = byte2;
					byte2 = aux;

					//Invierto los desplazamientos
					des.setY(des.getY()*-1);
					des.setX(des.getX()*-1);
				}
			}

			//Variables para controlar el inicio y fin X,Y de las imágenes.
			//Y el desplazamiento de las imagenes X,Y
			int desX2,desY2;
			int inicioY1, inicioY2, finY1, finY2, inicioX1, inicioX2, finX1, finX2;
			int height = 0, width = 0;

			inicioX1 = inicioX2 = 0;
			desX2 = 0;
			inicioY1 = inicioY2 = 0;
			desY2 = 0;

			finX1 = img1.getWidth();
			finX2 = img2.getWidth();

			finY1 = img1.getHeight();
			finY2 = img2.getHeight();

			//Panorama Horizontal
			if(direccion == HORIZONTAL)
			{
				if(des.getY() <= 0)
				{
					desY2 = 0;

					inicioY1 = -1*des.getY();
					inicioY2 = 0;

					finY1 = img1.getHeight();
					finY2 = Math.min(img2.getHeight(),img1.getHeight() - inicioY1);
					height = finY2;

				}
				else
				{

					desY2 = 0;
					inicioY1 = 0;
					inicioY2 = des.getY();
					finY1 = Math.min(img1.getHeight(),img2.getHeight() - inicioY2);
					finY2 = img2.getHeight();
					height = finY1;
				}


				desX2 = Math.abs(des.getX());
				width = Math.max(img2.getWidth() + desX2, img1.getWidth());

			}
			else
			{
				if(des.getX() <= 0)
				{	
					desX2 = 0;

					inicioX1 = -1*des.getX();
					inicioX2 = 0;

					finX1 = img1.getWidth();
					finX2 = Math.min(img2.getWidth(),img1.getWidth() - inicioX1);
					width = finX2;

				}
				else
				{

					desX2 = 0;
					inicioX1 = 0;
					inicioX2 = des.getX();
					finX1 = Math.min(img1.getWidth(),img2.getWidth() - inicioX2);
					finX2 = img2.getWidth();
					width = finX1;
				}

				desY2 = Math.abs(des.getY());
				height = Math.max(img2.getHeight() + desY2, img1.getHeight());
			}

			JIPBmpColor pano = new JIPBmpColor(width, height);

			float cambioIntensidad1 = 0, cambioIntensidad2 = 0;

			//Calculo las medias de las dos imagenes en escala de grises.
			float intensidad1 = getIntensidadMedia((JIPBmpByte)byte1);
			float intensidad2 = getIntensidadMedia((JIPBmpByte)byte2);

			if(intensidad1 < intensidad2)
			{
				cambioIntensidad1 = (intensidad2 - intensidad1)/2;
				cambioIntensidad2 = (intensidad1 - intensidad2)/2;
			}
			else
			{
				cambioIntensidad2 = (intensidad1 - intensidad2)/2;
				cambioIntensidad1 = (intensidad2 - intensidad1)/2;
			}

			//Pinto la imagen 1
			for(int banda = 0; banda < ((JIPBmpColor)img1).getNumBands(); banda++)
			{
				for(int j = inicioX1; j < finX1; j++)
				{
					for(int k = inicioY1; k < finY1 ; k++)
					{
						//Calculo del coeficiente para el degradado.
						//1.0 Pinta el pixel el entero ... 0.0 no pinta el pixel
						float coeficiente = 1.0f;
						if(direccion == HORIZONTAL)
						{
							if((j - desX2) > 0 &&  (j - desX2) <= degradado) //Si estoy a 20 píxeles
							{
								coeficiente = 1-((j - desX2)/(float)degradado);
							}
						}
						else
						{
							if((k - desY2) > 0 &&  (k - desY2) <= degradado) //Si estoy a 20 píxeles
							{
								coeficiente = 1-((k - desY2)/(float)degradado);
							}
						}

						//Pintar en la imagen panorama la primera imagen.
						try
						{
							pano.setPixel(banda, j-inicioX1, k-inicioY1,(((JIPBmpColor)img1).getPixel(banda, j, k) + cambioIntensidad1)*coeficiente);
						}
						catch(Exception e)
						{

						}
					}
				}
			}

			//Pinto la seguna imagen
			for(int banda = 0; banda < ((JIPBmpColor)img2).getNumBands(); banda++)
			{
				for(int j = inicioX2; j < finX2; j++)
				{
					for(int k = inicioY2; k < finY2; k++)
					{
						float coeficiente = 1.0f;
						if(direccion == HORIZONTAL)
						{
							if(j - inicioX2 <= degradado) //Si estoy a DEGRADADO píxeles
							{
								coeficiente = (j - inicioX2)/(float)degradado;
							}
						}
						else
						{
							if(k - inicioY2 <= degradado) //Si estoy a DEGRADADO píxeles
							{
								coeficiente = (k - inicioY2)/(float)degradado;
							}
						}


						try
						{
							//Si el coeficiente != 1.0f, es que he pintado pixels en esta posicion
							//de la otra iamgen porque corresponde a la zona del degradado.
							//Asi que lo sumo a lo que ya hay.
							if(coeficiente != 1.0f)
							{
								pano.setPixel(banda, j+desX2-inicioX2, k+desY2-inicioY2, pano.getPixel(banda, j+desX2-inicioX2, k+desY2-inicioY2) + (((JIPBmpColor)img2).getPixel(banda, j, k) + cambioIntensidad2) * coeficiente);
							}
							else
							{
								pano.setPixel(banda, j+desX2-inicioX2, k+desY2-inicioY2,(((JIPBmpColor)img2).getPixel(banda, j, k) + cambioIntensidad2) * coeficiente);
							}
						}
						catch (Exception e)
						{

						}

					}
				}
			}

			juntas.setName("Imagenes juntas");
			res.addFrame(juntas);
			puntos.setName("Imagen relacional");
			res.addFrame(puntos);
			pano.setName("Resultado intermedio");
			res.addFrame(pano);

			seq.removeFrame(i+1);
			seq.setFrame(pano, i);

		}
		seq.getFrame(0).setName("Resultado Final");
		res.insertFrame(seq.getFrame(0), 0);
		return res;
	}

	private float CC(Ventana v1, Ventana v2) throws JIPException
	{
		float cc = 0.0f;

		double[] bw1 = v1.pixeles;
		double[] bw2 = v2.pixeles;

		//N: Numerador.
		//D1: Denominador parte (I1 - m1)^2
		//D2: Denominador parte (I2 - m2)^2
		float n = 0;
		float d1 = 0, d2 = 0;
		for(int i = 0;i < bw1.length; i++)
		{
			n += ((bw1[i] - v1.media) * (bw2[i] - v2.media));
			d1 += (bw1[i] - v1.media)*(bw1[i] - v1.media);
			d2 += (bw2[i] - v2.media)*(bw2[i] - v2.media);
		}

		d1 = (float) Math.sqrt(d1);
		d2 = (float) Math.sqrt(d2);
		cc = n / (d1*d2);

		return cc;
	}

	private JIPImage myColorToGrey(JIPImage img) throws JIPException
	{
		ColorToGray c2g = new ColorToGray();
		ParamString p = new ParamString("gray");
		p.setValue("BYTE");

		c2g.addParam(p);

		return c2g.processImg(img);
	}

	private ArrayList<Ventana> getVentanas(JIPImage img, JIPGeomPoint points, int tam)
	{
		ArrayList<Ventana> ventanas = new ArrayList<Ventana>();
		int x = 0, y = 0;

		for (int i = 0; i < points.getLength(); i++) 
		{
			try
			{
				x = points.getPoint(i).getX() - tam/2;
				y = points.getPoint(i).getY() - tam/2;
			}
			catch(Exception e)
			{
				System.err.println("Error al sacar X e Y de los puntos: " + e);
			}
			try
			{
				Crop crop = new Crop();
				crop.setParamValue("x", x);
				crop.setParamValue("y", y);
				crop.setParamValue("w", tam);
				crop.setParamValue("h", tam);
				//Recorto las ventanas alrededor del punto

				JIPImage window = crop.processImg(img);
				double[] bytesWindows = ((JIPBmpByte)window).getAllPixels(0);
				
				//lo guardo con su media y el punto caracteristico
				ventanas.add(new Ventana(points.getPoint(i), bytesWindows,getIntensidadMedia((JIPBmpByte)window)));
			}
			catch(Exception e)
			{
			}
		}

		return ventanas;
	}

	private JIPImage crearImagenGeometrica(JIPImage img1, JIPImage img2) throws JIPException
	{
		//Pinto para cada par de puntos un segmento
		JIPImage puntos = new JIPGeomSegment(Math.max(img1.getWidth(), img2.getWidth()), img1.getHeight() + img2.getHeight());
		for(int j = 0; j < puntos_similares.size(); j++)
		{
			Point2D p = new Point2D(puntos_similares.get(j).v2.punto);
			p.setY(p.getY() + img2.getHeight());

			Segment segmento = new Segment(puntos_similares.get(j).v1.punto, p);
			((JIPGeomSegment)puntos).addSegment(segmento);
		}

		return puntos;
	}
	private Point2D getDesplazamientos()
	{
		Point2D desplazamiento = null;
		ArrayList<Integer> desX, desY;
		desX = new ArrayList<Integer>();
		desY = new ArrayList<Integer>();

		//Calculo los desplazamientos
		for(int i = 0; i < puntos_similares.size(); i++)
		{
			desX.add(puntos_similares.get(i).v2.punto.getX() - puntos_similares.get(i).v1.punto.getX());
			desY.add(puntos_similares.get(i).v2.punto.getY() - puntos_similares.get(i).v1.punto.getY());
		}

		Collections.sort(desX);
		Collections.sort(desY);

		//Me quedo con la mediana
		if(desX.size() > 0 && desY.size() > 0)
		{
			desplazamiento = new Point2D(desX.get(desX.size()/2), desY.get(desY.size()/2));
		}
		else if(desX.size() > 0)
		{
			desplazamiento = new Point2D(desX.get(desX.size()/2), 0);
		}
		else if(desY.size() > 0)
		{
			desplazamiento = new Point2D(0, desY.get(desY.size()/2));
		}
		else
		{
			desplazamiento = new Point2D(0, 0);
		}
		return desplazamiento;
	}

	private float getIntensidadMedia(JIPBmpByte img)
	{
		float media = 0.0f;
		try
		{
			for(int j = 0 ; j < img.getWidth(); j++)
			{
				for(int k = 0; k < img.getHeight(); k++)
				{
					media += ((JIPBmpByte)img).getPixel(j, k);
				}
			}

			media = media / ((JIPBmpByte)img).getAllPixels().length;
		}
		catch(Exception e)
		{
			media = -1.0f;
			System.err.println("Error en el getIntensidadMedia ");
		}
		return media;
	}

	void calcularPuntosSimilares(float lambda)
	{
		float cc = 0.0f;

		for(int v1 = 0; v1 < ventanas1.size(); v1++)
		{
			Ventana ventana = null;
			float cc1 = 0, cc2 = 0;
			for(int v2 = 0; v2 < ventanas2.size(); v2++)
			{
				try
				{
					//Calculo el CC
					cc = CC(ventanas1.get(v1), ventanas2.get(v2));

					//Si es mejor que el anterior actualizo
					if(cc > cc1)
					{
						cc2 = cc1;
						cc1 = cc;

						ventana = ventanas2.get(v2);
					}
					else if(cc > cc2)
					{
						cc2 = cc;
					}
				}
				catch(Exception e)
				{

				}
			}

			//Si supera el umbral de lambda lo inserto como puntos comunes
			if(cc1 * lambda > cc2 && ventana != null)
			{
				puntos_similares.add(new Pareja(ventanas1.get(v1),ventana));
			}
		}
	}

	private JIPBmpColor generarImagenJuntas(JIPImage img1, JIPImage img2) throws JIPException
	{
		JIPBmpColor juntas = new JIPBmpColor(Math.max(img1.getWidth(), img2.getWidth()), img1.getHeight() + img2.getHeight());
		
		//Simplemente pinto las dos imagenes con el desplazamiento en Y de la segunda
		for(int banda = 0; banda < ((JIPBmpColor)img1).getNumBands(); banda++)
		{
			double [] bi1 = ((JIPBmpColor)img1).getAllPixels(banda);
			double [] bi2 = ((JIPBmpColor)img2).getAllPixels(banda);

			double[] bi12 = new double[Math.max(img1.getWidth(), img2.getWidth()) *(img1.getHeight() + img2.getHeight())];

			int fila = -1;
			int ancho_total = Math.max(img1.getWidth(), img2.getWidth());
			for(int j = 0; j < bi1.length; j++)
			{
				if(j%img1.getWidth() == 0)
				{
					fila++;
				}

				bi12[fila*ancho_total + j%img1.getWidth()] = bi1[j];
			}
			fila--;
			for(int j = 0; j < bi2.length; j++)
			{
				if(j%img2.getWidth() == 0)
				{
					fila++;
				}
				bi12[fila*ancho_total + j%img2.getWidth()] = bi2[j];

			}

			juntas.setAllPixels(banda, bi12);
		}

		return juntas;
	}

	private int calcularDireccionPanorama(Point2D des)
	{
		int direccion;

		if(Math.abs(des.getX()) > Math.abs(des.getY()))
		{
			direccion = HORIZONTAL;
		}
		else
		{
			direccion = VERTICAL;
		}

		return direccion;
	}

}

class  Ventana
{
	public Point2D punto;
	public double[] pixeles;
	public float media;

	public Ventana(Point2D point2d, double[] pxl, float media_)
	{
		punto = point2d;
		pixeles = pxl;
		media = media_;
	}
}

class Pareja {

	public Ventana v1;
	public Ventana v2;

	public Pareja(Ventana _v1, Ventana _v2)
	{
		v1 = _v1;
		v2 = _v2;
	}

}
