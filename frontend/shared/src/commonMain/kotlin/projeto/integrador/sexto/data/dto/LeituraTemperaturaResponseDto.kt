package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LeituraTemperaturaResponseDto(
    val id: Long,
    val temperatura: Double,
    val dataHora: String,
    val aquario: AquarioResumoDto,
    val dispositivoIoT: DispositivoIoTResumoDto
)