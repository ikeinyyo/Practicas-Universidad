function mostrarSuscritos() {




        var req = new XMLHttpRequest();
        req.open('GET', 'damePrincipal.php', false);
        req.send(null);

        if(req.status == 200)
        {

            var foto_seleccionada = eval('('+req.responseText+')');

            var seleccionada = document.getElementById("seleccionada");

            seleccionada.innerHTML = "";

            seleccionada.innerHTML += "<a href=\"detalles.php?id=" + foto_seleccionada["IdFoto"] + "\" id=\"seleccionada\" class=\"marcoImagenSeleccionada\">"
             +"<img class=\"imageSeleccionada\" src=\"" + foto_seleccionada["Fichero"] + "\" alt=\"Imagen1\"/>"
            +"<h3>Foto Seleccionada</h3>"
            +"Título: " + foto_seleccionada["Titulo"] + "<br/>"
            + "Fecha: " + foto_seleccionada["Fecha"] +"<br/>"

            + "Pais: " + foto_seleccionada["Pais"]
            + "</a>";
            //seleccionada.innerHTML = "";

            /*for(var i = 0; i < foto_seleccionada.length; i++)
            {*/

            //seleccionada.innerHTML = "Holaaaaa";
                /*seleccionada.innerHTML += "<p class='texto_eventos' id='evento" + lista_eventos[i].id + "'>" + lista_eventos[i].titulo + " | "  + fecha.toDateString()
                                      + " | " + "<button onclick='centrarEvento(" + lista_eventos[i].id + ")' ><img src='multimedia/mapa.png' alt=Ver en Mapa' title='Ver en Mapa' class='icono'></button>"
                                      + " | " + "<button onclick='mostrarDetalles(" + lista_eventos[i].id + ")' ><img src='multimedia/detalles.png' alt='Ver Detalles' title='Ver Detalles' class='icono'></button>"
                                      + " | " + "<button onclick='buscarComentarios(" + lista_eventos[i].id + ")' ><img src='multimedia/flecha.png' alt='Ver Comentarios' title='Ver Comentarios' class='icono'></button>" + "</p>";

            */
            //}

        }
        else
            alert("Fallo en AJAX");
}

//mostrarSuscritos();

setInterval(mostrarSuscritos, 2000);