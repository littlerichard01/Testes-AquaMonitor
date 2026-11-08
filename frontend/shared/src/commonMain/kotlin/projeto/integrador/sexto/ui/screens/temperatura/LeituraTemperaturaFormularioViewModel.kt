package projeto.integrador.sexto.ui.screens.leituratemperatura

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import projeto.integrador.sexto.data.dto.LeituraTemperaturaRequestDto
import projeto.integrador.sexto.data.remote.AquaMonitorApi
import projeto.integrador.sexto.data.remote.ResultadoApi
import projeto.integrador.sexto.di.ProvedorDependencias

class LeituraTemperaturaFormularioViewModel(
    private val api: AquaMonitorApi = ProvedorDependencias.api
) {

    data class UiState(
        val temperatura: String = "",
        val aquarioId: String = "",
        val dispositivoIoTId: String = "",
        val carregando: Boolean = false,
        val erroTemperatura: String? = null,
        val erroAquarioId: String? = null,
        val erroDispositivoIoTId: String? = null,
        val erroGeral: String? = null,
        val mensagemSucesso: String? = null
    )

    var uiState by mutableStateOf(UiState())
        private set

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    fun aoMudarTemperatura(valor: String) {
        uiState = uiState.copy(temperatura = valor, erroTemperatura = null, erroGeral = null, mensagemSucesso = null)
    }

    fun aoMudarAquarioId(valor: String) {
        uiState = uiState.copy(aquarioId = valor, erroAquarioId = null, erroGeral = null, mensagemSucesso = null)
    }

    fun aoMudarDispositivoIoTId(valor: String) {
        uiState = uiState.copy(
            dispositivoIoTId = valor,
            erroDispositivoIoTId = null,
            erroGeral = null,
            mensagemSucesso = null
        )
    }

    fun limparCampos() {
        uiState = UiState()
    }

    fun gravar() {
        if (uiState.carregando) return

        val erros = mutableMapOf<String, String>()

        val tempDouble = uiState.temperatura.replace(',', '.').toDoubleOrNull()
        if (tempDouble == null) {
            erros["temperatura"] = "Informe uma temperatura medida válida."
        }

        val aqIdLong = uiState.aquarioId.toLongOrNull()
        if (aqIdLong == null || aqIdLong <= 0) {
            erros["aquarioId"] = "Informe um ID de aquário válido."
        }

        val dispIdLong = uiState.dispositivoIoTId.toLongOrNull()
        if (dispIdLong == null || dispIdLong <= 0) {
            erros["dispositivoIoTId"] = "Informe um ID de dispositivo válido."
        }

        if (erros.isNotEmpty()) {
            uiState = uiState.copy(
                erroTemperatura = erros["temperatura"],
                erroAquarioId = erros["aquarioId"],
                erroDispositivoIoTId = erros["dispositivoIoTId"]
            )
            return
        }

        scope.launch {
            uiState = uiState.copy(
                carregando = true,
                erroGeral = null,
                mensagemSucesso = null
            )

            val request = LeituraTemperaturaRequestDto(
                temperatura = tempDouble!!,
                aquarioId = aqIdLong!!,
                dispositivoIoTId = dispIdLong!!,
                dataHora = null
            )

            when (val resultado = api.criarLeituraTemperatura(request)) {
                is ResultadoApi.Sucesso -> {
                    uiState = uiState.copy(
                        carregando = false,
                        mensagemSucesso = "Leitura enviada com sucesso!"
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