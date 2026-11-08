package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LojaResponseDto(
    val id: Long,
    val nome: String,
    val email: String,
    val ativo: Boolean,
    val criadoEm: String,
    val atualizadoEm: String
)
