
public class Tipo {
	public int tipo;
	public TablaSimbolos ts;
	public int parametro;
	public int retorno;
	public int tamano;
	public Tipo tipoBase;
	public String clase;

	public static final int
	ENTERO     = 1,
	REAL  = 2,
	CLASE   = 3,
	ARRAY = 4,
	PUNTERO = 5,
	METODO = 6,
	BOOLEAN = 7,
	ARGUMENTO = 8,
	OBJETO = 9,
	NONE = -1;
	
	public Tipo()
	{
		ts = null;
		tipoBase = null;
		tamano = retorno = parametro = tipo = 0;
		clase = "";
	}
	
	public Tipo(Tipo t)
	{
		ts = t.ts;
		
		if(t.tipoBase != null)
		{
			tipoBase = new Tipo(t.tipoBase);
		}
		tamano = t.tamano;
		retorno = t.retorno;
		parametro = t.parametro;
		tipo = t.tipo;
		clase = t.clase;
	}
	
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

	boolean isMetodo()
	{
		return tipo == METODO;
	}

	boolean isBoolean()
	{
		return tipo == BOOLEAN;
	}
	
	

}
