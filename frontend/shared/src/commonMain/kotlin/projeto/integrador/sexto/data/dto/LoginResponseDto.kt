package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    val token: String,
    val tipo: String = "Bearer",
    val lojaId: Long,
    val nomeLoja: String,
    val emailLoja: String
)
