package appVGShop.shared.config;

import org.modelmapper.*;
import org.springframework.context.annotation.*;

@Configuration
public class BaseConfiguration {

    /**
     * ModelMapper es una librería Java para copiar o mapear propiedades de un
     * tipo de objeto a otro tipo de objeto, permitiendo copiar también los
     * datos de las referencias a los objetos que contengan.
     *
     * @return
     */
    @Bean
    public ModelMapper modelMapper() {

        return new ModelMapper();

    }

}
