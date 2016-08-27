

public class Simbolo {

    public String nombre;
    public int posicion;
    public int tamano;
    public Tipo tipo;
    public boolean indice;

    // Tipos de datos:
    public static final int
      ENTERO  = 1,
      REAL    = 2,
      NULL 	  = 3,
      BOOL	  = 4;

    public Simbolo(String nombre, Tipo tipoSimbolo, int pos) {
        this.nombre = nombre;
        this.tipo = tipoSimbolo;
        this.posicion = pos;
        indice = false;
        tamano = 1;
    }
    
    public Simbolo(String nombre, Tipo tipoSimbolo, int pos, int tam) {
        this.nombre = nombre;
        this.tipo = tipoSimbolo;
        this.posicion = pos;
        this.tamano = tam;
        indice = false;
    }
    
    public String toString()
    {
    	return "N: " + nombre  + " P: " + posicion + " - Tam : " + tamano + " - Tipo: " + tipo.tipo + " - I: " + indice + "\n";
    }

}

/* Un diseÃ±o mejor pasa por crear una clase derivada de Simbolo para 
   cada tipo de sÃ­mbolo: SimboloVariable, SimboloFuncion, etc. */
