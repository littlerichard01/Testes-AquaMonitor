package com.example.backend.dto

import com.example.backend.entity.Especie
import com.example.backend.entity.TipoEspecie
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDateTime

@Schema(description = "Resposta contendo os dados de uma Especie.")
data class EspecieResponse(
    @Schema(description = "Identificador unico da especie", example = "1")
    val id: Long,

    @Schema(description = "Nome popular da especie", example = "Peixe-palhaco")
    val nomePopular: String,

    @Schema(description = "Nome cientifico da especie", example = "Amphiprion ocellaris", nullable = true)
    val nomeCientifico: String?,

    @Schema(description = "Categoria taxonomica da especie", example = "PEIXE")
    val tipo: TipoEspecie,

    @Schema(description = "Informacoes gerais sobre a especie", nullable = true)
    val descricao: String?,

    @Schema(description = "Temperatura minima recomendada em graus Celsius", example = "24.00", nullable = true)
    val temperaturaMinima: BigDecimal?,

    @Schema(description = "Temperatura maxima recomendada em graus Celsius", example = "28.00", nullable = true)
    val temperaturaMaxima: BigDecimal?,

    @Schema(description = "Data e hora em que a especie foi cadastrada")
    val criadoEm: LocalDateTime,

    @Schema(description = "Data e hora da ultima atualizacao")
    val atualizadoEm: LocalDateTime
)