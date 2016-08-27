/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GatosYRaton;

/**
 *
 * @author mireia
 */
public class Tablero {

    final int tamaño = 8;

    //Datos del tablero
    private int m_tablero[][];


    /**
     * Constructor del tablero
     * @param anchura Anchura del tablero
     * @param altura Altura del tablero
     */
    public Tablero(int tamaño)
    {
        //Crea el tablero
        m_tablero = new int[tamaño][tamaño];

        //Inicializa el tablero
        //En primer lugar vacía todo el tablero
        for (int i=0; i<tamaño; i++)
        {
            for(int j=0; j<tamaño; j++)
                m_tablero[i][j] = 0;
        }
        
        //Dibuja el ratón en la primera fila
        m_tablero[4][0] = 2;

        //Dibuja los gatos en la última fila
        for(int i=0; i<4; i++){
            m_tablero[i*2+1][7]=1;
        }

    }

    /**
     * Constsructor de Copia del tablero
     * @param original Tablero del cual realizar la copia
     */
    public Tablero (Tablero original)
    {
        //Crea el tablero
        m_tablero = new int[tamaño][tamaño];

        //Inicializa el tablero
        for (int i=0; i<tamaño; i++)
        {
            for(int j=0; j<tamaño; j++)
                m_tablero[i][j] = original.m_tablero[i][j];
        }
    }

    /**
     * Devuelve el tamaño
     */
    public int tamaño()
    {
        return tamaño;

    }

    /**
     * Indica si existe una ficha en la posición indicada.
     */
    public boolean existeFicha(int i, int j)
    {
        if(m_tablero[i][j] != 0)
            return true;
        else
            return false;
    }

    /**
     * Devuelve a quién pertenece la casilla especificada.
     * 0 está vacía
     * 1 pertenece al jugador 1 (blancas)
     * 2 pertenece al jugador 2 (negras)
     */
    public int obtenerCasilla(int i, int j)
    {
        if((i >= 0) && (i<8) && (j>=0) && (j<8))
            return m_tablero[i][j];
        else
            return -1;
    }

    /**
     * Cambiar valor casilla
     */
    public void cambiarCasilla(int i, int j, int valor)
    {
       if(i>=0 && i<tamaño && j>=0 && j<tamaño)
        m_tablero[i][j] = valor;
    }


    //Limpia el tablero. Deja todas las casillas vacías.
    public void limpiarTablero()
    {
        for(int i=0; i<tamaño; i++)
            for(int j=0; j<tamaño;j++)
                m_tablero[i][j] = 0;
    }
    
    public void inicializarTablero()
    {
        //En primer lugar vacía todo el tablero
        for (int i=0; i<tamaño; i++)
        {
            for(int j=0; j<tamaño; j++)
                m_tablero[i][j] = 0;
        }
        
        //Dibuja el ratón en la primera fila
        m_tablero[4][0] = 2;

        //Dibuja los gatos en la última fila
        for(int i=0; i<4; i++){
            m_tablero[i*2+1][7]=1;
        }
    }

    /**
     * Comprueba si existen movimientos posibles para un jugador.
     * @param jugador Jugador que tiene el turno para el que comprueba si existen movimientos.
     * @return movimiento True si quedan movimientos posibles.
     */
    public boolean quedanMovimientos(int jugador)
    {
        int ficha;
        boolean movimiento;
        movimiento = false;

        //Recorre el tablero mientras no haya movimiento
        for(int i=0; i<tamaño() && !movimiento; i++)
            for(int j=0; j<tamaño() && !movimiento;j++)
            {
                ficha = obtenerCasilla(j,i);
                //Si existe una ficha en la casilla
               if(ficha != 0)
               {
                   //Si la ficha es del jugador
                   if(ficha == jugador)
                   {   //Si hay movimiento posible
                       if( movimientoPosible(j, i, ficha) != -1)
                           movimiento = true;
                   }
               }
            }
        return movimiento;
    }

    /**
     *Calcula si la ficha que ocupa la columna y fila indicada tiene algún
     * movimiento posible.
     */
    public int movimientoPosible(int columna, int fila, int jugador)
    {
        int contrario;

        if(jugador ==1)
            contrario = 2;
        else
            contrario =1;

        //Comprueba que la ficha pertenece al jugador que le toca jugar
        if(obtenerCasilla(columna, fila) == jugador)
        {
            //Si la ficha pertenece al jugador, comprueba si puede hacer alguna jugada
            //Si es una ficha gato, la ficha puede mover para arriba en diagonal
            if(jugador == 1)
            {
                //Si es la primera fila ya no tiene movimiento
                if(fila == 0)
                    return -1;

                //Si está en la primera columna, solo puede mover hacia delante derecha
                if((columna == 0) && (fila > 0))
                    if(obtenerCasilla(columna+1, fila-1) == 0)
                        return 1;

                //Si está en la última columna, solo puede mover hacia delante izquierda
                 if((columna == tamaño()-1) && (fila > 0))
                    if(obtenerCasilla(columna-1, fila-1)== 0)
                        return 1;

                //si está en cualquier columna intermedia puede mover hacia delante izquierda o derecha
                if(columna>0 && columna<tamaño()-1 && fila > 0)
                    if((obtenerCasilla(columna-1, fila-1)== 0) || (obtenerCasilla(columna+1, fila-1)== 0))
                        return 1;

            }

            // Si es una ficha ratón, puede mover hacia arriba y hacia bajo en diagonal
            if(jugador == 2)
            {
                //Si está en la primera fila, solo puede mover hacia bajo
                if(fila==0){
                    //Si está en la primera columna, solo puede mover hacia bajo derecha
                    if(columna==0){
                        if(obtenerCasilla(columna+1, fila+1)==0)
                            return 1;
                    }else{ //Sino, puede mover izquierda o derecha
                        if((obtenerCasilla(columna+1, fila+1)==0)|| (obtenerCasilla(columna-1, fila+1)==0))
                            return 1;
                    }
                }

                //Si está en la última fila, solo puede mover hacia atrás
                if (fila == tamaño()-1){
                    //Si está en la última columna, solo puede mover hacia atrás izquierda
                    if(columna==tamaño()-1){
                        if(obtenerCasilla(columna-1, fila-1)==0)
                            return 1;
                    }else{
                        if((obtenerCasilla(columna+1, fila-1)==0)|| (obtenerCasilla(columna-1, fila-1)==0))
                            return 1;
                    }
                }

                //Si está en la primera columna, puede mover hacia derecha delante o atrás
                if((columna==0) && (fila>0)){
                    if((obtenerCasilla(columna+1, fila-1)==0)|| (obtenerCasilla(columna+1, fila+1)==0))
                            return 1;
                }

                //Si está en la última columna, puede mover hacia izquierda delante o atrás
                if((columna==tamaño()-1) && (fila>0)){
                    if((obtenerCasilla(columna-1, fila-1)==0)|| (obtenerCasilla(columna-1, fila+1)==0))
                            return 1;
                }

                //Si está en cualquier otra posición
                if(columna>0 && columna<tamaño()-1 && fila > 0 && fila<tamaño()-1){
                    if((obtenerCasilla(columna-1, fila-1)==0)|| (obtenerCasilla(columna-1, fila+1)==0)|| (obtenerCasilla(columna+1, fila+1)==0)|| (obtenerCasilla(columna+1, fila-1)==0))
                            return 1;
                }
            }
        }
        return -1;
    }

     /**
     * Comprueba si es una tirada válida.
     * @param filAnt Fila donde se encuentra la ficha
     * @param colAnt Columna donde se encuentra la ficha
     * @param columna Columna donde se desea mover
     * @param fila Fila donde se desea mover
     * @param jugador Jugador que tiene el turno
     */
    public int movimientoValido(int filAnt, int colAnt, int fila, int columna, int jugador)
    {
        int contrario;

        if(jugador ==1)
            contrario = 2;
        else
            contrario = 1;

             //Si la casilla a la que se intenta mover está ocupada
             if(obtenerCasilla(columna, fila) != 0)
                return -1;

            //Si es una ficha gato, la ficha puede mover para arriba en diagonal
            if(jugador == 1)
            {
                //Si está en la primera fila ya no tiene movimientos
                if (filAnt == 0)
                    return -1;

                //Si está en la primera columna
                if(colAnt == 0)
                {
                    if(fila == filAnt-1 && columna == colAnt+1)
                        return 1;
                }

                //Si está en la última columna
                if(colAnt == tamaño()-1)
                {
                     if(fila == filAnt-1 && columna == colAnt-1)
                        return 1;
                }

                //Si está en columnas y filas intermedias
                if(colAnt>0 && colAnt<tamaño()-1 && filAnt >0)
                {
                     if((fila == filAnt-1) && (columna == colAnt+1 || columna == colAnt-1))
                        return 1;
                }
            }

            // Si es la ficha ratón, puede mover hacia arriba y bajo en diagonal
            if(jugador == 2)
            {
                //Si está en la primera fila solo puede mover hacia abajo.
                if(filAnt == 0){
                    //Si está en la primera columna solo hacia la derecha
                    if(colAnt == 0){
                        if(fila == filAnt+1 && columna == colAnt+1)
                            return 1;
                    }else{//Sino, a derecha e izquierda
                        if(fila == filAnt+1 && (columna == colAnt+1 || columna == colAnt-1))
                            return 1;
                    }
                }

                //Si está en la última fila solo puede mover hacia arriba.
                if(filAnt == tamaño()-1){
                    //Si está en la última columna solo hacia la izquierda
                    if(colAnt == tamaño()-1){
                        if(fila == filAnt-1 && columna == colAnt-1)
                            return 1;
                    }else{//Sino, a derecha e izquierda
                        if(fila == filAnt-1 && (columna == colAnt+1 || columna == colAnt-1))
                            return 1;
                    }
                }

                //Si está en la primera columna
                if(colAnt == 0)
                {
                    if((fila == filAnt+1 || fila == filAnt-1) && columna == colAnt+1)
                        return 1;
                }

                //Si está en la última columna
                if(colAnt == tamaño()-1)
                {
                     if((fila == filAnt+1 || fila == filAnt-1) && columna == colAnt-1)
                        return 1;
                }

                //Si está en columnas o filas intermedias
                if(colAnt>0 && colAnt<tamaño()-1 && filAnt>0 && filAnt<tamaño()-1)
                {
                     if((fila == filAnt+1 || fila == filAnt-1) && (columna == colAnt+1 || columna == colAnt-1))
                        return 1;
                }
            }
        return -1;
    }

    /**
     * Mueve las fichas a partir de una posición actual.
     * @param filAnt
     * @param colAnt
     * @param columna
     * @param fila
     * @param jugador
     * @return
     */
    public int hacerTirada(Movimiento movimiento, int jugador)
    {
        int valor;
        valor = -1;
        //Posición actual de la ficha
        int filAnt = 0;
        int colAnt= 0;
        //Posición donde se desea poner la ficha
        int fila=0;
        int columna=0;

        filAnt = movimiento.getInicial().getX();
        colAnt = movimiento.getInicial().getY();
        fila = movimiento.getFinal().getX();
        columna = movimiento.getFinal().getY();


        //Calcula si el movimiento es posible
        if (movimientoValido(filAnt, colAnt, fila, columna, jugador) == 1)
        {
            //Si el movimiento es posible
            //Borra la ficha anterior
            cambiarCasilla(colAnt, filAnt, 0);

            //Coloca la ficha en la nueva posición
            cambiarCasilla(columna, fila, jugador);

            valor = 0;
        }
        return valor;
    }
}
