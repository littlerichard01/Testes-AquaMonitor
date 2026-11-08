package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CompradorRequestDto(
    val nome: String,
    val cpf: String? = null,
    val email: String? = null,
    val telefone: String? = null,
    val observacoes: String? = null
)