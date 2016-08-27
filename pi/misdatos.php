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

    $sentencia = "SELECT * from Usuarios where NomUsuario = '$login'";
    $resultado = mysqlQuery($sentencia);
    if($resultado)
    {

    $usuario = mysql_fetch_assoc($resultado);
    
    }


?>
       <div id="cuerpo">
                <h2>Datos Personales</h2>
                <form class="formDatos" action="modificardatos.php" onsubmit="return validarRegistro(this)" method="POST" enctype="multipart/form-data">
                    <p>
                        <label for="email">eMail: </label><br/>
                        <input type="text" id="email" name="email" value=<?php echo "\"" . $usuario['Email'] . "\""; ?>/>
                    </p>
                    <p class="error" id="errorEmail"></p>
                    <p>Sexo</p>
                    <p class="error" id="errorSexo"></p>
                    <p>
                        <input type="radio" id="sexoM" value="0" name="sexo" <?php if($usuario['Sexo'] == 0) echo "checked"; ?>/>
                        <label for="sexoM">Masculino </label>
                    </p>
                    <p>
                        <input type="radio" id="sexoF" value="1" name="sexo" <?php if($usuario['Sexo'] == 1) echo "checked"; ?>/>
                        <label for="sexoF">Femenino</label>
                    </p>
                    <p>
                        <label for="fecha">Nacimiento (aaaa-mm-dd): </label><br/>
                        <input type="text" id="fecha" name="fecha" value=<?php echo "\"" . $usuario['FNacimiento'] . "\""; ?>/>
                    </p>
                    <p class="error" id="errorFecha"></p>
                    <p>
                        <label for="ciudad">Ciudad: </label><br/>
                        <input type="text" id="ciudad" name="ciudad" value=<?php echo "\"" . $usuario['Ciudad'] . "\""; ?>/>
                    </p>
                    <p>

                        <label for="pais">País: </label><br/>
                        <!--<input type="text" id="pais" name="pais"/>
                        <select name="pais" id="pais"></select>-->
                        <?php
                        //echo $usuario['Pais'];
                            llenarPaisesSeleccion("pais", $usuario['Pais']);
                        ?>
                    </p>
                    <p>
                        <input type="submit" value="Guardar"/>
                    </p>
                </form>
                <div id="infoRegistro">
                    <form class="formDatos" action="modificardatos.php" onsubmit="return validarRegistro(this)" method="POST" enctype="multipart/form-data">
                    
                    <p>
                        <label for="Oldpass">Password Antigua: </label><br/>
                        <input type="password" id="Oldpass" name="Oldpass"/>
                    </p>
                    <p>
                        <label for="Rpass">Password: </label><br/>
                        <input type="password" id="Rpass" name="pass"/>
                    </p>
                    <p class="error" id="errorPass"></p>
                    <p>
                        <label for="repass">Repetir: </label><br/>
                        <input type="password" id="repass" name="repass"/>
                    </p>
                    <p class="error" id="errorRepass"></p>
                    <p>
                        <input type="submit" value="Cambiar Password"/>
                    </p>
                </form>
                <?php
                    mostrarFotoPerfil($usuario['NomUsuario']);
                ?>
                <form class="formDatos" action="modificardatos.php" method="POST" enctype="multipart/form-data">
                    
                    <p>
                        <label for="foto">Foto: </label><br/>
                        <input type="file" id="foto" name="foto"/>
                    </p>
                    <p class="error" id="errorFoto"></p>
                     <p>
                        <input type="submit" value="Cambiar Foto"/>
                    </p>
                </form>

                </div>
                <div class="fixed"><br/></div>
            </div>
<?php
    include "inc/footer.inc"
?>