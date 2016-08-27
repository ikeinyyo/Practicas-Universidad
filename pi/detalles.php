<?php


    //echo "Muestro el isset($_SESSION[UserSession]) ";
    session_start();
    
    $logeado = isset($_SESSION['UserSession']);

    //$logeado = isset($_COOKIE['usuario']);
    
    if(!$logeado)
     {
        $host = $_SERVER['HTTP_HOST'];
        $uri = rtrim(dirname($_SERVER['PHP_SELF']), '/\\');
        $extra = 'registro.php';
        header("Location: http://$host$uri/$extra");
        exit;
     }

    $titulo = "Detalles";
    include "inc/header.inc";
    include "php/libsql.php";
?>        
            <div id="cuerpo">
                <h2>Detalles</h2>
                <?php
                    echo "<p>Identificador: $_GET[id]</p>"; 
                    mostrarFoto($_GET['id']);
                ?>
                <!--<div class="marcoImagen">
                    <img class="image" src="images/p1.jpg" alt="Foto1"/>
                    <p>Título: título</p>
                    <p>Fecha: fecha</p>
                    <p>País: país</p>
                    <p>Álbum: álbum</p>
                    <p>Usuario: usuario</p>
                </div>-->
                <div id="infoImage">
                    <h2>
                        Detalles de la imagen:
                    </h2>
                    <p>
                        Esta imagen fue tomada en Tomakio media mañana cuando 
                        los rayos de sol iluminaban el puente.
                    </p>
                </div>
                <div class="fixed"><br/></div>
            </div>
<?php
    include "inc/footer.inc"
?>
