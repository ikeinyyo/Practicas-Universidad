
public class Error {

	public enum E_Type {
		incorrect_char,
		ieof
	}
	
	public static void throwError1(int row, int column, String lexema)
	{
		column++;
		System.err.println("Error 1 (" + row + "," + column + "): '" + lexema + "' ya existe en este ambito");
		System.exit(1);
	}
	
	public static void throwError()
	{
		System.err.println("Error 2: fin de fichero inesperado");
		System.exit(1);
	}
	
	public static void throwError2(int row, int column, String lexema)
	{
		column++;
		System.err.println("Error 2 (" + row + "," + column + "): simbolo '" + lexema + "' no ha sido declarado");
		System.exit(1);
	}
	
	public static void throwError3(int row, int column, String lexema)
	{
		column++;
		System.err.println("Error 3 (" + row + "," + column + "): tipo incompatible en operador aritmetico '" + lexema + "'");
		System.exit(1);
	}
	
	public static void throwError4(int row, int column, String lexema)
	{
		column++;
		System.err.println("Error 4 (" + row + "," + column + "): tipo incompatible en operador logico '" + lexema + "'");
		System.exit(1);
	}
	
	public static void throwError5(int row, int column, String lexema)
	{
		column++;
		System.err.println("Error 5 (" + row + "," + column + "): la expresion debe ser de tipo booleano en la instruccion '" + lexema + "'");
		System.exit(1);
	}
	
	public static void throwError6(int row, int column)
	{
		column++;
		System.err.println("Error 6 (" + row + "," + column + "): tipos incompatibles en la instruccion de asignacion");
		System.exit(1);
	}
	
	public static void throwError7(int row, int column)
	{
		column++;
		System.err.println("Error 7 (" + row + "," + column + "): tipos incompatibles en la instruccion de lectura");
		System.exit(1);
	}
	
	public static void throwError8(int row, int column)
	{
		column++;
		System.err.println("Error 8 (" + row + "," + column + "): tamanyo incorrecto");
		System.exit(1);
	}
	
	public static void throwError9(int row, int column, String lexema)
	{
		column++;
		System.err.println("Error 9 (" + row + "," + column + "): numero insuficiente de indices en el array '" + lexema + "'");
		System.exit(1);
	}
	
	public static void throwError10(int row, int column)
	{
		column++;
		System.err.println("Error 10 (" + row + "," + column + "): numero de dimensiones incorrecto");
		System.exit(1);
	}
	
	public static void throwError11(int row, int column, String lexema)
	{
		column++;
		System.err.println("Error 11 (" + row + "," + column + "): el identificador '" + lexema + "' no es de tipo array");
		System.exit(1);
	}
	
	public static void throwError12(int row, int column)
	{
		column++;
		System.err.println("Error 12 (" + row + "," + column + "): demasiados indices");
		System.exit(1);
	}
	
	public static void throwError13(int row, int column)
	{
		column++;
		System.err.println("Error 13 (" + row + "," + column + "): indice de tipo incompatible");
		System.exit(1);
	}
	
	public static void throwError14(String pos,  int tipo)
	{
		switch(tipo)
		{
		case Tipo.ENTERO:
			System.err.println("Error 14 (" + pos + "): tipo 'int' incompatible con la declaracion");
			break;
		case Tipo.REAL:
			System.err.println("Error 14 (" + pos + "): tipo 'double' incompatible con la declaracion");
			break;
		case Tipo.BOOLEAN:
			System.err.println("Error 14 (" + pos + "): tipo 'bool' incompatible con la declaracion");
			break;
		}
		System.exit(1);
	}
	
	public static void throwError15(int row, int column)
	{
		column++;
		System.err.println("Error 15 (" + row + "," + column + "): la variable que se intenta modificar es una variable indice");
		System.exit(1);
	}
	
	public static void throwError16(int row, int column, String lexema)
	{
		column++;
		System.err.println("Error 16 (" + row + "," + column + "): instruccion '" + lexema + "' no permitida fuera de un bucle");
		System.exit(1);
	}
	
	public static void throwError17(int row, int column)
	{
		column++;
		System.err.println("Error 17 (" + row + "," + column + "): la expresion debe ser de tipo numerico");
		System.exit(1);
	}
	
	public static void throwError18()
	{
		System.err.println("Error 18: debe existir un unico metodo Main");
		System.exit(1);
	}
	
	public static void throwError19(int row, int column, String lexema)
	{
		column++;
		System.err.println("Error 19 (" + row + "," + column + "): identificador '" + lexema + "' usado incorrectamente");
		System.exit(1);
	}
	
	public static void throwError21(int row, int column, Simbolo s, int tamano)
	{
		column++;
		String cadena = "Error 21 (" + row + "," + column + "): el metodo '" + s.tipo.tipoBase.clase + "." + s.nombre + "(";
		
		for(int  i = 0; i < tamano; i++)
		{
			if(i != 0)
			{
				cadena += ",";
			}
			cadena += "double";
		}
		cadena += ")' no esta definido";
		
		System.err.println(cadena);
		System.exit(1);
	}
	
	public static void throwError20(int row, int column, Simbolo s, int tamano)
	{
		column++;
		String cadena = "Error 20 (" + row + "," + column + "): el metodo '" + s.tipo.clase + "." + s.nombre + "(";
		
		for(int  i = 0; i < tamano; i++)
		{
			if(i != 0)
			{
				cadena += ",";
			}
			cadena += "double";
		}
		cadena += ")' ya esta definido";
		
		System.err.println(cadena);
		System.exit(1);
	}
	
	public static void throwError23(int row, int column)
	{
		column++;
		System.err.println("Error 23 (" + row + "," + column + "): valor devuelto de tipo incompatible");
		System.exit(1);
	}
	
	public static void throwError22(int row, int column)
	{
		column++;
		System.err.println("Error 22 (" + row + "," + column + "): aqui no puede usarse return");
		System.exit(1);
	}
	
	public static void throwError24(int row, int column)
	{
		column++;
		System.err.println("Error 24 (" + row + "," + column + "): tipo incompatible en el parametro");
		System.exit(1);
	}
	
	public static void throwError25(int row, int column)
	{
		column++;
		System.err.println("Error 25 (" + row + "," + column + "): no se permite la declaracion de arrays de objetos");
		System.exit(1);
	}

	public static void throwError26(int row, int column, String objeto, String clase)
	{
		column++;
		System.err.println("Error 26 (" + row + "," + column + "): objeto '" + objeto + "' no es de clase '" + clase + "'");
		System.exit(1);
	}

	public static void throwError27(int row, int column, String lexema)
	{
		column++;
		System.err.println("Error 27 (" + row + "," + column + "): miembro '" + lexema + "' no accesible desde Main");
		System.exit(1);
	}	
	
	public static void throwError29(int row, int column, String lexema)
	{
		column++;
		System.err.println("Error 29 (" + row + "," + column + "): tipo incompatible en operador relacional '" + lexema + "'");
		System.exit(1);
	}
	
	public static void throwError28(int row, int column)
	{
		column++;
		System.err.println("Error 28 (" + row + "," + column + "): la referencia es de tipo objeto");
		System.exit(1);
	}
	
	public static void throwError30(int row, int column)
	{
		column++;
		System.err.println("Error 30 (" + row + "," + column + "): constructor usado incorrectamente");
		System.exit(1);
	}
	
	public static void throwError31(int row, int column)
	{
		column++;
		System.err.println("Error 31 (" + row + "," + column + "): un constructor no puede devolver nada");
		System.exit(1);
	}
	
	public static void throwError32(int row, int column)
	{
		column++;
		System.err.println("Error 32 (" + row + "," + column + "): un metodo debe devolver algo");
		System.exit(1);
	}


	
}
