grammar plp4;

@header {
import java.lang.String;
}

@members {
    /* Miembros de la clase del analizador sintáctico */
    TablaSimbolos ts = new TablaSimbolos();
    TablaSimbolos tsGlobal;
	ArrayList<Simbolo> simbolos;
	int tipoSimbolo;
	String claseActual;
	boolean hayMain = false;
	boolean hayConstructor = false;
	boolean canReturn;
	boolean inMain = false;
	int tipoRetorno;
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
s returns[String s] 
@init{
   ts = new TablaSimbolos();
} :
	{
		tsGlobal = ts;
	s = ".assembly extern mscorlib{}\n";
	s += ".assembly 'pl'{}\n";
	}
	(
	//TODO: Cambio - Cambiado
	miclase = clase
	{
		s += miclase.trad + "\n";
	}
	)+
	
	{
		if(!hayMain)
		{
			Error.throwError18();
		}
	}
;

clase returns [Atributos at]
@init{
   at = new Atributos();
} :
	CLASS
	id = ID
	{
		TablaSimbolos auxTS = ts;
		claseActual = $ID.text;
		hayConstructor = false;
		if(!ts.existe($ID.text))
		{
			Tipo t = new Tipo();
			t.ts = new TablaSimbolos(ts);
			t.clase = $ID.text;
			t.tipo = Tipo.CLASE;
			Simbolo laClase = tsGlobal.nuevoSimbolo($ID.text, t, Tipo.CLASE, -1);
			laClase.tipo.tipo = Tipo.CLASE;
			//ts.nuevoSimbolo($ID.text, t, Tipo.CLASE, -1);
			ts = t.ts;
		}
		else
		{
			//ERROR: Ya está declarado.+
			Error.throwError1($ID.line, $ID.pos, $ID.text);
			
		}
		
	}
	//TODO: Cambio
	{at.trad = ".class " + $id.text + " extends [mscorlib]System.Object // Definicion de la clase 'Single'\n";}
	LLAVEI
	{
		at.trad += "{\n";
		//at.trad += "/*Constructor de la clase Single*/\n";
		//at.trad += ".method public specialname rtspecialname instance void .ctor() cil managed\n{\n";
		
	}
	//TODO: Cambio
	//aux = metodo
	(
	aux = miembro
	{
		at.trad += aux.decl + aux.trad;
	}
	)+
	LLAVED
	{
		ts = auxTS;
		
		if(!hayConstructor)
		{
			at.trad += ".method public specialname rtspecialname instance void .ctor() cil managed\n{\n";
			at.trad += "ldarg 0\n";
			at.trad += "call instance void [mscorlib]System.Object::.ctor()\n";
			at.trad += "ret\n";
			at.trad += "}\n";
		
			Simbolo laClase = ts.busca(claseActual);
			Tipo tip = new Tipo();
			tip.tipo = Tipo.METODO;
			Simbolo elMetodo = laClase.tipo.ts.busca($ID.text);
			
			if(elMetodo == null)
			{
				elMetodo = laClase.tipo.ts.nuevoSimbolo($ID.text, tip, -1, 1);
			}
			elMetodo.tamanos.add(0);
			elMetodo.retornos.add(Tipo.NONE);
		}
		at.trad += "\n} /*Fin de la clase*/";
		//at.trad += "} /*Fin del constructor*/\n";
		//at.trad += aux.decl + aux.trad + "\n} /*Fin de la clase*/";
	}
;

metodo returns [Atributos at]
@init{
   at = new Atributos();
   at.decl = "";
   at.trad = "";
} :
	PUBLIC
	STATIC
	VOID
	MAIN
	PARI
	PARD
	
	{
		canReturn = false;
		inMain = true;
		if(!hayMain)
		{
			hayMain = true;
		}
		else
		{
			Error.throwError18();
		}
			
		at.decl = ".method static public void main () cil managed {\n";
		at.trad = "";
		
		Simbolo laClase = ts.busca(claseActual);
		Tipo tip = new Tipo();
		tip.ts = new TablaSimbolos(ts);
		tip.tipo = Tipo.METODO;
		laClase.tipo.ts.nuevoSimbolo("Main", tip, -1, 1);  
		
		TablaSimbolos auxTS = ts;
		ts = tip.ts;
		//ts = new TablaSimbolos(ts);
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
		
		ts = auxTS;
		//ts = ts.pop();
		
	}
	
	{	
		at.trad += "ret\n}";
		inMain = false;
	}
	
	|
	
	PUBLIC
	(
	t = tipoSimple
	)?
	ID
	{
		Simbolo laClase = ts.busca(claseActual);
		Simbolo elMetodo = laClase.tipo.ts.busca($ID.text);
		
		if(elMetodo != null && elMetodo.tipo.tipo != Tipo.METODO && !elMetodo.nombre.equals(claseActual))
		{
			Error.throwError1($ID.line, $ID.pos, $ID.text);
		}
	}
	PARI
	{
		canReturn = true;
		laClase = ts.busca(claseActual);
		Tipo tip = new Tipo();
		tip.tipo = Tipo.METODO;
		tip.clase = claseActual;
		tip.tipoBase = new Tipo();
		tip.tipoBase.clase = claseActual;
		if(t != null)
		{
			if($ID.text.equals(claseActual))
			{
				Error.throwError31($ID.line, $ID.pos);	
			}
			
			tip.retorno = t.tipoSimple;
		}
		else
		{
			tip.retorno = Tipo.NONE;
			
			if(!$ID.text.equals(claseActual))
			{
				Error.throwError32($ID.line, $ID.pos);	
			}
		}
		
		tipoRetorno = tip.retorno;
		tip.ts = new TablaSimbolos(ts);
		//laClase.tipo.ts.nuevoSimbolo($ID.text, tip, -1, 1);  
		
		TablaSimbolos auxTS = ts;
		//ts = new TablaSimbolos(ts);  
		ts = tip.ts;
	}
	a = args
	PARD
	{
		elMetodo = laClase.tipo.ts.busca($ID.text);
		
		
		if( elMetodo != null)
		{
			if(!elMetodo.tamanos.contains(a.tamano))
			{
				elMetodo.tamanos.add(a.tamano);
				elMetodo.retornos.add(tipoRetorno);
				
			}
			else
			{
				Error.throwError20($ID.line, $ID.pos, elMetodo, a.tamano);
			}
			
			if($ID.text.equals(claseActual) && a.trad.equals(""))
			{
				if(!hayConstructor)
				{
					at.trad += ".method public specialname rtspecialname instance void .ctor() cil managed\n{\n";
					hayConstructor = true;
					canReturn = false;
				}
				else
				{
					System.err.println("Dos por defecto");
				}
			}
			else if($ID.text.equals(claseActual))
			{
				canReturn = false;
				at.trad += ".method public specialname rtspecialname instance void .ctor(" + a.trad + ") cil managed\n{\n";
			}
			else
			{
				at.trad = ".method public " + t.trad + " '" + $ID.text + "'(" + a.trad + ") cil managed {\n";
			}
		}
		else
		{
			elMetodo = laClase.tipo.ts.nuevoSimbolo($ID.text, tip, -1, 1);
			elMetodo.tamanos.add(a.tamano);
			elMetodo.retornos.add(tipoRetorno);
			
			if($ID.text.equals(claseActual) && a.trad.equals(""))
			{
				if(!hayConstructor)
				{
					canReturn = false;
					at.trad += ".method public specialname rtspecialname instance void .ctor() cil managed\n{\n";
					hayConstructor = true;
				}
				else
				{
					System.err.println("Dos por defecto");
				}
			}
			else if($ID.text.equals(claseActual))
			{
				canReturn = false;
				at.trad += ".method public specialname rtspecialname instance void .ctor(" + a.trad + ") cil managed\n{\n";
			}
			else
			{
				at.trad = ".method public " + t.trad + " '" + $ID.text + "'(" + a.trad + ") cil managed {\n";
			}
		}
		
		//tip.tamano = a.tipoSimple;
		
		//System.out.println(ts);
	}
	aux = bloque["",""]
	{
		if(aux.decl != null)
		{
			at.trad += ".locals (" + aux.decl + ")\n";
		}
		at.trad += ".maxstack 99999\n";
		at.trad += aux.trad;
		at.decl = "";
		if($ID.text.equals(claseActual) && a.trad.equals(""))
		{
			at.trad += "ldarg 0\n";
			at.trad += "call instance void [mscorlib]System.Object::.ctor()\n";
			at.trad += "ret\n";
		}
		
		if(t != null)
		{
			if(t.tipo.tipo == Tipo.ENTERO || t.tipo.tipo == Tipo.BOOLEAN)
			{
				at.trad += "ldc.i4 0\nret\n}\n";
			}
			else if(t.tipo.tipo == Tipo.REAL)
			{
				at.trad += "ldc.r8 0.0\nret\n}\n";
			}
		}
		else
		{
			at.trad += "ret\n}\n";
		}
		
		
		//ts = ts.pop();
		ts = auxTS;
		//System.out.println(ts);
		
	}
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
		at.tipoSimple = Tipo.ENTERO;
		at.trad = "int32";
		at.th = $INT.line + "," + ($INT.pos + 1);
	}
	|
	DOUBLE
	{
		at.tipo.tipo = Tipo.REAL;
		at.tipoSimple = Tipo.REAL;
		at.trad = "float64";
		at.th = $DOUBLE.line + "," + ($DOUBLE.pos + 1);
	}
	|
	BOOL
	{
		at.tipo.tipo = Tipo.BOOLEAN;
		at.tipoSimple = Tipo.BOOLEAN;
		at.trad = "int32";
		at.th = $BOOL.line + "," + ($BOOL.pos + 1);
	}
;

decl[boolean miembro, boolean isPublic] returns [Atributos at]
@init{
   at = new Atributos();
   at.trad = "";
} :
	//TODO: Cambio
	tipoVar = tipo
	var = varid[tipoVar]
	{
		if(!miembro)
		{
			if(var.tipo.tipo == Tipo.ENTERO || var.tipo.tipo == Tipo.BOOLEAN)
			{
				at.decl = "int32";
				
			}
			else if(var.tipo.tipo == Tipo.REAL)
			{
				at.decl = "float64";
			}
			
			else if(var.tipo.tipo == Tipo.OBJETO)
			{
				at.decl = "class '" + var.tipo.tipoBase.clase + "'";
			}
			else if(var.tipo.tipo == Tipo.ARRAY)
			{
				if(tipoVar.tipo.tipo == Tipo.ENTERO || tipoVar.tipo.tipo == Tipo.BOOLEAN)
				{
					at.decl = "int32[]";
				}
				else if(tipoVar.tipo.tipo == Tipo.REAL)
				{
					at.decl = "float64[]";
				}
				else if(var.tipo.tipo == Tipo.OBJETO)
				{
					at.decl = "class '" + var.tipo.tipoBase.clase + "'[]";
				}
			}
			
			Simbolo s = ts.nuevoSimbolo(var.trad, var.tipo, ts.getPos(), var.tipo.tamano);
			
			//System.out.println(var.tipo.mostrar());
			//System.out.println("TS: " + ts);
		}
		else
		{
			if(var.tipo.tipo == Tipo.ENTERO || var.tipo.tipo == Tipo.BOOLEAN)
			{
				at.decl = ".field public int32 '" + var.trad + "'\n";
				
			}
			else if(var.tipo.tipo == Tipo.REAL)
			{
				at.decl = ".field public float64 '" + var.trad + "'\n";
			}
			else if(var.tipo.tipo == Tipo.ARRAY)
			{
				if(tipoVar.tipo.tipo == Tipo.ENTERO || tipoVar.tipo.tipo == Tipo.BOOLEAN)
				{
					at.decl = ".field public int32[] '" + var.trad + "'\n";
				}
				else if(tipoVar.tipo.tipo == Tipo.REAL)
				{
					at.decl = ".field public float64[] '" + var.trad + "'\n";
				}
			}
			else if(var.tipo.tipo == Tipo.OBJETO)
			{
				at.decl = ".field public " + var.tipo.tipoBase.clase + " '" + var.trad + "'\n";
			}
			
			Simbolo laClase = ts.busca(claseActual);
			var.tipo.clase = laClase.nombre;
			if(var.tipo.tipoBase == null)
			{
				var.tipo.tipoBase = new Tipo();
			}
			//var.tipo.tipoBase.clase = claseActual;
			Simbolo s = laClase.tipo.ts.nuevoSimbolo(var.trad, var.tipo, -1, var.tipo.tamano);
			s.posicion = -1;
			if(isPublic)
			{
				s.visibilidad = Simbolo.PUBLICO;
			}
			else
			{
				s.visibilidad = Simbolo.PRIVADO;
			}
		}
	}
	(COMA
	var = varid[tipoVar]
	{
		if(!miembro)
		{
			if(var.tipo.tipo == Tipo.ENTERO || var.tipo.tipo == Tipo.BOOLEAN)
			{
				at.decl += ", int32";
			}
			else if(var.tipo.tipo == Tipo.REAL)
			{
				at.decl += ", float64";
			}
			else if(var.tipo.tipo == Tipo.OBJETO)
			{
				at.decl += ", class '" + var.tipo.tipoBase.clase + "'";
			}
			else if(var.tipo.tipo == Tipo.ARRAY)
			{
				if(tipoVar.tipo.tipo == Tipo.ENTERO || tipoVar.tipo.tipo == Tipo.BOOLEAN)
				{
					at.decl += ", int32[]";
				}
				else if(tipoVar.tipo.tipo == Tipo.REAL)
				{
					at.decl += ", float64[]";
				}
				else if(var.tipo.tipo == Tipo.OBJETO)
				{
					at.decl += ", class '" + var.tipo.clase + "'[]";
				}
			}
		
			Simbolo s = ts.nuevoSimbolo(var.trad, var.tipo, ts.getPos(), var.tipo.tamano);
			
		}
		else
		{
			if(var.tipo.tipo == Tipo.ENTERO || var.tipo.tipo == Tipo.BOOLEAN)
			{
				at.decl += ".field public int32 '" + var.trad + "'\n";
				
			}
			else if(var.tipo.tipo == Tipo.REAL)
			{
				at.decl += ".field public float64 '" + var.trad + "'\n";
			}
			else if(var.tipo.tipo == Tipo.ARRAY)
			{
				if(tipoVar.tipo.tipo == Tipo.ENTERO || tipoVar.tipo.tipo == Tipo.BOOLEAN)
				{
					at.decl += ".field public int32[] '" + var.trad + "'\n";
				}
				else if(tipoVar.tipo.tipo == Tipo.REAL)
				{
					at.decl += ".field public float64[] '" + var.trad + "'\n";
				}
			}
			else if(var.tipo.tipo == Tipo.OBJETO)
			{
				at.decl = ".field public " + var.tipo.tipoBase.clase + " '" + var.trad + "'\n";
			}
			
			Simbolo laClase = ts.busca(claseActual);
			var.tipo.clase = laClase.nombre;
			if(var.tipo.tipoBase == null)
			{
				var.tipo.tipoBase = new Tipo();
			}
			//var.tipo.tipoBase.clase = claseActual;
			
			Simbolo s = laClase.tipo.ts.nuevoSimbolo(var.trad, var.tipo, -1, var.tipo.tamano);
			if(isPublic)
			{
				s.visibilidad = Simbolo.PUBLICO;
			}
			else
			{
				s.visibilidad = Simbolo.PRIVADO;
			}
		}
	
	}
	)*
	PYC
;

varid[Atributos h] returns [Atributos at]
@init{
   at = new Atributos();
   at.tipo = new Tipo();
   at.tipo = new Tipo(h.tipo);
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
		if(h.tipo.tipo == Tipo.OBJETO)
		{
			Error.throwError25($CORI.line, $CORI.pos);
		}
		
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
	aux = decl[false, true]
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
	//TODO: Cambio
	//idvar = ID
	misubref = subref[false]
	{
		if(ts.existe(misubref.nombre))
		{
			Error.throwError1(misubref.row, misubref.column, misubref.nombre);
		}
	}
	IN
	a = subref[true]
	{
		//Simbolo a = ts.busca($idarray.nombre);
		Tipo eltipo = new Tipo();
		eltipo.tipo = a.tipo.getTipoBase();
		/*if(a != null)
		{
			eltipo.tipo = a.tipo.getTipoBase();
		}
		else
		{
			Error.throwError2(a.line, a.column, a.nombre);
		}*/
		
		if(a.tipo.tipo != Tipo.ARRAY)
		{
			Error.throwError11(a.row, a.column, a.nombre);
		}
		
		Simbolo s = new Simbolo(misubref.nombre, eltipo, ts.getPos());
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
		//System.out.println("Total: " + total);
		
		int iter = ts.getPos();
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
		
		String prefix;
		if(a.isCampo && !a.isOtra)
		{
			prefix = "ldarg 0\nldfld ";
		}
		else if(a.isCampo && a.isOtra)
		{
			prefix = "ldfld ";
		}
		else if(a.tipo.tipo == Tipo.ARGUMENTO)
		{
			prefix = "ldarg ";
		}
		else
		{
			prefix = "ldloc ";
		}
		
		//Cargo elemento en la variable
		at.trad += a.prefix + prefix +  a.trad;
		/*if(a.posicion != -1)
		{
			
			at.trad += "ldloc " + a.posicion + "\n";
		}
		else
		{
			at.trad += "ldarg 0\n";
			if(eltipo.tipo == Tipo.ENTERO || eltipo.tipo == Tipo.BOOLEAN)
			{
				at.trad += "ldfld int32[]'" + a.tipo.clase + "'::'" + a.nombre + "'\n"; 
			}
			else if(eltipo.tipo == Tipo.REAL)
			{
				at.trad += "ldfld float64[]'" + a.tipo.clase + "'::'" + a.nombre + "'\n"; 
			}
			 
		}*/
		
		at.trad += "ldloc " + iter + "\n";
		
		if(eltipo.tipo == Tipo.ENTERO || eltipo.tipo == Tipo.BOOLEAN)
		{
			at.trad += "ldelem.i4\n";
		}
		else if(eltipo.tipo == Tipo.REAL)
		{
			at.trad += "ldelem.r8\n";
		}
		
		//at.trad += "stloc " + s.posicion + "\n";
		if(s.posicion == -1)
		{
			at.trad = "'" + s.tipo.clase + "'::'" + s.nombre + "'"; 
		}
		else
		{
			at.trad += "stloc " + s.posicion + "\n";
		}
		
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
		Simbolo s = new Simbolo($ID.text, eltipo, ts.getPos());
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
		
		if(s.posicion == -1)
		{
			at.trad = "stfld '" + s.tipo.clase + "'::'" + s.nombre + "'"; 
		}
		else
		{
			at.trad += "stloc " + s.posicion + "\n";
		}
		
		
		
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
	ID
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
		
		if(s.posicion == -1)
		{
			at.trad += "ldarg 0\n";
		}
		at.trad += d.trad;
		if(t.tipo.tipo == Tipo.ENTERO || t.tipo.tipo == Tipo.BOOLEAN)
		{
			at.trad += "newarr [mscorlib]System.Int32\n";
			if(s.posicion == -1)
			{
				at.trad += "stfld int32[] '" + s.tipo.clase + "'::'" + s.nombre + "'\n"; 
			}
			else
			{
				at.trad += "stloc " + s.posicion + "\n";
			}
		}
		else if(t.tipo.tipo == Tipo.REAL)
		{
			at.trad += "newarr [mscorlib]System.Double\n";
			if(s.posicion == -1)
			{
				at.trad += "stfld float64[]'" + s.tipo.clase + "'::'" + s.nombre + "'\n"; 
			}
			else
			{
				at.trad += "stloc " + s.posicion + "\n";
			}
		}
		
		
		
	}
	PYC
	
	|
	
	WRITELINE
	PARI
	var = expr
	
	{
		if(var.tipo.tipo == Tipo.OBJETO)
		{
			Error.throwError28($WRITELINE.line, $WRITELINE.pos);
		}
		
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
	
	|
	
	RETURN
	{
		if(!canReturn)
		{
			Error.throwError22($RETURN.line, $RETURN.pos);
		}
	}
	e = expr
	{
		if(canReturn)
		{
			if(e.tipoSimple == tipoRetorno)
			{
				at.trad = e.trad + "\nret\n";
			}
			else if(e.tipoSimple == Tipo.ENTERO && tipoRetorno == Tipo.REAL)
			{
				at.trad = e.trad + "\nconv.r8\nret\n";
			}
			else if(e.tipoSimple == Tipo.REAL && tipoRetorno == Tipo.ENTERO)
			{
				at.trad = e.trad + "\nconv.i4\nret\n";
			}
			else
			{
				Error.throwError23($RETURN.line, $RETURN.pos);
			}
		}
		else
		{
			Error.throwError22($RETURN.line, $RETURN.pos);
		}
		
		
	}
	PYC
	
	|
	//Construcción de Objetos
	idvar = ID
	{
		Simbolo s = ts.busca($idvar.text);
		String claseObjeto = "";
		
		if(s != null)
		{
			if(s.posicion == -1 && inMain)
			{
				Error.throwError27($idvar.line, $idvar.pos, $idvar.text);
			}
			
			if(s.tipo != null && !s.tipo.clase.equals(""))
			{
				claseObjeto = s.tipo.tipoBase.clase;
			}
			else
			{
				//ERROR No es objeto.
				Error.throwError19($idvar.line, $idvar.pos, $idvar.text);
			}
		}
		else
		{
			Error.throwError2($idvar.line, $idvar.pos, $idvar.text);
		}
	}
	ASIG
	NEW
	idclase = ID
	{
		Simbolo sClase = tsGlobal.busca($idclase.text);
		if(sClase != null)
		{
			//if(sClase.tipo.tipo == Tipo.CLASE || sClase.tipo.tipo == Tipo.METODO)
			//{
				if(sClase.nombre.equals(claseObjeto))
				{
					
				}
				else
				{
					Error.throwError26($idvar.line, $idvar.pos, $idvar.text, sClase.nombre);
				}
			//}
			/*else
			{
				Error.throwError19($idclase.line, $idclase.pos, $idclase.text);
			}*/
		}
		else
		{
			//Error
			Error.throwError2($idclase.line, $idclase.pos, $idclase.text);
			//Error.throwError19($idclase.line, $idclase.pos, $idclase.text);
		}
	}
	PARI
	p = params[$idclase.text,claseObjeto, $idclase.line, $idclase.pos, $PARI.line, $PARI.pos]
	{
		String paramsCount = "";
		
		for(int i = 0; i < p.tamano; i++)
		{
			if(i != 0)
			{
				paramsCount += ", ";
			}
			
			paramsCount += "float64";
		}
		at.tipo = new Tipo();
		at.tipo.tipo = p.tipo.tipo;
		at.tipoSimple = p.tipo.tipo;
		
		at.trad = p.trad + "\nnewobj instance void '" + claseObjeto + "'::.ctor(" + paramsCount +  ")\n";
				
		//System.out.println("PL : " + s.nombre + " " + s.posicion);
		if(s.posicion == -1)
		{
			at.trad =  "ldarg 0\n" + at.trad + "stfld " + s.tipo.tipoBase.clase + " '" + s.tipo.clase + "'::'" + s.nombre + "'\n"; 
		}
		else
		{
			at.trad += "stloc " + s.posicion + "\n";
		}
				
		//at.trad +=  "(" + paramsCount + ")\npop\n";
	}
	PARD
	PYC
	
	|
	
	misubref = subref[true]
	{
		at.tipo = new Tipo(misubref.tipo);
		at.tipoSimple = misubref.tipoSimple;
		at.trad = misubref.trad;
		at.prefix = misubref.prefix;
		at.sufix = misubref.sufix;
		at.isCampo = misubref.isCampo;
		at.row = misubref.row;
		at.column = misubref.column;
		at.nombre = misubref.nombre;
		at.isOtra = misubref.isOtra;
		
		if(misubref.nombre.equals(misubref.tipo.tipoBase.clase))
		{
			Error.throwError30( misubref.row,  misubref.column);
		}
		
		
	}
	PARI
	p = params[misubref.nombre, misubref.tipo.tipoBase.clase, misubref.row, misubref.column, $PARI.line, $PARI.pos]
	PARD
	{
		Simbolo elMetodo = ts.busca(misubref.nombre);
		
		
		
		String paramsCount = "";
		
		for(int i = 0; i < p.tamano; i++)
		{
			if(i != 0)
			{
				paramsCount += ", ";
			}
			
			paramsCount += "float64";
		}
		at.tipo = new Tipo();
		at.tipo.tipo = p.tipo.tipo;
		at.tipoSimple = p.tipo.tipo;
		at.trad = misubref.prefix + p.trad + misubref.interfix + p.th + misubref.sufix + "(" + paramsCount + ")\npop\n";
	}
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
		
		if(r.tipo.tipo == Tipo.METODO)
		{
			Error.throwError19(r.row, r.column, r.nombre);
		}
		
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
		
		String prefix;
		String load;
		load = "";
		if(r.isCampo && !r.isOtra)
		{
			prefix = "stfld ";
			load = "ldarg 0\n";
		}
		else if(r.isCampo && r.isOtra)
		{
			prefix = "stfld ";
			load = r.prefix;
		}
		else
		{
			if(r.tipo.tipo == Tipo.ARGUMENTO)
			{
				prefix = "starg ";
			}
			else
			{
				prefix = "stloc ";
			}
		}
		
		String prefixe = "";
		/*if(e.isCampo)
		{
			prefixe = "ldfld ";
		}
		else
		{
			prefixe = "ldloc ";
		}*/
		if(e.isCampo && !e.isOtra)
		{
			prefixe = "ldarg 0\nldfld ";
			
		}
		else if(e.isCampo && e.isOtra)
		{
			prefixe = "ldfld ";
		}
		else
		{
			prefixe = "ldloc ";
		}
		
		if(r.tipo.tipo == Tipo.ARRAY)
		{
			
			if(r.isCampo && !r.isOtra)
			{
				prefix = "ldfld ";
				load = "ldarg 0\n";
			}
			else if(r.isCampo && r.isOtra)
			{
				prefix = "ldfld ";
				load = r.prefix;
			}
			else
			{
				prefix = "ldloc ";
			}
			//System.out.println("Load: " + load + " PREFIX: " + prefix  + " R.TRAD: " + r.trad + " E.TRAD: "+ e.trad);
			if(e.tipo.tipo == Tipo.ENTERO && r.tipo.getTipoBase() == Tipo.ENTERO)
			{
				at.trad = load + prefix + r.trad + e.trad + "stelem.i4\n";
					//at.trad =  e.prefix + prefixe + e.sufix + r.prefix + prefix + r.sufix;
			}
			else if((e.tipo.tipo == Tipo.REAL || e.tipo.tipo == Tipo.ARGUMENTO) && (r.tipo.getTipoBase() == Tipo.REAL))
			{
				at.trad = load + prefix + r.trad + e.trad + "stelem.r8\n";
				
				//at.trad =  e.prefix + prefixe + e.sufix + r.prefix + prefix + r.sufix;
			}
			else if((e.tipo.tipo == Tipo.REAL || e.tipo.tipo == Tipo.ARGUMENTO) && r.tipo.getTipoBase() == Tipo.ENTERO)
			{
				//at.trad += "conv.i4\n";
				at.trad = load + prefix + r.trad + e.trad + "conv.i4\nstelem.i4\n";
			}
			else if(e.tipo.tipo == Tipo.ENTERO && r.tipo.getTipoBase() == Tipo.REAL)
			{
				//at.trad += "conv.r8\n";
				at.trad = load + prefix + r.trad + e.trad + "conv.r8\nstelem.r8\n";
			}
			else if(e.tipo.tipo == Tipo.BOOLEAN && r.tipo.getTipoBase() == Tipo.BOOLEAN)
			{
				at.trad = load + prefix + r.trad + e.trad + "stelem.i4\n";
			}
			else
			{
				Error.throwError6($ASIG.line, $ASIG.pos);
			}
		}
		else
		{
			at.trad = load + e.trad;
			if(e.tipo.tipo == Tipo.ENTERO && r.tipo.tipo == Tipo.ENTERO)
			{
				at.trad += prefix + r.trad + "\n";
			}
			else if((e.tipo.tipo == Tipo.REAL || e.tipo.tipo == Tipo.ARGUMENTO) && (r.tipo.tipo == Tipo.REAL || r.tipo.tipo == Tipo.ARGUMENTO))
			{
				at.trad += prefix + r.trad + "\n";
			}
			else if((e.tipo.tipo == Tipo.REAL || e.tipo.tipo == Tipo.ARGUMENTO) && r.tipo.tipo == Tipo.ENTERO)
			{
				at.trad += "conv.i4\n";
				at.trad += prefix + r.trad + "\n";
			}
			else if(e.tipo.tipo == Tipo.ENTERO && (r.tipo.tipo == Tipo.REAL || r.tipo.tipo == Tipo.ARGUMENTO))
			{
				at.trad += "conv.r8\n";
				at.trad += prefix + r.trad + "\n";
			}
			else if(e.tipo.tipo == Tipo.BOOLEAN && r.tipo.tipo == Tipo.BOOLEAN)
			{
				at.trad += prefix + r.trad + "\n";
			}
			else if(e.tipo.tipo == Tipo.OBJETO && r.tipo.tipo == Tipo.OBJETO)
			{
				//System.out.println("E: " + e.tipo.clase + " R: " + r.tipo.clase);
				/*System.out.println("E:\n" + e.prefix + "\n---------\n");
				System.out.println("E:\n" + e.sufix + "\n---------\n");
				System.out.println("R:\n" + prefix + "\n---------\n");
				System.out.println("Prefix:\n" + r.prefix + "\n---------\n");
				System.out.println("Prefixe:\n" + prefixe + "\n---------\n");
				System.out.println("L:\n" + load + "\n---------\n");
				System.out.println("Sufix:\n" + r.sufix + "\n---------\n");*/
				
				
				if(e.tipo.tipoBase != null && r.tipo.tipoBase != null)
				{
					if(e.tipo.tipoBase.clase.equals(r.tipo.tipoBase.clase))
					{
						//if(r.isC)
						//at.trad += prefix + r.trad + "\n";
						at.trad =  load + e.prefix + prefixe + e.sufix  + prefix + r.sufix;
						//at.trad = load + r.trad + e.prefix + prefix + e.sufix; 
						//at.trad = e.trad + load + prefix + r.trad;
					}
				}
				else if(r.tipo.clase.equals("Read") || r.tipo.clase.equals("Write") || r.tipo.clase.equals("intermedia"))
				{
						//at.trad += prefix + r.trad + "\n";
						//at.trad = e.trad + load + prefix + r.trad;
						
						
					at.trad =   load + e.prefix + prefixe + e.sufix +   prefix + r.sufix;
				}
				else
				{
					Error.throwError6($ASIG.line, $ASIG.pos);
				}
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
		if(r.tipo.tipo == Tipo.OBJETO)
		{
			Error.throwError28($READLINE.line, $READLINE.pos);
		}
		
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
				if(r.isCampo && !r.isOtra)
				{
					at.trad = "ldarg 0\nldfld " + r.trad;
				}
				else if(r.isCampo && r.isOtra)
				{
					at.trad = r.prefix + "ldfld " + r.trad;
				}
				else
				{
					at.trad = "ldloc " + r.trad;
				}
				at.trad += "call string [mscorlib]System.Console::ReadLine()\n" + 
				"call int32 [mscorlib]System.Int32::Parse(string)\n" + 
				"stelem.i4\n";
			}
			
			else if($READLINE.text.contains("double") && r.tipo.getTipoBase() == Tipo.REAL)
			{
				
				if(r.isCampo)
				{
					at.trad = "ldarg 0\nldfld " + r.trad;
				}
				else
				{
					at.trad = "ldloc " + r.trad;
				}
				at.trad += "call string [mscorlib]System.Console::ReadLine()\n" + 
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
				if(r.isCampo)
				{
					at.trad = "ldarg 0\nldfld " + r.trad;
				}
				else
				{
					at.trad = "ldloc " + r.trad;
				}
				at.trad += "call string [mscorlib]System.Console::ReadLine()\n" + 
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
				if(r.isCampo && !r.isOtra)
				{
					at.trad = "ldarg 0\n";
				}
				else if(r.isCampo && r.isOtra)
				{
					at.trad = r.prefix;
				}
				at.trad += 
				"call string [mscorlib]System.Console::ReadLine()\n" + 
				"call int32 [mscorlib]System.Int32::Parse(string)\n";
				if(r.isCampo)
				{
					at.trad += "stfld " + r.trad;
				}
				else
				{
					at.trad += "stloc " + r.trad;
				}
				at.trad += "\n";
			}
			else if($READLINE.text.contains("double") && r.tipo.tipo == Tipo.REAL)
			{
				if(r.isCampo)
				{
					at.trad = "ldarg 0\n";
				}
				else
				{
					at.trad = "";
				}
				at.trad += 
				"call string [mscorlib]System.Console::ReadLine()\n" + 
				"call float64 [mscorlib]System.Double::Parse(string)\n";
				if(r.isCampo)
				{
					at.trad += "stfld " + r.trad;
				}
				else
				{
					at.trad += "stloc " + r.trad + at.trad;
				}
				at.trad += "\n";
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
				/*if(r.isCampo)
				{
					at.trad = "ldarg 0\n";
				}
				else
				{
					at.trad = "";
				}*/
				at.trad +=  "call string [mscorlib]System.Console::ReadLine()\n" + 
				"call bool [mscorlib]System.Boolean::Parse(string)\n";
				if(r.isCampo)
				{
					at.trad += "stfld " + r.trad;
				}
				else
				{
					at.trad += "stloc " + r.trad;
				}
				at.trad += "\n";
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
		at.prefix = e1.prefix;
		at.sufix = e1.sufix; 
		at.isCampo = e1.isCampo; 
		at.isOtra = e1.isOtra;
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
		at.prefix = e1.prefix;
		at.sufix = e1.sufix; 
		at.isCampo = e1.isCampo; 
		at.isOtra = e1.isOtra;
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
		at.prefix = e.prefix;
		at.sufix = e.sufix; 
		at.isCampo = e.isCampo; 
		at.isOtra = e.isOtra;
	}
	(
	RELOP
	{
		//at.trad = "";
		//hacerPop = true;
		if(e.tipoSimple == Tipo.OBJETO)
		{
			Error.throwError29($RELOP.line, $RELOP.pos, $RELOP.text);
		}
	}
	e = esum
	{
		if(e.tipoSimple == Tipo.OBJETO)
		{
			Error.throwError29($RELOP.line, $RELOP.pos, $RELOP.text);
		}
		
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
		at.prefix = t1.prefix;
		at.sufix = t1.sufix; 
		at.isCampo = t1.isCampo; 
		at.isOtra = t1.isOtra;
		
		
	}
	(
	ADDOP 
	{
		if(t1.tipoSimple == Tipo.BOOLEAN)
		{
			Error.throwError3($ADDOP.line, $ADDOP.pos, $ADDOP.text);
		}
		else if(t1.tipoSimple == Tipo.CLASE)
		{
			Error.throwError19(t1.row, t1.column, t1.nombre);
		}
	}
	t = term
	
	{
		if(t.tipoSimple  == Tipo.CLASE)
		{
			System.out.println("FAAAALLOOOO");
		}
		
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
		at.row = f1.row;
		at.column = f1.column;
		at.nombre = f1.nombre;
		at.prefix = f1.prefix;
		at.sufix = f1.sufix; 
		at.isCampo = f1.isCampo; 
		at.isOtra = f1.isOtra;
	}
		
	(
	
	MULOP
	{
		if(f1.tipoSimple == Tipo.BOOLEAN)
		{
			Error.throwError3($MULOP.line, $MULOP.pos, $MULOP.text);
		}
		else if(f1.tipoSimple == Tipo.CLASE)
		{
			Error.throwError3(0,0, $MULOP.text);
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
		at.row = b.row;
		at.column = b.column;
		at.nombre = b.nombre;
		at.prefix = b.prefix;
		at.sufix = b.sufix; 
		at.isCampo = b.isCampo; 
		at.isOtra = b.isOtra;
		
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
		at.row = r.row;
		at.column = r.column;
		at.nombre = r.nombre;
		at.prefix = r.prefix;
		at.sufix = r.sufix; 
		at.isCampo = r.isCampo; 
		at.isOtra = r.isOtra;
		String prefix;
		
		if(r.isCampo && !r.isOtra)
		{
			prefix = "ldarg 0\nldfld ";
		}
		else if(r.isCampo && r.isOtra)
		{
			prefix = "ldfld ";
		}
		else if(r.tipo.tipo == Tipo.ARGUMENTO)
		{
			prefix = "ldarg ";
		}
		else
		{
			prefix = "ldloc ";
		}
		
		if(at.tipo.tipo == Tipo.ARRAY)
		{
			if(at.tipoSimple == Tipo.ENTERO || at.tipoSimple == Tipo.BOOLEAN)
			{
				at.trad = r.prefix + prefix + r.trad + "ldelem.i4\n";
			}
			else if(at.tipoSimple == Tipo.REAL)
			{
				at.trad = r.prefix + prefix + r.trad + "ldelem.r8\n";
			}
		}
		else
		{
			at.trad = r.prefix + prefix + r.trad + "\n";
		}
		
	}
	
	|
	
	misubref = subref[true]
	{
		at.trad = misubref.trad;
		at.tipo = misubref.tipo;
		//at.tipo = new Tipo();
		at.tipo.tipo = misubref.tipo.tipo;
		at.tipoSimple = misubref.tipoSimple;
		//System.out.println("Nombre: " + misubref.nombre + " clase: " + misubref.tipo.tipoBase.clase + " tipo: " + misubref.tipo.tipo);
		if(misubref.nombre.equals(misubref.tipo.tipoBase.clase))
		{
			Error.throwError30(misubref.row, misubref.column);
		}
		
		//System.out.println("N: " + misubref.nombre + " " + misubref.tipo.tipo + " " + misubref.tipo.tipoBase.clase);
		//if(misubref.tipo.tipoSimple != null && misubref.nombtr)
		if(misubref.tipo.tipo == Tipo.ARGUMENTO)
		{
			Error.throwError19( misubref.row,  misubref.column, misubref.nombre);
		}
	}
	PARI
	p = params[misubref.nombre, misubref.tipo.tipoBase.clase, misubref.row, misubref.column, $PARI.line, $PARI.pos]
	PARD
	{
		/*Simbolo elMetodo = ts.busca(misubref.nombre);
		
		if(elMetodo != null)
		{
			if(!elMetodo.tamanos.contains(p.tamano))
			{
				Error.throwError21(misubref.row, misubref.column, elMetodo, p.tamano);
			}
		}
		else
		{
			System.err.println("Error, no existe");
		}*/
		
		at.trad = misubref.prefix + p.trad + misubref.interfix + p.th + misubref.sufix;
		at.tipo = new Tipo();
		at.tipo.tipo = p.tipo.tipo;
		at.tipoSimple = p.tipo.tipo;
		at.trad += "(";
		
		for(int i = 0; i < p.tamano; i++)
		{	
			if(i != 0)
			{
				at.trad += ", ";
			}
			at.trad+= "float64";
		}
		at.trad += ")\n";
	}
;

ref returns [Atributos at]
@init{
   at = new Atributos();
   at.trad = "";
   at.tipo = new Tipo();
} :
	//TODO: Cambio
	//idvar = ID
	misubref = subref[true]
	{
		at.tipo = new Tipo(misubref.tipo);
		at.tipoSimple = misubref.tipoSimple;
		at.trad = misubref.trad;
		at.prefix = misubref.prefix;
		at.sufix = misubref.sufix;
		at.isCampo = misubref.isCampo;
		at.row = misubref.row;
		at.column = misubref.column;
		at.nombre = misubref.nombre;
		at.isOtra = misubref.isOtra;
	}
	(CORI 
	{
		//TODO: Cambio
		if(misubref.tipo.tipo != Tipo.ARRAY)
		{
			Error.throwError11(misubref.row, misubref.column, misubref.nombre);
		}
	}
	i = indices[misubref.tipo, $CORI.line, $CORI.pos] 
	{
		//at.tipo = i.tipo;
		at.tipoSimple = i.tipoSimple;
		at.trad += i.trad;
	}
	CORD)?
	{
		if(at.tipoSimple == Tipo.ARRAY)
		{
			Error.throwError9(misubref.row, misubref.column, misubref.nombre);
		}
	}
;

indices[Tipo tActual, int line, int pos] returns [Atributos at]
@init{
   at = new Atributos();
   at.trad = "";
   at.tipo = new Tipo();
   Tipo actual = tActual;
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

miembro returns [Atributos at]
@init{
   at = new Atributos();
}:
	c = campo
	{
		at.trad = c.trad;
		at.decl = c.decl;
	}
	|
	m = metodo
	{
		at.trad = m.trad;
		at.decl = m.decl;
	}
;

campo returns [Atributos at]
@init{
   at = new Atributos();
   at.trad = "";
}:
	v = visibilidad
	d = decl[true, v.isPublic]
	{
		at.decl = d.decl;
	}
;

visibilidad returns [Atributos at]
@init{
   at = new Atributos();
}:
	PUBLIC
	{
		at.trad = "public";
		at.isPublic = true;
	}
	
	|
	PRIVATE
	{
		at.trad = "private";
		at.isPublic = false;
	}
;
	
args returns [Atributos at]
@init{
   at = new Atributos();
   at.trad = "";
   at.tamano = 0;
   int num_arg = 1;
}:
	(
	DOUBLE
	id1 = ID
	{
		Tipo t = new Tipo();
		t.tipo = Tipo.ARGUMENTO;
		ts.nuevoSimbolo($id1.text, t, num_arg, 1);
		num_arg++;
		at.trad = "float64";
		at.tamano++;
	}
	(
	COMA
	DOUBLE
	id2 = ID
	{
		if(!ts.existe($id2.text))
		{
			t = new Tipo();
			t.tipo = Tipo.ARGUMENTO;
			ts.nuevoSimbolo($id2.text, t, num_arg, 1);
			num_arg++;
			at.trad += ", float64";
			at.tamano++;
		}
		else
		{
			Error.throwError2($id2.line, $id2.pos, $id2.text);
		}
	}
	)*
	)?
;

tipo returns [Atributos at]
@init{
   at = new Atributos();
}:
	ID
	{
		Simbolo s = ts.busca($ID.text);
		
		if(s != null)
		{
			at.tipo = s.tipo;
			at.tipo.clase = claseActual;
			at.tipo.tipo = Tipo.OBJETO;
			at.tipo.tipoBase = new Tipo();
			at.tipo.tipoBase.clase = s.nombre;
		}
		else
		{
			//ERROR
			Error.throwError2($ID.line, $ID.pos, $ID.text);
		}
		/*at.tipo = new Tipo();
		at.tipo.tipo = Tipo.CLASE;
		at.tipo.ts = new TablaSimbolos();
		at.tipo.clase = $ID.text;*/
	}
	|
	t = tipoSimple
	{
		at.tipo = t.tipo; 
		at.tipoSimple = t.tipo.tipo;
		at.th = t.th;
	}
;

params[String h, String laclase, int row, int column, int rowPari, int columnPari] returns [Atributos at]
@init{
   at = new Atributos();
   String prefix = "";
   int contador = 0;
   at.trad = "";
   at.tipo = new Tipo();
   at.th = "";
   at.trad = "";
}:
	(
		e = expr
		{
			
			
			at.trad = e.trad;
			
			if(e.tipo.tipo == Tipo.REAL || e.tipo.tipo == Tipo.ARGUMENTO)
			{
			}
			else if(e.tipo.tipo == Tipo.ENTERO)
			{
				at.trad += "conv.r8\n";
			}
			else
			{
				Error.throwError24(rowPari, columnPari);
			}
			contador++;
		}
	(
	COMA
	 e2 = expr
	 {
	 	
			
	 	at.trad += e2.trad;
	 	
	 	if(e2.tipo.tipo == Tipo.REAL || e2.tipo.tipo == Tipo.ARGUMENTO)
		{
		}
		else if(e2.tipo.tipo == Tipo.ENTERO)
		{
			at.trad += "conv.r8\n";
		}
		else
		{
			Error.throwError24($COMA.line, $COMA.pos);
		}
	 	contador++;
	 	
	 	
	 }
	)*
	)?
	
	{
		Simbolo laClase;
		at.tamano = contador;
		if(laclase.equals(""))
		{
			laClase = ts.busca(claseActual);
		}
		else
		{
			laClase = ts.busca(laclase);
		}
		
		Simbolo elMetodo = laClase.tipo.ts.busca(h);
		
		if(elMetodo != null)
		{
			if(!h.equals(laclase) || at.tamano != 0)
			{
				if(!elMetodo.tamanos.contains(at.tamano))
				{
					Error.throwError21(row, column, elMetodo, at.tamano);
				}
				else
				{
					int elRetorno = elMetodo.retornos.get(elMetodo.tamanos.indexOf(at.tamano));
					at.tipo.tipo = elRetorno;
					if(elRetorno == Tipo.ENTERO || elRetorno == Tipo.BOOLEAN)
					{
						at.th = "int32 ";
					}
					else
					{
						at.th = "float64 ";
					}
				}
			}
			else
			{
				//System.err.println("Error, no existe");
			}
		}
		else
		{
			//System.err.println("Error, no existe");
		}

	}
;

subref[boolean declarado] returns [Atributos at]
@init{
   at = new Atributos();
   at.tipo = new Tipo();
   at.tipo.tipoBase = new Tipo();
   at.tipo.tipoBase.clase = "";
   at.isCampo = false;
   at.isOtra = false;
   String prefix = "";
   String claseAcceso = "";
   boolean primera = true;
   String anterior = "";
}:
	id1 = ID
	{
		Simbolo s = ts.busca($id1.text);
		at.nombre = $id1.text;
		at.row = $id1.line;
		at.column = $id1.pos;
		at.prefix = "";
		if(s != null)
		{
			
			
			//at.tipo = s.tipo;
			at.tipo = new Tipo(s.tipo);
			at.tipo.tamano = s.tipo.tamano;
			at.tipoSimple = s.tipo.tipo;
			at.tipo.tipo = s.tipo.tipo;
			if(s.tipo.tipoBase != null)
			{
				if(at.tipo.tipoBase == null)
				{
					at.tipo.tipoBase = new Tipo();
				}
				
				at.tipo.tipoBase.clase = s.tipo.tipoBase.clase;
				at.tipo.tipoBase.tipo = s.tipo.tipoBase.tipo;
				at.tipo.tipoBase.tamano = s.tipo.tipoBase.tamano;
				//at.tipo = s.tipo;
			}
			else
			{
				if(at.tipo.tipoBase == null)
				{
					at.tipo.tipoBase = new Tipo();
				}
				at.tipo.tipoBase.clase = s.tipo.clase;
				s.tipo.tipoBase = new Tipo();
				s.tipo.tipoBase.clase = s.tipo.clase;
			}
			
			claseAcceso = s.tipo.tipoBase.clase;
			
			if(claseAcceso.equals($id1.text))
			{
				if(inMain)
				{
					Error.throwError27($id1.line, $id1.pos, $id1.text);
				}
			}
			
			/*if(s.tipo.tipo == Tipo.CLASE)
			{
				Error.throwError30($id1.line, $id1.pos);
			}*/
			
			if(s.tipo.tipo == Tipo.CLASE)
			{
				at.tipo.tipo = Tipo.METODO;
				at.prefix = "";
				at.sufix = "newobj instance void 'A'::.ctor";
				at.interfix = "";
				at.trad = "";
			}
			else
			{
				prefix = "ldarg 0\nldfld ";
				if(s.posicion == -1)
				{
					
						
					if(inMain)
					{
						Error.throwError27($id1.line, $id1.pos, $id1.text);
					}
					at.isCampo = true;
					if(at.tipo.tipo ==  Tipo.ARRAY)
					{
						if(at.tipo.getTipoBase() == Tipo.ENTERO || at.tipo.getTipoBase() == Tipo.BOOLEAN)
						{
							at.sufix = at.trad;
							at.trad = "int32[] '" + s.tipo.clase + "'::'" + s.nombre + "'\n"; 
						}
						else if(at.tipo.getTipoBase() == Tipo.REAL)
						{
							at.sufix = at.trad;
							at.trad = "float64[] '" + s.tipo.clase + "'::'" + s.nombre + "'\n"; 
						}
					}
					else if(at.tipo.tipo == Tipo.METODO)
					{
						
						at.row = $id1.line;
						at.column = $id1.pos;
						String retorno = "";
						if(s.tipo.retorno == Tipo.ENTERO || s.tipo.retorno == Tipo.BOOLEAN)
						{
							retorno = "int32";
						}
						else if(s.tipo.retorno == Tipo.REAL)
						{
							retorno = "float64";
						}
						prefix = "ldarg 0\n";
						at.prefix = "ldarg 0\n";
						at.interfix = "call instance ";
						at.sufix = " '" + s.tipo.clase + "'::'" + s.nombre + "'";
						at.trad = at.prefix + at.interfix + at.sufix;
						//at.tipo = s.tipo;
						at.tipo.tipo = Tipo.METODO;
						at.tipoSimple = s.tipo.retorno;
						
						
						
					}
					else
					{
						if(at.tipo.tipo == Tipo.ENTERO || at.tipo.tipo == Tipo.BOOLEAN)
						{
							at.trad = "int32 '" + s.tipo.clase + "'::'" + s.nombre + "'\n"; 
						}
						else if(at.tipo.tipo == Tipo.REAL)
						{
							at.trad = "float64 '" + s.tipo.clase + "'::'" + s.nombre + "'\n"; 
						}
						else if(at.tipo.tipo == Tipo.OBJETO)
						{
							at.sufix = s.tipo.tipoBase.clase + " '" + s.tipo.clase + "'::'" + s.nombre + "'\n"; 
							at.trad = s.tipo.tipoBase.clase + " '" + s.tipo.clase + "'::'" + s.nombre + "'\n"; 
						}
						else if(s.tipo.tipo == Tipo.CLASE)
						{
							at.tipoSimple = Tipo.CLASE;
						}
						if(at.tipo.tipo ==  Tipo.ARRAY)
						{
							if(at.tipo.getTipoBase() == Tipo.ENTERO || at.tipo.getTipoBase() == Tipo.BOOLEAN)
							{
								at.sufix = at.trad;
								at.trad = "int32[] '" + s.tipo.clase + "'::'" + s.nombre + "'\n"; 
							}
							else if(at.tipo.getTipoBase() == Tipo.REAL)
							{
								at.sufix = at.trad;
								at.trad = "float64[] '" + s.tipo.clase + "'::'" + s.nombre + "'\n"; 
							}
						}
					}
				}
				else
				{
					if(at.tipo.tipo == Tipo.ARGUMENTO)
					{
						prefix = "ldarg ";
						at.trad = s.posicion + "\n";
						at.tipoSimple = Tipo.REAL;
					}
					else
					{
						prefix = "ldloc ";
						at.trad = s.posicion + "\n";
						at.sufix = s.posicion + "\n";
					}
				}	
			}
		}
		else
		{
			if(declarado)
			{
				Error.throwError2($id1.line, $id1.pos, $id1.text);
			}
			at.nombre = $id1.text;
		}
		
		//at.sufix = at.trad;
		
		
	}
	(
	{
		if(at.tipo.tipo != Tipo.OBJETO)
		{
			//ERROR.
			
		}
		
		if(primera)
		{
			at.sufix =  at.trad;
			at.prefix = prefix;
			primera = false;
		}
		
		
	}
	PUNTO
	id2 = ID
	{
		
		Simbolo laClase = ts.busca(claseAcceso);
		at.nombre = $id2.text;
		at.row = $id2.line;
		at.column = $id2.pos;
		
		if(laClase != null)
		{	
			Simbolo s2 = laClase.tipo.ts.busca($id2.text);
			if(s2 != null)
			{
				
				if(s2.nombre.equals(laClase.nombre))
				{
					Error.throwError30($id2.line, $id2.pos);
				}
				at.tipo = new Tipo(s2.tipo);
				at.tipo.tamano = s2.tipo.tamano;
				at.tipoSimple = s2.tipo.tipo;
				at.tipo.tipo = s2.tipo.tipo;
				//at.tipo = s2.tipo;
				if(s2.tipo.tipoBase != null)
				{
					if(at.tipo.tipoBase == null)
					{
						at.tipo.tipoBase = new Tipo();
					}
					
					at.tipo.tipoBase.clase = s2.tipo.tipoBase.clase;
					at.tipo.tipoBase.tipo = s2.tipo.tipoBase.tipo;
					at.tipo.tipoBase.tamano = s2.tipo.tipoBase.tamano;
					//at.tipo = s2.tipo;
				}
				else
				{
					if(at.tipo.tipoBase == null)
					{
						at.tipo.tipoBase = new Tipo();
					}
					at.tipo.tipoBase.clase = s2.tipo.clase;
					s2.tipo.tipoBase = new Tipo();
					s2.tipo.tipoBase.clase = at.tipo.tipoBase.clase;
				}
				
				if(s2.tipo.tipo == Tipo.METODO)
				{
					
					at.row = $id2.line;
					at.column = $id2.pos;
					String retorno = "";
					if(s2.tipo.retorno == Tipo.ENTERO || s2.tipo.retorno == Tipo.BOOLEAN)
					{
						retorno = "int32";
					}
					else if(s2.tipo.retorno == Tipo.REAL)
					{
						retorno = "float64";
					}
					anterior = at.prefix + at.sufix;
					at.prefix = at.prefix + at.sufix + prefix + "";
					//at.prefix = at.prefix + at.trad + "\n";
					at.interfix = "call instance ";
					at.sufix = " '" + s2.tipo.tipoBase.clase + "'::'" + s2.nombre + "'";
					//at.trad = at.prefix + at.sufix;
					//at.tipo = s2.tipo;
					//at.tipo.tipo = s2.tipo.retorno;
					//at.tipoSimple = s2.tipo.retorno;
					
					/*anterior = at.prefix + at.sufix;
						at.prefix = at.prefix + at.sufix + prefix + "";
						at.sufix = s2.tipo.tipoBase.clase + " '" + claseAcceso + "'::'" + s2.nombre + "'\n"; */
					
					/* ANTERIOR	
					at.prefix = at.prefix + at.trad + "\n";
                    at.interfix = "call instance ";
                    at.sufix = " '" + s2.tipo.tipoBase.clase + "'::'" + s2.nombre + "'";
                    at.trad = at.prefix + at.sufix;
                    //at.tipo = s2.tipo;
                    at.tipo.tipo = s2.tipo.retorno;
                    at.tipoSimple = s2.tipo.retorno;*/
				}
				else
				{
					
					at.isOtra = true;
					
					if(s2.posicion == -1)
					{
						if(s2.visibilidad == Simbolo.PRIVADO)
						{
							if(!s2.tipo.clase.equals(claseActual))
							{
								Error.throwError2($id2.line, $id2.pos, $id2.text);
							}
						}
						
						if(s2.posicion == -1)
						{
							prefix = "ldfld ";
						}
						else
						{
							if(at.tipo.tipo == Tipo.ARGUMENTO)
							{
								prefix = "ldarg ";
								at.tipoSimple = Tipo.REAL;
							}
							else
							{
								prefix = "ldloc ";
							}
						}
						
						//anterior = at.prefix;
						//at.prefix = at.prefix + at.trad;
						//at.prefix = at.prefix + at.sufix + prefix + "";
						//at.sufix = s2.tipo.tipoBase.clase + " '" + claseAcceso + "'::'" + s2.nombre + "'\n";
						
						/*anterior = at.prefix + at.sufix;
						//at.prefix = at.prefix + at.trad;*/
						//at.prefix = at.prefix + prefix + at.trad;
						//at.prefix = "ldloc " + s.posicion + "\n";
						//at.sufix = at.trad;
						//at.sufix = s2.tipo.tipoBase.clase + " '" + claseAcceso + "'::'" + s2.nombre + "'\n";
						//at.trad = at.sufix;
						
						//at.prefix = at.prefix + prefix + at.trad;
						anterior = at.prefix + at.sufix;
						at.prefix = anterior + prefix;
						at.sufix = s2.tipo.tipoBase.clase + " '" + claseAcceso + "'::'" + s2.nombre + "'\n";
						at.trad = at.prefix + at.sufix;
						
						//at.trad = anterior + at.sufix;
						at.isCampo = true;
						
						
						
						
					}
					
					if(s2.tipo.tipo == Tipo.ENTERO || s2.tipo.tipo == Tipo.BOOLEAN)
					{
						at.trad = "int32 '" + claseAcceso + "'::'" + s2.nombre + "'\n"; 
					}
					else if(s2.tipo.tipo == Tipo.REAL)
					{
						at.trad = "float64 '" + claseAcceso + "'::'" + s2.nombre + "'\n"; 
					}
					else if(s2.tipo.tipo ==  Tipo.ARRAY)
					{
						if(s2.tipo.getTipoBase() == Tipo.ENTERO || s2.tipo.getTipoBase() == Tipo.BOOLEAN)
						{
							
							at.prefix = at.prefix + at.trad;
							//at.prefix = "ldloc " + s.posicion + "\n";
							at.sufix = at.trad;
							at.trad = "int32[] '" + claseAcceso + "'::'" + s2.nombre + "'\n"; 
							//at.prefix = at.prefix + at.trad + "\n";
							//at.sufix= "int32[] '" + claseAcceso + "'::'" + s2.nombre + "'\n"; 
						}
						else if(at.tipo.getTipoBase() == Tipo.REAL)
						{
							at.prefix = at.prefix + at.trad;
							//at.prefix = "ldloc " + s.posicion + "\n";
							at.sufix = at.trad;
							at.trad = "float64[] '" + claseAcceso + "'::'" + s2.nombre + "'\n"; 
						}
					}
					else if(s2.tipo.tipo == Tipo.OBJETO)
					{
						//anterior = at.prefix + prefix;
						//at.prefix = at.prefix + at.trad;
						//at.sufix = at.trad;
						//at.trad = at.prefix;
						//at.sufix = s2.tipo.tipoBase.clase + " '" + claseAcceso + "'::'" + s2.nombre + "'\n";
						//at.trad = at.prefix + at.sufix;
						
						//anterior = at.prefix + at.sufix;
						at.prefix = anterior + prefix;
						at.sufix = s2.tipo.tipoBase.clase + " '" + claseAcceso + "'::'" + s2.nombre + "'\n";
						at.trad = at.prefix + at.sufix;
					}
					
				}
			}
			else
			{
				//ERROR NO EXISTE
				Error.throwError2($id2.line, $id2.pos, $id2.text);
			}
			
			
		 	claseAcceso = s2.tipo.tipoBase.clase;
			
			//at.tipo = s2.tipo;
		}
		else
		{
			//System.err.println("NO EXISTE LA CLASE");	
		}
				
		/*Simbolo s2 = ts.busca($id2.text);
		if(s2 != null)
		{
			if(s2.tipo.tipo == Tipo.METODO)
			{
				System.err.println("EXISTE");	
				
				
			}
			
		}
		else
		{
			//ERROR
		 }*/
	}
	)*
	
	{
		if(!anterior.equals(""))
		{
			at.prefix = anterior;
			
			
		}
	}
	

;
/*Especificacion Lexica*/
CLASS	:	'class';
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
RETURN		:	'return';
PRIVATE		:	'private';
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


