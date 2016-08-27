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

    $fecha = "Error en la fecha";

    if(isFecha($_POST['fecha']))
    {


        
        $fechaRegistro = time();
        $fecha = cambiarFecha($_POST['fecha']);

        $nombre = guardarNuevaFoto('foto', $fechaRegistro);
        $sentencia = "insert into Fotos(Titulo, Fecha, Pais, Album, FRegistro, Fichero) 
        values('$_POST[titulo]', '$fecha', '$_POST[pais]', '$_POST[album]', '$fechaRegistro', '$nombre');";

        //echo "La sentencia: $sentencia <br/>";

        //echo "Sentencia: " . $sentencia  . "<br>";
        mysqlExecuteNonQuery($sentencia);
        $error = false;
    } 
    else
    {
        $error = true;
    }  



?>
       <div id="cuerpo">
                <h2>Nueva Foto</h2>

                <?php
                    if(!$error)
                    {
                        echo "<p>Fotografia insertada correctamente:</p>";
                    }
                    else
                    {
                         echo "<p>Error al insertar la fotografía:</p>";
                    }
                        echo "<h3>Título</h3>";
                        echo "<p>$_POST[titulo]</p>";
                        echo "<h3>Fecha</h3>";
                        echo "<p>$fecha</p>";
                        echo "<h3>País</h3>";
                        echo "<p>" . getPais($_POST['pais']) . "</p>";
                        echo "<h3>Álbum</h3>";
                        echo "<p>" .getAlbum($_POST['album']) . "</p>";
                        echo "<h3>Foto</h3>";
                        mostrarFotoByArchivo($nombre);


                ?>
                </div>
                <div class="fixed"><br/></div>
            </div>
<?php
    include "inc/footer.inc"
?>