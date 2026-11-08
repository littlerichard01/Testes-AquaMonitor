package projeto.integrador.sexto.ui.screens.especie

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import projeto.integrador.sexto.data.dto.EspecieRequestDto
import projeto.integrador.sexto.data.remote.AquaMonitorApi
import projeto.integrador.sexto.data.remote.ResultadoApi
import projeto.integrador.sexto.di.ProvedorDependencias

class EspecieFormularioViewModel(
    private val api: AquaMonitorApi = ProvedorDependencias.api
) {

    data class UiState(
        val nomePopular: String = "",
        val nomeCientifico: String = "",
        val descricao: String = "",
        val temperaturaMinima: String = "",
        val temperaturaMaxima: String = "",
        val tipo: String = "",
        val carregando: Boolean = false,
        val erroNomePopular: String? = null,
        val erroNomeCientifico: String? = null,
        val erroDescricao: String? = null,
        val erroTemperaturaMinima: String? = null,
        val erroTemperaturaMaxima: String? = null,
        val erroTipo: String? = null,
        val erroGeral: String? = null,
        val mensagemSucesso: String? = null
    )

    var uiState by mutableStateOf(UiState())
        private set

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    fun aoMudarNomePopular(valor: String) {
        uiState = uiState.copy(nomePopular = valor, erroNomePopular = null, erroGeral = null, mensagemSucesso = null)
    }

    fun aoMudarNomeCientifico(valor: String) {
        uiState = uiState.copy(nomeCientifico = valor, erroNomeCientifico = null, erroGeral = null, mensagemSucesso = null)
    }

    fun aoMudarDescricao(valor: String) {
        uiState = uiState.copy(descricao = valor, erroDescricao = null, erroGeral = null, mensagemSucesso = null)
    }

    fun aoMudarTemperaturaMinima(valor: String) {
        uiState = uiState.copy(temperaturaMinima = valor, erroTemperaturaMinima = null, erroGeral = null, mensagemSucesso = null)
    }

    fun aoMudarTemperaturaMaxima(valor: String) {
        uiState = uiState.copy(temperaturaMaxima = valor, erroTemperaturaMaxima = null, erroGeral = null, mensagemSucesso = null)
    }

    fun aoMudarTipo(valor: String) {
        uiState = uiState.copy(tipo = valor, erroTipo = null, erroGeral = null, mensagemSucesso = null)
    }

    fun limparCampos() {
        uiState = UiState()
    }

    fun gravar() {
        if (uiState.carregando) return

        val erros = mutableMapOf<String, String>()

        if (uiState.nomePopular.isBlank()) {
            erros["nomePopular"] = "Informe o nome popular da espécie."
        }

        if (uiState.nomeCientifico.isBlank()) {
            erros["nomeCientifico"] = "Informe o nome científico da espécie."
        }

        if (uiState.tipo.isBlank()) {
            erros["tipo"] = "Selecione o tipo da espécie."
        }

        val temperaturaMinima = uiState.temperaturaMinima
            .takeIf { it.isNotBlank() }
            ?.replace(',', '.')
            ?.toDoubleOrNull()
        if (uiState.temperaturaMinima.isNotBlank() && temperaturaMinima == null) {
            erros["temperaturaMinima"] = "Informe uma temperatura mínima válida."
        }

        val temperaturaMaxima = uiState.temperaturaMaxima
            .takeIf { it.isNotBlank() }
            ?.replace(',', '.')
            ?.toDoubleOrNull()
        if (uiState.temperaturaMaxima.isNotBlank() && temperaturaMaxima == null) {
            erros["temperaturaMaxima"] = "Informe uma temperatura máxima válida."
        }

        if (temperaturaMinima != null && temperaturaMaxima != null && temperaturaMinima > temperaturaMaxima) {
            erros["temperaturaMaxima"] = "A temperatura máxima deve ser maior ou igual à mínima."
        }

        if (erros.isNotEmpty()) {
            uiState = uiState.copy(
                erroNomePopular = erros["nomePopular"],
                erroNomeCientifico = erros["nomeCientifico"],
                erroTipo = erros["tipo"],
                erroTemperaturaMinima = erros["temperaturaMinima"],
                erroTemperaturaMaxima = erros["temperaturaMaxima"]
            )
            return
        }

        scope.launch {
            uiState = uiState.copy(
                carregando = true,
                erroGeral = null,
                mensagemSucesso = null
            )

            val request = EspecieRequestDto(
                nomePopular = uiState.nomePopular.trim(),
                nomeCientifico = uiState.nomeCientifico.trim(),
                tipo = uiState.tipo,
                descricao = uiState.descricao.trim().takeIf { it.isNotEmpty() },
                temperaturaMinima = temperaturaMinima,
                temperaturaMaxima = temperaturaMaxima
            )

            when (val resultado = api.criarEspecie(request)) {
                is ResultadoApi.Sucesso -> {
                    uiState = uiState.copy(
                        carregando = false,
                        mensagemSucesso = "Espécie cadastrada com sucesso!"
                    )
                    limparCampos()
                }

                is ResultadoApi.Falha -> {
                    uiState = uiState.copy(
                        carregando = false,
                        erroGeral = resultado.mensagem
                    )
                }
            }
        }
    }

    fun onCleared() {
        scope.cancel()
    }
}