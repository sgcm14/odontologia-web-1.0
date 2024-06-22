window.addEventListener("load", function () {
  //Al cargar la pagina buscamos y obtenemos el formulario donde estarán
  //los datos que el usuario cargará del nuevo paciente
  const formulario = document.querySelector("#add_new_paciente");

  //Ante un submit del formulario se ejecutará la siguiente funcion
  formulario.addEventListener("submit", function (event) {
    //creamos un JSON que tendrá los datos del nuevo paciente
    const formData = {
      nombre: document.querySelector("#nombre").value,
      apellido: document.querySelector("#apellido").value,
      cedula: document.querySelector("#cedula").value,
      fechaIngreso: document.querySelector("#fechaIngreso").value,
      domicilio: {
        calle: document.querySelector("#calle").value,
        numero: document.querySelector("#numero").value,
        localidad: document.querySelector("#localidad").value,
        provincia: document.querySelector("#provincia").value,
      },
      email: document.querySelector("#email").value,
    };
    //invocamos utilizando la función fetch la API pacientes con el método POST que guardará
    //el paciente que enviaremos en formato JSON
    const url = "/pacientes";
    const settings = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    };

    fetch(url, settings)
      .then((response) => response.json())
      .then((data) => {
        //Si no hay ningun error se muestra un mensaje diciendo que el paciente
        //se agrego bien
        let successAlert =
          '<div class="alert alert-success alert-dismissible">' +
          '<button type="button" class="close" data-dismiss="alert">&times;</button>' +
          "<strong></strong> Paciente Guardado </div>";

        document.querySelector("#response").innerHTML = successAlert;
        document.querySelector("#response").style.display = "block";
        resetUploadForm();
      })
      .catch((error) => {
        //Si hay algun error se muestra un mensaje diciendo que el paciente
        //no se pudo guardar y se intente nuevamente
        let errorAlert =
          '<div class="alert alert-danger alert-dismissible">' +
          '<button type="button" class="close" data-dismiss="alert">&times;</button>' +
          "<strong> Error intente nuevamente</strong> </div>";

        document.querySelector("#response").innerHTML = errorAlert;
        document.querySelector("#response").style.display = "block";
        //se dejan todos los campos vacíos por si se quiere ingresar otro paciente
        resetUploadForm();
      });
  });

  function resetUploadForm() {
    document.querySelector("#titulo").value = "";
    document.querySelector("#categoria").value = "";
    document.querySelector("#premios").value = "";
  }

  (function () {
    let pathname = window.location.pathname;
    if (pathname === "/") {
      document.querySelector(".nav .nav-item a:first").addClass("active");
    } else if (pathname == "/get_pacientes.html") {
      document.querySelector(".nav .nav-item a:last").addClass("active");
    }
  })();
});