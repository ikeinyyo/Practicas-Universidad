/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GatosYRaton;

/**
 *
 * @author mireia
 */
public class Movimiento {
    public Posicion m_inicial;
    public Posicion m_final;

    public Movimiento()
    {
        m_inicial = new Posicion();
        m_final = new Posicion();
    }

    public Movimiento (Movimiento original)
    {
        m_inicial = original.m_inicial;
        m_final = original.m_final;
    }

    public void setInicial (int x, int y)
    {
        m_inicial.setX(x);
        m_inicial.setY(y);
    }

    public void setFinal (int x, int y)
    {
        m_final.setX(x);
        m_final.setY(y);
    }

    public Posicion getInicial()
    {
        return m_inicial;
    }

    public Posicion getFinal()
    {
        return m_final;
    }
}
