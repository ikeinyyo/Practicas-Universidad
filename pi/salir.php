<?php

	session_start();

	$_SESSION = array();
	$_COOKIE = array();

	setcookie('usuario', '', time()-1);
	setcookie('ultima', '', time()-1);

	if(isset($_COOKIE[session_name()])) {
		setcookie(session_name(), '', time() - 42000, '/');
	}

	session_destroy();

	$host = $_SERVER['HTTP_HOST'];
    $uri = rtrim(dirname($_SERVER['PHP_SELF']), '/\\');
    $extra = 'index.php';
    header("Location: http://$host$uri/$extra");
    exit;

?>