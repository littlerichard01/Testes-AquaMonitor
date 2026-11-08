package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LojaRequestDto(
    val nome: String,
    val email: String,
    val senha: String,
    val ativo: Boolean = true
)
