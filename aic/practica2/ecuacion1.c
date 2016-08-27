#include <stdlib.h>
#include <stdio.h>
#include <time.h>

const int N = 2;
void multiplicar(int);

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
	int A[tamano][tamano], B[tamano][tamano];
	int RES[tamano][tamano];
	int inicio, fin;

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

	/*printf("A\tB\tRES\n");
	for(i = 0; i < tamano; i++)
	{
		for(j = 0; j < tamano; j++)
		{
			printf("%d ", A[i][j]);
		}

		printf("\t");
		for(j = 0; j < tamano; j++)
		{
			printf("%d ", B[i][j]);
		}

		printf("\t");
		for(j = 0; j < tamano; j++)
		{
			printf("%d ", RES[i][j]);
		}
		printf("\n");
	}*/

	double secs = (double)(fin - inicio) / CLOCKS_PER_SEC;
	printf("%.16g segundos\n", secs);
	printf("%.16g milisegundos\n", secs * 1000.0);
}
