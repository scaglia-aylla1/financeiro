package com.scaglia.financeiro.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API: Controle Financeiro Pessoal",
                version = "v1.0.0",
                description = "API RESTful completa para gerenciamento de finanças pessoais, incluindo autenticação JWT, CRUD, relatórios de balanço e filtros avançados (paginação e ordenação).",
                contact = @Contact(
                        name = "Aylla Scaglia",
                        email = "aylla@scaglia.com.br",
                        url = "https://github.com/scaglia-aylla1/financeiro"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"
                )
        )
)
public class OpenApiConfig {
    // Esta classe usa anotações para configurar o Swagger globalmente.
    // Nenhuma lógica de código é necessária aqui, apenas a configuração.
}
