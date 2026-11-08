package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class EspecimeResponseDto(
    val id: Long,
    val identificacao: String,
    val preco: Double,
    val status: String,
    val observacoes: String? = null,
    val dataEntrada: String,
    val especie: EspecieResumoDto,
    val aquario: AquarioResumoDto? = null,
    val lojaId: Long,
    val criadoEm: String,
    val atualizadoEm: String,
)
