public class Atributos
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
