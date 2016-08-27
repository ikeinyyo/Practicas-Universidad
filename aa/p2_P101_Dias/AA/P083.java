package AA;

import java.util.ArrayList;

public class P083 {

	ArrayList<Integer> days;
	int N;
	int L;
	int bestSolution;
	int[][] A;
	
	public P083()
	{
		days = new ArrayList<Integer>();
	}
	
	public int best(String data)
	{
		init(data);
		return pdi();
	}
	
	private int pdi()
	{
		int value = -1;
		int size = days.size();
		int primer = days.get(0);
		
		for(int i = 0; i <= N; i++)
		{
			A[0][i] = primer;
		}
		
		for(int i = 0; i < size; i++)
		{
			A[i][0] = 0;
		}
		
		int row;
		
		for(int i = 1; i < size; i++)
		{
			for(int j = 1; j <= N; j++)
			{
				row = i - L;
				
				if(row < 0)
				{
					A[i][j] = Math.max(A[i-1][j], days.get(i));
				}
				else
				{
					A[i][j] = Math.max(A[i-1][j], A[row][j-1] + days.get(i));
				}
				
				if(A[i][j] > value)
				{
					value = A[i][j];
				}
			}
		}
		
		
		return value;
	}
	
	public void init(String data)
	{	
		days = new ArrayList<Integer>();
		String[] aux = data.split(" ");
		
		N = Integer.parseInt(aux[0]);
		L = Integer.parseInt(aux[1]);
		
		for(int i = 2; i < aux.length; i++)
		{
			days.add(Integer.parseInt(aux[i]));
		}
		
		A = new int[days.size()][N+1];
	}
	
	public static void main(String[] args)
	{
		P083 p = new P083();
		System.out.println(p.best(args[0]));
	}
}
