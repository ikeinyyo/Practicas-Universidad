#!/bin/bash
#Script autocorrector PL
#Autores: Carlos Carrillo, Sergio Gallardo, Adrian Gonzalez
#Modo de uso:

#Llamada: pruebasPED dir_pruebas
Salida=".tmp"
EXE="java plp0"
inc=0
correctas=0

clear
echo "Autocorrector PL"

if (test -d ${1}); then
 
	for i in ${1}*.txt; do
		if (test -f ${i}); then 
			((inc = inc + 1))
			echo "Prueba - ${inc}"
			echo "Probando fichero:" ${i}
	
	
			echo "Ejecutando prueba..."
			${EXE} ${i} > ${Salida}
	
			echo "Comprobando salida..."
			if (diff -w  ${i}.sal ${Salida}); then
				boolsalida=true;
				echo "SALIDA CORRECTA"
			else
				boolsalida=false;
				echo "¡¡SALIDA FALLIDA!!"
			fi
			

			if ($boolsalida); then
				((correctas = correctas + 1))
			fi
			echo "_________________________________________________"
		else
			echo "No existe ningun fichero fuente *.txt"
			exit
		fi 
	done
	echo "Pruebas Correctas $correctas"/"$inc"
	if ((correctas == inc)); then
	echo "PRUEBAS FINALIZADAS - TODAS LAS PRUEBAS CORRECTAS :D"
	else
	echo "PRUEBAS FINALIZADAS - HAY PRUEBAS FALLIDAS"
	fi
else
echo "¡¡ERROR!!"
echo "[1]: \"" ${1} "\" No es un directorio válido"
echo "Modo de uso: pruebasPL.sh dir_pruebas"
fi
echo "FIN DEL AUTOCORRECTOR - by: Carlos Carrillo, Sergio Gallardo, Adrian Gonzalez"
rm -f .tmp
