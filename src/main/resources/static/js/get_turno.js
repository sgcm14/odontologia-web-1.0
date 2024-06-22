window.addEventListener("load", function () {
  (function () {
    //con fetch invocamos a la API de turnos con el método GET
    //nos devolverá un JSON con una colección de turnos
    const url = "/turnos";
    const settings = {
      method: "GET",
    };

    fetch(url, settings)
      .then((response) => response.json())
      .then((data) => {
        //recorremos la colección de turnos del JSON
        for (turno of data) {
          //por cada turno armaremos una fila de la tabla
          //cada fila tendrá un id que luego nos permitirá borrar la fila si eliminamos al turno
          var table = document.getElementById("turnoTable");
          var turnoRow = table.insertRow();
          let tr_id = "tr_" + turno.id;
          turnoRow.id = tr_id;

          //por cada turno creamos un boton delete que agregaremos en cada fila para poder eliminar la misma
          //dicho boton invocara a la funcion de java script deleteByKey que se encargará
          //de llamar a la API para eliminar un turno
          let deleteButton =
            "<button" +
            " id=" +
            '"' +
            "btn_delete_" +
            turno.id +
            '"' +
            ' type="button" onclick="deleteBy(' +
            turno.id +
            ')" class="btn btn-danger btn_delete">' +
            "&times" +
            "</button>";

          //por cada turno creamos un boton que muestra el id y que al hacerle clic invocará
          //a la función de java script findBy que se encargará de buscar al turno que queremos
          //modificar y mostrar los datos del mismo en un formulario.
          let updateButton =
            "<button" +
            " id=" +
            '"' +
            "btn_id_" +
            turno.id +
            '"' +
            ' type="button" onclick="findBy(' +
            turno.id +
            ')" class="btn btn-info btn_id">' +
            turno.id +
            "</button>";
          
          // Formatear fecha y hora para mostrar en el listado
          let fechaHoraCita = new Date(turno.fechaHoraCita);
          let day = String(fechaHoraCita.getDate()).padStart(2, "0");
          let month = String(fechaHoraCita.getMonth() + 1).padStart(2, "0");
          let year = fechaHoraCita.getFullYear();
          let hours = fechaHoraCita.getHours();
          let minutes = String(fechaHoraCita.getMinutes()).padStart(2, "0");
          let ampm = hours >= 12 ? "p.m." : "a.m.";
          hours = hours % 12;
          hours = hours ? hours : 12; // the hour '0' should be '12'
          let formattedDate = `${day}/${month}/${year} ${hours}:${minutes} ${ampm}`;

          // Concatenar nombre y apellido del paciente y del odontólogo
          const pacienteNombreCompleto = `${turno.paciente.nombre} ${turno.paciente.apellido}`;
          const odontologoNombreCompleto = `${turno.odontologo.nombre} ${turno.odontologo.apellido}`;

          //armamos cada columna de la fila
          //como primer columna pondremos el boton modificar
          //luego los datos del turno
          //como ultima columna el boton eliminar
          turnoRow.innerHTML =
            "<td>" +
            turno.id +
            "</td>" +
            '<td class="td_paciente">' +
            pacienteNombreCompleto +
            "</td>" +
            '<td class="td_odontologo">' +
            odontologoNombreCompleto +
            "</td>" +
            '<td class="td_fechaHoraCita">' +
            formattedDate +
            "</td>" +
            "<td>" +
            updateButton + deleteButton
            "</td>"/* +
            "<td>" +
            deleteButton +
            "</td>";*/
        }
      });
  })(function () {
    let pathname = window.location.pathname;
    if (pathname == "/get_turnos.html") {
      document.querySelector(".nav .nav-item a:last").addClass("active");
    }
  });
});