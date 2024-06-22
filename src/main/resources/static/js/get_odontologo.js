window.addEventListener("load", function () {
  (function () {
    //con fetch invocamos a la API de odontologos con el método GET
    //nos devolverá un JSON con una colección de odontologos
    const url = "/odontologos";
    const settings = {
      method: "GET",
    };

    fetch(url, settings)
      .then((response) => response.json())
      .then((data) => {
        //recorremos la colección de odontologos del JSON
        for (odontologo of data) {
          //por cada odontologo armaremos una fila de la tabla
          //cada fila tendrá un id que luego nos permitirá borrar la fila si eliminamos el odontologo
          var table = document.getElementById("odontologoTable");
          var odontologoRow = table.insertRow();
          let tr_id = "tr_" + odontologo.id;
          odontologoRow.id = tr_id;

          //por cada odontologo creamos un boton delete que agregaremos en cada fila para poder eliminar la misma
          //dicho boton invocara a la funcion de java script deleteByKey que se encargará
          //de llamar a la API para eliminar un odontologo
          let deleteButton =
            "<button" +
            " id=" +
            '"' +
            "btn_delete_" +
            odontologo.id +
            '"' +
            ' type="button" onclick="deleteBy(' +
            odontologo.id +
            ')" class="btn btn-danger btn_delete">' +
            "&times" +
            "</button>";

          //por cada odontologo creamos un boton que muestra el id y que al hacerle clic invocará
          //a la función de java script findBy que se encargará de buscar el odontologo que queremos
          //modificar y mostrar los datos del mismo en un formulario.
          let updateButton =
            "<button" +
            " id=" +
            '"' +
            "btn_id_" +
            odontologo.id +
            '"' +
            ' type="button" onclick="findBy(' +
            odontologo.id +
            ')" class="btn btn-info btn_id">' +
            odontologo.id +
            "</button>";

          //armamos cada columna de la fila
          //como primer columna pondremos el boton modificar
          //luego los datos del odontologo
          //como ultima columna el boton eliminar
          odontologoRow.innerHTML =
            "<td>" +
            odontologo.id +
            "</td>" +
            '<td class="td_matricula">' +
            odontologo.numeroMatricula.toUpperCase() +
            "</td>" +
            '<td class="td_nombre">' +
            odontologo.nombre.toUpperCase() +
            "</td>" +
            '<td class="td_apellido">' +
            odontologo.apellido.toUpperCase() +
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
    if (pathname == "/get_odontologos.html") {
      document.querySelector(".nav .nav-item a:last").addClass("active");
    }
  });
});