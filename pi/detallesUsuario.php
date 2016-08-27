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
     
    $titulo = "Inicio";
    include "inc/header.inc";
    include "php/libsql.php";
    include "php/libsqli.php";
    include "php/validar.php";

    $login = $_GET['id'];

    $sentencia = "SELECT * from Usuarios where IdUsuario = '$login'";
    $resultado = mysqlQuery($sentencia);
    if($resultado)
    {

    $usuario = mysql_fetch_assoc($resultado);
    
    }


?>
       <div id="cuerpo">
                <h2>Datos Personales de <?php echo $usuario['NomUsuario'] ?></h2>

                <?php
                    mostrarFotoPerfil($usuario['NomUsuario']);
                ?>
                <h3>EMail</h3>
                <p> <?php echo $usuario['Email']; ?> </p>

                <h3>Sexo</h3>
                <p> 
                    <?php 
                    if($usuario['Sexo'] == 0)
                    {
                        echo "Mascuino";
                    }
                    else
                    {
                        echo "Femenino";
                    }
                    ?> 
                </p>

                <h3>Fecha de Nacimiento</h3>
                <p> <?php echo cambiarFecha($usuario['FNacimiento']); ?> </p>

                <h3>País</h3>
                <p> <?php echo getPais($usuario['Pais']); ?> </p>

                <h3>Ciudad</h3>
                <p> <?php echo $usuario['Ciudad']; ?> </p>

                <h3>Albumes</h3>
                <?php
                    mostrarAlbumesByUser($usuario['IdUsuario']);
                ?>

                <div class="fixed"><br/></div>
            </div>
<?php
    include "inc/footer.inc"
?>