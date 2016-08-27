
public class Token {

	//Constantes
	public static enum TokenType { 
		none(""),
		pari("'('"),
		mulop("'*' '/' 'div'"),
		addop("'+' '-'"),
		relop("'<' '>' '<=' '>=' '=' '<>'"),
		pyc("';'"),
		dosp("':'"),
		coma("','"),
		asig("':='"),
		var("'var'"),
		real("'real'"),
		integer("'integer'"),
		program("'program'"),
		begin("'begin'"),
		end("'end'"),
		function("'function'"),
		t_if("'if'"),
		then("'then'"),
		t_else("'else'"),
		endif("'endif'"),
		t_while("'while'"),
		t_do("'do'"),
		writeln("'writeln'"),
		nentero("numero entero"),
		id("identificador"),
		nreal("numero real"),
		pard("')'"),
		coment("comentario"),
		error("error"),
		EOF("final de fichero");

		private final String text;

		TokenType(String text) {
			this.text = text;
		}

		public String toString() {
			return text;
		}
	}

	//Variables
	private String lexema;
	private int row;
	private int column;
	private TokenType type;
	private String text;

	//Constructores
	public Token(int row_, int column_)
	{
		lexema = "";
		row = row_;
		column = column_;
		type = TokenType.none;
		text = "";
	}
	public Token()
	{
		row = -1;
		column = -1;
		lexema = "";
		type = TokenType.none;
		text = "";
	}

	//Setters y Getters
	public String Lexema()
	{
		return lexema;
	}
	public void Lexema(String lexema_)
	{
		lexema = lexema_;
	}
	public int Row()
	{
		return row;
	}
	public void Row(int row_)
	{
		row = row_;
	}
	public int Column()
	{
		return column;
	}
	public void Column(int column_)
	{
		column = column_;
	}
	public TokenType Type()
	{
		return type;
	}
	public void Type(TokenType type_)
	{
		type = type_;
	}
	public String Text()
	{
		return text;
	}
	public void Text(String text_)
	{
		text = text_;
	}
	public String toString()
	{
		String string;

		string = lexema + " " + type + " " + row + " " + column;

		return string;
	}

}
