<?php

function mysqliQuery($query)
{

	//echo "<p>Sentencia: " . $query . "</p>";

	$iden = @new mysqli('localhost', 'root', '', 'pibd');

	if(mysqli_connect_errno()) {
		echo "<p>Error al conectar con la base de datos: " . mysqli_connect_error();
		echo "</p>";
		exit;
	}
	//mysql_query("SET NAMES 'utf8'");
	$iden->set_charset("utf8");

	// Ejecuta la sentencia SQL
	$resultado = $iden->query($query);
	if(!$resultado)
		die("Error: no se pudo realizar la consulta");

	return $resultado;
}

function mysqliExecuteNonQuery($command) 
{
	// Se conecta al SGBD
	/*if(!($iden = mysql_connect("localhost", "root", "")))
		die("Error: No se pudo conectar");

	// Selecciona la base de datos
	if(!mysql_select_db("pibd", $iden))
		die("Error: No existe la base de datos");
	mysql_query("SET NAMES 'utf8'");

	// Ejecuta la sentencia SQL
	$resultado = mysql_query($command, $iden);
	if(!$resultado)
		die("Error: no se pudo realizar la consulta");

	mysql_close($iden);*/
} 


function mostrarAlbumesByUser($user) {

	$sentencia = "SELECT * from Albumes where Usuario = '$user'";

	$resultado = mysqliQuery($sentencia);

	echo "<ul>\n";

	while($fila = $resultado->fetch_assoc())
	{
		echo "<li>\n";
		echo "<a href=\"iverAlbum.php?id=$fila[IdAlbum]\" class=\"marcoImagen\">\n";

		//PAIS
	    // Sentencia SQL: muestra todo el contenido de la tabla "books"
		$sentencia = "SELECT * FROM Fotos where Album='$fila[IdAlbum]' order by FRegistro DESC limit 1";
		//echo $sentencia;
		// Ejecuta la sentencia SQL
		$resultado2 = mysqliQuery($sentencia);
		if(!$resultado2)
			die("Error: no se pudo realizar la consulta");

		if($foto = $resultado2->fetch_assoc())	
		{
			echo "<img class=\"image\" src=\"$foto[Fichero]\" alt=\"Imagen1\"/>\n";
		}
		else
		{
			echo "<img class=\"image\" src=\"images/default.jpg\" alt=\"Imagen1\"/>\n";
		}
     	echo "Título: $fila[Titulo]<br/>\n";
	    echo "Fecha: $fila[Fecha]<br/>\n";

	    //PAIS
	    // Sentencia SQL: muestra todo el contenido de la tabla "books"
		$sentencia = "SELECT * FROM Paises where IdPais='$fila[Pais]'";
		//echo $sentencia;
		// Ejecuta la sentencia SQL
		$resultado2 = mysqliQuery($sentencia);
		if(!$resultado2)
			die("Error: no se pudo realizar la consulta");

		$pais = $resultado2->fetch_assoc();

	    echo "Pais: $pais[NomPais]\n";//    País: España<br/>

	    //FIN PAIS

	    echo "</a>\n";
	    echo "</li>\n";


		$resultado2->Close();
	}

	echo "</ul>\n";

	$resultado->Close();
}

function iverAlbum($id) {

	$sentencia = "SELECT * FROM Fotos where Album='$id' order by Fecha DESC";
	//echo $sentencia;
	// Ejecuta la sentencia SQL
	$resultado = mysqliQuery($sentencia);
	if(!$resultado)
		die("Error: no se pudo realizar la consulta");

	echo "<ul>\n";

	//while($fila = mysql_fetch_assoc($resultado))
	while($fila = $resultado->fetch_assoc())
	{
		echo "<li>\n";
		echo "<a href=\"detalles.php?id=$fila[IdFoto]\" class=\"marcoImagen\">\n";
		echo "<img class=\"image\" src=\"$fila[Fichero]\" alt=\"Imagen1\"/>\n";
     	echo "Título: $fila[Titulo]<br/>\n";
	    echo "Fecha: $fila[Fecha]<br/>\n";

	    //PAIS
	    // Sentencia SQL: muestra todo el contenido de la tabla "books"
		$sentencia = "SELECT * FROM Paises where IdPais='$fila[Pais]'";
		//echo $sentencia;
		// Ejecuta la sentencia SQL
		$resultado2 = mysqliQuery($sentencia);
		if(!$resultado2)
			die("Error: no se pudo realizar la consulta");

		//$pais = mysql_fetch_assoc($resultado2);

		$pais = $resultado2->fetch_assoc();
	    echo "Pais: $pais[NomPais]\n";//    País: España<br/>


		//mysql_free_result($resultado2);

		$resultado2->Close();
	    //FIN PAIS

	    echo "</a>\n";
	    echo "</li>\n";
	}

	echo "</ul>\n";

	$resultado->Close();

}

?>