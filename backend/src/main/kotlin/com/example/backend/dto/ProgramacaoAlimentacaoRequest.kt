package com.example.backend.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

@Schema(description = "Requisicao para criar ou atualizar uma ProgramacaoAlimentacao de um Aquario.")
data class ProgramacaoAlimentacaoRequest(
    @field:NotBlank(message = "Horario e obrigatorio")
    @field:Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "Horario deve estar no formato HH:mm")
    @Schema(description = "Horario no formato HH:mm (24h)", example = "08:00")
    val horario: String,

    @Schema(description = "Descricao opcional", nullable = true, example = "Alimentacao da manha com flocos")
    val descricao: String? = null,

    @Schema(description = "Se a programacao esta ativa. Default = true", example = "true", nullable = true)
    val ativo: Boolean? = true,

    @field:NotNull(message = "ID do aquario e obrigatorio")
    @Schema(description = "ID do Aquario associado", example = "3")
    val aquarioId: Long
)
