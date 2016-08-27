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

    
?>
        <div id="cuerpo">
            
            <div id="menu">
                
                <?php
                    mostrarFotoPerfil($login);
                ?>  
            	<ul>
            		<li>
            			<a class="boton_menu" href="misdatos.php">Mis Datos</a>
            		</li>
            		<li>
            			<a class="boton_menu" href="misalbumes.php">Mis álbumes</a>
            		</li>
            		<li>
            			<a class="boton_menu" href="nuevoalbum.php">Crear álbum</a>
            		</li>
            		<li>
            			<a class="boton_menu" href="nuevafoto.php">Añadir foto</a>
            		</li>
            		<li>
            			<a class="boton_menu" href="baja.php">Darse de baja</a>
            		</li>
            	</ul>
            </div>
            <div id="infoRegistrado">
            	<h2>Bienvenido usuario!</h2>
            	<p>
            		Estás en la zona de usuarios registrado. Desde aquí puedes
            	   	modificar tus datos personales y de tu cuenta.
            	</p>
            </div>
            <div class="fixed"><br/></div>
        </div>
<?php
    include "inc/footer.inc"
?>