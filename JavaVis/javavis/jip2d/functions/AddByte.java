package javavis.jip2d.functions;

import javavis.base.ImageType;
import javavis.base.JIPException;
import javavis.base.parameter.ParamInt;
import javavis.jip2d.base.Function2D;
import javavis.jip2d.base.FunctionGroup;
import javavis.jip2d.base.JIPImage;
import javavis.jip2d.base.bitmaps.JIPBmpByte;

public class AddByte extends Function2D{

	public AddByte()
	{
		super();
		name = "AddColor";
		description = "Añade un valor al cada pixel de la imágen de tipo Byte";
		groupFunc = FunctionGroup.Gallardo;

		ParamInt p1 = new ParamInt("value", false, true);
		p1.setDefault(5);
		p1.setDescription("Valor a añadir a cada píxel");

		addParam(p1);
	}

	@Override
	public JIPImage processImg(JIPImage img) throws JIPException {

		JIPBmpByte res = null;
		int valor = getParamValueInt("value");

		if (img.getType() == ImageType.BYTE) {

			int width = img.getWidth();
			int height = img.getHeight();
			int totalPix = width*height;
			int numBands = ((JIPBmpByte)img).getNumBands();
			res = new JIPBmpByte(numBands, width, height);

			for (int nb=0; nb < numBands; nb++) {
				double[] bmp = ((JIPBmpByte)img).getAllPixels(nb);
				double[] bin = new double[totalPix];
				for (int k=0; k < totalPix; k++) {
					if(bmp[k] + valor <= 255 && bmp[k] + valor >= 0)
					{
						bin[k] = bmp[k] + valor;
					}
					else
					{
						if(bmp[k] + valor > 255)	
						{
							bin[k] = 255;
						}
						else
						{
							bin[k] = 0;
						}
					}
				}
				res.setAllPixels(nb, bin);
			}
		}
		else
		{
			throw new JIPException("La función AddColor solo funciona con imágenes tipo Byte");	
		}

		return res;
	}
}
