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

    $fecha = "Error en la fecha";

    if(isFecha($_POST['fecha']))
    {

        $fecha = cambiarFecha($_POST['fecha']);
        $sentencia = "insert into Albumes(Titulo, Descripcion, Fecha, Pais, Usuario) values('$_POST[titulo]', '$_POST[descripcion]', '$fecha', '$_POST[pais]', '$_SESSION[IdUser]');";

        mysqlExecuteNonQuery($sentencia);
        $error = false;
    }
    else
    {
        $error = true;
    }


?>
       <div id="cuerpo">
                <h2>Nuevo Álbum</h2>

                <?php

                    if(!$error)
                    {
                        echo "<p>Se ha creado el álbum correctamente:</p>";
                    }
                    else
                    {
                       echo "<p>Error al crear el álbum</p>"; 
                    }
                    echo "<h3>Título</h3>";
                    echo "<p>$_POST[titulo]</p>";
                    echo "<h3>Descripción</h3>";
                    echo "<p>$_POST[descripcion] </p>";
                    echo "<h3>Fecha</h3>";
                    echo "<p>$fecha</p>";
                    echo "<h3>País</h3>";
                    echo "<p>" . getPais($_POST['pais']) . "</p>";
                ?>
                </div>
                <div class="fixed"><br/></div>
            </div>
<?php
    include "inc/footer.inc"
?>