#!/bin/bash
if test "$#" == 1; then
	java -classpath antlr-3.5-complete.jar:. plp3  $1 > $1.il;
	cat $1.il
elif test "$#" == 2; then
	if test "$2" == "-c"; then
	  ilasm  $1.il
	elif test "$2" == "-e"; then
	  mono  $1.exe;
	elif test "$2" == "-ce"; then
	  ilasm  $1.il	
	  mono  $1.exe;
	elif test "$2" == "-cc"; then
	  java -classpath antlr-3.5-complete.jar:. plp3  $1 > $1.il;
	  ilasm  $1.il	
	elif test "$2" == "-a"; then
	  java -classpath antlr-3.5-complete.jar:. plp3  $1 > $1.il;
	  ilasm  $1.il
 	  mono  $1.exe;
	fi

else
	echo "Normas de uso:";
	echo "ejecutar.sh fichero [opt]";
	echo "opt: -c => Compilar el il a exe";
	echo "opt: -e => Ejecuta el exe";
	echo "opt: -ce => Compilar el il a exe y lo ejecuta";
	echo "opt: -cc => Ejecuta el traductor y compila el il a exe";
	echo "opt: -a => Ejecuta el traductor y compila el il a exe y lo ejecuta";
fi
