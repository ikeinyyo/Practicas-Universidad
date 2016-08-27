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

    //print_r($_POST);

    /*for($i = 0; $i < count($_POST); $i++)
    {
        echo "Album: " . $_POST[$i];
    }*/
    
    /*foreach($_POST as $c=>$v)
        echo "<p>El vector con indice $c tiene el valor $v </p>";*/

?>


            <div id="cuerpo">

                <h2>Eliminar Foto</h2>
                <p>¿Deseas eliminar esta foto?</p>

                <?php
                    foreach($_POST as $c=>$v)
                    {
                        mostrarFoto($v);   
                    }
                ?>

                <form method="POST" action="resultadoEliminarFoto.php">
                    <?php
                        foreach($_POST as $c=>$v)
                        {
                            echo "<input type=\"hidden\" name=\"$v\" value=\"$v\">";       
                        }
                    ?>

                    <input type="submit" value="Confirmar">
                </form>
                    <a href="index.php">Nooooooo! Lo he pensado mejor.</a>
                    <div class="fixed"></div> 
            </div>
<?php
    include "inc/footer.inc"
?>
