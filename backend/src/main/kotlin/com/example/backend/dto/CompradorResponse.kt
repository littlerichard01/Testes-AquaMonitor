package com.example.backend.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "Resposta contendo os dados de um Comprador.")
data class CompradorResponse(
    @Schema(description = "Identificador unico do comprador", example = "1")
    val id: Long,

    @Schema(description = "Nome completo", example = "Joao da Silva")
    val nome: String,

    @Schema(description = "CPF (11 digitos)", nullable = true, example = "12345678901")
    val cpf: String?,

    @Schema(description = "Email", nullable = true, example = "joao@email.com")
    val email: String?,

    @Schema(description = "Telefone com DDD", nullable = true, example = "11987654321")
    val telefone: String?,

    @Schema(description = "Observacoes adicionais", nullable = true)
    val observacoes: String?,

    @Schema(description = "Data e hora do cadastro")
    val criadoEm: LocalDateTime,

    @Schema(description = "Data e hora da ultima atualizacao")
    val atualizadoEm: LocalDateTime
)
