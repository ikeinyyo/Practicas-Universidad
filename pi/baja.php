<?php


	session_start();

	$usuario = $_SESSION['UserSession'];

	$_SESSION = array();
	$_COOKIE = array();


	setcookie('usuario', '', time()-1);
	setcookie('ultima', '', time()-1);

	if(isset($_COOKIE[session_name()])) {
		setcookie(session_name(), '', time() - 42000, '/');
	}

	session_destroy();
     
    $titulo = "Darse de baja";
    include "inc/header.inc";
    include "php/libsql.php";

    $command = "delete from Usuarios where NomUsuario = '$usuario'";
    mysqlExecuteNonQuery($command);


?>
        <div id="cuerpo">
            <p>Has abandonado Pictures&amp;Images</p>
        </div>
<?php
    include "inc/footer.inc"
?>