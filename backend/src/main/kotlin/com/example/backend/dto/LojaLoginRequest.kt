package com.example.backend.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

@Schema(description = "Requisicao para realizar login de uma Loja.")
data class LojaLoginRequest(
    @field:NotBlank(message = "O email e obrigatorio")
    @field:Email(message = "O email deve ser valido")
    @Schema(description = "Email cadastrado da loja", example = "contato@aquariossilva.com")
    val email: String,

    @field:NotBlank(message = "A senha e obrigatoria")
    @Schema(description = "Senha da loja", example = "minhasenha123")
    val senha: String
)
