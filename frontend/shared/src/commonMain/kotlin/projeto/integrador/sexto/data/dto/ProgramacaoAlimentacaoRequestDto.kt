package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProgramacaoAlimentacaoRequestDto(
    val horario: String,
    val descricao: String? = null,
    val ativo: Boolean? = true,
    val aquarioId: Long
)