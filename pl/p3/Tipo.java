
public class Tipo {
	int tipo;
	TablaSimbolos ts;
	int parametro;
	int retorno;
	int tamano;
	Tipo tipoBase;

	public static final int
	ENTERO     = 1,
	REAL  = 2,
	CLASE   = 3,
	ARRAY = 4,
	PUNTERO = 5,
	RETORNO = 6,
	BOOLEAN = 7,
	NONE = 8;
	
	public int getTipoBase()
	{
		Tipo actual = this;
		
		while(actual.tipoBase != null)
		{
			actual = actual.tipoBase;
		}
		
		return actual.tipo;
	}

	public void copiar(Tipo t)
	{
		tipo = t.tipo;
		ts = t.ts;
		parametro = t.parametro;
		retorno = t.retorno;
		tamano = t.tamano;

		if(t.tipoBase != null)
		{
			if(tipoBase == null)
			{
				tipoBase = new Tipo();
			}
			
			tipoBase.copiar(t.tipoBase);
		}
		else
		{
			tipoBase = null;
		}
	}
	
	public String mostrar()
	{
		String cadena = "Tipo: " + tipo + " TS: " + ts + " Param: " + parametro + " Retorno: " + retorno + " Tamaño: " + tamano;
		
		if(tipoBase != null)
		{
			cadena += "\n\t>" + tipoBase.mostrar() + "\n";
		}
		return cadena;
	}
	
	boolean isArray()
	{
		return tipo == ARRAY;
	}

	boolean isEntero()
	{
		return tipo == ENTERO;
	}

	boolean isReal()
	{
		return tipo == REAL;
	}

	boolean isClase()
	{
		return tipo == CLASE;
	}

	boolean isPuntero()
	{
		return tipo == PUNTERO;
	}

	boolean isRetorno()
	{
		return tipo == RETORNO;
	}

	boolean isBoolean()
	{
		return tipo == BOOLEAN;
	}
	
	

}
