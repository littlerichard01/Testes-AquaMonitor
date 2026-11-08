package projeto.integrador.sexto.ui.screens.venda

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import projeto.integrador.sexto.data.dto.VendaRequestDto
import projeto.integrador.sexto.data.remote.AquaMonitorApi
import projeto.integrador.sexto.data.remote.ResultadoApi
import projeto.integrador.sexto.di.ProvedorDependencias

class VendaFormularioViewModel(
    private val api: AquaMonitorApi = ProvedorDependencias.api
) {

    data class UiState(
        val valor: String = "",
        val observacoes: String = "",
        val compradorId: String = "",
        val especimeId: String = "",
        val aquarioId: String = "",
        val carregando: Boolean = false,
        val erroValor: String? = null,
        val erroCompradorId: String? = null,
        val erroItem: String? = null,
        val erroGeral: String? = null,
        val mensagemSucesso: String? = null
    )

    var uiState by mutableStateOf(UiState())
        private set

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    fun aoMudarValor(valor: String) {
        uiState = uiState.copy(
            valor = valor,
            erroValor = null,
            erroGeral = null,
            mensagemSucesso = null
        )
    }

    fun aoMudarObservacoes(valor: String) {
        uiState = uiState.copy(
            observacoes = valor,
            erroGeral = null,
            mensagemSucesso = null
        )
    }

    fun aoMudarCompradorId(valor: String) {
        uiState = uiState.copy(
            compradorId = valor.filter(Char::isDigit),
            erroCompradorId = null,
            erroGeral = null,
            mensagemSucesso = null
        )
    }

    fun aoMudarEspecimeId(valor: String) {
        val digitos = valor.filter(Char::isDigit)
        uiState = uiState.copy(
            especimeId = digitos,
            aquarioId = if (digitos.isNotEmpty()) "" else uiState.aquarioId,
            erroItem = null,
            erroGeral = null,
            mensagemSucesso = null
        )
    }

    fun aoMudarAquarioId(valor: String) {
        val digitos = valor.filter(Char::isDigit)
        uiState = uiState.copy(
            aquarioId = digitos,
            especimeId = if (digitos.isNotEmpty()) "" else uiState.especimeId,
            erroItem = null,
            erroGeral = null,
            mensagemSucesso = null
        )
    }

    fun limparCampos() {
        uiState = UiState()
    }

    fun gravar() {
        if (uiState.carregando) return

        val valor = uiState.valor.trim().replace(',', '.').toDoubleOrNull()
        val compradorId = uiState.compradorId.toLongOrNull()
        val especimeId = uiState.especimeId.toLongOrNull()
        val aquarioId = uiState.aquarioId.toLongOrNull()

        val erroValor = if (valor == null || !valor.isFinite() || valor <= 0.0) {
            "Informe um valor valido maior que zero."
        } else {
            null
        }
        val erroCompradorId = if (compradorId == null || compradorId <= 0) {
            "Informe um ID de comprador valido."
        } else {
            null
        }
        val itensInformados = listOfNotNull(especimeId, aquarioId).size
        val erroItem = when {
            itensInformados != 1 ->
                "Informe o ID de um especime ou de um aquario, mas nao os dois."
            especimeId != null && especimeId <= 0 ->
                "Informe um ID de especime valido."
            aquarioId != null && aquarioId <= 0 ->
                "Informe um ID de aquario valido."
            else -> null
        }

        if (erroValor != null || erroCompradorId != null || erroItem != null) {
            uiState = uiState.copy(
                erroValor = erroValor,
                erroCompradorId = erroCompradorId,
                erroItem = erroItem
            )
            return
        }

        scope.launch {
            uiState = uiState.copy(
                carregando = true,
                erroGeral = null,
                mensagemSucesso = null
            )

            val request = VendaRequestDto(
                valor = valor!!,
                observacoes = uiState.observacoes.trim().ifBlank { null },
                compradorId = compradorId!!,
                especimeId = especimeId,
                aquarioId = aquarioId
            )

            when (val resultado = api.criarVenda(request)) {
                is ResultadoApi.Sucesso -> {
                    uiState = UiState(
                        mensagemSucesso = "Venda registrada com sucesso!"
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