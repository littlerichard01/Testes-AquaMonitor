package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class EspecimeRequestDto(
    val identificacao: String,
    val preco: Double,
    val status: String? = "DISPONIVEL",
    val observacoes: String? = null,
    val dataEntrada: String? = null,
    val especieId: Long,
    val aquarioId: Long? = null
)
