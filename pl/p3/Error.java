
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
	





	
}
