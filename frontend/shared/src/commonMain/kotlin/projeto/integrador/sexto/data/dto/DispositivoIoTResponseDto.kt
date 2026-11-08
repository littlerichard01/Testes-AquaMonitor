package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable

data class DispositivoIoTResponseDto(

    val id: Long,

    val nome: String,

    val identificacao: String,

    val mqttTopic: String,

    val status: String,

    val ultimaComunicacao: String?,

    val aquario: AquarioResumoDto?,

    val criadoEm: String,

    val atualizadoEm: String

)