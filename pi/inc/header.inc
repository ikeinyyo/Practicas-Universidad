<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="es" lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <link rel="stylesheet" href="css/azul.css" type="text/css" media="screen" title="Azul"/>
        <link rel="alternate stylesheet" href="css/verde.css" type="text/css" media="screen" title="Verde"/>
        <link rel="stylesheet" href="css/maquetado.css" type="text/css" media="screen"/>
        <link rel="stylesheet" href="css/print.css" type="text/css" media="print"/>
        <script type="text/javascript" src="js/pi.js"></script>
        <script type="text/javascript" src="js/cookies.js"></script>

        <title>

            <?php
                echo "Pictures &amp; Images - $titulo";
            ?>

        </title>
        
        
    </head>
    <body>

        <?php

        //$logeado = isset($_COOKIE['usuario']);
        $logeado = isset($_SESSION['UserSession']) || isset($_COOKIE['usuario']);

        

        if($logeado)
        {

            
            if(isset($_SESSION['UserSession']))
            {
                $fecha = date(DATE_RFC822, $_SESSION['DateSession']); 
                echo "<div class='salir'>";
                echo "<p> Hola $_SESSION[UserSession]! Tu última visita fue $fecha <a href='salir.php'>Desconectar</a> <a href='registrado.php'>Acceder</a></p>";
                echo "</div>";
            }
            else if(isset($_COOKIE['usuario']))
            {
                $fecha = date(DATE_RFC822, $_COOKIE['ultima']); 
                echo "<div class='salir'>";
                echo "<p> Hola $_COOKIE[usuario]! Tu última visita fue $fecha <a href='salir.php'>Desconectar</a> <a href='registrado.php'>Acceder</a></p>";
                echo "</div>";
            }
            
        }
        else
        {
            
            echo "<div id='formlogin'>";
            echo "<form action='acceso.php' id='log-in' method='POST' onsubmit='return validarLogin(this)'>";
            echo "<p>";

            echo "<label for='login'>Usuario: </label><input type='text' id ='login' name='login'/>";
            echo "            <label for='pass'>Contraseña: </label><input type='password' id='pass' name='pass'/>";
            echo "            <input type='submit' value='Login'/>";
            echo "            <a href='registro.php'>Regístrate</a>";
            echo "        </p>";
            echo "        <p class='detalle'>Recordar <input type='checkbox' name='recordar' id='recordar' value='recordar'/> </p>";
            echo "        <p id='errorFormLogin' class='error'></p>";
            echo "    </form>";
            echo "</div>";
        }

        ?>
        
        <div id="pagina">

            <div id="estilos"> 
                <span>Elige tu estilo: </span>
                <a href="javascript:estilo('Azul')"><img class="estilosIcon" src="resources/estiloAzul.png" alt=""/></a> 
                <a href="javascript:estilo('Verde')"><img class="estilosIcon" src="resources/estiloVerde.png" alt=""/></a> 
            </div>
            <div id="Title">
                <h1><a class="linkTitle" href="index.php">Pictures &amp; Images </a></h1>
            </div>