package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProgramacaoAlimentacaoResponseDto(
    val id: Long,
    val horario: String,
    val descricao: String? = null,
    val ativo: Boolean,
    val aquario: AquarioResumoDto,
    val criadoEm: String,
    val atualizadoEm: String
)