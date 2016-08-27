<?php
    $titulo = "Inicio";
    $logeado = false;
    include "inc/header.inc";
    include "php/libsql.php";
    include "php/validar.php";
    include "php/gestionFotos.php";

    $freg = time();
    $fecha = "Error en la fecha";
    $login = "Error en el login";
    $email = "Error en el email";
    $pass = "Error en la pass";
    $correcto = true;

    if(isFecha($_POST['fecha']))
    {
        $fecha = cambiarFecha($_POST['fecha']);
    }
    else
    {
        $correcto = false;
    }

    if(isLogin($_POST['login']))
    {
        $login = $_POST['login'];
    }
    else
    {
        $correcto = false;
    }

    if(isPass($_POST['pass']))
    {
        $pass = $_POST['pass'];
    }
    else
    {
        $correcto = false;
    }

    if(isEmail($_POST['email']))
    {
        $email = $_POST['email'];
    }
    else
    {
        $correcto = false;
    }

    


    guardarFotoRegistro('foto', $login);    
    
    if($correcto)
    {

        $sentencia = "insert into Usuarios(NomUsuario, Clave, Email, Sexo, FNacimiento, Ciudad, Pais, Foto, FRegistro) 
        values('$login', '$_POST[pass]', '$_POST[email]', '$_POST[sexo]', '$fecha', '$_POST[ciudad]', '$_POST[pais]', '', '$freg');";

        //echo "Sentencia: " . $sentencia  . "<br>";
        //mysqlExecuteNonQuery($sentencia);
    }

?>
        <div id="cuerpo">
            
            <h2>Registro</h2>

            <?php

            if($correcto)
            {
                echo "<p>Te has registrado correctamente:</p>";
            }
            else
            {
                echo "<p>Error en el registro</p>";
            }

            echo "<h3>Nombre</h3>";
            echo "$login";

            echo "<h3>Contraseña</h3>";
            echo "$pass";

            echo "<h3>eMail</h3>";
            echo "$email";

            echo "<h3>Fecha de Nacimiento</h3>";
            echo "$fecha";

            echo "<h3>Ciudad</h3>";
            echo "$_POST[ciudad]";

             echo "<h3>Sexo</h3>";
            if($_POST['sexo'] == "0")
                echo "<p>Masculino </p>";
            else
                echo "<p>Femenino </p>";

            echo "<h3>País</h3>";
            echo "<p>" . getPais($_POST['pais']) . "</p>";

            ?>
            <div class="fixed"><br/></div>
        </div>
<?php
    include "inc/footer.inc"
?>