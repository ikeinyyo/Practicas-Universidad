
import java.util.HashMap;
/*
 * Autor: Juan R. Rico
 * 
 * Descripción: P004 es una solución propuesta al juego del Nim para 2 jugadores con N fichas totales 
 * y M a retirar por jugada. El programa devuelve la primera jugada ganadora, o bien, -1 en 
 * caso de no tener estrategia ganadora.
 * 
 * Está resulto por:
 *   - programación dinámica (PD) recursiva (pura);
 *   - PD con almacén (memoization); 
 *   - PD iterativa.
 * 
 * 
 * Advertencia: Este código contiene algún error para que el alumno lo rectifique y pueda probarlo 
 * en el sistema de corrección de problemas de la asignatura llamado Javaludor 
 * (http://javaludor.dlsi.ua.es/) el usuario y la contraseña es el mismo que el usado en los
 * servicios de la EPSA. Este sistema automático testea el programa con una batería de test y 
 * devuelve el porcentaje de tests superador correctamente.
 * 
 */
public class P004 {

	int N, M;
	Boolean[][] A2;
	private void init(String data) {
		String[] token = data.split("\\p{Space}+");
		this.N = Integer.parseInt(token[0]);  //Número total de fichas
		this.M = Integer.parseInt(token[1]);  //Número máximo a retirar por jugada
	}

	private void jout(String cadena)
	{
		System.out.print(cadena);
	}

	private void joutln(String cadena)
	{
		System.out.println(cadena);
	}


	public void best () {

		joutln("Algoritmo 1: " + algoritmo_1(N,M));
		joutln("Algoritmo 2: " + reservar_2() + algoritmo_2(N,M));
		joutln("Algoritmo 3: " + algoritmo_3(N,M));
		joutln("Algoritmo 3_1: " + algoritmo_3_1(N,M));
	}
	public String reservar_2()
	{
		A2 = new Boolean[N+1][M+1];
		return "";
	}
	

	public boolean algoritmo_1 (int n, int m) {
		boolean res=false;
		if ( n==0 || (n==1 && m==1)){
			res = false;
		} else {
			for ( int k = 1; k <= Math.min(n, M); ++k )
				if ( k != m && !algoritmo_1(n - k, k) )
					res = true;
		}
		return res;
	}
	
	public boolean algoritmo_2 (int n, int m) {
		boolean res = false;
		if ( A2[n][m] == null ) {
			if ( n==0 || (n==1 && m==1)){
				res = false;
			} else {
				for ( int k = 1; k <= Math.min(n, M); ++k )
					if ( k != m && !algoritmo_2(n - k, k) )
						res = true;
			}
			A2[n][m] = res;
		} else {
			res = A2[n][m];
		}
		return res;
	}
	public boolean algoritmo_3 (int n0, int m0) {
		boolean[][] A = new boolean[N+1][M+1];
		for ( int n = 0; n <= N; ++n )
			for ( int m = 0; m <= M; ++m ) {
				A[n][m] = false;
				for ( int k = 1; k <= Math.min(n, M); ++k ){
					if ( k != m && !A[n-k][k] ) {
						A[n][m] = true;
					}
				}
			}
		return A[n0][m0];
	}
	
	public boolean algoritmo_3_1 (int n0, int m0) {
		boolean[][] A = new boolean[M+1][M+1];
		for ( int n = 0; n <= N; ++n )
			for ( int m = 0; m <= M; ++m ) {
				A[n % (M+1)][m] = false;
				for ( int k = 1; k <= Math.min(n, M); ++k ){
					if ( k != m && !A[(n-k) % (M+1)][k] ) {
						A[n % (M+1)][m] = true;
					}
				}
			}
		return A[n0 % (M+1)][m0];
	}



	public static void main(String[] args) {
		P004 p = new P004();
		p.init(args[0]);
		p.best();
	}
}

