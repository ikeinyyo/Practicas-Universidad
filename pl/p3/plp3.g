grammar plp3;

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
s returns[String s] :

	miclase = clase
	{
	s = ".assembly extern mscorlib{}\n";
	s += ".assembly 'pl'{}\n";
	}
	
	{s += miclase.trad;}
;

clase returns [Atributos at]
@init{
   at = new Atributos();
} :
	CLASS
	SINGLE
	{at.trad = ".class 'Single' extends [mscorlib]System.Object // Definicion de la clase 'Single'\n";}
	LLAVEI
	{
		at.trad += "{\n";
		//at.trad += "/*Constructor de la clase Single*/\n";
		//at.trad += ".method public specialname rtspecialname instance void .ctor() cil managed\n{\n";
	}
	aux = metodo
	LLAVED
	{
		//at.trad += "} /*Fin del constructor*/\n";
		at.trad += aux.decl + aux.trad + "\n} /*Fin de la clase*/";
	}
;

metodo returns [Atributos at]
@init{
   at = new Atributos();
} :
	PUBLIC
	STATIC
	VOID
	MAIN
	PARI
	PARD
	
	{
		at.decl = ".method static public void main () cil managed {\n";
		at.trad = "";
	}
	aux = bloque["",""]
	{
		if(aux.decl != null)
		{
			at.decl += ".locals (" + aux.decl + ")\n";
		}
		at.decl += ".entrypoint\n";
		at.decl += ".maxstack 99999\n";
		at.trad += aux.trad;
		
	}
	
	{at.trad += "ret\n}";}
;

tipoSimple returns [Atributos at]
@init{
   at = new Atributos();
   at.tipo = new Tipo();
   at.tipo.tamano = 1;
} :

	INT
	{
		at.tipo.tipo = Tipo.ENTERO;
		at.th = $INT.line + "," + ($INT.pos + 1);
	}
	|
	DOUBLE
	{
		at.tipo.tipo = Tipo.REAL;
		at.th = $DOUBLE.line + "," + ($DOUBLE.pos + 1);
	}
	|
	BOOL
	{
		at.tipo.tipo = Tipo.BOOLEAN;
		at.th = $BOOL.line + "," + ($BOOL.pos + 1);
	}
;

decl returns [Atributos at]
@init{
   at = new Atributos();
   at.trad = "";
} :
	tipo = tipoSimple
	var = varid[tipo]
	{
		if(var.tipo.tipo == Tipo.ENTERO || var.tipo.tipo == Tipo.BOOLEAN)
		{
			at.decl = "int32";
			
		}
		else if(var.tipo.tipo == Tipo.REAL)
		{
			at.decl = "float64";
		}
		else if(var.tipo.tipo == Tipo.ARRAY)
		{
			if(tipo.tipo.tipo == Tipo.ENTERO || tipo.tipo.tipo == Tipo.BOOLEAN)
			{
				at.decl = "int32[]";
			}
			else if(tipo.tipo.tipo == Tipo.REAL)
			{
				at.decl = "float64[]";
			}
		}
		
		ts.nuevoSimbolo(var.trad, var.tipo, ts.getPos(), var.tipo.tamano);
		//System.out.println(var.tipo.mostrar());
		//System.out.println("TS: " + ts);
	}
	(COMA
	var = varid[tipo]
	{
		if(var.tipo.tipo == Tipo.ENTERO || var.tipo.tipo == Tipo.BOOLEAN)
		{
			at.decl += ", int32";
		}
		else if(var.tipo.tipo == Tipo.REAL)
		{
			at.decl += ", float64";
		}
		else if(var.tipo.tipo == Tipo.ARRAY)
		{
			if(tipo.tipo.tipo == Tipo.ENTERO || tipo.tipo.tipo == Tipo.BOOLEAN)
			{
				at.decl += ", int32[]";
			}
			else if(tipo.tipo.tipo == Tipo.REAL)
			{
				at.decl += ", float64[]";
			}
		}
		ts.nuevoSimbolo(var.trad, var.tipo, ts.getPos(), var.tipo.tamano);
		
		
		//System.out.println(var.tipo.mostrar());
		//System.out.println("TS: " + ts);
	}
	)*
	PYC
;

varid[Atributos h] returns [Atributos at]
@init{
   at = new Atributos();
   at.tipo = new Tipo();
   at.tipo = h.tipo;
   at.tipo.tipo = h.tipo.tipo;
} :
	ID
	{
		at.trad = $ID.text;
		
		if(ts.existe($ID.text))
		{
			//Error1
			Error.throwError1($ID.line, $ID.pos, $ID.text);
		}
	}
	(
	CORI 
	{
		Tipo aux = new Tipo();
		aux.tipo = Tipo.ARRAY;
		aux.tamano = 0;
		aux.tipoBase = at.tipo;
		
		at.tipo = aux;
	}
	(
	COMA
	{
		aux = new Tipo();
		aux.tipo = Tipo.ARRAY;
		aux.tamano = 0;
		aux.tipoBase = at.tipo;
		
		at.tipo = aux;
	}
	)* 
	CORD
	)?
;

declins[String etiqBreak, String etiqCont] returns [Atributos at]
@init{
   at = new Atributos();
   at.trad = "";
} :
	(
	aux = instr[etiqBreak, etiqCont, true]
	{
		if(aux.decl != null)
		{
			if(at.decl == null)
			{
				at.decl = aux.decl;
			}
			else
			{
				at.decl += "," + aux.decl;
			} 
		}
		
		at.trad  += aux.trad;
	}
	| 
	aux = decl
	{
		if(aux.decl != null)
		{
			if(at.decl == null)
			{
				at.decl = aux.decl;
			}
			else
			{
				at.decl += "," + aux.decl;
			} 
		}
		/*
		if(at.decl == null)
		{
			at.decl = aux.decl;
		}
		else
		{
			at.decl += "," + aux.decl;
		}*/
		at.trad  += aux.trad;
	}
	)*
;

bloque[String etiqBreak, String etiqCont] returns [Atributos at]
@init{
   at = new Atributos();
} :
	LLAVEI 
	aux = declins[etiqBreak, etiqCont]
	{
		at.decl = aux.decl;
		at.trad = aux.trad;
	} 
	LLAVED
;

instr[String etiqBreak, String etiqCont, boolean ambito] returns [Atributos at]
@init{
   at = new Atributos();
   //at.decl = "";
   /*if(!etiq.equals(""))
   {
   	at.trad = etiq + ":\n";
   	etiq = "";
   }*/
   at.trad = "";
} :
	
	{
		if(ambito)
		{
			ts = new TablaSimbolos(ts);
		}
	}
	aux = bloque[etiqBreak, etiqCont]
	{
		
		at.decl = aux.decl;
		at.trad += aux.trad;
		
		if(ambito)
		{
			ts = ts.pop();
		}
	}
	
	|
	
	IF
	PARI
	aux = expr
	{
		//ts = new TablaSimbolos(ts);
		
		
		at.trad += aux.trad;
		String eel = TablaSimbolos.getEtiqueta();
		String efin = TablaSimbolos.getEtiqueta();
		at.trad += "ldc.i4 0\nbeq " + eel + "\n";
		
		if(aux.tipo.tipo != Tipo.BOOLEAN)
		{
			Error.throwError5($IF.line, $IF.pos, $IF.text);
		}
	}
	PARD
	aux = instr[etiqBreak, etiqCont, true]
	{
		at.decl = aux.decl;
		at.trad += aux.trad;
		at.trad += "br " + efin + "\n" + eel + ":\n";
		
		
		//ts = ts.pop();
	}
	(
	ELSE 
	{
		//ts = new TablaSimbolos(ts);
	}
	aux = instr[etiqBreak, etiqCont, true]
	{
		if(aux.decl != null)
		{
			if(at.decl == null)
			{
				at.decl = aux.decl;
			}
			else
			{
				at.decl += "," + aux.decl;
			}
		}
		at.trad += aux.trad;
		
		
		//ts = ts.pop();
	}
	)?
	{
		at.trad += efin + ":\n";
	}
	
	|
	
	WHILE 
	PARI 
	aux = expr
	{
		String econd = TablaSimbolos.getEtiqueta();
		String efin = TablaSimbolos.getEtiqueta();
		at.trad += econd + ":\n";
		at.trad += aux.trad;
		at.trad += "ldc.i4 0\nbeq " + efin + "\n"; 
		etiqBreak = efin;
		etiqCont = econd;
		
		if(aux.tipo.tipo != Tipo.BOOLEAN)
		{
			Error.throwError5($WHILE.line, $WHILE.pos, $WHILE.text);
		}
	}
	PARD 
	{
		//ts = new TablaSimbolos(ts);
	}
	aux = instr[etiqBreak, etiqCont, true]
	{
		at.decl = aux.decl;
		at.trad += aux.trad;
		at.trad += "br " + econd + "\n";
		at.trad += efin + ":\n";
		
		
		//ts = ts.pop();
	}
	
	|
	
	FOREACH
	PARI
	{
		ts = new TablaSimbolos(ts);
	}
	VAR
	idvar = ID
	{
		if(ts.existe($idvar.text))
		{
			Error.throwError1($idvar.line, $idvar.pos, $idvar.text);
		}
		
		
	}
	IN
	idarray = ID
	{
		Simbolo a = ts.busca($idarray.text);
		Tipo eltipo = new Tipo();
		
		if(a != null)
		{
			eltipo.tipo = a.tipo.getTipoBase();
		}
		else
		{
			Error.throwError2($idarray.line, $idarray.pos, $idarray.text);
		}
		
		if(a.tipo.tipo != Tipo.ARRAY)
		{
			Error.throwError11($idarray.line, $idarray.pos, $idarray.text);
		}
		
		Simbolo s = new Simbolo($idvar.text, eltipo, TablaSimbolos.getPos());
		s.indice = true;
		ts.nuevoSimbolo(s);
		
		if(eltipo.tipo == Tipo.ENTERO || eltipo.tipo == Tipo.BOOLEAN)
		{
			at.decl = "int32";
		}
		else if(eltipo.tipo == Tipo.REAL)
		{
			at.decl = "float64";
		}
		String ffor = TablaSimbolos.getEtiqueta();
		String ifor = TablaSimbolos.getEtiqueta();
		String cfor = TablaSimbolos.getEtiqueta();
		int total = 1;
		Tipo actual = a.tipo;
		
		while(actual != null)
		{
			total *= actual.tamano;
			actual = actual.tipoBase;
		}	
		
		int iter = TablaSimbolos.getPos();
		at.decl += ", int32";
		at.trad += "ldc.i4 0\n";
		at.trad += "stloc " + iter + "\n";
		//Comprobacion
		at.trad += ifor + ":\n";
		at.trad += "ldc.i4 " + total + "\n";
		at.trad += "ldloc " + iter + "\n";
		at.trad += "cgt\n";
		at.trad += "ldc.i4 0\n";
		at.trad += "beq " + ffor + "\n";
		
		//Cargo elemento en la variable
		at.trad += "ldloc " + a.posicion + "\n";
		at.trad += "ldloc " + iter + "\n";
		
		if(eltipo.tipo == Tipo.ENTERO || eltipo.tipo == Tipo.BOOLEAN)
		{
			at.trad += "ldelem.i4\n";
		}
		else if(eltipo.tipo == Tipo.REAL)
		{
			at.trad += "ldelem.r8\n";
		}
		
		at.trad += "stloc " + s.posicion + "\n";
		
		etiqBreak = ffor;
		etiqCont = cfor;
	}
	PARD
	aux = instr[etiqBreak, etiqCont, false]
	{
		if(aux.decl != null)
		{
			if(at.decl != null)
			{
				at.decl += "," + aux.decl;
			}
			else 
			{
				at.decl = aux.decl;
			}
		}
		at.trad += aux.trad;
	}
	{
		at.trad += cfor + ":\n";
		at.trad += "ldc.i4 1\n";
		at.trad += "ldloc " + iter + "\n";
		at.trad += "add\n";
		at.trad += "stloc " + iter + "\n";
		at.trad += "br " + ifor + "\n";
		at.trad += ffor + ":\n";
	}
	{
		ts = ts.pop();
	}
	|
	
	FOR
	{
		ts = new TablaSimbolos(ts);
	}
	PARI
	INT
	idvar = ID
	{
		if(ts.existe($ID.text))
		{
			Error.throwError1($idvar.line, $idvar.pos, $idvar.text);
		}
		
		Tipo eltipo = new Tipo();
		eltipo.tipo = Tipo.ENTERO;
		Simbolo s = new Simbolo($ID.text, eltipo, TablaSimbolos.getPos());
		s.indice = true;
		ts.nuevoSimbolo(s);
		at.decl = "int32";
		boolean ascendente = true; 
		String ffor = TablaSimbolos.getEtiqueta();
		String ifor = TablaSimbolos.getEtiqueta();
		String cfor = TablaSimbolos.getEtiqueta();
	}
	ASIG
	aux = expr
	{
		if(aux.tipoSimple == Tipo.REAL)
		{
			
			at.trad += aux.trad;
			at.trad += "conv.i4\n";
		}
		else if(aux.tipoSimple != Tipo.ENTERO)
		{
			Error.throwError6($ASIG.line, $ASIG.pos);
		}
		else
		{
			at.trad += aux.trad;
		}
		
		
		at.trad += "stloc " + s.posicion + "\n";
		
		
		
	}
	TO
	e = expr
	{
		if(e.tipoSimple == Tipo.REAL)
		{
			
			at.trad += e.trad;
			at.trad += "conv.i4\n";
		}
		else if(e.tipoSimple != Tipo.ENTERO)
		{
			Error.throwError17($TO.line, $TO.pos);
		}
		else
		{
			at.trad += e.trad;
		}
	}
	STEP
	(
	ADDOP
	{
		if($ADDOP.text.equals("-"))
		{
			ascendente = false;
		}
	}
	)?
	paso = ENTERO
	PARD
	{
		etiqBreak = ffor;
		etiqCont = cfor;
	}
	aux = instr[etiqBreak, etiqCont, false]
	{
		if(at.decl == null)
		{
			at.decl = aux.decl;
		}
		else
		{
			if(aux.decl != null)
			{
				at.decl += "," + aux.decl;
			}
		}
		
		at.trad += ifor + ":\n" + "dup\n";
		at.trad += "ldloc " + s.posicion + "\n";
		if(ascendente)
		{
			at.trad += "clt\n";
		}
		else
		{
			at.trad += "cgt\n";
		}
		
		at.trad += "ldc.i4 1\n";
		at.trad += "beq " + ffor + "\n";
		at.trad += aux.trad;
		at.trad += cfor + ":\n";
		at.trad += "ldloc " + s.posicion + "\n";
		at.trad += "ldc.i4 " + $paso.text + "\n";
		
		if(ascendente)
		{
			at.trad += "add\n";
		}
		else
		{
			at.trad += "sub\n";
		}
		
		at.trad += "stloc " + s.posicion + "\n";
		
	}
	{
		at.trad += "br " + ifor + "\n" + ffor + ":\npop\n";
	}
	
	{
		ts = ts.pop();
	}
	
	|
	
	BREAK
	{
		if(!etiqBreak.equals(""))
		{
			at.trad += "br " + etiqBreak + "\n";
		}
		else
		{
			Error.throwError16($BREAK.line, $BREAK.pos, $BREAK.text);
		}
	}
	PYC
	
	|
	
	CONTINUE
	{
		if(!etiqCont.equals(""))
		{
			at.trad += "br " + etiqCont + "\n";
		}
		else
		{
			Error.throwError16($CONTINUE.line, $CONTINUE.pos, $CONTINUE.text);
		}
	}
	PYC
	
	|
	
	r = ref
	c = cambio[r]
	{
		at.trad += c.trad;
	}
	
	|
	
	/*ldc.i4 40        // tamaño total del array
	newarr [mscorlib]System.Int32 // instrucción para reservar memoria para arrays
	stloc pos        // almacenar dirección del array en la variable local.*/
	idvar = ID
	{
		Simbolo s = ts.busca($ID.text);
		
		if(s == null)
		{
			Error.throwError2($ID.line, $ID.pos, $ID.text);
		}
	}
	ASIG
	{
		if(s.tipo.tipo != Tipo.ARRAY)
		{
			Error.throwError11($idvar.line, $idvar.pos, $idvar.text);
		}
	}
	NEW
	t = tipoSimple
	{
		Tipo actual = s.tipo;
		while(actual.tipoBase != null)
		{
			actual = actual.tipoBase;
		}
		
		if(t.tipo.tipo != actual.tipo)
		{
			Error.throwError14(t.th, t.tipo.tipo);
		}
	}
	CORI
	d = dims[s, $CORI.line, $CORI.pos]
	CORD
	{
		if(d.trad == null)
		{
			Error.throwError10($CORD.line, $CORD.pos);
		}
		
		at.trad += d.trad;
		if(t.tipo.tipo == Tipo.ENTERO || t.tipo.tipo == Tipo.BOOLEAN)
		{
			at.trad += "newarr [mscorlib]System.Int32\n";
		}
		else if(t.tipo.tipo == Tipo.REAL)
		{
			at.trad += "newarr [mscorlib]System.Double\n";
		}
		at.trad += "stloc " + s.posicion + "\n";
		
	}
	PYC
	
	|
	
	WRITELINE
	PARI
	var = expr
	
	{
		at.trad = var.trad;
		if(var.tipoSimple == Tipo.ENTERO)
		{
			at.trad += "call void [mscorlib]System.Console::WriteLine(int32)\n";
			
		}
		else if(var.tipoSimple == Tipo.REAL)
		{
			at.trad += "call void [mscorlib]System.Console::WriteLine(float64)\n";
			
		}
		else if(var.tipoSimple == Tipo.BOOLEAN)
		{
			at.trad += "call void [mscorlib]System.Console::WriteLine(bool)\n";
		}
		else
		{
			at.trad += "call void [mscorlib]System.Console::WriteLine(NADA)\n";
		}
	
	}
	PARD
	PYC
;

dims[Simbolo s, int line, int pos] returns [Atributos at]
@init{
   at = new Atributos();
   at.tipo = new Tipo();
   at.trad = "";
   Tipo actual = s.tipo;
   int total = 1;
} :
	e = ENTERO 
	{
		if(Integer.parseInt($e.text) > 0)
		{
			if(actual.tipo == Tipo.ARRAY)
			{
				int aux = Integer.parseInt($e.text);
				actual.tamano  = aux; 
				total *= aux;
				actual = actual.tipoBase;
			}
			else
			{
				Error.throwError11($e.line, $e.pos, s.nombre);
			}
		
		}
		else
		{
			Error.throwError8($e.line, $e.pos);
		}
	}
	(
	c = COMA 
	e = ENTERO
	{
		if(Integer.parseInt($e.text) > 0)
		{
			if(actual.tipo ==Tipo.ARRAY)
			{
				int aux = Integer.parseInt($e.text);
				actual.tamano  = aux; 
				total *= aux;
				actual = actual.tipoBase;
			}
			else
			{
				Error.throwError10($c.line, $c.pos);
			}
		}
		else
		{
			Error.throwError8($e.line, $e.pos);
		}
	}
	)*
	
	{
		
		if(actual.tipo == Tipo.ARRAY)
		{
			//Error.throwError10(line, pos);
			at.trad = null;
		}
		else
		{
			at.trad = "ldc.i4 " + total + "\n";
			s.tamano = total;
		}
	}
;

cambio[Atributos r] returns [Atributos at]
@init{
   at = new Atributos();
   at.tipo = new Tipo();
   at.trad = "";
} :
	ASIG
	{
		Simbolo ind = ts.busca(r.nombre);
		if(ind != null)
		{
			if(ind.indice)
			{
				Error.throwError15($ASIG.line, $ASIG.pos);
			}
		}
	}
	e = expr
	{
		at.trad = e.trad;
		at.tipoSimple = r.tipoSimple;
		at.tipo.tipo = r.tipoSimple;
		
		
		if(r.tipo.tipo == Tipo.ARRAY)
		{
			
			if(e.tipo.tipo == Tipo.ENTERO && r.tipo.getTipoBase() == Tipo.ENTERO)
			{
				at.trad = "ldloc " + r.trad + e.trad + "stelem.i4\n";
			}
			else if(e.tipo.tipo == Tipo.REAL && r.tipo.getTipoBase() == Tipo.REAL)
			{
				at.trad = "ldloc " + r.trad + e.trad + "stelem.r8\n";
			}
			else if(e.tipo.tipo == Tipo.REAL && r.tipo.getTipoBase() == Tipo.ENTERO)
			{
				//at.trad += "conv.i4\n";
				at.trad = "ldloc " + r.trad + e.trad + "conv.i4\nstelem.i4\n";
			}
			else if(e.tipo.tipo == Tipo.ENTERO && r.tipo.getTipoBase() == Tipo.REAL)
			{
				//at.trad += "conv.r8\n";
				at.trad = "ldloc " + r.trad + e.trad + "conv.r8\nstelem.r8\n";
			}
			else if(e.tipo.tipo == Tipo.BOOLEAN && r.tipo.getTipoBase() == Tipo.BOOLEAN)
			{
				at.trad = "ldloc " + r.trad + e.trad + "stelem.i4\n";
			}
			else
			{
				Error.throwError6($ASIG.line, $ASIG.pos);
			}
		}
		else
		{
			if(e.tipo.tipo == Tipo.ENTERO && r.tipo.tipo == Tipo.ENTERO)
			{
				at.trad += "stloc " + r.trad + "\n";
			}
			else if(e.tipo.tipo == Tipo.REAL && r.tipo.tipo == Tipo.REAL)
			{
				at.trad += "stloc " + r.trad + "\n";
			}
			else if(e.tipo.tipo == Tipo.REAL && r.tipo.tipo == Tipo.ENTERO)
			{
				at.trad += "conv.i4\n";
				at.trad += "stloc " + r.trad + "\n";
			}
			else if(e.tipo.tipo == Tipo.ENTERO && r.tipo.tipo == Tipo.REAL)
			{
				at.trad += "conv.r8\n";
				at.trad += "stloc " + r.trad + "\n";
			}
			else if(e.tipo.tipo == Tipo.BOOLEAN && r.tipo.tipo == Tipo.BOOLEAN)
			{
				at.trad += "stloc " + r.trad + "\n";
			}
			else
			{
				Error.throwError6($ASIG.line, $ASIG.pos);
			}
		}
	}
	PYC
	
	|
	
	PUNTO
	READLINE
	{
		Simbolo ind = ts.busca(r.nombre);
		if(ind != null)
		{
			if(ind.indice)
			{
				Error.throwError15($READLINE.line, $READLINE.pos);
			}
		}
		
		if(r.tipo.tipo == Tipo.ARRAY)
		{
			at.trad += "call string [mscorlib]System.Console::ReadLine()\n";
			if($READLINE.text.contains("int") && r.tipo.getTipoBase() == Tipo.ENTERO)
			{
				at.trad = "ldloc " + r.trad + 
				"call string [mscorlib]System.Console::ReadLine()\n" + 
				"call int32 [mscorlib]System.Int32::Parse(string)\n" + 
				"stelem.i4\n";
			}
			else if($READLINE.text.contains("double") && r.tipo.getTipoBase() == Tipo.REAL)
			{
				at.trad = "ldloc " + r.trad + 
				"call string [mscorlib]System.Console::ReadLine()\n" + 
				"call float64 [mscorlib]System.Double::Parse(string)\n" + 
				"stelem.r8\n";
			}
			/*else if($READLINE.text.contains("double") && r.tipo.getTipoBase() == Tipo.ENTERO)
			{
				at.trad = "ldloc " + r.trad + 
				"call string [mscorlib]System.Console::ReadLine()\n" + 
				"call float64 [mscorlib]System.Double::Parse(string)\n" + 
				"conv.i4\n" + 
				"stelem.i4\n";
			}
			else if($READLINE.text.contains("int") && r.tipo.getTipoBase() == Tipo.REAL)
			{	
				at.trad = "ldloc " + r.trad + 
				"call string [mscorlib]System.Console::ReadLine()\n" + 
				"call int32 [mscorlib]System.Int32::Parse(string)\n" + 
				"conv.r8\n" + 
				"stelem.r8\n";
				//TODO: Preguntar los tipos acorde.
			}*/
			else if($READLINE.text.contains("bool") && r.tipo.getTipoBase() == Tipo.BOOLEAN)
			{
				at.trad = "ldloc " + r.trad + 
				"call string [mscorlib]System.Console::ReadLine()\n" + 
				"call bool [mscorlib]System.Boolean::Parse(string)\n" + 
				"stelem.i4\n";
			}
			else
			{
				Error.throwError7($READLINE.line, $READLINE.pos);
			}
		}
		else
		{
			if($READLINE.text.contains("int") && r.tipo.tipo == Tipo.ENTERO)
			{
				at.trad = 
				"call string [mscorlib]System.Console::ReadLine()\n" + 
				"call int32 [mscorlib]System.Int32::Parse(string)\n" +
				"stloc " + r.trad + "\n";
			}
			else if($READLINE.text.contains("double") && r.tipo.tipo == Tipo.REAL)
			{
				at.trad =
				"call string [mscorlib]System.Console::ReadLine()\n" + 
				"call float64 [mscorlib]System.Double::Parse(string)\n" +
				"stloc " + r.trad + "\n";
			}
			/*else if($READLINE.text.contains("double") && r.tipo.tipo == Tipo.ENTERO)
			{
				at.trad = "call string [mscorlib]System.Console::ReadLine()\n" + 
				"call float64 [mscorlib]System.Double::Parse(string)\n";
				at.trad += "conv.i4\n";
				at.trad += "stloc " + r.trad + "\n";
			}
			else if($READLINE.text.contains("int") && r.tipo.tipo == Tipo.REAL)
			{
				at.trad = "call string [mscorlib]System.Console::ReadLine()\n" + 
				"call int32 [mscorlib]System.Int32::Parse(string)\n";
				at.trad += "conv.r8\n";
				at.trad += "stloc " + r.trad + "\n";
			}*/
			else if($READLINE.text.contains("bool") && r.tipo.tipo == Tipo.BOOLEAN)
			{
				at.trad = "call string [mscorlib]System.Console::ReadLine()\n" + 
				"call bool [mscorlib]System.Boolean::Parse(string)\n";
				at.trad += "stloc " + r.trad + "\n";
			}
			else
			{
				Error.throwError7($READLINE.line, $READLINE.pos);
			}
		}
	}
	PYC
;

expr returns [Atributos at]
@init{
   at = new Atributos();
   //TODO: Cambiar esto
   at.tipo = new Tipo();
   at.tipo.tipo = Tipo.ENTERO;
   at.trad = "";
} :
	e1 = eand
	{
		at.trad = e1.trad;
		at.tipoSimple = e1.tipoSimple; 
		at.tipo.tipo = e1.tipoSimple; 
	}
	(
	OR 
	{
		if(e1.tipoSimple != Tipo.BOOLEAN)
		{
			Error.throwError4($OR.line, $OR.pos, $OR.text);
		}
	}
	e = eand
	{
		if(e.tipoSimple == Tipo.BOOLEAN && at.tipoSimple == Tipo.BOOLEAN)
		{
			at.trad += e.trad;
			at.trad += "or\n";
			at.tipoSimple = Tipo.BOOLEAN;
		}
		else
		{
			Error.throwError4($OR.line, $OR.pos, $OR.text);
		}
	}
	)*
;

eand returns [Atributos at]
@init{
   at = new Atributos();
   at.trad = "";
} :
	e1 = erel
	{
		at.trad = e1.trad;
		at.tipoSimple = e1.tipoSimple;
		at.th = e1.th;
	}
	(
	AND 
	{
		if(e1.tipoSimple != Tipo.BOOLEAN)
		{
			Error.throwError4($AND.line, $AND.pos, $AND.text);
		}
	}
	e = erel
	{
		if(e.tipoSimple == Tipo.BOOLEAN && at.tipoSimple == Tipo.BOOLEAN)
		{
			at.trad += e.trad;
			at.trad += "and\n";
			at.tipoSimple = Tipo.BOOLEAN;
		}
		else
		{
			Error.throwError4($AND.line, $AND.pos, $AND.text);
		}
	}
	)*
;

erel returns [Atributos at]
@init{
   at = new Atributos();
   at.trad = "";
   int ant;
   String antTrad = "";
   boolean hacerPop = false;
} :
	e = esum
	{
		at.trad = e.trad;
		at.tipoSimple = e.tipoSimple;
		at.th = e.th;
		ant = e.tipoSimple;
		antTrad = e.trad;
	}
	(
	RELOP
	{
		//at.trad = "";
		//hacerPop = true;
	}
	e = esum
	{
		String op = "";
		if($RELOP.text.equals(">"))
		{
			op = "cgt";
		}
		else if($RELOP.text.equals("<"))
		{
			op = "clt";
		}
		else if($RELOP.text.equals(">="))
		{
			op = "clt\nldc.i4 1\nxor";
		}
		else if($RELOP.text.equals("<="))
		{
			op = "cgt\nldc.i4 1\nxor";
		}
		else if($RELOP.text.equals("=="))
		{
			op = "ceq";
		}
		else if($RELOP.text.equals("!="))
		{
			op = "ceq\nldc.i4 1\nxor";
		}
		
		//at.trad += antTrad;
		//antTrad = e.trad;
		if((ant == Tipo.ENTERO || ant == Tipo.BOOLEAN) && (e.tipoSimple == Tipo.ENTERO || e.tipoSimple == Tipo.BOOLEAN))
		{
			
			at.trad += e.trad;
			at.trad += op + "\n";
			
			at.tipoSimple = Tipo.BOOLEAN;
			ant = Tipo.BOOLEAN;
		}
		else if(ant == Tipo.REAL && e.tipoSimple == Tipo.REAL)
		{
			at.trad += e.trad;
			at.trad += op + "\n";
			
			at.tipoSimple = Tipo.BOOLEAN;
			ant = Tipo.BOOLEAN;
		}
		else if((ant == Tipo.ENTERO || ant == Tipo.BOOLEAN) && e.tipoSimple == Tipo.REAL)
		{
			at.trad += "conv.r8\n";
			at.trad += e.trad;
			at.trad += op + "\n";
			
			at.tipoSimple = Tipo.BOOLEAN;
			ant = Tipo.BOOLEAN;
		}
		else if(ant == Tipo.REAL && (e.tipoSimple == Tipo.ENTERO || e.tipoSimple == Tipo.BOOLEAN))
		{
			at.trad += e.trad;
			at.trad += "conv.r8\n";
			at.trad += op + "\n";
			
			at.tipoSimple = Tipo.BOOLEAN;
			ant = Tipo.BOOLEAN;
		}
	} 
	)*
	
	/*{
	if(hacerPop)
	{	
		//at.trad += "pop\n";
	}
	}*/
;

esum returns [Atributos at]
@init{
   at = new Atributos();
   at.trad = "";
} : 
	t1 = term
	{
		at.trad = t1.trad;
		at.th = t1.th;
		at.tipoSimple = t1.tipoSimple;
		
		
	}
	(
	ADDOP 
	{
		if(t1.tipoSimple == Tipo.BOOLEAN)
		{
			Error.throwError3($ADDOP.line, $ADDOP.pos, $ADDOP.text);
		}
	}
	t = term
	
	{
		String op = "";
		if($ADDOP.text.equals("+"))
		{
			op = "add";
		}
		else if($ADDOP.text.equals("-"))
		{
			op = "sub";
		}
		
		if(at.tipoSimple == Tipo.ENTERO && t.tipoSimple == Tipo.ENTERO)
		{
			at.trad += t.trad;
			at.trad += op + "\n";
			
			at.tipoSimple = Tipo.ENTERO;
		}
		else if(at.tipoSimple == Tipo.REAL && t.tipoSimple == Tipo.REAL)
		{
			at.trad += t.trad;
			at.trad += op + "\n";
			
			at.tipoSimple = Tipo.REAL;
		}
		else if(at.tipoSimple == Tipo.ENTERO && t.tipoSimple == Tipo.REAL)
		{
			at.trad += "conv.r8\n";
			at.trad += t.trad;
			at.trad += op + "\n";
			
			at.tipoSimple = Tipo.REAL;
		}
		else if(at.tipoSimple == Tipo.REAL && t.tipoSimple == Tipo.ENTERO)
		{
			at.trad += t.trad;
			at.trad += "conv.r8\n";
			at.trad += op + "\n";
			
			at.tipoSimple = Tipo.REAL;
		}
		else
		{
			Error.throwError3($ADDOP.line, $ADDOP.pos, $ADDOP.text);
		}
		
	}
	
	)*
;

term returns [Atributos at]
@init{
   at = new Atributos();
   at.trad = "";
} :
	f1 = factor
	{
		at.th = f1.th;
		at.tipoSimple = f1.tipoSimple;
		at.trad = f1.trad;
	}
		
	(
	
	MULOP
	{
		if(f1.tipoSimple == Tipo.BOOLEAN)
		{
			Error.throwError3($MULOP.line, $MULOP.pos, $MULOP.text);
		}
	}
	f = factor
	{
		String op = "";
		if($MULOP.text.equals("*"))
		{
			op = "mul";
		}
		else if($MULOP.text.equals("/"))
		{
			op = "div";
		}
		
		if(at.tipoSimple == Tipo.ENTERO && f.tipoSimple == Tipo.ENTERO)
		{
			at.trad += f.trad;
			at.trad += op + "\n";
			
			at.tipoSimple = Tipo.ENTERO;
		}
		else if(at.tipoSimple == Tipo.REAL && f.tipoSimple == Tipo.REAL)
		{
			at.trad += f.trad;
			at.trad += op + "\n";
			
			at.tipoSimple = Tipo.REAL;
		}
		else if(at.tipoSimple == Tipo.ENTERO && f.tipoSimple == Tipo.REAL)
		{
			at.trad += "conv.r8\n";
			at.trad += f.trad;
			at.trad += op + "\n";
			
			at.tipoSimple = Tipo.REAL;
		}
		else if(at.tipoSimple == Tipo.REAL && f.tipoSimple == Tipo.ENTERO)
		{
			at.trad += f.trad;
			at.trad += "conv.r8\n";
			at.trad += op + "\n";
			
			at.tipoSimple = Tipo.REAL;
		}
		else
		{
			Error.throwError3($MULOP.line, $MULOP.pos, $MULOP.text);
		}
	}
	)*
;

factor returns [Atributos at]
@init{
   at = new Atributos();
   at.trad = "";
} :
	b = base
	{
		at.th = b.th;
		at.tipoSimple = b.tipoSimple;
		at.trad = b.trad;
		
	}
	
	|
	
	NOT
	f = factor
	{
		if(f.tipoSimple == Tipo.BOOLEAN)
		{
			at.trad = f.trad + "ldc.i4 1\nxor\n";	
			at.tipoSimple = f.tipoSimple;
		}
		else
		{
			Error.throwError4($NOT.line, $NOT.pos, $NOT.text);
		}
	}
	
	|
	
	PARI
	ADDOP
	f = factor
	{
		if(f.tipoSimple == Tipo.REAL || f.tipoSimple == Tipo.ENTERO)
		{
			if($ADDOP.text.equals("-"))
			{
				at.trad += f.trad;
				at.trad += "neg\n";
				at.tipo = f.tipo;
				at.tipoSimple = f.tipoSimple;
			}
			else
			{
				at.trad += f.trad;
				at.tipo = f.tipo;
				at.tipoSimple = f.tipoSimple;
			}
		}
		else
		{
			Error.throwError3($ADDOP.line, $ADDOP.pos, $ADDOP.text);
		}
	}
	PARD
;

base returns [Atributos at]
@init{
   at = new Atributos();
   at.trad = "";
   
		
} :

	ENTERO 
	{
		at.tipoSimple = Tipo.ENTERO;
		at.th = $ENTERO.text;
		at.trad = "ldc.i4 " + at.th + "\n";
	}
	| 
	REAL 
	{
		at.tipoSimple = Tipo.REAL;
		at.th = $REAL.text;
		at.trad = "ldc.r8 " + at.th + "\n";
	}
	|
	BOOLEANO
	{
		at.tipoSimple = Tipo.BOOLEAN;
		if($BOOLEANO.text.equals("True"))
		{
			at.trad = "ldc.i4 1\n";
		}
		else
		{
			at.trad = "ldc.i4 0\n";
		}
		//at.trad = "ldc.i4 " + at.th + "\n";
	}
	|
	
	PARI
	e = expr 
	{
		at.tipoSimple = e.tipoSimple;
		at.trad = e.trad;
	}
	PARD
	
	|
	
	r = ref
	{
		at.trad = r.trad;
		at.tipo = r.tipo;
		at.tipoSimple = r.tipoSimple;
		
		if(at.tipo.tipo == Tipo.ARRAY)
		{
			if(at.tipoSimple == Tipo.ENTERO || at.tipoSimple == Tipo.BOOLEAN)
			{
				at.trad = "ldloc " + r.trad + "ldelem.i4\n";
			}
			else if(at.tipoSimple == Tipo.REAL)
			{
				at.trad = "ldloc " + r.trad + "ldelem.r8\n";
			}
		}
		else
		{
			at.trad = "ldloc " + r.trad + "\n";
		}
	}
;

ref returns [Atributos at]
@init{
   at = new Atributos();
   at.trad = "";
   at.tipo = new Tipo();
} :
	idvar = ID
	{
		Simbolo s = ts.busca($ID.text);
		
		if(s != null)
		{
			at.tipo = s.tipo;
			at.tipoSimple = s.tipo.tipo;
			at.trad += s.posicion + "\n";	
		}
		else
		{
			Error.throwError2($ID.line, $ID.pos, $ID.text);
		}
		
		at.nombre = $idvar.text;
	}
	(CORI 
	{
		if(s.tipo.tipo != Tipo.ARRAY)
		{
			Error.throwError11($idvar.line, $idvar.pos, $idvar.text);
		}
	}
	i = indices[s, $CORI.line, $CORI.pos] 
	{
		//at.tipo = i.tipo;
		at.tipoSimple = i.tipoSimple;
		at.trad += i.trad;
	}
	CORD)?
	{
		if(at.tipoSimple == Tipo.ARRAY)
		{
			Error.throwError9($idvar.line, $idvar.pos, $idvar.text);
		}
	}
;

indices[Simbolo s, int line, int pos] returns [Atributos at]
@init{
   at = new Atributos();
   at.trad = "";
   at.tipo = new Tipo();
   Tipo actual = s.tipo;
   int total = 0;
} :
	e = expr
	{
		at.trad = "ldc.i4 0\n";
		if(e.tipoSimple == Tipo.ENTERO)
		{
			total = actual.tamano;
			at.trad += e.trad;
			at.trad += "add\n";
			actual = actual.tipoBase;
		}
		else if(e.tipoSimple == Tipo.REAL)
		{
			total = actual.tamano;
			at.trad += e.trad;
			at.trad += "conv.i4\n";
			at.trad += "add\n";
			actual = actual.tipoBase;
		}
		else
		{
			Error.throwError13(line, pos);
		}
	}
	(
	COMA 
	{
		if(actual.tipo != Tipo.ARRAY)
		{
			Error.throwError12($COMA.line, $COMA.pos);
		}
	}
	e = expr
	{
		if(e.tipoSimple == Tipo.ENTERO)
		{
			total = actual.tamano;
			
			at.trad += "ldc.i4 " + total + "\n"; 
			at.trad += "mul\n";
			at.trad += e.trad;
			at.trad += "add\n";
			actual = actual.tipoBase;
		}
		else if(e.tipoSimple == Tipo.REAL)
		{
			total = actual.tamano;
			
			at.trad += "ldc.i4 " + total + "\n"; 
			at.trad += "mul\n";
			at.trad += e.trad;
			at.trad += "conv.i4\n";
			at.trad += "add\n";
			actual = actual.tipoBase;
		}
		else
		{
			Error.throwError13($COMA.line, $COMA.pos);
		}
	}
	)*
	{
		if(actual != null)
		{
			at.tipo = actual;
			at.tipoSimple = actual.tipo;
		}
	}
;	
	
/*Especificacion Lexica*/
CLASS	:	'class';
SINGLE	:	'Single';
VOID	:	'void';
MAIN	:	'Main';
INT		:	'int';
DOUBLE	:	'double';
BOOL	:	'bool';
PUBLIC	:	'public';
STATIC	:	'static';
IF		:	'if';
ELSE	:	'else';
FOREACH	:	'foreach';
VAR		:	'var';
IN		:	'in';
FOR		:	'for';
TO		:	'to';
STEP	:	'step';
WHILE	:	'while';
BREAK	:	'break';
CONTINUE :	'continue';
NEW		:	'new';
WRITELINE	: 'System.Console.WriteLine';
READLINE	:	('int'|'double'|'bool')'.Parse(System.Console.ReadLine())';
LLAVEI		:	'{';
LLAVED		:	'}';
PARI		:	'(';
PARD		:	')';
CORI		:	'[';
CORD		:	']';
COMA		:	',';
PYC			:	';';
ASIG		:	'=';
OR			:	'|';
AND			:	'&';
RELOP		:	'=='|'!='|'<' | '>' | '<=' | '>=';
ADDOP		:	'+' | '-';
MULOP		:	'*' | '/';
NOT			:	'!';
PUNTO		:	'.';
ENTERO		:	('0'..'9')+;
REAL		:	('0'..'9')+'.'('0'..'9')+;
BOOLEANO	:	'True' | 'False';
ID			:	('a'..'z'|'A'..'Z')('a'..'z'|'A'..'Z'|'0'..'9'|'_')*;
SPACE		:	(' ' | '\n' | '\t' | '\r')+ {skip();};
COMENT		:	('//'.*'\n' | '/*'.*'*/') {skip();};


