package appVGShop;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Clase de tipo Main
 */
@SpringBootApplication
public class Application {

    /**
     * Ejecuta la aplicación
     *
     * @param args
     */
    public static void main(String[] args) {

        SpringApplication.run(Application.class, args); //Ejecuta el protocolo de inicio de la aplicación Spring

    }

}
