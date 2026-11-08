package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable

data class DispositivoIoTRequestDto(

    val nome: String,

    val identificacao: String,

    val mqttTopic: String,

    val status: String? = "OFFLINE",

    val aquarioId: Long? = null

)