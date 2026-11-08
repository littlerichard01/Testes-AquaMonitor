package com.example.backend.dto

import com.example.backend.entity.StatusAquario
import com.example.backend.entity.TipoAquario
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

@Schema(description = "Requisicao para criar ou atualizar um Aquario.")
data class AquarioRequest(
    @field:NotBlank(message = "Nome do aquario e obrigatorio")
    @Schema(description = "Nome identificador do aquario", example = "Aquario 01")
    val nome: String,

    @Schema(description = "Descricao opcional do aquario", nullable = true, example = "Aquario comunitario de agua doce")
    val descricao: String? = null,

    @field:Positive(message = "Volume deve ser positivo")
    @Schema(description = "Capacidade em litros", nullable = true, example = "120.00")
    val volumeLitros: BigDecimal? = null,

    @field:NotNull(message = "Tipo do aquario e obrigatorio")
    @Schema(description = "Tipo de agua", example = "AGUA_DOCE")
    val tipo: TipoAquario,

    @Schema(description = "Localizacao fisica na loja", nullable = true, example = "Corredor 3")
    val localizacao: String? = null,

    @Schema(description = "Status atual do aquario. Default: ATIVO", example = "ATIVO", nullable = true)
    val status: StatusAquario? = StatusAquario.ATIVO,

    @field:Positive(message = "Preco deve ser positivo")
    @Schema(description = "Preco de venda do aquario (se aplicavel)", nullable = true, example = "899.90")
    val preco: BigDecimal? = null
)
