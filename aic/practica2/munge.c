#include <stdlib.h>
#include <stdio.h>

void Munge8( void *data, unsigned int size);
void Munge16( void *data, unsigned int size);
void Munge32( void *data, unsigned int size);
void Munge64( void *data, unsigned int size);

const int BUFFER_SIZE = 2000000000;
int main(int argc, char *argv[])
{

	int option, alignment, size;
	if(argc == 2 || argc == 3)
	{
		size = atoi(argv[1]);
		if(argc == 3)
		{
			alignment = atoi(argv[2]);
		}
		else
		{
			alignment = 0;
		}

		char * buffer = (char *) malloc (sizeof (char) * BUFFER_SIZE + alignment);
		buffer += alignment;

		switch(size)
		{
		case 8:
			Munge8(buffer, BUFFER_SIZE);
			break;
		case 16:
			Munge16(buffer, BUFFER_SIZE);
			break;
		case 32:
			Munge32(buffer, BUFFER_SIZE);
			break;
		case 64:
			Munge64(buffer, BUFFER_SIZE);
			break;
		default:
			printf("Error en los parámetros: munge 8|16|32|64 [0|1|2|3|4...]");
			break;
		}


	}
	else
	{
		printf("Error en los parámetros: munge 8|16|32|64 [0|1|2|3|4...]");
	}

	return 0;
}

void Munge8( void *data, unsigned int size) {
	unsigned char *data8 = ( unsigned char*) data;
	unsigned char *data8End = data8 + size;

	while( data8 != data8End ) {
		*data8++ = -*data8;
	}
}

void Munge16( void *data, unsigned int size) {
	unsigned short *data16 = (unsigned short*) data;
	unsigned short *data16End = data16 + (size >> 1); /* Divide size by 2. */
	unsigned char *data8 = (unsigned char*) data16End;
	unsigned char *data8End = data8 + (size & 0x00000001); /* Strip upper 31 bits. */

    while( data16 != data16End ) {
        *data16++ = -*data16;
    }
    while( data8 != data8End ) {
        *data8++ = -*data8;
    }
}


void Munge32(void *data, unsigned int size) {
	unsigned int *data32 = (unsigned int*) data;
	unsigned int *data32End = data32 + (size >> 2); /* Divide size by 4. */
	unsigned char *data8 = (unsigned char*) data32End;
	unsigned char *data8End = data8 + (size & 0x00000003); /* Strip upper 30 bits. */

    while( data32 != data32End ) {
        *data32++ = -*data32;
    }
    while( data8 != data8End ) {
        *data8++ = -*data8;
    }
}


void Munge64(void *data, unsigned int size) {
    double *data64 = (double*) data;
    double *data64End = data64 + (size >> 3); /* Divide size by 8. */
    unsigned char *data8 = (unsigned char*) data64End;
    unsigned char *data8End = data8 + (size & 0x00000007); /* Strip upper 29 bits. */

    while( data64 != data64End ) {
        *data64++ = -*data64;
    }
    while( data8 != data8End ) {
        *data8++ = -*data8;
    }
}
