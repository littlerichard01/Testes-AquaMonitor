package com.example.backend.dto

import com.example.backend.entity.Alerta
import com.example.backend.entity.SeveridadeAlerta
import com.example.backend.entity.TipoAlerta
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "Resposta contendo os dados de um Alerta.")
data class AlertaResponse(
    @Schema(description = "Identificador unico do alerta", example = "1")
    val id: Long,

    @Schema(description = "TEMPERATURA ou ALIMENTACAO", example = "TEMPERATURA")
    val tipo: TipoAlerta,

    @Schema(description = "BAIXA / MEDIA / ALTA", example = "MEDIA")
    val severidade: SeveridadeAlerta,

    @Schema(description = "Se a regra esta ativa", example = "true")
    val ativo: Boolean,

    @Schema(description = "Resumo do Aquario associado")
    val aquario: AquarioResumo,

    @Schema(description = "Data e hora do cadastro")
    val criadoEm: LocalDateTime,

    @Schema(description = "Data e hora da ultima atualizacao")
    val atualizadoEm: LocalDateTime
)
