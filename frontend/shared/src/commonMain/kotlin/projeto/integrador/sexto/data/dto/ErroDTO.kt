package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErroDto(
    val mensagem: String? = null,
    val codigo: String? = null,
    val erros: Map<String, String>? = null
)
