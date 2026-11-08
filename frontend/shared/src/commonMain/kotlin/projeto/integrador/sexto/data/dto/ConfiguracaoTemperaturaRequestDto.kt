package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ConfiguracaoTemperaturaRequestDto(
    val temperaturaMinima: Double,
    val temperaturaMaxima: Double,
    val ativo: Boolean? = true,
    val aquarioId: Long
)