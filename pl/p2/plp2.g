grammar plp2;

@header {
import java.lang.String;
}

@members {
    /* Miembros de la clase del analizador sintáctico */
    TablaSimbolos ts = new TablaSimbolos();
	ArrayList<Simbolo> simbolos;
	int tipoSimbolo;
	public void emitErrorMessage(String msg) {
    System.err.println(msg);
    System.exit(1);
  }
}

@rulecatch { 
  catch (RecognitionException re) { 
    reportError(re); 
    System.exit(1); 
  } 
}
 
@lexer::members {
  public void emitErrorMessage(String msg) {
    System.err.println(msg);
    System.exit(1);
  }
}

/* Analizador sintactico: */
//S → program id pyc VSp  Bloque
s returns[String s]:  {Atributos at = new Atributos();} PROGRAM ID {at.trad = "// program " + $ID.text + "\n";}
					PYC
					aux = vsp[null] {at.trad += aux.trad;}
					aux = bloque[true] {at.trad += aux.trad;}
					EOF
					{$s = at.trad;};

//VSp → (UnVsp)+
vsp[Atributos h] returns [Atributos at]
@init{
   at = new Atributos();
}:
	(
		aux = unvsp[$h]
		{at.trad += aux.trad;
		 at.th1 = aux.th1;
		 at.th2 = aux.th2;
		 at.tipo = aux.tipo;
		//h = at;
		}
	)+
;

//UnVsp → function id dosp Tipo pyc VSp Bloque pyc
//UnVsp → var (V)+

unvsp[Atributos h] returns [Atributos at]
@init{
   at = new Atributos();
   String auxFunc = "", auxTrad = "";
}:
	//function
		FUNCTION
		ID
		{
		if(!ts.existe($ID.text))
			{
				if($h == null || $h.th2 == "")
				{
					auxFunc = auxTrad = $ID.text;
					at.trad += " " + $ID.text + "()";
					at.th2 += $ID.text + "_";
				}
				else
				{
					auxFunc = $ID.text;
					auxTrad = $h.th2 + $ID.text;
					at.trad += " " + $h.th2 + $ID.text + "()";
					at.th2 += $h.th2 + $ID.text  + "_";
				}
			}
			else
			{
				Error.throwError5($ID.line, $ID.pos + 1, $ID.text);
			}
			
		}
		DOSP
		aux = tipo
		
		{	at.th1 = aux.trad;
			at.trad = at.th1 + " " + at.trad + " ";
			ts.nuevoSimbolo(auxFunc, auxTrad,Simbolo.METODO, aux.tipo);

			ts = new TablaSimbolos(ts);
		}
		
		PYC
		aux = vsp[$at]
		{at.trad = aux.trad + at.trad;}
		aux = bloque[false]
		{at.trad += aux.trad;}
		PYC
		{ts = ts.pop();}
		
		
	//var
	|
		VAR
		(aux = v[h] {at.trad += aux.trad; /*h = at;*/} )+
		
	;
	
	
//V → id (coma id)* dosp Tipo pyc
v[Atributos h] returns [Atributos at]
@init{
   at = new Atributos();
   simbolos = new ArrayList<Simbolo>();
   String auxVar = "", auxTrad = "";
   
}:

	id1 = ID
	{
	if(h == null || h.th2 == "")
	{
		auxVar = $id1.text;
		auxTrad = "main_" + $id1.text;
	}
	else
	{
		auxVar = $id1.text;
		auxTrad = h.th2 + $id1.text;
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
		Error.throwError5($id1.line, $id1.pos + 1, $id1.text);
	}

	at.trad = auxTrad;
	}
	
	(
		COMA
		{at.trad += ", ";}
		id2 = ID
		{
			if(h == null || h.th2 == "")
			{
				auxVar = $id2.text;
				auxTrad = "main_" + $id2.text;
			}
			else
			{
				auxVar = $id2.text;
				auxTrad = h.th2 + $id2.text;
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
				Error.throwError5($id2.line, $id2.pos + 1, $id2.text);
			}
		
			at.trad += auxTrad;
		}
	)*
	//(coma id)*
	//at.trad += LI(h).trad;
	//emparejar(Token.TokenType.dosp);
	DOSP
	eltipo = tipo
	{
	tipoSimbolo = eltipo.tipo;
	at.th1 = eltipo.trad;
	at.tipo = eltipo.tipo;
	at.trad = at.th1  + " " + at.trad + ";\n";
	
	for(Simbolo s: simbolos)
	{
		s.tipo = tipoSimbolo;
	}
	}
	PYC
;

bloque[boolean main] returns [Atributos at]
@init{
   at = new Atributos();
}:
	BEGIN
	aux = sinstr
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
		at.trad += aux.trad;
		at.trad += "}\n";
	}
	END
;

tipo returns [Atributos at]
@init{
   at = new Atributos();
}:
	INTEGER {at.trad = "int";
			at.tipo = Simbolo.ENTERO;}
	| REAL {at.trad = "double";
			at.tipo = Simbolo.REAL;}
;

//SInstr → Instr (pyc Instr)* 				
sinstr returns [Atributos at]
@init{
   at = new Atributos();
   String auxFunc = "", auxTrad = "";
}:

	aux = instr
	{
		at.trad = aux.trad;
	}
	(
	PYC
	aux = instr
	{
		at.trad += aux.trad;
	}
	)*
;


instr returns [Atributos at]
@init{
   at = new Atributos();
   String tokenreal = "";
   int rowreal = 0, columnreal = 0, rowbool = 0, columnbool = 0;
}:
	//Instr → Bloque
	//BEGIN
	aux = bloque[false]
	{at.trad = aux.trad;}
	|
	//Instr → id asig E
	ID
	{
		if(ts.busca($ID.text) != null)
			{
				if(ts.busca($ID.text).tipoSimbolo == Simbolo.VAR)
				{
					tokenreal = ts.busca($ID.text).nombre;
					at.trad = ts.busca($ID.text).traduccion;
					at.tipo = ts.busca($ID.text).tipo;
					rowreal = $ID.line;
					columnreal = $ID.pos + 1;
				}
				else
				{
					Error.throwError7($ID.line, $ID.pos + 1, $ID.text);
				}
			}
			else
			{
				//ERROR
				Error.throwError6($ID.line, $ID.pos + 1, $ID.text);
			}
	}
	
			ASIG
	{
			rowbool = $ASIG.line;
			columnbool = $ASIG.pos + 1;
	}
			
			 op1 = e[$at]
	 {
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
	|
	//Instr → if E then Instr (else Instr)? endif
	IF
	{
	at.trad = "if ( ";
	rowbool = $IF.line;
	columnbool = $IF.pos + 1;
	}

	op2 = e[$at]
	{
		if(op2.tipo != Simbolo.BOOL)
		{
			Error.throwError10(rowbool, columnbool, "if");
		}
		at.trad += op2.trad + " )\n";
	}
	THEN
	aux = instr
	{at.trad += aux.trad;}
	(
	ELSE
	{at.trad += "else\n";}
	aux = instr
	{at.trad += aux.trad;}
		
	)?
	ENDIF
	|
	//Instr → while E do Instr
	WHILE
	//aux = e[$at]
	//DO
	{
	at.trad = "while ( ";
	rowbool = $WHILE.line;
	columnbool = $WHILE.pos + 1;
	}

	op2 = e[$at]
	{
	if(op2.tipo != Simbolo.BOOL)
	{
		Error.throwError10(rowbool, columnbool, "while");	
	}

	at.trad += op2.trad + " )\n";
	}
	DO
	aux = instr
	{at.trad += aux.trad;}
	|
	//Instr → writeln pari E pard
	WRITELN
	{
	rowbool = $WRITELN.line;
	columnbool = $WRITELN.pos + 1;
	}
	PARI
	op2 = e[$at]
{
	if(op2.tipo == Simbolo.ENTERO)
	{
		at.trad = "printf(\"\%d\\n\", ";
	}
	else if(op2.tipo == Simbolo.REAL)
	{
		at.trad = "printf(\"\%g\\n\", ";
	}
	else if(op2.tipo == Simbolo.BOOL)
	{
		Error.throwError12(rowbool, columnbool);
	}
	at.trad += op2.trad;
}
	PARD
	{
	at.trad += ");\n";
	}
;

//E → Expr (relop Expr)?
e[Atributos h] returns [Atributos at]
@init{
   at = new Atributos();
   String operando = "";
}:
	op1 = expr[$h]
	{
		at.trad = op1.trad;
		at.tipo = op1.tipo;
		at.th1 = op1.trad;
	}
	(
		RELOP
		op2 = expr[op1]
		{
			
		operando = $RELOP.text;
		if(operando.equals("="))
		{
			operando = "==";
		}
		else if(operando.equals("<>"))
		{
			operando = "!=";
		}

		if(op1.tipo == Simbolo.REAL && op2.tipo == Simbolo.REAL)
		{
			at.trad = op1.th1 + " " + operando + "r " + op2.th1;
		}
		else if(op1.tipo == Simbolo.REAL && op2.tipo == Simbolo.ENTERO)
		{
			at.trad = op1.th1 + " " + operando + "r itor(" + op2.th1 + ") ";
		}
		else if(op1.tipo == Simbolo.ENTERO && op2.tipo == Simbolo.REAL)
		{
			at.trad ="itor(" +  op1.th1 + ") " + operando + "r " + op2.th1;
		}
		else if(op1.tipo == Simbolo.ENTERO && op2.tipo == Simbolo.ENTERO)
		{
			at.trad = op1.th1 + " " + operando + "i " + op2.th1;
		}
		else
		{
			at.trad = op1.th1 + " " + operando + "i " + op2.th1;
		}
		at.th1 = at.trad;
		at.tipo = Simbolo.BOOL;
		op1 = at;
		}
		/*op1 = expr[op1]
		{
		at.trad = op2.trad;
		at.tipo = op2.tipo;
		at.th1 = op2.trad;
		}*/
	)?
;

//Expr → Term (addop Term)*
expr[Atributos h] returns [Atributos at]
@init{
   at = new Atributos();
   String operando = "";
}:
//TODO e por term
	op1 = term[$h]
	{
		at.trad = op1.trad;
		at.tipo = op1.tipo;
		at.th1 = op1.trad;
	}
	(
	ADDOP
	/*op2 = term[op1]
	{
	at.trad = op2.trad;
	at.tipo = op2.tipo;
	at.th1 = op2.trad;
	}*/
	
	 {
		operando = $ADDOP.text;

		//at.trad += Term().trad;
		//at.trad += Expr2().trad;
	 }
		op2 = term[op1]
	{
		if(op1.tipo == Simbolo.REAL && op2.tipo == Simbolo.REAL)
		{
			at.trad = op1.th1 + " " + operando + "r " + op2.th1;
			at.tipo = Simbolo.REAL;
		}
		else if(op1.tipo == Simbolo.REAL && op2.tipo == Simbolo.ENTERO)
		{
			at.trad = op1.th1 + " " + operando + "r itor(" + op2.th1 + ") ";
			at.tipo = Simbolo.REAL;
		}
		else if(op1.tipo == Simbolo.ENTERO && op2.tipo == Simbolo.REAL)
		{
			at.trad ="itor(" +  op1.th1 + ") " + operando + "r " + op2.th1;
			at.tipo = Simbolo.REAL;
		}
		else if(op1.tipo == Simbolo.ENTERO && op2.tipo == Simbolo.ENTERO)
		{
			at.trad = op1.th1 + " " + operando + "i " + op2.th1;
			at.tipo = Simbolo.ENTERO;
		}
		else
		{
			at.trad = op1.th1 + " " + operando + "i " + op2.th1;
		}
		/*at.th1 += at.trad;
		op1.trad = at.trad;
		op1.tipo = op2.tipo;
		op1.th1 = op2.th1;*/
		op1 = at;
		op1.th1 = at.trad;
	 }
	)*
;

//Term → Factor (mulop Factor)*
term[Atributos h] returns [Atributos at]
@init{
   at = new Atributos();
   String operando = "";
   int row = 0, column = 0;
}:
	//{at.trad = "NADA";}
	op1 = factor[$h]
	{
		at.trad = op1.trad;
		at.th1 = op1.th1;
		at.tipo = op1.tipo;
	} 
	
	(
	MULOP
	{
			operando = $MULOP.text;
			row = $MULOP.line;
			column = $MULOP.pos + 1;
	}
	
	op2 = factor[op1]
	{
			if(operando.equals("div"))
			{
				operando = "/";

				if(op1.tipo == Simbolo.ENTERO && op2.tipo == Simbolo.ENTERO)
				{
					at.trad = op1.th1 + " " + operando + "i " + op2.th1;
					at.tipo = Simbolo.ENTERO;
				}
				else
				{
					Error.throwError11(row, column);
				}
			}
			else
			{
				if(op1.tipo == Simbolo.REAL && op2.tipo == Simbolo.REAL)
				{
					at.trad = op1.th1 + " " + operando + "r " + op2.th1;
					at.tipo = Simbolo.REAL;
				}
				else if(op1.tipo == Simbolo.REAL && op2.tipo == Simbolo.ENTERO)
				{
					at.trad = op1.th1 + " " + operando + "r itor(" + op2.th1 + ")";
					at.tipo = Simbolo.REAL;
				}
				else if(op1.tipo == Simbolo.ENTERO && op2.tipo == Simbolo.REAL)
				{
					at.trad ="itor(" +  op1.th1 + ") " + operando + "r " + op2.th1;
					at.tipo = Simbolo.REAL;
				}
				else if(op1.tipo == Simbolo.ENTERO && op2.tipo == Simbolo.ENTERO)
				{
					if(operando.equals("/"))
					{
						at.trad ="itor(" +  op1.th1 + ") " + operando + "r itor(" + op2.th1 + ")";
						at.tipo = Simbolo.REAL;
					}
					else
					{
						at.trad = op1.th1 + " " + operando + "i " + op2.th1;
						at.tipo = Simbolo.ENTERO;
					}
				}
				else
				{
					at.trad = op1.th1 + " " + operando + "i " + op2.th1;
				}
			}
			at.th1 = at.trad;
			op1 = at;
	}
	)*
;

//Factor → id
//Factor → nentero
//Factor → nreal
//Factor → pari Expr pard
factor[Atributos h] returns [Atributos at]
@init{
   at = new Atributos();
}:
	ID
	{
	if(ts.busca($ID.text) != null)
		{
			if(ts.busca($ID.text).tipoSimbolo == Simbolo.VAR)
			{

				at.tipo = ts.busca($ID.text).tipo;
				at.trad = ts.busca($ID.text).traduccion;
				at.th1 = ts.busca($ID.text).traduccion;
			}
			else
			{
				Error.throwError7($ID.line, $ID.pos + 1, $ID.text);
			}
		}
		else
		{
			//ERROR
			Error.throwError6($ID.line, $ID.pos + 1, $ID.text);
		}
	}
	|
	NENTERO
	{
		at.trad = $NENTERO.text;
		at.th1 = $NENTERO.text;
		at.tipo = Simbolo.ENTERO;
	}
	|
	NREAL
	{
		at.trad = $NREAL.text;
		at.th1 = $NREAL.text;
		at.tipo = Simbolo.REAL;
	}
	|
	PARI
	{
	at.th1 = "( ";
	at.trad = "( ";
	}
	
	op2 = expr[$h]
	{
	at.th1 += op2.trad;
	at.trad += op2.trad;
	at.tipo = op2.tipo;
	at.th1 += " )";
	at.trad += " )";
	}
	PARD
;

/*Especificacion Lexica*/
PARI 	:	'(';
PARD 	:	')';
MULOP 	:	'*' | '/' | 'div';
ADDOP 	:	'+' | '-';
RELOP 	:	'<' | '>' | '<=' | '>=' | '=' | '<>';
PYC 	:	';';
DOSP	:	':';
COMA	: 	',';
ASIG	:	':=';
VAR		:	'var';
REAL	:	'real';
INTEGER	:	'integer';
PROGRAM	:	'program';
BEGIN	:	'begin';
END		:	'end';
FUNCTION:	'function';
IF		:	'if';
THEN	:	'then';
ELSE	:	'else';
ENDIF	:	'endif';
WHILE	:	'while';
DO		:	'do';
WRITELN	:	'writeln';
NENTERO	:	('0'..'9')+;
ID 		:	('a'..'z'|'A'..'Z')('a'..'z'|'A'..'Z'|'0'..'9')*;
NREAL	:	('0'..'9')+'.'('0'..'9')+;
SPACE	:	(' ' | '\n' | '\t' | '\r')+ {skip();};
COMENT	:	'(*' .* '*)' {skip();};

