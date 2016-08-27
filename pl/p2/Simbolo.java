

public class Simbolo {

    public String nombre;
    public String traduccion;
    public int tipoSimbolo;
    public int tipo;

    // Clases de si­mbolos:
    public static final int
      VAR     = 1,
      METODO  = 2,
      CLASE   = 3;

    // Tipos de datos:
    public static final int
      ENTERO  = 1,
      REAL    = 2,
      NULL 	  = 3,
      BOOL	  = 4;

    public Simbolo(String nombre, int tipoSimbolo, int tipo) {
        this.nombre = nombre;
        this.tipoSimbolo = tipoSimbolo;
        this.tipo = tipo;
    }
    
    public Simbolo(String nombre, String traduccion, int tipoSimbolo, int tipo) {
        this.nombre = nombre;
        this.tipoSimbolo = tipoSimbolo;
        this.tipo = tipo;
        this.traduccion = traduccion;
    }

}

/* Un diseÃ±o mejor pasa por crear una clase derivada de Simbolo para 
   cada tipo de sÃ­mbolo: SimboloVariable, SimboloFuncion, etc. */
