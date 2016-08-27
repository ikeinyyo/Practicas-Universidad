/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package GatosYRaton;

import java.util.*;
/**
 *
 * @author mireia
 */
public class JugadorMaquina extends Jugador{

    //Profundidad hasta la que se va a desarrollar el árbol de juego
    public final static int NIVEL_DEFECTO = 8;//8
    
    //Profundidad hasta la que se va a desarrollar el árbol de juego
    public final static int BLANCAS = 1;
    
    //Profundidad hasta la que se va a desarrollar el árbol de juego
    public final static int NEGRAS = 2;

    //Constructor
    public JugadorMaquina(int jugador)
    {
        super(jugador);
        m_movimiento = new Movimiento();
    }

    // Función que se ejecuta en el thread
    public void run()
    {
        //Llama a la función alfaBeta() que implementa el algoritmo para calcular la jugada
        alfaBeta();
        
        //No borrar esta línea!!
        isDone(true);
    }


    /**
     * Se debe determinar la mejor jugada mediante AlfaBeta. El tablero de juego se
     * encuentra en la variable m_tablero.
     * Al final de la función de la variable m_movimiento debe contener la tirada.
     * @return
     */
    public void alfaBeta()
    {
        Movimiento movimiento = new Movimiento();
        int alfa = -9999;
        int beta = 9999;
        
        Nodo nodo = new Nodo(m_tablero, alfa, beta, 0, null, m_jugador);
        
        alfabeta(nodo);
        
        m_movimiento = nodo.movimiento;
        
        //System.out.println("Alfa: " + nodo.alfa);

    }
    
    public int alfabeta(Nodo nodo)
    {
        int aux;
        
        if(nodo.isNodoHoja())
        {
            return nodo.evaluarNodo(m_jugador); //Función de evaluación
        }
        else
        {
            ArrayList<Movimiento> posibilidades = nodo.getMovimientos();
            Nodo hijo;
            
            
            if(nodo.isMAX())
            {
                
                for(int i = 0; i < posibilidades.size(); i++)
                {
                    Movimiento mov = (Movimiento)(posibilidades.get(i));
                    
                    hijo = new Nodo(nodo.tablero, nodo.alfa, nodo.beta, nodo.profundidad + 1, mov, nodo.getContrario());
                    
                    aux = alfabeta(hijo);
                    
                    if(aux > nodo.alfa)
                    {
                        //nodo.alfa = aux;
                        
                        if(nodo.profundidad == 0)
                        {   
                            if(nodo.movimiento == null)
                            {
                                nodo.movimiento = new Movimiento(mov);
                            }
                            else
                            {
                                if(aux == nodo.alfa)
                                {
                                    Random random = new Random();

                                    if(random.nextBoolean())
                                    {
                                        nodo.movimiento = new Movimiento(mov);
                                    }
                                }
                                else
                                {
                                   nodo.movimiento = new Movimiento(mov); 
                                }
                            }
                        }
                        
                        nodo.alfa = aux;
                    }
                    
                    if(nodo.alfa >= nodo.beta)
                    {
                        return nodo.beta;
                    }
                }
                
                return nodo.alfa; 
            }
            else
            {
                
                for(int i = 0; i < posibilidades.size(); i++)
                {
                    Movimiento mov = (Movimiento)(posibilidades.get(i));
                    
                    hijo = new Nodo(nodo.tablero, nodo.alfa, nodo.beta, nodo.profundidad + 1, mov, nodo.getContrario());
                    nodo.beta = Math.min(nodo.beta, alfabeta(hijo));
                    
                    if(nodo.alfa >= nodo.beta)
                    {
                        return nodo.alfa;
                    }
                }
                
                return nodo.beta; 
            }
        }
    }

}
//Clase Nodo: Contiene toda la información sobre una jugada.
//Esta clase evalúa la jugada.
class Nodo
{
    Tablero tablero;
    int alfa;
    int beta;
    int profundidad;
    Movimiento movimiento;
    int jugador;
    
    //En el constructor se ejecuta el movimiento sobre el tablero
    public Nodo(Tablero tablero_, int alfa_, int beta_, int profundidad_, Movimiento movimiento_, int jugador_)
    {
        tablero = new Tablero(tablero_);
        alfa = alfa_;
        beta = beta_;
        jugador = jugador_;
        
        profundidad = profundidad_;
        if(movimiento_ != null)
        {
            movimiento = new Movimiento(movimiento_);
        }
        
        //Ejecuto la tirada
        if(profundidad != 0 && movimiento_ != null)
        {
            //Se ejecuta la jugada que hace llegar a este nodo (por eso la ejecuta el contrario).
            tablero.hacerTirada(movimiento, getContrario());
        }//System.out.println("P: " + profundidad + " V: " + evaluarNodo(JugadorMaquina.NEGRAS) + this);
        
    }
    
    public boolean isMAX()
    {
        return ((profundidad % 2) == 0);
    }
    
    public boolean isMIN()
    {
        return ((profundidad % 2) != 0);
    }
    
    public void setAlfa(int alfa_)
    {
        alfa = alfa_;
    }
    
    public void setBeta(int beta_)
    {
        beta = beta_;
    }
    
    public void setProfundidad(int profundidad_)
    {
        profundidad = profundidad_;
    }
    
    public int getAlfa()
    {
        return alfa;
    }
    
    public int getBeta()
    {
        return beta;
    }
    
    public int getProfundidad()
    {
        return profundidad;
    }
    
    public boolean isNodoHoja()
    {
        boolean hoja = false;
        
        if(profundidad >= JugadorMaquina.NIVEL_DEFECTO || !tablero.quedanMovimientos(jugador) || !tablero.quedanMovimientos(getContrario()) || finWin(jugador) || finLose(jugador))
        {
            hoja = true;
        }
        
        return hoja;
    }
    
    //Nos devuelve todos los movimientos posibles de un jugador
    public ArrayList<Movimiento> getMovimientos()
    {
        ArrayList<Movimiento> movimientos = new ArrayList<Movimiento>();
        
        ArrayList<Posicion> fichas = getFichas();
        
        for(int i = 0; i < fichas.size(); i++)
        {
            Posicion actual = (Posicion)(fichas.get(i));
            
            //Comprobar si existe 
            if(jugador == JugadorMaquina.NEGRAS)
            {
                if(tablero.movimientoValido(actual.getX(), actual.getY(), actual.getX() + 1, actual.getY() + 1, jugador) == 1)
                {
                    Movimiento movimiento = new Movimiento();

                    movimiento.m_inicial = new Posicion(actual);

                    movimiento.m_final = new Posicion();
                    movimiento.m_final.setX(actual.getX() + 1);
                    movimiento.m_final.setY(actual.getY() + 1);

                    movimientos.add(movimiento);
                }

                if(tablero.movimientoValido(actual.getX(), actual.getY(), actual.getX() + 1, actual.getY() - 1, jugador) == 1)
                {
                    Movimiento movimiento = new Movimiento();

                    movimiento.m_inicial = new Posicion(actual);

                    movimiento.m_final = new Posicion();
                    movimiento.m_final.setX(actual.getX() + 1);
                    movimiento.m_final.setY(actual.getY() - 1);

                    movimientos.add(movimiento);
                }
                
            }
            
            
            if(tablero.movimientoValido(actual.getX(), actual.getY(), actual.getX() - 1, actual.getY() - 1, jugador) == 1)
            {
                Movimiento movimiento = new Movimiento();

                movimiento.m_inicial = new Posicion(actual);

                movimiento.m_final = new Posicion();
                movimiento.m_final.setX(actual.getX() - 1);
                movimiento.m_final.setY(actual.getY() - 1);

                movimientos.add(movimiento);
            }

            if(tablero.movimientoValido(actual.getX(), actual.getY(), actual.getX() - 1, actual.getY() + 1, jugador) == 1)
            {
                Movimiento movimiento = new Movimiento();

                movimiento.m_inicial = new Posicion(actual);

                movimiento.m_final = new Posicion();
                movimiento.m_final.setX(actual.getX() - 1);
                movimiento.m_final.setY(actual.getY() + 1);

                movimientos.add(movimiento);
            }
            
            
        }
        
        return movimientos;
    }
    
    public ArrayList<Posicion> getFichas()
    {
        ArrayList<Posicion> fichas = new ArrayList<Posicion>();
        
        int max = 1;
        
        if(jugador == JugadorMaquina.BLANCAS)
        {
            max = 4;
        }
        
        for(int i = 0; i < tablero.tamaño && fichas.size() < max; i++)
        {
            for(int j = 0; j < tablero.tamaño && fichas.size() < max; j++)
            {
                if(tablero.obtenerCasilla(i, j) == jugador)
                {
                    Posicion p = new Posicion();
                    
                    p.setX(j);
                    p.setY(i);
                    
                    fichas.add(p);
                }
            }
        }
        
        return fichas;
    }
    
    //Nos devuelve un vecror con las posiciones de las fichas de un jugador
    public ArrayList<Posicion> getFichas(int color_jugador)
    {
        ArrayList<Posicion> fichas = new ArrayList<Posicion>();
        
        int max = 1;
        
        if(color_jugador == JugadorMaquina.BLANCAS)
        {
            max = 4;
        }
        
        for(int i = 0; i < tablero.tamaño && fichas.size() < max; i++)
        {
            for(int j = 0; j < tablero.tamaño && fichas.size() < max; j++)
            {
                if(tablero.obtenerCasilla(i, j) == color_jugador)
                {
                    Posicion p = new Posicion();
                    
                    p.setX(j);
                    p.setY(i);
                    
                    fichas.add(p);
                }
            }
        }
        
        return fichas;
    }
    
    public int getContrario()
    {
        int contrario = JugadorMaquina.BLANCAS;
        
        if(jugador == JugadorMaquina.BLANCAS)
        {
            contrario = JugadorMaquina.NEGRAS;
        }
        
        return contrario;
    }
    
    //Nos idica la distancia en filas que está la negra de ganar
    public int distanciaNegra()
    {
        int distancia = -1;
        
        ArrayList<Posicion> negra = getFichas(JugadorMaquina.NEGRAS);
        
        Posicion p = (Posicion)(negra.get(0));
        
        distancia = tablero.tamaño - p.getX();

        return distancia;
    }
    
    //Nos devuelve un valor según si la ficha negra está centrada en las columnas
    public int centradoNegras()
    {
        int valor = -4;
        
        ArrayList<Posicion> negra = getFichas(JugadorMaquina.NEGRAS);
        
        Posicion p = (Posicion)(negra.get(0));
        
        if(p.m_py > 0 && p.m_py < 7)
        {
            valor = 2;
            
            if(p.m_py > 1 && p.m_py < 6)
            {
                valor += 3;
            }
        }

        return valor;
    }
    
    //Nos calcula la distancia media de las fichas blancas a la negra.
    //Solo tiene en cuenta las blancas por debajo de la negra
    public float distanciaBlancas()
    {
        float distancia = 0.0f;
        float total = 0.0f;
        int fichas = 0;
        
        ArrayList<Posicion> negra = getFichas(JugadorMaquina.NEGRAS);
        ArrayList<Posicion> blancas = getFichas(JugadorMaquina.BLANCAS);
        
        Posicion posNegra = (Posicion)(negra.get(0));
        
        
        for(int i = 0; i < blancas.size(); i++)
        {
            if(blancas.get(i).m_px > negra.get(0).m_px)
            {
                total += Math.sqrt(Math.pow(posNegra.m_px - blancas.get(i).m_px, 2) + Math.pow(posNegra.m_py - blancas.get(i).m_py, 2));
                fichas++;
            }
        }
        
        distancia = total / fichas;

        return distancia;
    }
    
    //Devuelve la disntancia de la blanca más cercana a la negra
    public int distanciaBlancasCorta()
    {
        int distancia = 99;
        
        ArrayList<Posicion> negra = getFichas(JugadorMaquina.NEGRAS);
        ArrayList<Posicion> blancas = getFichas(JugadorMaquina.BLANCAS);
        
        Posicion posNegra = (Posicion)(negra.get(0));
        
        for(int i = 0; i < blancas.size(); i++)
        {
            if(blancas.get(i).m_px > negra.get(0).m_px)
            {
                int aux = (int)Math.sqrt(Math.pow(posNegra.m_px - blancas.get(i).m_px, 2) + Math.pow(posNegra.m_py - blancas.get(i).m_py, 2));
                if(aux < distancia)
                {
                    distancia = aux;
                }
            }
        }
        
        return distancia;
    }
    
    //Distancia más larga entre las blancas en filas
    public int distanciaEntreBlancas()
    {
        int distancia = -1;
        
        ArrayList<Posicion> blancas = getFichas(JugadorMaquina.BLANCAS);
        
        for(int i = 0; i < blancas.size(); i++)
        {
            for(int j = i; j < blancas.size(); j++)
            {
                
                    int aux = Math.abs(blancas.get(i).m_px - blancas.get(j).m_px);
                    if(aux > distancia)
                    {
                        distancia = aux;
                    }
            }
        }
        
        return distancia;
    }
    
    //Función que indica cuantos movimiento puede realizar la negra en esa configuración de tablero.
    public int movimientosNegras()
    {
        int moivientos = 0;
        
        ArrayList<Posicion> negra = getFichas(JugadorMaquina.NEGRAS);
        
        Posicion posNegra = (Posicion)(negra.get(0));
        
        for(int i = -1; i <= 1; i+=2)
        {
            for(int j = 0; j <= 1; j+=2)
            {
                //if(tablero.obtenerCasilla(posNegra.m_px + i, posNegra.m_py + j) == 0)
                if(tablero.movimientoValido(posNegra.m_px, posNegra.m_py, posNegra.m_px + i, posNegra.m_py + j, jugador)==1)
                {
                    moivientos++;
                }
            }
        }

        return moivientos;
    }
    
    //Devuelve true si el jugador está en una situación en la que tiene asegurada la victoria. (Pero no ha ganado aún).
    public boolean isWin(int color_jugador)
    {
        boolean win = false;
        ArrayList<Posicion> negra = getFichas(JugadorMaquina.NEGRAS);
        
        if(color_jugador == JugadorMaquina.NEGRAS)
        {
            if(negra.get(0).m_px == 7)
            {
                win = true;
            }
        }
        else
        {
            if(!tablero.quedanMovimientos(JugadorMaquina.NEGRAS) && negra.get(0).m_px < 7)
            {
                win = true;
            }
        }
        
        return win;
    }
    
    //Devuelve true si el jugador está en una situación de victoria.
     public boolean finWin(int color_jugador)
    {
        boolean win = false;
        ArrayList<Posicion> negra = getFichas(JugadorMaquina.NEGRAS);
        
        if(color_jugador == JugadorMaquina.NEGRAS)
        {
            if(negra.get(0).m_px == 7)
            {
                win = true;
            }
        }
        else
        {
            if(!tablero.quedanMovimientos(JugadorMaquina.NEGRAS) && negra.get(0).m_px < 7)
            {
                win = true;
            }
        }
        
        return win;
    }
    
    //Comprueba si la Negra está en el lado del tablero (Izq., Der.) más alejado de las blancas.
    public int ladoCorrectoNegra()
    {
        int bueno = 0;
        int izq = 0, der = 0;
        ArrayList<Posicion> negra = getFichas(JugadorMaquina.NEGRAS);
        ArrayList<Posicion> blancas = getFichas(JugadorMaquina.BLANCAS);
        
        for(int i = 0; i < blancas.size(); i++)
        {
            if(blancas.get(i).m_px < negra.get(0).m_px)
            {
                if(blancas.get(i).m_py < 4) //Izquierda
                {
                    izq++;
                }
                else                        //Derecha
                {
                    der++;
                }
                      
            }
        }
        
        if(izq > der)
        {
            if(negra.get(0).m_py < 4)
            {
                bueno = -10;
            }
            else
            {
                bueno = 10;
            }
        }
        else
        {
           if(negra.get(0).m_py < 4)
            {
                bueno = 10;
            }
            else
            {
                bueno = -10;
            } 
        }
        
        return bueno;
    }
    
    //Indica si un jugador está en una situación ganadora. Tiene la victoria asegurada
    public boolean situacionGanadora(int color_jugador)
    {
        boolean win = true;
        ArrayList<Posicion> negra = getFichas(JugadorMaquina.NEGRAS);
        ArrayList<Posicion> blancas = getFichas(JugadorMaquina.BLANCAS);
        
        if(color_jugador == JugadorMaquina.NEGRAS)
        {
            for(int i = 0; i < blancas.size() & win; i++)
            {
                if(blancas.get(i).m_px > negra.get(0).m_px)
                {
                    win = false;
                }
            }
            
            if(caminoDeNegra())
            {
                win = true;
            }
        }
        else
        {
            if(!tablero.quedanMovimientos(JugadorMaquina.NEGRAS) && negra.get(0).m_px < 7)
            {
                win = true;
            }
        }
        
        return win;
    }
    
    //Indica el número de fichas blancas que la negra tiene por encima. Y por tanto no le cortan el paso.
    public int blancasPorEncima()
    {
        int porEncima = 0;
        ArrayList<Posicion> negra = getFichas(JugadorMaquina.NEGRAS);
        ArrayList<Posicion> blancas = getFichas(JugadorMaquina.BLANCAS);
        
        for(int i = 0; i < blancas.size(); i++)
        {
            if(blancas.get(i).m_px <= negra.get(0).m_px)
            {
                porEncima++;
            }
        }
        
        return porEncima;
    }
    
    //Indica si la negra puede tener un camino de paso. 
    //Se calcula si la distancia en filas de cada ficha es mayor o igual que la distancia en columnas.
    public boolean caminoDeNegra()
    {
        boolean camino = false;
        int fichas = 0;
        
        ArrayList<Posicion> negra = getFichas(JugadorMaquina.NEGRAS);
        ArrayList<Posicion> blancas = getFichas(JugadorMaquina.BLANCAS);
        
        Posicion posNegra = (Posicion)(negra.get(0));
        
        for(int i = 0; i < blancas.size(); i++)
        {
            if(blancas.get(i).m_px > negra.get(0).m_px)
            {
                if(Math.abs(blancas.get(i).m_px - negra.get(0).m_px) <= Math.abs(blancas.get(i).m_py - negra.get(0).m_py))
                {
                    fichas++;
                }
            }
        }
        
        if(fichas == 4)
        {
            camino = true;
        }
        
        return camino;
    }
    
    public boolean isLose(int color_jugador)
    {
        boolean lose = false;
        ArrayList<Posicion> negra = getFichas(JugadorMaquina.NEGRAS);
        
        if(color_jugador == JugadorMaquina.BLANCAS)
        {
            if(negra.get(0).m_px == 7)
            {
                lose = true;
            }
            
            if(blancasPorEncima() == 4)
            {
                lose = true;
            }
            
            if(caminoDeNegra())
            {
                lose = true;
            }
        }
        else
        {
            if(!tablero.quedanMovimientos(JugadorMaquina.NEGRAS) && negra.get(0).m_px < 7)
            {
                lose = true;
            }
        }
        
        return lose;
    }
    
     public boolean finLose(int color_jugador)
    {
        boolean lose = false;
        ArrayList<Posicion> negra = getFichas(JugadorMaquina.NEGRAS);
        
        if(color_jugador == JugadorMaquina.BLANCAS)
        {
            if(negra.get(0).m_px == 7)
            {
                lose = true;
            }
        }
        else
        {
            if(!tablero.quedanMovimientos(JugadorMaquina.NEGRAS) && negra.get(0).m_px < 7)
            {
                lose = true;
            }
        }
        
        return lose;
    }
    
    public int evaluarNodo(int color_jugador)
    {
        int valor = 0;
        /*if(color_jugador == JugadorMaquina.NEGRAS)
            return (8 - distanciaNegra());
        else
            return distanciaNegra();*/
        //System.out.println("Color: " + color_jugador + "Dist: " + distanciaNegra());
        
        if(color_jugador == JugadorMaquina.NEGRAS)  //Negras
        {
            if(isWin(JugadorMaquina.NEGRAS) || situacionGanadora(JugadorMaquina.NEGRAS))
            {
                valor = 1000;
            }
            else if(isLose(JugadorMaquina.NEGRAS))
            {
                valor = -1000;
            }
            else
            {
                
                //Cerca 30%, BlancasPorEncima: 20%, Mov.Posibles: 20%, DistanciaCorta: 20% DistGlobal: 10%
                //8, 4, 4, 8, 8
                //240(30), 160(40), 160(40), 160(20), 80(10) == 800
                //valor = (8 - distanciaNegra()) * 100;
                /*if(distanciaBlancasCorta() >= 3)
                {
                    valor = (8 - distanciaNegra()) * 100;
                }
                else
                {
                    valor = (8 - distanciaNegra()) * 40;
                    valor += blancasPorEncima() * 20;
                    valor += movimientosNegras() * 20;
                    valor += distanciaBlancasCorta() * 20;
                    valor += distanciaBlancas() * 10;
                }*/
                
                /*valor = (8 - distanciaNegra()) * 30;
                valor += blancasPorEncima() * 40;
                valor += movimientosNegras() * 40;
                valor += ladoCorrectoNegra() * 20;
                //valor += distanciaBlancasCorta() * 20;
                //valor += distanciaBlancas() * 10;
                valor += centradoNegras() * 20;*/
                valor = (8 - distanciaNegra())*3;
                valor += blancasPorEncima() * 3;
                valor += movimientosNegras() * 4;
                valor += centradoNegras();
                valor += distanciaBlancas();
                //valor += 10*(JugadorMaquina.NIVEL_DEFECTO - profundidad);
                
            }
        }
        else                                        //Blancas
        {
            if(isWin(JugadorMaquina.BLANCAS))
            {
                valor = 1000;
            }
            else if(isLose(JugadorMaquina.BLANCAS))
            {
                valor = -1000;
            }
            else
            {
                valor = distanciaNegra()/2;
                valor -= distanciaEntreBlancas()*2;
                valor -= distanciaBlancas();
                valor -= movimientosNegras();
                valor += (4 - blancasPorEncima()) * 3;
                //valor += 10*(JugadorMaquina.NIVEL_DEFECTO - profundidad);
                
                /*valor = distanciaNegra() * 30;
                valor -= blancasPorEncima() * 40;
                valor -= movimientosNegras() * 40;
                valor -= ladoCorrectoNegra() * 20;
                //valor += distanciaBlancasCorta() * 20;
                valor -= distanciaBlancas() * 10;
                valor -= centradoNegras() * 20;*/
                //valor++;
                
                
                
            }
        }
        
        return valor;
    }
    
    public String toString()
    {
        System.out.println();
        
        for(int i = 0; i < tablero.tamaño; i++)
        {
            for(int j = 0; j < tablero.tamaño; j++)
            {
                System.out.print(tablero.obtenerCasilla(j, i) + " ");
            }
            
            System.out.println();
        }
        
        return "";
    }
    
    
}