package com.example.backend.dto

import com.example.backend.entity.SeveridadeAlerta
import com.example.backend.entity.TipoAlerta
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

@Schema(
    description = "Requisicao para criar ou atualizar um Alerta. A regra de negocio do alerta e orquestrada no backend."
)
data class AlertaRequest(
    @field:NotNull(message = "Tipo do alerta e obrigatorio")
    @Schema(description = "TEMPERATURA (compara limites de ConfiguracaoTemperatura) ou ALIMENTACAO (verifica horario de ProgramacaoAlimentacao)", example = "TEMPERATURA")
    val tipo: TipoAlerta,

    @field:NotNull(message = "Severidade do alerta e obrigatoria")
    @Schema(description = "BAIXA / MEDIA / ALTA — influencia a cor/estilo visual no front", example = "MEDIA")
    val severidade: SeveridadeAlerta,

    @Schema(description = "Se a regra esta ativa. Default = true", example = "true", nullable = true)
    val ativo: Boolean? = true,

    @field:NotNull(message = "ID do aquario e obrigatorio")
    @Schema(description = "ID do Aquario associado ao alerta", example = "3")
    @field:Positive(message = "ID do aquario deve ser maior que zero")
    val aquarioId: Long
)
