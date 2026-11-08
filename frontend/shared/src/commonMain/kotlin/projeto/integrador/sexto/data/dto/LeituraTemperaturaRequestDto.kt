package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LeituraTemperaturaRequestDto(
    val temperatura: Double,
    val dataHora: String? = null,
    val aquarioId: Long,
    val dispositivoIoTId: Long
)
