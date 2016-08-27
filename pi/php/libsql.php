
<?php

function llenarPaises($id)
{
	// Se conecta al SGBD
	if(!($iden = mysql_connect("localhost", "root", "")))
		die("Error: No se pudo conectar");

	// Selecciona la base de datos
	if(!mysql_select_db("pibd", $iden))
		die("Error: No existe la base de datos");
	mysql_query("SET NAMES 'utf8'");

	// Sentencia SQL: muestra todo el contenido de la tabla "books"
	$sentencia = "SELECT * FROM Paises";
	//echo $sentencia;
	// Ejecuta la sentencia SQL
	$resultado = mysql_query($sentencia, $iden);
	if(!$resultado)
		die("Error: no se pudo realizar la consulta");

	echo "<select id=\"$id\" name=\"$id\">";

	while($fila = mysql_fetch_assoc($resultado))
	{
		//echo $fila['IdUsuario'];
		echo "<option value=\"$fila[IdPais]\">$fila[NomPais]</option>";
	}

	echo "</select>";

	mysql_free_result($resultado);
	mysql_close($iden);
}

function llenarPaisesSeleccion($id, $seleccion)
{
	// Se conecta al SGBD
	if(!($iden = mysql_connect("localhost", "root", "")))
		die("Error: No se pudo conectar");

	// Selecciona la base de datos
	if(!mysql_select_db("pibd", $iden))
		die("Error: No existe la base de datos");
	mysql_query("SET NAMES 'utf8'");

	// Sentencia SQL: muestra todo el contenido de la tabla "books"
	$sentencia = "SELECT * FROM Paises";
	//echo $sentencia;
	// Ejecuta la sentencia SQL
	$resultado = mysql_query($sentencia, $iden);
	if(!$resultado)
		die("Error: no se pudo realizar la consulta");

	echo "<select id=\"$id\" name=\"$id\">";

	while($fila = mysql_fetch_assoc($resultado))
	{
		if($fila['IdPais'] == $seleccion)
		{
			echo "<option value=\"$fila[IdPais]\" selected=\"selected\">$fila[NomPais]</option>";
		}
		else
		{
			echo "<option value=\"$fila[IdPais]\">$fila[NomPais]</option>";
		}
	}

	echo "</select>";

	mysql_free_result($resultado);
	mysql_close($iden);
}

function ultimasFotos() {

	// Se conecta al SGBD
	if(!($iden = mysql_connect("localhost", "root", "")))
		die("Error: No se pudo conectar");

	// Selecciona la base de datos
	if(!mysql_select_db("pibd", $iden))
		die("Error: No existe la base de datos");
	mysql_query("SET NAMES 'utf8'");

	// Sentencia SQL: muestra todo el contenido de la tabla "books"
	$sentencia = "SELECT * FROM Fotos order by Fecha DESC limit 6";
	//echo $sentencia;
	// Ejecuta la sentencia SQL
	$resultado = mysql_query($sentencia, $iden);
	if(!$resultado)
		die("Error: no se pudo realizar la consulta");

	echo "<ul>\n";

	while($fila = mysql_fetch_assoc($resultado))
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
		$resultado2 = mysql_query($sentencia, $iden);
		if(!$resultado2)
			die("Error: no se pudo realizar la consulta");

		$pais = mysql_fetch_assoc($resultado2);

	    echo "Pais: $pais[NomPais]\n";//    País: España<br/>

		mysql_free_result($resultado2);

	    //FIN PAIS

	    echo "</a>\n";
	    echo "</li>\n";
	}

	echo "</ul>\n";

	mysql_free_result($resultado);
	mysql_close($iden);
}

function buscarFotos($titulo, $fecha, $pais) {

	// Se conecta al SGBD
	if(!($iden = mysql_connect("localhost", "root", "")))
		die("Error: No se pudo conectar");

	// Selecciona la base de datos
	if(!mysql_select_db("pibd", $iden))
		die("Error: No existe la base de datos");
	mysql_query("SET NAMES 'utf8'");

	// Sentencia SQL: muestra todo el contenido de la tabla "books"
	$sentencia = "SELECT * FROM Fotos where Titulo like '%$titulo%' or Pais = '$pais' or Fecha = '$fecha'";
	//echo $sentencia;
	// Ejecuta la sentencia SQL
	$resultado = mysql_query($sentencia, $iden);
	if(!$resultado)
		die("Error: no se pudo realizar la consulta");

	echo "<ul id=\"listaResultados\">\n";

    while($fila = mysql_fetch_assoc($resultado))
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
		$resultado2 = mysql_query($sentencia, $iden);
		if(!$resultado2)
			die("Error: no se pudo realizar la consulta");

		$pais = mysql_fetch_assoc($resultado2);

	    echo "Pais: $pais[NomPais]\n";//    País: España<br/>


		mysql_free_result($resultado2);
	    //FIN PAIS

	    echo "</a>\n";
	    echo "</li>\n";
	}

	echo "</ul>\n";

	mysql_free_result($resultado);
	mysql_close($iden);
}

function mostrarFoto($id) {

// Se conecta al SGBD
	if(!($iden = mysql_connect("localhost", "root", "")))
		die("Error: No se pudo conectar");

	// Selecciona la base de datos
	if(!mysql_select_db("pibd", $iden))
		die("Error: No existe la base de datos");
	mysql_query("SET NAMES 'utf8'");

	// Sentencia SQL: muestra todo el contenido de la tabla "books"
	$sentencia = "SELECT * FROM Fotos where IdFoto = '$id'";
	//echo $sentencia;
	// Ejecuta la sentencia SQL
	$resultado = mysql_query($sentencia, $iden);
	if(!$resultado)
		die("Error: no se pudo realizar la consulta");


    while($fila = mysql_fetch_assoc($resultado))
	{
		echo "<div class=\"marcoImagen\">";
        echo "<img class=\"image\" src=\"$fila[Fichero]\" alt=\"\"/>";
        echo "<p>Título: $fila[Titulo]</p>";
        echo "<p>Fecha: $fila[Fecha]</p>";

        //PAIS
	    // Sentencia SQL: muestra todo el contenido de la tabla "books"
		$sentencia = "SELECT * FROM Paises where IdPais='$fila[Pais]'";
		//echo $sentencia;
		// Ejecuta la sentencia SQL
		$resultado2 = mysql_query($sentencia, $iden);
		if(!$resultado2)
			die("Error: no se pudo realizar la consulta");

		$tabla = mysql_fetch_assoc($resultado2);

	    echo "<p>Pais: $tabla[NomPais]</p>\n";//    País: España<br/>

	    //FIN PAIS

		//ÁLBUM
	    // Sentencia SQL: muestra todo el contenido de la tabla "books"
		$sentencia = "SELECT * FROM Albumes where IdAlbum='$fila[Album]'";
		//echo $sentencia;
		// Ejecuta la sentencia SQL
		$resultado2 = mysql_query($sentencia, $iden);
		if(!$resultado2)
			die("Error: no se pudo realizar la consulta");

		$tabla = mysql_fetch_assoc($resultado2);

	    echo "<p>Álbum: $tabla[Titulo]</p>\n";//    País: España<br/>

	    //FIN ÁLBUM

        //ÁLBUM
	    // Sentencia SQL: muestra todo el contenido de la tabla "books"
		$sentencia = "SELECT * FROM Usuarios where IdUsuario='$tabla[Usuario]'";
		//echo $sentencia;
		// Ejecuta la sentencia SQL
		$resultado2 = mysql_query($sentencia, $iden);
		if(!$resultado2)
			die("Error: no se pudo realizar la consulta");

		$tabla = mysql_fetch_assoc($resultado2);

	    echo "<p>Usuario: <a href='detallesUsuario.php?id=$tabla[IdUsuario]'>$tabla[NomUsuario]</a></p>\n";//    País: España<br/>

	    //FIN ÁLBUM
    	echo "</div>";


		mysql_free_result($resultado2);
	}

	mysql_free_result($resultado);
	mysql_close($iden);

}

function mostrarMisAlbumes() {

	// Se conecta al SGBD
	if(!($iden = mysql_connect("localhost", "root", "")))
		die("Error: No se pudo conectar");

	// Selecciona la base de datos
	if(!mysql_select_db("pibd", $iden))
		die("Error: No existe la base de datos");
	mysql_query("SET NAMES 'utf8'");

	// Sentencia SQL: muestra todo el contenido de la tabla "books"
	$sentencia = "SELECT * FROM Albumes where Usuario='$_SESSION[IdUser]'";
	//echo $sentencia;
	// Ejecuta la sentencia SQL
	$resultado = mysql_query($sentencia, $iden);
	if(!$resultado)
		die("Error: no se pudo realizar la consulta");

	echo "<ul>\n";

	echo "<form method='POST' action=\"eliminarAlbumes.php\">";
	while($fila = mysql_fetch_assoc($resultado))
	{
		echo "<input type=\"checkbox\" name=\"album_$fila[IdAlbum]\" value=\"$fila[IdAlbum]\" />$fila[Titulo]<br />";
	}
	echo "<input type=\"submit\" value=\"Eliminar Albumes\">";
	echo "</form>";
	$resultado = mysql_query($sentencia, $iden);
	while($fila = mysql_fetch_assoc($resultado))
	{
		echo "<li>\n";
		echo "<a href=\"veralbum.php?id=$fila[IdAlbum]\" class=\"marcoImagen\">\n";

		//PAIS
	    // Sentencia SQL: muestra todo el contenido de la tabla "books"
		$sentencia = "SELECT * FROM Fotos where Album='$fila[IdAlbum]' order by FRegistro DESC limit 1";
		//echo $sentencia;
		// Ejecuta la sentencia SQL
		$resultado2 = mysql_query($sentencia, $iden);
		if(!$resultado2)
			die("Error: no se pudo realizar la consulta");

		if($foto = mysql_fetch_assoc($resultado2))	
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
		$resultado2 = mysql_query($sentencia, $iden);
		if(!$resultado2)
			die("Error: no se pudo realizar la consulta");

		$pais = mysql_fetch_assoc($resultado2);

	    echo "Pais: $pais[NomPais]\n";//    País: España<br/>

	    //FIN PAIS

	    echo "</a>\n";
	    echo "</li>\n";


		mysql_free_result($resultado2);
	}

	echo "</ul>\n";

	mysql_free_result($resultado);
	mysql_close($iden);
}

function verAlbum($id) {

	// Se conecta al SGBD
	if(!($iden = mysql_connect("localhost", "root", "")))
		die("Error: No se pudo conectar");

	// Selecciona la base de datos
	if(!mysql_select_db("pibd", $iden))
		die("Error: No existe la base de datos");
	mysql_query("SET NAMES 'utf8'");

	// Sentencia SQL: muestra todo el contenido de la tabla "books"
	$sentencia = "SELECT * FROM Fotos where Album='$id' order by Fecha DESC";
	//echo $sentencia;
	// Ejecuta la sentencia SQL
	$resultado = mysql_query($sentencia, $iden);
	if(!$resultado)
		die("Error: no se pudo realizar la consulta");

	echo "<ul>\n";

	while($fila = mysql_fetch_assoc($resultado))
	{
		echo "<li class=\"marcoImagen\">\n";
		echo "<a href=\"detalles.php?id=$fila[IdFoto]\" >\n";
		echo "<img class=\"image\" src=\"$fila[Fichero]\" alt=\"Imagen1\"/>\n";
     	echo "Título: $fila[Titulo]<br/>\n";
	    echo "Fecha: $fila[Fecha]<br/>\n";

	    //PAIS
	    // Sentencia SQL: muestra todo el contenido de la tabla "books"
		$sentencia = "SELECT * FROM Paises where IdPais='$fila[Pais]'";
		//echo $sentencia;
		// Ejecuta la sentencia SQL
		$resultado2 = mysql_query($sentencia, $iden);
		if(!$resultado2)
			die("Error: no se pudo realizar la consulta");

		$pais = mysql_fetch_assoc($resultado2);

	    echo "Pais: $pais[NomPais]\n";//    País: España<br/>



		mysql_free_result($resultado2);
	    //FIN PAIS

	    echo "</a>\n";

	    echo "<form action=\"eliminarFoto.php\" method='POST'>
	    <input type=\"hidden\" value=\"$fila[IdFoto]\" name=\"id_foto\">
	    <input type=\"submit\" value=\"Eliminar\">
	    </form>";
	    echo "</li>\n";
	}

	echo "</ul>\n";

	mysql_free_result($resultado);
	mysql_close($iden);

}

function llenarAlbumes($id)
{
	// Se conecta al SGBD
	if(!($iden = mysql_connect("localhost", "root", "")))
		die("Error: No se pudo conectar");

	// Selecciona la base de datos
	if(!mysql_select_db("pibd", $iden))
		die("Error: No existe la base de datos");
	mysql_query("SET NAMES 'utf8'");

	// Sentencia SQL: muestra todo el contenido de la tabla "books"
	$sentencia = "SELECT * FROM Albumes where Usuario = '$_SESSION[IdUser]'";
	//echo $sentencia;
	// Ejecuta la sentencia SQL
	$resultado = mysql_query($sentencia, $iden);
	if(!$resultado)
		die("Error: no se pudo realizar la consulta");

	echo "<select id=\"$id\" name=\"$id\">";

	while($fila = mysql_fetch_assoc($resultado))
	{
		//echo $fila['IdUsuario'];
		echo "<option value=\"$fila[IdAlbum]\">$fila[Titulo]</option>";
	}

	echo "</select>";

	mysql_free_result($resultado);
	mysql_close($iden);
}

function obtenerUser($id)
{

	$host = $_SERVER['HTTP_HOST'];
	$uri = rtrim(dirname($_SERVER['PHP_SELF']), '/\\');


	// Se conecta al SGBD
	if(!($iden = mysql_connect("localhost", "root", "")))
		die("Error: No se pudo conectar");

	// Selecciona la base de datos
	if(!mysql_select_db("pibd", $iden))
		die("Error: No existe la base de datos");

	// Sentencia SQL: muestra todo el contenido de la tabla "books"
	$sentencia = "SELECT * FROM Usuarios where NomUsuario = '$id'";
	// Ejecuta la sentencia SQL
	$resultado = mysql_query($sentencia, $iden);
	if(!$resultado)
		die("Error: no se pudo realizar la consulta");

	if($fila = mysql_fetch_assoc($resultado))
		$_SESSION['IdUser'] = $fila['IdUsuario'];

	mysql_free_result($resultado);
	mysql_close($iden);
	
}

function mysqlQuery($query)
{
	// Se conecta al SGBD
	if(!($iden = mysql_connect("localhost", "root", "")))
		die("Error: No se pudo conectar");

	// Selecciona la base de datos
	if(!mysql_select_db("pibd", $iden))
		die("Error: No existe la base de datos");
	mysql_query("SET NAMES 'utf8'");

	// Ejecuta la sentencia SQL
	$resultado = mysql_query($query, $iden);
	if(!$resultado)
		die("Error: no se pudo realizar la consulta");

	return $resultado;
}

function mysqlExecuteNonQuery($command) 
{
	// Se conecta al SGBD
	if(!($iden = mysql_connect("localhost", "root", "")))
		die("Error: No se pudo conectar");

	// Selecciona la base de datos
	if(!mysql_select_db("pibd", $iden))
		die("Error: No existe la base de datos");
	mysql_query("SET NAMES 'utf8'");

	// Ejecuta la sentencia SQL
	$resultado = mysql_query($command, $iden);
	if(!$resultado)
		die("Error: no se pudo realizar la consulta");

	mysql_close($iden);

} 

function getPais($id)
{

	// Se conecta al SGBD
	if(!($iden = mysql_connect("localhost", "root", "")))
		die("Error: No se pudo conectar");

	// Selecciona la base de datos
	if(!mysql_select_db("pibd", $iden))
		die("Error: No existe la base de datos");
	mysql_query("SET NAMES 'utf8'");

    //PAIS
    // Sentencia SQL: muestra todo el contenido de la tabla "books"
	$sentencia = "SELECT * FROM Paises where IdPais='$id'";
	//echo $sentencia;
	// Ejecuta la sentencia SQL
	$resultado2 = mysql_query($sentencia, $iden);
	if(!$resultado2)
		die("Error: no se pudo realizar la consulta");

	$tabla = mysql_fetch_assoc($resultado2);


    return $tabla['NomPais'];
}

function getAlbum($id)
{

	// Se conecta al SGBD
	if(!($iden = mysql_connect("localhost", "root", "")))
		die("Error: No se pudo conectar");

	// Selecciona la base de datos
	if(!mysql_select_db("pibd", $iden))
		die("Error: No existe la base de datos");
	mysql_query("SET NAMES 'utf8'");

    //PAIS
    // Sentencia SQL: muestra todo el contenido de la tabla "books"
	$sentencia = "SELECT * FROM Albumes where IdAlbum='$id'";
	//echo $sentencia;
	// Ejecuta la sentencia SQL
	$resultado2 = mysql_query($sentencia, $iden);
	if(!$resultado2)
		die("Error: no se pudo realizar la consulta");

	$tabla = mysql_fetch_assoc($resultado2);


    return $tabla['Titulo'];
}

function fotoSeleccionada() {


		$id = leeIdSeleccionado();
		$resultado = mysqlQuery("SELECT * FROM Fotos where IdFoto = '$id'");

		$fila = mysql_fetch_assoc($resultado);

		echo "<div id=\"seleccionada\">";
		echo "<a href=\"detalles.php?id=$fila[IdFoto]\"  class=\"marcoImagenSeleccionada\">\n";
		echo "<img class=\"imageSeleccionada\" src=\"$fila[Fichero]\" alt=\"Imagen1\"/>\n";
		echo "<h3>Foto Seleccionada</h3>";
     	echo "Título: $fila[Titulo]<br/>\n";
	    echo "Fecha: $fila[Fecha]<br/>\n";

	    //PAIS
	    // Sentencia SQL: muestra todo el contenido de la tabla "books"
		$sentencia = "SELECT * FROM Paises where IdPais='$fila[Pais]'";
		$resultado2 = mysqlQuery($sentencia );

		$pais = mysql_fetch_assoc($resultado2);

	    echo "Pais: $pais[NomPais]\n";//    País: España<br/>

		mysql_free_result($resultado2);

	    //FIN PAIS

	    echo "</a>\n";
	    echo "</div>";
}

function getSeleccionada()
{
	$id = leeIdSeleccionado();
	$resultado = mysqlQuery("SELECT * FROM Fotos where IdFoto = '$id'");

	$fila = mysql_fetch_assoc($resultado);

	$sentencia = "SELECT * FROM Paises where IdPais='$fila[Pais]'";
	$resultado2 = mysqlQuery($sentencia );

	$pais = mysql_fetch_assoc($resultado2);

	$fila['Pais'] = $pais['NomPais'];

	return $fila;
}

function leeIdSeleccionado() {


	chdir("gestion/foto_seleccionada");

	$seleccion = array();
	if(($fichero = @file("seleccion.txt")) == false)
		echo "No se ha podido abrir el fichero";
	else
	{

		foreach($fichero as $numLinea => $linea)
		{
			//echo "Línea #<b>" . sprintf("%03d", $numLinea) . "</b> : ";
			//echo htmlspecialchars($linea);
			array_push($seleccion, $linea);
		}
	}

	$foto = $seleccion[rand(0,count($seleccion)-1)];
	//echo count($seleccion);
	//print_r($seleccion);
	//echo "Foto seleccionada: $foto";

	return $foto;
}

function mostrarFotoPerfil($user) {

	$sentencia = "SELECT * from Usuarios where NomUsuario = '$user'";
    $resultado = mysqlQuery($sentencia);
    if($resultado)
    {

    	$usuario = mysql_fetch_assoc($resultado);
    
    }
    else
    	return;

	echo "<img class=\"image\" src=\"$usuario[Foto]\" alt=\"Imagen1\"/>\n";

}

function mostrarFotoByArchivo($archivo) {

	$sentencia = "SELECT * from Fotos where Fichero = '$archivo'";
    $resultado = mysqlQuery($sentencia);
    if($resultado)
    {

    	$foto = mysql_fetch_assoc($resultado);
    
    }
    else
    	return;

	echo "<img class=\"image\" src=\"$foto[Fichero]\" alt=\"Imagen1\"/>\n";

}

?>