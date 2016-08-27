// AUTOR = 74380745; Gallardo Sales, Sergio
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.io.*;

import javax.swing.text.Position;


//Programa principal
class plp0
{
	static String fichero = "";

	public static void main(String[] argv)
	{
		if(leerArgumentos(argv))	//Entrada correcta
		{
			try
			{
				Analizador analizador = new Analizador(fichero);
				analizador.readFile();
				analizador.mostrarTokens();
			}
			catch(FileNotFoundException e)
			{
				System.out.println("No se ha encontrado el fichero.");
			}
		}
	}

	public static boolean leerArgumentos(String[] argv)
	{
		boolean correcto = false;
		if(argv.length == 1)
		{
			fichero = argv[0];
			correcto = true;
		}

		return correcto;
	}
}

class Token {

	public int fila;
	public int columna;
	public int indice;

	public String lexema;

	public int tipo;
	public static final int
	ID = 1,
	ENTERO = 2,
	REAL = 3;

	public Token(String lexema_, int fila_, int columna_, int tipo_, int indice_)
	{
		lexema = lexema_;
		fila = fila_;
		columna = columna_;
		tipo = tipo_;
		indice = indice_;
	}

	public String toString()
	{
		if(tipo == ID)
		{
			return "\"" + lexema + "\" " + fila + " " + columna + " " + indice; 
		}
		else
		{
			return "\"" + lexema + "\" " + fila + " " + columna; 
		}
	}

}

/*
 * ==================================================
 * 				DIAGRAMA DE TRANSICIONES
 * ==================================================
 */
class Transicion
{
	public Estado e_final;
	public int valor;

	public Transicion(Estado e_final_, int valor_)
	{
		e_final = e_final_;
		valor = valor_;
	}

}

class Estado
{
	ArrayList<Transicion> transiciones;
	String nombre;
	boolean aceptacion;
	int retornos;
	int tipo;

	public Estado(String nombre_, boolean aceptacion_, ArrayList<Transicion> transiciones_, int retornos_)
	{
		nombre = nombre_;
		transiciones = transiciones_;
		aceptacion = aceptacion_;
		retornos = retornos_;
		tipo = -1;
	}

	public Estado transicion(int valor)
	{
		Estado e_final = null;

		for (Transicion tran : transiciones) {

			if(tran.valor == valor)
			{
				return tran.e_final;
			}
		}

		for (Transicion tran : transiciones) {

			if(tran.valor == DT.OTRO)
			{
				return tran.e_final;
			}
		}


		return e_final;
	}
}


class DT
{
	ArrayList<Estado> estados;
	Estado e_inicial;
	Estado e_actual;

	static int L = 1;
	static int D = 2;
	static int PUNTO = 3;
	static int OTRO = 4;

	public DT(ArrayList<Estado> estados_, Estado e_inicial_)
	{
		estados = estados_;
		e_inicial = e_inicial_;
		e_actual = e_inicial;
	}

	public void transicion(int valor)
	{
		e_actual = e_actual.transicion(valor);
	}

	public String ejecutar(RandomAccessFile ra, String cadena, char c, ArrayList<Token> tokens, Posicion pos)
	{
		if(e_actual.aceptacion)
		{
			try
			{
				ra.seek(ra.getFilePointer() - e_actual.retornos);
			}
			catch(IOException e)
			{
				System.out.println("No se puede hacer seek");
			}

			if(e_actual.retornos > 1)
			{
				cadena = cadena.substring(0, cadena.length() - (e_actual.retornos - 1));
				pos.columna  -= (e_actual.retornos - 1);
			}

			int total = 0;
			for (Token token : tokens) {
				if(token.lexema.equals(cadena))
				{
					total++;
				}
			}
			tokens.add(new Token(cadena, pos.fila, pos.columna - (cadena.length() - 1), e_actual.tipo, total+1));


			e_actual = e_inicial;
			cadena = "";
		}
		else if(e_actual != e_inicial)
		{
			cadena += c;
			pos.columna++;
		}
		else
		{
			if(c == '\n')
			{
				pos.fila++;
				pos.columna = 0;
			}
			else
			{
				pos.columna++;
			}
		}

		return cadena;
	}

}

class Analizador
{
	public DT dt;
	public RandomAccessFile ra;
	public ArrayList<Token> tokens;
	//public const char EOF = '\0';

	public Analizador(String fichero) throws FileNotFoundException
	{
		try
		{
			File file = new File(fichero);
			ra = new RandomAccessFile(file, "r");
			crearDT();
			tokens = new ArrayList<Token>(); 
		}
		catch(FileNotFoundException e)
		{
			throw e;
		}

	}

	public void readFile()
	{
		char aux;
		String token = "";
		Posicion pos = new Posicion(1, 0);

		try
		{
			aux = readCharacter();
		}
		catch(IOException e)
		{
			aux = '\0';
		}

		while(aux != '\0')
		{


			dt.transicion(clasificar(aux));
			token = dt.ejecutar(ra, token, aux, tokens, pos);

			try
			{
				aux = readCharacter();
			}
			catch(IOException e)
			{
				aux = '\0';
			}


		}
	}

	public void mostrarTokens()
	{
		for (Token token : tokens) {
			System.out.println(token);
		}
	}

	private int clasificar(char c)
	{
		int valor = DT.OTRO;

		if((c >= 'a' && c <= 'z') || (c >= 'A' && c<= 'Z'))
		{
			valor = DT.L;
		}
		else if((c >= '0' && c <= '9'))
		{
			valor = DT.D;
		}
		else if(c == '.')
		{
			valor = DT.PUNTO;
		}

		return valor;
	}
	public char readCharacter() throws IOException
	{
		char currentChar;
		byte b = 0;
		try {
			b = ra.readByte();
			currentChar = (char)b;
			return currentChar;
		}
		catch (EOFException e) {
			return '\0';
		}

	}

	public void crearDT()
	{

		Estado id4 = new Estado("ID - 4", true, null, 1);
		id4.tipo = Token.ID;
		Estado entero6 = new Estado("ENTERO - 6", true, null, 2);
		entero6.tipo = Token.ENTERO;
		Estado entero3 = new Estado("ENTERO - 3", true, null, 1);
		entero3.tipo = Token.ENTERO;
		Estado flotante8 = new Estado("FLOTANTE - 8", true, null, 1);
		flotante8.tipo = Token.REAL;

		Estado inicial = new Estado("INICIAL - 0", false, null, 0);
		Estado e1 = new Estado("ESTADO - 1", false, null, 0);
		Estado e2 = new Estado("ESTADO - 2", false, null, 0);
		Estado e5 = new Estado("ESTADO - 5", false, null, 0);
		Estado e7 = new Estado("ESTADO - 7", false, null, 0);

		//Transiciones del estado 1
		ArrayList<Transicion> t1 = new ArrayList<Transicion>();

		t1.add(new Transicion(id4, DT.OTRO));
		t1.add(new Transicion(e1, DT.L));
		//t1.add(new Transicion(e1, DT.D));

		e1.transiciones = t1;

		//Transiciones del estado 2
		ArrayList<Transicion> t2 = new ArrayList<Transicion>();

		t2.add(new Transicion(e2, DT.D));
		t2.add(new Transicion(e5, DT.PUNTO));
		t2.add(new Transicion(entero3, DT.OTRO));

		e2.transiciones = t2;

		//Transiciones del estado 5
		ArrayList<Transicion> t5 = new ArrayList<Transicion>();

		t5.add(new Transicion(e7, DT.D));
		t5.add(new Transicion(entero6, DT.OTRO));

		e5.transiciones = t5;

		//Transiciones del estado 7
		ArrayList<Transicion> t7 = new ArrayList<Transicion>();

		t7.add(new Transicion(e7, DT.D));
		t7.add(new Transicion(flotante8, DT.OTRO));

		e7.transiciones = t7;

		//Transiciones del estado Inicial
		ArrayList<Transicion> tInicial = new ArrayList<Transicion>();

		tInicial.add(new Transicion(e1, DT.L));
		tInicial.add(new Transicion(e2, DT.D));
		tInicial.add(new Transicion(inicial, DT.OTRO));

		inicial.transiciones = tInicial;

		//Incluir en el Diagrama de Transiciones

		dt = new DT(null, null);

		dt.estados = new ArrayList<Estado>();

		dt.estados.add(inicial);
		dt.estados.add(e1);
		dt.estados.add(e2);
		dt.estados.add(entero3);
		dt.estados.add(id4);
		dt.estados.add(e5);
		dt.estados.add(entero6);
		dt.estados.add(e7);
		dt.estados.add(flotante8);

		dt.e_inicial = inicial;
		dt.e_actual = inicial;

	}
}

class Posicion
{
	public int fila;
	public int columna;

	public Posicion(int fila_, int columna_)
	{
		fila = fila_;
		columna = columna_;
	}
}