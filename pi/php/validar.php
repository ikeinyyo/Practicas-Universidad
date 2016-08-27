<?php

function isEmail($email) {

	$correcto = true;

	$correcto = filter_var($email, FILTER_VALIDATE_EMAIL);

	return $correcto;
}

function isPass($pass) {
	$correcto = true;

	$expreg = "(?!^[0-9]*$)(?!^[a-z]*$)(?!^[A-Z]*$)^([a-zA-Z0-9_]{6,15})$";
	$correcto = preg_match("/" . $expreg . "/", $pass);
	return $correcto;
}

function isLogin($login) {
	$correcto = true;

	$expreg = "^[a-zA-Z0-9]{3,15}$";
	$correcto = preg_match("/" . $expreg . "/", $login);
	return $correcto;
}

function isFecha($fecha) {
	$correcto = true;

	$expreg = "^\\d{2}\\-\\d{2}\\-\\d{4}$";
	$correcto = preg_match("/" . $expreg . "/", $fecha);
	return $correcto;
}

function cambiarFecha($fecha)
{
	$arrayFecha = explode("-", $fecha);

	$nueva = $arrayFecha[2] . "-" . $arrayFecha[1] . "-" . $arrayFecha[0];

	return $nueva;
}
?> 