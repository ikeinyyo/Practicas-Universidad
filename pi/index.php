<?php
    session_start();

    if(isset($_COOKIE['usuario']))
    {
        $_SESSION['UserSession'] = $_COOKIE['usuario'];
        $_SESSION['DateSession'] = time();
    }

    $titulo = "Inicio";
    include "inc/header.inc";
    include "php/libsql.php";
?>
            <!--<div>
                <a href="busqueda.php">Búsqueda</a>
                <a href="detalles.php">Detalles</a>
                <a href="registro.php">Registro</a>
                <a href="resultados.php">Resultados</a>
                <a href="nuevousuario.php">Nuevo Usuario</a>
                <a href="nuevoalbum.php">Nuevo Álbum</a>
                <a href="registrado.php">Registrado</a>
                <a href="nuevafoto.php">Nueva Foto</a>
                <a href="veralbum.php">Ver Álbum</a>
                <a href="misalbumes.php">Mis Albumes</a>
            </div>-->


            <div id="cuerpo">
                <?php
                    fotoSeleccionada();
                ?>
                <div class="fixed"><br/></div>
                <!--<ul>
                    <li>
                        <a href="detalles.php?id=Nivel_Completado" class="marcoImagen">
                            <img class="image" src="images/p1.jpg" alt="Imagen1"/>
                            Título: Nivel Completado<br/>
                            Fecha: 17/09/1991<br/>
                            País: España<br/>
                        </a>
                    </li>
                    <li>
                        <a href="detalles.php?id=Inicio" class="marcoImagen">
                            <img class="image" src="images/p2.jpg" alt="Imagen2"/>
                            Título: Inicio<br/>
                            Fecha: 17/09/1991<br/>
                            País: España<br/>
                        </a>
                    </li>
                    <li>
                        <a href="detalles.php?id=Batalla" class="marcoImagen">
                            <img class="image" src="images/p3.jpg" alt="Imagen3"/>
                            Título: Batalla<br/>
                            Fecha: 17/09/1991<br/>
                            País: España<br/>
                        </a>
                    </li>
                    <li>
                        <a href="detalles.php?id=No_hay_wifi" class="marcoImagen">
                            <img class="image" src="images/p4.jpg" alt="Imagen4"/>
                            Título: No hay Wifi<br/>
                            Fecha: 17/09/1991<br/>
                            País: España<br/>
                        </a>
                    </li>
                    <li>
                        <a href="detalles.php?id=Masacre" class="marcoImagen">
                            <img class="image" src="images/p5.jpg" alt="Imagen5"/>
                            Título: Masacre<br/>
                            Fecha: 17/09/1991<br/>
                            País: España<br/>
                        </a>
                    </li>
                    <li>
                        <a href="detalles.php?id=Nivel_Completado" class="marcoImagen">
                            <img class="image" src="images/p1.jpg" alt="Imagen6"/>
                            Título: Nivel Completado<br/>
                            Fecha: 17/09/1991<br/>
                            País: España<br/>
                        </a>
                    </li>
                    
                </ul>-->

                <?php
                    ultimasFotos();
                ?>
                <div class="fixed"><br/></div>
            </div>

            <script type="text/javascript" src="js/ajax.js"></script>
<?php
    include "inc/footer.inc"
?>
