package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class VendaResponseDto(
    val id: Long,
    val valor: Double,
    val dataVenda: String,
    val observacoes: String? = null,
    val lojaId: Long,
    val comprador: CompradorResumoDto,
    val especime: EspecimeResumoDto? = null,
    val aquario: AquarioResumoDto? = null,
    val criadoEm: String,
    val atualizadoEm: String
)