package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class EspecimeResumoDto(
    val id: Long,
    val identificacao: String,
    val status: String
)