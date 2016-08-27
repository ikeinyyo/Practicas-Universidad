#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <unistd.h>

#include "pvm3.h"
#define NUMPROC 8
int main()
{
	int mytid;
	int tids [NUMPROC]; int mi_pid = -1, host; char buf[100];
	int i;
	time_t t_inicial, t_final, t_total, t_aux; t_inicial = t_final = t_total = 0; t_inicial = time
	(&t_aux);
	mytid = pvm_mytid();
	host = pvm_parent();
	pvm_recv (host, 0);
	pvm_upkint (tids, NUMPROC, 1);
	for (i=0; i < NUMPROC; i++) {
		if (mytid == tids [i]) {
			mi_pid = i;
			break;
		}
	}
	strcpy(buf, "hola, desde ");
	gethostname(buf + strlen(buf), 64);
	pvm_initsend(PvmDataDefault);
	pvm_pkstr(buf);
	pvm_send(host, 1);
	sleep (mi_pid+5); t_final = time(&t_aux); t_total = t_final - t_inicial;
	pvm_initsend(PvmDataDefault); pvm_pklong(&t_total, 1, 1); pvm_send(host, 0);
	pvm_exit();
	exit(0);
}
////////////////////// Fin ESCLAVO
