#include <stdio.h>
#include <stdlib.h>
#include "pvm3.h"
#include <time.h>
#define NUMPROC 3
#define TAM 5
//#define DEBUG
void printMatrix(float* m, int tam);
int main()
{
	int tids[NUMPROC];
	int cc;
	float matriz[TAM*TAM+1];
	int i,j,k, iter;

	for(iter = 0; iter < 1000; iter++)
	{

		cc = pvm_spawn("operarMatriz", (char**)0, 0, "", NUMPROC, tids);

		if (cc<1) {
			printf("Error al hacer pvm_spawn\n");
			pvm_exit();
			exit(1);
		}

		//Crear Matriz
		for(j = 0; j < TAM; j++)
		{
			for(k = 0; k < TAM; k++)
			{
				matriz[j*TAM + k] = j*TAM + k;
			}
		}

		for(i = 0; i < NUMPROC; i++)
		{

			matriz[TAM*TAM] = i;

			//Envío los datos
			pvm_initsend(PvmDataDefault);
			pvm_pkfloat(matriz, TAM*TAM+1, 1);
			pvm_send(tids[i], 0);

			#ifdef DEBUG
			printf("Iteración %d-%d: \nEntrada\n", i, iter);
			printMatrix(matriz, TAM);
			#endif

			//Recibo los datos del proceso hijo.
			pvm_recv(tids[i], 1);
			pvm_upkfloat(matriz, TAM*TAM+1, 1);

			#ifdef DEBUG
			printf("Salida\n");
			printMatrix(matriz, TAM);
			printf("======================\n");
			#endif


		}
	}

	pvm_exit();
	exit(0);
}

void printMatrix(float* m, int tam)
{
	int j, k;

	for(j = 0; j < tam; j++)
	{
		for(k = 0; k < tam; k++)
		{
			printf("%f ", m[j*tam + k]);
		}
		printf("\n");
	}
	printf("it: %f\n", m[tam*tam]);
}

////////////////////// Fin MAESTRO

