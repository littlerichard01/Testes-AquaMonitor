package com.example.backend.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Resposta contendo o token de acesso apos login da Loja.")
data class LoginResponse(
    @Schema(description = "Token JWT de acesso", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    val token: String,

    @Schema(description = "Tipo do token (padrao de mercado Bearer)", example = "Bearer")
    val tipo: String = "Bearer",

    @Schema(description = "Identificador unico da loja logada", example = "1")
    val lojaId: Long,

    @Schema(description = "Nome da loja logada", example = "Aquarios Silva Ltda")
    val nomeLoja: String,

    @Schema(description = "Email autenticado da loja", example = "contato@aquariossilva.com")
    val emailLoja: String
)
