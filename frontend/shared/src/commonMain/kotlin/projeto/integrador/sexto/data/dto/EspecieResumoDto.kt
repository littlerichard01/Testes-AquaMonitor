package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class EspecieResumoDto(
    val id: Long,
    val nomePopular: String,
    val tipo: String
)