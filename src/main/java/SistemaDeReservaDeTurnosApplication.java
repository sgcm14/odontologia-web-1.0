import dao.BD;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SistemaDeReservaDeTurnosApplication {
    public static void main(String[] args) {
        BD.crearTablas();
        SpringApplication.run(SistemaDeReservaDeTurnosApplication.class, args);
    }
}
