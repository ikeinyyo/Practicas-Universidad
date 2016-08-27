<?php
    $titulo = "Registro";
    $logeado = false;
    include "inc/header.inc";
    include "php/libsql.php";

    //llenarPaises("hola");
?>
            <div id="cuerpo">
                <h2>Registro</h2>
                <!--<form id="formRegistro" action="nuevousuario.php" onsubmit="return validarRegistro(this)" method="POST" enctype="multipart/form-data">-->
                <form id="formRegistro" action="nuevousuario.php" method="POST" enctype="multipart/form-data">
                    <p>
                        <label for="Rlogin">Login: </label><br/>
                        <input type="text" id="Rlogin" name="login"/>
                    </p>
                    <p class="error" id="errorLogin"></p>
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
                        <label for="email">eMail: </label><br/>
                        <input type="text" id="email" name="email"/>
                    </p>
                    <p class="error" id="errorEmail"></p>
                    <p>Sexo</p>
                    <p class="error" id="errorSexo"></p>
                    <p>
                        <input type="radio" value='0' id="sexoM" name="sexo"/>
                        <label for="sexoM">Masculino </label>
                    </p>
                    <p>
                        <input type="radio" id="sexoF" value='1' name="sexo"/>
                        <label for="sexoF">Femenino</label>
                    </p>
                    <p>
                        <label for="fecha">Nacimiento (dd-mm-aaaa): </label><br/>
                        <input type="text" id="fecha" name="fecha"/>
                    </p>
                    <p class="error" id="errorFecha"></p>
                    <p>
                        <label for="ciudad">Ciudad: </label><br/>
                        <input type="text" id="ciudad" name="ciudad"/>
                    </p>
                    <p>
                        <label for="pais">País: </label><br/>
                        <!--<input type="text" id="pais" name="pais"/>
                        <select name="pais" id="pais"></select>-->
                        <?php
                            llenarPaises("pais");
                        ?>
                    </p>
                    <p>
                        <label for="foto">Foto: </label><br/>
                        <input type="file" id="foto" name="foto"/>
                    </p>
                    <p class="error" id="errorFoto"></p>
                    <p>
                        <input type="submit" value="Registrar"/>
                    </p>
                </form>
                <div id="infoRegistro">
                    <h2>Crea tu cuenta en Pictures &amp; Images</h2>
                    
                    <h3>Únete a nosotros:</h3>
                    <p>
                        Consigue ya una cuenta en Pictures &amp; Images y podrás disfrutas de todas las ventajas de nuestros usuarios.
                    </p>
                    
                    <h3>Comparte tus imágenes:</h3>
                    
                    <p>Si te registras podrás subir tus propias imágenes y compartirlas utilizando licencias de 
                       <a href="http://creativecommons.org/licenses/by-sa/3.0/">Creative Commons</a>.
                    </p>  
                    
                    <h3>Descarga imágenes:</h3>
                    <p>
                        Podrás descargarte las imágenes de otros usuarios. 
                    </p> 
                    <p>
                        Regístrate ya!
                    </p>
                </div>
                <div class="fixed"><br/></div>
            </div>
<?php
    include "inc/footer.inc"
?>
