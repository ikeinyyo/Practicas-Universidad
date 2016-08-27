<?php


function guardarFotoRegistro($id, $user) {

	$fichero = $_FILES[$id];

	$mimes = array('image/png', 'image/jpg', 'image/jpeg');


	//echo "Fichero: " . $fichero['name'] . "<br/>";
	//echo "Mimes: " . print_r($mimes) . "<br/>";
	echo $fichero['type'] . "<br/>";
	


	if($fichero['size'] < 4194304)
	{
		if(in_array($fichero['type'], $mimes))
		{
			//echo "Correcta<br/>";
			$extension = substr( $fichero['name'], strrpos( $fichero['name'], '.')); 
			//echo "Archivo: $extension <br/>";
			$nuevo = "gestion/usuarios/". $user . $extension;
			//echo "Archivo: " . $nuevo . "<br/>";

			//Guardo el archivo
			move_uploaded_file($fichero["tmp_name"], $nuevo);

			$command = "UPDATE Usuarios set Foto = '$nuevo' where NomUsuario = '$user'";
			mysqlExecuteNonQuery($command); 

			//echo "Nuevo archivo: $nuevo";
		}
		else
		{
			echo "Formato Incorrecto<br/>";
		}
	}
	else
	{
		echo "Muy Grande: $fichero[size]<br/>";
	}

}

function guardarNuevaFoto($id, $nombre) {

	$fichero = $_FILES[$id];

	$mimes = array('image/png', 'image/jpg', 'image/jpeg');
	$nuevo = "";

	//echo "Fichero: " . $fichero['name'] . "<br/>";
	//echo "Mimes: " . print_r($mimes) . "<br/>";
	//echo $fichero['type'] . "<br/>";
	


	if($fichero['size'] < 4194304)
	{
		if(in_array($fichero['type'], $mimes))
		{
			//echo "Correcta<br/>";
			$extension = substr( $fichero['name'], strrpos( $fichero['name'], '.')); 
			//echo "Archivo: $extension <br/>";
			$nuevo = "gestion/images/". $nombre . $extension;
			//echo "Archivo: " . $nuevo . "<br/>";

			//Guardo el archivo
			move_uploaded_file($fichero["tmp_name"], $nuevo);
			return $nuevo;

			//echo "Nuevo archivo: $nuevo";
		}
		else
		{
			echo "Formato Incorrecto<br/>";
		}
	}
	else
	{
		echo "Muy Grande: $fichero[size]<br/>";
	}

}

?>