#include <stdlib.h>
#include <stdio.h>
#include <time.h>

const int N = 2;
void multiplicar(int);
int min(int a, int b);

int main(int argc, char *argv[])
{

	int tamano;

	if(argc == 2)
	{
		tamano = atoi(argv[1]);
	}
	else
	{
		tamano = N;
	}



	multiplicar(tamano);

	return 0;
}

void multiplicar(int tamano)
{

	int i, j , k;
	int A[tamano][tamano], B[tamano][tamano], T[tamano][tamano];
	int RES[tamano][tamano];
	int inicio, fin;
	int inicio2, fin2;
	int inicio3, fin3;

	srand(time(NULL));

	for(i = 0; i < tamano; i++)
	{
		for(j = 0; j < tamano; j++)
		{
			A[i][j] = rand()%10;
			B[i][j] = rand()%10;
		}
	}

	for(i = 0;i < tamano;i++)
	{
		for(j = 0;j < tamano;j++)
		{
			RES[i][j] = 0;
		}
	}

	//Tomo el valor inicial del reloj
	inicio = clock();

	for(i = 0;i < tamano;i++)
	{
		for(j = 0;j < tamano;j++)
		{
			for(k = 0;k < tamano;k++)
			{
				RES[i][j] += A[i][k] * B[k][j];
			}
		}
	}
	//Tomo el valor final del reloj
	fin = clock();

	/*printf("1\n");
	for(i = 0; i < tamano; i++)
	{
		for(j = 0; j < tamano; j++)
		{
			printf("%d ", RES[i][j]);
		}

		printf("\n");
	}*/

	for(i = 0;i < tamano;i++)
	{
		for(j = 0;j < tamano;j++)
		{
			RES[i][j] = 0;
		}
	}

	//Transponer matriz
	for(i = 0;i < tamano;i++)
	{
		for(j = 0;j < tamano;j++)
		{
			T[i][j] = B[j][i];
		}
	}

	//Tomo el valor inicial del reloj
	inicio2 = clock();

	for(i = 0;i < tamano;i++)
	{
		for(j = 0;j < tamano;j++)
		{
			for(k = 0;k < tamano;k++)
			{
				//RES[i][j] += A[i][k] * B[k][j];
				RES[i][j] += A[i][k] * T[j][k];
			}
		}
	}
	//Tomo el valor final del reloj
	fin2 = clock();

	/*printf("2\n");
	for(i = 0; i < tamano; i++)
	{
		for(j = 0; j < tamano; j++)
		{
			printf("%d ", RES[i][j]);
		}

		printf("\n");
	}*/

	int tByte = 192;
	int jj, kk;
	int r;

	for(i = 0;i < tamano;i++)
	{
		for(j = 0;j < tamano;j++)
		{
			RES[i][j] = 0;
		}
	}

	inicio3 = clock();
	for (jj = 0; jj < tamano; jj = jj + tByte)
		for (kk = 0; kk < tamano; kk = kk + tByte)
			for (i = 0; i < tamano; i = i + 1)
				for (j = jj; j < min(jj + tByte, tamano); j = j + 1)
				{  r = 0;
				for (k = kk; k  <  min(kk + tByte,tamano); k = k + 1)
				{ r = r + A[i][k] *T[j][k];};
				RES[i][j] = RES[i][j] +r;
				};
	fin3 = clock();

	/*printf("3\n");
	for(i = 0; i < tamano; i++)
	{
		for(j = 0; j < tamano; j++)
		{
			printf("%d ", RES[i][j]);
		}

		printf("\n");
	}*/


	double secs = (double)(fin - inicio) / CLOCKS_PER_SEC;
	printf("Tiempo ecuación 1: %.16g segundos\n", secs);
	printf("Tiempo ecuación 1: %.16g milisegundos\n", secs * 1000.0);

	double secs2 = (double)(fin2 - inicio2) / CLOCKS_PER_SEC;
	printf("Tiempo ecuación 2: %.16g segundos\n", secs2);
	printf("Tiempo ecuación 2: %.16g milisegundos\n", secs2 * 1000.0);

	double secs3 = (double)(fin3 - inicio3) / CLOCKS_PER_SEC;
	printf("Tiempo ecuación 3: %.16g segundos\n", secs3);
	printf("Tiempo ecuación 3: %.16g milisegundos\n", secs3 * 1000.0);
	printf("Ganancia (1-2): %.16g\n", secs/secs2);
	printf("Ganancia (1-3): %.16g\n", secs/secs3);
	printf("Ganancia (2-3): %.16g\n", secs2/secs3);
}

int min(int a, int b)
{
	int minimo = b;

	if(a < b)
	{
		minimo = a;
	}

	return minimo;
}
