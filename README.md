Sistema de reserva de turnos para Clinica Odontologica
---

Se desea implementar un sistema que permita administrar la reserva de turnos para una clínica odontológica. Esta necesita informatizar su operatoria. Por lo cual, te solicitan un sistema que debe cumplir con los siguientes requerimientos:

* **Administración de datos de odontólogos:** listar, agregar, modificar y eliminar odontólogos. Registrar apellido, nombre y matrícula de los mismos.

* **Administración de datos de los pacientes:** listar, agregar, modificar y eliminar pacientes. Al registrar un paciente los datos que se le solicitan son:
    * Apellido
    * Nombre
    * DNI
    * Fecha de alta 
    * Domicilio 
        * Calle
        * Número
        * Localidad
        * Provincia

    Además, le agregaremos un ID autoincremental tanto a los pacientes como a los domicilios.

    Se pide utilizar H2 como base de datos, aplicar el patrón DAO y testear con JUnit. Tener en cuenta que el modelado de clases debe contar con al menos dos clases: **PACIENTE** y **DOMICILIO**, con la consideración de que los pacientes podrán tener solo un domicilio.

    Crear solo una clase de servicio, PacienteService, y crear por cada entidad un DAO, es decir, DomicilioDAOH2 y PacienteDAOH2. Al guardar y buscar un paciente en PacienteDAOH2 deberás invocar el guardar y buscar de DomicilioDAOH2.

* **Registrar turno**: se tiene que poder permitir asignar a un paciente un turno con un odontólogo a una determinada fecha y hora. 

* **Login**: validar el ingreso al sistema mediante un login con usuario y password. 
Se debe permitir a cualquier usuario logueado **(ROLE_USER)** registrar un turno, pero solo a quienes tengan un rol de administración **(ROLE_ADMIN)** poder gestionar odontólogos y pacientes. Un usuario podrá tener un único rol y los mismos se ingresarán directamente en la base de datos.

**Requerimientos técnicos:**
---

La aplicación debe ser desarrollada en capas:
* **Capa de entidades de negocio:** son las clases Java de nuestro negocio modelado a través del paradigma orientado a objetos.

* **Capa de acceso a datos (Repository)**: son las clases que se encargarán de acceder a la base de datos.

* **Capa de datos (base de datos)**: es la base de datos de nuestro sistema modelado a través de un modelo entidad-relación. Utilizaremos la base H2 por su practicidad. 
* **Capa de negocio**: son las clases service que se encargan de desacoplar el acceso a datos de la vista.

* **Capa de presentación**: son las pantallas web que tendremos que desarrollar utilizando el framework de Spring Boot MVC con los controladores y alguna de estas dos opciones: HTML+JavaScript para la vista.


**Tests de aceptación:**
---

Es importante realizar el manejo de excepciones logueando cualquier excepción que se pueda generar y la realización de test unitarios para garantizar la calidad de los desarrollos.

**Tests de integración:**

* Realizar test de integración para el controller de turnos, odontólogos y pacientes.
* Utilizando MockMvc, testear el endpoint que nos retorna un listado de turnos, odontólogos y pacientes. 
* El test debe comprobar que el código de respuesta es 200 y que en el body nos retorna un listado de turnos, odontólogos y pacientes con datos.
* Utilizando MockMvc, testear el endpoint que nos permite registrar un nuevo turno, odontólogo y paciente. 
* El test debe comprobar que el código de respuesta es 200 y que en el body nos retorne el turno, odontólogo y paciente cargado.
* Utilizando MockMvc, testear el endpoint que nos permite actualizar un turno, odontólogo y paciente. 
* El test debe comprobar que el código de respuesta es 200 y que en el body nos retorne el turno, odontólogo y paciente cargado.
* Utilizando MockMvc, testear el endpoint que nos permite eliminar un turno, odontólogo y paciente. 


**APIS:**
---

* Listar todos los odontólogos:
    
    Método: GET.

        PATH: /odontologos 

* Buscar un odontólogo: 

    Método: GET.
        
        PATH: /odontologos/{id} 

* Guardar odontólogo:

    Método: POST

        PATH: /odontologos 

* Eliminar odontólogo:
    
    Método: DELETE.

        PATH: /odontologos/eliminar/{id} 

* Actualizar odontólogo:

    Método: PUT

        PATH: /odontologos 

* Listar todos los pacientes:
    
    Método: GET.

        PATH: /pacientes 

* Buscar un paciente: 

    Método: GET.
        
        PATH: /pacientes/{id} 

* Buscar un paciente por email: 

    Método: GET.
        
        PATH: /pacientes/email/{email}

* Guardar paciente:

    Método: POST

        PATH: /pacientes 

* Eliminar paciente:
    
    Método: DELETE.

        PATH: /pacientes/eliminar/{id} 

* Actualizar paciente:

    Método: PUT

        PATH: /pacientes 

* Listar todos los turnos:
    
    Método: GET.

        PATH: /turnos 

* Buscar un turno:
    
    Método: GET

        PATH: /turnos/{id} 

* Guardar turno:

    Método: POST

        PATH: /turnos 

* Eliminar turno:
        
   Método: DELETE

        PATH :/turnos/{id} 

* Actualizar turno:

    Método: PUT

        PATH: /turnos 

**Documentación de las APIS**
---

Para poder revisar la documentación, al tener implementado security, primero te pedirá tus credenciales, las cuales deberás ingresarlas como se muestra a continuación:

![](https://raw.githubusercontent.com/sgcm14/odontologia-web-1.0/main/src/docs/Captura1.PNG)
> Pantalla de login

Luego ir a la url: http://localhost:8080/swagger-ui.html y podrás tener toda la información acerca de las APIS:

![](https://raw.githubusercontent.com/sgcm14/odontologia-web-1.0/main/src/docs/doc1.PNG)
> 1era Pantalla de swagger

![](https://raw.githubusercontent.com/sgcm14/odontologia-web-1.0/main/src/docs/doc2.PNG)
> 2da Pantalla de swagger

![](https://raw.githubusercontent.com/sgcm14/odontologia-web-1.0/main/src/docs/doc3.PNG)
> 3era Pantalla de swagger

**Ejecución:**
---

En esta primera vista te pedirá ingresar las credenciales:
![](https://raw.githubusercontent.com/sgcm14/odontologia-web-1.0/main/src/docs/Captura1.PNG)
> Pantalla de login

Si ingresamos con un tipo admin:
![](https://raw.githubusercontent.com/sgcm14/odontologia-web-1.0/main/src/docs/Captura2.PNG)
> Pantalla de login - admin

En esta vista podemos ver las opciones que tenemos siendo admin:
![](https://raw.githubusercontent.com/sgcm14/odontologia-web-1.0/main/src/docs/Captura3.PNG)
> Pantalla de menu - admin

En esta vista se observa el menu **pacientes**
![](https://raw.githubusercontent.com/sgcm14/odontologia-web-1.0/main/src/docs/Captura4.PNG)
>  Pantalla de menu **pacientes**

Si deseamos agregar un paciente, ingresamos a **Guardar**, agregamos los datos y le damos en **Guardar**
![](https://raw.githubusercontent.com/sgcm14/odontologia-web-1.0/main/src/docs/Captura5.PNG)
> Pantalla de **agregar paciente**

En esta vista se listan los **pacientes**
![](https://raw.githubusercontent.com/sgcm14/odontologia-web-1.0/main/src/docs/Captura6.PNG)
> Pantalla de **listado de pacientes**

Si queremos editar algun paciente le damos click al botón celeste al lado del item de nuestro interés, llenamos los datos y le damos a **modificar**. En caso queramos eliminarlo le damos click a la **x** 
![](https://raw.githubusercontent.com/sgcm14/odontologia-web-1.0/main/src/docs/Captura7.PNG)
> Pantalla de **actualizar paciente**

En esta vista se observa el menu **odontólogos**
![](https://raw.githubusercontent.com/sgcm14/odontologia-web-1.0/main/src/docs/Captura8.PNG)
>  Pantalla de menu **odontólogos** - admin

Si deseamos agregar un odontólogo, ingresamos a **Guardar**, agregamos los datos y le damos en **Guardar**
![](https://raw.githubusercontent.com/sgcm14/odontologia-web-1.0/main/src/docs/Captura9.PNG)
> Pantalla de **agregar odontólogo**

En esta vista se listan los **odontólogos**
![](https://raw.githubusercontent.com/sgcm14/odontologia-web-1.0/main/src/docs/Captura10.PNG)
> Pantalla de **listado de ondotólogos**

Si queremos editar algun odontólogo le damos click al botón celeste al lado del item de nuestro interés, llenamos los datos y le damos a **modificar**. En caso queramos eliminarlo le damos click a la **x** 
![](https://raw.githubusercontent.com/sgcm14/odontologia-web-1.0/main/src/docs/Captura11.PNG)
> Pantalla de **actualizar odontólogo**

En caso queramos cambiar de usuario le damos click a **Log Out** de la parte superior derecha
![](https://raw.githubusercontent.com/sgcm14/odontologia-web-1.0/main/src/docs/Captura12.PNG)
> Pantalla de logout

En esta vista te mostrará el login con un mensaje de deslogueado:
![](https://raw.githubusercontent.com/sgcm14/odontologia-web-1.0/main/src/docs/Captura13.PNG)
> Pantalla de login

Si ingresamos con un tipo user:
![](https://raw.githubusercontent.com/sgcm14/odontologia-web-1.0/main/src/docs/Captura14.PNG)
> Pantalla de login - user

En esta vista podemos ver las opciones que tenemos siendo user:
![](https://raw.githubusercontent.com/sgcm14/odontologia-web-1.0/main/src/docs/Captura15.PNG)
> Pantalla de menu - user

En esta vista se observa el menu **turnos**
![](https://raw.githubusercontent.com/sgcm14/odontologia-web-1.0/main/src/docs/Captura16.PNG)
>  Pantalla de menu **turnos**

Si deseamos agregar un turno, ingresamos a **Guardar**, podemos escoger al paciente y al odóntologo desde los selects, y escoger fecha y hora desde el calendar.
![](https://raw.githubusercontent.com/sgcm14/odontologia-web-1.0/main/src/docs/Captura17.PNG)
> Pantalla de **agregar turno**

Agregamos los datos y le damos en **Guardar**
![](https://raw.githubusercontent.com/sgcm14/odontologia-web-1.0/main/src/docs/Captura18.PNG)
> Pantalla de **agregar turno**

En esta vista se listan los **turnos**
![](https://raw.githubusercontent.com/sgcm14/odontologia-web-1.0/main/src/docs/Captura19.PNG)
> Pantalla de **listado de turnos**

Si queremos editar algun turno le damos click al botón celeste al lado del item de nuestro interés, llenamos los datos y le damos a **modificar**. En caso queramos eliminarlo le damos click a la **x** 
![](https://raw.githubusercontent.com/sgcm14/odontologia-web-1.0/main/src/docs/Captura20.PNG)
> Pantalla de **actualizar turno**

En caso queramos cambiar de usuario le damos click a **Log Out** de la parte superior derecha
![](https://raw.githubusercontent.com/sgcm14/odontologia-web-1.0/main/src/docs/Captura21.PNG)
> Pantalla de logout

**Realizado por :** Sammy Gigi Cantoral Montejo (sgcm14)

<img src ="https://raw.githubusercontent.com/sgcm14/sgcm14/main/sammy.jpg" width="200">
