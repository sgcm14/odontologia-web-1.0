package pruebas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class SistemaDeReservaDeTurnosApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaDeReservaDeTurnosApplication.class, args);
	}

	@GetMapping
	public String holaMundo(){
		return "holaMundo";
	}


}
