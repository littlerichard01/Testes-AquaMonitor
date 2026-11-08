package com.example.backend.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    companion object {
        private const val BEARER_JWT = "bearerAuth"
    }

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .addServersItem(
                Server()
                    .url("http://localhost:8081")
                    .description("Servidor local de desenvolvimento")
            )
            .components(
                Components()
                    .addSecuritySchemes(
                        BEARER_JWT,
                        SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                            .name(BEARER_JWT)
                            .description(
                                "Autenticacao por Bearer Token JWT. " +
                                    "Acesse POST /api/auth/signin para obter um token, " +
                                    "depois clique em Authorize e cole o valor (sem a palavra 'Bearer')."
                            )
                    )
            )
            .addSecurityItem(
                SecurityRequirement().addList(BEARER_JWT)
            )
            .info(
                Info()
                    .title("AquaMonitor - API REST")
                    .version("0.0.1-SNAPSHOT")
                    .description(
                        "Sistema de gerenciamento e monitoramento de lojas de aquarismo. " +
                            "Inclui CRUD de lojas, aquários, espécies, espécimes, compradores, vendas, alertas, " +
                            "dispositivos IoT, configurações de temperatura, programações de alimentação e " +
                            "recebimento de leituras de temperatura via integração MQTT com ESP32.\n\n" +
                            "**Autenticacao**\n\n" +
                            "1. Utilize `POST /api/auth/signup` para criar uma conta de loja (publica).\n" +
                            "2. Utilize `POST /api/auth/signin` para obter um token JWT (publico).\n" +
                            "3. Clique no botao Authorize no topo da pagina e cole o token.\n" +
                            "4. Todos os outros endpoints exigem autenticacao via Bearer Token."
                    )
                    .contact(
                        Contact()
                            .name("Projeto Integrador 6º Semestre")
                            .url("https://github.com/littlerichard01/AquaMonitor")
                    )
                    .license(
                        License()
                            .name("MIT License")
                            .url("https://opensource.org/licenses/MIT")
                    )
            )
    }
}
