package com.example.backend.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "Resposta contendo os dados de uma Loja (sem expor a senha).")
data class LojaResponse(
    @Schema(description = "Identificador unico da loja", example = "1")
    val id: Long,

    @Schema(description = "Nome do responsavel ou identificacao da loja", example = "Aquarios Silva Ltda")
    val nome: String,

    @Schema(description = "Email de autenticacao", example = "contato@aquariossilva.com")
    val email: String,

    @Schema(description = "Se a conta esta ativa", example = "true")
    val ativo: Boolean,

    @Schema(description = "Data e hora do cadastro")
    val criadoEm: LocalDateTime,

    @Schema(description = "Data e hora da ultima atualizacao")
    val atualizadoEm: LocalDateTime
)
