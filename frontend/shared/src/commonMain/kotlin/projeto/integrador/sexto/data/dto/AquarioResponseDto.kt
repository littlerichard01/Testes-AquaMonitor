package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AquarioResponseDto(
    val id: Long,
    val nome: String,
    val descricao: String? = null,
    val volumeLitros: Double? = null,
    val tipo: String,
    val localizacao: String? = null,
    val status: String,
    val preco: Double? = null,
    val lojaId: Long,
    val criadoEm: String,
    val atualizadoEm: String
)
