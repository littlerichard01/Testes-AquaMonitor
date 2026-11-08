package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AquarioResumoDto(
    val id: Long,
    val nome: String,
    val status: String
)