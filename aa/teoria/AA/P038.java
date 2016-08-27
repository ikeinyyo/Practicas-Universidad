package AA;

import java.util.ArrayList;

public class P038 {

	int best = Integer.MAX_VALUE / 2;
	ArrayList<Integer> T1;
	ArrayList<Integer> T2;
	ArrayList<Integer> tiempos;

	public int best(String[] data)
	{
		init(data);
		int n = T1.size();

		tiempos = new ArrayList<Integer>();
		tiempos.add(0);
		int res = 0;
		for ( int k = 0; k < n; ++k ) {
			res += Math.min( T1.get(k), T2.get(k) );
			tiempos.add(res);
		}
		minTime(n, 0,0);
		return best;
	}

	private void init(String[] data)
	{
		T1 = new ArrayList<Integer>();
		T2 = new ArrayList<Integer>();
		best = Integer.MAX_VALUE / 2;

		String a = data[0];

		String[] tareas1 = a.split(" ");
		for(String s: tareas1)
		{
			T1.add(Integer.parseInt(s));
		}

		String b = data[1];

		String[] tareas2 = b.split(" ");
		for(String s: tareas2)
		{
			T2.add(Integer.parseInt(s));
		}


	}


	private void minTime (int n, int t1, int t2) {
		//Si podemos mejorar exploramos
		if ( lowerBound(n, t1, t2) < best ) {
			if ( n == 0 ) { // no quedan trabajos
				best = Math.min(best, Math.max(t1 , t2));
			} else {
				minTime(n - 1, t1 + T1.get(n-1), t2);
				minTime(n - 1, t1, t2 + T2.get(n-1));
			}
		}
	}

	private int lowerBound(int n, int t1, int t2) {
		//int res = 0;
		/*for ( int k = 0; k < n; ++k ) {
			res += Math.min( T1.get(k), T2.get(k) );
		}
		return max(t1,t2,(t1 + t2 + res + 1)/2); // ceiling*/
		return max(t1,t2, ((t1 + t2 + tiempos.get(n) + 1)/2));
	}

	private int max(int a, int b, int c)
	{
		return Math.max(a, Math.max(b ,c));
	}

	public static void main(String[] args)
	{
		P038 p = new P038();

		System.out.println(p.best(args));
	}


}
