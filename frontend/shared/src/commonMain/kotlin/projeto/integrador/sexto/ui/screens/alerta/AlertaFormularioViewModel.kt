package projeto.integrador.sexto.ui.screens.alerta

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import projeto.integrador.sexto.data.dto.AlertaRequestDto
import projeto.integrador.sexto.data.remote.AquaMonitorApi
import projeto.integrador.sexto.data.remote.ResultadoApi
import projeto.integrador.sexto.di.ProvedorDependencias

class AlertaFormularioViewModel(
    private val api: AquaMonitorApi = ProvedorDependencias.api
) {

    data class UiState(
        val tipo: String = "TEMPERATURA",
        val severidade: String = "MEDIA",
        val ativo: Boolean = true,
        val aquarioId: String = "",
        val carregando: Boolean = false,
        val erroTipo: String? = null,
        val erroSeveridade: String? = null,
        val erroAquarioId: String? = null,
        val erroGeral: String? = null,
        val mensagemSucesso: String? = null
    )

    var uiState by mutableStateOf(UiState())
        private set

    private val scope =
        CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    fun aoMudarTipo(valor: String) {
        uiState = uiState.copy(
            tipo = valor,
            erroTipo = null,
            erroGeral = null,
            mensagemSucesso = null
        )
    }

    fun aoMudarSeveridade(valor: String) {
        uiState = uiState.copy(
            severidade = valor,
            erroSeveridade = null,
            erroGeral = null,
            mensagemSucesso = null
        )
    }

    fun aoMudarAtivo(valor: Boolean) {
        uiState = uiState.copy(
            ativo = valor,
            erroGeral = null,
            mensagemSucesso = null
        )
    }

    fun aoMudarAquarioId(valor: String) {
        uiState = uiState.copy(
            aquarioId = valor,
            erroAquarioId = null,
            erroGeral = null,
            mensagemSucesso = null
        )
    }

    fun limparCampos() {
        if (uiState.carregando) return
        uiState = UiState()
    }

    fun gravar() {
        if (uiState.carregando) return

        val dados = uiState
        val tipo = dados.tipo.trim().uppercase()
        val severidade = dados.severidade.trim().uppercase()
        val aquarioId = dados.aquarioId.trim().toLongOrNull()

        val erroTipo =
            if (tipo !in listOf("TEMPERATURA", "ALIMENTACAO")) {
                "Selecione um tipo valido."
            } else {
                null
            }

        val erroSeveridade =
            if (severidade !in listOf("BAIXA", "MEDIA", "ALTA")) {
                "Selecione uma severidade valida."
            } else {
                null
            }

        val erroAquarioId =
            if (aquarioId == null || aquarioId <= 0L) {
                "Informe um ID de aquario maior que zero."
            } else {
                null
            }

        if (
            erroTipo != null ||
            erroSeveridade != null ||
            erroAquarioId != null
        ) {
            uiState = uiState.copy(
                erroTipo = erroTipo,
                erroSeveridade = erroSeveridade,
                erroAquarioId = erroAquarioId,
                erroGeral = null,
                mensagemSucesso = null
            )
            return
        }

        val request = AlertaRequestDto(
            tipo = tipo,
            severidade = severidade,
            ativo = dados.ativo,
            aquarioId = aquarioId!!
        )

        uiState = uiState.copy(
            carregando = true,
            erroTipo = null,
            erroSeveridade = null,
            erroAquarioId = null,
            erroGeral = null,
            mensagemSucesso = null
        )

        scope.launch {
            try {
                when (val resultado = api.criarAlerta(request)) {
                    is ResultadoApi.Sucesso -> {
                        uiState = UiState(
                            mensagemSucesso =
                                "Alerta cadastrado com sucesso! " +
                                        "ID: ${resultado.dados.id}"
                        )
                    }

                    is ResultadoApi.Falha -> {
                        uiState = uiState.copy(
                            carregando = false,
                            erroGeral = resultado.mensagem
                        )
                    }
                }
            } finally {
                uiState = uiState.copy(carregando = false)
            }
        }
    }

    fun onCleared() {
        scope.cancel()
    }
}