package AA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class P102 {

	private int bestValue;
	private ArrayList<ArrayList<Integer>> blocks;
	private ArrayList<Integer> mejores;

	public P102()
	{
		bestValue = -1;
	}

	public void init(String[] data)
	{
		bestValue = -1;
		blocks = new ArrayList<ArrayList<Integer>>();
		mejores = new ArrayList<Integer>();

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
			
			block = new ArrayList<Integer>();
			block.add(b);
			block.add(c);
			block.add(a);
			blocks.add(block);
			mejores.add(0);
			
			block = new ArrayList<Integer>();
			block.add(c);
			block.add(a);
			block.add(b);
			blocks.add(block);
			mejores.add(0);
		}
		

		
		
		
		Collections.sort(blocks, new micompare());

	}

	
	
	public int best(String[] data)
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

		return bestValue;
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

	private void ryp(int w, int l, int value)
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
	}

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

				//System.out.println("ENTRO a: " + a + " b: " + b + " c: " + c + " b2: " + b2 + " c2: " + c2);
				
				if((b <= b2 && c < c2) || (b < b2 && c <= c2) || (c <= b2 && b < c2) || (c < b2 && b <= c2))
				//if(true)
				{
					if(mejores.get(i) > max)
					{
						max = mejores.get(i);
					}
				}
			}
			
			mejores.set(n, max + a);
			//System.out.println("MAX = " + max);
			
			if(mejores.get(n) > bestValue)
			{
				bestValue = mejores.get(n);
			}
		}
			
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
		P102 p = new P102();

		System.out.println("Mejor solución: " + p.best(argv));
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
