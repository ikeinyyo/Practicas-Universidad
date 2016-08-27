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
?>
        <div id="cuerpo">

        	<form id="formRegistro" action="respuestaAlbum.php" method="POST">
        		<p><label for="titulo">Título</label><br/><input type="text" id="titulo" name="titulo"/></p>
        		<p><label for="descripcion">Descripción</label><br/><input type="text" name="descripcion" id="descripcion"/></p>
        		<p><label for="fecha">Fecha</label><br/><input type="text" name="fecha" id="fecha"/></p>
        		<p>
                    <label for="pais">País</label><br/> <!--<input type="text" id="pais"/> -->
                    <?php
                        llenarPaises("pais");
                    ?>
    
                </p>
                <input type="submit" value="Crear Álbum"/>
        	</form>
            <div id="infoRegistro">
            	<h2>Creas tus álbumes!</h2>
            	<p>
            		Ahora puedes crear tus propios albumes para tener organizadas
            		tus fotos.
            	</p>

            	<h3>Titulo</h3>
            	<p>
            		Añade un títlo al album para agrupar tus fotos.
            	</p>

            	<h3>Descripción</h3>
            	<p>
            		¿De qué era esta foto? Añade una descripción a tu álbum y localiza lo que quieres.
            	</p>

            	<h3>Fecha</h3>
            	<p>
            		Guarda tus fotos por fechas.
            	</p>

            	<h3>País</h3>
            	<p>
            		Ahora podrás recordar de qué pais es cada foto.
            	</p>
            </div>
            <div class="fixed"><br/></div>

        </div>
<?php
    include "inc/footer.inc"
?>