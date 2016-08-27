
//window.onload = setEstilo;

function setCookie(c_name, value, expiredays) {
    var exdate = new Date();
    exdate.setDate(exdate.getDate() + expiredays);
    document.cookie = c_name + "=" + escape(value) +
        ((expiredays == null) ? "" : ";expires="+ exdate.toGMTString());
}
function getCookie(c_name) {
    if(document.cookie.length > 0) {
        c_start = document.cookie.indexOf(c_name + "=");
        if(c_start != -1) {
            c_start = c_start + c_name.length + 1;
            c_end = document.cookie.indexOf(";", c_start);
        if(c_end == -1)
            c_end = document.cookie.length;
        return unescape(document.cookie.substring(c_start, c_end));
        }
    }
    return "";
}

function setEstilo() {
    
    var c = getCookie('estilo')
    
    estilo('');
    
    if(c != null || c != "")
    {
        estilo(c);
        
    }
    else
    {
        setCookie('estilo', 'Azul', 365);
        estilo('Azul');
    }
} 


function estilo(titulo) {
    
    var links = document.getElementsByTagName('link');
    
    for(var i = 0; i < links.length; i++)
    {
        //Links con estilos que no sean de impresión.
        if(links[i].getAttribute('rel') != null && 
           links[i].getAttribute('rel').indexOf('stylesheet') != -1 && 
           links[i].getAttribute('media') != 'print') {
           
            if(links[i].getAttribute('title') != null &&
                links[i].getAttribute('title').length > 0) {
                
            if(links[i].getAttribute('title') == titulo)
            {
                //alert(titulo + " " + links[i].getAttribute('title'));
                links[i].disabled = false;
                setCookie('estilo', titulo, 365);
            }
            else
                links[i].disabled = true;
            
            
            }
        }

    }
}


function desconectar() {
    setCookie('usuario', '', -1);
    setCookie('ultima', '', -1);
    location.href="index.php";
}

setEstilo();