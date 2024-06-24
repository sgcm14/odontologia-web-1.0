window.addEventListener("load", function () {
  //Al cargar la pagina buscamos y obtenemos el formulario donde estarán
  //los datos que el usuario cargará del nuevo paciente
  const formulario = document.querySelector("#add_new_paciente");

  //Ante un submit del formulario se ejecutará la siguiente funcion
  formulario.addEventListener("submit", function (event) {
    event.preventDefault();
    //creamos un JSON que tendrá los datos del nuevo paciente
    const formData = {
      nombre: document.getElementById("nombre").value,
      apellido: document.getElementById("apellido").value,
      cedula: document.getElementById("cedula").value,
      fechaIngreso: document.getElementById("fechaIngreso").value,
      domicilio: {
        calle: document.getElementById("calle").value,
        numero: document.getElementById("numero").value,
        localidad: document.getElementById("localidad").value,
        provincia: document.getElementById("provincia").value,
      },
      email: document.getElementById("email").value,
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
      .then((response) => {
        let responseData = response.json();
        return responseData;
       })
      .then((data) => {
        //Si no hay ningun error se muestra un mensaje diciendo que el paciente
        //se agrego bien
       let successAlert =
         '<div class="alert alert-success alert-dismissible fade show" role="alert">' +
         '<strong>Paciente Guardado</strong>' +
         '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>' +
         '</div>';
        document.getElementById("response").innerHTML = successAlert;
        document.getElementById("response").style.display = "block";
        formulario.reset();
      })
      .catch((error) => {
        //Si hay algun error se muestra un mensaje diciendo que el paciente
        //no se pudo guardar y se intente nuevamente
        let errorAlert =
          '<div class="alert alert-danger alert-dismissible fade show" role="alert">' +
          '<strong>Error: Intente nuevamente</strong>' +
          '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>' +
          '</div>';
        document.getElementById("response").innerHTML = errorAlert;
        document.getElementById("response").style.display = "block";
      });
  });

  (function () {
    let pathname = window.location.pathname;
    if (pathname === "/") {
      document.querySelector(".nav .nav-item a:first").addClass("active");
    } else if (pathname == "/get_pacientes.html") {
      document.querySelector(".nav .nav-item a:last").addClass("active");
    }
  })();
});