package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CompradorResumoDto(
    val id: Long,
    val nome: String,
    val cpf: String? = null
)