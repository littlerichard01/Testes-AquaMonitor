package projeto.integrador.sexto.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class HomeViewModel(
    val nomeUsuario: String
) {

    data class UiState(
        val saudacao: String = "",
        val carregando: Boolean = true
    )

    var uiState by mutableStateOf(UiState())
        private set

    init {
        carregarDados()
    }

    private fun carregarDados() {
        uiState = uiState.copy(carregando = true)

        val saudacao = "Ola, $nomeUsuario!"

        uiState = uiState.copy(
            saudacao = saudacao,
            carregando = false
        )
    }

    fun pedirParaSair() {
    }

    fun abrirEntidade(entidade: String) {
    }
}