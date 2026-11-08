package projeto.integrador.sexto.ui.screens.programacaoAlimentacao

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import projeto.integrador.sexto.data.dto.ProgramacaoAlimentacaoRequestDto
import projeto.integrador.sexto.data.remote.AquaMonitorApi
import projeto.integrador.sexto.data.remote.ResultadoApi
import projeto.integrador.sexto.di.ProvedorDependencias

class ProgramacaoAlimentacaoFormularioViewModel(
    private val api: AquaMonitorApi = ProvedorDependencias.api
) {

    data class UiState(
        val horario: String = "",
        val descricao: String = "",
        val aquarioId: String = "",
        val ativo: Boolean = true,
        val carregando: Boolean = false,
        val erroHorario: String? = null,
        val erroAquarioId: String? = null,
        val erroGeral: String? = null,
        val mensagemSucesso: String? = null
    )

    var uiState by mutableStateOf(UiState())
        private set

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    fun aoMudarHorario(valor: String) {
        val digitos = valor.filter(Char::isDigit).take(4)
        val horarioFormatado = if (digitos.length > 2) {
            "${digitos.take(2)}:${digitos.drop(2)}"
        } else {
            digitos
        }
        uiState = uiState.copy(
            horario = horarioFormatado,
            erroHorario = null,
            erroGeral = null,
            mensagemSucesso = null
        )
    }

    fun aoMudarDescricao(valor: String) {
        uiState = uiState.copy(
            descricao = valor,
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

    fun aoMudarAtivo(valor: Boolean) {
        uiState = uiState.copy(
            ativo = valor,
            erroGeral = null,
            mensagemSucesso = null
        )
    }

    fun limparCampos() {
        uiState = UiState()
    }

    fun gravar() {
        if (uiState.carregando) return

        val horario = uiState.horario.trim()
        val aquarioId = uiState.aquarioId.trim().toLongOrNull()
        val erroHorario = if (horario.matches(Regex("^([01]\\d|2[0-3]):[0-5]\\d$"))) {
            null
        } else {
            "Informe um horario valido no formato HH:mm."
        }
        val erroAquarioId = if (aquarioId == null || aquarioId <= 0) {
            "Informe um ID de aquario valido."
        } else {
            null
        }

        if (erroHorario != null || erroAquarioId != null) {
            uiState = uiState.copy(
                erroHorario = erroHorario,
                erroAquarioId = erroAquarioId
            )
            return
        }

        scope.launch {
            uiState = uiState.copy(
                carregando = true,
                erroGeral = null,
                mensagemSucesso = null
            )

            val request = ProgramacaoAlimentacaoRequestDto(
                horario = horario,
                descricao = uiState.descricao.trim().ifBlank { null },
                ativo = uiState.ativo,
                aquarioId = aquarioId!!
            )

            when (val resultado = api.criarProgramacaoAlimentacao(request)) {
                is ResultadoApi.Sucesso -> {
                    uiState = UiState(
                        mensagemSucesso = "Programacao de alimentacao cadastrada com sucesso!"
                    )
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