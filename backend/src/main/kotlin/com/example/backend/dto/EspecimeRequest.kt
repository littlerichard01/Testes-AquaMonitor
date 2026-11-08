package com.example.backend.dto

import com.example.backend.entity.StatusEspecime
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.time.LocalDate

@Schema(description = "Requisicao para criar ou atualizar um Especime (criatura individual da loja).")
data class EspecimeRequest(
    @field:NotBlank(message = "Identificacao do especime e obrigatoria")
    @Schema(description = "Codigo unico do especime na loja", example = "PEIXE-034")
    val identificacao: String,

    @field:NotNull(message = "Preco do especime e obrigatorio")
    @field:Positive(message = "Preco deve ser positivo")
    @Schema(description = "Valor de venda", example = "120.00")
    val preco: BigDecimal,

    @Schema(description = "Status: DISPONIVEL / VENDIDO / INDISPONIVEL. Default: DISPONIVEL", example = "DISPONIVEL", nullable = true)
    val status: StatusEspecime? = StatusEspecime.DISPONIVEL,

    @Schema(description = "Observacoes individuais", nullable = true, example = "Machadinho, cor vibrante")
    val observacoes: String? = null,

    @Schema(description = "Data de entrada na loja. Default = hoje", nullable = true, example = "2026-09-10")
    val dataEntrada: LocalDate? = LocalDate.now(),

    @field:NotNull(message = "ID da especie e obrigatorio")
    @Schema(description = "ID da Especie a qual o especime pertence", example = "1")
    val especieId: Long,

    @Schema(description = "ID do Aquario onde esta alocado. Pode ser NULL se ainda nao alocado", nullable = true, example = "3")
    val aquarioId: Long? = null
)
