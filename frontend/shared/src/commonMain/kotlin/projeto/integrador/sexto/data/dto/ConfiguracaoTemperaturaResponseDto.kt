package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ConfiguracaoTemperaturaResponseDto(
    val id: Long,
    val temperaturaMinima: Double,
    val temperaturaMaxima: Double,
    val ativo: Boolean,
    val aquario: AquarioResumoDto,
    val criadoEm: String,
    val atualizadoEm: String
)