#include <stdio.h>
#include <stdlib.h>
#include "pvm3.h"
#include <time.h>
#define NUMPROC 1
#define TAM 10
#define DEBUG
void printVector(int* m, int tam);
int main()
{
	srand (time(NULL));

	int tids[NUMPROC];
	int cc;
	int vector[TAM+2];
	int i,j,k, iter;

	int fib = TAM;
	//for(iter = 0; iter < 1000; iter++)
	//{

	cc = pvm_spawn("venceras", (char**)0, 0, "", NUMPROC, tids);

	if (cc<1) {
		printf("Error al hacer pvm_spawn\n");
		pvm_exit();
		exit(1);
	}

	//Crear Vector
	for(i = 0; i < TAM; i++)
	{
		vector[i] = rand() % 100;
	}

	vector[TAM] = 0;
	vector[TAM+1] = TAM;

	for(i = 0; i < NUMPROC; i++)
	{
		//Envío los datos
		pvm_initsend(PvmDataDefault);
		pvm_pkint(vector, TAM+2, 1);
		pvm_send(tids[i], 0);
#ifdef DEBUG
		printf("Entrada:\n");
		printVector(vector, TAM);
#endif

		//Recibo los datos del proceso hijo.
		pvm_recv(tids[i], 1);
		pvm_upkint(&fib, 1, 1);

#ifdef DEBUG
		printf("Salida: %i\n", fib);
#endif


		//}
	}

	pvm_exit();
	exit(0);
}

void printVector(int* m, int tam)
{
	int j;

	for(j = 0; j < tam; j++)
	{
		printf("%i, ", m[j]);
	}
	printf("\n");
}

////////////////////// Fin MAESTRO

