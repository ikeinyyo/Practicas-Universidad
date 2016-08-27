import java.util.HashMap;

/* Tipo basado en el material de las clases de Terence Parr:
     http://www.cs.usfca.edu/~parrt/course/652/lectures/symtab-solution-3.html 
*/

class TablaSimbolos {
    TablaSimbolos padre = null;
    HashMap<String,Simbolo> simbolos = new HashMap<String,Simbolo>(); 
    static int pos;
    static int etiq;

    public TablaSimbolos(TablaSimbolos padre) {
        this.padre = padre;
        pos = padre.pos;
        etiq = padre.etiq;
    }
    
    public TablaSimbolos() {
    	pos = 0;
    	etiq = 0;
    }
    
    public TablaSimbolos pop() { return padre; }

    public Simbolo nuevoSimbolo(String nombre, Tipo tipoSimbolo, int pos, int tamano) {
        Simbolo s = new Simbolo(nombre, tipoSimbolo, pos, tamano);
        simbolos.put(nombre, s);
        return s;
    }
    
    public Simbolo nuevoSimbolo(Simbolo s) {
        simbolos.put(s.nombre, s);
        return s;
    }

    public Simbolo busca(String nombre) {
        Simbolo s = simbolos.get(nombre);
        if ( s!=null ) {
            return s;  // encontrado
        }
	// si no lo encuentra, busca en el Ã¡mbito exterior
        if ( padre!=null ) {
            return padre.busca(nombre);
        }
        return null; // no se encontrÃ³
    }

    public boolean existe(String nombre) {
        return simbolos.get(nombre)!=null;
    }
    
    
    public String toString()
    {
    	String cadena = "Nombre |  Posición | Tamaño | Tipo | Índice\n";
    	return cadena + simbolos.toString();
    }
    
    public static int getPos()
	{
		int posicion = pos;
		pos++;
		return posicion;
	}
    
    public static String getEtiqueta()
	{
		int etiqueta = etiq;
		etiq++;
		return "L" + etiqueta;
	}
}

