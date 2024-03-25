package GestionPagoMensual.club;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PagoCuotaClubApplication {

	public static void main(String[] args) {
		SpringApplication.run(PagoCuotaClubApplication.class, args);
	}

}
