package com.example.backend.dto

import com.example.backend.entity.StatusAquario
import com.example.backend.entity.TipoAquario
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDateTime

@Schema(description = "Resposta contendo os dados de um Aquario.")
data class AquarioResponse(
    @Schema(description = "Identificador unico do aquario", example = "1")
    val id: Long,

    @Schema(description = "Nome identificador", example = "Aquario 01")
    val nome: String,

    @Schema(description = "Descricao opcional", nullable = true, example = "Aquario comunitario de agua doce")
    val descricao: String?,

    @Schema(description = "Volume em litros", nullable = true, example = "120.00")
    val volumeLitros: BigDecimal?,

    @Schema(description = "Tipo de agua", example = "AGUA_DOCE")
    val tipo: TipoAquario,

    @Schema(description = "Localizacao na loja", nullable = true, example = "Corredor 3")
    val localizacao: String?,

    @Schema(description = "Status: ATIVO / INATIVO / VENDIDO", example = "ATIVO")
    val status: StatusAquario,

    @Schema(description = "Preco de venda (se aplicavel)", nullable = true, example = "899.90")
    val preco: BigDecimal?,

    @Schema(description = "ID da Loja dona", example = "1")
    val lojaId: Long,

    @Schema(description = "Data e hora do cadastro")
    val criadoEm: LocalDateTime,

    @Schema(description = "Data e hora da ultima atualizacao")
    val atualizadoEm: LocalDateTime
)