package com.example.backend.dto

import com.example.backend.entity.Especime
import com.example.backend.entity.StatusAquario
import com.example.backend.entity.StatusEspecime
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Schema(description = "Resposta contendo os dados de um Especime.")
data class EspecimeResponse(
    @Schema(description = "Identificador unico do especime")
    val id: Long,

    @Schema(description = "Identificacao unica (ex: anilha ou chip)")
    val identificacao: String,

    @Schema(description = "Preco do especime")
    val preco: BigDecimal,

    @Schema(description = "Status atual do especime")
    val status: StatusEspecime,

    @Schema(description = "Observacoes adicionais", nullable = true)
    val observacoes: String?,

    @Schema(description = "Data de entrada do especime")
    val dataEntrada: LocalDate,

    @Schema(description = "ID da especie associada")
    val especieId: Long,

    @Schema(description = "ID do aquario associado", nullable = true)
    val aquarioId: Long?
)
