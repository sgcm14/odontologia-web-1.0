window.addEventListener("load", function () {
  //Al cargar la pagina buscamos y obtenemos el formulario donde estarán
  //los datos que el usuario cargará del nuevo turno
  const formulario = document.querySelector("#add_new_turno");

   // Función para obtener y llenar la lista de pacientes desde la API
   function obtenerPacientes() {
    fetch("/pacientes")
      .then((response) => response.json())
      .then((data) => {
        const selectPaciente = document.querySelector("#paciente");
        selectPaciente.innerHTML = ""; // Limpiar opciones actuales
        data.forEach((paciente) => {
          const option = document.createElement("option");
          option.value = paciente.id;
          option.textContent = `${paciente.nombre} ${paciente.apellido}`;
          selectPaciente.appendChild(option);
        });
      })
      .catch((error) => {
        console.error("Error obteniendo pacientes:", error);
        alert("Error: No se pudo cargar la lista de pacientes.");
      });
  }

  // Función para obtener y llenar la lista de odontólogos desde la API
  function obtenerOdontologos() {
    fetch("/odontologos")
      .then((response) => response.json())
      .then((data) => {
        const selectOdontologo = document.querySelector("#odontologo");
        selectOdontologo.innerHTML = ""; // Limpiar opciones actuales
        data.forEach((odontologo) => {
          const option = document.createElement("option");
          option.value = odontologo.id;
          option.textContent = `${odontologo.nombre} ${odontologo.apellido}`;
          selectOdontologo.appendChild(option);
        });
      })
      .catch((error) => {
        console.error("Error obteniendo odontólogos:", error);
        alert("Error: No se pudo cargar la lista de odontólogos.");
      });
  }

  // Cargar lista de pacientes y odontólogos al cargar la página
  obtenerPacientes();
  obtenerOdontologos();

  //Ante un submit del formulario se ejecutará la siguiente funcion
  formulario.addEventListener("submit", function (event) {
    event.preventDefault();
    //creamos un JSON que tendrá los datos del nuevo turno
    const formData = {
      paciente: {
        id: document.querySelector("#paciente").value,
      },
      odontologo: {
        id: document.querySelector("#odontologo").value,
      },
      fechaHoraCita: document.querySelector("#fechaHoraCita").value,
    };
    //invocamos utilizando la función fetch la API turnos con el método POST que guardará
    //el turno que enviaremos en formato JSON
    const url = "/turnos";
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
        //Si no hay ningun error se muestra un mensaje diciendo que el turno
        //se agrego bien
        let successAlert =
                 '<div class="alert alert-success alert-dismissible fade show" role="alert">' +
                 '<strong>Turno Guardado</strong>' +
                 '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>' +
                 '</div>';
        document.querySelector("#response").innerHTML = successAlert;
        document.querySelector("#response").style.display = "block";
        formulario.reset();
      })
      .catch((error) => {
        //Si hay algun error se muestra un mensaje diciendo que el turno
        //no se pudo guardar y se intente nuevamente
        let errorAlert =
                  '<div class="alert alert-danger alert-dismissible fade show" role="alert">' +
                  '<strong>Error: Intente nuevamente</strong>' +
                  '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>' +
                  '</div>';
        document.querySelector("#response").innerHTML = errorAlert;
        document.querySelector("#response").style.display = "block";
      });
  });

  (function () {
    let pathname = window.location.pathname;
    if (pathname === "/") {
      document.querySelector(".nav .nav-item a:first").addClass("active");
    } else if (pathname == "/get_turnos.html") {
      document.querySelector(".nav .nav-item a:last").addClass("active");
    }
  })();
});