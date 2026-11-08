package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class DispositivoIoTResumoDto(
    val id: Long,
    val nome: String,
    val identificacao: String,
    val status: String
)