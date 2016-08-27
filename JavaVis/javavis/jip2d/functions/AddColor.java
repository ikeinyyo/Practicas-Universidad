package javavis.jip2d.functions;

import javavis.base.ImageType;
import javavis.base.JIPException;
import javavis.base.parameter.ParamInt;
import javavis.jip2d.base.Function2D;
import javavis.jip2d.base.FunctionGroup;
import javavis.jip2d.base.JIPImage;
import javavis.jip2d.base.bitmaps.JIPBmpByte;
import javavis.jip2d.base.bitmaps.JIPBmpColor;

public class AddColor extends Function2D{

	public AddColor()
	{
		super();
		name = "AddColor";
		description = "Añade un valor al cada pixel de la imágen de tipo Byte";
		groupFunc = FunctionGroup.Gallardo;

		ParamInt p1 = new ParamInt("rojo", false, true);
		p1.setDefault(5);
		p1.setDescription("Rojo");

		ParamInt p2 = new ParamInt("verde", false, true);
		p2.setDefault(5);
		p2.setDescription("Verde");

		ParamInt p3 = new ParamInt("azul", false, true);
		p3.setDefault(5);
		p3.setDescription("Azul");

		addParam(p1);
		addParam(p2);
		addParam(p3);
	}

	@Override
	public JIPImage processImg(JIPImage img) throws JIPException {

		JIPBmpColor res = null;
		int rojo = getParamValueInt("rojo");
		int verde = getParamValueInt("verde");
		int azul = getParamValueInt("azul");

		//if (img.getType() == ImageType.BYTE) {

		int width = img.getWidth();
		int height = img.getHeight();
		int totalPix = width*height;
		int numBands = ((JIPBmpColor)img).getNumBands();
		res = new JIPBmpColor(width, height);

		for (int nb=0; nb < numBands; nb++) {
			double[] bmp = ((JIPBmpColor)img).getAllPixels(nb);
			double[] bin = new double[totalPix];
			for (int k=0; k < totalPix; k++) {

				switch(nb)
				{
				case 0:
					bin[k] = bmp[k] + rojo;
					break;
				case 1:
					bin[k] = bmp[k] + verde;
					break;
				case 2:
					bin[k] = bmp[k] + azul;
					break;
				}
			}
			res.setAllPixels(nb, bin);
		}
		/*}
		else
		{
			throw new JIPException("La función AddColor solo funciona con imágenes tipo Byte");	
		}*/

		return res;
	}
}
