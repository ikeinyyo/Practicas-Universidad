<?php
    session_start();
    $titulo = "Resultados";
    include "inc/header.inc";
    include "php/libsql.php";
?>    
            <div id="cuerpo">
                <h2>Resultados</h2>
                <div>
                    <?php
                        echo "<p>Resultados para la búsqueda por título <strong>'$_POST[titulo]'</strong>, fecha <strong>'$_POST[fecha]'</strong> y país <strong>'$_POST[pais]'</strong></p>";
                    ?>
                </div>
                <div id="ordenar">
                    <span>Ordenar:</span>
                    <button onclick="ordenar('titulo', this)">Título <img class="fechasOrden" src="resources/des.png" alt=""/></button>
                    <button onclick="ordenar('fecha', this)">Fecha <img class="fechasOrden" src="resources/des.png" alt=""/></button>
                    <button onclick="ordenar('pais', this)">País <img class="fechasOrden" src="resources/des.png" alt=""/></button>
                </div>
                <?php
                    buscarFotos($_POST['titulo'], $_POST['fecha'], $_POST['pais']);
                ?>
                <!--<ul id="listaResultados">
                    <li>
                        <a href="detalles.php?id=Nivel_Completado" class="marcoImagen">
                            <img class="image" src="images/p1.jpg" alt="Imagen1"/>
                            <span>Título: Nivel Completado</span><br/>
                            <span>Fecha: 15-03-2007</span><br/>
                            <span>País: Francia</span><br/>
                        </a>
                    </li>
                    <li>
                        <a href="detalles.php?id=Inicio" class="marcoImagen">
                            <img class="image" src="images/p2.jpg" alt="Imagen2"/>
                            <span>Título: Inicio</span><br/>
                            <span>Fecha: 11-04-2011</span><br/>
                            <span>País: España</span><br/>
                        </a>
                    </li>
                    <li>
                        <a href="detalles.php?id=Acción" class="marcoImagen">
                            <img class="image" src="images/p3.jpg" alt="Imagen3"/>
                            <span>Título: Acción</span><br/>
                            <span>Fecha: 23-10-2010</span><br/>
                            <span>País: Camerún</span><br/>
                        </a>
                    </li>
                    <li>
                        <a href="detalles.php?id=Masacre" class="marcoImagen">
                            <img class="image" src="images/p5.jpg" alt="Imagen3"/>
                            <span>Título: Masacre</span><br/>
                            <span>Fecha: 21-05-2002</span><br/>
                            <span>País: Perú</span><br/>
                        </a>
                    </li>
                </ul>-->
                <div class="fixed"><br/></div>
            </div>
<?php
    include "inc/footer.inc"
?>
