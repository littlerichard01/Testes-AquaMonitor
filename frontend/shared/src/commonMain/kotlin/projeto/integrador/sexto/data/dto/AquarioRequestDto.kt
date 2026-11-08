package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AquarioRequestDto(
    val nome: String,
    val descricao: String? = null,
    val volumeLitros: Double? = null,
    val tipo: String,
    val localizacao: String? = null,
    val status: String? = "ATIVO",
    val preco: Double? = null
)
