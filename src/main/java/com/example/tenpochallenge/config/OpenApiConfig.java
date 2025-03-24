import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server()
                .url("http://localhost:8080")
                .description("Servidor de desarrollo");

        Contact contact = new Contact()
                .name("Tenpo Challenge")
                .email("support@tenpo.com");

        Info info = new Info()
                .title("Tenpo Challenge API")
                .version("1.0")
                .contact(contact)
                .description("API para el cálculo de porcentajes con rate limiting")
                .license(new License().name("Apache 2.0").url("http://springdoc.org"));

        // Agregar ErrorResponse a los componentes
        Components components = new Components()
                .schemas(Map.of(
                    "ErrorResponse", new ObjectSchema()
                            .description("Estructura de respuesta para errores de la API")
                            .addProperty("message", new Schema<String>().description("Mensaje descriptivo del error"))
                ));

        return new OpenAPI()
                .info(info)
                .servers(java.util.List.of(devServer))
                .components(components);
    }
}
