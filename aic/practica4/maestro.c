#include <stdio.h>
#include <stdlib.h>
#include "pvm3.h"
#include <time.h>
#define NUMPROC 8
int main()
{
	int tids[NUMPROC];
	int cc, mytid; char buf[100];
	int i;
	long *tiempo_proceso;
	tiempo_proceso = (long *) calloc (NUMPROC, sizeof(long));
	mytid = pvm_mytid();
	printf("i'm t%x\n", mytid);
	cc = pvm_spawn("esclavo", (char**)0, 0, "", NUMPROC, tids);
	pvm_initsend (PvmDataDefault); pvm_pkint (tids, NUMPROC, 1); pvm_mcast (tids, NUMPROC, 0);
	if (cc > 1) {
		for (i=0; i <NUMPROC; i++) {
			cc = pvm_recv(tids[i], -1);
			pvm_bufinfo(cc, (int*)0, (int*)0, &tids[i]);
			pvm_upkstr(buf);
			printf("de la tarea t%x: %s\n", tids[i], buf);
		}
		for (i=0; i<NUMPROC; i++) {
			pvm_recv(tids[i], 0);
			pvm_upklong (&tiempo_proceso[i], 1, 1);
			printf("Tiempo del Proc[%d]=%ld\n", i, tiempo_proceso[i]);
		}
	} else
		printf("No puedo arrancar los esclavos, cc = %d\n", cc);
	free (tiempo_proceso);
	pvm_exit();
	exit(0);
}
////////////////////// Fin MAESTRO
