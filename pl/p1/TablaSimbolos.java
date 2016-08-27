import java.util.HashMap;

/* Tipo basado en el material de las clases de Terence Parr:
     http://www.cs.usfca.edu/~parrt/course/652/lectures/symtab-solution-3.html 
*/

class TablaSimbolos {
    TablaSimbolos padre = null;
    HashMap<String,Simbolo> simbolos = new HashMap<String,Simbolo>(); 

    public TablaSimbolos(TablaSimbolos padre) {
        this.padre = padre;
    }
    
    public TablaSimbolos() {
    }
    
    public TablaSimbolos pop() { return padre; }

    public Simbolo nuevoSimbolo(String nombre, String traduccion, int tipoSimbolo, int tipo) {
        Simbolo s = new Simbolo(nombre, traduccion, tipoSimbolo, tipo);
        simbolos.put(nombre, s);
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
}

