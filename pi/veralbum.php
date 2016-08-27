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

    $titulo = "Ver Álbum";
    include "inc/header.inc";
    include "php/libsql.php";
?>


            <div id="cuerpo">

                <?php
                    verAlbum($_GET['id']);
                ?>
                <div class="fixed"><br/></div>
            </div>
<?php
    include "inc/footer.inc"
?>
