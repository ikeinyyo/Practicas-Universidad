import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.io.*;



public class AnalizadorLexico {

	public DT dt;
	public RandomAccessFile ra;
	private Position position;

	public AnalizadorLexico(String fichero)  throws FileNotFoundException
	{
		try
		{
			File file = new File(fichero);
			ra = new RandomAccessFile(file, "r");
			dt = new DT();
			//dt.makeDT();
			position = new Position(1, 1);
		}
		catch(FileNotFoundException e)
		{
			throw e;
		}

	}

	public void readFile()
	{
		Token token = siguienteToken();

		while(token.Type() != Token.TokenType.EOF && token.Type() != Token.TokenType.error)
		{
			System.out.println(token);
			token = siguienteToken();
		}
		
		//System.out.println(token);
	}

	public Token siguienteToken()
	{
		char aux;
		Token token = new Token();

		try
		{
			aux = readCharacter();
		}
		catch(IOException e)
		{
			aux = '\0';
		}

		dt.transition(dt.charType(aux));

		while(!dt.execute(ra, token, aux, position))
		{


			//dt.transition(dt.charType(aux));

			//token = dt.ejecutar(ra, token, aux, row, column);

			try
			{
				aux = readCharacter();
			}
			catch(IOException e)
			{
				aux = '\0';
			}

			dt.transition(dt.charType(aux));

		}

		return token;
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

}
