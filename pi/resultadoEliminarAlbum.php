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
    include "php/libsqli.php";
    include "php/adod.php";

    //print_r($_POST);

    /*for($i = 0; $i < count($_POST); $i++)
    {
        echo "Album: " . $_POST[$i];
    }*/
    
    foreach($_POST as $c=>$v)
        BorrarAlbum($v);

?>


            <div id="cuerpo">
                <h3>Eliminar Álbum</h3>
                <p>El álbum ha sido eliminado correctamente</p>
            </div>
<?php
    include "inc/footer.inc"
?>
