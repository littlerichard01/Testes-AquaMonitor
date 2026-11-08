package com.example.backend.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.br.CPF

@Schema(description = "Requisicao para criar ou atualizar um Comprador.")
data class CompradorRequest(
    @field:NotBlank(message = "Nome do comprador e obrigatorio")
    @Schema(description = "Nome completo do comprador", example = "Joao da Silva")
    val nome: String,

    @Schema(description = "CPF (11 digitos)", nullable = true, example = "12345678901")
    @field:CPF(message = "CPF invalido")
    val cpf: String? = null,

    @Schema(description = "Email do comprador", nullable = true, example = "joao@email.com")
    @field:Email(message = "Email invalido")
    val email: String? = null,

    @Schema(description = "Telefone com DDD", nullable = true, example = "11987654321")
    val telefone: String? = null,

    @Schema(description = "Observacoes adicionais", nullable = true, example = "Cliente desde 2020, prefere peixes marinhos")
    val observacoes: String? = null
)
