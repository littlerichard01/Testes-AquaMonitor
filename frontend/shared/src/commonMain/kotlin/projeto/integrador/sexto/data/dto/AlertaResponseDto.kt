package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AlertaResponseDto(
    val id: Long,
    val tipo: String,
    val severidade: String,
    val ativo: Boolean,
    val aquario: AquarioResumoDto,
    val criadoEm: String,
    val atualizadoEm: String
)
