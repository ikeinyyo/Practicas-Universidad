package AA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class P103 {

	private int bestValue;
	private ArrayList<ArrayList<Integer>> blocks;
	private ArrayList<Integer> mejores;
	private ArrayList<ArrayList<Integer>> indices;
	private int indice;

	public P103()
	{
		bestValue = -1;
	}

	public void init(String[] data)
	{
		bestValue = -1;
		blocks = new ArrayList<ArrayList<Integer>>();
		mejores = new ArrayList<Integer>();
		indices = new ArrayList<ArrayList<Integer>>();
		indice = -1;

		for(String s: data)
		{
			String[] aux = s.split(" ");
			int a = Integer.parseInt(aux[0]);
			int b = Integer.parseInt(aux[1]);
			int c = Integer.parseInt(aux[2]);

			ArrayList<Integer> block = new ArrayList<Integer>();
			block.add(a);
			block.add(b);
			block.add(c);
			blocks.add(block);
			mejores.add(0);
			indices.add(new ArrayList<Integer>());

			block = new ArrayList<Integer>();
			block.add(b);
			block.add(c);
			block.add(a);
			blocks.add(block);
			mejores.add(0);
			indices.add(new ArrayList<Integer>());

			block = new ArrayList<Integer>();
			block.add(c);
			block.add(a);
			block.add(b);
			blocks.add(block);
			mejores.add(0);
			indices.add(new ArrayList<Integer>());
		}





		Collections.sort(blocks, new micompare());

	}



	public ArrayList<String> bestSolution(String[] data)
	{
		init(data);

		//ArrayList<Integer> block = blocks.get(0);

		/*bt(999999999, 999999999, 0);
		System.out.println("BT: " + bestValue);
		bestValue = 0;

		ryp(999999999, 999999999, 0);
		System.out.println("RyP: " + bestValue);
		bestValue = 0;*/

		pd();
		//System.out.println("pd: " + bestValue);

		return sCompleta();
	}

	private void bt(int w, int l, int value)
	{
		if(value > bestValue)
		{
			bestValue = value;
		}

		int wb, lb, hb;
		for(ArrayList<Integer> block: blocks)
		{
			hb = block.get(0);
			wb = block.get(1);
			lb = block.get(2);

			//W-L
			if(wb <= w && lb < l || wb < w && lb <= l)
			{
				bt(wb, lb, value + hb);
			}

			if(lb <= w && wb < l || lb < w && wb <= l)
			{
				bt(lb, wb, value + hb);
			}

			//H-W
			if(wb <= w && hb < l || wb < w && hb <= l)
			{
				bt(wb, hb, value + lb);
			}

			if(hb <= w && wb < l || hb < w && wb <= l)
			{
				bt(hb, wb, value + lb);
			}

			//H-L
			if(hb <= w && lb < l || hb < w && lb <= l)
			{
				bt(hb, lb, value + wb);
			}

			if(lb <= w && hb < l || lb < w && hb <= l)
			{
				bt(lb, hb, value + wb);
			}
		}
	}

	/*private void ryp(int w, int l, int value)
	{
		if(value > bestValue)
		{
			bestValue = value;
		}

		int wb, lb, hb;
		if(cota(w,l,value) > bestValue)
		{
			for(ArrayList<Integer> block: blocks)
			{
				hb = block.get(0);
				wb = block.get(1);
				lb = block.get(2);

				//W-L
				if(wb <= w && lb < l || wb < w && lb <= l)
				{
					bt(wb, lb, value + hb);
				}

				if(lb <= w && wb < l || lb < w && wb <= l)
				{
					bt(lb, wb, value + hb);
				}

				//H-W
				if(wb <= w && hb < l || wb < w && hb <= l)
				{
					bt(wb, hb, value + lb);
				}

				if(hb <= w && wb < l || hb < w && wb <= l)
				{
					bt(hb, wb, value + lb);
				}

				//H-L
				if(hb <= w && lb < l || hb < w && lb <= l)
				{
					bt(hb, lb, value + wb);
				}

				if(lb <= w && hb < l || lb < w && hb <= l)
				{
					bt(lb, hb, value + wb);
				}
			}
		}
	}*/

	private int cota(int a, int b, int value)
	{
		return 9999999;
	}

	private void pd()
	{
		int max = -1;
		int a,b,c;
		int b2,c2;
		ArrayList<Integer> actual;
		ArrayList<Integer> prueba;
		//System.out.println("Cajas: " + blocks.size());
		for(int n = 0; n < mejores.size(); n++)
		{

			max = 0;
			actual = blocks.get(n);
			a = actual.get(0);
			b = actual.get(1);
			c = actual.get(2);

			for(int i = n-1; i >= 0; i--)
			{
				prueba = blocks.get(i);
				b2 = prueba.get(1);
				c2 = prueba.get(2);


				if((b <= b2 && c < c2) || (b < b2 && c <= c2) || (c <= b2 && b < c2) || (c < b2 && b <= c2))
				{
					if(mejores.get(i) > max)
					{
						indices.set(n, new ArrayList<Integer>());
						
						for(int j = 0; j < indices.get(i).size(); j++)
						{
							indices.get(n).add(indices.get(i).get(j));
						}
						
						//indices.get(n).add(i);
						max = mejores.get(i);
					}
				}
			}

			mejores.set(n, max + a);
			indices.get(n).add(n);
			//System.out.println("MAX = " + max);

			if(mejores.get(n) > bestValue)
			{
				bestValue = mejores.get(n);
				indice = n;
			}
		}

	}

	public ArrayList<String> sCompleta()
	{
		ArrayList<String> salida = new ArrayList<String>();
		ArrayList<Integer> ind = indices.get(indice);
		ArrayList<Integer> caja;

		for(int i = 0; i < ind.size(); i++)
		{
			caja = blocks.get(ind.get(i));
			
			salida.add(caja.get(0).toString() + " " + caja.get(1).toString() + " " + caja.get(2).toString());
		}
		return salida;
	}


	public class micompare implements Comparator<ArrayList<Integer>> {

		public int compare(ArrayList<Integer> a, ArrayList<Integer> b)
		{
			if(a.get(1) * a.get(2) < b.get(1) * b.get(2))
			{
				return 1;
			}
			else if(a.get(1) * a.get(2) == b.get(1) * b.get(2))
			{
				if(a.get(0) > b.get(0))
				{
					return -1;
				}
				else
				{
					return 0;
				}
			}
			else
			{
				return -1;
			}

		}
	}

	public static void main(String[] argv)
	{
		P103 p = new P103();

		System.out.println("Mejor solución: " + p.bestSolution(argv));
	}
}



/*
class Box {

	public int height;		//Altura en Z							
	public int length;		//Longitud en Y						    
	public int width;		//Anchura en X

	public Box(int h, int l, int w)
	{
		height = h;
		length = l;
		width = w;
	}

}*/
