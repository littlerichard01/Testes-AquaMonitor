package projeto.integrador.sexto.ui.screens.configuracaotemperatura

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import projeto.integrador.sexto.data.dto.ConfiguracaoTemperaturaRequestDto
import projeto.integrador.sexto.data.remote.AquaMonitorApi
import projeto.integrador.sexto.data.remote.ResultadoApi
import projeto.integrador.sexto.di.ProvedorDependencias

class ConfiguracaoTemperaturaFormularioViewModel(
    private val api: AquaMonitorApi = ProvedorDependencias.api
) {

    data class UiState(
        val temperaturaMinima: String = "",
        val temperaturaMaxima: String = "",
        val aquarioId: String = "",
        val ativo: Boolean = true,
        val carregando: Boolean = false,
        val erroTemperaturaMinima: String? = null,
        val erroTemperaturaMaxima: String? = null,
        val erroAquarioId: String? = null,
        val erroGeral: String? = null,
        val mensagemSucesso: String? = null
    )

    var uiState by mutableStateOf(UiState())
        private set

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    fun aoMudarTemperaturaMinima(valor: String) {
        uiState = uiState.copy(
            temperaturaMinima = valor,
            erroTemperaturaMinima = null,
            erroGeral = null,
            mensagemSucesso = null
        )
    }

    fun aoMudarTemperaturaMaxima(valor: String) {
        uiState = uiState.copy(
            temperaturaMaxima = valor,
            erroTemperaturaMaxima = null,
            erroGeral = null,
            mensagemSucesso = null
        )
    }

    fun aoMudarAquarioId(valor: String) {
        uiState = uiState.copy(aquarioId = valor, erroAquarioId = null, erroGeral = null, mensagemSucesso = null)
    }

    fun aoMudarAtivo(novoValor: Boolean) {
        uiState = uiState.copy(ativo = novoValor, erroGeral = null, mensagemSucesso = null)
    }

    fun limparCampos() {
        uiState = UiState()
    }

    fun gravar() {
        if (uiState.carregando) return

        val erros = mutableMapOf<String, String>()

        val minDouble = uiState.temperaturaMinima.replace(',', '.').toDoubleOrNull()
        if (minDouble == null) {
            erros["temperaturaMinima"] = "Informe uma temperatura mínima válida."
        }

        val maxDouble = uiState.temperaturaMaxima.replace(',', '.').toDoubleOrNull()
        if (maxDouble == null) {
            erros["temperaturaMaxima"] = "Informe uma temperatura máxima válida."
        }

        val aqIdLong = uiState.aquarioId.toLongOrNull()
        if (aqIdLong == null || aqIdLong <= 0) {
            erros["aquarioId"] = "Informe um ID de aquário válido."
        }

        if (minDouble != null && maxDouble != null && minDouble > maxDouble) {
            uiState = uiState.copy(erroGeral = "A temperatura mínima não pode ser maior que a máxima.")
            return
        }

        if (erros.isNotEmpty()) {
            uiState = uiState.copy(
                erroTemperaturaMinima = erros["temperaturaMinima"],
                erroTemperaturaMaxima = erros["temperaturaMaxima"],
                erroAquarioId = erros["aquarioId"]
            )
            return
        }

        scope.launch {
            uiState = uiState.copy(
                carregando = true,
                erroGeral = null,
                mensagemSucesso = null
            )

            val request = ConfiguracaoTemperaturaRequestDto(
                temperaturaMinima = minDouble!!,
                temperaturaMaxima = maxDouble!!,
                aquarioId = aqIdLong!!,
                ativo = uiState.ativo
            )

            when (val resultado = api.criarConfiguracaoTemperatura(request)) {
                is ResultadoApi.Sucesso -> {
                    uiState = uiState.copy(
                        carregando = false,
                        mensagemSucesso = "Configuração salva com sucesso!"
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