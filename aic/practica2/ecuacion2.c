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
	int A[tamano][tamano], B[tamano][tamano], T[tamano][tamano];
	int RES[tamano][tamano];
	int inicio, fin;
	int inicio2, fin2;

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


	double secs = (double)(fin - inicio) / CLOCKS_PER_SEC;
	printf("Tiempo ecuación 1: %.16g segundos\n", secs);
	printf("Tiempo ecuación 1: %.16g milisegundos\n", secs * 1000.0);

	double secs2 = (double)(fin2 - inicio2) / CLOCKS_PER_SEC;
	printf("Tiempo ecuación 2: %.16g segundos\n", secs2);
	printf("Tiempo ecuación 2: %.16g milisegundos\n", secs2 * 1000.0);
	printf("Ganancia: %.16g\n", secs/secs2);
}
