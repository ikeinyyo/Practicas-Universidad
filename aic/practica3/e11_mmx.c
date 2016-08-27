//MMX Ejemplo: Packed Sum and Absolute Difference
#include <stdio.h>
int main(int argc, char *argv[])
{
unsigned char buf1[8] = { 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F };
unsigned char buf2[8] = { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07 };
unsigned char *buf1_ptr = buf1;
unsigned char *buf2_ptr = buf2;
unsigned int out = 0;
/* <--- 32 bit integer */
__asm__ __volatile__ (
"movq (%1), %%mm0\n\t"
"movq (%2), %%mm1\n\t"
"psadbw %%mm1, %%mm0\n\t" // Packed Sum and Absolute Difference
"movd %%mm0, %0\n\t"
: "=m" (out)
: "r" (buf1_ptr),
"r" (buf2_ptr)
);

fprintf(stdout, "out = %08x (%d)\n", out, out);
//getchar();
return 0; /* <--- in C this has to be there !! */
}
