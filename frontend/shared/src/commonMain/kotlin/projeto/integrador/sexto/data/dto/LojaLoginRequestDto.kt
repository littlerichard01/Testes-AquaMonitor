package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LojaLoginRequestDto(
    val email: String,
    val senha: String
)
