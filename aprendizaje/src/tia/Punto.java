package tia;
import static java.lang.Math.signum;

import java.util.ArrayList;
import java.util.List;

public class Punto {

	private double x;
	private double y;
	private double tipo;

	public Punto(double x, double y, double tipo) {
		super();
		this.x = x;
		this.y = y;
		this.setTipo(tipo);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getTipo() {
		return tipo;
	}

	public void setTipo(double tipo) {
		this.tipo = signum(tipo);
	}

	public double clasificar(List<Recta> clasificadores)
	{
		double clase = 0.0;
		for(Recta r : clasificadores)
		{
			clase += r.clasificar(this);
		}

		return Math.signum(clase);
	}

	public boolean isCorrecto(List<Recta> clasificadores)
	{
		double clase = 0.0;
		boolean correcto = true;
		for(Recta r : clasificadores)
		{
			clase += r.clasificar(this);
		}

		if(clasificadores.size() == 0)
		{
			correcto = true;
		}
		else
		{
			correcto = (Math.signum(clase) == getTipo());
		}

		return correcto;
	}

}
