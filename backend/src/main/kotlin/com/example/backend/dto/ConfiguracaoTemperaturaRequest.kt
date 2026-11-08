package com.example.backend.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

@Schema(description = "Requisicao para criar ou atualizar ConfiguracaoTemperatura (1:1 com Aquario).")
data class ConfiguracaoTemperaturaRequest(
    @field:NotNull(message = "Temperatura minima e obrigatoria")
    @field:DecimalMin(value = "0.0", message = "Temperatura minima nao pode ser menor que 0")
    @field:DecimalMax(value = "50.0", message = "Temperatura minima nao pode ser maior que 50")
    @field:Digits(integer = 3, fraction = 2, message = "Temperatura minima deve ter no maximo 2 casas decimais")
    @Schema(description = "Limite inferior aceitavel em graus Celsius", example = "24.00")
    val temperaturaMinima: BigDecimal,

    @field:NotNull(message = "Temperatura maxima e obrigatoria")
    @field:DecimalMin(value = "0.0", message = "Temperatura maxima nao pode ser menor que 0")
    @field:DecimalMax(value = "50.0", message = "Temperatura maxima nao pode ser maior que 50")
    @field:Digits(integer = 3, fraction = 2, message = "Temperatura maxima deve ter no maximo 2 casas decimais")
    @Schema(description = "Limite superior aceitavel em graus Celsius", example = "28.00")
    val temperaturaMaxima: BigDecimal,

    @Schema(description = "Se a configuracao esta ativa. Default = true", example = "true", nullable = true)
    val ativo: Boolean? = true,

    @field:NotNull(message = "ID do aquario e obrigatorio")
    @field:Positive(message = "ID do aquario deve ser positivo")
    @Schema(description = "ID do Aquario (relacionamento 1:1 — cada aquario tem exatamente UMA configuracao)", example = "3")
    val aquarioId: Long
)
