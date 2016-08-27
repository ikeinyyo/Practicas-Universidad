import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

class Transition
{
	public State e_final;
	public int value;

	public Transition(State e_final_, int value_)
	{
		e_final = e_final_;
		value = value_;
	}

}

class State
{
	ArrayList<Transition> transicions;
	String nombre;
	boolean aceptacion;
	int retornos;
	Token.TokenType type;
	int tag;

	public State(String nombre_, boolean aceptacion_, ArrayList<Transition> transicions_, int retornos_)
	{
		nombre = nombre_;
		transicions = transicions_;
		aceptacion = aceptacion_;
		retornos = retornos_;
	}

	public State(String nombre_, boolean aceptacion_, int retornos_)
	{
		nombre = nombre_;
		transicions = null;
		aceptacion = aceptacion_;
		retornos = retornos_;
	}

	public State(String nombre_)
	{
		nombre = nombre_;
		transicions = null;
		aceptacion = false;
		retornos = 0;

	}

	public State transition(int value)
	{
		State e_final = null;

		for (Transition tran : transicions) {

			if(tran.value == value)
			{
				return tran.e_final;
			}
		}

		for (Transition tran : transicions) {

			if(tran.value == DT.C_OTHER)
			{
				return tran.e_final;
			}
		}


		return e_final;
	}
}

class DT
{
	ArrayList<State> states;
	State e_inicial;
	State e_actual;

	//Constantes de Valor de Transición
	public static final int C_L = 1,
			C_D = 2,
			C_DOT = 3,
			C_EQUAL = 4,
			C_COMA = 5,
			C_DOTCOMA = 6,
			C_COLON = 7,
			C_LEFTB = 8,
			C_RIGHTB = 9,
			C_LT = 10,
			C_HT = 11,
			C_PLUS = 12,
			C_MINUS = 13,
			C_AST = 14,
			C_BAR = 15,
			C_EOF = 16,
			C_OTHER = 17,
			C_SEP = 18,
			C_ERROR = -1;


	public DT(ArrayList<State> states_, State e_inicial_)
	{
		states = states_;
		e_inicial = e_inicial_;
		e_actual = e_inicial;
	}

	public DT()
	{
		/*states = null;
		e_inicial = null;
		e_actual = null;*/
		makeDT();
	}

	public void transition(int value)
	{
		e_actual = e_actual.transition(value);
	}

	//ejecutar(ra, token, aux, row, column);
	public boolean execute(RandomAccessFile ra, Token token, char c, Position position)
	{
		boolean tokenOK = false;

		if(e_actual.aceptacion)
		{
			if(e_actual.type == Token.TokenType.coment)
			{
				token.Lexema("");
				e_actual = e_inicial;
				position.column++;
				token.Column(position.column);
				token.Row(position.row);
				tokenOK =  false;
			}
			else
			{
				if(e_actual.retornos == -1)
				{
					if(token.Lexema() == "")
					{
						token.Row(position.row);
						token.Column(position.column);
					}

					token.Lexema(token.Lexema() + c);

					position.column++;
				}
				else
				{
					try
					{
						if(ra.getFilePointer() == ra.length())
						{

						}
						else
						{
							ra.seek(ra.getFilePointer() - e_actual.retornos);
						}
					}
					catch(IOException e)
					{
						System.out.println("No se puede hacer seek");
					}

					if(e_actual.retornos > 1)
					{
						String cadena = token.Lexema();
						cadena = cadena.substring(0, cadena.length() - (e_actual.retornos - 1));
						token.Lexema(cadena);
						position.column  -= (e_actual.retornos - 1);
					}
				}

				if(e_actual.type == Token.TokenType.id)
				{
					token.Type(getType(token.Lexema()));
				}
				else
				{
					token.Type(e_actual.type);
				}
				e_actual = e_inicial;

				tokenOK =  true;
			}


		}
		else if(e_actual.type == Token.TokenType.error)
		{
			token.Type(Token.TokenType.error);
			switch(e_actual.tag)
			{
			case 1:
				Error.throwError(position.row, position.column, c);
				break;
			case 2:
				Error.throwError();
				break;
			}
			tokenOK = true;
		}
		else if(e_actual != e_inicial)				//Añade
		{
			if(token.Lexema() == "")
			{
				token.Row(position.row);
				token.Column(position.column);
			}
			token.Lexema(token.Lexema() + c);
			//position.column++;
			if(c == '\n' /*|| c == '\r'*/)
			{
				position.row++;
				position.column = 1;
			}
			else
			{
				position.column++;
			}
		}
		else
		{
			if(c == '\n' /*|| c == '\r'*/)
			{
				position.row++;
				position.column = 1;
			}
			else
			{
				position.column++;
			}
		}

		return tokenOK;
	}

	public int charType(char c)
	{
		int type = C_ERROR;

		if(c >= '0' && c <= '9')
		{
			type = C_D;
		}
		else if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
		{
			type = C_L;
		}
		else
		{
			switch (c) {
			case '.':
				type = C_DOT;
				break;
			case '=':
				type = C_EQUAL;
				break;
			case ',':
				type = C_COMA;
				break;
			case ';':
				type = C_DOTCOMA;
				break;
			case ':':
				type = C_COLON;
				break;
			case '(':
				type = C_LEFTB;
				break;
			case ')':
				type = C_RIGHTB;
				break;
			case '<':
				type = C_LT;
				break;
			case '>':
				type = C_HT;
				break;
			case '+':
				type = C_PLUS;
				break;
			case '-':
				type = C_MINUS;
				break;
			case '*':
				type = C_AST;
				break;
			case '/':
				type = C_BAR;
				break;
			case ' ':
			case '\t':
			case '\n':
			case '\r':
				type = C_SEP;
				break;
			case '\0':
				type = C_EOF;
				break;
			}
		}

		return type;
	}

	public void makeDT()
	{
		State e1 = new State("E1 - Estado incial");
		//ID
		State e2 = new State("E2");
		State e3 = new State("E3 - ID", true, 1);
		e3.type = Token.TokenType.id;
		//Números
		State e4 = new State("E4");
		State e5 = new State("E5 - Entero", true, 1);
		e5.type = Token.TokenType.nentero;
		State e6 = new State("E6");
		State e7 = new State("E7");
		State e8 = new State("E8 - Real", true, 1);
		e8.type = Token.TokenType.nreal;
		State e9 = new State("E9 - Entero", true, 2);
		e9.type = Token.TokenType.nentero;

		//Puntuación y operandos
		State e10 = new State("E10");
		State e11 = new State("E11 - asig", true, -1);
		e11.type = Token.TokenType.asig;
		State e12 = new State("E12 - dosp", true, 1);
		e12.type = Token.TokenType.dosp;
		State e13 = new State("E13 - coma", true, -1);
		e13.type = Token.TokenType.coma;
		State e14 = new State("E14 - mulop", true, -1);
		e14.type = Token.TokenType.mulop;
		State e15 = new State("E15 - pyc", true, -1);
		e15.type = Token.TokenType.pyc;
		State e16 = new State("E16 - addop", true, -1);
		e16.type = Token.TokenType.addop;

		//Comparación
		State e17 = new State("E17");
		State e18 = new State("E18 - relop <=", true, -1);
		e18.type = Token.TokenType.relop;
		State e19 = new State("E19 - relop <>", true, -1);
		e19.type = Token.TokenType.relop;
		State e20 = new State("E20 - relop <", true, 1);
		e20.type = Token.TokenType.relop;
		State e21 = new State("E21");
		State e22 = new State("E22 - relop >=", true, -1);
		e22.type = Token.TokenType.relop;
		State e23 = new State("E23 - relop >", true, 1);
		e23.type = Token.TokenType.relop;
		State e24 = new State("E24 - relop =", true, -1);
		e24.type = Token.TokenType.relop;

		//Comentarios
		State e25 = new State("E25 - Inicio Comentario");
		State e26 = new State("E26 - pari", true, 1);
		e26.type = Token.TokenType.pari;
		State e27 = new State("E27");
		State e28 = new State("E28");
		State e29 = new State("E29 - Comentario", true, -1);
		e29.type = Token.TokenType.coment;

		//)
		State e30 = new State("E30 - pard", true, -1);
		e30.type = Token.TokenType.pard;
		//Especiales
		State eEOF = new State("Estado Fin de Fichero", true, -1);
		eEOF.type = Token.TokenType.EOF;
		State eError = new State("Estado Error");
		eError.type = Token.TokenType.error;
		eError.tag = 1;

		State eErrEOF = new State("Estado Error fin fichero");
		eErrEOF.type = Token.TokenType.error;
		eErrEOF.tag = 2;

		//TRANSICIONES
		//Estado 1
		e1.transicions = new ArrayList<Transition>();
		e1.transicions.add(new Transition(e1, C_SEP));
		e1.transicions.add(new Transition(e2, C_L));
		e1.transicions.add(new Transition(e4, C_D));
		e1.transicions.add(new Transition(e15, C_DOTCOMA));
		e1.transicions.add(new Transition(e10, C_COLON));
		e1.transicions.add(new Transition(e13, C_COMA));
		e1.transicions.add(new Transition(e14, C_AST));
		e1.transicions.add(new Transition(e14, C_BAR));
		e1.transicions.add(new Transition(e16, C_PLUS));
		e1.transicions.add(new Transition(e16, C_MINUS));
		e1.transicions.add(new Transition(e17, C_LT));
		e1.transicions.add(new Transition(e21, C_HT));
		e1.transicions.add(new Transition(e24, C_EQUAL));
		e1.transicions.add(new Transition(e25, C_LEFTB));
		e1.transicions.add(new Transition(e30, C_RIGHTB));
		e1.transicions.add(new Transition(eEOF, C_EOF));
		e1.transicions.add(new Transition(eError, C_OTHER));

		//Estado 2
		e2.transicions = new ArrayList<Transition>();
		e2.transicions.add(new Transition(e2, C_L));
		e2.transicions.add(new Transition(e2, C_D));
		e2.transicions.add(new Transition(e3, C_OTHER));

		//Estado 4
		e4.transicions = new ArrayList<Transition>();
		e4.transicions.add(new Transition(e4, C_D));
		e4.transicions.add(new Transition(e6, C_DOT));
		e4.transicions.add(new Transition(e5, C_OTHER));

		//Estado 6
		e6.transicions = new ArrayList<Transition>();
		e6.transicions.add(new Transition(e7, C_D));
		e6.transicions.add(new Transition(e9, C_OTHER));

		//Estado 7
		e7.transicions = new ArrayList<Transition>();
		e7.transicions.add(new Transition(e7, C_D));
		e7.transicions.add(new Transition(e8, C_OTHER));

		//Estado 10
		e10.transicions = new ArrayList<Transition>();
		e10.transicions.add(new Transition(e11, C_EQUAL));
		e10.transicions.add(new Transition(e12, C_OTHER));

		//Estado 17
		e17.transicions = new ArrayList<Transition>();
		e17.transicions.add(new Transition(e18, C_EQUAL));
		e17.transicions.add(new Transition(e19, C_HT));
		e17.transicions.add(new Transition(e20, C_OTHER));

		//Estado 21
		e21.transicions = new ArrayList<Transition>();
		e21.transicions.add(new Transition(e22, C_EQUAL));
		e21.transicions.add(new Transition(e23, C_OTHER));

		//Estado 25
		e25.transicions = new ArrayList<Transition>();
		e25.transicions.add(new Transition(e27, C_AST));
		e25.transicions.add(new Transition(e26, C_OTHER));

		//Estado 27
		e27.transicions = new ArrayList<Transition>();
		e27.transicions.add(new Transition(e28, C_AST));
		e27.transicions.add(new Transition(eErrEOF, C_EOF));
		e27.transicions.add(new Transition(e27, C_OTHER));

		//Estado 28
		e28.transicions = new ArrayList<Transition>();
		e28.transicions.add(new Transition(e28, C_AST));
		e28.transicions.add(new Transition(e29, C_RIGHTB));
		e28.transicions.add(new Transition(eErrEOF, C_EOF));
		e28.transicions.add(new Transition(e27, C_OTHER));

		states = new ArrayList<State>();
		states.add(e1);
		states.add(e2);
		states.add(e3);
		states.add(e4);
		states.add(e5);
		states.add(e6);
		states.add(e7);
		states.add(e8);
		states.add(e9);
		states.add(e10);
		states.add(e11);
		states.add(e12);
		states.add(e13);
		states.add(e14);
		states.add(e15);
		states.add(e16);
		states.add(e17);
		states.add(e18);
		states.add(e19);
		states.add(e20);
		states.add(e21);
		states.add(e22);
		states.add(e23);
		states.add(e24);
		states.add(e25);
		states.add(e26);
		states.add(e27);
		states.add(e28);
		states.add(e29);

		states.add(eError);
		states.add(eEOF);

		e_inicial = e1;
		e_actual = e_inicial;

	}

	private Token.TokenType getType(String lexema)
	{
		Token.TokenType type = Token.TokenType.id;

		if(lexema.equals("var"))
		{
			type = Token.TokenType.var;
		}
		else if(lexema.equals("real"))
		{
			type = Token.TokenType.real;
		}
		else if(lexema.equals("integer"))
		{
			type = Token.TokenType.integer;
		}
		else if(lexema.equals("program"))
		{
			type = Token.TokenType.program;
		}
		else if(lexema.equals("begin"))
		{
			type = Token.TokenType.begin;
		}
		else if(lexema.equals("end"))
		{
			type = Token.TokenType.end;
		}
		else if(lexema.equals("function"))
		{
			type = Token.TokenType.function;
		}
		else if(lexema.equals("if"))
		{
			type = Token.TokenType.t_if;
		}
		else if(lexema.equals("then"))
		{
			type = Token.TokenType.then;
		}
		else if(lexema.equals("else"))
		{
			type = Token.TokenType.t_else;
		}
		else if(lexema.equals("endif"))
		{
			type = Token.TokenType.endif;
		}
		else if(lexema.equals("while"))
		{
			type = Token.TokenType.t_while;
		}
		else if(lexema.equals("do"))
		{
			type = Token.TokenType.t_do;
		}
		else if(lexema.equals("writeln"))
		{
			type = Token.TokenType.writeln;
		}
		else if(lexema.equals("div"))
		{
			type = Token.TokenType.mulop;
		}

		return type;

	}
}

class Position
{
	public int row;
	public int column;

	public Position(int row_, int column_)
	{
		row = row_;
		column = column_;
	}
}

/**
 * A -> Aa
 * A -> b
 * 
 * A -> bA'
 * A' -> aA'
 * A' -> e
 * 
 * */
