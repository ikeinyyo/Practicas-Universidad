
public class Error {

	public enum E_Type {
		incorrect_char,
		ieof
	}
	
	public static void throwError(int row, int column, char c)
	{
		System.err.println("Error 1 (" + row + "," + column + "): caracter '" + c + "' incorrecto");
		System.exit(1);
	}
	
	public static void throwError()
	{
		System.err.println("Error 2: fin de fichero inesperado");
		System.exit(1);
	}
	
	public static void throwError6(int row, int column, String lexema)
	{
		System.err.println("Error 6 (" + row + "," + column + "): '" + lexema + "' no ha sido declarado");
		System.exit(1);
	}
	
	public static void throwError5(int row, int column, String lexema)
	{
		System.err.println("Error 5 (" + row + "," + column + "): '" + lexema + "' ya existe en este ambito");
		System.exit(1);
	}
	
	public static void throwError7(int row, int column, String lexema)
	{
		System.err.println("Error 7 (" + row + "," + column + "): '" + lexema + "' no es una variable");
		System.exit(1);
	}
	
	public static void throwError8(int row, int column, String lexema)
	{
		System.err.println("Error 8 (" + row + "," + column + "): '" + lexema + "' debe ser de tipo real");
		System.exit(1);
	}
	
	public static void throwError9(int row, int column)
	{
		System.err.println("Error 9 (" + row + "," + column + "): el operador ':=' no admite expresiones relacionales");
		System.exit(1);
	}
	
	
	public static void throwError10(int row, int column, String lexema)
	{
		System.err.println("Error 10 (" + row + "," + column + "): en la instruccion '" + lexema + "' la expresion debe ser relacional");
		System.exit(1);
	}

	public static void throwError11(int row, int column)
	{
		System.err.println("Error 11 (" + row + "," + column + "): los dos operandos de 'div' deben ser enteros");
		System.exit(1);
	}
	
	public static void throwError12(int row, int column)
	{
		System.err.println("Error 12 (" + row + "," + column + "): 'writeln' no admite expresiones booleanas");
		System.exit(1);
	}





	
}
