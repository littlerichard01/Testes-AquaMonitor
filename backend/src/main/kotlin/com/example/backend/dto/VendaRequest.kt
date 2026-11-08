package com.example.backend.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.time.LocalDateTime

@Schema(
    description = "Requisicao para registrar uma Venda. Regra XOR: informe EXATAMENTE UM entre especimeId ou aquarioId."
)
data class VendaRequest(
    @field:NotNull(message = "Valor da venda e obrigatorio")
    @field:Positive(message = "Valor deve ser positivo")
    @Schema(description = "Valor pelo qual o item foi vendido", example = "150.00")
    val valor: BigDecimal,

    @Schema(description = "Data/hora do registro. Default = agora", nullable = true)
    val dataVenda: LocalDateTime? = LocalDateTime.now(),

    @Schema(description = "Observacoes sobre a venda", nullable = true, example = "Pagamento em Pix")
    val observacoes: String? = null,

    @field:NotNull(message = "ID do comprador e obrigatorio")
    @Schema(description = "ID do Comprador", example = "2")
    val compradorId: Long,

    @Schema(
        description = "ID do Especime vendido. Informe SOMENTE se aquarioId for NULL. Regra XOR",
        nullable = true,
        example = "5"
    )
    val especimeId: Long? = null,

    @Schema(
        description = "ID do Aquario vendido. Informe SOMENTE se especimeId for NULL. Regra XOR",
        nullable = true,
        example = "3"
    )
    val aquarioId: Long? = null
)
