public class Atributos
{
	public String trad;
	public String decl;
	public Tipo tipo;
	public int tipoSimple;
	public String th;
	public String nombre;
	public boolean isCampo;
	public int tamano;
	public String prefix;
	public String sufix;
	public String interfix;
	public boolean isOtra;
	//public String th1;
	//public String th2;
	//public int tipo;
	public int row;
	public int column;
	public boolean isPublic;

	public Atributos()
	{
		trad = null;
		decl = null;
		tipo = null;
		th = null;
		tipoSimple = Tipo.NONE;
		nombre = null;
		isCampo = false;
		prefix = null;
		sufix = null;
		//th1 = "";
		//th2 = "";
		//tipo = 0;
		/*row = 0;
		column = 0;*/
	}

}
