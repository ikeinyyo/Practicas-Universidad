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

    $titulo = "Nueva Foto";
    include "inc/header.inc";
    include "php/libsql.php";
?>
        <div id="cuerpo">

        	<form id="formRegistro" action="respuestaFoto.php" method="POST" enctype="multipart/form-data">
        		<p><label for="titulo">Título</label><br/><input type="text" name="titulo" id="titulo"/></p>
        		<p><label for="fecha">Fecha</label><br/><input type="text" name="fecha" id="fecha"/></p>
        		<p>
                    <label for="pais">País</label><br/> <!--<input type="text" id="pais"/> -->
                    <?php
                        llenarPaises("pais");
                    ?>
    
                </p>
                <p>
                    <label for="album">Álbum</label><br/> <!--<input type="text" id="pais"/> -->
                    <?php
                        llenarAlbumes("album");
                    ?>
    
                </p>
                <p><label for="foto">Foto</label></p>
                <p><input type="file" name="foto" id="foto"/></p>
                <input type="submit" value="Añadir Foto"/>
        	</form>
            <div id="infoRegistro">
            	<h2>Añade una nueva foto a tu álbum!</h2>
            	<p>
            		Ahora puedes crear tus propios albumes para tener organizadas
            		tus fotos.
            	</p>

            	<h3>Titulo</h3>
            	<p>
            		El título de la foto es importante para poder organizarlas y localizarlas.
            	</p>

            	<h3>Fecha</h3>
            	<p>
            		Guarda tus fotos por fechas.
            	</p>
            </div>
            <div class="fixed"><br/></div>

        </div>
<?php
    include "inc/footer.inc"
?>