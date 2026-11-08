package com.example.backend.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDateTime

@Schema(description = "Resposta contendo a ConfiguracaoTemperatura de um Aquario (1:1).")
data class ConfiguracaoTemperaturaResponse(
    @Schema(description = "Identificador unico", example = "1")
    val id: Long,

    @Schema(description = "Limite inferior em graus Celsius", example = "24.00")
    val temperaturaMinima: BigDecimal,

    @Schema(description = "Limite superior em graus Celsius", example = "28.00")
    val temperaturaMaxima: BigDecimal,

    @Schema(description = "Se a configuracao esta ativa", example = "true")
    val ativo: Boolean,

    @Schema(description = "Resumo do Aquario associado")
    val aquario: AquarioResumo,

    @Schema(description = "Data e hora do cadastro")
    val criadoEm: LocalDateTime,

    @Schema(description = "Data e hora da ultima atualizacao")
    val atualizadoEm: LocalDateTime
)