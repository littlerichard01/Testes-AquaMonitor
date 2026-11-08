package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AlertaRequestDto(
    val tipo: String,
    val severidade: String,
    val ativo: Boolean? = true,
    val aquarioId: Long,
)
