<?php
	
	include "php/libsql.php";

	$foto = getSeleccionada();

	$json = json_encode($foto);

	echo $json;

?>