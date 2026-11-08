package com.example.backend.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "Requisicao para CRIAR uma nova Loja (conta de acesso).")
data class LojaRequest(
    @field:NotBlank(message = "Nome da loja e obrigatorio")
    @Schema(description = "Nome do responsavel ou identificacao da loja", example = "Aquarios Silva Ltda")
    val nome: String,

    @field:NotBlank(message = "Email e obrigatorio")
    @field:Email(message = "Email invalido")
    @Schema(description = "Email usado para autenticacao (UNIQUE)", example = "contato@aquariossilva.com")
    val email: String,

    @field:NotBlank(message = "Senha e obrigatoria")
    @field:Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    @Schema(description = "Senha em texto plano (sera armazenada com BCrypt)", example = "minhasenha123")
    val senha: String,

    @Schema(description = "Indica se a conta esta habilitada", example = "true", nullable = true)
    val ativo: Boolean? = true
)
