package AA;

public class P002 {

	int[][] sudoku;
	final int TAM = 9;

	public P002()
	{

	}

	private void init(String data)
	{
		sudoku = new int[TAM][TAM];

		String[] filas = data.split("" + '\n');

		for(int i = 0; i < TAM; i++)
		{
			for(int j = 0; j < TAM; j++)
			{
				sudoku[i][j] = Integer.parseInt("" + filas[i].charAt(j));
			}
		}
	}

	public String sol(String data)
	{
		init(data);
		resolver(sudoku, 0, 0);
		return aString();
	}
	
	private void resolver(int[][] miSudoku, int fila, int columna)
	{
		if(fila < TAM && columna < TAM)
		{
			if(miSudoku[fila][columna] == 0)
			{
				
				for(int i = 1; i <= TAM; i++)
				{
					if(cabe(i, fila, columna, miSudoku))
					{
						int[][] nuevoSudoku = copiarSudoku(miSudoku);
						nuevoSudoku[fila][columna] = i;
						
						if(columna >= TAM -1)
						{
							resolver(nuevoSudoku, fila + 1, 0);
						}
						else
						{
							resolver(nuevoSudoku, fila, columna + 1);
						}
					}
				}
			}
			else
			{
				if(columna >= TAM -1)
				{
					resolver(copiarSudoku(miSudoku), fila + 1, 0);
				}
				else
				{
					resolver(copiarSudoku(miSudoku), fila, columna + 1);
				}
			}
		}
		else
		{
			sudoku = copiarSudoku(miSudoku);
		}
	}
	
	private boolean cabe(int numero, int fila, int columna, int[][] miSudoku)
	{
		boolean cabe = true;
		
		for(int i = 0; i < 9 && cabe; i++)
		{
			if(miSudoku[fila][i] == numero || miSudoku[i][columna] == numero)
			{
				cabe = false;
			}
		}
		
		if(cabe)
		{
			int fila0 = fila/3,columna0 = columna/3;
			
			for(int i = 0; i < 3 && cabe; i++)
			{
				for(int j = 0; j < 3 && cabe; j++)
				{
					if(miSudoku[fila0*3 + i][columna0*3 + j] == numero)
					{
						cabe = false;
					}
				}
			}
		}
		
		return cabe;
	}
	
	private int[][] copiarSudoku(int[][] original)
	{
		int[][] copia = new int[TAM][TAM];
		
		for(int i = 0; i < TAM; i++)
		{
			for(int j = 0; j < TAM; j++)
			{
				copia[i][j] = original[i][j];
			}
		}
		
		return copia;
	}
	
	private String aString()
	{
		String salida = "";
		
		for(int i = 0; i < 9; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				salida += sudoku[i][j];
			}
			
			salida += '\n';
		}
		
		return salida;
	}
	
	private boolean completo(int[][] miSudoku)
	{
		boolean completo = true;
		for(int i = 0; i < 9 && completo; i++)
		{
			for(int j = 0; j < 9 && completo; j++)
			{
				if(miSudoku[i][j] == 0)
				{
					completo = false;
				}
			}
		}
		
		return completo;
		
	}

	public static void main(String[] argv)
	{
		P002 p = new P002();
		
		System.out.print(p.sol(argv[0]));
		
	}
}