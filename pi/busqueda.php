<?php
    session_start();
    $titulo = "Búsqueda";
    include "inc/header.inc";
    include "php/libsql.php";
?>
            <div id="cuerpo">
                <h2> Búsqueda </h2>
                <div id="formBusqueda">
                    <form action="resultados.php" method='POST'>
                        <p>
                           <label for="titulo">Título: <br/>
                           </label><input type="text" id="titulo" name="titulo"/>
                        </p>
                        <p>
                            <label for="fecha">Fecha <br/>
                            </label><input type="text" id="fecha" name="fecha"/>
                        </p>
                        <p>
                            <label for="pais">País: </label><br/>
                            <!--<input type="text" id="pais" name="pais"/>-->
                            <?php
                            llenarPaises("pais");
                            ?>
                        </p>
                        <p><input type="submit" value="Buscar"/></p>
                    </form>
                </div>
                <div id="infoBusqueda">
                    <h2>Busca entre más de 1000 imágenes:</h2>
                    <p>
                        Ahora podrás buscar entre todas nuestra imágenes disponible.
                        De esta forma te será más fácil encontrar lo que necesitas.
                    </p>
                </div>
                <div class="fixed"><br/></div>
            </div>
<?php
    include "inc/footer.inc"
?>
