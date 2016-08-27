#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <unistd.h>
#include <math.h>
#include <stdio.h>


#include "pvm3.h"
#define NUMPROC 3
#define TAM 5
int main()
{
	int host;
	int j, k;

	float matriz[TAM*TAM+1];

	//Máquina del padre
	host = pvm_parent();

	//Espero a recibir
	pvm_recv(host, 0);
	pvm_upkfloat(matriz, TAM*TAM+1, 1);

	//Trabajo sobre la matriz
	int it = matriz[TAM*TAM];
	switch(it)
	{
	case 0:
		for(j = 0; j < TAM; j++)
		{
			for(k = 0; k < TAM; k++)
			{
				matriz[j*TAM + k] =  5*matriz[j*TAM + k];
			}
		}
		break;
	case 1:
		for(j = 0; j < TAM; j++)
		{
			for(k = 0; k < TAM; k++)
			{
				matriz[j*TAM + k] =  matriz[j*TAM + k]/2.0 + j + k;
			}
		}
		break;
	case 2:
		for(j = 0; j < TAM; j++)
		{
			for(k = 0; k < TAM; k++)
			{
				matriz[j*TAM + k] = matriz[j*TAM + k] * sqrt(2.0);
			}
		}
		break;

	}

	//Envío
	pvm_initsend(PvmDataDefault);
	pvm_pkfloat(matriz, TAM*TAM+1, 1);
	pvm_send(host, 1);

	pvm_exit();
	exit(0);
}
////////////////////// Fin ESCLAVO
