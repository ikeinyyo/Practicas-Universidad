import java.io.FileNotFoundException;
import java.util.ArrayList;

public class AnalizadorSintactico {

	AnalizadorLexico al;
	Token token;
	ArrayList<Token.TokenType> tokensEsperados;
	TablaSimbolos ts = new TablaSimbolos();
	ArrayList<Simbolo> simbolos;
	int tipoSimbolo;

	public AnalizadorSintactico(String nameFile)  throws FileNotFoundException {
		try
		{
			al = new AnalizadorLexico(nameFile);
			tokensEsperados = new ArrayList<Token.TokenType>();
		}
		catch(FileNotFoundException e)
		{
			throw e;
		}
	}

	public void readFile()
	{
		//al.readFile();
		token = al.siguienteToken();

		Atributos at = S(); // símbolo inicial de la gramática

		if (token.Type() != Token.TokenType.EOF)
		{
			tokensEsperados.add(Token.TokenType.EOF);
			errorSintaxis();
		}
		else
		{
			traducir(at.trad);
		}

	}

	//Funciones auxiliares
	void emparejar(Token.TokenType tokEsperado)
	{
		if (token.Type() == tokEsperado)
			token = al.siguienteToken();
		else
		{
			tokensEsperados.add(tokEsperado);
			errorSintaxis();
		}
	}

	private void traducir(String trad)
	{
		System.out.print(trad);
	}

	private void errorSintaxis()
	{
		if(token.Type() == Token.TokenType.EOF)
		{
			System.err.print("Error 4: encontrado final de fichero, esperaba");
		}
		else
		{
			System.err.print("Error 3 (" + token.Row() + "," + token.Column() + "): encontrado '" + token.Lexema() + "', esperaba");
		}

		for(Token.TokenType t : tokensEsperados)
		{
			System.err.print(" " + t);
		}
		System.err.println();
		//tokensEsperados = new ArrayList<Token.TokenType>();

		System.exit(1);
	}

	private Token.TokenType token()
	{
		return token.Type();
	}

	//Reglas de la gramatica
	private Atributos S()
	{
		Atributos at = new Atributos();
		if(token() == Token.TokenType.program)
		{
			emparejar(Token.TokenType.program);
			at.trad = "// program " + token.Lexema() + "\n"; 
			emparejar(Token.TokenType.id);
			emparejar(Token.TokenType.pyc);
			at.trad += VSp(null).trad;
			at.trad += Bloque(true).trad;

		}
		else
		{
			tokensEsperados.add(Token.TokenType.program);
			errorSintaxis();
		}

		return at;
	}

	private Atributos VSp(Atributos h)
	{
		Atributos at = new Atributos();
		if(token() == Token.TokenType.function || token() == Token.TokenType.var)
		{
			at = UnVSp(h);
			at.trad += VSp2(h).trad;
		}
		else
		{
			tokensEsperados.add(Token.TokenType.var);
			tokensEsperados.add(Token.TokenType.function);
			errorSintaxis();
		}

		return at;
	}

	private Atributos VSp2(Atributos h)
	{
		Atributos at = new Atributos();

		if(token() == Token.TokenType.function || token() == Token.TokenType.var)
		{
			at.trad = UnVSp(h).trad;
			at.trad += VSp2(h).trad;
		}
		else if(token() == Token.TokenType.begin)
		{

		}
		else
		{
			tokensEsperados.add(Token.TokenType.var);
			tokensEsperados.add(Token.TokenType.begin);
			tokensEsperados.add(Token.TokenType.function);
			errorSintaxis();
		}

		return at;
	}


	private Atributos UnVSp(Atributos h)
	{
		Atributos at = new Atributos();
		String auxFunc = "", auxTrad = "";

		if(token() == Token.TokenType.function)
		{
			//at.trad = token.Lexema();

			emparejar(Token.TokenType.function);

			if(!ts.existe(token.Lexema()))
			{
				if(h == null || h.th2 == "")
				{
					auxFunc = auxTrad = token.Lexema();
					at.trad += " " + token.Lexema() + "()";
					at.th2 += token.Lexema() + "_";
				}
				else
				{
					auxFunc = token.Lexema();
					auxTrad = h.th2 + token.Lexema();
					at.trad += " " + h.th2 + token.Lexema() + "()";;
					at.th2 += h.th2 + token.Lexema() + "_";
				}
			}
			else
			{
				Error.throwError5(token.Row(), token.Column(), token.Lexema());
			}
			emparejar(Token.TokenType.id);
			emparejar(Token.TokenType.dosp);
			Atributos tipo = Tipo();
			at.th1 = tipo.trad;
			at.trad = at.th1 + " " + at.trad + " ";
			emparejar(Token.TokenType.pyc);
			ts.nuevoSimbolo(auxFunc, auxTrad,Simbolo.METODO, tipo.tipo);

			ts = new TablaSimbolos(ts);
			at.trad = VSp(at).trad + at.trad;
			at.trad += Bloque(false).trad;
			emparejar(Token.TokenType.pyc);
			ts = ts.pop();
		}
		else if(token() == Token.TokenType.var)
		{
			//simbolos = new ArrayList<Simbolo>();
			emparejar(Token.TokenType.var);
			at.trad = LV(h).trad;
			/*for(Simbolo s: simbolos)
			{
				s.tipo = tipoSimbolo;
			}*/
		}
		else 
		{
			tokensEsperados.add(Token.TokenType.var);
			tokensEsperados.add(Token.TokenType.function);
			errorSintaxis();
		}

		return at;
	}

	private Atributos LV(Atributos h)
	{
		Atributos at = new Atributos();
		if(token() == Token.TokenType.id)
		{
			at.trad = V(h).trad;
			at.trad += LV2(h).trad;
		}
		else
		{
			tokensEsperados.add(Token.TokenType.id);
			errorSintaxis();
		}

		return at;
	}

	private Atributos LV2(Atributos h)
	{
		Atributos at = new Atributos();
		if(token() == Token.TokenType.id)
		{
			at.trad = V(h).trad;
			at.trad += LV2(h).trad;
		}
		else if(token() == Token.TokenType.function || token() == Token.TokenType.var || token() == Token.TokenType.begin)
		{
			at.trad = "";
		}
		else
		{
			tokensEsperados.add(Token.TokenType.var);
			tokensEsperados.add(Token.TokenType.begin);
			tokensEsperados.add(Token.TokenType.function);
			tokensEsperados.add(Token.TokenType.id);
			errorSintaxis();
		}

		return at;
	}

	private Atributos V(Atributos h)
	{
		Atributos at = new Atributos();
		String auxVar = "";
		String auxTrad = "";
		Atributos tipo;

		if(token() == Token.TokenType.id)
		{
			simbolos = new ArrayList<Simbolo>();
			if(h == null || h.th2 == "")
			{
				auxVar = token.Lexema();
				auxTrad = "main_" + token.Lexema();
			}
			else
			{
				auxVar = token.Lexema();
				auxTrad = h.th2 + token.Lexema();
			}

			if(!ts.existe(auxVar))
			{
				//Añado la variable a la tabla de simbolos sin tipo. Y a la lista de nuevas variables.
				Simbolo s = ts.nuevoSimbolo(auxVar, auxTrad, Simbolo.VAR, Simbolo.NULL);
				simbolos.add(s);
			}
			else
			{
				//Error ya existe.
				Error.throwError5(token.Row(), token.Column(), token.Lexema());
			}

			at.trad = auxTrad;
			emparejar(Token.TokenType.id);
			at.trad += LI(h).trad;
			emparejar(Token.TokenType.dosp);
			tipo = Tipo();
			tipoSimbolo = tipo.tipo;
			at.th1 = tipo.trad;
			at.tipo = tipo.tipo;
			at.trad = at.th1  + " " + at.trad + ";\n";
			
			for(Simbolo s: simbolos)
			{
				s.tipo = tipoSimbolo;
			}
			emparejar(Token.TokenType.pyc);
		}
		else
		{
			tokensEsperados.add(Token.TokenType.id);
			errorSintaxis();
		}

		return at;
	}

	private Atributos LI(Atributos h)
	{
		Atributos at = new Atributos();

		if(token() == Token.TokenType.coma)
		{
			emparejar(Token.TokenType.coma);

			if(h == null || h.th2 == "")
			{
				at.trad = ", main_" + token.Lexema();

				if(!ts.existe(token.Lexema()))
				{
					//Añado la variable a la tabla de simbolos sin tipo. Y a la lista de nuevas variables.
					Simbolo s = ts.nuevoSimbolo(token.Lexema(),"main_" + token.Lexema(), Simbolo.VAR, Simbolo.NULL);
					simbolos.add(s);
				}
				else
				{
					//Error ya existe.
					Error.throwError5(token.Row(), token.Column(), token.Lexema());

				}
			}
			else
			{
				at.trad = ", "  + h.th2 + token.Lexema();
				//ts.nuevoSimbolo(token.Lexema(), h.th2 + token.Lexema(), Simbolo.VAR, h.tipo);
				if(!ts.existe(token.Lexema()))
				{
					//Añado la variable a la tabla de simbolos sin tipo. Y a la lista de nuevas variables.
					Simbolo s = ts.nuevoSimbolo(token.Lexema(), h.th2 + token.Lexema(), Simbolo.VAR, Simbolo.NULL);
					simbolos.add(s);
				}
				else
				{
					//Error ya existe.
					Error.throwError5(token.Row(), token.Column(), token.Lexema());

				}
			}
			emparejar(Token.TokenType.id);
			at.trad += LI(h).trad;
		}
		else if(token() == Token.TokenType.dosp)
		{
			at.trad = "";
		}
		else
		{
			tokensEsperados.add(Token.TokenType.dosp);
			tokensEsperados.add(Token.TokenType.coma);
			errorSintaxis();
		}
		return at;
	}

	private Atributos Tipo()
	{
		Atributos at = new Atributos();

		if(token() == Token.TokenType.integer)
		{
			at.trad = "int";
			at.tipo = Simbolo.ENTERO;
			emparejar(Token.TokenType.integer);
		}
		else if(token() == Token.TokenType.real)
		{
			at.trad = "double";
			at.tipo = Simbolo.REAL;
			emparejar(Token.TokenType.real);
		}
		else
		{
			tokensEsperados.add(Token.TokenType.real);
			tokensEsperados.add(Token.TokenType.integer);
			errorSintaxis();
		}

		return at;
	}

	private Atributos Bloque(boolean main)
	{
		Atributos at = new Atributos();

		if(token() == Token.TokenType.begin)
		{
			if(main)
			{
				at.trad = "int main() ";
			}
			else
			{
				at.trad = "";
			}
			at.trad += "{\n";
			emparejar(Token.TokenType.begin);
			at.trad += SInstr().trad;
			emparejar(Token.TokenType.end);
			at.trad += "}\n";
		}
		else
		{
			tokensEsperados.add(Token.TokenType.begin);
			errorSintaxis();
		}

		return at;
	}


	private Atributos SInstr()
	{
		Atributos at = new Atributos();
		if(token() == Token.TokenType.t_while ||
				token() == Token.TokenType.writeln ||
				token() == Token.TokenType.t_if ||
				token() == Token.TokenType.id ||
				token() == Token.TokenType.begin)
		{
			at.trad = Instr().trad;
			at.trad += SInstr2().trad;
		}
		else
		{
			tokensEsperados.add(Token.TokenType.begin);
			tokensEsperados.add(Token.TokenType.t_if);
			tokensEsperados.add(Token.TokenType.t_while);
			tokensEsperados.add(Token.TokenType.writeln);
			tokensEsperados.add(Token.TokenType.id);
			errorSintaxis();
		}

		return at;
	}

	private Atributos SInstr2()
	{
		Atributos at = new Atributos();
		if(token() == Token.TokenType.pyc)
		{
			emparejar(Token.TokenType.pyc);
			at.trad = Instr().trad;
			at.trad += SInstr2().trad;
		}
		else if(token() == Token.TokenType.end)
		{
			at.trad = "";
		}
		else
		{
			tokensEsperados.add(Token.TokenType.pyc);
			tokensEsperados.add(Token.TokenType.end);
			errorSintaxis();
		}
		return at;
	}

	private Atributos Instr()
	{
		Atributos at = new Atributos();
		int rowbool = 0, columnbool = 0, rowreal = 0, columnreal = 0;
		String tokenreal = "";
		if(token() == Token.TokenType.begin)
		{
			at.trad = Bloque(false).trad;
		}
		else if(token() == Token.TokenType.id)
		{
			//at.trad = token.Lexema();
			if(ts.busca(token.Lexema()) != null)
			{
				if(ts.busca(token.Lexema()).tipoSimbolo == Simbolo.VAR)
				{
					tokenreal = ts.busca(token.Lexema()).nombre;
					at.trad = ts.busca(token.Lexema()).traduccion;
					//at.th1 = ts.busca(token.Lexema()).traduccion;
					at.tipo = ts.busca(token.Lexema()).tipo;
					rowreal = token.Row();
					columnreal = token.Column();
				}
				else
				{
					Error.throwError7(token.Row(), token.Column(), token.Lexema());
				}
			}
			else
			{
				//ERROR
				Error.throwError6(token.Row(), token.Column(), token.Lexema());
			}
			emparejar(Token.TokenType.id);
			//at.trad += " = ";
			rowbool = token.Row();
			columnbool = token.Column();
			emparejar(Token.TokenType.asig);
			//at.trad += E(at).trad + ";\n";
			Atributos op1 = E(at);
			if(at.tipo == Simbolo.ENTERO && op1.tipo == Simbolo.ENTERO)
			{
				at.trad += " =i " + op1.trad + ";\n";
			}
			else if(at.tipo == Simbolo.REAL && op1.tipo == Simbolo.ENTERO)
			{
				at.trad += " =r itor(" + op1.trad + ");\n";
			}
			else if(at.tipo == Simbolo.ENTERO && op1.tipo == Simbolo.REAL)
			{
				Error.throwError8(rowreal, columnreal, tokenreal);
			}
			else if(at.tipo == Simbolo.REAL && op1.tipo == Simbolo.REAL)
			{
				at.trad += " =r " + op1.trad + ";\n";
			}
			else if(at.tipo == Simbolo.BOOL || op1.tipo == Simbolo.BOOL)
			{
				Error.throwError9(rowbool, columnbool);
			}

		}
		else if(token() == Token.TokenType.t_if)
		{
			at.trad = "if ( ";
			rowbool = token.Row();
			columnbool = token.Column();
			emparejar(Token.TokenType.t_if);

			Atributos op2 = E(at);

			if(op2.tipo != Simbolo.BOOL)
			{
				Error.throwError10(rowbool, columnbool, "if");
			}
			at.trad += op2.trad + " )\n";
			emparejar(Token.TokenType.then);
			at.trad +=Instr().trad;
			at.trad +=Instr2().trad;
		}
		else if(token() == Token.TokenType.t_while)
		{
			at.trad = "while ( ";
			rowbool = token.Row();
			columnbool = token.Column();
			emparejar(Token.TokenType.t_while);

			Atributos op2 = E(at);
			if(op2.tipo != Simbolo.BOOL)
			{
				Error.throwError10(rowbool, columnbool, "while");	
			}

			at.trad += op2.trad + " )\n";
			emparejar(Token.TokenType.t_do);
			at.trad +=Instr().trad;
		}
		else if(token() == Token.TokenType.writeln)
		{
			//writeln(a+3) ==> printf("%d\n",main_a +i 3);
			//at.trad = "printf(...,";
			rowbool = token.Row();
			columnbool = token.Column();
			emparejar(Token.TokenType.writeln);
			emparejar(Token.TokenType.pari);

			Atributos op2 = E(at);

			if(op2.tipo == Simbolo.ENTERO)
			{
				at.trad = "printf(\"%d\\n\", ";
			}
			else if(op2.tipo == Simbolo.REAL)
			{
				at.trad = "printf(\"%g\\n\", ";
			}
			else if(op2.tipo == Simbolo.BOOL)
			{
				Error.throwError12(rowbool, columnbool);
			}
			at.trad += op2.trad;
			emparejar(Token.TokenType.pard);
			at.trad += ");\n";
		}
		else
		{			
			tokensEsperados.add(Token.TokenType.begin);
			tokensEsperados.add(Token.TokenType.t_if);
			tokensEsperados.add(Token.TokenType.t_while);
			tokensEsperados.add(Token.TokenType.writeln);
			tokensEsperados.add(Token.TokenType.id);
			errorSintaxis();
		}

		return at;
	}

	private Atributos Instr2()
	{
		Atributos at = new Atributos();
		if(token() == Token.TokenType.endif)
		{
			emparejar(Token.TokenType.endif);
		}
		else if(token() == Token.TokenType.t_else)
		{
			emparejar(Token.TokenType.t_else);
			at.trad = "else\n";
			at.trad += Instr().trad;
			emparejar(Token.TokenType.endif);
		}
		else
		{
			tokensEsperados.add(Token.TokenType.t_else);
			tokensEsperados.add(Token.TokenType.endif);
			errorSintaxis();
		}
		return at;
	}

	private Atributos E(Atributos h)
	{
		Atributos at = new Atributos();
		if(token() == Token.TokenType.nentero ||
				token() == Token.TokenType.nreal ||
				token() == Token.TokenType.pari ||
				token() == Token.TokenType.id)
		{
			Atributos op1 = Expr(h);
			//at.trad = Expr().trad;
			Atributos op2 = E2(op1);
			at.trad = op2.trad;
			at.tipo = op2.tipo;
			at.th1 = op2.trad;
		}
		else
		{
			tokensEsperados.add(Token.TokenType.pari);
			tokensEsperados.add(Token.TokenType.nentero);
			tokensEsperados.add(Token.TokenType.id);
			tokensEsperados.add(Token.TokenType.nreal);
			errorSintaxis();
		}
		return at;
	}

	private Atributos E2(Atributos h)
	{
		Atributos at = new Atributos();
		String operando;
		if(token() == Token.TokenType.relop)
		{
			operando = token.Lexema();
			emparejar(Token.TokenType.relop);
			Atributos op1 = Expr(h);
			//at.trad += op1.trad;
			/*at.th1 = h.th1 + operando + op1.trad;
			at.trad = h.trad + operando + op1.trad;*/

			if(operando.equals("="))
			{
				operando = "==";
			}
			else if(operando.equals("<>"))
			{
				operando = "!=";
			}

			if(h.tipo == Simbolo.REAL && op1.tipo == Simbolo.REAL)
			{
				at.trad = h.th1 + " " + operando + "r " + op1.th1;
			}
			else if(h.tipo == Simbolo.REAL && op1.tipo == Simbolo.ENTERO)
			{
				at.trad = h.th1 + " " + operando + "r itor(" + op1.th1 + ") ";
			}
			else if(h.tipo == Simbolo.ENTERO && op1.tipo == Simbolo.REAL)
			{
				at.trad ="itor(" +  h.th1 + ") " + operando + "r " + op1.th1;
			}
			else if(h.tipo == Simbolo.ENTERO && op1.tipo == Simbolo.ENTERO)
			{
				at.trad = h.th1 + " " + operando + "i " + op1.th1;
			}
			else
			{
				at.trad = h.th1 + " " + operando + "i " + op1.th1;
			}
			at.th1 = at.trad;
			at.tipo = Simbolo.BOOL;
		}
		else if(token() == Token.TokenType.pyc ||
				token() == Token.TokenType.end ||
				token() == Token.TokenType.endif ||
				token() == Token.TokenType.t_else ||
				token() == Token.TokenType.then ||
				token() == Token.TokenType.t_do ||
				token() == Token.TokenType.pard)
		{
			//at.trad = ";\n";
			at.trad = h.trad;
			at.tipo = h.tipo;
			at.th1 = h.th1;
		}
		else
		{
			tokensEsperados.add(Token.TokenType.pard);
			tokensEsperados.add(Token.TokenType.relop);
			tokensEsperados.add(Token.TokenType.pyc);
			tokensEsperados.add(Token.TokenType.end);
			tokensEsperados.add(Token.TokenType.then);
			tokensEsperados.add(Token.TokenType.t_else);
			tokensEsperados.add(Token.TokenType.endif);
			tokensEsperados.add(Token.TokenType.t_do);
			errorSintaxis();
		}

		return at;
	}

	private Atributos Expr(Atributos h)
	{
		Atributos at = new Atributos();
		if(token() == Token.TokenType.nentero ||
				token() == Token.TokenType.nreal ||
				token() == Token.TokenType.pari ||
				token() == Token.TokenType.id)
		{
			Atributos op1 = Term(h);
			//at.trad = op1.trad;
			Atributos op2 = Expr2(op1);
			at.trad = op2.trad;
			at.tipo = op2.tipo;
			at.th1 = op2.trad;
		}
		else
		{
			tokensEsperados.add(Token.TokenType.pari);
			tokensEsperados.add(Token.TokenType.nentero);
			tokensEsperados.add(Token.TokenType.id);
			tokensEsperados.add(Token.TokenType.nreal);
			errorSintaxis();
		}

		return at;
	}

	private Atributos Expr2(Atributos h)
	{
		Atributos at = new Atributos();
		String operando;
		if(token() == Token.TokenType.addop)
		{
			operando = token.Lexema();
			emparejar(Token.TokenType.addop);

			//at.trad += Term().trad;
			//at.trad += Expr2().trad;
			Atributos op1 = Term(h);

			if(h.tipo == Simbolo.REAL && op1.tipo == Simbolo.REAL)
			{
				at.trad = h.th1 + " " + operando + "r " + op1.th1;
				at.tipo = Simbolo.REAL;
			}
			else if(h.tipo == Simbolo.REAL && op1.tipo == Simbolo.ENTERO)
			{
				at.trad = h.th1 + " " + operando + "r itor(" + op1.th1 + ") ";
				at.tipo = Simbolo.REAL;
			}
			else if(h.tipo == Simbolo.ENTERO && op1.tipo == Simbolo.REAL)
			{
				at.trad ="itor(" +  h.th1 + ") " + operando + "r " + op1.th1;
				at.tipo = Simbolo.REAL;
			}
			else if(h.tipo == Simbolo.ENTERO && op1.tipo == Simbolo.ENTERO)
			{
				at.trad = h.th1 + " " + operando + "i " + op1.th1;
				at.tipo = Simbolo.ENTERO;
			}
			else
			{
				at.trad = h.th1 + " " + operando + "i " + op1.th1;
			}
			at.th1 = at.trad;

			Atributos op2 = Expr2(at);
			at.trad = op2.trad;
			at.th1 = op2.trad;
			at.tipo = op2.tipo;
		}
		else if(token() == Token.TokenType.relop ||
				token() == Token.TokenType.pyc ||
				token() == Token.TokenType.end ||
				token() == Token.TokenType.endif ||
				token() == Token.TokenType.t_else ||
				token() == Token.TokenType.then ||
				token() == Token.TokenType.t_do ||
				token() == Token.TokenType.pard)
		{
			at.th1 = h.th1;
			at.tipo = h.tipo;
			at.trad = h.trad;
		}
		else
		{
			tokensEsperados.add(Token.TokenType.pard);
			tokensEsperados.add(Token.TokenType.addop);
			tokensEsperados.add(Token.TokenType.relop);
			tokensEsperados.add(Token.TokenType.pyc);
			tokensEsperados.add(Token.TokenType.end);
			tokensEsperados.add(Token.TokenType.then);
			tokensEsperados.add(Token.TokenType.t_else);
			tokensEsperados.add(Token.TokenType.endif);
			tokensEsperados.add(Token.TokenType.t_do);
			errorSintaxis();
		}

		return at;
	}

	private Atributos Term(Atributos h)
	{
		Atributos at = new Atributos();

		if(token() == Token.TokenType.nentero ||
				token() == Token.TokenType.nreal ||
				token() == Token.TokenType.pari ||
				token() == Token.TokenType.id)
		{
			Atributos op1 = Factor(h);
			//at.trad = Factor().trad;//
			//at.trad = op1.trad;
			Atributos op2 = Term2(op1);
			at.trad = op2.trad;
			at.th1 = op2.th1;
			at.tipo = op2.tipo;
		}
		else
		{
			tokensEsperados.add(Token.TokenType.pari);
			tokensEsperados.add(Token.TokenType.nentero);
			tokensEsperados.add(Token.TokenType.id);
			tokensEsperados.add(Token.TokenType.nreal);
			errorSintaxis();
		}

		return at;
	}

	private Atributos Term2(Atributos h)
	{
		Atributos at = new Atributos();
		String operando;
		int row, column;

		if(token() == Token.TokenType.mulop)
		{
			operando = token.Lexema();
			row = token.Row();
			column = token.Column();
			emparejar(Token.TokenType.mulop);

			//at.trad += Factor(h).trad;
			//at.trad += Term2(h).trad;
			Atributos op1 = Factor(h);

			if(operando.equals("div"))
			{
				operando = "/";

				if(h.tipo == Simbolo.ENTERO && op1.tipo == Simbolo.ENTERO)
				{
					at.trad = h.th1 + " " + operando + "i " + op1.th1;
					at.tipo = Simbolo.ENTERO;
				}
				else
				{
					Error.throwError11(row, column);
				}
			}
			else
			{
				if(h.tipo == Simbolo.REAL && op1.tipo == Simbolo.REAL)
				{
					at.trad = h.th1 + " " + operando + "r " + op1.th1;
					at.tipo = Simbolo.REAL;
				}
				else if(h.tipo == Simbolo.REAL && op1.tipo == Simbolo.ENTERO)
				{
					at.trad = h.th1 + " " + operando + "r itor(" + op1.th1 + ")";
					at.tipo = Simbolo.REAL;
				}
				else if(h.tipo == Simbolo.ENTERO && op1.tipo == Simbolo.REAL)
				{
					at.trad ="itor(" +  h.th1 + ") " + operando + "r " + op1.th1;
					at.tipo = Simbolo.REAL;
				}
				else if(h.tipo == Simbolo.ENTERO && op1.tipo == Simbolo.ENTERO)
				{
					if(operando.equals("/"))
					{
						at.trad ="itor(" +  h.th1 + ") " + operando + "r itor(" + op1.th1 + ")";
						at.tipo = Simbolo.REAL;
					}
					else
					{
						at.trad = h.th1 + " " + operando + "i " + op1.th1;
						at.tipo = Simbolo.ENTERO;
					}
				}
				else
				{
					at.trad = h.th1 + " " + operando + "i " + op1.th1;
				}
			}
			at.th1 = at.trad;

			Atributos op2 = Term2(at);
			at.trad = op2.trad;
			at.tipo = op2.tipo;
			at.th1 = op2.trad;

			/*
			 * Atributos op1 = Term(h);

			if(h.tipo == Simbolo.REAL && op1.tipo == Simbolo.REAL)
			{
				at.trad = h.th1 + " " + operando + "r " + op1.th1;
			}
			else
			{
				at.trad = h.th1 + " " + operando + "i " + op1.th1;
			}
			Atributos op2 = Expr2(op1);
			at.trad += op2.trad;
			at.tipo = op2.tipo;
			 */
		}
		else if(token() == Token.TokenType.addop ||
				token() == Token.TokenType.relop ||
				token() == Token.TokenType.pyc ||
				token() == Token.TokenType.end ||
				token() == Token.TokenType.endif ||
				token() == Token.TokenType.t_else ||
				token() == Token.TokenType.then ||
				token() == Token.TokenType.t_do ||
				token() == Token.TokenType.pard)
		{
			//System.out.println("TH1: " + h.th1);
			at.th1 = h.th1;
			at.tipo = h.tipo;
			at.trad = h.trad;
		}
		else
		{
			tokensEsperados.add(Token.TokenType.pard);
			tokensEsperados.add(Token.TokenType.mulop);
			tokensEsperados.add(Token.TokenType.addop);
			tokensEsperados.add(Token.TokenType.relop);
			tokensEsperados.add(Token.TokenType.pyc);
			tokensEsperados.add(Token.TokenType.end);
			tokensEsperados.add(Token.TokenType.then);
			tokensEsperados.add(Token.TokenType.t_else);
			tokensEsperados.add(Token.TokenType.endif);
			tokensEsperados.add(Token.TokenType.t_do);
			errorSintaxis();
		}

		return at;
	}

	private Atributos Factor(Atributos h)
	{
		Atributos at = new Atributos();
		if(token() == Token.TokenType.id)
		{
			if(ts.busca(token.Lexema()) != null)
			{
				if(ts.busca(token.Lexema()).tipoSimbolo == Simbolo.VAR)
				{

					at.tipo = ts.busca(token.Lexema()).tipo;
					at.trad = ts.busca(token.Lexema()).traduccion;
					at.th1 = ts.busca(token.Lexema()).traduccion;
				}
				else
				{
					Error.throwError7(token.Row(), token.Column(), token.Lexema());
				}
			}
			else
			{
				//ERROR
				Error.throwError6(token.Row(), token.Column(), token.Lexema());
			}
			//at.trad = token.Lexema();
			emparejar(Token.TokenType.id);
		}
		else if(token() == Token.TokenType.nentero)
		{
			/*at.trad = "itor(" + token.Lexema() + ")";
			at.th1 = "itor(" + token.Lexema()+ ")";
			at.tipo = Simbolo.REAL;*/

			at.trad = token.Lexema();
			at.th1 = token.Lexema();
			at.tipo = Simbolo.ENTERO;

			emparejar(Token.TokenType.nentero);
		}
		else if(token() == Token.TokenType.nreal)
		{
			at.trad = token.Lexema();
			at.th1 = token.Lexema();
			at.tipo = Simbolo.REAL;
			emparejar(Token.TokenType.nreal);
		}
		else if(token() == Token.TokenType.pari)
		{
			/*at.trad = "( ";
			emparejar(Token.TokenType.pari);
			at.trad += Expr().trad;
			at.trad += " )";*/

			at.th1 = "( ";
			at.trad = "( ";
			emparejar(Token.TokenType.pari);
			Atributos op2 = Expr(h);
			at.th1 += op2.trad;
			at.trad += op2.trad;
			at.tipo = op2.tipo;
			at.th1 += " )";
			at.trad += " )";
			emparejar(Token.TokenType.pard);
		}
		else
		{
			tokensEsperados.add(Token.TokenType.pari);
			tokensEsperados.add(Token.TokenType.nentero);
			tokensEsperados.add(Token.TokenType.id);
			tokensEsperados.add(Token.TokenType.nreal);
			errorSintaxis();
		}

		return at;
	}
}

class Atributos
{
	public String trad;
	public String th1;
	public String th2;
	public int tipo;
	//public int row;
	//public int column;

	public Atributos()
	{
		trad = "";
		th1 = "";
		th2 = "";
		tipo = 0;
		/*row = 0;
		column = 0;*/
	}

	public Atributos(String _trad)
	{
		trad = _trad;
		th1 = "";
		th2 = "";
		tipo = 0;
		/*row = 0;
		column = 0;*/
	}

	public Atributos(String _trad, String th1_)
	{
		trad = _trad;
		th1 = th1_;
		th2 = "";
		tipo = 0;
		/*row = 0;
		column = 0;*/
	}

}
