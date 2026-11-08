package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CompradorResponseDto(
    val id: Long,
    val nome: String,
    val cpf: String? = null,
    val email: String? = null,
    val telefone: String? = null,
    val observacoes: String? = null,
    val criadoEm: String,
    val atualizadoEm: String
)