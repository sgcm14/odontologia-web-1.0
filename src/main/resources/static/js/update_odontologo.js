window.addEventListener("load", function () {
  //Buscamos y obtenemos el formulario donde estan
  //los datos que el usuario pudo haber modificado del odontologo
  const formulario = document.querySelector("#update_odontologo_form");

  formulario.addEventListener("submit", function (event) {
    // let odontologoId = document.querySelector('#id').value;

    //creamos un JSON que tendrá los datos del odontologo
    //a diferencia de un odontologo nuevo en este caso enviamos el id
    //para poder identificarlo y modificarlo para no cargarlo como nuevo
    const formData = {
      id: document.querySelector("#id").value,
      numeroMatricula: document.querySelector("#numeroMatricula").value,
      nombre: document.querySelector("#nombre").value,
      apellido: document.querySelector("#apellido").value,
    };

    //invocamos utilizando la función fetch la API odontologos con el método PUT que modificará
    //el odontologo que enviaremos en formato JSON
    const url = "/odontologos";
    const settings = {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    };
    fetch(url, settings).then((response) => response.json());
  });
});

//Es la funcion que se invoca cuando se hace click sobre el id de un odontologo del listado
//se encarga de llenar el formulario con los datos del odontologo
//que se desea modificar
function findBy(id) {
  const url = "/odontologos" + "/" + id;
  const settings = {
    method: "GET",
  };
  fetch(url, settings)
    .then((response) => response.json())
    .then((data) => {
      let odontologo = data;
      document.querySelector("#id").value = odontologo.id;
      document.querySelector("#numeroMatricula").value =
        odontologo.numeroMatricula;
      document.querySelector("#nombre").value = odontologo.nombre;
      document.querySelector("#apellido").value = odontologo.apellido;
      //el formulario por default esta oculto y al editar se habilita
      document.querySelector("#div_odontologo_updating").style.display =
        "block";
    })
    .catch((error) => {
      alert("Error: " + error);
    });
}