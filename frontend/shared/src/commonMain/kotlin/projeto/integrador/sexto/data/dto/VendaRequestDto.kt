package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class VendaRequestDto(
    val valor: Double,
    val dataVenda: String? = null,
    val observacoes: String? = null,
    val compradorId: Long,
    val especimeId: Long? = null,
    val aquarioId: Long? = null
)