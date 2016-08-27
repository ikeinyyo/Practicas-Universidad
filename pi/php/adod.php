
<?php 


	include_once "../adodb5/adodb.inc.php";
function BorrarAlbum($id)
{
	$con = NewADOConnection("mysql");
	$con->debug = false;
	$con->Connect("localhost", "root", "", "pibd");

	// Ejecuta una sentencia SQL
	//$sentencia = "SELECT * FROM libros";
	$sentencia = "DELETE FROM Albumes where IdAlbum = '$id'";
	$resultado = $con->Execute($sentencia);
	/*echo "<pre>";
	print_r($resultado->GetRows());
	echo "</pre>";*/
	$resultado->Close(); // Opcional

	$sentencia = "DELETE FROM Fotos where Album = '$id'";
	$resultado = $con->Execute($sentencia);
	/*echo "<pre>";
	print_r($resultado->GetRows());
	echo "</pre>";*/
	$resultado->Close(); // Opcional

	$con->Close();
}

function BorrarFoto($id)
{
	$con = NewADOConnection("mysql");
	$con->debug = false;
	$con->Connect("localhost", "root", "", "pibd");

	// Ejecuta una sentencia SQL
	//$sentencia = "SELECT * FROM libros";
	$sentencia = "DELETE FROM Fotos where IdFoto = '$id'";
	$resultado = $con->Execute($sentencia);
	/*echo "<pre>";
	print_r($resultado->GetRows());
	echo "</pre>";*/
	$resultado->Close(); // Opcional
	$con->Close();
}

?>
