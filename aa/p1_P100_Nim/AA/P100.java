/*
 * Autor: Sergio Gallardo Sales
 * 
 */
package AA;

import java.util.ArrayList;


public class P100 {

	private int N1, N2, M, m0;
	private Integer[][][] A;

	private void  init(String data)
	{
		String[] token = data.split("\\p{Space}+");
		this.N1 = Integer.parseInt(token[0]);  	//Número total de fichas de la Fila 1.
		this.N2 = Integer.parseInt(token[1]);  	//Número total de fichas de la Fila 2.
		this.m0 = Integer.parseInt(token[2]);  	//Última Jugada.
		this.M = Integer.parseInt(token[3]);  	//Número máximo a retirar por jugada.
		this.A = new Integer[N1+1][N2+1][(2*M)+1];

	}

	public int best(String data)
	{
		Integer value = -1;

		this.init(data);

		//value = pdr(this.N1, this.N2, this.m0);
		//System.out.println("Recursiva: " + value);
		value = pdr_a(this.N1, this.N2, this.m0);
		//System.out.println("Recursiva con Almacen: " + value);
		//this.A = new Integer[N1+1][N2+1][(2*M)+1];
		//value = pdi(this.N1, this.N2, this.m0);
		//System.out.println("Iterativa: " + value);

		//value = -1;

		return value;
	}

	private int pdr(int n1, int n2, int mi)
	{
		int value = 0;
		for(int i = 1; i <= Math.min(n1, this.M); i++)
		{

			if(-i != mi  && i != 0)
			{
				if(pdr(n1 - Math.abs(i), n2, -i) == 0)
				{
					//System.out.println(i);
					return -i;
				}
			}
		}

		for(int i = 1; i <= Math.min(n2, this.M); i++)
		{

			if(i != mi && i != 0)
			{
				if(pdr(n1, n2 - Math.abs(i) , i) == 0)
				{
					return i;
				}
			}
		}

		return value;
	}

	private int pdr_a(int n1, int n2, int mi)
	{
		int value = 0;

		if(A[n1][n2][mi + M] == null)
		{
			for(int i = 1; i <= Math.min(n2, this.M); i++)					//Segunda fila
			{

				//if(isPosible(n1,n2,mi,-i))
				if(-i != mi && Math.abs(i) <= n2)
				{
					if(pdr_a(n1, n2 - Math.abs(i), -i) == 0)
					{
						A[n1][n2][mi + M] = -i;
						return -i;
					}
				}
			}

			//Primera fila
			for(int i = 1; i <= Math.min(n1, this.M); i++)
			{

				//if(isPosible(n1,n2,mi,i))
				if(i != mi && Math.abs(i) <= n1)
				{
					if(pdr_a(n1 - Math.abs(i), n2, i) == 0)
					{
						//System.out.println(i);
						A[n1][n2][mi + M] = i;
						return i;
					}
				}
			}


		}
		else
		{
			return A[n1][n2][mi + M];
		}

		return value;
	}

	private int pdi(int n1, int n2, int mi)
	{

		int value = 0;
		//System.out.println("N1: " + n1 + " N2: " + n2 + " Mi: " + mi + " M: " + M + "Puntero: " + A[n1][n2][mi + M]);

		for(int i = 0; i <= n1; i++)
		{
			for(int j = 0; j <= n2; j++)
			{
				for(int k = 0; k < 2*M; k++)
				{
					value = 0;
					
					
					
					if(k >= M)							//Primera fila y se resta item - M + 1
					{
						for(int l = Math.max(-M, -n2); l <= Math.min(M, n1); l++)
						{
							if(l != 0)
							{
								if(l > 0)				//Primera fila
								{
									if(l <= i && (A[i-l][j][l + M - 1] == null || A[i-l][j][l + M - 1] == 0))
									{
										value = 0;
										break;
									}
								}
								else
								{
									if(Math.abs(l) <= j && (A[i][j+l][l + M] == null || A[i][j+l][l + M] == 0))
									{
										value = 0;
										break;
									}
								}
							}
						}
						A[N1][N2][k - 1] = value;
					}
					else								//SEgunda fila y se resta item - M
					{
						for(int l = Math.max(-M, -n2); l <= Math.min(M, n1); l++)
						{
							if(l != 0)
							{
								if(l > 0)				//Primera fila
								{
									if(l <= i && (A[i-l][j][l + M - 1] == null || A[i-l][j][l + M - 1] == 0))
									{
										value = 0;
										break;
									}
								}
								else
								{
									if(Math.abs(l) <= j && (A[i][j+l][l + M] == null || A[i][j+l][l + M] == 0))
									{
										value = 0;
										break;
									}
								}
							}
						}
						A[N1][N2][k] = value;
					}
				}
			}
		}

		if(A[N1][N2][m0 + M] != null)
		{
			return A[N1][N2][m0 + M];
		}
		else
		{
			return 0;
		}
	}


	private boolean isPosible(Integer n1, Integer n2, Integer mi, Integer mj)
	{
		boolean posible = false;

		if(mj > 0)			//Primera fila
		{
			if(mj != mi && Math.abs(mj) <= n1)
			{
				posible = true;
			}
		}
		else if(mj < 0)		//Segunda fila	
		{ 
			if(mj != mi && Math.abs(mj) <= n2)
			{
				posible = true;
			}
		}

		return posible;
	}

	private boolean isPosible(Integer n1, Integer n2, Integer mi)
	{
		if(n1 > 0 && n1 > 0)
		{
			return true;
		}
		else if(mi > 0)			//Primera fila
		{
			if(n2 > 0)
			{
				return true;
			}
			else
			{
				for(int i = 1; i < Math.min(n1, M); i++)
				{
					if(i != mi)
					{
						return true;
					}
				}
			}
		}
		else if(mi < 0)			//Segunda fila
		{
			if(n1 > 0)
			{
				return true;
			}
			else
			{
				for(int i = 1; i < Math.min(n2, M); i++)
				{
					if(-i != mi)
					{
						return true;
					}
				}
			}
		}
		else
		{
			if(n1 > 0 || n2 > 0)
			{
				return true;
			}
		}

		return false;
	}

	public static void main(String[] args)
	{
		P100 p = new P100();
		for (String s : args) {
			System.out.println(p.best(s));
		}
	}
}