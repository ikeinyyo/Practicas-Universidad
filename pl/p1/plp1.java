import java.io.FileNotFoundException;


public class plp1 {

	static String fichero = "";
	
	public static void main(String[] args)
	{
		if(leerArgumentos(args))	//Entrada correcta
		{
		
			try
			{
				AnalizadorSintactico as = new AnalizadorSintactico(fichero);
				as.readFile();
			}
			catch(FileNotFoundException e)
			{
				System.out.println("No se ha encontrado el fichero.");
			}
		}
	}

	public static boolean leerArgumentos(String[] args)
	{
		boolean correcto = false;
		if(args.length == 1)
		{
			fichero = args[0];
			correcto = true;
		}

		return correcto;
	}

}
