package projeto.integrador.sexto.ui.screens.dispositivoiot

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import projeto.integrador.sexto.data.dto.DispositivoIoTRequestDto
import projeto.integrador.sexto.data.remote.AquaMonitorApi
import projeto.integrador.sexto.data.remote.ResultadoApi
import projeto.integrador.sexto.di.ProvedorDependencias

class DispositivoIoTFormularioViewModel(
    private val api: AquaMonitorApi = ProvedorDependencias.api
) {

    data class UiState(
        val nome: String = "",
        val identificacao: String = "",
        val mqttTopic: String = "",
        val status: String = "OFFLINE",
        val carregando: Boolean = false,
        val erroNome: String? = null,
        val erroIdentificacao: String? = null,
        val erroMqttTopic: String? = null,
        val erroStatus: String? = null,
        val erroGeral: String? = null,
        val mensagemSucesso: String? = null
    )

    var uiState by mutableStateOf(UiState())
        private set

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    fun aoMudarNome(valor: String) {
        uiState = uiState.copy(nome = valor, erroNome = null, erroGeral = null, mensagemSucesso = null)
    }

    fun aoMudarIdentificacao(valor: String) {
        uiState = uiState.copy(identificacao = valor, erroIdentificacao = null, erroGeral = null, mensagemSucesso = null)
    }

    fun aoMudarMqttTopic(valor: String) {
        uiState = uiState.copy(mqttTopic = valor, erroMqttTopic = null, erroGeral = null, mensagemSucesso = null)
    }

    fun aoMudarStatus(valor: String) {
        uiState = uiState.copy(status = valor, erroStatus = null, erroGeral = null, mensagemSucesso = null)
    }

    fun limparCampos() {
        uiState = UiState()
    }

    fun gravar() {
        if (uiState.carregando) return

        val erros = mutableMapOf<String, String>()

        if (uiState.nome.isBlank()) {
            erros["nome"] = "Informe o nome do dispositivo."
        }

        if (uiState.identificacao.isBlank()) {
            erros["identificacao"] = "Informe a identificação do dispositivo."
        }

        if (uiState.mqttTopic.isBlank()) {
            erros["mqttTopic"] = "Informe o tópico MQTT do dispositivo."
        }

        if (erros.isNotEmpty()) {
            uiState = uiState.copy(
                erroNome = erros["nome"],
                erroIdentificacao = erros["identificacao"],
                erroMqttTopic = erros["mqttTopic"]
            )
            return
        }

        scope.launch {
            uiState = uiState.copy(
                carregando = true,
                erroGeral = null,
                mensagemSucesso = null
            )

            val request = DispositivoIoTRequestDto(
                nome = uiState.nome.trim(),
                identificacao = uiState.identificacao.trim(),
                mqttTopic = uiState.mqttTopic.trim(),
                status = uiState.status
            )

            when (val resultado = api.criarDispositivoIoT(request)) {
                is ResultadoApi.Sucesso -> {
                    uiState = uiState.copy(
                        carregando = false,
                        mensagemSucesso = "Dispositivo IoT cadastrado com sucesso!"
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