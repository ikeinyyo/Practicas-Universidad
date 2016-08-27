/* 
 * Fichero: pi.js
 * Autor: Sergio Gallardo Sales
 * Fecha: 07/03/2012 18:27
 */

var ErrorVacio = "(*) El campo no puede estar vacio";
var ErrorLongitudLogin = "(*) El campo debe tener entre 3 y 15 caracteres";
var ErrorLongitudPass = "(*) El campo debe tener entre 6 y 15 caracteres";
var ErrorNoAlphanumeric = "(*) El campo solo puede contener caracteres Alfanuméricos";
var ErrorNoAlphanumericOr_ = "(*) El campo solo puede contener caracteres Alfanuméricos y subrayados (_)";
var ErrorNoSegura = "(*) La contraseña debe contener al menos una letra en minúculas, una en mayúsculas y un número";
var ErrorRePass = "(*) Las contraseñas deben coincidir";
var ErrorEmail = "(*) Debe ser una direccion correcta.";
var ErrorSexo = "(*) Debe seleccionar una opción.";
var ErrorFecha = "(*) Debe ser una fecha correcta."

var minLogin = 3;
var maxLogin = 15;
var minPass = 6;
var maxPass = 15;
var minDominio = 2;
var maxDominio = 4;

function validarRegistro(f) {
    
    
    var correcto = true;
    var pass = true;
    
    //Hasta poder ponerlo en el form.
    //f = document.getElementById('formRegistro');
    var error;  //Variable para usar en todas las validaciones.
                //Almacena el error del campo.
  
    var campo; //Variable para usar en todas las validaciones.
               //Almacena el valor del campo.
               
    
    //Validación del Login:
    campo = f.Rlogin.value; 
    error = document.getElementById('errorLogin');
    error.innerHTML = "";
    
    if(campo.length > 0)
    {
        if(campo.length >= minLogin && campo.length <= maxLogin)
        {
           if(!isAlphaNumeric(campo))
            {
                correcto = false;
                error.innerHTML = ErrorNoAlphanumeric;  
            }
        }
        else
        {
            correcto = false;
            error.innerHTML = ErrorLongitudLogin;   
        }
    }
    else
    {
        correcto = false;
        error.innerHTML = ErrorVacio;
    }
    
    //Validación del Pass:
    campo = f.Rpass.value; 
    error = document.getElementById('errorPass');
    error.innerHTML = "";
    
    if(campo.length > 0)
    {
        if(campo.length >= minPass && campo.length <= maxPass)
        {
           if(isAlphaNumericAnd_(campo))
            {
                if(!isASecurePass(campo))
                {
                    correcto = false;
                    pass = false;
                    error.innerHTML = ErrorNoSegura;
                }
            }
            else
            {
                correcto = false;
                pass = false;
                error.innerHTML = ErrorNoAlphanumericOr_;
            }
        }
        else
        {
            correcto = false;
            pass = false;
            error.innerHTML = ErrorLongitudPass;   
        }
    }
    else
    {
        correcto = false;
        pass = false;
        error.innerHTML = ErrorVacio;
    }
    
    //La repetición de la contraseña la compruebo si la contraseña es correcta
    if(pass)
    {
        //Validación del Pass:
        campo = f.repass.value; 
        error = document.getElementById('errorRepass');
        error.innerHTML = "";
        
        if(!(campo == f.pass.value))
        {
            correcto = false;
            error.innerHTML = ErrorRePass;
        }
    }
    
    //Validación del eMail:
    campo = f.email.value; 
    error = document.getElementById('errorEmail');
    error.innerHTML = "";
    
    if(!isEMail(campo))
    {
        correcto = false;
        error.innerHTML = ErrorEmail;
    }
    
    //Validación del Sexo:
    error = document.getElementById('errorSexo');
    error.innerHTML = "";
    
    if(!f.sexoM.checked && !f.sexoF.checked)
    {
        correcto = false;
        error.innerHTML = ErrorSexo;
    }
    
    //Validación del Fecha:
    campo = f.fecha.value; 
    
    error = document.getElementById('errorFecha');
    error.innerHTML = "";
    
    if(isDateFormat(campo))
    {
        //var fecha = fechaNoEuropea(campo);
        
        if(!isDate(campo))
        {
            correcto = false;
            error.innerHTML = ErrorFecha;
        }
      
    }
    else
    {
        correcto = false;
        error.innerHTML = ErrorFecha;
    }
  
    quitarBlancosInicio(f.ciudad.value);
    
    return correcto;
}

function validarLogin(f) {
    
    var correcto = true;
    //f = document.getElementById('log-in');
    var error;  //Variable para usar en todas las validaciones.
                //Almacena el error del campo.
    
    error = document.getElementById('errorFormLogin');
    error.innerHTML = "";
    
    
    var vlogin = quitarBlancosInicio(f.login.value);
    var vpass = quitarBlancosInicio(f.pass.value);
    
    if(vlogin.length == 0 || vpass.length == 0)
    {
        correcto = false;
        error.innerHTML = ErrorVacio;
    }

    return correcto;
    
}
//Funciones de vaidación:

function isAlphaNumeric(cadena) {
    
    var alphanumeric = true;
      
    for(var i  = 0; i < cadena.length && alphanumeric; i++)
    {
        if(!((cadena[i] >= 'a' && cadena[i] <= 'z') 
              || (cadena[i] >= 'A' && cadena[i] <= 'Z')
              || (cadena[i] >= '0' && cadena[i] <= '9')))
            alphanumeric = false;
    } 
    
    return alphanumeric;
}

function isAlphaNumericAnd_(cadena) {
    
    var alphanumeric = true;
      
    for(var i  = 0; i < cadena.length && alphanumeric; i++)
    {
        if(!((cadena[i] >= 'a' && cadena[i] <= 'z') 
              || (cadena[i] >= 'A' && cadena[i] <= 'Z')
              || (cadena[i] >= '0' && cadena[i] <= '9')
              || (cadena[i] == '_')))
            alphanumeric = false;
    } 
    
    return alphanumeric;
}

function isAlphaNumericAnd_AndDot(cadena) {
    
    var alphanumeric = true;
      
    for(var i  = 0; i < cadena.length && alphanumeric; i++)
    {
        if(!((cadena[i] >= 'a' && cadena[i] <= 'z') 
              || (cadena[i] >= 'A' && cadena[i] <= 'Z')
              || (cadena[i] >= '0' && cadena[i] <= '9')
              || (cadena[i] == '_')
              || (cadena[i] == '.')))
            alphanumeric = false;
    } 
    
    return alphanumeric;
}

function isASecurePass(cadena) {
    
    var M = false;      //Almacena si hay mayśculas.
    var m = false;      //Almacena si hay minúsculas.
    var num = false;    //Almacena si hay números.
    
    for(var i  = 0; i < cadena.length && (!M || !m || !num); i++)
    {
        if(cadena[i] >= 'a' && cadena[i] <= 'z' && !m)
            m = true;
        if(cadena[i] >= 'A' && cadena[i] <= 'Z' && !M)
            M = true;
        if(cadena[i] >= '0' && cadena[i] <= '9' && !num)
            num = true;
    } 
    return (M && m && num); 
}

function isEMail(cadena) {
    
    var email = true;
    
    //usuario @ dominios . dominioPrincipal
  
    //Dominio principal desde el último punto hasta el final
    var dominioPrincipal = cadena.substring(cadena.lastIndexOf('.') + 1, cadena.length);
    
    //Dominios desde la arroba hasta el último punto
     var dominios = cadena.substring(cadena.lastIndexOf('@') + 1, cadena.lastIndexOf('.'));
     
    //Usuario desde el inicio hasta la arroba
    var usuario = cadena.substring(0, cadena.lastIndexOf('@')); 
    
    //alert("Usuario: " + usuario);
    //alert("dominios: " + dominios);
    //alert("Dominio Principal: " + dominioPrincipal);
    
    if(!isAlphaNumericAnd_AndDot(usuario)
        || !isAlphaNumericAnd_AndDot(dominios)
        || !isAlphaNumericAnd_AndDot(dominioPrincipal)
        || !(dominioPrincipal.length >= minDominio && dominioPrincipal.length <= maxDominio))
    {
        email = false;
    }
       
   
    return email;
}

function isDateFormat(cadena) {
    
    var date = true;
      
    for(var i  = 0; i < cadena.length && date; i++)
    {
        if(!((cadena[i] >= '0' && cadena[i] <= '9')
              || (cadena[i] == '-')))
            date = false;
    } 
    
    return date;
    
} 
//Funciń que cambia los meses y los dias para poner la fecha en formato no europeo:
// dd-mm-aaaa
function fechaNoEuropea(cadena) {
    
    var vecFecha = cadena.split('-');
    
    var cadFecha = vecFecha[1] + "-" + vecFecha[0] + "-" + vecFecha[2];
    
    return cadFecha;
        
}

function isDate(fecha) {
    
    var correcta = true;
    var bisiesto = false;
    
    var ano = parseFloat(fecha.substring(fecha.lastIndexOf("-")+1,fecha.length)); 
    
    var mes = parseFloat(fecha.substring(fecha.indexOf("-")+1,fecha.lastIndexOf("-"))); 
    
    var dia = parseFloat(fecha.substring(0,fecha.indexOf("-")));
    
    
    
    if(isNaN(ano) || ano < 1900)
        correcta = false;
    else if(isNaN(mes) || mes < 1 || mes > 12)
        correcta = false;
    else if(isNaN(dia) || dia < 1 || dia > 31)
        correcta = false;
    else
    {
        if ((ano%4 == 0 && ano % 100 != 0) || ano % 400 == 0)
           bisiesto = true;
       
       if ((mes == 4 || mes == 6 || mes == 9 || mes == 11) && (dia > 30)) 
            correcta = false;
        else if(mes == 2)
        {
            if(bisiesto && dia > 29)
                correcta = false;
            else if(!bisiesto && dia > 28)
                correcta = false;
        }
    }
    
    return correcta;
       
}

function quitarBlancosInicio(cadena) {
    
    var salida = "";
    var i;
    
    for(i = 0; i < cadena.length && cadena[i] == ' '; i++) {}
     
        
    salida = cadena.substring(i, cadena.length); 
    
    
    return salida;
}
