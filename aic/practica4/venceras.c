#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <unistd.h>
#include <math.h>
#include <stdio.h>


#include "pvm3.h"
#define NUMPROC 3
#define TAM 10
int main()
{
	int host;
	int j, k;
	int cc;
	int inicio, fin;
	int tids[2];

	int vector[TAM+2];
	int fib;


	//Máquina del padre
	host = pvm_parent();

	//Espero a recibir
	pvm_recv(host, 0);
	pvm_upkint(vector, TAM+2, 1);

	inicio = vector[TAM];
	fin = vector[TAM+1];

	if(fin - inicio <= 1)//Caso base
	{
		fib = vector[inicio];
		pvm_initsend(PvmDataDefault);
		pvm_pkint(&fib, 1, 1);
		pvm_send(host, 1);
	}
	else
	{
		//Divido en dos
		cc = pvm_spawn("venceras", (char**)0, 0, "", 2, tids);

		int rec1, rec2;

		//Primer envio
		pvm_initsend(PvmDataDefault);
		vector[TAM] = inicio;
		vector[TAM+1] = inicio + (fin - inicio)/2;
		pvm_pkint(vector, TAM+2, 1);
		pvm_send(tids[0], 0);

		//Recibo los datos del proceso hijo.
		pvm_recv(tids[0], 1);
		pvm_upkint(&rec1, 1, 1);

		//Segundo envio
		pvm_initsend(PvmDataDefault);
		vector[TAM] = inicio + (fin - inicio)/2;
		vector[TAM+1] = fin;
		pvm_pkint(vector, TAM+2, 1);
		pvm_send(tids[1], 0);

		//Recibo los datos del proceso hijo.
		pvm_recv(tids[1], 1);
		pvm_upkint(&rec2, 1, 1);

		if(rec1 > rec2)
		{
			fib = rec1;
		}
		else
		{
			fib = rec2;
		}

		//Envío AL PADRE
		pvm_initsend(PvmDataDefault);
		pvm_pkint(&fib, 1, 1);
		pvm_send(host, 1);

	}




	pvm_exit();
	exit(0);
}
////////////////////// Fin ESCLAVO
