package com.example.backend.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDateTime

@Schema(description = "Resposta contendo os dados de uma Venda registrada.")
data class VendaResponse(
    @Schema(description = "Identificador unico da venda", example = "1")
    val id: Long,

    @Schema(description = "Valor da venda", example = "150.00")
    val valor: BigDecimal,

    @Schema(description = "Data/hora do registro")
    val dataVenda: LocalDateTime,

    @Schema(description = "Observacoes", nullable = true, example = "Pagamento em Pix")
    val observacoes: String?,

    @Schema(description = "ID da Loja dona", example = "1")
    val lojaId: Long,

    @Schema(description = "Resumo do Comprador")
    val comprador: CompradorResumo,

    @Schema(description = "Resumo do Especime vendido. NULL se a venda for de um Aquario", nullable = true)
    val especime: EspecimeResumo?,

    @Schema(description = "Resumo do Aquario vendido. NULL se a venda for de um Especime", nullable = true)
    val aquario: AquarioResumo?,

    @Schema(description = "Data e hora do cadastro")
    val criadoEm: LocalDateTime,

    @Schema(description = "Data e hora da ultima atualizacao")
    val atualizadoEm: LocalDateTime
)

@Schema(description = "Resumo simplificado de um Comprador (para embutir em responses).")
data class CompradorResumo(
    val id: Long,
    val nome: String,
    val cpf: String?
)

@Schema(description = "Resumo simplificado de um Especime (para embutir em responses).")
data class EspecimeResumo(
    val id: Long,
    val identificacao: String,
    val status: com.example.backend.entity.StatusEspecime
)
