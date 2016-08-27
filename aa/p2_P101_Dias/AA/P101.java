package AA;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class P101 {

	ArrayList<Integer> days;
	int N;
	int L;
	int bestRow;
	int bestColumn;
	int[][] A;

	public P101()
	{
		days = new ArrayList<Integer>();
	}

	public ArrayList<Integer> bestSolution(String data)
	{
		init(data);
		return pdi();
	}

	private ArrayList<Integer> pdi()
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
					bestColumn = j;
					bestRow = i;
				}
			}
		}

		return getSolution();
	}

	private ArrayList<Integer> getSolution()
	{
		ArrayList<Integer> solution = new ArrayList<Integer>();

		int size = days.size();
		int value = 0;
		int row;
		/*for(int i = bestRow; i < size; i++)
		{
			for(int j = bestColumn; j <= N; j++)
			{*/
		int i = bestRow;
		int j = bestColumn;
		
		do
		{
			row = i - L;

			if(row < 0)
			{
				if(A[i][j] == days.get(i))
				{
					solution.add(i+1);
					i = i - L;
				}
				else if(A[i][j] == A[i-1][j])
				{
					i = i -1;
				}
				//A[i][j] = Math.max(A[i-1][j], days.get(i));
			}
			else
			{
				if(A[i][j] == (A[row][j-1] + days.get(i)))
				{
					solution.add(i+1);
					j = j -1;
					i = row;
				}
				else if(A[i][j] == A[i-1][j])
				{
					i = i -1;
				}
			
				//A[i][j] = Math.max(A[i-1][j], A[row][j-1] + days.get(i));
			}

		}while(i > 0 && j > 0);

		Collections.reverse(solution);
		return solution;
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
		P101 p = new P101();
		ArrayList<Integer> al = p.bestSolution(args[0]);
		
		for(int i = 0; i < al.size(); i++)
		{
			System.out.print(al.get(i) + " ");
		}
		
		System.out.println();
	}
}
