<?php
    session_start();
    $logeado = isset($_SESSION['UserSession']);
    //$logeado = isset($_COOKIE['usuario']);
          
    if(!$logeado)
     {
        $host = $_SERVER['HTTP_HOST'];
        $uri = rtrim(dirname($_SERVER['PHP_SELF']), '/\\');
        $extra = 'index.php';
        header("Location: http://$host$uri/$extra");
        exit;
     }
     else
     {
        $login = $_SESSION['UserSession'];
     }
     
    $titulo = "Inicio";
    include "inc/header.inc";
    include "php/libsql.php";
    include "php/validar.php";
    include "php/gestionFotos.php";

    $error = false;
    $pass = "Error en la pass";
    $email = "Error en el Email";
    $fecha = "Error en la Fecha";

    if(isset($_POST['email']))
    {
        if(!isEmail($_POST['email']))
        {
            $error = true;
            $email = "Error en el formato";
        }
        else
        {
            $email = $_POST['email'];
        }

        if(!isFecha($_POST['fecha']))
        {
            $error = true;
            $fecha = "Fecha incorrecta";
        }
        else
        {
            $fecha = cambiarFecha($_POST['fecha']);
        }

        if(!$error)
        {
            $sentencia = "Update Usuarios set Email = '$email', Sexo = '$_POST[sexo]', FNacimiento = '$fecha', Pais = '$_POST[pais]', Ciudad = '$_POST[ciudad]' where NomUsuario = '$login';";
            //echo "Sentencia: " . $sentencia  . "<br>";
            mysqlExecuteNonQuery($sentencia);
        }
    }
    else if(isset($_POST['Oldpass']))
    {
        $sentencia = "SELECT * from Usuarios where NomUsuario = '$login'";
        $resultado = mysqlQuery($sentencia);
        if($resultado)
        {

            $usuario = mysql_fetch_assoc($resultado);
            $Myoldpass = $usuario['Clave'];
    
        }


        //print_r($resultado);

        if($Myoldpass == $_POST['Oldpass'])
        {
            if(isPass($_POST['pass']))
            {
                if($_POST['pass'] == $_POST['repass'])
                {
                    $sentencia = "Update Usuarios set Clave = '$_POST[pass]' where NomUsuario = '$login';";
                    mysqlExecuteNonQuery($sentencia);
                }
                else
                {
                    $pass = "Las contraseñas deben coincidir";
                    $error = true;
                }
            }
            else
            {
                $pass = "Error en el formato de la Clave";
                $error = true;
            }
        }
        else
        {
            $pass = "Clave anterior incorrecta";
            $error = true;
        }
        
    }
    else
    {
        guardarFotoRegistro('foto', $login);
    }

    $sentencia = "SELECT * from Usuarios where NomUsuario = '$login'";
    $resultado = mysqlQuery($sentencia);
    if($resultado)
    {

    $usuario = mysql_fetch_assoc($resultado);
    
    }


?>
       <div id="cuerpo">
                <h2>Cambio de datos</h2>

                <?php

                    if(isset($_POST['email']))
                    {
                        if($error)
                        {
                            echo "<p>Error al modificar tus datos</p>";
                        }
                        else
                        {
                            echo "<p>Datos cambiados correctamente. Tus datos son:</p>";
                        }

                        echo "<h3>Email</h3>";
                        echo "<p>$email</p>";
                        echo "<h3>Sexo</h3>";
                        if($usuario['Sexo'] == "0")
                            echo "<p>Masculino </p>";
                        else
                            echo "<p>Femenino </p>";
                        echo "<h3>Ciudad</h3>";
                        echo "<p>$usuario[Ciudad] </p>";
                        echo "<h3>País</h3>";
                        echo "<p>" . getPais($usuario['Pais']) . "</p>";

                        echo "<h3>Fecha de nacimientos</h3>";
                        echo "<p>$fecha</p>";
                    }
                   else if(isset($_POST['Oldpass']))
                   {
                    if(!$error)
                    {
                        echo "<p>Contraseña cambiada corectamente</p>";
                    }
                    else
                    {
                        echo "<p>Error al cambiar la contraseña.</p>";
                        echo "<p>$pass</p>";
                    }
                   }
                   else
                   {
                        echo "<p>Foto cambiada corectamente</p>";
                        mostrarFotoPerfil($usuario['NomUsuario']);
                   }

                ?>
                </div>
                <div class="fixed"><br/></div>
            </div>
<?php
    include "inc/footer.inc"
?>