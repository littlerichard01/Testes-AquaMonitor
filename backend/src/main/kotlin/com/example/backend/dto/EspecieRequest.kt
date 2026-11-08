package com.example.backend.dto

import com.example.backend.entity.TipoEspecie
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

@Schema(description = "Requisicao para criar ou atualizar uma Especie.")
data class EspecieRequest(
    @field:NotBlank(message = "Nome popular e obrigatorio")
    @Schema(description = "Nome popular da especie", example = "Peixe-palhaco")
    val nomePopular: String,

    @Schema(description = "Nome cientifico da especie", example = "Amphiprion ocellaris", nullable = true)
    val nomeCientifico: String? = null,

    @field:NotNull(message = "Tipo da especie e obrigatorio")
    @Schema(description = "Categoria taxonomica da especie", example = "PEIXE")
    val tipo: TipoEspecie,

    @Schema(description = "Informacoes gerais sobre a especie", nullable = true)
    val descricao: String? = null,

    @Schema(description = "Temperatura minima recomendada em graus Celsius", example = "24.00", nullable = true)
    val temperaturaMinima: BigDecimal? = null,

    @Schema(description = "Temperatura maxima recomendada em graus Celsius", example = "28.00", nullable = true)
    val temperaturaMaxima: BigDecimal? = null
)
