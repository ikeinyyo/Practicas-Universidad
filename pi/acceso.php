<?php

	session_start();
	$login = $_POST['login'];
	$pass = $_POST['pass'];
	$remember = false; 

	if(isset($_POST['recordar']) && $_POST['recordar'] == 'recordar')
	{
  	 	$remember = true;
	}


	echo "<p>Login: $login</p>";
	echo "<p>Pass: $pass</p>";
	


	$host = $_SERVER['HTTP_HOST'];
	$uri = rtrim(dirname($_SERVER['PHP_SELF']), '/\\');


	// Se conecta al SGBD
	if(!($iden = mysql_connect("localhost", "root", "")))
		die("Error: No se pudo conectar");

	// Selecciona la base de datos
	if(!mysql_select_db("pibd", $iden))
		die("Error: No existe la base de datos");

	// Sentencia SQL: muestra todo el contenido de la tabla "books"
	$sentencia = "SELECT * FROM Usuarios where NomUsuario = '$login' and Clave = '$pass'";
	echo $sentencia;
	// Ejecuta la sentencia SQL
	$resultado = mysql_query($sentencia, $iden);
	if(!$resultado)
		die("Error: no se pudo realizar la consulta");
	//echo '<table>';

	//while($fila = mysql_fetch_assoc($resultado))
	//{
		//echo $fila['IdUsuario'];
	//}


	//if(($login == "gallardo" && $pass == "mola") || ($login == "pepe" && $pass == "pepe"))
	if($fila = mysql_fetch_assoc($resultado))
	{
		echo "Acceso permitido.";
		if($remember == true)
		{
			setcookie('usuario', $login, time() + 360 * 24 * 3600);
			setcookie('ultima', time(), time() + 360 * 24 * 3600);

			$_SESSION['UserSession'] = $login;
			$_SESSION['DateSession'] = time();
			$_SESSION['IdUser'] = $fila['IdUsuario'];

		}
		else
		{
			//setcookie('usuario', $login, time() +1);
			//setcookie('ultima', time(), time() +1);


			$_SESSION['UserSession'] = $login;
			$_SESSION['DateSession'] = time();
			$_SESSION['IdUser'] = $fila['IdUsuario'];

		}
		
		$logeado = true;
		$extra = 'registrado.php';
	}
	else
	{
		echo "Acceso denegado";
		$extra = 'index.php';
	}


	/* Redirecciona a una página diferente que se encuentra en el directorio actual */
	
	
	header("Location: http://$host$uri/$extra");
	exit;

	

?>