/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GatosYRaton;

/**
 *
 * @author mireia
 */
public class Juego {

    //Crea el tablero de juego.
    public Tablero m_tablero;

    //Para saber si estamos jugando.
    public boolean m_jugando = false;

    //Mensaje que se mostrará en el juego:
    //1. Turno del jugador X.
    //2. Ha ganado el jugador X.
    //3. Empate.
    //4. Error. Colocación de ficha incorrecta.
    public int m_mensaje;

    //Para saber a quién le toca el turno, 1 blancas, 2 negra
    private int m_turno;

    //Tiempo máximo de respuesta del ordenador
    private int m_tiempoMaximo;

    //Los jugadores máquina
    private JugadorMaquina m_maquina1, m_maquina2;

    //Indica el modo de juego.
    //1. Juega con negra.
    //2. Juega con blancas.
    //3. Máquina contra máquina.
    private int m_modoJuego;


    /**
     * Constructor del juego.
     * Crea el tablero, especifica el turno de juego (si empieza a jugar el jugador 1 o 2), si los jugadores son humanos o la máquina.
     */
    public Juego(int tamaño, int tiempoMaximo, int modoJ, int turnoJugador)
    {
        m_tablero = new Tablero(tamaño);
        m_tiempoMaximo = tiempoMaximo;
        m_turno = turnoJugador;
        m_modoJuego = modoJ;
        m_jugando = false;
    }

    /*************************************************************
    //Funciones de acceso a las variables de juego
    *************************************************************/
    //Devuelve si estamos jugando
    public boolean getJugando()
    {
        return m_jugando;
    }

    //Devuelve a quién le toca turno
    public int getTurno()
    {
        return m_turno;
    }

    //Devuelve el modo de juego
    public int getModo()
    {
        return m_modoJuego;
    }


    /**
     * Inicializa el juego. Guarda el modo de juego y crea los jugadores máquina.
     * @param modoJ 
     */
    public void setJuego(int modoJ)
    {
        //Guarda el modo de juego
        m_modoJuego = modoJ;

        //Si el modo de juego es Jugar con negra, crea una máquina que jugará como jugador 1 (blancas)
        if(m_modoJuego == 1)
            m_maquina1 = new JugadorMaquina(1);
        
        //Si el modo de juego es Jugar con blancas, crea una máquina que jugará como jugador 2 (negra).
        if(m_modoJuego == 2)
           m_maquina1 = new JugadorMaquina(2);

        //Si el modo de juego es Maquina contra máquina, crea 2 máquinas, una juega como jugador 1 (blancas) y otra como 2 (negra).
        //Para esto se utiliza el parámetro.
        if(m_modoJuego == 3)
        {
            m_maquina1 = new JugadorMaquina(1);
            m_maquina2 = new JugadorMaquina(2);
        }

        //Indica que ya empieza el juego.
        m_jugando = true;
        //Indica que se muestre el mensaje de turno.
        m_mensaje = 1;

    }


    /**
     * Controla el juego de los jugadores máquina.
     */
    public void controlJuego()
    {
        int resultado;
        
            //Si el modo de juego es Jugar con negra
            if(m_modoJuego == 1)
            {
                //Si le toca el turno a la máquina.
                if(m_turno==1)
                {
                    //Realiza la jugada la máquina.
                    resultado = jugadaMaquina(m_maquina1);
                    //Si el juego no ha terminado cambia de turno.
                    if(m_jugando)
                        cambiaTurno();
                }
            }

            //Si el modo de juego es Jugar con blancas
            if(m_modoJuego == 2)
            {
                //Si le toca el turno a la máquina.
                if(m_turno==2)
                {
                    //Realiza la jugada la máquina.
                    resultado = jugadaMaquina(m_maquina1);
                    //Si el juego no ha terminado cambia de turno.
                    if(m_jugando)
                        cambiaTurno();
                }
            }

            //Si el modo de juego es máquina contra máquina.
            if(m_modoJuego == 3)
            {
                    //Si le toca el turno a la máquina que juega como jugador 1 (blancas).
                    if(m_turno==1)
                    {
                        resultado = jugadaMaquina(m_maquina1);
                    }
                    
                    //Si le toca el turno a la máquina que juega como jugador 2 (negras).
                    if(m_turno==2)
                    {
                        resultado = jugadaMaquina(m_maquina2);
                    }

                    if(m_jugando)
                            cambiaTurno();
            }
    }

    /**
     * El jugador humano realiza una tirada si es su turno, y se puede colocar la ficha en la columna especificada.
     * Devuelve 0 si todo ha ido correctamente.
     * @param filAnt Fila en la que estaba la ficha
     * @param colAnt  Columna en la que estaba la ficha
     * @param columna Columna donde se desea mover
     * @param fila Fila donde se desea mover
     * @param jugador Indica si el humano juega como jugador 1 (blancas) o como jugador 2 (negras)
     * @return
     */
    public int jugadaHumano(int filAnt, int colAnt, int columna, int fila, int jugador)
    {
        int resultado = 0;
        int contrario;
        int fin;
        
        if(jugador == 1)
            contrario = 2;
        else
            contrario = 1;
        
        //Si le toca su turno.
        if (m_turno == jugador)
        {
            //Introduce la ficha en el tablero.
             m_tablero.cambiarCasilla(columna, fila,  jugador);

            //Comprobar si ha terminado la partida
            fin = finPartida();
            if(fin!=0)
            {
                 //Si ha terminado la partida muestra mensaje de fin
                m_jugando = false;
                if (fin == 1)
                    m_mensaje = 2;
                else
                    if (fin == 2)
                        m_mensaje = 3;
                    else
                        if (fin == 3)
                            m_mensaje = 6;
            }
        }
        return resultado;
    }


    /**
     * Crea un thread donde se ejecutará JugadorMaquina para que calcule la jugada.
     * @param jugador JugadorMaquina que está jugando.
     * @return Si todo ha ido correctamente devuelve 0.
     */
    public int jugadaMaquina(JugadorMaquina jugador)
    {
        int resultado = 0;
        int fin;
        Movimiento movimiento;

        //Si es el turno del jugador.
        if(m_turno == jugador.m_jugador)
        {
            //Se duplica el tablero para que no se pueda modificar desde la clase JugadorMaquina.
            jugador.isDone(false);
            jugador.asignarTablero(m_tablero);

            //Se crea e inicia el thread para que el jugador máquina calcule la jugada.
            Thread myThread = new Thread(jugador);
            myThread.start();

            //Se espera al thread como mucho el tiempo específicado en m_tiempoMaximo.
            long timeStart = System.currentTimeMillis();
            long elapsed = 0;

            while(!jugador.isDone() && (elapsed < m_tiempoMaximo))
            {
                elapsed = System.currentTimeMillis() - timeStart;
            }

            //Se obtiene la jugada obtenida por JugadorMaquina
            movimiento = jugador.m_movimiento;

            //Si no ha finalizado el thread se finaliza
            myThread = null;

            //Introduce la ficha en el tablero
              resultado = m_tablero.hacerTirada(movimiento, jugador.m_jugador);

              //Si la colocación de la ficha ha sido correcta
                 if(resultado != -1)
                 {
                      //Comprobar si ha terminado la partida
                        fin = finPartida();
                        if(fin!=0)
                        {
                            //Si ha terminado la partida muestra mensaje de fin
                            m_jugando = false;
                            if (fin == 1)
                                m_mensaje = 2;
                            else
                                if (fin == 2)
                                    m_mensaje = 3;
                                else
                                    if (fin == 3)
                                        m_mensaje = 6;
                        }
                 }
                 else //Si la ficha se coloca incorrectamente el juego termina.
                     {
                        m_mensaje = 4;
                        m_jugando = false;
                     }
        }
        return resultado;
    }

    /**
     * Comprueba si una ficha seleccionada puede hacer una tirada válida.
     * @param columna Columna donde se encuentra la ficha
     * @param fila Fila donde se encuentra la ficha
     * @param jugador Jugador que tiene el turno
     */
    public int posibleMovimiento(int columna, int fila, int jugador)
    {
        int contrario, resultado;
        
        if(jugador ==1)
            contrario = 2;
        else
            contrario =1;

        resultado = -1;
        //Comprueba que la ficha pertenece al jugador que le toca jugar
        if(m_tablero.obtenerCasilla(columna, fila) == jugador)
        {
            resultado = m_tablero.movimientoPosible(columna, fila, jugador);

        }    
        return resultado;
    }


     /**
     * Comprueba si es una tirada válida.
     * @param filAnt Fila donde se encuentra la ficha
     * @param colAnt Columna donde se encuentra la ficha
     * @param columna Columna donde se desea mover
     * @param fila Fila donde se desea mover
     * @param jugador Jugador que tiene el turno
     */
    public int MovimientoBueno(int filAnt, int colAnt, int columna, int fila, int jugador)
    {
        int contrario, resultado;
        
        if(jugador ==1)
            contrario = 2;
        else
            contrario = 1;

        resultado = -1;

        resultado = m_tablero.movimientoValido(filAnt, colAnt, fila, columna, jugador);
        
        return resultado;
    }
    
    /**
     * Cambia el turno de jugador. Se utiliza después de cada jugada.
     */
    public void cambiaTurno()
    {
        if(m_turno == 1)
            m_turno = 2;
        else
            m_turno = 1;
        m_mensaje = 1;
    }


    /**
     * Reinicializa todas las variables para empezar un nuevo juego.
     */
    public void reiniciarJuego()
    {
        m_tablero.inicializarTablero();
        m_turno = 1;
        m_mensaje = 0;
        m_jugando = false;
    }

    /**
     * Comprueba si la partida ha terminado.
     * @return 0 La partida no ha terminado
     * @return 1 La partida ha terminado y han ganado las blancas
     * @return 2 La partida ha terminado y han ganado las negras
     * @return 3 La partida ha terminado y ha habido empate
     */
    public int finPartida()
    {
        int blancas;
        int negras;
        int fin;
        int ficha;
        fin = 0;
        blancas = 0;
        negras = 0;

        //Si el ratón ha llegado a la última fila, gana la partida
        for(int i=0; i<m_tablero.tamaño(); i++)
            if(m_tablero.obtenerCasilla(i,m_tablero.tamaño()-1) == 2)
                fin = 2;

        //Si el ratón se ha quedado sin movimientos ganan las blancas
        for(int i=0; i<m_tablero.tamaño(); i++)
            for(int j=0; j<m_tablero.tamaño();j++)
            {
                ficha = m_tablero.obtenerCasilla(j,i);
                //Si existe una ficha en la casilla
               if(ficha == 2)
               {
                   if( posibleMovimiento(j, i, ficha) != -1)
                           negras++; //Tiene algún movimiento
               }

            }

        if(negras==0)
            fin = 1;
        
        return fin; 
    }

}

   